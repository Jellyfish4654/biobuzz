package com.example.jellytest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestTelemetry {

    //synchronized because can be written to by tests while being read from in main thread
    public final List<String> lines = Collections.synchronizedList(new ArrayList<String>());
    private int progress = 0;
    private boolean clearOnNextAddition = false;
    public int repeatIndex = 0;

    public void addLine(){
        synchronized (lines) {
            if (clearOnNextAddition) {
                lines.clear();
                clearOnNextAddition = false;
            }
            lines.add("");
        }
    }
    public void addLine(String lineCaption){
        synchronized (lines) {
            if (clearOnNextAddition) {
                lines.clear();
                clearOnNextAddition = false;
            }

            lines.add(lineCaption);
        }
    }

    public void addData(String dataCaption, Object data){
        synchronized (lines) {
            if (clearOnNextAddition) {
                lines.clear();
                clearOnNextAddition = false;
            }
            lines.add(String.format("%s %s", dataCaption, data.toString()));
        }
    }

    /**Emulates the update() function, but doesn't really do anything due to how the main thread reads
     * the telemetry and it doesn't need to actively push it. Instead, it emulates how telemetry clears
     * if nothing is pushed to it after update();
     */
    public void update(){
        clearOnNextAddition = true;
    }

    /**Set the progress of the running Test for purely telemetry purposes
     * from 0% to 100%*/
    public void setProgress(int progress){
        this.progress = (progress>=0&&progress<=100)?progress:this.progress;
    }
    public int getProgress(){
        return progress;
    }

}
