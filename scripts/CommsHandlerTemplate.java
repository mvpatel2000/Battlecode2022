package examplefuncsplayer;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;
    // CONSTS

    public class ArchonStatus {
        public static final int DEAD = 0;
        public static final int STANDBY = 1;
        public static final int UNDER_ATTACK = 2;
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
        public static final int THEIRS = 2;
        public static final int EXPLORING = 3;
    }
    public class ClaimStatus {
        public static final int UNCLAIMED = 0;
        public static final int CLAIMED = 1;
    }

    final int UNDEFINED_CLUSTER_INDEX = 127;

    public CommsHandler(RobotController rc) throws GameActionException {
        this.rc = rc;
    }

    /**
     * Initial setup of comms which wipes the clusters
     * @throws GameActionException
     */
    public void clearShortlist() throws GameActionException {
        for (int i = 0; i < COMBAT_CLUSTER_SLOTS; i++) {
            writeCombatClusterIndex(i, UNDEFINED_CLUSTER_INDEX);
        }
        for (int i = 0; i < EXPLORE_CLUSTER_SLOTS; i++) {
            writeExploreClusterIndex(i, UNDEFINED_CLUSTER_INDEX);
        }
        for (int i = 0; i < MINE_CLUSTER_SLOTS; i++) {
            writeMineClusterIndex(i, UNDEFINED_CLUSTER_INDEX);
        }
    }

    public MapLocation getOurArchonLocation(int idx) throws GameActionException {
        return new MapLocation(readOurArchonXCoord(idx), readOurArchonYCoord(idx));
    }

    public void writeOurArchonLocation(int idx, MapLocation loc) throws GameActionException {
        writeOurArchonXCoord(idx, loc.x);
        writeOurArchonYCoord(idx, loc.y);
    }

    // TO BE GENERATED
}