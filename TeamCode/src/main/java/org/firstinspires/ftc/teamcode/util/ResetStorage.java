package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.framework.Storage;

@Utility(name = "Reset Storage", description = "Resets the robot's current position and alliance to fix field centric teleop driving.")
public class ResetStorage extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Storage.poseBackup = Storage.DEFAULT_POSE;
        Storage.alliance = Storage.Alliance.UNKNOWN;
    }
}
