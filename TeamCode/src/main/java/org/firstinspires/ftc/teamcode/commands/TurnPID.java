package org.firstinspires.ftc.teamcode.commands;

import static androidx.core.math.MathUtils.clamp;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.calculators.AngleCalculator;

// Single use per object
public class TurnPID {
    private final double targetAngle;
    private final double kP, kI, kD;
    private final ElapsedTime timer = new ElapsedTime();
    // Last error and time are used to determine error slope; this is useful
    // to understand how much correction the PID controller is supplying at a given
    // point-in-time
    private double lastError = 0;
    private double lastTimeInSeconds = 0;
    private double lastSlope = 0;
    // Integral is used to measure accumulated error values; this is multiplied by kI
    // to become the "I" portion of this PID controller
    private double integral = 0;

    public TurnPID(double targetAngle, double kP, double kI, double kD) {
        this.targetAngle = targetAngle;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Calculate the derivative between the current error and the previous error. The derivative
     * at any point-in-time is just the slope where the y-axis is the difference between current and previous error.
     * The x-axis is the amount of seconds since the previous measurement. This method has a safeguard to
     * return a derivative of zero if no time has passed between measurements or if this is our
     * first measurement
     */
    private double calculateDerivative(
            double currentError,
            double previousError,
            double secondsBetweenMeasurements
    ) {
        return lastTimeInSeconds == 0 || secondsBetweenMeasurements == 0 ?
                0 :
                (currentError - previousError) / secondsBetweenMeasurements;
    }

    public double update(double currentAngle) {
        double currentTimeInSeconds = timer.seconds();
        double secondsSincePreviousMeasurement = currentTimeInSeconds - lastTimeInSeconds;
        double error = AngleCalculator.getShortestAngleBetween(targetAngle, currentAngle);
        // Determine the aggregated integral adjustment and the derivative between the current and previous error
        integral += error * secondsSincePreviousMeasurement;
        double derivative = calculateDerivative(error, lastError, secondsSincePreviousMeasurement);

        // TODO - we might consider using raw voltage instead of the -1 to 1 power scale. This would
        //  avoid our robot slowing down as our battery drains. The -1 to 1 power scale is directly
        //  tied to the total amount of voltage available to the robot.
        // Run the PID algorithm to determine how much power to give the motor
        // This is then constrained by max power to avoid turning too rapidly
        double motorPower = (kP * error + kI * integral + kD * derivative);
        double maxPower = .35;
        motorPower = clamp(motorPower, -maxPower, maxPower);

        // Set the 'last' values as we now have new measurements
        lastSlope = derivative;
        lastError = error;
        lastTimeInSeconds = currentTimeInSeconds;

        return motorPower;
    }

    public double getLastSlope() {
        return lastSlope;
    }
}