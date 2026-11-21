package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.framework.scoring.BeamBreak;
import org.firstinspires.ftc.teamcode.framework.scoring.Vision;

import java.util.Arrays;

@Utility(name = "Vision Tuner", description = "Tests if the LL can scan apriltags")
@Configurable
public class VisionTuner extends OpMode {
    private Vision vision;

    public static int[] TARGETS = new int[]{1,2,3,4};
    private int[] pastTargets = Arrays.copyOf(TARGETS,4);
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        telemetry.setMsTransmissionInterval(1);

        vision = new Vision(hardwareMap.get(Limelight3A.class, "limelight"));
        vision.start(pastTargets);
    }

    @Override
    public void loop() {
        telemetry.addData("is gut? ",vision.tagsFound());
        telemetry.addLine(vision.targets.toString());
        if(!Arrays.equals(pastTargets, TARGETS)){
            vision.pause();
            vision.start(TARGETS);
            pastTargets = Arrays.copyOf(TARGETS,4);;
        }
    }
}