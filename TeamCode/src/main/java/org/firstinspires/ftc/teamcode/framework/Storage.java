package org.firstinspires.ftc.teamcode.framework;

import com.pedropathing.math.Pose;

public class Storage {
    public static final Pose DEFAULT_POSE = Pose.zero();
    public static Pose poseBackup = DEFAULT_POSE;
    public static Alliance alliance = Alliance.UNKNOWN; // TODO: set in auto
    
    // TODO: check if these offsets work as intended
    public enum Alliance {
        UNKNOWN(-DEFAULT_POSE.heading()),
        RED(0),
        BLUE(Math.toRadians(180));
        
        public final double headingOffset;
        
        Alliance(double headingOffset) {
            this.headingOffset = headingOffset;
        }
    }
}
