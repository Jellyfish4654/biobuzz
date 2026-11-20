package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Scheduler;
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
import com.bylazar.configurables.annotations.Configurable;
@Autonomous
@Configurable
public class GardenRedAuto extends BaseAuto{
    @Override
    protected PoseFactory allianceBasedSetup() {
        return PoseFactory.degrees();
    }

    @Override
    protected Pose getStartPose() {
        return poseFactory.of(59.0,9.0,90.0);
    }

    public static double TIME_TO_PARK = 26;

    Pose gardenPose = poseFactory.of(9,8.7,270);
    Pose gardenControlPose = poseFactory.of(10,15,260);
    Pose flowerPose = poseFactory.of(13,46,180);
    Pose parkPose = poseFactory.of(10,91,0);

    Path toGarden() {return Paths.curve(getStartPose(),gardenControlPose,gardenPose).linear(getStartPose(),gardenPose);}
    Path toShootOne() {return Paths.line(gardenPose,getStartPose()).linear(gardenPose,getStartPose());}
    Path toFlower() {return Paths.line(getStartPose(),flowerPose).linear(getStartPose(),flowerPose);}
    Path toShootTwo() {return Paths.line(flowerPose,getStartPose()).linear(flowerPose,getStartPose());}
    Path parkFromShoot() {return Paths.line(getStartPose(),parkPose).linear(getStartPose(),parkPose);}

    @Override
    protected Command fullRoutine() {
        //CommandBuilder/.until could be used instead of waitUntil, but that's noah's prerogative -- this may be more readable.
        return race(sequential(
            instant(scorer::shootStart),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            race(
                    waitUntil(()-> Storage.balls == 4),
                    follow(follower,toGarden()),
                    Groups.loop(instant(scorer::intakeSustain))
            ),
            follow(follower, toShootOne()),
            //the true will be substituted for limelight seeing the apriltag cluster, indicating a tip
            waitUntil(()->true || getRuntime() >= TIME_TO_PARK),
            conditional(
                    ()->getRuntime() >= TIME_TO_PARK,
                    sequential(follow(follower, parkFromShoot()),instant(Scheduler::reset)),
                    instant(scorer::shootStart)
            ),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            race(
                waitUntil(()-> Storage.balls == 4),
                follow(follower,toFlower()),
                instant(scorer.flowerRamp::toggle),
                Groups.loop(instant(scorer::intakeSustain))
            ),
            follow(follower,toShootTwo()),
            //same thing with this true
            waitUntil(()->true || getRuntime() >= TIME_TO_PARK),
            conditional(
                    ()->getRuntime() >= TIME_TO_PARK,
                    sequential(follow(follower, parkFromShoot()),instant(Scheduler::reset)),
                    instant(scorer::shootStart)
            ),
            waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
            follow(follower,parkFromShoot())

        ),Groups.loop(instant(scorer::update)));
    }
}
