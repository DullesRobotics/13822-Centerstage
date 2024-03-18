package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class LeverTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        double ticks = 288;
        double topTarget = 380;
        double intakeTarget = -39;
        double level = 1;
        double servoManualAdjust = 1;
        double virtualLink;
        boolean isLeverOn = false;
        boolean link = true;
        int pos = 5;
        int max = 12;

        DcMotor leftLever = hardwareMap.get(DcMotor.class, "LLM");
        DcMotor rightLever = hardwareMap.get(DcMotor.class, "RLM");
        Servo bucket = hardwareMap.get(Servo.class, "BUCKET");
        leftLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Hardware:", "Initialized");

        //Uncomment this if the motor resets each time
        //leftLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()){
        telemetry.addData("Left Motor", leftLever.getCurrentPosition());
        telemetry.addData("Right Motor", rightLever.getCurrentPosition());
        telemetry.addData("Bucket", bucket.getPosition());
        telemetry.addData("Right_Stick", gamepad2.right_stick_y);
        telemetry.addData("Position", pos);
        telemetry.update();

        if(gamepad2.x) {
            rightLever.setPower(-.5);
        }else if(gamepad2.y) {
            rightLever.setPower(.5);
        }else {
            rightLever.setPower(0);
        }

        /*
            bucket.setPosition((leftLever.getCurrentPosition()+ abs(intakeTarget))*(1/topTarget + abs(intakeTarget)));
            while (gamepad2.right_trigger > 0) {
                leftLever.setPower(-gamepad2.right_trigger);
                rightLever.setPower(gamepad2.right_trigger);
                //leftLever.setTargetPosition((int) (leftLever.getCurrentPosition() + gamepad2.right_trigger * 1));
                //rightLever.setTargetPosition((int) (rightLever.getCurrentPosition() - gamepad2.right_trigger * 1));
            }
            while (gamepad2.left_trigger > 0) {
                leftLever.setPower(gamepad2.left_trigger);
                rightLever.setPower(-gamepad2.left_trigger);
                //leftLever.setTargetPosition((int) (leftLever.getCurrentPosition() - gamepad2.left_trigger * 1));
                //rightLever.setTargetPosition((int) (rightLever.getCurrentPosition() + gamepad2.left_trigger * 1));
            }
            while (gamepad2.right_stick_y != 0) {
                telemetry.addData("Right_Stick", gamepad2.right_stick_y);
                telemetry.update();
                bucket.setPosition((leftLever.getCurrentPosition() + abs(intakeTarget))*(1/topTarget + abs(intakeTarget)));
            }
            leftLever.setPower(0);
            rightLever.setPower(0);

         */

            /*
            //creates a number of different positions for the arm and bucket
            if(gamepad2.right_bumper && pos <= 3){
                pos = pos + 3;
                sleep(250);
            }
            if (gamepad2.right_bumper && pos <= max) {
                pos++;
                sleep(100);
            }
            if(gamepad2.left_bumper && pos <= 3){
                pos = pos - 3;
                sleep(100);
            }
            if (gamepad2.left_bumper && pos <= max) {
                pos--;
                sleep(100);
            }
            if(gamepad2.right_stick_button)
                pos = 0;
            if(gamepad2.left_stick_button)
                pos = max;

            leftLever.setTargetPosition((int) (intakeTarget + (pos * 42)));
            leftLever.setPower(.25);
            leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightLever.setTargetPosition((int) (-intakeTarget - (pos * 42)));
            rightLever.setPower(.25);
            rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bucket.setPosition((1 - (pos * 0.1)));

             */


            /*

            //uses the motor encoder values to relate the servo position to the point of rotation in the arm
            while(topTarget >= leftLever.getCurrentPosition() && leftLever.getCurrentPosition() >= intakeTarget){
                double x = leftLever.getCurrentPosition();
                virtualLink = ((x+ abs(intakeTarget))*(1/topTarget + abs(intakeTarget)));
                bucket.setPosition(virtualLink);
            } if(leftLever.getCurrentPosition() > topTarget){
                bucket.setPosition(0);
            } if(leftLever.getCurrentPosition() < intakeTarget){
                bucket.setPosition(.8);
            }

            if(gamepad2.left_bumper){
                //bucket.setPosition(-level + .2);
                //leftLever.setPower(-1);
                //rightLever.setPower(1);
                link = true;
                leftLever.setTargetPosition((int)topTarget);
                leftLever.setPower(.7);
                isLeverOn = true;
                leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightLever.setTargetPosition((int)-topTarget);
                rightLever.setPower(.7);
                rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*
                //uses the motor encoder values to relate the servo position to the point of rotation in the arm
                while(isLeverOn){
                    if(288 >= leftLever.getCurrentPosition() && leftLever.getCurrentPosition() >= -50){
                        double x = leftLever.getCurrentPosition();
                        virtualLink = ((x+50)*(.002577));
                        bucket.setPosition(virtualLink);
                    } else if(leftLever.getCurrentPosition() > 288){
                        bucket.setPosition(0);
                    }else{
                        bucket.setPosition(.8);
                    }
                }

            }
            if(gamepad2.right_bumper){
                //bucket.setPosition(level);
                //leftLever.setPower(1);
                //rightLever.setPower(-1);
                link = true;
                leftLever.setTargetPosition((int)intakeTarget);
                leftLever.setPower(.7);
                leftLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightLever.setTargetPosition((int)-intakeTarget);
                rightLever.setPower(.7);
                rightLever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }


            if (gamepad2.right_stick_button && link) {
                link = false;
            }
            if (gamepad2.right_stick_button && link == false ) {
                link = true;
            }
            //Manual control of the lift and arm
            while (link) {
                while (gamepad2.right_trigger > 0) {
                    leftLever.setPower(-gamepad2.right_trigger);
                    rightLever.setPower(gamepad2.right_trigger);
                    //leftLever.setTargetPosition((int) (leftLever.getCurrentPosition() + gamepad2.right_trigger * 1));
                    //rightLever.setTargetPosition((int) (rightLever.getCurrentPosition() - gamepad2.right_trigger * 1));
                }
                while (gamepad2.left_trigger > 0) {
                    leftLever.setPower(gamepad2.left_trigger);
                    rightLever.setPower(-gamepad2.left_trigger);
                    //leftLever.setTargetPosition((int) (leftLever.getCurrentPosition() - gamepad2.left_trigger * 1));
                    //rightLever.setTargetPosition((int) (rightLever.getCurrentPosition() + gamepad2.left_trigger * 1));
                }
                while (gamepad2.right_stick_y != 0)
                    bucket.setPosition(bucket.getPosition() - gamepad2.right_stick_y);
                leftLever.setPower(0);
                rightLever.setPower(0);
            }


             */



        }


    }

}
