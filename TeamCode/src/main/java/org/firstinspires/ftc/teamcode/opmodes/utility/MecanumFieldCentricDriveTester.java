package org.firstinspires.ftc.teamcode.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Field-centric Mecanum Drive Tester", group = "Testing")
public class MecanumFieldCentricDriveTester extends LinearOpMode {
    private DcMotor getInitializedDriveMotor(HardwareMap hardwareMap, String deviceName, DcMotorSimple.Direction motorDirection) {
        DcMotor motor = hardwareMap.get(DcMotor.class, deviceName);
        motor.setDirection(motorDirection);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }

    @Override
    public void runOpMode() {
        DcMotor frontLeftMotor = getInitializedDriveMotor(hardwareMap,"frontLeftMotor", DcMotorSimple.Direction.REVERSE);
        DcMotor backLeftMotor = getInitializedDriveMotor(hardwareMap,"backLeftMotor", DcMotorSimple.Direction.REVERSE);
        DcMotor frontRightMotor = getInitializedDriveMotor(hardwareMap,"frontRightMotor", DcMotorSimple.Direction.FORWARD);
        DcMotor backRightMotor = getInitializedDriveMotor(hardwareMap,"backRightMotor", DcMotorSimple.Direction.FORWARD);

        // Initialize the IMU; adjust the orientation parameters to match your robot
        IMU imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot controlHubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
        );
        IMU.Parameters parameters = new IMU.Parameters(controlHubOrientation);
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // The Y stick value is reversed so negate it here
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Use the 'options' button to reset the zero angle on the robot
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

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
}