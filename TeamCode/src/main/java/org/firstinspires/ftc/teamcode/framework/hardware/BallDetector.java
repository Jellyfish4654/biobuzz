package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Configurable
public class BallDetector {
    public static double DEBOUNCE_MS = 10; //TODO: Tune debounce time

    private final DigitalChannel beamBreak;
    private boolean prevRaw = true;
    private long prevTime;
    
    // ↓ final values for if the beam sensor is broken on this and last iteration
    private boolean broken = false;
    private boolean prevBroken = false;

    public BallDetector(DigitalChannel sensor) {
        beamBreak = sensor;
        prevTime = System.currentTimeMillis();
    }

    public void update() {
        // if state changes, reset timer
        if (beamBreak.getState() != prevRaw) {
            prevTime = System.currentTimeMillis();
        }
        // update blocked if enough time has elapsed
        if (System.currentTimeMillis() - prevTime > DEBOUNCE_MS) {
            prevBroken = broken;
            broken = !beamBreak.getState();
        }
        prevRaw = beamBreak.getState();
    }
    
    public boolean isBlocked() {
        return broken;
    }

    public boolean justBlocked() {
        boolean ret = isBlocked() && !prevBroken;
        prevBroken = isBlocked();
        return ret;
    }

    public boolean justUnblocked() {
        boolean ret =  !isBlocked() && prevBroken;
        prevBroken  = isBlocked();
        return ret;
    }
}
