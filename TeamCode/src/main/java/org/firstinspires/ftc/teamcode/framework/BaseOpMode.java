package org.firstinspires.ftc.teamcode.framework;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.framework.pedro.Constants;

public abstract class BaseOpMode extends OpMode {
    protected Follower follower;
    protected ControlMap controls;
    
    // TODO: make sure nothing moves during auto → teleop transition
    @Override
    public void init() {
        // Bulk caching for hubs will hopefully improve performance!
        // TODO: Try manual caching if needed for ultra performance
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        
        // TODO: telemetry works?
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        
        follower = Constants.createFollower(hardwareMap);
        
        controls = new ControlMap(gamepad1, gamepad2);
    }
    
    @Override
    public void init_loop() {
        telemetry.addLine("Status: Init Finished  --------------------------------------------");
        for (int i=0; i<12; i++) {
            telemetry.addLine("---------------------------------------------------------------------------");
        }
        timingTelemetry();
    }
    
    @Override
    public void start() {
        resetRuntime();
    }
    
    @Override
    public void loop() {
        follower.update();
        Storage.poseBackup = follower.pose();
        
        // TODO: determine how much of this is useful
        telemetry.addLine("\nFollower:");
        telemetry.addData("\tPose X", "%.2f", follower.pose().x());
        telemetry.addData("\tPose Y", "%.2f", follower.pose().x());
        telemetry.addData("\tPose H", "%.2f", Math.toDegrees(follower.pose().heading()));
        telemetry.addLine("\t" + follower.velocity());
        telemetry.addLine("\t" + follower.twist());
        
        telemetry.addData("\tMode", follower.mode());
        if (follower.isBusy()) {
            telemetry.addData("\tTarget Pose", follower.poseAt(1));
            if (follower.following()) {
                telemetry.addData("\tPath Index", follower.pathIndex());
                telemetry.addData("\tPath Completion", "%.4f", follower.completion());
            }
        }
        
        timingTelemetry();
        // Manual bulk caching would go here
    }
    
    @Override
    public void stop() {
        follower.stop();
        follower.update();
        Storage.poseBackup = follower.pose();
    }
    
    private long prevLoopNanoTime = 0;
    private boolean alertedEndgame = false;
    protected void timingTelemetry() {
        // loop speed
        long currentNanoTime = System.nanoTime();
        long nanoPerLoop = currentNanoTime - prevLoopNanoTime;
        double hz = (nanoPerLoop > 0L) ? 1e9 / nanoPerLoop : 0;
        prevLoopNanoTime = currentNanoTime;
        
        telemetry.addLine("\nTiming:");
        telemetry.addData("\tLoop Ms", "%.2f", nanoPerLoop / 1e6);
        telemetry.addData("\tLoop Hz", "%.2f", hz);
        
        // runtime and alerts
        telemetry.addData("\tRuntime", "%.2f", getRuntime());
        if (getRuntime() >= 110 && !alertedEndgame) {
            alertedEndgame = true;
            controls.megaRumble();
        }
    }
}
