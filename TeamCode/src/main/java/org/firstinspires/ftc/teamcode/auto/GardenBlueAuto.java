package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.Storage;
import com.bylazar.configurables.annotations.Configurable;

@Autonomous
@Configurable
public class GardenBlueAuto extends GardenRedAuto{
    @Override
    protected PoseFactory allianceBasedSetup() {
        Storage.alliance = Storage.Alliance.BLUE;
        return PoseFactory.degrees().rotateAround(70.75,70.75,180);
    }

    @Override
    protected int[] getTags() {
        return new int[]{42,43,44,45};
    }
}
