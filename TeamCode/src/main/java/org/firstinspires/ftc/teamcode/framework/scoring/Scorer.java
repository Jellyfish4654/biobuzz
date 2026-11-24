package org.firstinspires.ftc.teamcode.framework.scoring;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
public class Scorer {
    public static long SHOOT_MS = 1000;
    
    private final Roller intake;
    private final BeamBreak intakeSensor;
    public final ServoArm flowerRamp; // only directly accessed by OpModes
    private final Roller transfer;
    private final ServoArm blockerNectar;
    private final ServoArm blockerPollen;
    private final Shooter shooterPollen, shooterNectar; // TODO: work on shooter, servo blockers
    private Mode mode = Mode.IDLE;
    private long shootStartTime = 0L;
    
    public Scorer(
            Roller intake, BeamBreak intakeSensor, ServoArm flowerRamp,
            Roller transfer, Shooter shooterPollen, Shooter shooterNectar, ServoArm blockerPollen, ServoArm blockerNectar
    ) {
        this.intake = intake;
        this.intakeSensor = intakeSensor;
        this.flowerRamp = flowerRamp;
        this.transfer = transfer;
        this.shooterPollen = shooterPollen;
        this.shooterNectar = shooterNectar;
        this.blockerNectar = blockerNectar;
        this.blockerPollen = blockerPollen;
    }
    
    public Mode mode() {
        return mode;
    }
    
    public void update(Pose robotPose) {
        trackIntake();
        switch (mode) {
            case IDLE: {
                intake.off();
                transfer.off();
                blockerPollen.down();
                blockerNectar.down();
                shooterPollen.off();
                shooterNectar.off();
                break;
            }
            case INTAKE: {
                intake.on();
                transfer.on();
                blockerPollen.down();
                blockerNectar.down();
                shooterPollen.off();
                shooterNectar.off();
                
                mode = Mode.IDLE; // transient
                break;
            }
            case SHOOT: {
                if (System.currentTimeMillis() - shootStartTime < SHOOT_MS) {
                    double shootDist = shootDistance(robotPose);
                    intake.on();
                    transfer.on();
                    blockerPollen.up();
                    blockerNectar.up();
                    shooterPollen.on(shootDist);
                    shooterNectar.on(shootDist);
                } else { // time is up -- go idle
                    Storage.balls = 0;
                    mode = Mode.IDLE;
                }
                break;
            }
            case EJECT: {
                intake.reverse();
                transfer.reverse();
                blockerPollen.up();
                blockerNectar.up();
                shooterPollen.off();
                shooterNectar.off();
                
                mode = Mode.IDLE; // transient
                break;
            }
        }
    }
    
    public void intakeSustain() {
        if (mode != Mode.SHOOT && Storage.balls < 4) {
            mode = Mode.INTAKE;
        }
    }
    
    public void shootStart() {
        if (mode != Mode.SHOOT && Storage.balls > 0) {
            mode = Mode.SHOOT;
            shootStartTime = System.currentTimeMillis();
        }
    }
    
    public void ejectSustain() {
        if (mode != Mode.SHOOT) {
            mode = Mode.EJECT;
        }
    }
    
    public void emergencyStop() {
        Storage.balls = 0;
        mode = Mode.IDLE;
    }
    
    // horizontal distance to the AprilTag cluster on the nearest alliance-specific cell, when tipped up
    private double shootDistance(Pose robotPose) {
        double distAudience;
        double distScoring;
        if (Storage.alliance == Storage.Alliance.BLUE) {
            distAudience = robotPose.distance(new Pose(57.7, 57.54));
            distScoring = robotPose.distance(new Pose(57.7, 83.92));
        } else { // defaults to red hive
            distAudience = robotPose.distance(new Pose(84.09, 57.54));
            distScoring = robotPose.distance(new Pose(84.09, 83.92));
        }
        
        return Math.min(distAudience, distScoring);
    }
    
    private void trackIntake() {
        intakeSensor.update();
        if (intakeSensor.justUnbroke()) {
            if (mode == Mode.INTAKE || mode == Mode.SHOOT) {
                Storage.balls++;
            } else if (Storage.balls > 0) {
                Storage.balls--;
            }
        }
    }
    
    public enum Mode {
        IDLE,
        INTAKE,
        SHOOT,
        EJECT
    }
}
