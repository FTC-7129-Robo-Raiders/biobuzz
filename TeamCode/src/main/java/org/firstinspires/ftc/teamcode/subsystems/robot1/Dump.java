package org.firstinspires.ftc.teamcode.subsystems.robot1;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Dump {
    private final LinearOpMode opMode;
    public Servo dump;
    private final ElapsedTime dumpTimer = new ElapsedTime();

    public Dump(LinearOpMode opMode) {
        this.opMode = opMode;
    }
public void init(HardwareMap hardwareMap){
        this.dump = hardwareMap.get(Servo.class, "dump");
}
    public void open() {
        dump.setPosition(.41);
    }


    public void close() {
        dump.setPosition(.2);
    }

    public void autoOpen(){
        dumpTimer.reset();
        while (dumpTimer.seconds() < 1) {
            open();
        }
    }

    public void autoClose(){
        dumpTimer.reset();
        while (dumpTimer.seconds() < 1){
            close();
        }
    }

    public void processGamepadInput() {
        if (opMode.gamepad1.x) {
            open();
        }
        else if (opMode.gamepad1.b) {
            close();
        }
    }
}