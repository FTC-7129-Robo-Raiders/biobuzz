package org.firstinspires.ftc.teamcode.opmodes.robot1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.robot1.Robot1;

@Autonomous(name="Robot 1: AutoTest", group="Robot 1")
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot1 robot1 = new Robot1(this);
        robot1.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        robot1.drivetrain.driveInches(.5, 4, 4, 10);
        robot1.collection.collect();
        //robot1.drivetrain.driveInches();(.5, 2, -2, 10);
        sleep(200);
       robot1.catapult.autoLaunch();
        sleep(2000);
       robot1.dump.autoOpen();
       robot1.dump.autoClose();
       robot1.catapult.autoLaunch();
        sleep(2000);
       robot1.dump.autoOpen();
       robot1.dump.autoClose();
       robot1.catapult.autoLaunch();
        sleep(2000);
       robot1.dump.autoOpen();
       robot1.dump.autoClose();
       robot1.catapult.autoLaunch();
        sleep(500);
        robot1.collection.stop();
        robot1.drivetrain.driveInches(.5, 3, 3, 10);
        robot1.drivetrain.driveInches(.5, -22, 22, 10);
        robot1.drivetrain.driveInches(.5, 35, 35, 10);
        robot1.drivetrain.driveInches(.5, 21, -21, 10);
        robot1.drivetrain.driveInches(.5, 70, 70, 10);
        robot1.drivetrain.driveInches(.5, -21, 21, 10);
        robot1.drivetrain.driveInches(.5, 15, 15, 10);

    }}
