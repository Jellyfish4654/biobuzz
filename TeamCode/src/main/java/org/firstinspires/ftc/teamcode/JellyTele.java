package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
@TeleOp(name = "JellyTele")
public class JellyTele extends BaseOpMode {
    public static double HOLD_START_VELOCITY = 0.2;
    
    @Override
    public void init() {
        super.init();
        
        follower.setPose(Storage.poseBackup);
        follower.update();
    }
    
    @Override
    public void loop() {
        controls.syncWithPanels();
        updateManualDrive();
        
        super.loop();
    }
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private boolean fieldCentric = false;
    private void updateManualDrive() {
        if (controls.driveModePressed()) {
            fieldCentric = !fieldCentric;
        }
        
        DrivePowers powers = new DrivePowers(
                controls.driveFwd(),
                controls.driveLeft(),
                controls.driveTurn()
        );
        
        if (fieldCentric) {
            powers = ManualDrive.fieldCentric(powers, follower.pose().heading(), Storage.alliance.headingOffset);
            telemetry.addLine("\n<b>Manual Drive <font color='yellow'>(Field Centric):</font></b>");
        } else {
            telemetry.addLine("\n<b>Manual Drive:</b>");
        }
        
        telemetry.addData("\tForward", "%.2f", controls.driveFwd());
        telemetry.addData("\tLeft", "%.2f", controls.driveLeft());
        telemetry.addData("\tTurn", "%.2f", controls.driveTurn());
        
        if (controls.defensive()) { // TODO: replace with Scorer.mode() == Scorer.Mode.SHOOT once it initializes
            ManualDrive.driveOrHold(follower, powers, 0, HOLD_START_VELOCITY);
        } else {
            follower.manual(powers);
        }
    }
}
