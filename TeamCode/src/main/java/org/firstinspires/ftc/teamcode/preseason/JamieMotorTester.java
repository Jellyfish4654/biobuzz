package org.firstinspires.ftc.teamcode.preseason;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class JamieMotorTester extends LinearOpMode {
    private static final double POWER = 0.3;

    public void runOpMode() {
        DcMotor motor = hardwareMap.get(DcMotor.class, "test_motor");
        TouchSensor touchSensor = hardwareMap.get(TouchSensor.class,"test_sensor");
        waitForStart();

        boolean currentTouchState;
        boolean prevTouchState = false;
        boolean status = false;

        while (opModeIsActive()) {
            currentTouchState = touchSensor.isPressed();

            if (currentTouchState && !prevTouchState) {
                status = !status;
                if (status) {
                    motor.setPower(POWER);
                } else {
                    motor.setPower(0);
                }
            }
            prevTouchState = currentTouchState;
        }
    }
}

