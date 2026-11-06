package org.firstinspires.ftc.teamcode.framework.hardware;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerRamp {
    private final Servo servo = hardwareMap.get(Servo.class, "flower_ramp");
    public static double UP = 0.0;
    public static double DOWN = 1.0;

    void up(){
        servo.setPosition(UP);
    }

    void down(){
        servo.setPosition(DOWN);
    }

}
