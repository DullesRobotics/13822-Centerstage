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

/*** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @IMPORTANT - This code represents 13822 TeleOp or Manual Operated code
 * This code holds many different functions and variables, use it to locate throughout
 * * @Variables
 * * @Movement
 * * @Intake
 * * @Bucket
 * * @Simple Methods
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


public class MasterTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        /*** @Variables ***/
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * Define our variables here, many different types are here
         * * First we define the hardware variables, connecting them to motors and servos that will help with completing tasks
         * * Next we define telemetry variables, useful for movement
         * * Last, we define general variables to be used in our code
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

        // General variables
        double speed = 1;
        double superSlowSpeed = 0.25;
        double range = .5;
        double bucketPos = 1;
        int intakeTarget = -100;
        boolean manual = true;
        boolean topTarget = false;
        boolean intakeFront = true;

        telemetry.addData("Hardware:", "Initialized");


        // General startup procedure
        waitForStart();
        runtime.reset();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            /*** @Movement ***/
            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * This code focuses on robot movement
             * * First part has a focus on different movement speeds based on the situation
             * * The second part has a focus on the general movement of the robot
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            // Speed control, bumper controls how fast/slow the robot move, else moves to regular speed
            if (gamepad1.right_bumper)
                range = speed;
            else if(gamepad1.left_bumper)
                range = superSlowSpeed;
            else
                range = 0.75;

            // maps the joysticks to move robot, triggers to strafe
            double leftJoy = gamepad1.left_stick_y;
            double rightJoy = gamepad1.right_stick_y;
            double leftTrigger = gamepad1.left_trigger;
            double rightTrigger = gamepad1.right_trigger;

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * Movement changes based on task being completed
             * * If intake is being needed, the robot switches left and right joysticks to compensate to picking up tokens
             * Else, the right stick moves the front motor, the left stick the back motors
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

        /*** @Intake ***/

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * Intake is a core part of our robot, being the backbone of our robot as it takes up the tokens that are
         * very important for this years game
         *
         * Our intake as such is like a system where everything else follows it
         * * First our intake has a toggle mode that determines how our robot moves
         * * The back of our robot contains the intake, so the motors in the back rotate to push them into a bucket
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            // Toggles intake driving mode
            if(gamepad1.dpad_up)
                intakeFront = true;
            else if(gamepad1.dpad_down)
                intakeFront = false;

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

        /*** @Bucket ***/
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * The bucket is how we move our token after getting them from the intake
         * * The bucket can be manually controlled, or have autonomous control setting it to certian positions
         * The bucket works with motors of an arm and servo of bucket
         *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            // checks for manual control
            if(gamepad2.dpad_right) {
                manual = true;
            } if(gamepad2.dpad_left){
                manual = false;
                bucketPos = bucket.getPosition();
            }

            // If manual, then moves the arm motor based on right and left triggers
            // works better if both are used
            if(manual) {
                rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                if (gamepad2.right_trigger > 0.2 || gamepad2.left_trigger > 0.2) {
                    rightLever.setPower((gamepad2.right_trigger / 4) -(gamepad2.left_trigger / 4));
                }else {
                    rightLever.setPower(0);
                }

                // Bucket motor changes tilt based on the bumpers
                bucket.setPosition(bucketPos);
                if (gamepad2.right_bumper && bucketPos<=1) {
                    bucketPos += 0.0015;
                }
                if (gamepad2.left_bumper && bucketPos>=0) {
                    bucketPos -= 0.0015;
                }


            }

            // If automatic, then motors are just based on the bumpers
            // Encoders help map the locations motors must snap to
            if(!manual) {
                rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                // right bumper sets the bucket to drop
                // left sets it in ready position
                if(gamepad2.right_bumper)
                    topTarget = true;
                else if(gamepad2.left_bumper)
                    topTarget = false;

                // if top target, bucket goes to the position and drops all remaining tokens in there
                // else, it goes to ready position
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

            }

            /*** @Simple Methods ***/
            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * These methods are for smaller functions, important, but not much to be said
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            // 
            if (gamepad2.y) {
                door.setPosition(.5);
            } else {
                door.setPosition(0);
            }


            // hang code
            // pressing x lifts the hang, pressing y pulls the hang up, allowing the robot to suspend in the air
            // else the hang stays stationary
            if (gamepad1.x)
                hang.setPower(1);
            else if (gamepad1.y)
                hang.setPower(-1);
            else
                hang.setPower(0);

            //drone code
            // pressing the up button releases the rubberband holding thr drone letting it fly
            if (gamepad1.dpad_up)
                drone.setPosition(0);
            else
                drone.setPosition(0.5);

        }
                // Show the elapsed game time and wheel power
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                //telemetry.addData("Motor", "FLM Power" + frontLeftPower, "BLM Power" + backLeftPower, "FRM Power" + frontRightPower, "BRM Power" + backRightPower);
                telemetry.addData("RLM", rightLever.getCurrentPosition());
                telemetry.addData("bucket", bucket.getPosition());
                telemetry.update();
    }


}
