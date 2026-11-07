package org.firstinspires.ftc.teamcode.subsystems.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.calculators.AngleCalculator;
import org.firstinspires.ftc.teamcode.controllers.TurnPID;

public class Drivetrain {
    private final LinearOpMode opMode;
    private final ControlHubIMU imu;

    public DcMotor leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;

    private final ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 4.1;
    // How many ticks will the motor advance per inch?
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    public Drivetrain(LinearOpMode opMode, ControlHubIMU imu) {
        this.opMode = opMode;
        this.imu = imu;
    }

    private DcMotor getInitializedDriveMotor(HardwareMap hardwareMap, String deviceName, DcMotorSimple.Direction motorDirection) {
        DcMotor motor = hardwareMap.get(DcMotor.class, deviceName);
        motor.setDirection(motorDirection);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }

    public void init(HardwareMap hardwareMap) {
        // TODO - consider consistent naming for motors (e.g. leftFrontDrive & leftBackDrive)
        leftFrontDrive = getInitializedDriveMotor(hardwareMap, "left_drive", DcMotorSimple.Direction.FORWARD);
        leftBackDrive = getInitializedDriveMotor(hardwareMap, "leftBackDrive", DcMotorSimple.Direction.FORWARD);
        rightFrontDrive = getInitializedDriveMotor(hardwareMap, "right_drive", DcMotorSimple.Direction.REVERSE);
        rightBackDrive = getInitializedDriveMotor(hardwareMap, "rightBackDrive", DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Advance a given motor the desired number of inches
     * @return The motor's position (in ticks) after the motor advances
     */
    private int getMotorPositionAfterAdvancingInches(DcMotor motor, double inches) {
        return motor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
    }

    private void recordTelemetryForFrontWheelActualAndTargets(double leftFrontTarget, double rightFrontTarget) {
        opMode.telemetry.addData("leftFrontTargetTicks", leftFrontTarget);
        opMode.telemetry.addData("leftFrontTicks", leftFrontDrive.getCurrentPosition());
        opMode.telemetry.addData("rightFrontTargetTicks", rightFrontTarget);
        opMode.telemetry.addData("rightFrontTicks", rightFrontDrive.getCurrentPosition());
        opMode.telemetry.update();
    }

    /**
     * Check if the drive motors are still working and the timeout is not exceeded.
     * This is useful for waiting for all motors to complete a given task
     * @param timeoutInSeconds How long before timing out the target checks
     * @return True if the motors are still working; false if they are not or the timeout has been exceeded
     */
    private boolean isDrivingToTargetInches(double runtimeInSeconds, double timeoutInSeconds) {
        boolean anyWheelsBusy = leftFrontDrive.isBusy() ||
                rightFrontDrive.isBusy() ||
                leftBackDrive.isBusy() ||
                rightBackDrive.isBusy();
        return runtimeInSeconds < timeoutInSeconds && anyWheelsBusy;
    }

    /**
     * Set all drive motors to run without the encoder; the encoders are still used
     * for collecting measurements, but the PID controller is used for actual motor speed
     * correction.
     */
    private void setMotorsToRunMode(DcMotor.RunMode runMode) {
        leftFrontDrive.setMode(runMode);
        rightFrontDrive.setMode(runMode);
        leftBackDrive.setMode(runMode);
        rightBackDrive.setMode(runMode);
    }

    /**
     * Set the robot's motor power for each drive motor. This gives control to independently
     * power each motor as desired
     */
    public void setMotorPowers(double leftFrontMotorPower, double leftBackMotorPower, double rightFrontMotorPower, double rightBackMotorPower) {
        leftFrontDrive.setPower(leftFrontMotorPower);
        leftBackDrive.setPower(leftBackMotorPower);
        rightFrontDrive.setPower(rightFrontMotorPower);
        rightBackDrive.setPower(rightBackMotorPower);
    }

    /**
     * Set the robot's motor power to the set left and right motor powers. This is useful
     * for turning; to turn the front/back left motors are set to one power and the
     * front/back right motors are set to another power
     */
    public void setMotorPowerPerSide(double leftMotorPower, double rightMotorPower) {
        setMotorPowers(leftMotorPower, leftMotorPower, rightMotorPower, rightMotorPower);
    }

    private void setMotorTargetPositions(int leftFrontTarget, int leftBackTarget, int rightFrontTarget, int rightBackTarget) {
        leftFrontDrive.setTargetPosition(leftFrontTarget);
        leftBackDrive.setTargetPosition(leftBackTarget);
        rightFrontDrive.setTargetPosition(rightFrontTarget);
        rightBackDrive.setTargetPosition(rightBackTarget);
    }

    /**
     * Drive the robot the set number of inches from its current position. Passing identical
     * left and right inches will drive the robot straight. Passing differing left/right inches
     * will cause the robot to turn
     * @param speed The speed to drive at; this is specified using the -1.0 to 1.0 power scale for the motors
     * @param leftInches How many inches to advance or retreat the left motors
     * @param rightInches How many inches to advance or retreat the right motors
     * @param timeoutInSeconds How long to wait (in seconds) before giving up on the robot making it to
     *                         the expected position.
     */
    public void driveInches(double speed,
                            double leftInches,
                            double rightInches,
                            double timeoutInSeconds) {
        // Determine new target position, and pass to motor controller
        int leftFrontTarget = getMotorPositionAfterAdvancingInches(leftFrontDrive, leftInches);
        int leftBackTarget = getMotorPositionAfterAdvancingInches(leftBackDrive, leftInches);
        int rightFrontTarget = getMotorPositionAfterAdvancingInches(rightFrontDrive, rightInches);
        int rightBackTarget = getMotorPositionAfterAdvancingInches(rightBackDrive, rightInches);
        setMotorTargetPositions(leftFrontTarget, leftBackTarget, rightFrontTarget, rightBackTarget);

        // Turn On RUN_TO_POSITION
        setMotorsToRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Reset the timeout time and start motion.
        runtime.reset();
        double absoluteSpeed = Math.abs(speed);
        setMotorPowerPerSide(absoluteSpeed, absoluteSpeed);
        while (opMode.opModeIsActive() && isDrivingToTargetInches(runtime.seconds(), timeoutInSeconds)) {
            // Display front actual & target front wheel ticks
            recordTelemetryForFrontWheelActualAndTargets(leftFrontTarget, rightFrontTarget);
        }

        // Stop all motion;
        setMotorPowerPerSide(0, 0);

        // Turn off RUN_TO_POSITION
        setMotorsToRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorsToRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Record useful telemetry to observe how the PID controller is applying motor correction
     * to achieve the desired angle
     */
    private void recordPIDTelemetry(double consecutiveTimesWithinDegreeThreshold, double currentAngle, double targetAngle, double pidSlope, double motorPower) {
        opMode.telemetry.addData("consecutiveTimesWithinDegreeThreshold", consecutiveTimesWithinDegreeThreshold);
        opMode.telemetry.addData("currentAngle", currentAngle);
        opMode.telemetry.addData("targetAngle", targetAngle);
        opMode.telemetry.addData("pidCorrectionSlope", pidSlope);
        opMode.telemetry.addData("motorPower", motorPower);
        opMode.telemetry.update();
    }

    /**
     * Determine if the robot is stabilizing the target angle. To do so, compare the
     * error to the acceptable error threshold. Additionally, compare the error slope to the acceptable
     * error slope. The slope comparison ensures the robot is actually stabilizing and not just oscillating
     * through and past the target angle
     * @param error The error between the target angle and the actual angle
     * @param acceptableError The acceptable error between the target angle and the actual angle. This should
     *                        be set to something above zero to avoid constant oscillation, but not so high that
     *                        the robot isn't considerably off the desired heading. This should use the same unit
     *                        as the error
     * @param errorSlope The slope of the error. This is generally measured in degrees per second
     * @param acceptableErrorSlope The acceptable slope of the error. This should use the same unit as the errorSlope
     * @return Is the robot stabilizing at the target angle?
     */
    @SuppressWarnings("SameParameterValue")
    private boolean isStabilizingAtTargetAngle(double error, double acceptableError, double errorSlope, double acceptableErrorSlope) {
        // NOTE: The "SuppressWarnings" block above is in place because right now, we always use an
        // acceptable error of 1 degree and an acceptable error slope of 10 degrees per second.
        // However, leaving this parameterized to support more easily changing those values later
        return Math.abs(error) < acceptableError && Math.abs(errorSlope) < acceptableErrorSlope;
    }

    /**
     * Turn the robot to the given target angle
     * @param targetAngle The desired angle the robot should turn to
     * @param stabilizationTimeoutInSeconds How long to wait (in seconds) before abandoning the turn.
     */
    public void turnToAngle(double targetAngle, double stabilizationTimeoutInSeconds) {
        // TODO; consider setting the motor encoder modes at the OpMode layer instead of within this function
        //  It might be unnecessary to set the motor mode every time we call turnToPID
        setMotorsToRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        TurnPID pid = new TurnPID(targetAngle, 0.004, 0.00000000, 0.001);
        double consecutiveStableMeasurements = 0;
        runtime.reset();

        // Run the while loop until either the timeout is hit or the robot has stabilized at the target angle
        while (runtime.seconds() < stabilizationTimeoutInSeconds) {
            double currentAngle = imu.getRobotYawInDegrees();
            double error = AngleCalculator.getShortestAngleBetween(targetAngle, currentAngle);
            double motorSpeed = pid.update(currentAngle);
            double errorSlope = pid.getLastSlope();

            // If the robot is within the stable threshold, increase the consecutive stable measurement count
            // Otherwise, reset the counter
            if (isStabilizingAtTargetAngle(error, 1, errorSlope, 10)) {
                consecutiveStableMeasurements++;
            } else {
                consecutiveStableMeasurements = 0;
            }
            // Has the robot entered a stable state? If so, exit the loop
            if (consecutiveStableMeasurements >= 4) {
                break;
            }
            // If the robot is not in a stable state, adjust the motor speed to turn the robot
            setMotorPowerPerSide(motorSpeed, -motorSpeed);
            recordPIDTelemetry(consecutiveStableMeasurements, currentAngle, targetAngle, errorSlope, motorSpeed);
        }

        // Stop the turn
        setMotorPowerPerSide(0, 0);
    }
}
