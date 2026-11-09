package org.firstinspires.ftc.teamcode.framework;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.gamepad.GamepadManager;
import com.bylazar.gamepad.PanelsGamepad;
import com.qualcomm.robotcore.hardware.Gamepad;

@Configurable
public class ControlMap {
    public static double DEADBAND = 0.02; // sticks and triggers
    public static double PRECISION_MULTIPLIER_COARSE = 1.0 / 2;
    public static double PRECISION_MULTIPLIER_FINE = 1.0 / 3;
    private final Gamepad gamepad1, gamepad2;
    
    public ControlMap(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        syncWithPanels();
    }
    
    public void syncWithPanels() { // TODO: test this
        combineGamepads(gamepad1, PanelsGamepad.INSTANCE.getFirstManager());
        combineGamepads(gamepad2, PanelsGamepad.INSTANCE.getSecondManager());
    }
    
    // ↓ -------------- ↓ -------------- ↓ GAMEPAD MAPPING ↓ -------------- ↓ -------------- ↓
    
    public double driveFwd() {
        return applyDeadband(-(gamepad1.left_stick_y + gamepad2.left_stick_y)) * activePrecisionMultiplier();
    }
    public double driveLeft() {
        return applyDeadband(-(gamepad1.left_stick_x + gamepad2.left_stick_x)) * activePrecisionMultiplier();
    }
    public double driveTurn() {
        return applyDeadband(-(gamepad1.right_stick_x + gamepad2.right_stick_x)) * activePrecisionMultiplier();
    }
    public boolean driveModePressed() {
        return gamepad1.psWasPressed() || gamepad2.psWasPressed();
    }
    public boolean defensive() { // TODO: temp
        return gamepad1.cross || gamepad2.cross;
    }
    
    public void rumble(int durationMs){
        gamepad1.rumble(durationMs);
        gamepad2.rumble(durationMs);
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
    
    private double activePrecisionMultiplier() {
        if (gamepad1.right_bumper || gamepad2.right_bumper) {
            return PRECISION_MULTIPLIER_FINE;
        } else if (gamepad1.left_bumper || gamepad2.left_bumper) {
            return PRECISION_MULTIPLIER_COARSE;
        }
        return 1;
    }
    
    // linear rescaled deadband: lowers inputs to start at 0 and scales up to reach 1
    private static double applyDeadband(double raw) {
        double absStick = Math.abs(raw);
        if (absStick > DEADBAND) {
            double scaledValue = (absStick - DEADBAND) / (1 - DEADBAND);
            return Math.copySign(scaledValue, raw);
        } else {
            return 0;
        }
    }
    
    private void combineGamepads(Gamepad main, GamepadManager panels) {
        if (panels == null) return;
        
        if (panels.getLeftStickX() != 0) main.left_stick_x = (float) panels.getLeftStickX();
        if (panels.getLeftStickY() != 0) main.left_stick_y = (float) panels.getLeftStickY();
        if (panels.getRightStickX() != 0) main.right_stick_x = (float) panels.getRightStickX();
        if (panels.getRightStickY() != 0) main.right_stick_y = (float) panels.getRightStickY();
        
        if (panels.getL2() != 0) main.left_trigger = (float) panels.getL2();
        if (panels.getR2() != 0) main.right_trigger = (float) panels.getR2();
        
        if (panels.getTouchpad()) main.touchpad = true;
        
        if (panels.getLeftStickPressed()) main.left_stick_button = true;
        if (panels.getRightStickPressed()) main.right_stick_button = true;
        
        if (panels.getDpadUp()) main.dpad_up = true;
        if (panels.getDpadDown()) main.dpad_down = true;
        if (panels.getDpadLeft()) main.dpad_left = true;
        if (panels.getDpadRight()) main.dpad_right = true;
        
        if (panels.getCross()) main.a = true;
        if (panels.getCircle()) main.b = true;
        if (panels.getSquare()) main.x = true;
        if (panels.getTriangle()) main.y = true;
        
        if (panels.getPs()) main.guide = true;
        if (panels.getOptions()) main.start = true;
        if (panels.getShare()) main.back = true;
        
        if (panels.getL1()) main.left_bumper = true;
        if (panels.getR1()) main.right_bumper = true;
        
        main.copy(main); // updates aliases, trigger booleans, & edge detection
    }
}
