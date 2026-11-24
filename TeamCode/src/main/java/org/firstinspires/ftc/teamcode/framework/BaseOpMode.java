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
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

import org.firstinspires.ftc.teamcode.framework.pedro.Constants;
import org.firstinspires.ftc.teamcode.framework.scoring.BeamBreak;
import org.firstinspires.ftc.teamcode.framework.scoring.Roller;
import org.firstinspires.ftc.teamcode.framework.scoring.ServoArm;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;
import org.firstinspires.ftc.teamcode.framework.scoring.Shooter;

import java.util.function.DoubleSupplier;

public abstract class BaseOpMode extends OpMode {
    protected DoubleSupplier voltage, current;
    protected ControlMap controls;
    protected Follower follower;
    protected Scorer scorer;
    
    // TODO: make sure nothing moves during auto → teleop transition
    @Override
    public void init() { // runs before extended
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        LynxModule controlHub = hardwareMap.get(LynxModule.class, "Control Hub");
        voltage = ()-> controlHub.getInputVoltage(VoltageUnit.VOLTS);
        current = ()-> controlHub.getCurrent(CurrentUnit.AMPS);
        
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        telemetry.setCaptionValueSeparator(": ");
        telemetry.setMsTransmissionInterval(250);
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        
        controls = new ControlMap(gamepad1, gamepad2);
        
        follower = Constants.createFollower(hardwareMap);
        
//        scorer = new Scorer(
//                new Roller(hardwareMap.get(DcMotor.class, "intake"), DcMotor.Direction.FORWARD, ()-> Roller.INTAKE_POWER),
//                new BeamBreak(hardwareMap.get(DigitalChannel.class, "intake_sensor")),
//                new ServoArm(hardwareMap.get(Servo.class, "flower_ramp"), Servo.Direction.FORWARD, ()-> ServoArm.FLOWER_POS_UP, ()-> ServoArm.FLOWER_POS_DOWN),
//                new Roller(hardwareMap.get(DcMotor.class, "transfer"), DcMotor.Direction.FORWARD, ()-> Roller.TRANSFER_POWER),
//                new Shooter(
//                        hardwareMap.get(DcMotorEx.class, "pollen_motor"), DcMotorEx.Direction.FORWARD, ()-> Shooter.POLLEN_KV, ()-> Shooter.POLLEN_KP,
//                        hardwareMap.get(Servo.class, "pollen_hood"), Servo.Direction.FORWARD, ()-> Shooter.POLLEN_HOOD_POS_MIN, ()-> Shooter.POLLEN_HOOD_POS_MAX,
//                        voltage
//                ),
//                new Shooter(
//                        hardwareMap.get(DcMotorEx.class, "nectar_motor"), DcMotorEx.Direction.FORWARD, ()-> Shooter.NECTAR_KV, ()-> Shooter.NECTAR_KP,
//                        hardwareMap.get(Servo.class, "nectar_hood"), Servo.Direction.FORWARD, ()-> Shooter.NECTAR_HOOD_POS_MIN, ()-> Shooter.NECTAR_HOOD_POS_MAX,
//                        voltage
//                ),
//                new ServoArm(hardwareMap.get(Servo.class,"pollen_blocker"), Servo.Direction.FORWARD, ()-> ServoArm.POLLEN_POS_UP, ()-> ServoArm.POLLEN_POS_DOWN),
//                new ServoArm(hardwareMap.get(Servo.class,"nectar_blocker"), Servo.Direction.FORWARD, ()-> ServoArm.NECTAR_POS_UP, ()-> ServoArm.NECTAR_POS_DOWN)
//        );
    }
    
    @Override
    public void init_loop() {
        telemetry.addLine("<b><font color='green'>INIT FINISHED</font></b>");
        utilityTelemetry();
    }
    
    @Override
    public void start() { // runs before extended
        resetRuntime();
        alertedEndgame = false;
    }
    
    @Override
    public void loop() { // runs after extended
        follower.update();
        Storage.poseBackup = follower.pose();
        //scorer.update(follower.pose());
        
//        telemetry.addLine("\n<b>Scorer:</b>");
//        telemetry.addData("\tMode", scorer.mode());
//        telemetry.addData("\tBalls", Storage.balls);
//        telemetry.addData("\tFlower Ramp Down", scorer.flowerRamp.isDown());
        
        // TODO: determine how much of this is useful
        telemetry.addLine("\n<b>Follower:</b>");
        telemetry.addData("\tMode", follower.mode());
        telemetry.addData("\tPose X", "%.2f", follower.pose().x());
        telemetry.addData("\tPose Y", "%.2f", follower.pose().y());
        telemetry.addData("\tHeading", "%.2f", Math.toDegrees(follower.pose().heading()));
//        telemetry.addLine("\t" + follower.velocity());
//        telemetry.addLine("\t" + follower.twist());
        
        
        if (follower.isBusy()) {
            telemetry.addData("\t\tTarget Pose", follower.poseAt(1));
            if (follower.following()) {
                telemetry.addData("\t\tPath Index", follower.pathIndex());
                telemetry.addData("\t\tPath Completion", "%.4f", follower.completion());
            }
        }
        
        utilityTelemetry();
    }
    
    @Override
    public void stop() { // runs before extended
        follower.stop();
        follower.update();
        Storage.poseBackup = follower.pose();
        
//        scorer.emergencyStop();
//        scorer.update(follower.pose());
    }
    
    private long prevLoopNanoTime = 0;
    private boolean alertedEndgame = false;
    protected void utilityTelemetry() {
        telemetry.addLine("\n<b>Utility:</b>");
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
        
        telemetry.addData("\tCurrent", current.getAsDouble());
    }
}
