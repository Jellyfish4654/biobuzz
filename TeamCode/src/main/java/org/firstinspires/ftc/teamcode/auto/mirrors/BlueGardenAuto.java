package org.firstinspires.ftc.teamcode.auto.mirrors;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.auto.RedGardenAuto;
import org.firstinspires.ftc.teamcode.framework.Storage;

@Configurable
@Autonomous(name = "Blue Garden", group = "2", preselectTeleOp = "JellyTele")
public class BlueGardenAuto extends RedGardenAuto {
    @Override
    protected PoseFactory allianceBasedSetup() {
        Storage.alliance = Storage.Alliance.BLUE;
        return PoseFactory.degrees().rotateAround(70.75,70.75,180);
    }

    @Override
    protected int[] getTags() { return new int[] {42, 43, 44, 45}; }
}
