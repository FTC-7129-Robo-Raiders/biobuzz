package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.R1Auto;

@Autonomous(name="Robot: AutoTest", group="Robot")
public class AutoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        R1Auto robot1 = new R1Auto(this);
        robot1.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();


        //robot1.encoderDrive(.5, 55, 55, 10);

        //robot1.encoderDrive(.5, 10, 10, 10);
        robot1.turnToPID(90,10);


    }}
