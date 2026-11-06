package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class FlowerRamp {
    public static double UP_POS = 1;
    public static double DOWN_POS = 0;
    
    private final Servo servo;
    private double cachedAngle = 0;

    public FlowerRamp(Servo servo) {
        this.servo = servo;
        up();
    }

    private void ensureAngle(double desired){
        if (cachedAngle != desired) {
            cachedAngle = desired;
            servo.setPosition(cachedAngle);
        }
    }
    
    public void up() {
        ensureAngle(UP_POS);
    }
    
    public void down() {
        ensureAngle(UP_POS);
    }
    
    // No toggle -- keep to scorer
    
    public boolean isDown() {
        return cachedAngle == DOWN_POS;
    }
}
