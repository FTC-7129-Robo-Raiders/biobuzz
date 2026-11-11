package org.firstinspires.ftc.teamcode.subsystems.robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Collection {
    private final LinearOpMode opMode;
    private DcMotor collectionMotor;
    
    public Collection(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init(HardwareMap hardwareMap) {
        // TODO - consider renaming device to "collection" instead of "collect1"
        collectionMotor = hardwareMap.get(DcMotor.class, "collect1");
        collectionMotor.setDirection(DcMotor.Direction.FORWARD);
        collectionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Force encoder reset and then run using it
        collectionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        collectionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void collect() {
        collectionMotor.setPower(-1);
    }

    public void eject() {
        collectionMotor.setPower(1);
    }

    public void stop() {
        collectionMotor.setPower(0);
    }

    /**
     * Process gamepad input to control the collection system
     */
    public void processGamepadInput() {
        // Stop immediately if the OpMode is no longer active
        if (!opMode.opModeIsActive()) return;

        if (opMode.gamepad1.left_bumper) {
            collect();
        } else if (opMode.gamepad1.right_bumper) {
            eject();
        } else {
            stop();
        }
    }
}
