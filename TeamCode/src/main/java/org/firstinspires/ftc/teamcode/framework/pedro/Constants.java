package org.firstinspires.ftc.teamcode.framework.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("drive_fl");
        c.frontRightName.set("drive_fr");
        c.backLeftName.set("drive_bl");
        c.backRightName.set("drive_br");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        // TODO: retune this and foresight
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.22503777198970026);
                Controller secondaryTranslationalForward = Controller.proportional(0.08314544363018145);
                Controller primaryTranslationalLateral = Controller.proportional(0.3218932351211385);
                Controller secondaryTranslationalLateral = Controller.proportional(0.11893094923160868);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.02137320753990728));
                c.brake.set(Controller.proportionalFeedforward(0.018167226408921187));

                c.headingFeedback.set(Controller.proportional(3.515154682571327));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04092469001793152, 0.005459951481110074));

                c.linearBrakeCoefficients.set(Matrix.diag(0.08674808296990529, 0.05645215460115728));
                c.quadraticBrakeCoefficients.set(Matrix.diag(-1.0237425513447864E-4, 0.0010600890061332189));

                c.maxAchievableForwardVelocity.set(51.76704817128543);
                c.maxAchievableStrafeVelocity.set(43.36922002410591);
                c.naturalForwardDeceleration.set(45.920869328360325);
                c.naturalStrafeDeceleration.set(56.44662589321291);
            }
    );
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }

}