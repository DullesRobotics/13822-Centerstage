package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
@Disabled
public class FrontIntakeTest extends OpMode {

    DcMotor intake;
    boolean on = false, forward = true, togglePressedUp = false, togglePressedDown = false;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class ,"INTAKE");
        telemetry.addData("Hardware:", "Initialized");
    }

    @Override
    public void loop() {

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
