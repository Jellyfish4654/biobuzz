package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
public class JellyTele extends BaseOpMode {
    public static double MAX_HOLD_VELOCITY = 0.2;
    
    @Override
    public void init() {
        super.init();
        
        follower.setPose(Storage.poseBackup);
        follower.update();
    }
    
    @Override
    public void loop() {
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
                controls.driveStr(),
                controls.driveTrn()
        );
        
        if (fieldCentric) {
            powers = ManualDrive.fieldCentric(powers, follower.pose().heading(), Storage.alliance.headingOffset);
            telemetry.addLine("\nDrivetrain (Field Centric):");
        } else {
            telemetry.addLine("\nDrivetrain:");
        }
        
        telemetry.addData("\tForward", "%.2f", controls.driveFwd());
        telemetry.addData("\tStrafe", "%.2f", controls.driveStr());
        telemetry.addData("\tTurn", "%.2f", controls.driveTrn());
        
        if (controls.defensive()) { // TODO: replace with if shooter active
            ManualDrive.driveOrHold(follower, powers, 0, MAX_HOLD_VELOCITY);
            // TODO: worried that this will not hold original position & release on shove; may need implement manually
        } else {
            follower.manual(powers);
        }
    }
}
