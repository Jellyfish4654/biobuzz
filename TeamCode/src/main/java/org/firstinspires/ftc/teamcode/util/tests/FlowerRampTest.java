package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.scoring.FlowerRamp;

@OnBotTestInfo(manualPassFail = true, testSetName = "servoTests")
public class FlowerRampTest extends OnBotTest {
    @Override
    public void run() {
        FlowerRamp b = new FlowerRamp(hardwareMap.get(Servo.class,"flower_ramp"));

        while(!Thread.interrupted()){
            b.toggle();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
