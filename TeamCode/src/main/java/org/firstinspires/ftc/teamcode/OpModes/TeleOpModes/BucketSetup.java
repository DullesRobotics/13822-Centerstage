package org.firstinspires.ftc.teamcode.OpModes.TeleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class BucketSetup extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Servo bucket = hardwareMap.get(Servo.class, "BUCKET");
        telemetry.addData("Hardware", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            while(gamepad2.x) {
                bucket.setPosition(1);
            }

            while (gamepad2.y){
                bucket.setPosition(0);
            }

            telemetry.addData("Position", bucket.getPosition());
            telemetry.update();

        }
    }
}
