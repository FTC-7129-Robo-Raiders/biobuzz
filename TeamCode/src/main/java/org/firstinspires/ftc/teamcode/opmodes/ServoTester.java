package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class ServoTester extends LinearOpMode {
    private Servo servo;
    // Track the desired position of the servo
    private double desiredServoPosition;
    // Used to avoid incrementing/decrementing too quickly when multiple up/down keypresses
    // are read within a loop
    private final ElapsedTime timeSinceServoChange = new ElapsedTime();

    /**
     * Initialize our subsystems; this should be called once at robot startup
     */
    private void initializeSubsystems() {
        servo = hardwareMap.get(Servo.class, "servo");
        desiredServoPosition = 0;
    }

    /**
     * Check if we're outside the debounce window for servo increment/decrement
     * operations. This method exists to avoid reading a single up/down button press
     * as multiple events; reading one button press multiple times causes too much
     * increase/decrease in position
     *
     * @return True if a servo change is allowed, false otherwise
     */
    private boolean servoChangeAllowed() {
        // There should be a 500 millisecond buffer between servo adjustments
        return timeSinceServoChange.milliseconds() > 500;
    }

    /**
     * Adjust the desired servo position from its current amount by the specified amount.
     * This is useful to increment or decrement the position relative to its current position
     *
     * @param amount The amount to increment or decrement the position
     */
    private void adjustDesiredPosition(double amount) {
        desiredServoPosition += amount;
        timeSinceServoChange.reset();
    }

    /**
     * Process gamepad input for the servo subsystem. This method takes in gamepad input
     * and adjusts the servo subsystem accordingly. It also records desired
     * and actual servo telemetry for observability's sake
     * ===============
     * Controls:
     * b button: Reset position to zero
     * dpad up: Increase the position of the servo
     * dpad down: Decrease the position of the servo
     */
    private void processGamepadInputForServo() {
        double servoPositionAdjustmentStep = 0.5;
        if (gamepad1.b) {
            desiredServoPosition = 0;
        } else if (gamepad1.dpad_up && servoChangeAllowed()) {
            adjustDesiredPosition(-servoPositionAdjustmentStep);
        } else if (gamepad1.dpad_down && servoChangeAllowed()) {
            // Note: Positive velocities actually are spinning the flywheel slower since we
            // start at a negative baseline
            adjustDesiredPosition(servoPositionAdjustmentStep);
        }
        servo.setPosition(desiredServoPosition);
    }

    /**
     * Record telemetry for the servo so we can observe actual
     * and desired servo positions
     */
    private void recordServoTelemetry() {
        // Record telemetry for the current servo position and the desired position
        telemetry.addData("servoPosition", servo.getPosition());
        telemetry.addData("desiredServoPosition", desiredServoPosition);
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        initializeSubsystems();

        waitForStart();

        while (opModeIsActive()) {
            processGamepadInputForServo();
            recordServoTelemetry();
        }
    }
}
