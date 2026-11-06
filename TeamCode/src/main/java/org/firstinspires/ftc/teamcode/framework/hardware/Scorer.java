package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
public class Scorer {
    public static long SHOOT_MS = 1000;
    
    private final Roller intake;
    private final Roller transfer;
    private final Shooter shooterPollen, shooterNectar; // TODO: implement shooter
    private Mode mode = Mode.IDLE;
    private long shootStartTime = 0L;
    
    public Scorer(Roller intake, Roller transfer, Shooter shooterPollen, Shooter shooterNectar) {
        this.intake = intake;
        this.transfer = transfer;
        this.shooterPollen = shooterPollen;
        this.shooterNectar = shooterNectar;
    }
    
    public Mode mode() {
        return mode;
    }
    
    public void update() {
        trackIntake();
        switch (mode) {
            case IDLE:
                intake.off();
                transfer.off();
                break;
                
                
            case INTAKE:
                intake.on();
                transfer.on();
                // Shooter.off()
                
                mode = Mode.IDLE; // transient
                break;
                
                
            case SHOOT:
                if (System.currentTimeMillis() - shootStartTime < SHOOT_MS) {
                    intake.on();
                    transfer.on();
                    // Shooter.on()
                } else { // time is up -- go idle
                    Storage.balls = 0;
                    mode = Mode.IDLE;
                }
                break;
                
                
            case EJECT:
                intake.reverse();
                transfer.reverse();
                
                mode = Mode.IDLE; // transient
                break;
        }
    }
    
    public void intakeSustain() {
        if (mode != Mode.SHOOT && Storage.balls < 4) {
            mode = Mode.INTAKE;
        }
    }
    
    public void shootStart() {
        if (Storage.balls > 0) {
            mode = Mode.SHOOT;
            shootStartTime = System.currentTimeMillis();
        }
    }
    
    public void ejectSustain() {
        if (mode != Mode.SHOOT && Storage.balls > 0) {
            mode = Mode.EJECT;
        }
    }
    
    private void trackIntake() {
        if (/*intakeSensor.justUnblocked()*/ false) {
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
