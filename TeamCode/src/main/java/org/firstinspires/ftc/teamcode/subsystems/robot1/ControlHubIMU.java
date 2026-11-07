package org.firstinspires.ftc.teamcode.subsystems.robot1;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class ControlHubIMU {
    public IMU imu;

    public void init(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot controlHubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        IMU.Parameters parameters = new IMU.Parameters(controlHubOrientation);
        imu.initialize(parameters);
    }

    /**
     * Get the robot's heading (yaw) in degrees
     */
    public double getRobotYawInDegrees() {
        YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
        return robotOrientation.getYaw(AngleUnit.DEGREES);
    }
}
