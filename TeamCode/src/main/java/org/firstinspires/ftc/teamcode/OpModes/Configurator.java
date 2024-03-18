package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Configurator extends LinearOpMode {




    @Override
    public void runOpMode() throws InterruptedException {

    }
    public Configurator(){
        ElapsedTime runtime = new ElapsedTime();
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "BRM");
        DcMotor intake = hardwareMap.get(DcMotor.class, "INTAKE");
        DcMotor rightLever = hardwareMap.get(DcMotor.class, "RLM");
        DcMotor leftLever = hardwareMap.get(DcMotor.class, "LLM");
        DcMotor hang = hardwareMap.get(DcMotor.class, "HANG");
        Servo bucket = hardwareMap.get(Servo.class, "BUCKET");
        CRServo intake2 = hardwareMap.get(CRServo.class, "INTAKESERVO");
        Servo drone = hardwareMap.get(Servo.class, "DRONE");


    }
    public void mecDrive(){
        new Configurator();
        // Setup a variable for each motor to save for telemetry
        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;

        // Add bumper buttons to slow the robot speed for more accuracy
        boolean slow = false;
        boolean superSlow = false;
        double slowSpeed = 0.5;
        double superSlowSpeed = 0.25;
        double range = 1;

        if (gamepad1.right_bumper)
            slow = true;
        if(gamepad1.left_bumper)
            superSlow = true;

        if (slow)
            range = slowSpeed;
        else if (superSlow)
            range = superSlowSpeed;


        // Strafe with Left stick
        /*
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad2.right_stick_x;
        frontLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
        backLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
        frontRightPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
        backRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
         */


        //Tank drive with trigger to strafe
        double leftJoy = gamepad1.left_stick_y;
        double rightJoy = gamepad1.right_stick_y;
        double leftTrigger = gamepad1.left_trigger;
        double rightTrigger = gamepad1.right_trigger;
        frontLeftPower = Range.clip(leftJoy + leftTrigger - rightTrigger, -range, range);
        backLeftPower = Range.clip(leftJoy - leftTrigger + rightTrigger, -range, range);
        frontRightPower = Range.clip(rightJoy - leftTrigger + rightTrigger, -range, range);
        backRightPower = Range.clip(rightJoy + leftTrigger - rightTrigger, -range, range);

        /*
        //send the power value to the wheel
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

        // Show the elapsed game time and wheel power
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
        telemetry.update();

         */
    }

    public void intake(){

    }
}
