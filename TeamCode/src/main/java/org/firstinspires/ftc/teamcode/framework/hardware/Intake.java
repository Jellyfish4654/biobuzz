package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

@Configurable
public class Intake {
    public static double ON_POWER = 1;
    private final DcMotor motor;
    private double cachedPower = 0;

    public Intake(DcMotor motor, DcMotor.Direction direction) {
        this.motor = motor;
        this.motor.setDirection(direction);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void ensurePower(double desired) {
        if (cachedPower != desired) {
            cachedPower = desired;
            motor.setPower(cachedPower);
        }
    }

    public void on() {
        ensurePower(ON_POWER);
    }
    
    public void reverse() {
        ensurePower(-ON_POWER);
    }

    public void off() {
        ensurePower(0);
    }

    public boolean isOn() {
        return cachedPower != 0;
    }
}
