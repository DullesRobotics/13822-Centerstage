package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
@Disabled
public class HangTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor winchMotor = hardwareMap.get(DcMotor.class, "HANG");

        waitForStart();

        if (isStopRequested()) return;
        
        while(opModeIsActive()){

            if(gamepad2.y)
                winchMotor.setPower(1);
            if (gamepad2.x)
                winchMotor.setPower(-1);
            winchMotor.setPower(0);
        }

    }
}
