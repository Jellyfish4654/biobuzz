package org.firstinspires.ftc.teamcode.framework;

//import com.bylazar.telemetry.JoinedTelemetry;
//import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class BaseOpMode extends OpMode {
//    protected Follower drivetrain; // Pedro!
//    protected Controls controls;
    
    private long prevLoopNanoTime = 0;
    private boolean alertedEndgame = false;
    
    // TODO: make sure nothing moves during auto → teleop transition
    // TODO: this gets overridden for auto, calling super
    @Override
    public void init() {
        // Bulk caching for hubs will hopefully improve performance!
        // TODO: Try manual caching if needed for ultra performance
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        
//        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
//        controls = new Controls(gamepad1, gamepad2);
    }
    
    @Override
    public void init_loop() {
        telemetry.addLine("Status: Init Finished  --------------------------------------------");
        for (int i=0; i<12; i++) {
            telemetry.addLine("---------------------------------------------------------------------------");
        }
        timingTelemetry();
    }
    
    // TODO: this gets overridden for auto, calling super, to schedule the routine
    @Override
    public void start() {
        resetRuntime();
    }
    
    // TODO: this gets overridden for all teleop and auto, calling super
    @Override
    public void loop() {
        timingTelemetry();
        // Manual bulk caching would go here
    }
    
    @Override
    public void stop() {
        // Add any shutdown or hardware stopping procedures here
    }
    
    protected void timingTelemetry() {
        // loop speed
        long currentNanoTime = System.nanoTime();
        long nanoPerLoop = currentNanoTime - prevLoopNanoTime;
        double hz = (nanoPerLoop > 0) ? 1e9 / nanoPerLoop : 0;
        prevLoopNanoTime = currentNanoTime;
        
        telemetry.addLine("\nLoop Timing:");
        telemetry.addData("\tMillis", "%.2f", nanoPerLoop / 1e6);
        telemetry.addData("\tHz", "%.2f", hz);
        
        // runtime and alerts
        telemetry.addData("\tRuntime", (int) getRuntime());
        if (getRuntime() >= 110 && !alertedEndgame) {
            alertedEndgame = true;
//            controls.megaRumble();
        }
    }
}