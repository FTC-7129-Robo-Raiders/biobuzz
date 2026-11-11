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

    public DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

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
        frontLeftMotor = getInitializedDriveMotor(hardwareMap, "leftFrontDrive", DcMotorSimple.Direction.FORWARD);
        backLeftMotor = getInitializedDriveMotor(hardwareMap, "leftBackDrive", DcMotorSimple.Direction.FORWARD);
        frontRightMotor = getInitializedDriveMotor(hardwareMap, "rightBackDrive", DcMotorSimple.Direction.REVERSE);
        backRightMotor = getInitializedDriveMotor(hardwareMap, "rightBackDrive", DcMotorSimple.Direction.REVERSE);

        // TODO - figure out if this makes sense for all drive styles or if this needs to be conditional
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
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
        opMode.telemetry.addData("leftFrontTicks", frontLeftMotor.getCurrentPosition());
        opMode.telemetry.addData("rightFrontTargetTicks", rightFrontTarget);
        opMode.telemetry.addData("rightFrontTicks", frontRightMotor.getCurrentPosition());
        opMode.telemetry.update();
    }

    /**
     * Check if the drive motors are still working and the timeout is not exceeded.
     * This is useful for waiting for all motors to complete a given task
     * @param timeoutInSeconds How long before timing out the target checks
     * @return True if the motors are still working; false if they are not or the timeout has been exceeded
     */
    private boolean isDrivingToTargetInches(double runtimeInSeconds, double timeoutInSeconds) {
        boolean anyWheelsBusy = frontLeftMotor.isBusy() ||
                frontRightMotor.isBusy() ||
                backLeftMotor.isBusy() ||
                backRightMotor.isBusy();
        return runtimeInSeconds < timeoutInSeconds && anyWheelsBusy;
    }

    /**
     * Set all drive motors to run without the encoder; the encoders are still used
     * for collecting measurements, but the PID controller is used for actual motor speed
     * correction.
     */
    private void setMotorsToRunMode(DcMotor.RunMode runMode) {
        frontLeftMotor.setMode(runMode);
        frontRightMotor.setMode(runMode);
        backLeftMotor.setMode(runMode);
        backRightMotor.setMode(runMode);
    }

    /**
     * Set the robot's motor power for each drive motor. This gives control to independently
     * power each motor as desired
     */
    public void setMotorPowers(double leftFrontMotorPower, double leftBackMotorPower, double rightFrontMotorPower, double rightBackMotorPower) {
        frontLeftMotor.setPower(leftFrontMotorPower);
        backLeftMotor.setPower(leftBackMotorPower);
        frontRightMotor.setPower(rightFrontMotorPower);
        backRightMotor.setPower(rightBackMotorPower);
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
        frontLeftMotor.setTargetPosition(leftFrontTarget);
        backLeftMotor.setTargetPosition(leftBackTarget);
        frontRightMotor.setTargetPosition(rightFrontTarget);
        backRightMotor.setTargetPosition(rightBackTarget);
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
        int leftFrontTarget = getMotorPositionAfterAdvancingInches(frontLeftMotor, leftInches);
        int leftBackTarget = getMotorPositionAfterAdvancingInches(backLeftMotor, leftInches);
        int rightFrontTarget = getMotorPositionAfterAdvancingInches(frontRightMotor, rightInches);
        int rightBackTarget = getMotorPositionAfterAdvancingInches(backRightMotor, rightInches);
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

    /**
     * Given gamepad inputs, drive using those inputs with a field-oriented control
     * scheme
     */
    public void driveFieldOrientedTeleop() {
        // Stop immediately if the OpMode is no longer active
        if (!opMode.opModeIsActive()) return;

        // Use the 'options' button to reset the zero angle on the robot
        if (opMode.gamepad1.options) {
            imu.resetYaw();
        }

        // The Y stick value is reversed so negate it here
        double y = -opMode.gamepad1.left_stick_y;
        double x = opMode.gamepad1.left_stick_x;
        double rx = opMode.gamepad1.right_stick_x;

        double botHeading = imu.getRobotYawInRadians();

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Counteract imperfect strafing; this provides a little more power
        // to our X axis during strafing due to the way mecanum wheels don't
        // perfectly transmit power evenly across the x/y planes
        rotX = rotX * 1.1;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        // The speed magnifier allows throttling the robot so that
        // it doesn't move too fast for the human operator
        double speedMagnifier = 0.8;
        frontLeftMotor.setPower(frontLeftPower * speedMagnifier);
        backLeftMotor.setPower(backLeftPower * speedMagnifier);
        frontRightMotor.setPower(frontRightPower * speedMagnifier);
        backRightMotor.setPower(backRightPower * speedMagnifier);
    }
}
