package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.DoubleSupplier;

@Configurable
public class Roller {
    public static double INTAKE_POWER = 1;
    public static double TRANSFER_POWER = 1;
    
    private final DcMotor motor;
    private final DoubleSupplier powerSupplier;
    private double cachedPower = 0;

    public Roller(DcMotor motor, DcMotor.Direction direction, DoubleSupplier powerSupplier) {
        this.motor = motor;
        this.motor.setDirection(direction);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.powerSupplier = powerSupplier;
    }

    private void ensurePower(double desired) {
        if (cachedPower != desired) {
            cachedPower = desired;
            motor.setPower(cachedPower);
        }
    }

    public void on() {
        ensurePower(powerSupplier.getAsDouble());
    }
    
    public void reverse() {
        ensurePower(-powerSupplier.getAsDouble());
    }

    public void off() {
        ensurePower(0);
    }

    public boolean isOn() {
        return cachedPower != 0;
    }
}
