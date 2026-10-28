package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Dual Horizontal Flywheel Tester")
public class DualHorizontalFlywheelTester extends LinearOpMode {
    private DcMotorEx leftFlywheel, rightFlywheel;
    // Track the desired velocity of the flywheel; this allows incrementing/decrementing
    // the velocity and also measuring the difference between actual and desired velocity
    private double desiredFlywheelVelocity;
    // Used to avoid incrementing/decrementing too quickly when multiple up/down keypresses
    // are read within a loop
    private final ElapsedTime timeSinceVelocityChange = new ElapsedTime();
    // Baseline velocity in ticks-per-second used when starting up the flywheels or resetting them to baseline
    private static final double baselineVelocity = 1500;
    // How much to bump the velocity up or down when changing speeds
    private static final double velocityAdjustmentAmount = 50;

    /**
     * Initialize our subsystems; this should be called once at robot startup
     */
    private void initializeSubsystems() {
        leftFlywheel = hardwareMap.get(DcMotorEx.class, "leftFlywheel");
        rightFlywheel = hardwareMap.get(DcMotorEx.class, "rightFlywheel");
        desiredFlywheelVelocity = 0;
        // A positive velocity sends the motor counter-clockwise; the motors should spin in opposite
        // directions of each other to propel the ball forwards. Set the right motor accordingly
        // to reverse its direction
        // Left: Counter-clockwise, Right: clockwise
        rightFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Check if we're outside the debounce window for flywheel increment/decrement
     * operations. This method exists to avoid reading a single up/down button press
     * as multiple events; reading one button press multiple times causes too much
     * increase/decrease in velocity
     *
     * @return True if a flywheel change is allowed, false otherwise
     */
    private boolean flywheelChangeAllowed() {
        // There should be a 500 millisecond buffer between flywheel adjustments
        return timeSinceVelocityChange.milliseconds() > 500;
    }

    /**
     * Adjust the desired velocity from its current amount by the specified amount.
     * This is useful to increment or decrement the velocity relative to its current position
     *
     * @param amount The amount to increment or decrement the velocity
     */
    private void adjustDesiredVelocity(double amount) {
        desiredFlywheelVelocity += amount;
        timeSinceVelocityChange.reset();
    }

    /**
     * Process gamepad input for the flywheel subsystem. This method takes in gamepad input
     * and adjusts the flywheel subsystems accordingly.
     * ===============
     * Controls:
     * right bumper: Reset flywheels to the baseline velocity; use this to turn on flywheel from a stop & reset
     * b button: Stop the flywheels
     * dpad up: Increase the speed of the flywheels
     * dpad down: Decrease the speed of the flywheels
     */
    private void processGamepadInputForFlywheel() {
        if (gamepad1.b) {
            desiredFlywheelVelocity = 0;
        } else if (gamepad1.right_bumper) {
            desiredFlywheelVelocity = baselineVelocity;
        } else if (gamepad1.dpad_up && flywheelChangeAllowed()) {
            // Note: Negative velocities actually are spinning the flywheel faster since we
            // start at a negative baseline
            adjustDesiredVelocity(velocityAdjustmentAmount);
        } else if (gamepad1.dpad_down && flywheelChangeAllowed()) {
            // Note: Positive velocities actually are spinning the flywheel slower since we
            // start at a negative baseline
            adjustDesiredVelocity(-velocityAdjustmentAmount);
        }
        // Set both flywheels to the desired velocity
        leftFlywheel.setVelocity(desiredFlywheelVelocity);
        rightFlywheel.setVelocity(desiredFlywheelVelocity);
    }

    /**
     * Record telemetry for the flywheel so we can observe actual
     * and desired flywheel velocities
     */
    private void recordFlywheelTelemetry() {
        // Record telemetry for the current Flywheel velocity and the desired velocity
        telemetry.addData("leftFlywheelVelocity", leftFlywheel.getVelocity());
        telemetry.addData("rightFlywheelVelocity", rightFlywheel.getVelocity());
        telemetry.addData("desiredFlywheelVelocity", desiredFlywheelVelocity);
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        initializeSubsystems();

        waitForStart();

        while (opModeIsActive()) {
            processGamepadInputForFlywheel();
            recordFlywheelTelemetry();
        }
    }
}
