package org.firstinspires.ftc.teamcode.preseason;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;


@Utility (name = "Brandon Motor Tester", description = "Toggles the power of \"motor\" when \"touchSensor\" is touched.")
public class BrandonMotorTester extends LinearOpMode {
    TouchSensor touchSensor;  // Touch sensor Object
    DcMotor motor; // Motor Object
    private static final double power = 0.3;

    @Override
    public void runOpMode() {

        touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");
        motor = hardwareMap.get(DcMotor.class, "motor");

        boolean toggle = false;
        boolean lastPressed = false;

        // wait for the start button to be pressed.
        waitForStart();

        // while the OpMode is active, loop and read whether the sensor is being pressed.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            if (touchSensor.isPressed()) {
                // send the info back to driver station using telemetry function.
                telemetry.addData("Touch Sensor", "Is Pressed");
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
                telemetry.addData("Touch Sensor", "Is Not Pressed");
                lastPressed = false;
            }

            telemetry.update();
        }
    }
}
