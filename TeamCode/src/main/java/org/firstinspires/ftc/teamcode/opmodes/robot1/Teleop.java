package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;

@TeleOp(name="R1test", group="Robot 1")
public class Teleop extends LinearOpMode {
    BNO055IMU imuCH;
    // Set this lower than 1.0 to throttle the drive train speed. E.g. .75 means
    // moving the joystick to full speed will actually drive the robot 3/4 of its max speed
    final double driveTrainSpeedAdjustment = 1.0;

    double zeroPosition = Math.toRadians(180);
    double robotZeroAngleOffset = 0;

    //private DcMotor lift = null;

    public DcMotor collect1 = null;


    //private Servo dump;

    @Override
    public void runOpMode() {
        Robot1 robot1 = new Robot1(this);
        robot1.init(hardwareMap);

        telemetry.addData(">", "Robot Ready.  Press START.");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        while (opModeIsActive()) {
            if (gamepad1.y) {
                robot1.catapult.launch();
            } else {
                robot1.catapult.stop();
            }

            if(gamepad1.left_bumper) {
                robot1.collection.collect();
            } else {
                robot1.collection.stop();
            }
            if (gamepad1.right_bumper) {
                robot1.collection.eject();
            } else {
                robot1.collection.stop();
            }

            // Question: Why do we negate the angle?
            robotZeroAngleOffset = -imuCH.getAngularOrientation().firstAngle;
            if (gamepad1.ps) {
                zeroPosition = robotZeroAngleOffset;
            }
            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x;
            double rx = -gamepad1.right_stick_x * .8;

            double botHeading = robotZeroAngleOffset - zeroPosition;

            double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            robot1.drivetrain.setMotorPowers(
                    frontLeftPower * driveTrainSpeedAdjustment,
                    backLeftPower * driveTrainSpeedAdjustment,
                    frontRightPower * driveTrainSpeedAdjustment,
                    backRightPower * driveTrainSpeedAdjustment
            );

            telemetry.addData("botHeading",botHeading);
            telemetry.update();
        }
    }
}