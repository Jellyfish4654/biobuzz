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
public class DoubleRedAuto extends BaseAuto{
    @Override
    protected PoseFactory allianceBasedSetup() {
        return PoseFactory.degrees();
    }

    @Override
    protected Pose getStartPose() {
        return poseFactory.of(59.0,9.0,90.0);
    }



    Pose firstPose = poseFactory.of(1,2,3);
    Path firstPath() {return Paths.line(getStartPose(),firstPose);}
    @Override
    protected Command fullRoutine() {
        return sequential(
                follow(follower, firstPath()),
                instant(scorer::ejectSustain)
        );
    }
}
