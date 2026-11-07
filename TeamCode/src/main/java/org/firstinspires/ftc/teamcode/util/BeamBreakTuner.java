package org.firstinspires.ftc.teamcode.util;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.framework.scoring.BallDetector;

@Utility(name = "Beam Break Tuner", description = "Logs the state of Digital Channel \"intake_sensor\" to telemetry.")
public class BeamBreakTuner extends OpMode {
    private DigitalChannel beamBrake;
    private BallDetector detector;
    private int amtOfBalls = 0;
    
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        telemetry.setMsTransmissionInterval(1);
        
        beamBrake = hardwareMap.get(DigitalChannel.class, "intake_sensor");
        detector = new BallDetector(beamBrake);
    }
    
    @Override
    public void loop() {
        detector.update();
        telemetry.addData("Raw State", beamBrake.getState() ? 1 : 0);
        telemetry.addData("Debounce Blocked", detector.isBlocked() ? 1 : 0);
        telemetry.addData("Amt of Balls", amtOfBalls);
        
        if (detector.justUnblocked()) {
            amtOfBalls++;
            telemetry.addData("Just unblocked", 1);
        } else {
            telemetry.addData("Just unblocked", 0);
        }
        
        if (detector.justBlocked()) {
            amtOfBalls++;
            telemetry.addData("Just blocked", 1);
        } else {
            telemetry.addData("Just blocked", 0);
        }
    }
}
