package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.Storage;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;

@Configurable
@Autonomous(name = "Red Solo", group = "1", preselectTeleOp = "JellyTele")
public class RedSoloAuto extends BaseAuto {
    public static double TIME_TO_PARK = 26;
    
    @Override
    protected PoseFactory allianceBasedSetup() {
        Storage.alliance = Storage.Alliance.RED;
        return PoseFactory.degrees();
    }

    @Override
    //same as GardenRedAuto position but we can adjust
    protected Pose startPose() { return poseFactory.of(59.0, 9.0, 90.0); }

    @Override
    protected int[] getTags() { return new int[] {34, 35, 36, 37}; } // TODO: two sets of tags?

    boolean shouldStop = false;

    Pose gardenPose = poseFactory.of(9,8.7,270);
    Pose gardenControlPose = poseFactory.of(21,116,0); //heading doesn't matter
    Pose flower1Pose = poseFactory.of(13,46,180); //garden side
    Pose flower1ControlPose = poseFactory.of(21,103,0);
    Pose flower2Pose = poseFactory.of(46,128,90); //flower side
    Pose hive2Pose = poseFactory.of(59,120,270);
    Pose parkPose = poseFactory.of(10,91,0);

    Path toFlower1() {return Paths.line(startPose(),flower1Pose).linear(startPose(),flower1Pose);}
    Path toShootOne() {return Paths.curve(flower1Pose,flower1ControlPose,hive2Pose).linear(flower1Pose,hive2Pose);}
    Path toFlower2() {return Paths.line(hive2Pose,flower2Pose).linear(hive2Pose,flower2Pose);}
    Path toShootTwo() {return Paths.line(flower2Pose,hive2Pose).linear(flower2Pose,hive2Pose);}
    Path toGarden() {return Paths.curve(hive2Pose,gardenControlPose,gardenPose).linear(hive2Pose,gardenPose);}
    Path toShootThree() {return Paths.line(gardenPose, startPose()).linear(gardenPose, startPose());}
    Path parkFromShoot() {return Paths.line(startPose(),parkPose).linear(startPose(),parkPose);}

    @Override
    protected Command fullRoutine() {
        return sequential(
                instant(scorer::shootStart),
                waitUntil(()-> scorer.mode()== Scorer.Mode.IDLE),
                parallel(
                        follow(follower,toFlower1()),
                        instant(scorer.flowerRamp::toggle),
                        Groups.loop(instant(scorer::intakeSustain))
                ).until(()-> Storage.balls >= 4),
                follow(follower, toShootOne()),
                conditional(
                        ()->getRuntime() >= TIME_TO_PARK,
                        sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                        instant(scorer::shootStart)
                ),
                waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
                parallel(
                        follow(follower,toFlower2()),
                        instant(scorer.flowerRamp::toggle),
                        Groups.loop(instant(scorer::intakeSustain))
                ).until(()-> Storage.balls >= 4),
                follow(follower,toShootTwo()),
                conditional(
                        ()->getRuntime() >= TIME_TO_PARK,
                        sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                        instant(scorer::shootStart)
                ),
                waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
                parallel(
                        follow(follower,toGarden()),
                        Groups.loop(instant(scorer::intakeSustain))
                ).until(()-> Storage.balls >= 4),
                follow(follower, toShootThree()),
                waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
                follow(follower,parkFromShoot())
        ).until(()->shouldStop);
    }
}
