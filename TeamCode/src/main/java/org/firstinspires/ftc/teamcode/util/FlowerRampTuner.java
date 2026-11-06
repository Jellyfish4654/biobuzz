package org.firstinspires.ftc.teamcode.util;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.hardware.FlowerRamp;

public class FlowerRampTuner extends OpMode {
    private Servo servo;
    private FlowerRamp flowerRamp;
    
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        servo = hardwareMap.get(Servo.class, "flower_ramp");
        flowerRamp = new FlowerRamp(servo);
    }
    
    @Override
    public void loop() {
        telemetry.addData("Position: ", servo.getPosition());
        
        if (gamepad1.dpad_down) {
            flowerRamp.down();
        } else if (gamepad1.dpad_up) {
            flowerRamp.up();
        }
    }
}
