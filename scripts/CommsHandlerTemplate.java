package examplefuncsplayer;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;
    // CONSTS

    public class ArchonStatus {
        public static final int DEAD = 0;
        public static final int STANDBY_ODD = 1;
        public static final int STANDBY_EVEN = 2;
        public static final int STATIONARY = 0;
        public static final int MOVING = 1;
        public static final int NOT_ACCEPTING_PATIENTS = 0;
        public static final int ACCEPTING_PATIENTS = 1;
    }
    public class MapSymmetry {
        public static final int UNKNOWN = 0;
        public static final int HORIZONTAL = 1;
        public static final int VERTICAL = 2;
        public static final int ROTATIONAL = 3;
    }
    public class ControlStatus {
        public static final int UNKNOWN = 0;
        public static final int OURS = 1;
        public static final int EXPLORING = 2;
        public static final int THEIRS = 4;
    }
    public class ClaimStatus {
        public static final int UNCLAIMED = 0;
        public static final int CLAIMED = 1;
    }
    public class BuilderRequest {
        public static final int NONE = 0; // default
        public static final int LABORATORY_LEVEL_2 = 2; // from lab
        public static final int LABORATORY_LEVEL_3 = 3; // from lab
        public static final int ARCHON_LEVEL_2 = 4; // from lab
        public static final int ARCHON_LEVEL_3 = 5; // from lab
    }

    final int UNDEFINED_CLUSTER_INDEX = 127;

    public CommsHandler(RobotController rc) throws GameActionException {
        this.rc = rc;
    }

    public MapLocation readOurArchonLocation(int idx) throws GameActionException {
        return new MapLocation(readOurArchonXCoord(idx), readOurArchonYCoord(idx));
    }

    public void writeOurArchonLocation(int idx, MapLocation loc) throws GameActionException {
        writeOurArchonXCoord(idx, loc.x);
        writeOurArchonYCoord(idx, loc.y);
    }
    
    // PRIORITY CLUSTER INIT
    
    // CLUSTER CONTROL STATUS RESET

    // MAIN READ AND WRITE METHODS
}