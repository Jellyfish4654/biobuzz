package org.firstinspires.ftc.teamcode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.Paths;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.paths.Path;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.Storage;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;

@Configurable
@Autonomous(name = "Red Garden", group = "1", preselectTeleOp = "JellyTele")
public class RedGardenAuto extends BaseAuto {
    public static double TIME_TO_PARK = 26;
    
    @Override
    protected PoseFactory allianceBasedSetup() {
        Storage.alliance = Storage.Alliance.RED;
        return PoseFactory.degrees();
    }

    @Override
    protected Pose startPose() { return poseFactory.of(59.0, 9.0, 90.0); }

    @Override
    protected int[] getTags() { return new int[] {34, 35, 36, 37}; }

    boolean shouldStop = false;

    Pose gardenPose = poseFactory.of(9,8.7,270);
    Pose gardenControlPose = poseFactory.of(10,15,260);
    Pose flowerPose = poseFactory.of(13,46,180);
    Pose parkPose = poseFactory.of(10,91,0);

    Path toGarden() {return Paths.curve(startPose(),gardenControlPose,gardenPose).linear(startPose(),gardenPose);}
    Path toShootOne() {return Paths.line(gardenPose, startPose()).linear(gardenPose, startPose());}
    Path toFlower() {return Paths.line(startPose(),flowerPose).linear(startPose(),flowerPose);}
    Path toShootTwo() {return Paths.line(flowerPose, startPose()).linear(flowerPose, startPose());}
    Path parkFromShoot() {return Paths.line(startPose(),parkPose).linear(startPose(),parkPose);}

    @Override
    protected Command fullRoutine() {
        //CommandBuilder/.until could be used instead of waitUntil, but that's noah's prerogative -- this may be more readable.
        return sequential(
            instant(scorer::shootStart),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            parallel(
                    follow(follower,toGarden()),
                    Groups.loop(instant(scorer::intakeSustain))
            ).until(()-> Storage.balls >= 4),
            follow(follower, toShootOne()),
            instant(()->vision.start()),
            waitUntil(()->vision.areTagsVisible(getTags()) || getRuntime() >= TIME_TO_PARK),
            conditional(
                    ()->getRuntime() >= TIME_TO_PARK,
                    sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                    instant(scorer::shootStart)
            ),
            instant(vision::pause),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            parallel(
                follow(follower,toFlower()),
                instant(scorer.flowerRamp::toggle), // TODO: switch this to on or off
                Groups.loop(instant(scorer::intakeSustain))
            ).until(()-> Storage.balls >= 4),
            follow(follower,toShootTwo()),
            instant(()->vision.start()),
            waitUntil(()-> vision.areTagsVisible(getTags()) || getRuntime() >= TIME_TO_PARK),
            conditional(
                    ()->getRuntime() >= TIME_TO_PARK,
                    sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                    instant(scorer::shootStart)
            ),
            instant(vision::stop),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            follow(follower,parkFromShoot())
        ).until(()->shouldStop);
    }
}
