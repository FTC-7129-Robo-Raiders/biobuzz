package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "HelloWorld", group = "Testing" )
public class HelloWorld extends LinearOpMode {

    private String text;
    private int loopCounter;


    private void initializeSubsystems() {
        //show HelloWorld on screen

        text = "HelloWorld";
        loopCounter = 0;

    }

    @Override
    public void runOpMode()
    {
        initializeSubsystems();

        waitForStart();
        while (opModeIsActive()) {
            loopCounter += 1;
            telemetry.addData("Some measurement", loopCounter);
            telemetry.addLine(text);
            telemetry.update();
        }
    }

}

