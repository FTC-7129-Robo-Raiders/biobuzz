package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Robot: blueFromFlower", group="Robot")

public class autotest extends LinearOpMode {
    private final ElapsedTime dumpPositionTimer = new ElapsedTime();

    private void setDumpPosition(Servo dump, double position) {
        dumpPositionTimer.reset();
        while (dumpPositionTimer.seconds() < 1) {
            dump.setPosition(position);
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {

        R1auto robot1 = new R1auto(this);
        robot1.init(hardwareMap );

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
//blueFromFlower
robot1.encoderDrive(.5, 4, 4, 10);
        robot1.collect.setPower(1);
        //robot1.encoderDrive(.5, 2, -2, 10);
        sleep(200);
        robot1.launch();
        sleep(2000);
        setDumpPosition(robot1.dump, .41);
        setDumpPosition(robot1.dump, .2);
        robot1.launch();
        sleep(2000);
        setDumpPosition(robot1.dump, .41);
        setDumpPosition(robot1.dump, .2);
        robot1.launch();
        sleep(2000);
        setDumpPosition(robot1.dump, .41);
        setDumpPosition(robot1.dump, .2);
        robot1.launch();
        sleep(500);
        robot1.collect.setPower(0);
        robot1.encoderDrive(.5, 3, 3, 10);
        robot1.encoderDrive(.5, -22, 22, 10);
        robot1.encoderDrive(.5, 35, 35, 10);
        robot1.encoderDrive(.5, 21, -21, 10);
        robot1.encoderDrive(.5, 70, 70, 10);
        robot1.encoderDrive(.5, -21, 21, 10);
        robot1.encoderDrive(.5, 15, 15, 10);

        //90 turn
        //robot1.encoderDrive(.5, 22, -22, 10);
}}



