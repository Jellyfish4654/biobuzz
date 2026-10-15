package org.firstinspires.ftc.teamcode.framework;

import com.pedropathing.math.Pose;

public final class Storage {
    public static final Pose DEFAULT_POSE = new Pose(0, 0, Math.toRadians(90)); // Pointing away from audience (doesn't really matter)
    public static Pose currentPose = DEFAULT_POSE; // TODO: update every auto and teleop loop, apply at teleop start
    public static Alliance alliance = Alliance.UNKNOWN; // TODO: set in auto
    
    // TODO: check if these offsets and the default pose work as intended
    public enum Alliance {
        UNKNOWN(-DEFAULT_POSE.heading()),
        RED(0),
        BLUE(Math.toRadians(180));
        
        public final double headingOffset; // TODO: Use to align field centric to the driver's perspective
        
        Alliance(double headingOffset) {
            this.headingOffset = headingOffset;
        }
    }
    private Storage() {} // Cannot be instantiated
}