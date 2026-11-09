package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;

@OnBotTestInfo(repeats = 2)
public class TestA extends OnBotTest {
    int runNum;
    @Override
    public void preRun(int runIndex) {
        runNum = runIndex;
    }

    @Override
    public void run() {
        telemetry.addData("I'm running test A for this time!!", runNum);
        telemetry.addLine("Now I'm waiting for test A to finish");
        telemetry.update();
        waitForManualApproval();
    }
}
