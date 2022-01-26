package smite;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int BUILDER_REQUEST_SLOTS = 1;
    final int RESERVED_RESOURCES_SLOTS = 1;
    final int PRODUCTION_CONTROL_SLOTS = 1;
    final int LEAD_DELTA_SLOTS = 1;
    final int WORKER_COUNT_SLOTS = 1;
    final int FIGHTER_COUNT_SLOTS = 1;
    final int BUILDING_COUNT_SLOTS = 1;
    final int MAP_SLOTS = 1;
    final int STARTING_ARCHON_CENTROID_SLOTS = 1;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;

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
    public class ProductionControl {
        public static final int CONTINUE = 0;
        public static final int HALT = 1;
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
    
    public void initPriorityClusters() throws GameActionException {
        rc.writeSharedArray(48, 1023);
        rc.writeSharedArray(49, 65535);
        rc.writeSharedArray(50, 65535);
        rc.writeSharedArray(51, 65535);
        rc.writeSharedArray(52, 65527);
        rc.writeSharedArray(53, 63479);
        rc.writeSharedArray(54, 63479);
        rc.writeSharedArray(55, 63479);
        rc.writeSharedArray(56, 63479);
        rc.writeSharedArray(57, 63473);
        rc.writeSharedArray(58, 64639);
        rc.writeSharedArray(59, 8135);
        rc.writeSharedArray(60, 61948);
        rc.writeSharedArray(61, 32543);
        rc.writeSharedArray(62, 51185);
        rc.writeSharedArray(63, 64639);
    }
    

    public void resetAllClusterControlStatus() throws GameActionException {
        rc.writeSharedArray(10, rc.readSharedArray(10) & 65534);
        rc.writeSharedArray(11, rc.readSharedArray(11) & 31207);
        rc.writeSharedArray(12, rc.readSharedArray(12) & 40569);
        rc.writeSharedArray(13, rc.readSharedArray(13) & 59294);
        rc.writeSharedArray(14, rc.readSharedArray(14) & 31207);
        rc.writeSharedArray(15, rc.readSharedArray(15) & 40569);
        rc.writeSharedArray(16, rc.readSharedArray(16) & 59294);
        rc.writeSharedArray(17, rc.readSharedArray(17) & 31207);
        rc.writeSharedArray(18, rc.readSharedArray(18) & 40569);
        rc.writeSharedArray(19, rc.readSharedArray(19) & 59294);
        rc.writeSharedArray(20, rc.readSharedArray(20) & 31207);
        rc.writeSharedArray(21, rc.readSharedArray(21) & 40569);
        rc.writeSharedArray(22, rc.readSharedArray(22) & 59294);
        rc.writeSharedArray(23, rc.readSharedArray(23) & 31207);
        rc.writeSharedArray(24, rc.readSharedArray(24) & 40569);
        rc.writeSharedArray(25, rc.readSharedArray(25) & 59294);
        rc.writeSharedArray(26, rc.readSharedArray(26) & 31207);
        rc.writeSharedArray(27, rc.readSharedArray(27) & 40569);
        rc.writeSharedArray(28, rc.readSharedArray(28) & 59294);
        rc.writeSharedArray(29, rc.readSharedArray(29) & 31207);
        rc.writeSharedArray(30, rc.readSharedArray(30) & 40569);
        rc.writeSharedArray(31, rc.readSharedArray(31) & 59294);
        rc.writeSharedArray(32, rc.readSharedArray(32) & 31207);
        rc.writeSharedArray(33, rc.readSharedArray(33) & 40569);
        rc.writeSharedArray(34, rc.readSharedArray(34) & 59294);
        rc.writeSharedArray(35, rc.readSharedArray(35) & 31207);
        rc.writeSharedArray(36, rc.readSharedArray(36) & 40569);
        rc.writeSharedArray(37, rc.readSharedArray(37) & 59294);
        rc.writeSharedArray(38, rc.readSharedArray(38) & 31207);
        rc.writeSharedArray(39, rc.readSharedArray(39) & 40569);
        rc.writeSharedArray(40, rc.readSharedArray(40) & 59294);
        rc.writeSharedArray(41, rc.readSharedArray(41) & 31207);
        rc.writeSharedArray(42, rc.readSharedArray(42) & 40569);
        rc.writeSharedArray(43, rc.readSharedArray(43) & 59294);
        rc.writeSharedArray(44, rc.readSharedArray(44) & 31207);
        rc.writeSharedArray(45, rc.readSharedArray(45) & 40569);
        rc.writeSharedArray(46, rc.readSharedArray(46) & 59294);
        rc.writeSharedArray(47, rc.readSharedArray(47) & 31207);
        rc.writeSharedArray(48, rc.readSharedArray(48) & 40959);
    }

    public int readOurArchonStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 49152) >>> 14;
            case 1:
                return (rc.readSharedArray(1) & 49152) >>> 14;
            case 2:
                return (rc.readSharedArray(2) & 49152) >>> 14;
            case 3:
                return (rc.readSharedArray(3) & 49152) >>> 14;
            default:
                return -1;
        }
    }

    public void writeOurArchonStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 16383) | (value << 14));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 16383) | (value << 14));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 16383) | (value << 14));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 16383) | (value << 14));
                break;
        }
    }

    public int readOurArchonIsMoving(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 8192) >>> 13;
            case 1:
                return (rc.readSharedArray(1) & 8192) >>> 13;
            case 2:
                return (rc.readSharedArray(2) & 8192) >>> 13;
            case 3:
                return (rc.readSharedArray(3) & 8192) >>> 13;
            default:
                return -1;
        }
    }

    public void writeOurArchonIsMoving(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 57343) | (value << 13));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 57343) | (value << 13));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 57343) | (value << 13));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 57343) | (value << 13));
                break;
        }
    }

    public int readOurArchonAcceptingPatients(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 4096) >>> 12;
            case 1:
                return (rc.readSharedArray(1) & 4096) >>> 12;
            case 2:
                return (rc.readSharedArray(2) & 4096) >>> 12;
            case 3:
                return (rc.readSharedArray(3) & 4096) >>> 12;
            default:
                return -1;
        }
    }

    public void writeOurArchonAcceptingPatients(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 61439) | (value << 12));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 61439) | (value << 12));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 61439) | (value << 12));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 61439) | (value << 12));
                break;
        }
    }

    public int readOurArchonXCoord(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 4032) >>> 6;
            case 1:
                return (rc.readSharedArray(1) & 4032) >>> 6;
            case 2:
                return (rc.readSharedArray(2) & 4032) >>> 6;
            case 3:
                return (rc.readSharedArray(3) & 4032) >>> 6;
            default:
                return -1;
        }
    }

    public void writeOurArchonXCoord(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 61503) | (value << 6));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 61503) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 61503) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 61503) | (value << 6));
                break;
        }
    }

    public int readOurArchonYCoord(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 63);
            case 1:
                return (rc.readSharedArray(1) & 63);
            case 2:
                return (rc.readSharedArray(2) & 63);
            case 3:
                return (rc.readSharedArray(3) & 63);
            default:
                return -1;
        }
    }

    public void writeOurArchonYCoord(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 65472) | (value));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 65472) | (value));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 65472) | (value));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 65472) | (value));
                break;
        }
    }

    public int readOurArchonAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 65535);
            case 1:
                return (rc.readSharedArray(1) & 65535);
            case 2:
                return (rc.readSharedArray(2) & 65535);
            case 3:
                return (rc.readSharedArray(3) & 65535);
            default:
                return -1;
        }
    }

    public void writeOurArchonAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 0) | (value));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 0) | (value));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 0) | (value));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 0) | (value));
                break;
        }
    }

    public int readBuilderRequestWatchtower() throws GameActionException {
        return (rc.readSharedArray(4) & 32768) >>> 15;
    }

    public void writeBuilderRequestWatchtower(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 32767) | (value << 15));
    }

    public int readBuilderRequestType() throws GameActionException {
        return (rc.readSharedArray(4) & 28672) >>> 12;
    }

    public void writeBuilderRequestType(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 36863) | (value << 12));
    }

    public int readBuilderRequestXCoord() throws GameActionException {
        return (rc.readSharedArray(4) & 4032) >>> 6;
    }

    public void writeBuilderRequestXCoord(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 61503) | (value << 6));
    }

    public int readBuilderRequestYCoord() throws GameActionException {
        return (rc.readSharedArray(4) & 63);
    }

    public void writeBuilderRequestYCoord(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 65472) | (value));
    }

    public int readBuilderRequestAll() throws GameActionException {
        return (rc.readSharedArray(4) & 65535);
    }

    public void writeBuilderRequestAll(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 0) | (value));
    }

    public int readReservedResourcesLead() throws GameActionException {
        return (rc.readSharedArray(5) & 65472) >>> 6;
    }

    public void writeReservedResourcesLead(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 63) | (value << 6));
    }

    public int readReservedResourcesGold() throws GameActionException {
        return (rc.readSharedArray(5) & 63);
    }

    public void writeReservedResourcesGold(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 65472) | (value));
    }

    public int readReservedResourcesAll() throws GameActionException {
        return (rc.readSharedArray(5) & 65535);
    }

    public void writeReservedResourcesAll(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 0) | (value));
    }

    public int readProductionControlGold() throws GameActionException {
        return (rc.readSharedArray(6) & 32768) >>> 15;
    }

    public void writeProductionControlGold(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 32767) | (value << 15));
    }

    public int readProductionControlAll() throws GameActionException {
        return (rc.readSharedArray(6) & 32768) >>> 15;
    }

    public void writeProductionControlAll(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 32767) | (value << 15));
    }

    public int readLeadDelta() throws GameActionException {
        return (rc.readSharedArray(6) & 32767);
    }

    public void writeLeadDelta(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 32768) | (value));
    }

    public int readLeadDeltaAll() throws GameActionException {
        return (rc.readSharedArray(6) & 32767);
    }

    public void writeLeadDeltaAll(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 32768) | (value));
    }

    public int readWorkerCountMiners() throws GameActionException {
        return (rc.readSharedArray(7) & 65280) >>> 8;
    }

    public void writeWorkerCountMiners(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 255) | (value << 8));
    }

    public int readWorkerCountBuilders() throws GameActionException {
        return (rc.readSharedArray(7) & 255);
    }

    public void writeWorkerCountBuilders(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 65280) | (value));
    }

    public int readWorkerCountAll() throws GameActionException {
        return (rc.readSharedArray(7) & 65535);
    }

    public void writeWorkerCountAll(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 0) | (value));
    }

    public int readFighterCountSoldiers() throws GameActionException {
        return (rc.readSharedArray(8) & 65280) >>> 8;
    }

    public void writeFighterCountSoldiers(int value) throws GameActionException {
        rc.writeSharedArray(8, (rc.readSharedArray(8) & 255) | (value << 8));
    }

    public int readFighterCountSages() throws GameActionException {
        return (rc.readSharedArray(8) & 255);
    }

    public void writeFighterCountSages(int value) throws GameActionException {
        rc.writeSharedArray(8, (rc.readSharedArray(8) & 65280) | (value));
    }

    public int readFighterCountAll() throws GameActionException {
        return (rc.readSharedArray(8) & 65535);
    }

    public void writeFighterCountAll(int value) throws GameActionException {
        rc.writeSharedArray(8, (rc.readSharedArray(8) & 0) | (value));
    }

    public int readBuildingCountLaboratories() throws GameActionException {
        return (rc.readSharedArray(9) & 65280) >>> 8;
    }

    public void writeBuildingCountLaboratories(int value) throws GameActionException {
        rc.writeSharedArray(9, (rc.readSharedArray(9) & 255) | (value << 8));
    }

    public int readBuildingCountWatchtowers() throws GameActionException {
        return (rc.readSharedArray(9) & 255);
    }

    public void writeBuildingCountWatchtowers(int value) throws GameActionException {
        rc.writeSharedArray(9, (rc.readSharedArray(9) & 65280) | (value));
    }

    public int readBuildingCountAll() throws GameActionException {
        return (rc.readSharedArray(9) & 65535);
    }

    public void writeBuildingCountAll(int value) throws GameActionException {
        rc.writeSharedArray(9, (rc.readSharedArray(9) & 0) | (value));
    }

    public int readMapSymmetry() throws GameActionException {
        return (rc.readSharedArray(10) & 49152) >>> 14;
    }

    public void writeMapSymmetry(int value) throws GameActionException {
        rc.writeSharedArray(10, (rc.readSharedArray(10) & 16383) | (value << 14));
    }

    public int readMapAll() throws GameActionException {
        return (rc.readSharedArray(10) & 49152) >>> 14;
    }

    public void writeMapAll(int value) throws GameActionException {
        rc.writeSharedArray(10, (rc.readSharedArray(10) & 16383) | (value << 14));
    }

    public int readStartingArchonCentroidXCoord() throws GameActionException {
        return (rc.readSharedArray(10) & 16128) >>> 8;
    }

    public void writeStartingArchonCentroidXCoord(int value) throws GameActionException {
        rc.writeSharedArray(10, (rc.readSharedArray(10) & 49407) | (value << 8));
    }

    public int readStartingArchonCentroidYCoord() throws GameActionException {
        return (rc.readSharedArray(10) & 252) >>> 2;
    }

    public void writeStartingArchonCentroidYCoord(int value) throws GameActionException {
        rc.writeSharedArray(10, (rc.readSharedArray(10) & 65283) | (value << 2));
    }

    public int readStartingArchonCentroidAll() throws GameActionException {
        return (rc.readSharedArray(10) & 16380) >>> 2;
    }

    public void writeStartingArchonCentroidAll(int value) throws GameActionException {
        rc.writeSharedArray(10, (rc.readSharedArray(10) & 49155) | (value << 2));
    }

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(10) & 3) << 1) + ((rc.readSharedArray(11) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(11) & 3584) >>> 9;
            case 2:
                return (rc.readSharedArray(11) & 56) >>> 3;
            case 3:
                return (rc.readSharedArray(12) & 57344) >>> 13;
            case 4:
                return (rc.readSharedArray(12) & 896) >>> 7;
            case 5:
                return (rc.readSharedArray(12) & 14) >>> 1;
            case 6:
                return (rc.readSharedArray(13) & 14336) >>> 11;
            case 7:
                return (rc.readSharedArray(13) & 224) >>> 5;
            case 8:
                return ((rc.readSharedArray(13) & 3) << 1) + ((rc.readSharedArray(14) & 32768) >>> 15);
            case 9:
                return (rc.readSharedArray(14) & 3584) >>> 9;
            case 10:
                return (rc.readSharedArray(14) & 56) >>> 3;
            case 11:
                return (rc.readSharedArray(15) & 57344) >>> 13;
            case 12:
                return (rc.readSharedArray(15) & 896) >>> 7;
            case 13:
                return (rc.readSharedArray(15) & 14) >>> 1;
            case 14:
                return (rc.readSharedArray(16) & 14336) >>> 11;
            case 15:
                return (rc.readSharedArray(16) & 224) >>> 5;
            case 16:
                return ((rc.readSharedArray(16) & 3) << 1) + ((rc.readSharedArray(17) & 32768) >>> 15);
            case 17:
                return (rc.readSharedArray(17) & 3584) >>> 9;
            case 18:
                return (rc.readSharedArray(17) & 56) >>> 3;
            case 19:
                return (rc.readSharedArray(18) & 57344) >>> 13;
            case 20:
                return (rc.readSharedArray(18) & 896) >>> 7;
            case 21:
                return (rc.readSharedArray(18) & 14) >>> 1;
            case 22:
                return (rc.readSharedArray(19) & 14336) >>> 11;
            case 23:
                return (rc.readSharedArray(19) & 224) >>> 5;
            case 24:
                return ((rc.readSharedArray(19) & 3) << 1) + ((rc.readSharedArray(20) & 32768) >>> 15);
            case 25:
                return (rc.readSharedArray(20) & 3584) >>> 9;
            case 26:
                return (rc.readSharedArray(20) & 56) >>> 3;
            case 27:
                return (rc.readSharedArray(21) & 57344) >>> 13;
            case 28:
                return (rc.readSharedArray(21) & 896) >>> 7;
            case 29:
                return (rc.readSharedArray(21) & 14) >>> 1;
            case 30:
                return (rc.readSharedArray(22) & 14336) >>> 11;
            case 31:
                return (rc.readSharedArray(22) & 224) >>> 5;
            case 32:
                return ((rc.readSharedArray(22) & 3) << 1) + ((rc.readSharedArray(23) & 32768) >>> 15);
            case 33:
                return (rc.readSharedArray(23) & 3584) >>> 9;
            case 34:
                return (rc.readSharedArray(23) & 56) >>> 3;
            case 35:
                return (rc.readSharedArray(24) & 57344) >>> 13;
            case 36:
                return (rc.readSharedArray(24) & 896) >>> 7;
            case 37:
                return (rc.readSharedArray(24) & 14) >>> 1;
            case 38:
                return (rc.readSharedArray(25) & 14336) >>> 11;
            case 39:
                return (rc.readSharedArray(25) & 224) >>> 5;
            case 40:
                return ((rc.readSharedArray(25) & 3) << 1) + ((rc.readSharedArray(26) & 32768) >>> 15);
            case 41:
                return (rc.readSharedArray(26) & 3584) >>> 9;
            case 42:
                return (rc.readSharedArray(26) & 56) >>> 3;
            case 43:
                return (rc.readSharedArray(27) & 57344) >>> 13;
            case 44:
                return (rc.readSharedArray(27) & 896) >>> 7;
            case 45:
                return (rc.readSharedArray(27) & 14) >>> 1;
            case 46:
                return (rc.readSharedArray(28) & 14336) >>> 11;
            case 47:
                return (rc.readSharedArray(28) & 224) >>> 5;
            case 48:
                return ((rc.readSharedArray(28) & 3) << 1) + ((rc.readSharedArray(29) & 32768) >>> 15);
            case 49:
                return (rc.readSharedArray(29) & 3584) >>> 9;
            case 50:
                return (rc.readSharedArray(29) & 56) >>> 3;
            case 51:
                return (rc.readSharedArray(30) & 57344) >>> 13;
            case 52:
                return (rc.readSharedArray(30) & 896) >>> 7;
            case 53:
                return (rc.readSharedArray(30) & 14) >>> 1;
            case 54:
                return (rc.readSharedArray(31) & 14336) >>> 11;
            case 55:
                return (rc.readSharedArray(31) & 224) >>> 5;
            case 56:
                return ((rc.readSharedArray(31) & 3) << 1) + ((rc.readSharedArray(32) & 32768) >>> 15);
            case 57:
                return (rc.readSharedArray(32) & 3584) >>> 9;
            case 58:
                return (rc.readSharedArray(32) & 56) >>> 3;
            case 59:
                return (rc.readSharedArray(33) & 57344) >>> 13;
            case 60:
                return (rc.readSharedArray(33) & 896) >>> 7;
            case 61:
                return (rc.readSharedArray(33) & 14) >>> 1;
            case 62:
                return (rc.readSharedArray(34) & 14336) >>> 11;
            case 63:
                return (rc.readSharedArray(34) & 224) >>> 5;
            case 64:
                return ((rc.readSharedArray(34) & 3) << 1) + ((rc.readSharedArray(35) & 32768) >>> 15);
            case 65:
                return (rc.readSharedArray(35) & 3584) >>> 9;
            case 66:
                return (rc.readSharedArray(35) & 56) >>> 3;
            case 67:
                return (rc.readSharedArray(36) & 57344) >>> 13;
            case 68:
                return (rc.readSharedArray(36) & 896) >>> 7;
            case 69:
                return (rc.readSharedArray(36) & 14) >>> 1;
            case 70:
                return (rc.readSharedArray(37) & 14336) >>> 11;
            case 71:
                return (rc.readSharedArray(37) & 224) >>> 5;
            case 72:
                return ((rc.readSharedArray(37) & 3) << 1) + ((rc.readSharedArray(38) & 32768) >>> 15);
            case 73:
                return (rc.readSharedArray(38) & 3584) >>> 9;
            case 74:
                return (rc.readSharedArray(38) & 56) >>> 3;
            case 75:
                return (rc.readSharedArray(39) & 57344) >>> 13;
            case 76:
                return (rc.readSharedArray(39) & 896) >>> 7;
            case 77:
                return (rc.readSharedArray(39) & 14) >>> 1;
            case 78:
                return (rc.readSharedArray(40) & 14336) >>> 11;
            case 79:
                return (rc.readSharedArray(40) & 224) >>> 5;
            case 80:
                return ((rc.readSharedArray(40) & 3) << 1) + ((rc.readSharedArray(41) & 32768) >>> 15);
            case 81:
                return (rc.readSharedArray(41) & 3584) >>> 9;
            case 82:
                return (rc.readSharedArray(41) & 56) >>> 3;
            case 83:
                return (rc.readSharedArray(42) & 57344) >>> 13;
            case 84:
                return (rc.readSharedArray(42) & 896) >>> 7;
            case 85:
                return (rc.readSharedArray(42) & 14) >>> 1;
            case 86:
                return (rc.readSharedArray(43) & 14336) >>> 11;
            case 87:
                return (rc.readSharedArray(43) & 224) >>> 5;
            case 88:
                return ((rc.readSharedArray(43) & 3) << 1) + ((rc.readSharedArray(44) & 32768) >>> 15);
            case 89:
                return (rc.readSharedArray(44) & 3584) >>> 9;
            case 90:
                return (rc.readSharedArray(44) & 56) >>> 3;
            case 91:
                return (rc.readSharedArray(45) & 57344) >>> 13;
            case 92:
                return (rc.readSharedArray(45) & 896) >>> 7;
            case 93:
                return (rc.readSharedArray(45) & 14) >>> 1;
            case 94:
                return (rc.readSharedArray(46) & 14336) >>> 11;
            case 95:
                return (rc.readSharedArray(46) & 224) >>> 5;
            case 96:
                return ((rc.readSharedArray(46) & 3) << 1) + ((rc.readSharedArray(47) & 32768) >>> 15);
            case 97:
                return (rc.readSharedArray(47) & 3584) >>> 9;
            case 98:
                return (rc.readSharedArray(47) & 56) >>> 3;
            case 99:
                return (rc.readSharedArray(48) & 57344) >>> 13;
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 61951) | (value << 9));
                break;
            case 2:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65479) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 8191) | (value << 13));
                break;
            case 4:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 64639) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65521) | (value << 1));
                break;
            case 6:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 51199) | (value << 11));
                break;
            case 7:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65311) | (value << 5));
                break;
            case 8:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 32767) | ((value & 1) << 15));
                break;
            case 9:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 61951) | (value << 9));
                break;
            case 10:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65479) | (value << 3));
                break;
            case 11:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 8191) | (value << 13));
                break;
            case 12:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 64639) | (value << 7));
                break;
            case 13:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65521) | (value << 1));
                break;
            case 14:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 51199) | (value << 11));
                break;
            case 15:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65311) | (value << 5));
                break;
            case 16:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 32767) | ((value & 1) << 15));
                break;
            case 17:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 61951) | (value << 9));
                break;
            case 18:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65479) | (value << 3));
                break;
            case 19:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 8191) | (value << 13));
                break;
            case 20:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 64639) | (value << 7));
                break;
            case 21:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65521) | (value << 1));
                break;
            case 22:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 51199) | (value << 11));
                break;
            case 23:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65311) | (value << 5));
                break;
            case 24:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 32767) | ((value & 1) << 15));
                break;
            case 25:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 61951) | (value << 9));
                break;
            case 26:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65479) | (value << 3));
                break;
            case 27:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 8191) | (value << 13));
                break;
            case 28:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 64639) | (value << 7));
                break;
            case 29:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65521) | (value << 1));
                break;
            case 30:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 51199) | (value << 11));
                break;
            case 31:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65311) | (value << 5));
                break;
            case 32:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 32767) | ((value & 1) << 15));
                break;
            case 33:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 61951) | (value << 9));
                break;
            case 34:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65479) | (value << 3));
                break;
            case 35:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 8191) | (value << 13));
                break;
            case 36:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 64639) | (value << 7));
                break;
            case 37:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65521) | (value << 1));
                break;
            case 38:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 51199) | (value << 11));
                break;
            case 39:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65311) | (value << 5));
                break;
            case 40:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 32767) | ((value & 1) << 15));
                break;
            case 41:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 61951) | (value << 9));
                break;
            case 42:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65479) | (value << 3));
                break;
            case 43:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 8191) | (value << 13));
                break;
            case 44:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 64639) | (value << 7));
                break;
            case 45:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65521) | (value << 1));
                break;
            case 46:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 51199) | (value << 11));
                break;
            case 47:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65311) | (value << 5));
                break;
            case 48:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 32767) | ((value & 1) << 15));
                break;
            case 49:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 61951) | (value << 9));
                break;
            case 50:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65479) | (value << 3));
                break;
            case 51:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 8191) | (value << 13));
                break;
            case 52:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 64639) | (value << 7));
                break;
            case 53:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65521) | (value << 1));
                break;
            case 54:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 51199) | (value << 11));
                break;
            case 55:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65311) | (value << 5));
                break;
            case 56:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 32767) | ((value & 1) << 15));
                break;
            case 57:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 61951) | (value << 9));
                break;
            case 58:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65479) | (value << 3));
                break;
            case 59:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 8191) | (value << 13));
                break;
            case 60:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 64639) | (value << 7));
                break;
            case 61:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65521) | (value << 1));
                break;
            case 62:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 51199) | (value << 11));
                break;
            case 63:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65311) | (value << 5));
                break;
            case 64:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 32767) | ((value & 1) << 15));
                break;
            case 65:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 61951) | (value << 9));
                break;
            case 66:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65479) | (value << 3));
                break;
            case 67:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 8191) | (value << 13));
                break;
            case 68:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 64639) | (value << 7));
                break;
            case 69:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65521) | (value << 1));
                break;
            case 70:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 51199) | (value << 11));
                break;
            case 71:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65311) | (value << 5));
                break;
            case 72:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 32767) | ((value & 1) << 15));
                break;
            case 73:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 61951) | (value << 9));
                break;
            case 74:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65479) | (value << 3));
                break;
            case 75:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 8191) | (value << 13));
                break;
            case 76:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 64639) | (value << 7));
                break;
            case 77:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65521) | (value << 1));
                break;
            case 78:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 51199) | (value << 11));
                break;
            case 79:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65311) | (value << 5));
                break;
            case 80:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 32767) | ((value & 1) << 15));
                break;
            case 81:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 61951) | (value << 9));
                break;
            case 82:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65479) | (value << 3));
                break;
            case 83:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 8191) | (value << 13));
                break;
            case 84:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 64639) | (value << 7));
                break;
            case 85:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65521) | (value << 1));
                break;
            case 86:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 51199) | (value << 11));
                break;
            case 87:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65311) | (value << 5));
                break;
            case 88:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 32767) | ((value & 1) << 15));
                break;
            case 89:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 61951) | (value << 9));
                break;
            case 90:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65479) | (value << 3));
                break;
            case 91:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 8191) | (value << 13));
                break;
            case 92:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 64639) | (value << 7));
                break;
            case 93:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65521) | (value << 1));
                break;
            case 94:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 51199) | (value << 11));
                break;
            case 95:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65311) | (value << 5));
                break;
            case 96:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 32767) | ((value & 1) << 15));
                break;
            case 97:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 61951) | (value << 9));
                break;
            case 98:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65479) | (value << 3));
                break;
            case 99:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 8191) | (value << 13));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(11) & 28672) >>> 12;
            case 1:
                return (rc.readSharedArray(11) & 448) >>> 6;
            case 2:
                return (rc.readSharedArray(11) & 7);
            case 3:
                return (rc.readSharedArray(12) & 7168) >>> 10;
            case 4:
                return (rc.readSharedArray(12) & 112) >>> 4;
            case 5:
                return ((rc.readSharedArray(12) & 1) << 2) + ((rc.readSharedArray(13) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(13) & 1792) >>> 8;
            case 7:
                return (rc.readSharedArray(13) & 28) >>> 2;
            case 8:
                return (rc.readSharedArray(14) & 28672) >>> 12;
            case 9:
                return (rc.readSharedArray(14) & 448) >>> 6;
            case 10:
                return (rc.readSharedArray(14) & 7);
            case 11:
                return (rc.readSharedArray(15) & 7168) >>> 10;
            case 12:
                return (rc.readSharedArray(15) & 112) >>> 4;
            case 13:
                return ((rc.readSharedArray(15) & 1) << 2) + ((rc.readSharedArray(16) & 49152) >>> 14);
            case 14:
                return (rc.readSharedArray(16) & 1792) >>> 8;
            case 15:
                return (rc.readSharedArray(16) & 28) >>> 2;
            case 16:
                return (rc.readSharedArray(17) & 28672) >>> 12;
            case 17:
                return (rc.readSharedArray(17) & 448) >>> 6;
            case 18:
                return (rc.readSharedArray(17) & 7);
            case 19:
                return (rc.readSharedArray(18) & 7168) >>> 10;
            case 20:
                return (rc.readSharedArray(18) & 112) >>> 4;
            case 21:
                return ((rc.readSharedArray(18) & 1) << 2) + ((rc.readSharedArray(19) & 49152) >>> 14);
            case 22:
                return (rc.readSharedArray(19) & 1792) >>> 8;
            case 23:
                return (rc.readSharedArray(19) & 28) >>> 2;
            case 24:
                return (rc.readSharedArray(20) & 28672) >>> 12;
            case 25:
                return (rc.readSharedArray(20) & 448) >>> 6;
            case 26:
                return (rc.readSharedArray(20) & 7);
            case 27:
                return (rc.readSharedArray(21) & 7168) >>> 10;
            case 28:
                return (rc.readSharedArray(21) & 112) >>> 4;
            case 29:
                return ((rc.readSharedArray(21) & 1) << 2) + ((rc.readSharedArray(22) & 49152) >>> 14);
            case 30:
                return (rc.readSharedArray(22) & 1792) >>> 8;
            case 31:
                return (rc.readSharedArray(22) & 28) >>> 2;
            case 32:
                return (rc.readSharedArray(23) & 28672) >>> 12;
            case 33:
                return (rc.readSharedArray(23) & 448) >>> 6;
            case 34:
                return (rc.readSharedArray(23) & 7);
            case 35:
                return (rc.readSharedArray(24) & 7168) >>> 10;
            case 36:
                return (rc.readSharedArray(24) & 112) >>> 4;
            case 37:
                return ((rc.readSharedArray(24) & 1) << 2) + ((rc.readSharedArray(25) & 49152) >>> 14);
            case 38:
                return (rc.readSharedArray(25) & 1792) >>> 8;
            case 39:
                return (rc.readSharedArray(25) & 28) >>> 2;
            case 40:
                return (rc.readSharedArray(26) & 28672) >>> 12;
            case 41:
                return (rc.readSharedArray(26) & 448) >>> 6;
            case 42:
                return (rc.readSharedArray(26) & 7);
            case 43:
                return (rc.readSharedArray(27) & 7168) >>> 10;
            case 44:
                return (rc.readSharedArray(27) & 112) >>> 4;
            case 45:
                return ((rc.readSharedArray(27) & 1) << 2) + ((rc.readSharedArray(28) & 49152) >>> 14);
            case 46:
                return (rc.readSharedArray(28) & 1792) >>> 8;
            case 47:
                return (rc.readSharedArray(28) & 28) >>> 2;
            case 48:
                return (rc.readSharedArray(29) & 28672) >>> 12;
            case 49:
                return (rc.readSharedArray(29) & 448) >>> 6;
            case 50:
                return (rc.readSharedArray(29) & 7);
            case 51:
                return (rc.readSharedArray(30) & 7168) >>> 10;
            case 52:
                return (rc.readSharedArray(30) & 112) >>> 4;
            case 53:
                return ((rc.readSharedArray(30) & 1) << 2) + ((rc.readSharedArray(31) & 49152) >>> 14);
            case 54:
                return (rc.readSharedArray(31) & 1792) >>> 8;
            case 55:
                return (rc.readSharedArray(31) & 28) >>> 2;
            case 56:
                return (rc.readSharedArray(32) & 28672) >>> 12;
            case 57:
                return (rc.readSharedArray(32) & 448) >>> 6;
            case 58:
                return (rc.readSharedArray(32) & 7);
            case 59:
                return (rc.readSharedArray(33) & 7168) >>> 10;
            case 60:
                return (rc.readSharedArray(33) & 112) >>> 4;
            case 61:
                return ((rc.readSharedArray(33) & 1) << 2) + ((rc.readSharedArray(34) & 49152) >>> 14);
            case 62:
                return (rc.readSharedArray(34) & 1792) >>> 8;
            case 63:
                return (rc.readSharedArray(34) & 28) >>> 2;
            case 64:
                return (rc.readSharedArray(35) & 28672) >>> 12;
            case 65:
                return (rc.readSharedArray(35) & 448) >>> 6;
            case 66:
                return (rc.readSharedArray(35) & 7);
            case 67:
                return (rc.readSharedArray(36) & 7168) >>> 10;
            case 68:
                return (rc.readSharedArray(36) & 112) >>> 4;
            case 69:
                return ((rc.readSharedArray(36) & 1) << 2) + ((rc.readSharedArray(37) & 49152) >>> 14);
            case 70:
                return (rc.readSharedArray(37) & 1792) >>> 8;
            case 71:
                return (rc.readSharedArray(37) & 28) >>> 2;
            case 72:
                return (rc.readSharedArray(38) & 28672) >>> 12;
            case 73:
                return (rc.readSharedArray(38) & 448) >>> 6;
            case 74:
                return (rc.readSharedArray(38) & 7);
            case 75:
                return (rc.readSharedArray(39) & 7168) >>> 10;
            case 76:
                return (rc.readSharedArray(39) & 112) >>> 4;
            case 77:
                return ((rc.readSharedArray(39) & 1) << 2) + ((rc.readSharedArray(40) & 49152) >>> 14);
            case 78:
                return (rc.readSharedArray(40) & 1792) >>> 8;
            case 79:
                return (rc.readSharedArray(40) & 28) >>> 2;
            case 80:
                return (rc.readSharedArray(41) & 28672) >>> 12;
            case 81:
                return (rc.readSharedArray(41) & 448) >>> 6;
            case 82:
                return (rc.readSharedArray(41) & 7);
            case 83:
                return (rc.readSharedArray(42) & 7168) >>> 10;
            case 84:
                return (rc.readSharedArray(42) & 112) >>> 4;
            case 85:
                return ((rc.readSharedArray(42) & 1) << 2) + ((rc.readSharedArray(43) & 49152) >>> 14);
            case 86:
                return (rc.readSharedArray(43) & 1792) >>> 8;
            case 87:
                return (rc.readSharedArray(43) & 28) >>> 2;
            case 88:
                return (rc.readSharedArray(44) & 28672) >>> 12;
            case 89:
                return (rc.readSharedArray(44) & 448) >>> 6;
            case 90:
                return (rc.readSharedArray(44) & 7);
            case 91:
                return (rc.readSharedArray(45) & 7168) >>> 10;
            case 92:
                return (rc.readSharedArray(45) & 112) >>> 4;
            case 93:
                return ((rc.readSharedArray(45) & 1) << 2) + ((rc.readSharedArray(46) & 49152) >>> 14);
            case 94:
                return (rc.readSharedArray(46) & 1792) >>> 8;
            case 95:
                return (rc.readSharedArray(46) & 28) >>> 2;
            case 96:
                return (rc.readSharedArray(47) & 28672) >>> 12;
            case 97:
                return (rc.readSharedArray(47) & 448) >>> 6;
            case 98:
                return (rc.readSharedArray(47) & 7);
            case 99:
                return (rc.readSharedArray(48) & 7168) >>> 10;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 36863) | (value << 12));
                break;
            case 1:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65087) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65528) | (value));
                break;
            case 3:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 58367) | (value << 10));
                break;
            case 4:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65423) | (value << 4));
                break;
            case 5:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 63743) | (value << 8));
                break;
            case 7:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65507) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 36863) | (value << 12));
                break;
            case 9:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65087) | (value << 6));
                break;
            case 10:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65528) | (value));
                break;
            case 11:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 58367) | (value << 10));
                break;
            case 12:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65423) | (value << 4));
                break;
            case 13:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 16383) | ((value & 3) << 14));
                break;
            case 14:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 63743) | (value << 8));
                break;
            case 15:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65507) | (value << 2));
                break;
            case 16:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 36863) | (value << 12));
                break;
            case 17:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65087) | (value << 6));
                break;
            case 18:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65528) | (value));
                break;
            case 19:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 58367) | (value << 10));
                break;
            case 20:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65423) | (value << 4));
                break;
            case 21:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 16383) | ((value & 3) << 14));
                break;
            case 22:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 63743) | (value << 8));
                break;
            case 23:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65507) | (value << 2));
                break;
            case 24:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 36863) | (value << 12));
                break;
            case 25:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65087) | (value << 6));
                break;
            case 26:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65528) | (value));
                break;
            case 27:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 58367) | (value << 10));
                break;
            case 28:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65423) | (value << 4));
                break;
            case 29:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 16383) | ((value & 3) << 14));
                break;
            case 30:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 63743) | (value << 8));
                break;
            case 31:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65507) | (value << 2));
                break;
            case 32:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 36863) | (value << 12));
                break;
            case 33:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65087) | (value << 6));
                break;
            case 34:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65528) | (value));
                break;
            case 35:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 58367) | (value << 10));
                break;
            case 36:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65423) | (value << 4));
                break;
            case 37:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 16383) | ((value & 3) << 14));
                break;
            case 38:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 63743) | (value << 8));
                break;
            case 39:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65507) | (value << 2));
                break;
            case 40:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 36863) | (value << 12));
                break;
            case 41:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65087) | (value << 6));
                break;
            case 42:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65528) | (value));
                break;
            case 43:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 58367) | (value << 10));
                break;
            case 44:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65423) | (value << 4));
                break;
            case 45:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 16383) | ((value & 3) << 14));
                break;
            case 46:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 63743) | (value << 8));
                break;
            case 47:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65507) | (value << 2));
                break;
            case 48:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 36863) | (value << 12));
                break;
            case 49:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65087) | (value << 6));
                break;
            case 50:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65528) | (value));
                break;
            case 51:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 58367) | (value << 10));
                break;
            case 52:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65423) | (value << 4));
                break;
            case 53:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 16383) | ((value & 3) << 14));
                break;
            case 54:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 63743) | (value << 8));
                break;
            case 55:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65507) | (value << 2));
                break;
            case 56:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 36863) | (value << 12));
                break;
            case 57:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65087) | (value << 6));
                break;
            case 58:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65528) | (value));
                break;
            case 59:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 58367) | (value << 10));
                break;
            case 60:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65423) | (value << 4));
                break;
            case 61:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 16383) | ((value & 3) << 14));
                break;
            case 62:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 63743) | (value << 8));
                break;
            case 63:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65507) | (value << 2));
                break;
            case 64:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 36863) | (value << 12));
                break;
            case 65:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65087) | (value << 6));
                break;
            case 66:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65528) | (value));
                break;
            case 67:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 58367) | (value << 10));
                break;
            case 68:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65423) | (value << 4));
                break;
            case 69:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 16383) | ((value & 3) << 14));
                break;
            case 70:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 63743) | (value << 8));
                break;
            case 71:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65507) | (value << 2));
                break;
            case 72:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 36863) | (value << 12));
                break;
            case 73:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65087) | (value << 6));
                break;
            case 74:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65528) | (value));
                break;
            case 75:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 58367) | (value << 10));
                break;
            case 76:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65423) | (value << 4));
                break;
            case 77:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 16383) | ((value & 3) << 14));
                break;
            case 78:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 63743) | (value << 8));
                break;
            case 79:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65507) | (value << 2));
                break;
            case 80:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 36863) | (value << 12));
                break;
            case 81:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65087) | (value << 6));
                break;
            case 82:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65528) | (value));
                break;
            case 83:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 58367) | (value << 10));
                break;
            case 84:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65423) | (value << 4));
                break;
            case 85:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 16383) | ((value & 3) << 14));
                break;
            case 86:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 63743) | (value << 8));
                break;
            case 87:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65507) | (value << 2));
                break;
            case 88:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 36863) | (value << 12));
                break;
            case 89:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65087) | (value << 6));
                break;
            case 90:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65528) | (value));
                break;
            case 91:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 58367) | (value << 10));
                break;
            case 92:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65423) | (value << 4));
                break;
            case 93:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 16383) | ((value & 3) << 14));
                break;
            case 94:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 63743) | (value << 8));
                break;
            case 95:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65507) | (value << 2));
                break;
            case 96:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 36863) | (value << 12));
                break;
            case 97:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65087) | (value << 6));
                break;
            case 98:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65528) | (value));
                break;
            case 99:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 58367) | (value << 10));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(10) & 3) << 4) + ((rc.readSharedArray(11) & 61440) >>> 12);
            case 1:
                return (rc.readSharedArray(11) & 4032) >>> 6;
            case 2:
                return (rc.readSharedArray(11) & 63);
            case 3:
                return (rc.readSharedArray(12) & 64512) >>> 10;
            case 4:
                return (rc.readSharedArray(12) & 1008) >>> 4;
            case 5:
                return ((rc.readSharedArray(12) & 15) << 2) + ((rc.readSharedArray(13) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(13) & 16128) >>> 8;
            case 7:
                return (rc.readSharedArray(13) & 252) >>> 2;
            case 8:
                return ((rc.readSharedArray(13) & 3) << 4) + ((rc.readSharedArray(14) & 61440) >>> 12);
            case 9:
                return (rc.readSharedArray(14) & 4032) >>> 6;
            case 10:
                return (rc.readSharedArray(14) & 63);
            case 11:
                return (rc.readSharedArray(15) & 64512) >>> 10;
            case 12:
                return (rc.readSharedArray(15) & 1008) >>> 4;
            case 13:
                return ((rc.readSharedArray(15) & 15) << 2) + ((rc.readSharedArray(16) & 49152) >>> 14);
            case 14:
                return (rc.readSharedArray(16) & 16128) >>> 8;
            case 15:
                return (rc.readSharedArray(16) & 252) >>> 2;
            case 16:
                return ((rc.readSharedArray(16) & 3) << 4) + ((rc.readSharedArray(17) & 61440) >>> 12);
            case 17:
                return (rc.readSharedArray(17) & 4032) >>> 6;
            case 18:
                return (rc.readSharedArray(17) & 63);
            case 19:
                return (rc.readSharedArray(18) & 64512) >>> 10;
            case 20:
                return (rc.readSharedArray(18) & 1008) >>> 4;
            case 21:
                return ((rc.readSharedArray(18) & 15) << 2) + ((rc.readSharedArray(19) & 49152) >>> 14);
            case 22:
                return (rc.readSharedArray(19) & 16128) >>> 8;
            case 23:
                return (rc.readSharedArray(19) & 252) >>> 2;
            case 24:
                return ((rc.readSharedArray(19) & 3) << 4) + ((rc.readSharedArray(20) & 61440) >>> 12);
            case 25:
                return (rc.readSharedArray(20) & 4032) >>> 6;
            case 26:
                return (rc.readSharedArray(20) & 63);
            case 27:
                return (rc.readSharedArray(21) & 64512) >>> 10;
            case 28:
                return (rc.readSharedArray(21) & 1008) >>> 4;
            case 29:
                return ((rc.readSharedArray(21) & 15) << 2) + ((rc.readSharedArray(22) & 49152) >>> 14);
            case 30:
                return (rc.readSharedArray(22) & 16128) >>> 8;
            case 31:
                return (rc.readSharedArray(22) & 252) >>> 2;
            case 32:
                return ((rc.readSharedArray(22) & 3) << 4) + ((rc.readSharedArray(23) & 61440) >>> 12);
            case 33:
                return (rc.readSharedArray(23) & 4032) >>> 6;
            case 34:
                return (rc.readSharedArray(23) & 63);
            case 35:
                return (rc.readSharedArray(24) & 64512) >>> 10;
            case 36:
                return (rc.readSharedArray(24) & 1008) >>> 4;
            case 37:
                return ((rc.readSharedArray(24) & 15) << 2) + ((rc.readSharedArray(25) & 49152) >>> 14);
            case 38:
                return (rc.readSharedArray(25) & 16128) >>> 8;
            case 39:
                return (rc.readSharedArray(25) & 252) >>> 2;
            case 40:
                return ((rc.readSharedArray(25) & 3) << 4) + ((rc.readSharedArray(26) & 61440) >>> 12);
            case 41:
                return (rc.readSharedArray(26) & 4032) >>> 6;
            case 42:
                return (rc.readSharedArray(26) & 63);
            case 43:
                return (rc.readSharedArray(27) & 64512) >>> 10;
            case 44:
                return (rc.readSharedArray(27) & 1008) >>> 4;
            case 45:
                return ((rc.readSharedArray(27) & 15) << 2) + ((rc.readSharedArray(28) & 49152) >>> 14);
            case 46:
                return (rc.readSharedArray(28) & 16128) >>> 8;
            case 47:
                return (rc.readSharedArray(28) & 252) >>> 2;
            case 48:
                return ((rc.readSharedArray(28) & 3) << 4) + ((rc.readSharedArray(29) & 61440) >>> 12);
            case 49:
                return (rc.readSharedArray(29) & 4032) >>> 6;
            case 50:
                return (rc.readSharedArray(29) & 63);
            case 51:
                return (rc.readSharedArray(30) & 64512) >>> 10;
            case 52:
                return (rc.readSharedArray(30) & 1008) >>> 4;
            case 53:
                return ((rc.readSharedArray(30) & 15) << 2) + ((rc.readSharedArray(31) & 49152) >>> 14);
            case 54:
                return (rc.readSharedArray(31) & 16128) >>> 8;
            case 55:
                return (rc.readSharedArray(31) & 252) >>> 2;
            case 56:
                return ((rc.readSharedArray(31) & 3) << 4) + ((rc.readSharedArray(32) & 61440) >>> 12);
            case 57:
                return (rc.readSharedArray(32) & 4032) >>> 6;
            case 58:
                return (rc.readSharedArray(32) & 63);
            case 59:
                return (rc.readSharedArray(33) & 64512) >>> 10;
            case 60:
                return (rc.readSharedArray(33) & 1008) >>> 4;
            case 61:
                return ((rc.readSharedArray(33) & 15) << 2) + ((rc.readSharedArray(34) & 49152) >>> 14);
            case 62:
                return (rc.readSharedArray(34) & 16128) >>> 8;
            case 63:
                return (rc.readSharedArray(34) & 252) >>> 2;
            case 64:
                return ((rc.readSharedArray(34) & 3) << 4) + ((rc.readSharedArray(35) & 61440) >>> 12);
            case 65:
                return (rc.readSharedArray(35) & 4032) >>> 6;
            case 66:
                return (rc.readSharedArray(35) & 63);
            case 67:
                return (rc.readSharedArray(36) & 64512) >>> 10;
            case 68:
                return (rc.readSharedArray(36) & 1008) >>> 4;
            case 69:
                return ((rc.readSharedArray(36) & 15) << 2) + ((rc.readSharedArray(37) & 49152) >>> 14);
            case 70:
                return (rc.readSharedArray(37) & 16128) >>> 8;
            case 71:
                return (rc.readSharedArray(37) & 252) >>> 2;
            case 72:
                return ((rc.readSharedArray(37) & 3) << 4) + ((rc.readSharedArray(38) & 61440) >>> 12);
            case 73:
                return (rc.readSharedArray(38) & 4032) >>> 6;
            case 74:
                return (rc.readSharedArray(38) & 63);
            case 75:
                return (rc.readSharedArray(39) & 64512) >>> 10;
            case 76:
                return (rc.readSharedArray(39) & 1008) >>> 4;
            case 77:
                return ((rc.readSharedArray(39) & 15) << 2) + ((rc.readSharedArray(40) & 49152) >>> 14);
            case 78:
                return (rc.readSharedArray(40) & 16128) >>> 8;
            case 79:
                return (rc.readSharedArray(40) & 252) >>> 2;
            case 80:
                return ((rc.readSharedArray(40) & 3) << 4) + ((rc.readSharedArray(41) & 61440) >>> 12);
            case 81:
                return (rc.readSharedArray(41) & 4032) >>> 6;
            case 82:
                return (rc.readSharedArray(41) & 63);
            case 83:
                return (rc.readSharedArray(42) & 64512) >>> 10;
            case 84:
                return (rc.readSharedArray(42) & 1008) >>> 4;
            case 85:
                return ((rc.readSharedArray(42) & 15) << 2) + ((rc.readSharedArray(43) & 49152) >>> 14);
            case 86:
                return (rc.readSharedArray(43) & 16128) >>> 8;
            case 87:
                return (rc.readSharedArray(43) & 252) >>> 2;
            case 88:
                return ((rc.readSharedArray(43) & 3) << 4) + ((rc.readSharedArray(44) & 61440) >>> 12);
            case 89:
                return (rc.readSharedArray(44) & 4032) >>> 6;
            case 90:
                return (rc.readSharedArray(44) & 63);
            case 91:
                return (rc.readSharedArray(45) & 64512) >>> 10;
            case 92:
                return (rc.readSharedArray(45) & 1008) >>> 4;
            case 93:
                return ((rc.readSharedArray(45) & 15) << 2) + ((rc.readSharedArray(46) & 49152) >>> 14);
            case 94:
                return (rc.readSharedArray(46) & 16128) >>> 8;
            case 95:
                return (rc.readSharedArray(46) & 252) >>> 2;
            case 96:
                return ((rc.readSharedArray(46) & 3) << 4) + ((rc.readSharedArray(47) & 61440) >>> 12);
            case 97:
                return (rc.readSharedArray(47) & 4032) >>> 6;
            case 98:
                return (rc.readSharedArray(47) & 63);
            case 99:
                return (rc.readSharedArray(48) & 64512) >>> 10;
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 4095) | ((value & 15) << 12));
                break;
            case 1:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 61503) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65472) | (value));
                break;
            case 3:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 1023) | (value << 10));
                break;
            case 4:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 64527) | (value << 4));
                break;
            case 5:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 49407) | (value << 8));
                break;
            case 7:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65283) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 4095) | ((value & 15) << 12));
                break;
            case 9:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 61503) | (value << 6));
                break;
            case 10:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65472) | (value));
                break;
            case 11:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 1023) | (value << 10));
                break;
            case 12:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 64527) | (value << 4));
                break;
            case 13:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 16383) | ((value & 3) << 14));
                break;
            case 14:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 49407) | (value << 8));
                break;
            case 15:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65283) | (value << 2));
                break;
            case 16:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 4095) | ((value & 15) << 12));
                break;
            case 17:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 61503) | (value << 6));
                break;
            case 18:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65472) | (value));
                break;
            case 19:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 1023) | (value << 10));
                break;
            case 20:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 64527) | (value << 4));
                break;
            case 21:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 16383) | ((value & 3) << 14));
                break;
            case 22:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 49407) | (value << 8));
                break;
            case 23:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65283) | (value << 2));
                break;
            case 24:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 4095) | ((value & 15) << 12));
                break;
            case 25:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 61503) | (value << 6));
                break;
            case 26:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65472) | (value));
                break;
            case 27:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 1023) | (value << 10));
                break;
            case 28:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 64527) | (value << 4));
                break;
            case 29:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 16383) | ((value & 3) << 14));
                break;
            case 30:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 49407) | (value << 8));
                break;
            case 31:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65283) | (value << 2));
                break;
            case 32:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 4095) | ((value & 15) << 12));
                break;
            case 33:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 61503) | (value << 6));
                break;
            case 34:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65472) | (value));
                break;
            case 35:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 1023) | (value << 10));
                break;
            case 36:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 64527) | (value << 4));
                break;
            case 37:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 16383) | ((value & 3) << 14));
                break;
            case 38:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 49407) | (value << 8));
                break;
            case 39:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65283) | (value << 2));
                break;
            case 40:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 4095) | ((value & 15) << 12));
                break;
            case 41:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 61503) | (value << 6));
                break;
            case 42:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65472) | (value));
                break;
            case 43:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 1023) | (value << 10));
                break;
            case 44:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 64527) | (value << 4));
                break;
            case 45:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 16383) | ((value & 3) << 14));
                break;
            case 46:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 49407) | (value << 8));
                break;
            case 47:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65283) | (value << 2));
                break;
            case 48:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 4095) | ((value & 15) << 12));
                break;
            case 49:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 61503) | (value << 6));
                break;
            case 50:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65472) | (value));
                break;
            case 51:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 1023) | (value << 10));
                break;
            case 52:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 64527) | (value << 4));
                break;
            case 53:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 16383) | ((value & 3) << 14));
                break;
            case 54:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 49407) | (value << 8));
                break;
            case 55:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65283) | (value << 2));
                break;
            case 56:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 4095) | ((value & 15) << 12));
                break;
            case 57:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 61503) | (value << 6));
                break;
            case 58:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65472) | (value));
                break;
            case 59:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 1023) | (value << 10));
                break;
            case 60:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 64527) | (value << 4));
                break;
            case 61:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 16383) | ((value & 3) << 14));
                break;
            case 62:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 49407) | (value << 8));
                break;
            case 63:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65283) | (value << 2));
                break;
            case 64:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 4095) | ((value & 15) << 12));
                break;
            case 65:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 61503) | (value << 6));
                break;
            case 66:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65472) | (value));
                break;
            case 67:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 1023) | (value << 10));
                break;
            case 68:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 64527) | (value << 4));
                break;
            case 69:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 16383) | ((value & 3) << 14));
                break;
            case 70:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 49407) | (value << 8));
                break;
            case 71:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65283) | (value << 2));
                break;
            case 72:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 4095) | ((value & 15) << 12));
                break;
            case 73:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 61503) | (value << 6));
                break;
            case 74:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65472) | (value));
                break;
            case 75:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 1023) | (value << 10));
                break;
            case 76:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 64527) | (value << 4));
                break;
            case 77:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 16383) | ((value & 3) << 14));
                break;
            case 78:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 49407) | (value << 8));
                break;
            case 79:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65283) | (value << 2));
                break;
            case 80:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 4095) | ((value & 15) << 12));
                break;
            case 81:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 61503) | (value << 6));
                break;
            case 82:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65472) | (value));
                break;
            case 83:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 1023) | (value << 10));
                break;
            case 84:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 64527) | (value << 4));
                break;
            case 85:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 16383) | ((value & 3) << 14));
                break;
            case 86:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 49407) | (value << 8));
                break;
            case 87:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65283) | (value << 2));
                break;
            case 88:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 4095) | ((value & 15) << 12));
                break;
            case 89:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 61503) | (value << 6));
                break;
            case 90:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65472) | (value));
                break;
            case 91:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 1023) | (value << 10));
                break;
            case 92:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 64527) | (value << 4));
                break;
            case 93:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 16383) | ((value & 3) << 14));
                break;
            case 94:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 49407) | (value << 8));
                break;
            case 95:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65283) | (value << 2));
                break;
            case 96:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 4095) | ((value & 15) << 12));
                break;
            case 97:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 61503) | (value << 6));
                break;
            case 98:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65472) | (value));
                break;
            case 99:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 1023) | (value << 10));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(48) & 1016) >>> 3;
            case 1:
                return ((rc.readSharedArray(48) & 7) << 4) + ((rc.readSharedArray(49) & 61440) >>> 12);
            case 2:
                return (rc.readSharedArray(49) & 4064) >>> 5;
            case 3:
                return ((rc.readSharedArray(49) & 31) << 2) + ((rc.readSharedArray(50) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(50) & 16256) >>> 7;
            case 5:
                return (rc.readSharedArray(50) & 127);
            case 6:
                return (rc.readSharedArray(51) & 65024) >>> 9;
            case 7:
                return (rc.readSharedArray(51) & 508) >>> 2;
            case 8:
                return ((rc.readSharedArray(51) & 3) << 5) + ((rc.readSharedArray(52) & 63488) >>> 11);
            case 9:
                return (rc.readSharedArray(52) & 2032) >>> 4;
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64519) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 4095) | ((value & 15) << 12));
                break;
            case 2:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 61471) | (value << 5));
                break;
            case 3:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 49279) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65408) | (value));
                break;
            case 6:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 511) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 2047) | ((value & 31) << 11));
                break;
            case 9:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 63503) | (value << 4));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(48) & 1016) >>> 3;
            case 1:
                return ((rc.readSharedArray(48) & 7) << 4) + ((rc.readSharedArray(49) & 61440) >>> 12);
            case 2:
                return (rc.readSharedArray(49) & 4064) >>> 5;
            case 3:
                return ((rc.readSharedArray(49) & 31) << 2) + ((rc.readSharedArray(50) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(50) & 16256) >>> 7;
            case 5:
                return (rc.readSharedArray(50) & 127);
            case 6:
                return (rc.readSharedArray(51) & 65024) >>> 9;
            case 7:
                return (rc.readSharedArray(51) & 508) >>> 2;
            case 8:
                return ((rc.readSharedArray(51) & 3) << 5) + ((rc.readSharedArray(52) & 63488) >>> 11);
            case 9:
                return (rc.readSharedArray(52) & 2032) >>> 4;
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64519) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 4095) | ((value & 15) << 12));
                break;
            case 2:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 61471) | (value << 5));
                break;
            case 3:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 49279) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65408) | (value));
                break;
            case 6:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 511) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 2047) | ((value & 31) << 11));
                break;
            case 9:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 63503) | (value << 4));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(52) & 8) >>> 3;
            case 1:
                return (rc.readSharedArray(53) & 2048) >>> 11;
            case 2:
                return (rc.readSharedArray(53) & 8) >>> 3;
            case 3:
                return (rc.readSharedArray(54) & 2048) >>> 11;
            case 4:
                return (rc.readSharedArray(54) & 8) >>> 3;
            case 5:
                return (rc.readSharedArray(55) & 2048) >>> 11;
            case 6:
                return (rc.readSharedArray(55) & 8) >>> 3;
            case 7:
                return (rc.readSharedArray(56) & 2048) >>> 11;
            case 8:
                return (rc.readSharedArray(56) & 8) >>> 3;
            case 9:
                return (rc.readSharedArray(57) & 2048) >>> 11;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65527) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 63487) | (value << 11));
                break;
            case 2:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65527) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 63487) | (value << 11));
                break;
            case 4:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65527) | (value << 3));
                break;
            case 5:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 63487) | (value << 11));
                break;
            case 6:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65527) | (value << 3));
                break;
            case 7:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 63487) | (value << 11));
                break;
            case 8:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65527) | (value << 3));
                break;
            case 9:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 63487) | (value << 11));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(52) & 7) << 4) + ((rc.readSharedArray(53) & 61440) >>> 12);
            case 1:
                return (rc.readSharedArray(53) & 2032) >>> 4;
            case 2:
                return ((rc.readSharedArray(53) & 7) << 4) + ((rc.readSharedArray(54) & 61440) >>> 12);
            case 3:
                return (rc.readSharedArray(54) & 2032) >>> 4;
            case 4:
                return ((rc.readSharedArray(54) & 7) << 4) + ((rc.readSharedArray(55) & 61440) >>> 12);
            case 5:
                return (rc.readSharedArray(55) & 2032) >>> 4;
            case 6:
                return ((rc.readSharedArray(55) & 7) << 4) + ((rc.readSharedArray(56) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(56) & 2032) >>> 4;
            case 8:
                return ((rc.readSharedArray(56) & 7) << 4) + ((rc.readSharedArray(57) & 61440) >>> 12);
            case 9:
                return (rc.readSharedArray(57) & 2032) >>> 4;
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 4095) | ((value & 15) << 12));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 63503) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 4095) | ((value & 15) << 12));
                break;
            case 3:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 63503) | (value << 4));
                break;
            case 4:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 4095) | ((value & 15) << 12));
                break;
            case 5:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 63503) | (value << 4));
                break;
            case 6:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 63503) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 4095) | ((value & 15) << 12));
                break;
            case 9:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 63503) | (value << 4));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(52) & 15) << 4) + ((rc.readSharedArray(53) & 61440) >>> 12);
            case 1:
                return (rc.readSharedArray(53) & 4080) >>> 4;
            case 2:
                return ((rc.readSharedArray(53) & 15) << 4) + ((rc.readSharedArray(54) & 61440) >>> 12);
            case 3:
                return (rc.readSharedArray(54) & 4080) >>> 4;
            case 4:
                return ((rc.readSharedArray(54) & 15) << 4) + ((rc.readSharedArray(55) & 61440) >>> 12);
            case 5:
                return (rc.readSharedArray(55) & 4080) >>> 4;
            case 6:
                return ((rc.readSharedArray(55) & 15) << 4) + ((rc.readSharedArray(56) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(56) & 4080) >>> 4;
            case 8:
                return ((rc.readSharedArray(56) & 15) << 4) + ((rc.readSharedArray(57) & 61440) >>> 12);
            case 9:
                return (rc.readSharedArray(57) & 4080) >>> 4;
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65520) | ((value & 240) >>> 4));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 4095) | ((value & 15) << 12));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 61455) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65520) | ((value & 240) >>> 4));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 4095) | ((value & 15) << 12));
                break;
            case 3:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 61455) | (value << 4));
                break;
            case 4:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65520) | ((value & 240) >>> 4));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 4095) | ((value & 15) << 12));
                break;
            case 5:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 61455) | (value << 4));
                break;
            case 6:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65520) | ((value & 240) >>> 4));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 61455) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65520) | ((value & 240) >>> 4));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 4095) | ((value & 15) << 12));
                break;
            case 9:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 61455) | (value << 4));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(57) & 14) >>> 1;
            case 1:
                return (rc.readSharedArray(58) & 896) >>> 7;
            case 2:
                return (rc.readSharedArray(59) & 57344) >>> 13;
            case 3:
                return (rc.readSharedArray(59) & 56) >>> 3;
            case 4:
                return (rc.readSharedArray(60) & 3584) >>> 9;
            case 5:
                return ((rc.readSharedArray(60) & 3) << 1) + ((rc.readSharedArray(61) & 32768) >>> 15);
            case 6:
                return (rc.readSharedArray(61) & 224) >>> 5;
            case 7:
                return (rc.readSharedArray(62) & 14336) >>> 11;
            case 8:
                return (rc.readSharedArray(62) & 14) >>> 1;
            case 9:
                return (rc.readSharedArray(63) & 896) >>> 7;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65521) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 64639) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 8191) | (value << 13));
                break;
            case 3:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65479) | (value << 3));
                break;
            case 4:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 61951) | (value << 9));
                break;
            case 5:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 32767) | ((value & 1) << 15));
                break;
            case 6:
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 65311) | (value << 5));
                break;
            case 7:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 51199) | (value << 11));
                break;
            case 8:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 65521) | (value << 1));
                break;
            case 9:
                rc.writeSharedArray(63, (rc.readSharedArray(63) & 64639) | (value << 7));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(57) & 1) << 6) + ((rc.readSharedArray(58) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(58) & 127);
            case 2:
                return (rc.readSharedArray(59) & 8128) >>> 6;
            case 3:
                return ((rc.readSharedArray(59) & 7) << 4) + ((rc.readSharedArray(60) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(60) & 508) >>> 2;
            case 5:
                return (rc.readSharedArray(61) & 32512) >>> 8;
            case 6:
                return ((rc.readSharedArray(61) & 31) << 2) + ((rc.readSharedArray(62) & 49152) >>> 14);
            case 7:
                return (rc.readSharedArray(62) & 2032) >>> 4;
            case 8:
                return ((rc.readSharedArray(62) & 1) << 6) + ((rc.readSharedArray(63) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(63) & 127);
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65408) | (value));
                break;
            case 2:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 57407) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65027) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 33023) | (value << 8));
                break;
            case 6:
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 16383) | ((value & 3) << 14));
                break;
            case 7:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 63503) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(63, (rc.readSharedArray(63) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(63, (rc.readSharedArray(63) & 65408) | (value));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(57) & 15) << 6) + ((rc.readSharedArray(58) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(58) & 1023);
            case 2:
                return (rc.readSharedArray(59) & 65472) >>> 6;
            case 3:
                return ((rc.readSharedArray(59) & 63) << 4) + ((rc.readSharedArray(60) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(60) & 4092) >>> 2;
            case 5:
                return ((rc.readSharedArray(60) & 3) << 8) + ((rc.readSharedArray(61) & 65280) >>> 8);
            case 6:
                return ((rc.readSharedArray(61) & 255) << 2) + ((rc.readSharedArray(62) & 49152) >>> 14);
            case 7:
                return (rc.readSharedArray(62) & 16368) >>> 4;
            case 8:
                return ((rc.readSharedArray(62) & 15) << 6) + ((rc.readSharedArray(63) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(63) & 1023);
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 64512) | (value));
                break;
            case 2:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 63) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 61443) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 255) | ((value & 255) << 8));
                break;
            case 6:
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 16383) | ((value & 3) << 14));
                break;
            case 7:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 49167) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(62, (rc.readSharedArray(62) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(63, (rc.readSharedArray(63) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(63, (rc.readSharedArray(63) & 64512) | (value));
                break;
        }
    }
}