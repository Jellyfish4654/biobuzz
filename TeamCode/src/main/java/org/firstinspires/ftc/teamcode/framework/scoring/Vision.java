package org.firstinspires.ftc.teamcode.framework.scoring;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.bylazar.configurables.annotations.Configurable;

import java.util.HashMap;
import java.util.List;

@Configurable
public class Vision {

    public static int TARGET_FOUND_THRESHOLD = 3;
    private final Limelight3A ll;
    public HashMap<Integer, Boolean> targets;
    public Vision(Limelight3A limelight){
        ll = limelight;
        ll.setPollRateHz(100);
        targets = new HashMap<>();
    }

    public void start(int[] targetIds){
        targets = new HashMap<>();
        for(int i=0;i<4;i++){targets.put(targetIds[i],false);}
        ll.start();
    }

    public void stop(){
        ll.stop();
    }
    public void pause(){
        ll.pause();
    }


    public boolean tagsFound(){
        LLResult result = ll.getLatestResult();
        int tagsFound = 0;

        for(int key : targets.keySet()){
            targets.replace(key, false);
        }

        if(result.isValid()){
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for(LLResultTypes.FiducialResult i : fiducialResults){
                if(targets.containsKey(i.getFiducialId()) && targets.get(i.getFiducialId()) == false){
                    targets.replace(i.getFiducialId(),true);
                    tagsFound++;
                }
            }
            return tagsFound >= TARGET_FOUND_THRESHOLD;
        }
        return false;
    }
}
