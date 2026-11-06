package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.hardware.FlowerRamp;

public class FlowerRampTuner extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final Servo flowerServo;
        flowerServo = hardwareMap.get(Servo.class, "flowerServo");
        FlowerRamp flowerRamp = new FlowerRamp(flowerServo);
        double position = flowerRamp.currAngle;

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("current position: ", flowerRamp.currAngle);
            telemetry.update();

            flowerRamp.setAngle(position);
            if (gamepad1.dpad_left)
            {
                position -= 0.0005;
            }
            if (gamepad1.dpad_right)
            {
                position += 0.0005;
            }
        }
    }
}
