package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.scoring.Blocker;

@OnBotTestInfo(manualPassFail = true, testSetName = "servoTests")
public class BlockerTest extends OnBotTest {
    @Override
    public void run() {
        Blocker b = new Blocker(hardwareMap.get(Servo.class,"pollen_blocker"));
        Blocker a = new Blocker(hardwareMap.get(Servo.class,"nectar_blocker"));

        while(!Thread.interrupted()){
            b.toggle();
            a.toggle();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
