package org.firstinspires.ftc.teamcode.framework;

import com.pedropathing.math.Pose;

public class Storage {
    public static final Pose DEFAULT_POSE = Pose.zero();
    public static Pose poseBackup = DEFAULT_POSE;
    
    public static double balls = 0;
    
    public static Alliance alliance = Alliance.UNKNOWN;
    public enum Alliance {
        UNKNOWN(-DEFAULT_POSE.heading()),
        RED(0),
        BLUE(Math.toRadians(180));
        
        public final double headingOffset; // TODO: check if these offsets work as intended
        
        Alliance(double headingOffset) {
            this.headingOffset = headingOffset;
        }
    }
    
    public static final Pose
            CELL_RED_AUDIENCE = new Pose(57.7, 57.54),
            CELL_RED_SCORING = new Pose(57.7, 83.92),
            CELL_BLUE_AUDIENCE = new Pose(84.09, 57.54),
            CELL_BLUE_SCORING = new Pose(84.09, 83.92);
    
    // Position the AprilTag cluster, when tipped up, on the nearest alliance-specific cell, defaulting to red
    public static Pose targetCell(Pose robotPose) {
        Pose audience;
        Pose scoring;
        if (Storage.alliance == Storage.Alliance.BLUE) {
            audience = Storage.CELL_BLUE_AUDIENCE;
            scoring = Storage.CELL_BLUE_SCORING;
        } else { // defaults to red hive
            audience = Storage.CELL_RED_AUDIENCE;
            scoring = Storage.CELL_RED_SCORING;
        }
        
        return (robotPose.distance(audience) < robotPose.distance(scoring)) ? audience : scoring;
    }
}
