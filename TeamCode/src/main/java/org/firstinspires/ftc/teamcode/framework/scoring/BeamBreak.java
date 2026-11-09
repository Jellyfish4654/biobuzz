package org.firstinspires.ftc.teamcode.framework.scoring;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Configurable
public class BeamBreak {
    public static long DEBOUNCE_NS = 10_000_000L; //TODO: Tune debounce time

    private final DigitalChannel digitalInput;
    
    private boolean rawBrokenPrevUpdate;
    private long rawLastChangedTime;
    
    // ↓ debounced values for if the beam sensor is broken on this and previous update
    private boolean debouncedBroken;
    private boolean debouncedBrokenPrevUpdate;

    public BeamBreak(DigitalChannel digitalInput) {
        this.digitalInput = digitalInput;
        this.digitalInput.setMode(DigitalChannel.Mode.INPUT);
        
        rawBrokenPrevUpdate = !this.digitalInput.getState();
        debouncedBroken = rawBrokenPrevUpdate;
        debouncedBrokenPrevUpdate = debouncedBroken;
        
        rawLastChangedTime = System.nanoTime();
    }

    public void update() {
        boolean rawBrokenCurrent = !digitalInput.getState();
        
        // if state changed, reset timer
        if (rawBrokenCurrent != rawBrokenPrevUpdate) {
            rawLastChangedTime = System.nanoTime();
            rawBrokenPrevUpdate = rawBrokenCurrent;
        }
        
        // save the current debounced to be the previous state first, then potentially update current
        debouncedBrokenPrevUpdate = debouncedBroken;
        // if raw is different from debounced and hasn't changed recently, update current
        if (rawBrokenCurrent != debouncedBroken && System.nanoTime() - rawLastChangedTime > DEBOUNCE_NS) {
            debouncedBroken = rawBrokenCurrent;
        }
    }
    
    public boolean isBroken() {
        return debouncedBroken;
    }

    public boolean justBroke() {
        return isBroken() && !debouncedBrokenPrevUpdate;
    }

    public boolean justUnbroke() {
        return !isBroken() && debouncedBrokenPrevUpdate;
    }
}
