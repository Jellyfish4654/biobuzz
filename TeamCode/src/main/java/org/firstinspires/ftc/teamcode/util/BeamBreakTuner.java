package org.firstinspires.ftc.teamcode.util;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.framework.scoring.BeamBreak;

@Utility(name = "Beam Break Tuner", description = "Logs the state of Digital Channel \"intake_sensor\" to telemetry.")
public class BeamBreakTuner extends OpMode {
    private DigitalChannel digitalInput;
    private BeamBreak beamBreak;
    private int amtOfBalls = 0;
    
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        telemetry.setMsTransmissionInterval(1);
        
        digitalInput = hardwareMap.get(DigitalChannel.class, "intake_sensor");
        beamBreak = new BeamBreak(digitalInput);
    }
    
    @Override
    public void loop() {
        beamBreak.update();
        telemetry.addData("Raw State", digitalInput.getState() ? -1 : -2);
        telemetry.addData("Debounce Blocked", beamBreak.isBroken() ? -4 : -3);
        telemetry.addData("Amt of Balls", amtOfBalls);
        
        if (beamBreak.justUnbroke()) {
            amtOfBalls++;
            telemetry.addData("Just unblocked", -5);
        } else {
            telemetry.addData("Just unblocked", -6);
        }
        
        if (beamBreak.justBroke()) {
            amtOfBalls++;
            telemetry.addData("Just blocked", -7);
        } else {
            telemetry.addData("Just blocked", -8);
        }
    }
}
