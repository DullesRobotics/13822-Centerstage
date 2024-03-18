package org.firstinspires.ftc.teamcode.OpModes.AutonOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class EncoderTest extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor rightLever = null;
    private DcMotor leftLever = null;


    @Override
    public void runOpMode() throws InterruptedException {

        AutonFunction functions = new AutonFunction();


        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class,"FLM");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "BRM");
        DcMotor rightLever = hardwareMap.get(DcMotor.class, "RLM");
        Servo bucket = hardwareMap.get(Servo.class, "BUCKET");

        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //frontLeftMotor.setPower(.25);

        rightLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //functions.initDrive(frontLeftMotor, backLeftMotor, backRightMotor, backRightMotor);

        waitForStart();
        resetRuntime();

        while (opModeIsActive()){
            /*
            frontLeftMotor.setPower(.25);
            backLeftMotor.setPower(.25);
            frontRightMotor.setPower(.25);
            backRightMotor.setPower(.25);
            frontLeftMotor.setTargetPosition(1000);
            backLeftMotor.setTargetPosition(1000);
            frontRightMotor.setTargetPosition(1000);
            backRightMotor.setTargetPosition(1000);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

             */

            //sleep(5000);



            //functions.driveTelemetry(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("FLM", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BLM", backLeftMotor.getCurrentPosition());
            telemetry.addData("FRM", frontRightMotor.getCurrentPosition());
            telemetry.addData("BRM", backRightMotor.getCurrentPosition());
            telemetry.addData("RLM", rightLever.getCurrentPosition());
            telemetry.addData("Bucket",bucket.getPosition());
            telemetry.update();

            //frontLeftMotor.setTargetPosition(0);
            //sleep(10000);



        }
        requestOpModeStop();
    }
}
