package org.firstinspires.ftc.teamcode.framework.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Transfer {

    private final DcMotor transfer;
    public static double POWER = 1;

    public Transfer(DcMotor motor) {
        this.transfer = motor;
    }

    public void on() {
        transfer.setPower(POWER);
    }

    public void off() {
        transfer.setPower(0);
    }

    public boolean isOn() {
        return transfer.getPower() != 0;
    }
}
