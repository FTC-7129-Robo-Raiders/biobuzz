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

    private boolean autoDumpRunning;


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
        if (!autoDumpRunning) {
            autoDumpRunning = true;
            dumpTimer.reset();
        }
        if (dumpTimer.seconds() < 1){
            open();
        }
        else{
            autoDumpRunning = false;
        }
    }

    public void autoClose(){
        if (!autoDumpRunning) {
            autoDumpRunning = true;
            dumpTimer.reset();
        }
        if (dumpTimer.seconds() < 1){
            close();
        }
        else {
            autoDumpRunning = false;
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