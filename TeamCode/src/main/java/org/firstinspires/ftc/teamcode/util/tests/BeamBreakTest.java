package org.firstinspires.ftc.teamcode.util.tests;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.framework.scoring.BeamBreak;

@OnBotTestInfo
public class BeamBreakTest extends OnBotTest {

    @Override
    public void run() {
        BeamBreak sensor = new BeamBreak(hardwareMap.get(DigitalChannel.class, "intake_sensor"));
        int balls = 0;
        long startTime = System.currentTimeMillis();
        while(!Thread.interrupted()){
            sensor.update();
            if(sensor.justUnbroke()){
                balls++;
                if(balls>=3){
                    passTest();
                }
                if(System.currentTimeMillis()-startTime > 30000){
                    failTest();
                }
            }
        }

    }
}
