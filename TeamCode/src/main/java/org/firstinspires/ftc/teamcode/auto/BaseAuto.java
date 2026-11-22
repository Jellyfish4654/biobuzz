package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.Vision;

public abstract class BaseAuto extends BaseOpMode {
    // TODO: figure out the cleanest way to do this
    protected abstract PoseFactory allianceBasedSetup();
    protected abstract Pose getStartPose();
    protected abstract Command fullRoutine();
    protected abstract int[] getTags();
    private Command routine;
    //vision is only here because we don't need it (as of now) in teleop
    protected Vision vision;
    protected final PoseFactory poseFactory = allianceBasedSetup();
    
    @Override
    public void init() {
        super.init();

        vision = new Vision(hardwareMap.get(Limelight3A.class,"limelight"));

        follower.setPose(getStartPose());
        follower.update();
        
        Scheduler.reset();
        routine = fullRoutine();
    }
    
    @Override
    public void start() {
        super.start();
        
        Scheduler.schedule(routine);
    }
    
    @Override
    public void loop() {
        Scheduler.execute();
        if (!Scheduler.isRunning(routine)) {
            if (Scheduler.isScheduled(routine)) {
                telemetry.addLine("\n<b><font color='red'>ERROR: Ivy routine scheduled, not running</font></b>");
            } else {
                requestOpModeStop();
            }
        }
        
        super.loop();
    }
    
    @Override
    public void stop() {
        super.stop();
        
        Scheduler.cancel(routine);
        Scheduler.reset();
    }
}
