package org.firstinspires.ftc.teamcode.framework.hardware;

import org.firstinspires.ftc.teamcode.framework.Storage;

public class Scorer {
    public static long SHOOT_MS = 1000;
    private final Intake intake;
    private final Transfer transfer;
//    private final Shooter shooterPollen, shooterNectar; // TODO: implement shooter
    private Mode mode = Mode.IDLE;
    private long shootStartTime = 0L;
    
    public Scorer(Intake intake, Transfer transfer) {
        this.intake = intake;
        this.transfer = transfer;
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
                mode = Mode.IDLE;
                break;
            case SHOOT:
                if (System.currentTimeMillis() - shootStartTime < SHOOT_MS) {
                    intake.on();
                    transfer.on();
                    // shooters
                } else {
                    Storage.balls = 0;
                    mode = Mode.IDLE;
                }
                break;
            case EJECT:
                intake.reverse();
                transfer.reverse();
                mode = Mode.IDLE;
                break;
        }
    }
    
    public void intake() {
        if (Storage.balls < 4) {
            mode = Mode.INTAKE;
        }
    }
    
    public void startShoot() {
        if (Storage.balls > 0) {
            mode = Mode.SHOOT;
            shootStartTime = System.currentTimeMillis();
        }
    }
    
    public void eject() {
        if (Storage.balls > 0) {
            mode = Mode.EJECT;
        }
    }
    
    public void trackIntake() {
        if (/*intakeSensor.justOpened()*/ false) {
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
