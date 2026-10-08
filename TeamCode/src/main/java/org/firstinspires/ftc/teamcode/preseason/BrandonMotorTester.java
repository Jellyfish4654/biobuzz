package org.firstinspires.ftc.teamcode.preseason;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;


@Utility(name = "Brandon Motor Tester", description = "Toggles the power of \"motor\" when \"touchSensor\" is touched.")
public class BrandonMotorTester extends LinearOpMode {
    DcMotor motor; // Motor Object
    private static final double power = 1;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "motor");

        boolean toggle = false;
        boolean lastPressed = false;

        // wait for the start button to be pressed.
        waitForStart();

        // while the OpMode is active, loop and read whether the sensor is being pressed.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            if (gamepad1.a) {
                // send the info back to driver station using telemetry function.
                if (!lastPressed) {
                    if (!toggle) {
                        motor.setPower(power);
                    }
                    else {
                        motor.setPower(0);
                    }
                    // motor switches on -> off or off -> on.
                    toggle = !toggle;
                }
                lastPressed = true;
            } else {
                lastPressed = false;
            }

            telemetry.update();
        }
    }
}