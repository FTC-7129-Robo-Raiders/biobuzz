package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Robot: AutoTest", group="Robot")

public class autotest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        R1auto robot1 = new R1auto(this);
        robot1.init(hardwareMap );

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();


        //robot1.encoderDrive(.5, 55, 55, 10);

        //robot1.encoderDrive(.5, 10, 10, 10);
        robot1.turnToPID(45,10);
        //robot1.launch();

       // robot1.encoderDrive(.5, 24, -24, 10);
}}



