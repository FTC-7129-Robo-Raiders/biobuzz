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

    private void recordLimelightResultTelemetry(LLResult limelightResult) {
        if (limelightResult == null) {
            telemetry.addLine("Limelight result is null");
        } else if (!limelightResult.isValid()) {
            telemetry.addLine("Limelight result is not valid");
        } else {
            Pose3D botPose = limelightResult.getBotpose();
            telemetry.addData("tx", limelightResult.getTx());
            telemetry.addData("ty", limelightResult.getTy());
            telemetry.addData("Bot pose", botPose.toString());
        }
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initializeSubsystems();

        waitForStart();

        limelight.start();
        while (opModeIsActive()) {
            LLResult limelightResult = limelight.getLatestResult();
            recordLimelightResultTelemetry(limelightResult);
        }
    }
}
