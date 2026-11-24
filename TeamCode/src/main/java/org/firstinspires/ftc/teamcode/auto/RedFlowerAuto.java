package org.firstinspires.ftc.teamcode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
@Autonomous(name = "Red Flower", group = "1", preselectTeleOp = "JellyTele")
public class RedFlowerAuto extends BaseAuto {
    public static double TIME_TO_PARK = 26;
    
    @Override
    protected PoseFactory allianceBasedSetup() {
        Storage.alliance = Storage.Alliance.RED;
        return PoseFactory.degrees();
    }

    @Override
    protected Pose startPose() { return poseFactory.of(30, 133, 270); }
    
    @Override
    protected int[] getTags() { return new int[] {30, 31, 32, 33}; }
    
    Pose shooting = poseFactory.of(58, 126, 270);

    Path shootPath() {return Paths.line(startPose(), shooting);}

    @Override
    protected Command fullRoutine() {
        return sequential (
                follow(follower, shootPath())
                // TODO: Finish the full routine for the auto
        );
    }
}
