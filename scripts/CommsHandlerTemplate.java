package examplefuncsplayer;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;
    // CONSTS

    public class ArchonStatus {
        public static final int DEAD = 0;
        public static final int STANDBY_ODD = 1;
        public static final int STANDBY_EVEN = 2;
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
        public static final int MINOR_ENEMY = 3;
        public static final int MEDIUM_ENEMY = 4;
        public static final int MAJOR_ENEMY = 5;
        // Enemy_offset = first status representing enemy control
        public static final int ENEMY_OFFSET = 3;
        public static final int CLUSTER_ENEMY_LEVELS = 3;
    }
    public class ClaimStatus {
        public static final int UNCLAIMED = 0;
        public static final int CLAIMED = 1;
    }
    public class ReserveUnit {
        public static final int NONE = 0;
        public static final int MINER = 1;
        public static final int SOLDIER = 2;
        public static final int SAGE = 3;
        public static final int BUILDER = 4;
    }

    final int UNDEFINED_CLUSTER_INDEX = 127;

    public CommsHandler(RobotController rc) throws GameActionException {
        this.rc = rc;
    }

    public MapLocation getOurArchonLocation(int idx) throws GameActionException {
        return new MapLocation(readOurArchonXCoord(idx), readOurArchonYCoord(idx));
    }

    public void writeOurArchonLocation(int idx, MapLocation loc) throws GameActionException {
        writeOurArchonXCoord(idx, loc.x);
        writeOurArchonYCoord(idx, loc.y);
    }
    
    // PRIORITY CLUSTER INIT

    // TO BE GENERATED
}