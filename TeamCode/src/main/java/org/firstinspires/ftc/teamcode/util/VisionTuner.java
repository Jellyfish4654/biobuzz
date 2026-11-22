package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.framework.scoring.Vision;

@Utility(name = "Vision Tuner", description = "Tests if Limelight \"limelight\" can detect AprilTags")
@Configurable
public class VisionTuner extends OpMode {
    public static int[] TARGETS = {1, 2, 3, 4};
    private Vision vision;
    
    //private int[] pastTargets = Arrays.copyOf(TARGETS,4);
    @Override
    public void init() {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        telemetry.setMsTransmissionInterval(1);

        vision = new Vision(hardwareMap.get(Limelight3A.class, "limelight"));
        vision.start();
    }

    @Override
    public void loop() {
        LLStatus status = vision.getStatus();
        if (status == null) {
            telemetry.addLine("Disconnected");
        } else {
            telemetry.addData("Visible Tags", vision.getVisibleTagIds());
            telemetry.addData("Match", vision.areTagsVisible(TARGETS));
            telemetry.addLine(vision.getStatus().toString());
        }
        
//        if(!Arrays.equals(pastTargets, TARGETS)){ // Idk what this does so feel free to add it back
//            vision.pause();
//            vision.start(TARGETS);
//            pastTargets = Arrays.copyOf(TARGETS,4);;
//        }
    }
}
