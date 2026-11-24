package org.firstinspires.ftc.teamcode.framework.scoring;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.controllers.Controller;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

@Configurable
public class Shooter {
    public static double READY_VELOCITY_THRESHOLD = 200; // TODO: tune
    
    public static double POLLEN_KV = 0, POLLEN_KP = 0;
    public static double NECTAR_KV = 0, NECTAR_KP = 0;
    
    public static double POLLEN_HOOD_POS_MIN = 0, POLLEN_HOOD_POS_MAX = 1;
    public static double NECTAR_HOOD_POS_MIN = 0, NECTAR_HOOD_POS_MAX = 1;
    
    private final Controller velocityController;
    private final DoubleSupplier hoodPosMin, hoodPosMax;
    
    private final DcMotorEx motor;
    private final Servo hood;
    
    private double cachedMotorPower = 0;
    private double cachedHoodPos = 0;
    
    public Shooter(
            DcMotorEx motor, DcMotorEx.Direction motorDirection, DoubleSupplier kV, Supplier<Double> kP,
            Servo hood, Servo.Direction hoodDirection, DoubleSupplier hoodMinPos, DoubleSupplier hoodMaxPos,
            DoubleSupplier voltage
    ) {
        this.motor = motor;
        this.motor.setDirection(motorDirection);
        this.motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        
        this.velocityController = Controller.sum(
                Controller.proportionalFeedforward(() -> kV.getAsDouble() * (12.0 / voltage.getAsDouble())),
                Controller.proportional(kP)
        );
        
        this.hood = hood;
        this.hood.setDirection(hoodDirection);
        this.hoodPosMin = hoodMinPos;
        this.hoodPosMax = hoodMaxPos;
    }
    
    private void ensureMotorPower(double desired) {
        if (cachedMotorPower != desired) {
            cachedMotorPower = desired;
            motor.setPower(cachedMotorPower);
        }
    }
    
    private void ensureHoodPos(double desired) {
        if (desired >= hoodPosMin.getAsDouble() && desired <= hoodPosMax.getAsDouble() && cachedHoodPos != desired) {
            cachedHoodPos = desired;
            hood.setPosition(cachedHoodPos);
        }
    }
    
    private double calcTargetVelocity(double distance) {
        return 0; // temp
    }
    
    public void on(double distance) {
        // Regression for velocity and hood angle thingy
        double targetVel = calcTargetVelocity(distance);
        // Hood angle offset based on velocity error (and distance?)
        ensureMotorPower(velocityController.calculate(targetVel, targetVel - motor.getVelocity()));
    }
    
    public void off() {
        ensureMotorPower(0);
    }
    
    public boolean isReady(double distance) {
        return isOn() && Math.abs(calcTargetVelocity(distance) - motor.getVelocity()) < READY_VELOCITY_THRESHOLD;
    }
    
    public boolean isOn() {
        return cachedMotorPower != 0;
    }
}
