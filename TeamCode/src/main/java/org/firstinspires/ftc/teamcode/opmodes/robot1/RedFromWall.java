package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;


@Autonomous(name="Park", group="Robot 1")
public class RedFromWall extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot1 robot1 = new Robot1(this);
        robot1.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();

        robot1.drivetrain.driveInches(1, 3, 3, 10);
        robot1.drivetrain.driveInches(.5, 23, -23, 10);
        robot1.drivetrain.driveInches(1, 4, 4, 10);
    }
}
