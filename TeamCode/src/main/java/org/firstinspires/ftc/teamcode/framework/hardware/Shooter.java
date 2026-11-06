package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class Shooter {
    private final DcMotorEx motor;
    private final Servo hood;
    private double cachedPower; // TODO: velocity?
    private double cachedAngle;
    
    public Shooter(
            DcMotorEx motor, DcMotorEx.Direction motorDirection,
            Servo hood, Servo.Direction hoodDirection, double hoodMin, double hoodMax
    ) {
        this.motor = motor;
        this.motor.setDirection(motorDirection);
        this.motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        
        this.hood = hood;
        this.hood.setDirection(hoodDirection);
        this.hood.scaleRange(hoodMin, hoodMax);
    }
    
    private void ensurePower(double desired) {
        if (cachedPower != desired) {
            cachedPower = desired;
            motor.setPower(cachedPower);
        }
    }
    
    private void ensureAngle(double desired) {
        if (cachedAngle != desired) {
            cachedAngle = desired;
            hood.setPosition(cachedAngle);
        }
    }
    
    public void off() {
        ensurePower(0);
    }
    
    public boolean isOn() {
        return cachedPower != 0;
    }
}
