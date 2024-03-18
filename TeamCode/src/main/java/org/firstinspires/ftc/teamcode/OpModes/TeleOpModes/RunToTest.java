package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class RunToTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor test = hardwareMap.get(DcMotor.class,"RLM");
        test.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            if(gamepad2.x) {
                test.setPower(.5);
                test.setTargetPosition(200);
                test.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }else{
                test.setPower(.25);
                test.setTargetPosition(-200);
                test.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
    }
}
