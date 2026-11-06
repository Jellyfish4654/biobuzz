package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

@Configurable
public class Roller {
    public static double[] POWERS = {
            1, // intake
            1  // transfer
    };
    
    private final DcMotor motor;
    private final int id;
    private double cachedPower = 0;

    public Roller(DcMotor motor, DcMotor.Direction direction, int id) {
        this.motor = motor;
        this.motor.setDirection(direction);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.id = id;
    }

    private void ensurePower(double desired) {
        if (cachedPower != desired) {
            cachedPower = desired;
            motor.setPower(cachedPower);
        }
    }

    public void on() {
        ensurePower(POWERS[id]);
    }
    
    public void reverse() {
        ensurePower(-POWERS[id]);
    }

    public void off() {
        ensurePower(0);
    }

    public boolean isOn() {
        return cachedPower != 0;
    }
}
