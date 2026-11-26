package org.firstinspires.ftc.teamcode.util.tests;

import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.jellyfishbbhs.jellytest.OnBotTest;
import com.jellyfishbbhs.jellytest.OnBotTestInfo;
import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.math.Pose;
import static com.pedropathing.ivy.commands.Commands.*;
import org.firstinspires.ftc.teamcode.framework.pedro.Constants;

@OnBotTestInfo
public class DriveTest extends OnBotTest {

    int inches = 5;
    int timeOutMS = 5000;
    boolean timeouted = false;
    @Override
    public void run() {
        Follower follower = Constants.createFollower(hardwareMap);
        follower.setPose(new Pose(0,0,0));
        Pose pose1 = new Pose(0,0, 45);
        Pose pose2 = new Pose(0,inches, 135);
        Pose pose3 = new Pose(inches,inches, 225);
        Pose pose4 = new Pose(inches,0, 315);
        Pose pose5 = new Pose(0,0, 0);
        Command c = Groups.race(
                Groups.sequential(
                        follow(follower, Paths.line(pose5,pose1).linear(pose5,pose1)),
                        instant(()->telemetry.setProgress(12)),
                        follow(follower, Paths.line(pose1,pose2).linear(pose1,pose2)),
                        instant(()->telemetry.setProgress(25)),
                        follow(follower, Paths.line(pose2,pose3).linear(pose2,pose3)),
                        instant(()->telemetry.setProgress(50)),
                        follow(follower, Paths.line(pose3,pose4).linear(pose3,pose4)),
                        instant(()->telemetry.setProgress(75)),
                        follow(follower, Paths.line(pose4,pose5).linear(pose4,pose5)),
                        instant(()->telemetry.setProgress(100))
                ),
                Groups.sequential(
                        waitMs(timeOutMS),
                        instant(()->{timeouted = true;})
                )
        );
        Scheduler.schedule(c);
        while(Scheduler.isRunning(c)){
            Scheduler.execute();
            follower.update();
        }
        if(timeouted){
            failTest();
        }else{
            passTest();
        }
    }
}
