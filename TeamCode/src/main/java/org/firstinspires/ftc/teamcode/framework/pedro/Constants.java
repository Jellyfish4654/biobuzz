package org.firstinspires.ftc.teamcode.framework.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.OptionalDouble;

public class Constants {
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.resetMode.set(PinpointLocalizer.ResetMode.RECALIBRATE_IMU); // pose will be reset with Follower.setPose()
        
        c.offsetUnits.set(DistanceUnit.INCH);
        c.xPodOffset.set(-0.25); // found with tape measure (X (forward) pod's distance left from the center of rotation)
        c.yPodOffset.set(-4.00625); // found with tape measure (Y (Strafe) pod's distance forward from the center of rotation)
        
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);

//        c.encoderResolutionUnit.set(DistanceUnit.INCH);
//        c.ticksPerUnit.set(OptionalDouble.of(337.909515460958)); // found with AutoTune
    });
    
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("drive_fl");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        
        c.frontRightName.set("drive_fr");
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        
        c.backLeftName.set("drive_bl");
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        
        c.backRightName.set("drive_br");
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        
        c.manualBrakeMode.set(false); // sent directly to motor ZeroPowerBehavior (coast for smoother movement)
        c.powerThreshold.set(0.0); // our deadband makes this unnecessary
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        Controller primaryTranslationalForward = Controller.proportional(0.17986796049574846);
        Controller secondaryTranslationalForward = Controller.proportional(0.06645640524275828);
        Controller primaryTranslationalLateral = Controller.proportional(0.46723387363351676);
        Controller secondaryTranslationalLateral = Controller.proportional(0.17263043158854655);

        c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.017529986384648918));
        c.brake.set(Controller.proportionalFeedforward(0.01490048842695158));

        c.headingFeedback.set(Controller.proportional(2.8663343885960733));
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.03621593972045331, 0.007053118324813312));

        c.linearBrakeCoefficients.set(Matrix.diag(0.05026197017912329, 0.02532687652610324));
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.0015102020345959762, 0.0019212112219693575));

        c.maxAchievableForwardVelocity.set(60.57858747064822);
        c.maxAchievableStrafeVelocity.set(51.716255745232864);
        c.naturalForwardDeceleration.set(52.5850227348);
        c.naturalStrafeDeceleration.set(66.32634096182964);
    });

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new Follower(
                new PinpointLocalizer(hardwareMap, localizerConfig),
                new Mecanum(hardwareMap, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}
