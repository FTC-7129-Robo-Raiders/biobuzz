package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;

@TeleOp(name="Robot 1", group="Robot 1")
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
            robot1.catapult.processGamepadInput();
            robot1.collection.processGamepadInput();
            robot1.drivetrain.processGamepadInputForFieldOrientedDrive();
            robot1.dump.processGamepadInput();

            telemetry.addData("botHeading", robot1.imu.getRobotYawInDegrees());
            telemetry.update();
        }
    }
}