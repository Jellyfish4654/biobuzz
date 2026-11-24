package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.scoring.ServoArm;

@OnBotTestInfo(manualPassFail = true, testSetName = "servoTests")
public class FlowerRampTest extends OnBotTest {
    @Override
    public void run() {
        ServoArm b = new ServoArm(hardwareMap.get(Servo.class,"flower_ramp"), Servo.Direction.FORWARD, () -> ServoArm.FLOWER_POS_UP, () -> ServoArm.FLOWER_POS_DOWN);

        while(!Thread.interrupted() && !done){
            b.toggle();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
