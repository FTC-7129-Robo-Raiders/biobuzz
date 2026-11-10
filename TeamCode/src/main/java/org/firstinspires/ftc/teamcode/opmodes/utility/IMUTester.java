package org.firstinspires.ftc.teamcode.opmodes.utility;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

// Context: We noticed our robot reporting what appeared to be incorrect
// orientation from our IMU. The OpMode is meant to allow testing the IMU
// in isolation to troubleshoot if our code is incorrectly reporting the current
// orientation of the robot or if the IMU is bad/needs calibration

// How to use this OpMode:
// 1. Startup the OpMode from the driver station
// 2. This will start reporting IMU measurements to the driver station
// 3. Turn the IMU unit (e.g. the test assembly, Control Hub, etc.) by hand to a target position
// 4. Observe the changes to the IMU measurements on the driver station
// 5. If desired, press "b" on the gamepad to reset the robot's heading measurement (yaw) to zero
// 6. After stopping the OpMode, a file named imu_testing_log will also be created on the Control Hub.
//    This file can be used for more in-depth analysis of changes to the IMU measurements throughout the run.

@TeleOp(name = "IMU Tester", group = "Testing")
public class IMUTester extends LinearOpMode {
    private IMU imu;

    private void initializeIMU(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, "imu");
        // If you reorient the IMU, adjust these values
        RevHubOrientationOnRobot controlHubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        IMU.Parameters parameters = new IMU.Parameters(controlHubOrientation);
        imu.initialize(parameters);
    }

    private void processGamepadInputForIMU() {
        if (gamepad1.b) {
            imu.resetYaw();
        }
    }

    private void recordIMUTelemetry() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        telemetry.addData("yaw", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("pitch", orientation.getPitch(AngleUnit.DEGREES));
        telemetry.addData("roll", orientation.getRoll(AngleUnit.DEGREES));
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        initializeIMU(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            processGamepadInputForIMU();
            recordIMUTelemetry();
        }
    }
}
