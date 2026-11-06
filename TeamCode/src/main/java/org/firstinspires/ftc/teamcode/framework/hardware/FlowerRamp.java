package org.firstinspires.ftc.teamcode.framework.hardware;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerRamp {
    private final Servo servo;
    public static double UP = 0;
    public static double DOWN = 0;
    public double currAngle = 0.0;

    public FlowerRamp(Servo servo) {
        this.servo = servo;
    }

    public void setAngle(double desired){
        if (currAngle != desired) {
            currAngle = desired;
            servo.setPosition(currAngle);
        }
    }

    private void toggle() {
        if (currAngle==UP) {
            currAngle = DOWN;
        } else {
            currAngle = UP;
        }
        servo.setPosition(currAngle);
    }

}
