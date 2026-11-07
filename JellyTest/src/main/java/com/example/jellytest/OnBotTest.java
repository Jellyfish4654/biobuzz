package com.example.jellytest;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class OnBotTest implements Runnable {
    public TestTelemetry telemetry;
    public HardwareMap hardwareMap;
    public Gamepad gamepad;
    public volatile boolean done = false;
    public volatile boolean passed = false;
    public volatile boolean isRunning = true;
    public volatile boolean waitingForManualApproval = false;

    public final void passTest(){
        done = true;
        passed = true;
        isRunning = false;
    }
    public final void failTest(){
        done = true;
        passed = false;
        isRunning = false;
    }
    protected final void waitForManualApproval(){
        waitingForManualApproval = true;
        while(!done & !Thread.interrupted()){}
    }

    protected final void acceptManualApproval(){
        waitingForManualApproval = true;
    }

    protected final boolean testIsRunning(){
        return !Thread.currentThread().isInterrupted() && isRunning;
    }


    protected final void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void preRun(int runIndex){};
    public void postRun(int runIndex){};
}
