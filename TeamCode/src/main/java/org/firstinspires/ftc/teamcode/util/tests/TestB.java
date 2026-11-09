package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;

@OnBotTestInfo(testSetName = "SetthathasBandC")
public class TestB extends OnBotTest {
    @Override
    public void run() {
        telemetry.addLine("I'm running test B!");
        telemetry.addLine("I don't need no fancy shmancy manual approval -- I just fail :(");
        telemetry.update();

        failTest();
    }
}
