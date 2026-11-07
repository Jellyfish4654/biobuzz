package org.firstinspires.ftc.teamcode.util;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.framework.hardware.BallDetector;

@Utility(name = "Beam Break Tuner", description = "Logs the state of Digital Channel \"intake_sensor\" to telemetry.")
public class BeamBreakTuner extends OpMode {
    private DigitalChannel beamBrake;
    private BallDetector detector;
    private int amtOfBalls = 0;
    
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        beamBrake = hardwareMap.get(DigitalChannel.class, "intake_sensor");
        detector = new BallDetector(beamBrake);
    }
    
    @Override
    public void loop() {
        detector.update();
        telemetry.addData("State", beamBrake.getState());
        if(detector.justUnblocked()){
            amtOfBalls++;
            telemetry.addData("Just unblocked", 1);
        }else{
            telemetry.addData("Just unblocked", 0);
        }

        telemetry.addData("Debounce State", detector.isBlocked());
        telemetry.addData("Amt of Balls", amtOfBalls);
        telemetry.update();

    }
}
