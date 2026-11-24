package org.firstinspires.ftc.teamcode.framework.scoring;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.DoubleSupplier;

@Configurable
public class Roller {
    public static double INTAKE_POWER = 1, TRANSFER_POWER = 1;
    private final DoubleSupplier power;
    private final DcMotor motor;
    private double cachedPower = 0;

    public Roller(DcMotor motor, DcMotor.Direction direction, DoubleSupplier power) {
        this.motor = motor;
        this.motor.setDirection(direction);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.power = power;
    }

    private void ensurePower(double desired) {
        if (cachedPower != desired) {
            cachedPower = desired;
            motor.setPower(cachedPower);
        }
    }

    public void on() {
        ensurePower(power.getAsDouble());
    }
    
    public void reverse() {
        ensurePower(-power.getAsDouble());
    }

    public void off() {
        ensurePower(0);
    }

    public boolean isOn() {
        return cachedPower != 0;
    }
}
