package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class ResetEncoder extends LinearOpMode {

    DcMotor rightLever = hardwareMap.get(DcMotor.class, "RLM");

    @Override
    public void runOpMode() throws InterruptedException {
        rightLever.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Arm Motor reset", rightLever.getCurrentPosition());
        sleep(5000);
    }
}
