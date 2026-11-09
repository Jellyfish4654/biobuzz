package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;

@OnBotTestInfo(testName = "testCLoluwuhahahahhaha",testSetName = "SetthathasBandC")
public class TestC extends OnBotTest {
    @Override
    public void run() {
        telemetry.addLine("I'm running test B!");
        telemetry.addLine("I do need fancy shmancy manual approval!");
        telemetry.update();
        waitForManualApproval();
    }
}
