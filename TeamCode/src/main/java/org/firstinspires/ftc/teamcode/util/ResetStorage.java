package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.Storage;

@TeleOp(name = "Reset Storage")
public class ResetStorage extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Storage.poseBackup = Storage.DEFAULT_POSE;
        Storage.alliance = Storage.Alliance.UNKNOWN;
        Storage.balls = 0;
    }
}
