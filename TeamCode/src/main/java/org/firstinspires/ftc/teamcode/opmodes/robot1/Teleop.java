package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;

@TeleOp(name="R1test", group="Robot 1")
public class Teleop extends LinearOpMode {
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

            robot1.drivetrain.driveFieldOrientedTeleop();

            telemetry.addData("botHeading", robot1.imu.getRobotYawInDegrees());
            telemetry.update();
        }
    }
}