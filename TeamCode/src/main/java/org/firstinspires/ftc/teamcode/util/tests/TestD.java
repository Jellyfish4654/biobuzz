package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;

@OnBotTestInfo(testName = "Not actually test D lol", testSetName = "SetthathasBandC")
public class TestD extends OnBotTest {
    @Override
    public void run() {
        telemetry.addLine("I do progress!");
        acceptManualApproval();
        for(int i=1;i<=10;i++){
            telemetry.setProgress(i*10);
            sleep(500);
        }
        while(testIsRunning()){
            telemetry.addData("We run? ",testIsRunning());
            telemetry.update();
        }

    }
}
