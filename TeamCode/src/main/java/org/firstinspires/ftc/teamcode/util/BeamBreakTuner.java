package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Utility(name = "Beam Break Tuner", description = "Logs the state of Digital Channel \"intake_sensor\" to telemetry.")
public class BeamBreakTuner extends OpMode {
    private DigitalChannel beamBrake;
    
    @Override
    public void init() {
        beamBrake = hardwareMap.get(DigitalChannel.class, "intake_sensor");
    }
    
    @Override
    public void loop() {
        telemetry.addData("State", beamBrake.getState());
    }
}
