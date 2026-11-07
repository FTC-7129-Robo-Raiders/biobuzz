package org.firstinspires.ftc.teamcode.subsystems.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

/** Robot 1 with all its subsystems */
public class Robot1 {
    public final ControlHubIMU imu;
    public final Drivetrain drivetrain;
    public final Collection collection;
    public final Catapult catapult;

    public Robot1(LinearOpMode opMode) {
        this.imu = new ControlHubIMU();
        this.drivetrain = new Drivetrain(opMode, imu);
        this.collection = new Collection();
        this.catapult = new Catapult(opMode);
    }

    public void init(HardwareMap hardwareMap) {
        this.imu.init(hardwareMap);
        this.drivetrain.init(hardwareMap);
        this.collection.init(hardwareMap);
        this.catapult.init(hardwareMap);
    }
}
