package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;

public abstract class BaseAuto extends BaseOpMode {
    // TODO: figure out the cleanest way to do this
    protected abstract PoseFactory allianceBasedSetup();
    protected abstract Pose getStartPose();
    protected abstract Command fullRoutine();
    private Command routine;
    private final PoseFactory poseFactory = allianceBasedSetup();
    
    @Override
    public void init() {
        super.init();
        
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
        telemetry.addLine("\nScheduler:");
        telemetry.addData("Scheduled", Scheduler.isScheduled(routine));
        telemetry.addData("Running", Scheduler.isRunning(routine));
        
        super.loop();
    }
    
    @Override
    public void stop() {
        super.stop();
        
        Scheduler.cancel(routine);
        Scheduler.reset();
    }
}
