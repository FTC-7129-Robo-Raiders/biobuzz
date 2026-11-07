package org.firstinspires.ftc.teamcode.subsystems.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Catapult {
    private final ElapsedTime launchTimer = new ElapsedTime();
    private final LinearOpMode opMode;
    private DcMotor catapultMotor;
    
    public Catapult(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init(HardwareMap hardwareMap) {
        // TODO - consider naming this catapult
        this.catapultMotor = hardwareMap.get(DcMotor.class, "launch");
        catapultMotor.setDirection(DcMotor.Direction.REVERSE);
        catapultMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapultMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void recordLaunchingTelemetry(double currentPosition, double targetPosition) {
        opMode.telemetry.addLine("Preparing to launch");
        opMode.telemetry.addData("Catapult position", currentPosition);
        opMode.telemetry.addData("Desired catapult position", targetPosition);
        opMode.telemetry.update();
    }

    private void recordLaunchedTelemetry() {
        opMode.telemetry.addLine("Done Launching");
        opMode.telemetry.update();
    }

    /**
     * Set motor power directly for launching from the catapult. This is used
     * to give the driver more control over the launch mechanism in teleop
     * as compared to autoLaunch which triggers a complete launch in one call
     */
    public void launch() {
        catapultMotor.setPower(1.0);
    }

    /**
     * Stop the catapult
     */
    public void stop() {
        catapultMotor.setPower(0.0);
    }

    /**
     * Auto-launch is used to trigger the catapult to fire
     */
    public void autoLaunch(){
        int targetPosition = catapultMotor.getCurrentPosition() + 1425;
        launch();

        launchTimer.reset();
        double launchTimeoutInSeconds = 5;
        while (catapultMotor.getCurrentPosition() < targetPosition && launchTimer.seconds() < launchTimeoutInSeconds) {
            recordLaunchingTelemetry(catapultMotor.getCurrentPosition(), targetPosition);
        };

        stop();
        recordLaunchedTelemetry();
    }
}
