package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Robot: bluePark", group="Robot")

public class bluePark extends LinearOpMode {
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
        robot1.encoderDrive(.5, 10, 10, 10);
        robot1.encoderDrive(.5, 22, -22, 10);
        robot1.encoderDrive(.5, 46, 46,  10);
       // robot1.encoderDrive(.5, 22, -22, 10);
       // robot1.encoderDrive(.5, 62, 62, 10);
        //robot1.encoderDrive(.5, -22,22, 10);
      //  robot1.encoderDrive(.5, 5, 5, 10);
    }}



