package org.firstinspires.ftc.teamcode.preseason;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class JamieMotorTester extends LinearOpMode {
    private TouchSensor touchSensor;
    private static final double POWER = 0.3;

    public void runOpMode() {
        DcMotor motor = hardwareMap.get(DcMotor.class, "test_motor");
        waitForStart();

        boolean currentTouchState;
        boolean prevTouchState = false;

        while (opModeIsActive()){
            currentTouchState = touchSensor.isPressed();
            if (currentTouchState && !prevTouchState) {
                motor.setPower(POWER);
            } else if (!currentTouchState && prevTouchState ){
                motor.setPower(0);
            }
            prevTouchState = currentTouchState;
        }
    }
}

