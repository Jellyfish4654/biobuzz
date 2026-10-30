package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;

@Configurable
public class Intake {
    private final DcMotor intake;
    public static double POWER = 1;
    public int balls = 0; //tracks possession

    public Intake (DcMotor motor) {
        //pasted from last year's intake
        this.intake = motor;
        intake.setDirection(DcMotor.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void off() {
        intake.setPower(0);
    }

    public void onIntake() {
        //TODO: implement possession tracking
        intake.setPower(POWER);
    }

    public void onShoot() {
        /*TODO: find optimal time to shoot before turning on intake motor
        4 nectar -> the bottom ball must move up (using an intake roller) while shooting */
        intake.setPower(POWER);
    }

    public boolean isOn() {
        return intake.getPower() != 0;
    }
}
