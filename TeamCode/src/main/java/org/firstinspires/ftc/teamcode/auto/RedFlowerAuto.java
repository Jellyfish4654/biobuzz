package org.firstinspires.ftc.teamcode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.Paths;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.paths.Path;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.Storage;
import org.firstinspires.ftc.teamcode.framework.scoring.Scorer;

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
    protected Pose startPose() { return poseFactory.of(59, 132.5, 270); }
    
    @Override
    protected int[] getTags() { return new int[] {30, 31, 32, 33}; }

    boolean shouldStop = false;
    
    Pose shootPose = poseFactory.of(58,125,270);
    Pose flowerPose = poseFactory.of(46,128,90);
    Pose flowerControlPose = poseFactory.of(48,117,0);
    Pose parkPose = poseFactory.of(12,116,0);

    Path toShootOne() {return Paths.line(startPose(),shootPose).linear(startPose(),shootPose);}
    Path toFlower() {return Paths.curve(startPose(),flowerControlPose,flowerPose).linear(startPose(),flowerPose);}
    Path toShootTwo() {return Paths.curve(flowerPose,flowerControlPose,shootPose).linear(flowerPose,shootPose);}
    Path parkFromShoot() {return Paths.line(shootPose,parkPose).linear(shootPose,parkPose);}

    @Override
    protected Command fullRoutine() {
        return sequential (
                follow(follower,toShootOne()),
                instant(()->vision.start()),
                waitUntil(()->vision.areTagsVisible(getTags()) || getRuntime() >= TIME_TO_PARK),
                conditional(
                        ()->getRuntime() >= TIME_TO_PARK,
                        sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                        instant(scorer::shootStart)
                ),
                instant(vision::pause),
                waitUntil(()-> scorer.mode()== Scorer.Mode.IDLE),
                parallel(
                        follow(follower,toFlower()),
                        instant(scorer.flowerRamp::toggle),
                        Groups.loop(instant(scorer::intakeSustain))
                ).until(()-> Storage.balls >= 4),
                follow(follower, toShootTwo()),
                instant(()->vision.start()),
                waitUntil(()->vision.areTagsVisible(getTags()) || getRuntime() >= TIME_TO_PARK),
                conditional(
                        ()->getRuntime() >= TIME_TO_PARK,
                        sequential(follow(follower, parkFromShoot()),instant(()->shouldStop=true)),
                        instant(scorer::shootStart)
                ),
                waitUntil(()-> scorer.mode()==Scorer.Mode.IDLE),
                follow(follower,parkFromShoot())
        ).until(()->shouldStop);
    }
}
