package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.math.Pose;
import com.pedropathing.utils.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class FlowerRedAuto extends BaseAuto {

    @Override
    protected PoseFactory allianceBasedSetup() { return PoseFactory.degrees(); }

    @Override
    protected Pose getStartPose() {
        return poseFactory.of(30, 133, 270);
    }

    Pose shooting = poseFactory.of(58, 126, 270);

    Path shootPath() {return Paths.line(getStartPose(), shooting);}

    @Override
    protected Command fullRoutine() {
        return sequential (
                follow(follower, shootPath())
                // TODO: Finish the full routine for the auto
        );
    }
}
