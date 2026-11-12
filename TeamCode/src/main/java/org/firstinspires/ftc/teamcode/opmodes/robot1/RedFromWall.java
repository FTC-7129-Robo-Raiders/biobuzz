package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;

@Disabled
@Autonomous(name="Robot1: RedFromWall", group="Robot 1")
public class RedFromWall extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot1 robot1 = new Robot1(this);
        robot1.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();

        robot1.drivetrain.driveInches(.5, 110, 110, 10);
        robot1.drivetrain.driveInches(.5, 68, -68, 10);
    }
}
