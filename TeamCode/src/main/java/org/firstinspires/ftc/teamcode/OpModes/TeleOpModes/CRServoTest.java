package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
@Disabled
public class CRServoTest extends OpMode {
    public CRServo right;
    public CRServo left;

    @Override
    public void init() {
        right = hardwareMap.get(CRServo.class, "RIGHT");
        left = hardwareMap.get(CRServo.class, "LEFT");
    }

    @Override
    public void loop() {
        boolean on = false, forward = true, togglePressedUp = false, togglePressedDown = false;

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

        while(on && forward){
            right.setPower(1);
            left.setPower(-1);
        }
        while(on && !forward){
            right.setPower(-1);
            left.setPower(1);
        }
        right.setPower(0);
        left.setPower(0);

    }
}
