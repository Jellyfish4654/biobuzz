package org.firstinspires.ftc.teamcode.framework;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.scoring.BallDetector;
import org.firstinspires.ftc.teamcode.framework.scoring.FlowerRamp;
import org.firstinspires.ftc.teamcode.framework.scoring.Roller;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;
import org.firstinspires.ftc.teamcode.framework.scoring.Shooter;
import org.firstinspires.ftc.teamcode.framework.pedro.Constants;

public abstract class BaseOpMode extends OpMode {
    protected Follower follower;
    protected Scorer scorer;
    protected ControlMap controls;
    
    // TODO: make sure nothing moves during auto → teleop transition
    @Override
    public void init() { // runs before extended
        // Bulk caching for hubs will hopefully improve performance!
        // TODO: Try manual caching if needed for ultra performance
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        
        // TODO: telemetry works?
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        
        follower = Constants.createFollower(hardwareMap);
        
//        scorer = new Scorer(
//                new Roller(hardwareMap.get(DcMotor.class, "intake"), DcMotor.Direction.FORWARD, () -> Roller.INTAKE_POWER),
//                new BallDetector(hardwareMap.get(DigitalChannel.class, "intake_sensor")),
//                new FlowerRamp(hardwareMap.get(Servo.class, "flower_ramp")),
//                new Roller(hardwareMap.get(DcMotor.class, "transfer"), DcMotor.Direction.FORWARD, () -> Roller.TRANSFER_POWER),
//                new Shooter(
//                        hardwareMap.get(DcMotorEx.class, "pollen_motor"), DcMotorEx.Direction.FORWARD,
//                        hardwareMap.get(Servo.class, "pollen_hood"), Servo.Direction.FORWARD, 0, 1
//                ),
//                new Shooter(
//                        hardwareMap.get(DcMotorEx.class, "nectar_motor"), DcMotorEx.Direction.FORWARD,
//                        hardwareMap.get(Servo.class, "nectar_hood"), Servo.Direction.FORWARD, 0, 1
//                )
//        );
        
        controls = new ControlMap(gamepad1, gamepad2);
    }
    
    @Override
    public void init_loop() {
        telemetry.addLine("Status: Init Finished  --------------------------------------------");
        for (int i=0; i<12; i++) {
            telemetry.addLine("---------------------------------------------------------------------------");
        }
        utilityTelemetry();
    }
    
    @Override
    public void start() { // runs before extended
        resetRuntime();
    }
    
    @Override
    public void loop() { // runs after extended
        follower.update();
        Storage.poseBackup = follower.pose();
        
        // TODO: determine how much of this is useful
        telemetry.addLine("\nFollower:");
        telemetry.addData("\tPose X", "%.2f", follower.pose().x());
        telemetry.addData("\tPose Y", "%.2f", follower.pose().x());
        telemetry.addData("\tPose H", "%.2f", Math.toDegrees(follower.pose().heading()));
//        telemetry.addLine("\t" + follower.velocity());
//        telemetry.addLine("\t" + follower.twist());
        
        telemetry.addData("\tMode", follower.mode());
        if (follower.isBusy()) {
            telemetry.addData("\t\tTarget Pose", follower.poseAt(1));
            if (follower.following()) {
                telemetry.addData("\t\tPath Index", follower.pathIndex());
                telemetry.addData("\t\tPath Completion", "%.4f", follower.completion());
            }
        }
        
        telemetry.addLine("\nScorer:");
        telemetry.addData("\tMode", scorer.mode());
        telemetry.addData("\tBalls", Storage.balls);
        telemetry.addData("\tFlower Ramp Down", scorer.flowerRamp.isDown());
        
        utilityTelemetry();
    }
    
    @Override
    public void stop() { // runs before extended
        follower.stop();
        follower.update();
        Storage.poseBackup = follower.pose();
    }
    
    private long prevLoopNanoTime = 0;
    private boolean alertedEndgame = false;
    protected void utilityTelemetry() {
        telemetry.addLine("\nUtility:");
        telemetry.addData("\tAlliance", Storage.alliance);
        
        // loop speed
        long currentNanoTime = System.nanoTime();
        long nanoPerLoop = currentNanoTime - prevLoopNanoTime;
        double hz = (nanoPerLoop > 0L) ? 1e9 / nanoPerLoop : 0;
        prevLoopNanoTime = currentNanoTime;
        
        telemetry.addData("\tLoop Ms", "%.2f", nanoPerLoop / 1e6);
        telemetry.addData("\tLoop Hz", "%.2f", hz);
        
        // runtime and alerts
        telemetry.addData("\tRuntime", (int) getRuntime());
        if (getRuntime() >= 110 && !alertedEndgame) {
            alertedEndgame = true;
            controls.megaRumble();
        }
    }
}
