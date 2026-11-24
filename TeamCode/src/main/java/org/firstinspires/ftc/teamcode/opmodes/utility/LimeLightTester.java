package org.firstinspires.ftc.teamcode.opmodes.utility;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp(name= "LimeLightTester", group = "Testing")
public class LimeLightTester extends LinearOpMode {
    private Limelight3A limelight;

    private void initializeSubsystems() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initializeSubsystems();
        waitForStart();
        limelight.start();
        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    Pose3D botPose = result.getBotpose();
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("Bot pose", botPose.toString());
                }
            }
        }
    }
}
