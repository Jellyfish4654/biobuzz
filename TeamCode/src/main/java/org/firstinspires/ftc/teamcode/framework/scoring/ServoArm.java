package org.firstinspires.ftc.teamcode.framework.scoring;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.function.DoubleSupplier;

@Configurable
public class ServoArm {
    public static double FLOWER_POS_UP = 1, FLOWER_POS_DOWN = 0;
    public static double POLLEN_POS_UP = 1, POLLEN_POS_DOWN = 0;
    public static double NECTAR_POS_UP = 1, NECTAR_POS_DOWN = 0;
    
    private final DoubleSupplier posUp, posDown;
    private final Servo servo;
    private double cachedPosition = 0;

    public ServoArm(Servo servo, Servo.Direction direction, DoubleSupplier posUp, DoubleSupplier posDown) {
        this.servo = servo;
        this.servo.setDirection(direction);
        this.posUp = posUp;
        this.posDown = posDown;
    }

    private void ensurePosition(double desired){
        if (cachedPosition != desired) {
            cachedPosition = desired;
            servo.setPosition(cachedPosition);
        }
    }
    
    public void up() {
        ensurePosition(posUp.getAsDouble());
    }
    
    public void down() {
        ensurePosition(posDown.getAsDouble());
    }
    
    public void toggle() {
        if (isDown()) up();
        else down();
    }
    
    public boolean isDown() {
        return cachedPosition == posDown.getAsDouble();
    }
}
