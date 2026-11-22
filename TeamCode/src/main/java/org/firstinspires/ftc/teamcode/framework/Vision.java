package org.firstinspires.ftc.teamcode.framework;

import androidx.annotation.Nullable;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.bylazar.configurables.annotations.Configurable;

import java.util.HashSet;
import java.util.Set;

@Configurable
public class Vision {
    public static int TARGET_THRESHOLD = 3;
    public static int DEFAULT_PIPELINE = 0;
    public static int POLL_RATE_HZ = 100;
    public static long MAX_STALE_MILLIS = 30;
    
    private final Limelight3A limelight;
    
    private int currentPipeline;
    public Vision(Limelight3A limelight) {
        this.limelight = limelight;
        this.limelight.setPollRateHz(POLL_RATE_HZ);
        setPipeline(DEFAULT_PIPELINE);
    }
    
    public void setPipeline(int index) {
        limelight.pipelineSwitch(index);
        currentPipeline = index;
    }

    public void start(){
        limelight.start();
    }

    public void stop(){
        limelight.stop();
    }
    public void pause(){
        limelight.pause();
    }
    
    @Nullable
    public LLResult getValidResult() {
        if (limelight.isConnected() && limelight.isRunning()) {
            LLResult result = limelight.getLatestResult();
            if (result.isValid() && result.getPipelineIndex() == currentPipeline && result.getStaleness() < MAX_STALE_MILLIS) {
                return result;
            }
        }
        return null;
    }
    
    @Nullable
    public LLStatus getStatus() {
        if (limelight.isConnected()) {
            return limelight.getStatus();
        }
        return null;
    }
    
    public Set<Integer> getVisibleTagIds() {
        LLResult result = getValidResult();
        Set<Integer> ids = new HashSet<>();
        if (result != null) {
            result.getFiducialResults().forEach((t) -> ids.add(t.getFiducialId()));
        }
        return ids;
    }

    public boolean areTagsVisible(int[] targetIds) {
        int visibleTargets = 0;
        Set<Integer> tags = getVisibleTagIds();
        for (int target : targetIds) {
            if (tags.remove(target)) { // remove tag to prevent matching with a duplicate in targetIds
                visibleTargets++;
                
                if (visibleTargets >= TARGET_THRESHOLD) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
