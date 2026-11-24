package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.scoring.ServoArm;

@OnBotTestInfo(manualPassFail = true, testSetName = "servoTests")
public class BlockerTest extends OnBotTest {
    @Override
    public void run() {
        ServoArm b = new ServoArm(hardwareMap.get(Servo.class,"pollen_blocker"), Servo.Direction.FORWARD,  () -> ServoArm.POLLEN_POS_UP, () -> ServoArm.POLLEN_POS_DOWN);
        ServoArm a = new ServoArm(hardwareMap.get(Servo.class,"nectar_blocker"), Servo.Direction.FORWARD,  () -> ServoArm.NECTAR_POS_UP, () -> ServoArm.NECTAR_POS_DOWN);

        while(!Thread.interrupted() && !done){
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
