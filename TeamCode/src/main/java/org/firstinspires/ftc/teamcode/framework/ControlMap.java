package org.firstinspires.ftc.teamcode.framework;

//import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Gamepad;

//@Configurable
public class ControlMap {
    // TODO: strafe adjustment factor?
    public static double DEADBAND = 0.02;
    public static double PRECISION_MULTIPLIER_COARSE = 0.35;
    public static double PRECISION_MULTIPLIER_FINE = 0.2;
    private final Gamepad gamepad1, gamepad2;
    
    public ControlMap(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    
    // ↓ -------------- ↓ -------------- ↓ GAMEPAD MAPPING ↓ -------------- ↓ -------------- ↓
    
    public double driveFwd() {
        return applyDeadband(gamepad1.left_stick_x + gamepad2.left_stick_x) * precisionMulti();
    }
    public double driveLat() {
        return applyDeadband(-(gamepad1.left_stick_y + gamepad2.left_stick_y)) * precisionMulti();
    }
    public double driveTrn() {
        return applyDeadband(gamepad1.right_stick_x + gamepad2.right_stick_x) * precisionMulti();
    }
    public boolean driveModePressed() {
        return gamepad1.psWasPressed() || gamepad2.psWasPressed();
    }
    
    public void rumble(int durationMs){
        this.gamepad1.rumble(durationMs);
        this.gamepad2.rumble(durationMs);
    }
    public void megaRumble() {
        Gamepad.RumbleEffect megaEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 250)
                .addStep(0, 0, 250)
                .addStep(0, 1, 250)
                .addStep(0, 0, 250)
                .addStep(1, 0, 250)
                .addStep(0, 0, 250)
                .addStep(0, 1, 250)
                .addStep(0, 0, 250)
                .addStep(1, 1, 500)
                .build();
        gamepad1.runRumbleEffect(megaEffect);
        gamepad2.runRumbleEffect(megaEffect);
    }
    
    // ↓ -------------- ↓ -------------- ↓ INTERNAL ↓ -------------- ↓ -------------- ↓
    
    // TODO: not sure if this is the best place for it
    private double precisionMulti() {
        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            return PRECISION_MULTIPLIER_FINE;
        } else if (gamepad1.left_bumper || gamepad2.left_bumper) {
            return PRECISION_MULTIPLIER_COARSE;
        }
        return 1;
    }
    
    // linear rescaled deadband: lowers inputs to start at 0 and scales up to reach 1
    private double applyDeadband(double stick) {
        double absStick = Math.abs(stick);
        if (absStick > DEADBAND) {
            double scaledValue = (absStick - DEADBAND) / (1 - DEADBAND);
            // make the lowered stick a fraction of its maximum range to stretch it back to 0 - 1
            return Math.copySign(scaledValue, stick); // finish by copying the sign
        } else {
            return 0;
        }
    }
}