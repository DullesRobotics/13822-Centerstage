package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class MechanumOpMode extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status","Initialized");
        telemetry.update();

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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while(opModeIsActive()){

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

            //send the power value to the wheel
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // Show the elapsed game time and wheel power
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
            telemetry.update();
        }

    }
}


/*

Initial version, but does not match the rest of the op modes

@TeleOp(name="basic= Mechanum OpMode", group="Linear OpMode")
@Disabled
public class MechanumOpMode extends BasicOpMode_Linear{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status","Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftMotor = hardwareMap.get(DcMotor.class,"FLM");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM");

        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each motor to save for telemetry
            double frontLeftPower;
            double backLeftPower;
            double frontRightPower;
            double backRightPower;

            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad2.right_stick_x;
            frontLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            backLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            frontRightPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            backRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);


            //send the power value to the wheel
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // Show the elapsed game time and wheel power
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
            telemetry.update();


        }

    }
}

 */
