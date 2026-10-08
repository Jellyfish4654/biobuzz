package org.firstinspires.ftc.teamcode.preseason;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Utility(name = "Jamie Motor Tester", description = "Toggles the power of \"test_motor\" when \"test_sensor\" is touched.")
public class JamieMotorTester extends LinearOpMode {
    private static final double POWER = 1;

    public void runOpMode() {
        DcMotor motor = hardwareMap.get(DcMotor.class, "test_motor");
        waitForStart();

        boolean currentTouchState;
        boolean prevTouchState = false;
        boolean status = false;

        while (opModeIsActive()) {
            currentTouchState = gamepad1.a; 

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