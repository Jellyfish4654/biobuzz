package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.pedropathing.utils.Angle;
import com.pedropathing.utils.Control;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.Storage;
import org.firstinspires.ftc.teamcode.framework.pedro.Constants;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;

@Configurable
@TeleOp(name = "JellyTele")
public class JellyTele extends BaseOpMode {
    public static double HOLD_START_VELOCITY = 0.2;
    public static double AIM_ASSISTANCE_STRENGTH = 0.5;
    
    @Override
    public void init() {
        super.init();
        
        follower.setPose(Storage.poseBackup);
        follower.update();
    }
    
    @Override
    public void loop() {
        controls.syncWithPanels();
        
        
//        if (controls.eject()) {
//            scorer.ejectSustain();
//        } else if (controls.shoot()) {
//            scorer.shootStart();
//        } else if (controls.intake()) {
//            scorer.intakeSustain();
//        }
//
//        if (controls.flowerPressed()) {
//            scorer.flowerRamp.toggle();
//        }

        updateManualDrive();

        super.loop();
    }
    
    // ↓ -------------- ↓ -------------- ↓ MANUAL DRIVE ↓ -------------- ↓ -------------- ↓
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
        
        // aim assistance TODO: test this
        if (/*scorer.mode() == Scorer.Mode.PRESPIN || scorer.mode() == Scorer.Mode.SHOOT*/ false) {
            Pose robotPose = follower.pose();
            double distX = Storage.targetCell(robotPose).x() - robotPose.x();
            double distY = Storage.targetCell(robotPose).y() - robotPose.y();
            double targetHeading = Math.atan2(distY, distX);
            
            powers = headingLock(Constants.foresightConfig, follower, powers, targetHeading, AIM_ASSISTANCE_STRENGTH);
        }
        
        if (/*scorer.mode() == Scorer.Mode.PRESPIN || scorer.mode() == Scorer.Mode.SHOOT ||*/ controls.driveHoldOverride()) {
            ManualDrive.driveOrHold(follower, powers, 0, HOLD_START_VELOCITY);
        } else {
            follower.manual(powers);
        }
    }
    
    // patched code from unreleased commit TODO: switch back to builtin when it's dropped
    public static DrivePowers headingLock(
            Follower follower,
            Controller headingFeedback,
            DrivePowers powers,
            double targetHeading,
            double headingLinear,
            double headingQuadratic,
            double strength) {
        double angularVel = follower.velocity().omega;
        double brakeDist =
                headingLinear * angularVel + headingQuadratic * angularVel * angularVel * Math.signum(angularVel);
        double headingError =
                Angle.normalizeSigned(targetHeading - follower.pose().heading());
        double error = Angle.normalizeSigned(headingError - brakeDist);
        double power = headingFeedback.calculate(0, error) * strength;
        power = Control.clampBrakingPower(power, angularVel, 0.3);
        return new DrivePowers(powers.forward(), powers.strafe(), power);
    }
    
    public static DrivePowers headingLock(ForesightConfig config, Follower follower, DrivePowers powers, double targetHeading, double strength) {
        return headingLock(
                follower,
                config.headingFeedback.get(),
                powers,
                targetHeading,
                config.headingBrakeCoefficients.get().x(),
                config.headingBrakeCoefficients.get().y(),
                strength
        );
    }
}
