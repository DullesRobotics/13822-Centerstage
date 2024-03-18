package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class MasterTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //MechanumOpMode obj = new MechanumOpMode();
        //obj.runOpMode();

        //declare your variables
        ElapsedTime runtime = new ElapsedTime();
        boolean on = false, forward = true, togglePressedUp = false, togglePressedDown = false;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "BLM");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "FRM");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "BRM");
        DcMotor intake = hardwareMap.get(DcMotor.class, "INTAKE");
        DcMotor rightLever = hardwareMap.get(DcMotor.class, "RLM");
        //DcMotor leftLever = hardwareMap.get(DcMotor.class, "LLM");
        DcMotor hang = hardwareMap.get(DcMotor.class, "HANG");
        Servo bucket = hardwareMap.get(Servo.class, "BUCKET");
        CRServo intake2 = hardwareMap.get(CRServo.class, "INTAKESERVO");
        Servo drone = hardwareMap.get(Servo.class, "DRONE");
        Servo door = hardwareMap.get(Servo.class,"OUTDOOR");


        //Fot the robot to drive forward one side of the bot needs to be reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Setup a variable for each motor to save for telemetry
        double frontLeftPower = 0;
        double backLeftPower = 0;
        double frontRightPower = 0;
        double backRightPower = 0;

        // Add bumper buttons to slow the robot speed for more accuracy
        boolean fast = false;
        boolean slow = false;
        double speed = 1;
        double superSlowSpeed = 0.25;
        double range = .5;
        int liftLocation = 0;
        double bucketPos = 1;
        int pos = 0;
        int max = 12;
        int intakeTarget = -100;
        double power;
        boolean manual = true;
        boolean topTarget = false;
        boolean intakeFront = true;

        //leftLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData("Hardware:", "Initialized");


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

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

            // When right bumper is pressed, the speed is increased and inverse for left bumper
            // if left or right bumper is pressed and it's either slow or fast, returns to normal speed
            /***
             * @IMPORTANT: This code will only cycle through the modes. To change from fast to slow, you have to press left_bumper twice
             */
            if((gamepad1.left_bumper || gamepad1.right_bumper) && (slow || fast)){
                fast = false;
                slow = false;
            }
            else if (gamepad1.right_bumper)
                fast = true;
            else if(gamepad1.left_bumper)
                slow = true;

            // speed changes depending on mode
            if (fast)
                range = speed;
            else if (slow)
                range = superSlowSpeed;
            else
                range = 0.75;


            if(gamepad1.dpad_up)
                intakeFront = true;
            else if(gamepad1.dpad_down)
                intakeFront = false;

            double leftJoy = gamepad1.left_stick_y;
            double rightJoy = gamepad1.right_stick_y;
            double leftTrigger = gamepad1.left_trigger;
            double rightTrigger = gamepad1.right_trigger;
            if(intakeFront) {
                //Tank drive with trigger to strafe
                frontLeftPower = Range.clip(leftJoy + leftTrigger - rightTrigger, -range, range);
                backLeftPower = Range.clip(leftJoy - leftTrigger + rightTrigger, -range, range);
                frontRightPower = Range.clip(rightJoy - leftTrigger + rightTrigger, -range, range);
                backRightPower = Range.clip(rightJoy + leftTrigger - rightTrigger, -range, range);
            }else {
                frontLeftPower = Range.clip(-rightJoy - leftTrigger + rightTrigger, -range, range);
                backLeftPower = Range.clip(-rightJoy + leftTrigger - rightTrigger, -range, range);
                frontRightPower = Range.clip(-leftJoy + leftTrigger - rightTrigger, -range, range);
                backRightPower = Range.clip(-leftJoy - leftTrigger + rightTrigger, -range, range);
            }



        //send the power value to the wheel using encoder values
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //send the power value to the wheel
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);



        // Show the elapsed game time and wheel power
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
        telemetry.update();




            //Intake controlled with a toggle from the dpad, direction can be reversed
            if (togglePressedUp && !gamepad2.dpad_up)
                togglePressedUp = false;

            if (togglePressedDown && !gamepad2.dpad_down)
                togglePressedDown = false;

            if (gamepad2.dpad_up && !togglePressedUp) {
                togglePressedUp = true;
                if (!forward)
                    forward = true;
                else if (on)
                    on = false;
                else {
                    on = true;
                    forward = true;
                }
            }

            if (gamepad2.dpad_down && !togglePressedDown) {
                togglePressedDown = true;
                if (forward)
                    forward = false;
                else if (on)
                    on = false;
                else {
                    on = true;
                    forward = false;
                }
            }


            if (on && forward) {
                intake.setPower(-1);
                intake2.setPower(-1);
            }
            else if (on && !forward) {
                intake.setPower(1);
                intake2.setPower(1);
            }else {
                intake.setPower(0);
                intake2.setPower(0);
            }


            if(gamepad2.dpad_right) {
                manual = true;
            } if(gamepad2.dpad_left){
                manual = false;
                bucketPos = bucket.getPosition();
            }

            if(manual) {
                rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                if (gamepad2.right_trigger > 0.2 || gamepad2.left_trigger > 0.2) {
                    rightLever.setPower((gamepad2.right_trigger / 4) -(gamepad2.left_trigger / 4));
                }else {
                    rightLever.setPower(0);
                }

                bucket.setPosition(bucketPos);
                if (gamepad2.right_bumper && bucketPos<=1) {
                    bucketPos += 0.0015;
                }
                if (gamepad2.left_bumper && bucketPos>=0) {
                    bucketPos -= 0.0015;
                }


            }

            if(!manual) {
                rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                /*
                if (((Math.abs(rightLever.getTargetPosition()) - Math.abs(rightLever.getCurrentPosition())) >= 25)) {
                    power = .25;
                } else {
                    power = .5;
                }

                 */

                if(gamepad2.right_bumper)
                    topTarget = true;
                else if(gamepad2.left_bumper)
                    topTarget = false;


                if (topTarget) {
                    rightLever.setTargetPosition(545);
                    sleep(500);
                    bucket.setPosition(.05);
                } else if(!topTarget){
                    rightLever.setTargetPosition(intakeTarget);
                    bucket.setPosition(1);
                }

                rightLever.setPower(0.3);
                rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                //telemetry.addData("motor pos", rightLever.getCurrentPosition());

            }

            if (gamepad2.y) {
                door.setPosition(.5);
            } else {
                door.setPosition(0);
            }


            //hang code
            if (gamepad1.x)
                hang.setPower(1);
            else if (gamepad1.y)
                hang.setPower(-1);
            else
                hang.setPower(0);

            //drone code
            if (gamepad1.dpad_up)
                drone.setPosition(0);
            else
                drone.setPosition(0.5);


            /*
            //changes lever speed(bad version of pid)
            if(Math.abs(leftLever.getCurrentPosition() - (intakeTarget +(pos*34))) > 50){
                rightLever.setPower(.75);
                leftLever.setPower(.75);
            } else{
                leftLever.setPower(.25);
                rightLever.setPower(.25);
            }


             //creates a number of different positions for the arm and bucket
            if(gamepad2.right_bumper && pos <= 3){
                pos = pos + 3;
                sleep(250);
            }
            if (gamepad2.right_bumper && pos <= max) {
                pos++;
                sleep(100);
            }
            if(gamepad2.left_bumper && pos <= 3 && pos >= 0){
                pos = pos - 3;
                sleep(100);
            }
            if (gamepad2.left_bumper && pos >= 0) {
                pos--;
                sleep(100);
            }
            if(gamepad2.right_stick_button)
                pos = 0;
            if(gamepad2.left_stick_button)
                pos = max;

            leftLever.setTargetPosition((int) (intakeTarget + (pos * 34)));
            leftLever.setPower(.25);
            leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightLever.setTargetPosition((int) (-intakeTarget - (pos * 34)));
            rightLever.setPower(.25);
            rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bucket.setPosition((1 - ((double)pos * (double)(1/12) )));

            /*
            bucket.setPosition(bucketPos);
            if (gamepad2.x && bucketPos<=1) {
                bucketPos += 0.005;
            }
            if (gamepad2.y&& bucketPos>=0) {
                bucketPos -= 0.005;
            }
            */



        }

                // Show the elapsed game time and wheel power
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                //telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
                telemetry.addData("RLM", rightLever.getCurrentPosition());
                telemetry.addData("bucket", bucket.getPosition());
                telemetry.update();
    }


}




    /*
    @Override
    public void init() {

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


        intake = hardwareMap.get(DcMotor.class ,"INTAKE");
        telemetry.addData("Hardware:", "Initialized");


    }

    @Override
    public void loop() {

        // Wait for the game to start (driver presses PLAY)
        runtime.reset();

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


    /*
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


        if (togglePressedUp && !gamepad2.dpad_up)
            togglePressedUp = false;

        if(togglePressedDown && !gamepad2.dpad_down)
            togglePressedDown = false;

        if(gamepad2.dpad_up && !togglePressedUp){
            togglePressedUp = true;
            if(!forward){
                forward = true;
            } else if(on){
                on = false;
            } else{
                on = true;
                forward = true;
            }
        }

        if(gamepad2.dpad_down && !togglePressedDown){
            togglePressedDown = true;
            if(forward){
                forward = false;
            } else if(on){
                on = false;
            } else{
                on = true;
                forward = false;
            }
        }

        if (on && forward){
            intake.setPower(-1);
        }
        if (on && !forward){
            intake.setPower(1);
        }
        intake.setPower(0);

    }
}

*/