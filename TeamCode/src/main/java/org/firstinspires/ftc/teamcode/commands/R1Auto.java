package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class R1Auto {
    BNO055IMU imu;

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor collect = null;
    public DcMotor launch = null;


    private final ElapsedTime runtime = new ElapsedTime();


    static final double FORWARD_SPEED = 1.0;
    static final double TURN_SPEED = 0.5;

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 4.1;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);
    LinearOpMode opMode;

    public R1Auto(LinearOpMode OP) {
        opMode = OP;
    }

    public void init(HardwareMap hardwareMap) {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");

        collect = hardwareMap.get(DcMotor.class, "collect1");

        launch = hardwareMap.get(DcMotor.class, "launch");


        collect.setDirection(DcMotor.Direction.FORWARD);

        launch.setDirection(DcMotor.Direction.REVERSE);


        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        collect.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        launch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        collect.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int LeftFrontTarget;

        int RightFrontTarget;
        int LeftBackTarget;
        int RightBackTarget;



        // Ensure that the OpMode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            LeftFrontTarget = leftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            RightFrontTarget = rightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            LeftBackTarget = leftBackDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            RightBackTarget = rightBackDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(LeftFrontTarget);
            rightDrive.setTargetPosition(RightFrontTarget);

            leftBackDrive.setTargetPosition(LeftBackTarget);
            rightBackDrive.setTargetPosition(RightBackTarget);


            // Turn On RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.


            runtime.reset();
            leftDrive.setPower(Math.abs(speed));
            rightDrive.setPower(Math.abs(speed));

            leftBackDrive.setPower(Math.abs(speed));
            rightBackDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftDrive.isBusy() && rightDrive.isBusy() && leftBackDrive.isBusy()))//rightBack.isBusy())) {

            {
                // Display it for the driver.
                opMode.telemetry.addData("Running to", " %7d :%7d", LeftFrontTarget, RightFrontTarget);
                opMode.telemetry.addData("Running to", " %7d :%7d", LeftBackTarget, RightBackTarget);
                opMode.telemetry.addData("Currently at", " at %7d :%7d",
                        leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                opMode.telemetry.addData("Currently at", " at %7d :%7d",
                        leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());


                opMode.telemetry.update();
            }

            // Stop all motion;
            leftDrive.setPower(0);
            rightDrive.setPower(0);

            leftBackDrive.setPower(0);
            rightBackDrive.setPower(0);

            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            // Turn off RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            opMode.sleep(250);   // optional pause after each move.
        }


    }
    public void launch(){
        int targetPos = (launch.getCurrentPosition() + (int) (1425.1));
        launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launch.setPower(1);


        while (opMode.opModeIsActive() && launch.getCurrentPosition() < (targetPos)) {
            opMode.telemetry.addData("Launch pos", launch.getCurrentPosition());
            opMode.telemetry.update();

        }
        ;

        launch.setPower(0);
        opMode.telemetry.addLine("Done Launching");
        opMode.telemetry.update();



    }



    public double getAbsoluteAngle() {

        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
    public void turnToPID ( double targetAngle, double timeoutS){
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        TurnPID pid = new TurnPID(targetAngle, 0.005, 0.00000000, 0.0000000000000);
        //theOpMode.telemetry.setMsTransmissionInterval(50);
        double degreeCount = 0;
        runtime.reset();
        // Checking lastSlope to make sure that it's not oscillating when it quits
        while ((Math.abs(targetAngle - getAbsoluteAngle()) > 1 || pid.getLastSlope() > 1.25 || degreeCount < 4) && runtime.seconds() < timeoutS) {
            if (Math.abs(targetAngle - getAbsoluteAngle()) < 1) {
                degreeCount += 1;
            }
            double motorSpeed = pid.update(getAbsoluteAngle());
            leftDrive.setPower(motorSpeed);
            rightDrive.setPower(-motorSpeed);
            leftBackDrive.setPower(motorSpeed);
            rightBackDrive.setPower(-motorSpeed);
            opMode.telemetry.addData("degreeCount", degreeCount);
            opMode.telemetry.addData("degreeCount", degreeCount);
            opMode.telemetry.addData("Current Angle", getAbsoluteAngle());
            opMode.telemetry.addData("Target Angle", targetAngle);
            opMode.telemetry.addData("Slope", pid.getLastSlope());
            opMode.telemetry.addData("Power", motorSpeed);
            opMode.telemetry.update();
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
}