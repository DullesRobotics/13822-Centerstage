package org.firstinspires.ftc.teamcode.OpModes.AutonOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class AutonFunction extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private DcMotor rightLever = null;
    private DcMotor leftLever = null;
    private Servo bucket = null;


    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftMotor = hardwareMap.get(DcMotor.class,"FLM");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM");

        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    //public double speed = 0.5;
    public boolean leftTurn = false;
    public boolean leftStrafe = false;
    public static int distance = 0;
    public static double speed = .5;
    private static double ticks = 537.7;
    private static double wheelDiameter = 96;
    //ticks per 1mm
    private static double conversion = (ticks/(2*Math.PI*(wheelDiameter/2)));
    private double strafeConversion = 1;
    private int intakeTarget = -39;
    int max = 12;





    public void initDrive(DcMotor frontLeftMotor, DcMotor backLeftMotor, DcMotor frontRightMotor, DcMotor backRightMotor){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftMotor = hardwareMap.get(DcMotor.class,"FLM");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM");

        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void initBot(){
        rightLever = hardwareMap.get(DcMotor.class, "RLM");
        leftLever = hardwareMap.get(DcMotor.class, "LLM");
        bucket = hardwareMap.get(Servo.class, "BUCKET");
    }

    public void driveTelemetry(DcMotor frontLeftMotor, DcMotor backLeftMotor, DcMotor frontRightMotor, DcMotor backRightMotor){
        telemetry.addData("FLM", frontLeftMotor.getCurrentPosition());
        telemetry.addData("BLM", backLeftMotor.getCurrentPosition());
        telemetry.addData("FRM", frontRightMotor.getCurrentPosition());
        telemetry.addData("BRM", backRightMotor.getCurrentPosition());
    }


    public void setSpeed(double speed){

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);
    }

    public void setTurn(double speed){
        if (leftTurn) {
            frontLeftMotor.setPower(-speed);
            backLeftMotor.setPower(-speed);
            frontRightMotor.setPower(speed);
            backRightMotor.setPower(speed);
        }else{
            frontLeftMotor.setPower(speed);
            backLeftMotor.setPower(speed);
            frontRightMotor.setPower(-speed);
            backRightMotor.setPower(-speed);
        }
    }

    public void stopMotor(){
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public void setStrafe(double speed){
        if(leftStrafe){
            frontLeftMotor.setPower(speed);
            backLeftMotor.setPower(-speed);
            frontRightMotor.setPower(-speed);
            backRightMotor.setPower(speed);
        } else{
            frontLeftMotor.setPower(-speed);
            backLeftMotor.setPower(speed);
            frontRightMotor.setPower(speed);
            backRightMotor.setPower(-speed);
        }
    }

    public void driveWithEncoders(double speed, int distance){

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftMotor.setTargetPosition((int) (distance*conversion));
        backLeftMotor.setTargetPosition((int) (distance*conversion));
        frontRightMotor.setTargetPosition((int) (-distance*conversion));
        backRightMotor.setTargetPosition((int) (-distance*conversion));

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void strafeWithEncoders(double speed, int distance, boolean leftStrafe){
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(leftStrafe) {
            frontLeftMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            backLeftMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            frontRightMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            backRightMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
        } else{
            frontLeftMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            backLeftMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
            frontRightMotor.setTargetPosition((int) (-distance * conversion * strafeConversion));
            backRightMotor.setTargetPosition((int) (distance * conversion * strafeConversion));
        }

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void turnWithEncoders(double speed, int distance){
        //distance is referring to the distance the wheels need to spin for a turn
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if(leftTurn) {
            frontLeftMotor.setTargetPosition((int) (-distance * conversion));
            backLeftMotor.setTargetPosition((int) (-distance * conversion));
            frontRightMotor.setTargetPosition((int) (-distance * conversion));
            backRightMotor.setTargetPosition((int) (-distance * conversion));
        } else{
            frontLeftMotor.setTargetPosition((int) (distance * conversion));
            backLeftMotor.setTargetPosition((int) (distance * conversion));
            frontRightMotor.setTargetPosition((int) (distance * conversion));
            backRightMotor.setTargetPosition((int) (distance * conversion));
        }

        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backRightMotor.setPower(speed);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void leverWithEncoders(int pos){
        leftLever.setTargetPosition((intakeTarget + (pos * 34)));
        leftLever.setPower(.25);
        leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLever.setTargetPosition((-intakeTarget - (pos * 34)));
        rightLever.setPower(.25);
        rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bucket.setPosition((double)(1 - ((double)pos * (1/12))));
    }

}
