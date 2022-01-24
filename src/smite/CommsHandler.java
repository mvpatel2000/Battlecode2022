package smite;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int BUILDER_REQUEST_SLOTS = 1;
    final int WORKER_COUNT_SLOTS = 1;
    final int FIGHTER_COUNT_SLOTS = 1;
    final int BUILDING_COUNT_SLOTS = 1;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;
    final int LAST_ARCHON_SLOTS = 1;
    final int RESERVED_RESOURCES_SLOTS = 1;
    final int MAP_SLOTS = 1;
    final int PRODUCTION_CONTROL_SLOTS = 1;

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
        rc.writeSharedArray(45, 255);
        rc.writeSharedArray(46, 65535);
        rc.writeSharedArray(47, 65535);
        rc.writeSharedArray(48, 65535);
        rc.writeSharedArray(49, 65533);
        rc.writeSharedArray(50, 65021);
        rc.writeSharedArray(51, 65021);
        rc.writeSharedArray(52, 65021);
        rc.writeSharedArray(53, 65021);
        rc.writeSharedArray(54, 65020);
        rc.writeSharedArray(55, 32543);
        rc.writeSharedArray(56, 51185);
        rc.writeSharedArray(57, 64639);
        rc.writeSharedArray(58, 8135);
        rc.writeSharedArray(59, 61948);
        rc.writeSharedArray(60, 32543);
        rc.writeSharedArray(61, 49152);
    }
    

    public void resetAllClusterControlStatus() throws GameActionException {
        rc.writeSharedArray(8, rc.readSharedArray(8) & 40569);
        rc.writeSharedArray(9, rc.readSharedArray(9) & 59294);
        rc.writeSharedArray(10, rc.readSharedArray(10) & 31207);
        rc.writeSharedArray(11, rc.readSharedArray(11) & 40569);
        rc.writeSharedArray(12, rc.readSharedArray(12) & 59294);
        rc.writeSharedArray(13, rc.readSharedArray(13) & 31207);
        rc.writeSharedArray(14, rc.readSharedArray(14) & 40569);
        rc.writeSharedArray(15, rc.readSharedArray(15) & 59294);
        rc.writeSharedArray(16, rc.readSharedArray(16) & 31207);
        rc.writeSharedArray(17, rc.readSharedArray(17) & 40569);
        rc.writeSharedArray(18, rc.readSharedArray(18) & 59294);
        rc.writeSharedArray(19, rc.readSharedArray(19) & 31207);
        rc.writeSharedArray(20, rc.readSharedArray(20) & 40569);
        rc.writeSharedArray(21, rc.readSharedArray(21) & 59294);
        rc.writeSharedArray(22, rc.readSharedArray(22) & 31207);
        rc.writeSharedArray(23, rc.readSharedArray(23) & 40569);
        rc.writeSharedArray(24, rc.readSharedArray(24) & 59294);
        rc.writeSharedArray(25, rc.readSharedArray(25) & 31207);
        rc.writeSharedArray(26, rc.readSharedArray(26) & 40569);
        rc.writeSharedArray(27, rc.readSharedArray(27) & 59294);
        rc.writeSharedArray(28, rc.readSharedArray(28) & 31207);
        rc.writeSharedArray(29, rc.readSharedArray(29) & 40569);
        rc.writeSharedArray(30, rc.readSharedArray(30) & 59294);
        rc.writeSharedArray(31, rc.readSharedArray(31) & 31207);
        rc.writeSharedArray(32, rc.readSharedArray(32) & 40569);
        rc.writeSharedArray(33, rc.readSharedArray(33) & 59294);
        rc.writeSharedArray(34, rc.readSharedArray(34) & 31207);
        rc.writeSharedArray(35, rc.readSharedArray(35) & 40569);
        rc.writeSharedArray(36, rc.readSharedArray(36) & 59294);
        rc.writeSharedArray(37, rc.readSharedArray(37) & 31207);
        rc.writeSharedArray(38, rc.readSharedArray(38) & 40569);
        rc.writeSharedArray(39, rc.readSharedArray(39) & 59294);
        rc.writeSharedArray(40, rc.readSharedArray(40) & 31207);
        rc.writeSharedArray(41, rc.readSharedArray(41) & 40569);
        rc.writeSharedArray(42, rc.readSharedArray(42) & 59294);
        rc.writeSharedArray(43, rc.readSharedArray(43) & 31207);
        rc.writeSharedArray(44, rc.readSharedArray(44) & 40569);
        rc.writeSharedArray(45, rc.readSharedArray(45) & 59391);
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

    public int readWorkerCountMiners() throws GameActionException {
        return (rc.readSharedArray(5) & 65280) >>> 8;
    }

    public void writeWorkerCountMiners(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 255) | (value << 8));
    }

    public int readWorkerCountBuilders() throws GameActionException {
        return (rc.readSharedArray(5) & 255);
    }

    public void writeWorkerCountBuilders(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 65280) | (value));
    }

    public int readWorkerCountAll() throws GameActionException {
        return (rc.readSharedArray(5) & 65535);
    }

    public void writeWorkerCountAll(int value) throws GameActionException {
        rc.writeSharedArray(5, (rc.readSharedArray(5) & 0) | (value));
    }

    public int readFighterCountSoldiers() throws GameActionException {
        return (rc.readSharedArray(6) & 65280) >>> 8;
    }

    public void writeFighterCountSoldiers(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 255) | (value << 8));
    }

    public int readFighterCountSages() throws GameActionException {
        return (rc.readSharedArray(6) & 255);
    }

    public void writeFighterCountSages(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 65280) | (value));
    }

    public int readFighterCountAll() throws GameActionException {
        return (rc.readSharedArray(6) & 65535);
    }

    public void writeFighterCountAll(int value) throws GameActionException {
        rc.writeSharedArray(6, (rc.readSharedArray(6) & 0) | (value));
    }

    public int readBuildingCountLaboratories() throws GameActionException {
        return (rc.readSharedArray(7) & 65280) >>> 8;
    }

    public void writeBuildingCountLaboratories(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 255) | (value << 8));
    }

    public int readBuildingCountWatchtowers() throws GameActionException {
        return (rc.readSharedArray(7) & 255);
    }

    public void writeBuildingCountWatchtowers(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 65280) | (value));
    }

    public int readBuildingCountAll() throws GameActionException {
        return (rc.readSharedArray(7) & 65535);
    }

    public void writeBuildingCountAll(int value) throws GameActionException {
        rc.writeSharedArray(7, (rc.readSharedArray(7) & 0) | (value));
    }

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(8) & 57344) >>> 13;
            case 1:
                return (rc.readSharedArray(8) & 896) >>> 7;
            case 2:
                return (rc.readSharedArray(8) & 14) >>> 1;
            case 3:
                return (rc.readSharedArray(9) & 14336) >>> 11;
            case 4:
                return (rc.readSharedArray(9) & 224) >>> 5;
            case 5:
                return ((rc.readSharedArray(9) & 3) << 1) + ((rc.readSharedArray(10) & 32768) >>> 15);
            case 6:
                return (rc.readSharedArray(10) & 3584) >>> 9;
            case 7:
                return (rc.readSharedArray(10) & 56) >>> 3;
            case 8:
                return (rc.readSharedArray(11) & 57344) >>> 13;
            case 9:
                return (rc.readSharedArray(11) & 896) >>> 7;
            case 10:
                return (rc.readSharedArray(11) & 14) >>> 1;
            case 11:
                return (rc.readSharedArray(12) & 14336) >>> 11;
            case 12:
                return (rc.readSharedArray(12) & 224) >>> 5;
            case 13:
                return ((rc.readSharedArray(12) & 3) << 1) + ((rc.readSharedArray(13) & 32768) >>> 15);
            case 14:
                return (rc.readSharedArray(13) & 3584) >>> 9;
            case 15:
                return (rc.readSharedArray(13) & 56) >>> 3;
            case 16:
                return (rc.readSharedArray(14) & 57344) >>> 13;
            case 17:
                return (rc.readSharedArray(14) & 896) >>> 7;
            case 18:
                return (rc.readSharedArray(14) & 14) >>> 1;
            case 19:
                return (rc.readSharedArray(15) & 14336) >>> 11;
            case 20:
                return (rc.readSharedArray(15) & 224) >>> 5;
            case 21:
                return ((rc.readSharedArray(15) & 3) << 1) + ((rc.readSharedArray(16) & 32768) >>> 15);
            case 22:
                return (rc.readSharedArray(16) & 3584) >>> 9;
            case 23:
                return (rc.readSharedArray(16) & 56) >>> 3;
            case 24:
                return (rc.readSharedArray(17) & 57344) >>> 13;
            case 25:
                return (rc.readSharedArray(17) & 896) >>> 7;
            case 26:
                return (rc.readSharedArray(17) & 14) >>> 1;
            case 27:
                return (rc.readSharedArray(18) & 14336) >>> 11;
            case 28:
                return (rc.readSharedArray(18) & 224) >>> 5;
            case 29:
                return ((rc.readSharedArray(18) & 3) << 1) + ((rc.readSharedArray(19) & 32768) >>> 15);
            case 30:
                return (rc.readSharedArray(19) & 3584) >>> 9;
            case 31:
                return (rc.readSharedArray(19) & 56) >>> 3;
            case 32:
                return (rc.readSharedArray(20) & 57344) >>> 13;
            case 33:
                return (rc.readSharedArray(20) & 896) >>> 7;
            case 34:
                return (rc.readSharedArray(20) & 14) >>> 1;
            case 35:
                return (rc.readSharedArray(21) & 14336) >>> 11;
            case 36:
                return (rc.readSharedArray(21) & 224) >>> 5;
            case 37:
                return ((rc.readSharedArray(21) & 3) << 1) + ((rc.readSharedArray(22) & 32768) >>> 15);
            case 38:
                return (rc.readSharedArray(22) & 3584) >>> 9;
            case 39:
                return (rc.readSharedArray(22) & 56) >>> 3;
            case 40:
                return (rc.readSharedArray(23) & 57344) >>> 13;
            case 41:
                return (rc.readSharedArray(23) & 896) >>> 7;
            case 42:
                return (rc.readSharedArray(23) & 14) >>> 1;
            case 43:
                return (rc.readSharedArray(24) & 14336) >>> 11;
            case 44:
                return (rc.readSharedArray(24) & 224) >>> 5;
            case 45:
                return ((rc.readSharedArray(24) & 3) << 1) + ((rc.readSharedArray(25) & 32768) >>> 15);
            case 46:
                return (rc.readSharedArray(25) & 3584) >>> 9;
            case 47:
                return (rc.readSharedArray(25) & 56) >>> 3;
            case 48:
                return (rc.readSharedArray(26) & 57344) >>> 13;
            case 49:
                return (rc.readSharedArray(26) & 896) >>> 7;
            case 50:
                return (rc.readSharedArray(26) & 14) >>> 1;
            case 51:
                return (rc.readSharedArray(27) & 14336) >>> 11;
            case 52:
                return (rc.readSharedArray(27) & 224) >>> 5;
            case 53:
                return ((rc.readSharedArray(27) & 3) << 1) + ((rc.readSharedArray(28) & 32768) >>> 15);
            case 54:
                return (rc.readSharedArray(28) & 3584) >>> 9;
            case 55:
                return (rc.readSharedArray(28) & 56) >>> 3;
            case 56:
                return (rc.readSharedArray(29) & 57344) >>> 13;
            case 57:
                return (rc.readSharedArray(29) & 896) >>> 7;
            case 58:
                return (rc.readSharedArray(29) & 14) >>> 1;
            case 59:
                return (rc.readSharedArray(30) & 14336) >>> 11;
            case 60:
                return (rc.readSharedArray(30) & 224) >>> 5;
            case 61:
                return ((rc.readSharedArray(30) & 3) << 1) + ((rc.readSharedArray(31) & 32768) >>> 15);
            case 62:
                return (rc.readSharedArray(31) & 3584) >>> 9;
            case 63:
                return (rc.readSharedArray(31) & 56) >>> 3;
            case 64:
                return (rc.readSharedArray(32) & 57344) >>> 13;
            case 65:
                return (rc.readSharedArray(32) & 896) >>> 7;
            case 66:
                return (rc.readSharedArray(32) & 14) >>> 1;
            case 67:
                return (rc.readSharedArray(33) & 14336) >>> 11;
            case 68:
                return (rc.readSharedArray(33) & 224) >>> 5;
            case 69:
                return ((rc.readSharedArray(33) & 3) << 1) + ((rc.readSharedArray(34) & 32768) >>> 15);
            case 70:
                return (rc.readSharedArray(34) & 3584) >>> 9;
            case 71:
                return (rc.readSharedArray(34) & 56) >>> 3;
            case 72:
                return (rc.readSharedArray(35) & 57344) >>> 13;
            case 73:
                return (rc.readSharedArray(35) & 896) >>> 7;
            case 74:
                return (rc.readSharedArray(35) & 14) >>> 1;
            case 75:
                return (rc.readSharedArray(36) & 14336) >>> 11;
            case 76:
                return (rc.readSharedArray(36) & 224) >>> 5;
            case 77:
                return ((rc.readSharedArray(36) & 3) << 1) + ((rc.readSharedArray(37) & 32768) >>> 15);
            case 78:
                return (rc.readSharedArray(37) & 3584) >>> 9;
            case 79:
                return (rc.readSharedArray(37) & 56) >>> 3;
            case 80:
                return (rc.readSharedArray(38) & 57344) >>> 13;
            case 81:
                return (rc.readSharedArray(38) & 896) >>> 7;
            case 82:
                return (rc.readSharedArray(38) & 14) >>> 1;
            case 83:
                return (rc.readSharedArray(39) & 14336) >>> 11;
            case 84:
                return (rc.readSharedArray(39) & 224) >>> 5;
            case 85:
                return ((rc.readSharedArray(39) & 3) << 1) + ((rc.readSharedArray(40) & 32768) >>> 15);
            case 86:
                return (rc.readSharedArray(40) & 3584) >>> 9;
            case 87:
                return (rc.readSharedArray(40) & 56) >>> 3;
            case 88:
                return (rc.readSharedArray(41) & 57344) >>> 13;
            case 89:
                return (rc.readSharedArray(41) & 896) >>> 7;
            case 90:
                return (rc.readSharedArray(41) & 14) >>> 1;
            case 91:
                return (rc.readSharedArray(42) & 14336) >>> 11;
            case 92:
                return (rc.readSharedArray(42) & 224) >>> 5;
            case 93:
                return ((rc.readSharedArray(42) & 3) << 1) + ((rc.readSharedArray(43) & 32768) >>> 15);
            case 94:
                return (rc.readSharedArray(43) & 3584) >>> 9;
            case 95:
                return (rc.readSharedArray(43) & 56) >>> 3;
            case 96:
                return (rc.readSharedArray(44) & 57344) >>> 13;
            case 97:
                return (rc.readSharedArray(44) & 896) >>> 7;
            case 98:
                return (rc.readSharedArray(44) & 14) >>> 1;
            case 99:
                return (rc.readSharedArray(45) & 14336) >>> 11;
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 8191) | (value << 13));
                break;
            case 1:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64639) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65521) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 51199) | (value << 11));
                break;
            case 4:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65311) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 32767) | ((value & 1) << 15));
                break;
            case 6:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 61951) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65479) | (value << 3));
                break;
            case 8:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 8191) | (value << 13));
                break;
            case 9:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 64639) | (value << 7));
                break;
            case 10:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65521) | (value << 1));
                break;
            case 11:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 51199) | (value << 11));
                break;
            case 12:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65311) | (value << 5));
                break;
            case 13:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 32767) | ((value & 1) << 15));
                break;
            case 14:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 61951) | (value << 9));
                break;
            case 15:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65479) | (value << 3));
                break;
            case 16:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 8191) | (value << 13));
                break;
            case 17:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 64639) | (value << 7));
                break;
            case 18:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65521) | (value << 1));
                break;
            case 19:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 51199) | (value << 11));
                break;
            case 20:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65311) | (value << 5));
                break;
            case 21:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 32767) | ((value & 1) << 15));
                break;
            case 22:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 61951) | (value << 9));
                break;
            case 23:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65479) | (value << 3));
                break;
            case 24:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 8191) | (value << 13));
                break;
            case 25:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 64639) | (value << 7));
                break;
            case 26:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65521) | (value << 1));
                break;
            case 27:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 51199) | (value << 11));
                break;
            case 28:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65311) | (value << 5));
                break;
            case 29:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 32767) | ((value & 1) << 15));
                break;
            case 30:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 61951) | (value << 9));
                break;
            case 31:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65479) | (value << 3));
                break;
            case 32:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 8191) | (value << 13));
                break;
            case 33:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64639) | (value << 7));
                break;
            case 34:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65521) | (value << 1));
                break;
            case 35:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 51199) | (value << 11));
                break;
            case 36:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65311) | (value << 5));
                break;
            case 37:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 32767) | ((value & 1) << 15));
                break;
            case 38:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61951) | (value << 9));
                break;
            case 39:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65479) | (value << 3));
                break;
            case 40:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 8191) | (value << 13));
                break;
            case 41:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64639) | (value << 7));
                break;
            case 42:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65521) | (value << 1));
                break;
            case 43:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 51199) | (value << 11));
                break;
            case 44:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65311) | (value << 5));
                break;
            case 45:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 32767) | ((value & 1) << 15));
                break;
            case 46:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 61951) | (value << 9));
                break;
            case 47:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65479) | (value << 3));
                break;
            case 48:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 8191) | (value << 13));
                break;
            case 49:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 64639) | (value << 7));
                break;
            case 50:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65521) | (value << 1));
                break;
            case 51:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 51199) | (value << 11));
                break;
            case 52:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65311) | (value << 5));
                break;
            case 53:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 32767) | ((value & 1) << 15));
                break;
            case 54:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 61951) | (value << 9));
                break;
            case 55:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65479) | (value << 3));
                break;
            case 56:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 8191) | (value << 13));
                break;
            case 57:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 64639) | (value << 7));
                break;
            case 58:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65521) | (value << 1));
                break;
            case 59:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 51199) | (value << 11));
                break;
            case 60:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65311) | (value << 5));
                break;
            case 61:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 32767) | ((value & 1) << 15));
                break;
            case 62:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 61951) | (value << 9));
                break;
            case 63:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65479) | (value << 3));
                break;
            case 64:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 8191) | (value << 13));
                break;
            case 65:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 64639) | (value << 7));
                break;
            case 66:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65521) | (value << 1));
                break;
            case 67:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 51199) | (value << 11));
                break;
            case 68:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65311) | (value << 5));
                break;
            case 69:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 32767) | ((value & 1) << 15));
                break;
            case 70:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 61951) | (value << 9));
                break;
            case 71:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65479) | (value << 3));
                break;
            case 72:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 8191) | (value << 13));
                break;
            case 73:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64639) | (value << 7));
                break;
            case 74:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65521) | (value << 1));
                break;
            case 75:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 51199) | (value << 11));
                break;
            case 76:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65311) | (value << 5));
                break;
            case 77:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 32767) | ((value & 1) << 15));
                break;
            case 78:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 61951) | (value << 9));
                break;
            case 79:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65479) | (value << 3));
                break;
            case 80:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 8191) | (value << 13));
                break;
            case 81:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 64639) | (value << 7));
                break;
            case 82:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65521) | (value << 1));
                break;
            case 83:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 51199) | (value << 11));
                break;
            case 84:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65311) | (value << 5));
                break;
            case 85:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 32767) | ((value & 1) << 15));
                break;
            case 86:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 61951) | (value << 9));
                break;
            case 87:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65479) | (value << 3));
                break;
            case 88:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 8191) | (value << 13));
                break;
            case 89:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 64639) | (value << 7));
                break;
            case 90:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65521) | (value << 1));
                break;
            case 91:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 51199) | (value << 11));
                break;
            case 92:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65311) | (value << 5));
                break;
            case 93:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 32767) | ((value & 1) << 15));
                break;
            case 94:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 61951) | (value << 9));
                break;
            case 95:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65479) | (value << 3));
                break;
            case 96:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 8191) | (value << 13));
                break;
            case 97:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 64639) | (value << 7));
                break;
            case 98:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65521) | (value << 1));
                break;
            case 99:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 51199) | (value << 11));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(8) & 7168) >>> 10;
            case 1:
                return (rc.readSharedArray(8) & 112) >>> 4;
            case 2:
                return ((rc.readSharedArray(8) & 1) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(9) & 1792) >>> 8;
            case 4:
                return (rc.readSharedArray(9) & 28) >>> 2;
            case 5:
                return (rc.readSharedArray(10) & 28672) >>> 12;
            case 6:
                return (rc.readSharedArray(10) & 448) >>> 6;
            case 7:
                return (rc.readSharedArray(10) & 7);
            case 8:
                return (rc.readSharedArray(11) & 7168) >>> 10;
            case 9:
                return (rc.readSharedArray(11) & 112) >>> 4;
            case 10:
                return ((rc.readSharedArray(11) & 1) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(12) & 1792) >>> 8;
            case 12:
                return (rc.readSharedArray(12) & 28) >>> 2;
            case 13:
                return (rc.readSharedArray(13) & 28672) >>> 12;
            case 14:
                return (rc.readSharedArray(13) & 448) >>> 6;
            case 15:
                return (rc.readSharedArray(13) & 7);
            case 16:
                return (rc.readSharedArray(14) & 7168) >>> 10;
            case 17:
                return (rc.readSharedArray(14) & 112) >>> 4;
            case 18:
                return ((rc.readSharedArray(14) & 1) << 2) + ((rc.readSharedArray(15) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(15) & 1792) >>> 8;
            case 20:
                return (rc.readSharedArray(15) & 28) >>> 2;
            case 21:
                return (rc.readSharedArray(16) & 28672) >>> 12;
            case 22:
                return (rc.readSharedArray(16) & 448) >>> 6;
            case 23:
                return (rc.readSharedArray(16) & 7);
            case 24:
                return (rc.readSharedArray(17) & 7168) >>> 10;
            case 25:
                return (rc.readSharedArray(17) & 112) >>> 4;
            case 26:
                return ((rc.readSharedArray(17) & 1) << 2) + ((rc.readSharedArray(18) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(18) & 1792) >>> 8;
            case 28:
                return (rc.readSharedArray(18) & 28) >>> 2;
            case 29:
                return (rc.readSharedArray(19) & 28672) >>> 12;
            case 30:
                return (rc.readSharedArray(19) & 448) >>> 6;
            case 31:
                return (rc.readSharedArray(19) & 7);
            case 32:
                return (rc.readSharedArray(20) & 7168) >>> 10;
            case 33:
                return (rc.readSharedArray(20) & 112) >>> 4;
            case 34:
                return ((rc.readSharedArray(20) & 1) << 2) + ((rc.readSharedArray(21) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(21) & 1792) >>> 8;
            case 36:
                return (rc.readSharedArray(21) & 28) >>> 2;
            case 37:
                return (rc.readSharedArray(22) & 28672) >>> 12;
            case 38:
                return (rc.readSharedArray(22) & 448) >>> 6;
            case 39:
                return (rc.readSharedArray(22) & 7);
            case 40:
                return (rc.readSharedArray(23) & 7168) >>> 10;
            case 41:
                return (rc.readSharedArray(23) & 112) >>> 4;
            case 42:
                return ((rc.readSharedArray(23) & 1) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(24) & 1792) >>> 8;
            case 44:
                return (rc.readSharedArray(24) & 28) >>> 2;
            case 45:
                return (rc.readSharedArray(25) & 28672) >>> 12;
            case 46:
                return (rc.readSharedArray(25) & 448) >>> 6;
            case 47:
                return (rc.readSharedArray(25) & 7);
            case 48:
                return (rc.readSharedArray(26) & 7168) >>> 10;
            case 49:
                return (rc.readSharedArray(26) & 112) >>> 4;
            case 50:
                return ((rc.readSharedArray(26) & 1) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(27) & 1792) >>> 8;
            case 52:
                return (rc.readSharedArray(27) & 28) >>> 2;
            case 53:
                return (rc.readSharedArray(28) & 28672) >>> 12;
            case 54:
                return (rc.readSharedArray(28) & 448) >>> 6;
            case 55:
                return (rc.readSharedArray(28) & 7);
            case 56:
                return (rc.readSharedArray(29) & 7168) >>> 10;
            case 57:
                return (rc.readSharedArray(29) & 112) >>> 4;
            case 58:
                return ((rc.readSharedArray(29) & 1) << 2) + ((rc.readSharedArray(30) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(30) & 1792) >>> 8;
            case 60:
                return (rc.readSharedArray(30) & 28) >>> 2;
            case 61:
                return (rc.readSharedArray(31) & 28672) >>> 12;
            case 62:
                return (rc.readSharedArray(31) & 448) >>> 6;
            case 63:
                return (rc.readSharedArray(31) & 7);
            case 64:
                return (rc.readSharedArray(32) & 7168) >>> 10;
            case 65:
                return (rc.readSharedArray(32) & 112) >>> 4;
            case 66:
                return ((rc.readSharedArray(32) & 1) << 2) + ((rc.readSharedArray(33) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(33) & 1792) >>> 8;
            case 68:
                return (rc.readSharedArray(33) & 28) >>> 2;
            case 69:
                return (rc.readSharedArray(34) & 28672) >>> 12;
            case 70:
                return (rc.readSharedArray(34) & 448) >>> 6;
            case 71:
                return (rc.readSharedArray(34) & 7);
            case 72:
                return (rc.readSharedArray(35) & 7168) >>> 10;
            case 73:
                return (rc.readSharedArray(35) & 112) >>> 4;
            case 74:
                return ((rc.readSharedArray(35) & 1) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(36) & 1792) >>> 8;
            case 76:
                return (rc.readSharedArray(36) & 28) >>> 2;
            case 77:
                return (rc.readSharedArray(37) & 28672) >>> 12;
            case 78:
                return (rc.readSharedArray(37) & 448) >>> 6;
            case 79:
                return (rc.readSharedArray(37) & 7);
            case 80:
                return (rc.readSharedArray(38) & 7168) >>> 10;
            case 81:
                return (rc.readSharedArray(38) & 112) >>> 4;
            case 82:
                return ((rc.readSharedArray(38) & 1) << 2) + ((rc.readSharedArray(39) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(39) & 1792) >>> 8;
            case 84:
                return (rc.readSharedArray(39) & 28) >>> 2;
            case 85:
                return (rc.readSharedArray(40) & 28672) >>> 12;
            case 86:
                return (rc.readSharedArray(40) & 448) >>> 6;
            case 87:
                return (rc.readSharedArray(40) & 7);
            case 88:
                return (rc.readSharedArray(41) & 7168) >>> 10;
            case 89:
                return (rc.readSharedArray(41) & 112) >>> 4;
            case 90:
                return ((rc.readSharedArray(41) & 1) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(42) & 1792) >>> 8;
            case 92:
                return (rc.readSharedArray(42) & 28) >>> 2;
            case 93:
                return (rc.readSharedArray(43) & 28672) >>> 12;
            case 94:
                return (rc.readSharedArray(43) & 448) >>> 6;
            case 95:
                return (rc.readSharedArray(43) & 7);
            case 96:
                return (rc.readSharedArray(44) & 7168) >>> 10;
            case 97:
                return (rc.readSharedArray(44) & 112) >>> 4;
            case 98:
                return ((rc.readSharedArray(44) & 1) << 2) + ((rc.readSharedArray(45) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(45) & 1792) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 58367) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65423) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 63743) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65507) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 36863) | (value << 12));
                break;
            case 6:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65087) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65528) | (value));
                break;
            case 8:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 58367) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65423) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 63743) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65507) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 36863) | (value << 12));
                break;
            case 14:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65087) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65528) | (value));
                break;
            case 16:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 58367) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65423) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 63743) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65507) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 36863) | (value << 12));
                break;
            case 22:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65087) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65528) | (value));
                break;
            case 24:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 58367) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65423) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 63743) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65507) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 36863) | (value << 12));
                break;
            case 30:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65087) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65528) | (value));
                break;
            case 32:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 58367) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65423) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 63743) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65507) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 36863) | (value << 12));
                break;
            case 38:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65087) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65528) | (value));
                break;
            case 40:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 58367) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65423) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 63743) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65507) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 36863) | (value << 12));
                break;
            case 46:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65087) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65528) | (value));
                break;
            case 48:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 58367) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65423) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 63743) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65507) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 36863) | (value << 12));
                break;
            case 54:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65087) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65528) | (value));
                break;
            case 56:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 58367) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65423) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 63743) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65507) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 36863) | (value << 12));
                break;
            case 62:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65087) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65528) | (value));
                break;
            case 64:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 58367) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65423) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 63743) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65507) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 36863) | (value << 12));
                break;
            case 70:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65087) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65528) | (value));
                break;
            case 72:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 58367) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65423) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 63743) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65507) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 36863) | (value << 12));
                break;
            case 78:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65087) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65528) | (value));
                break;
            case 80:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 58367) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65423) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 63743) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65507) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 36863) | (value << 12));
                break;
            case 86:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65087) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65528) | (value));
                break;
            case 88:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 58367) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65423) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 63743) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65507) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 36863) | (value << 12));
                break;
            case 94:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65087) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65528) | (value));
                break;
            case 96:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 58367) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65423) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 63743) | (value << 8));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(8) & 64512) >>> 10;
            case 1:
                return (rc.readSharedArray(8) & 1008) >>> 4;
            case 2:
                return ((rc.readSharedArray(8) & 15) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(9) & 16128) >>> 8;
            case 4:
                return (rc.readSharedArray(9) & 252) >>> 2;
            case 5:
                return ((rc.readSharedArray(9) & 3) << 4) + ((rc.readSharedArray(10) & 61440) >>> 12);
            case 6:
                return (rc.readSharedArray(10) & 4032) >>> 6;
            case 7:
                return (rc.readSharedArray(10) & 63);
            case 8:
                return (rc.readSharedArray(11) & 64512) >>> 10;
            case 9:
                return (rc.readSharedArray(11) & 1008) >>> 4;
            case 10:
                return ((rc.readSharedArray(11) & 15) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(12) & 16128) >>> 8;
            case 12:
                return (rc.readSharedArray(12) & 252) >>> 2;
            case 13:
                return ((rc.readSharedArray(12) & 3) << 4) + ((rc.readSharedArray(13) & 61440) >>> 12);
            case 14:
                return (rc.readSharedArray(13) & 4032) >>> 6;
            case 15:
                return (rc.readSharedArray(13) & 63);
            case 16:
                return (rc.readSharedArray(14) & 64512) >>> 10;
            case 17:
                return (rc.readSharedArray(14) & 1008) >>> 4;
            case 18:
                return ((rc.readSharedArray(14) & 15) << 2) + ((rc.readSharedArray(15) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(15) & 16128) >>> 8;
            case 20:
                return (rc.readSharedArray(15) & 252) >>> 2;
            case 21:
                return ((rc.readSharedArray(15) & 3) << 4) + ((rc.readSharedArray(16) & 61440) >>> 12);
            case 22:
                return (rc.readSharedArray(16) & 4032) >>> 6;
            case 23:
                return (rc.readSharedArray(16) & 63);
            case 24:
                return (rc.readSharedArray(17) & 64512) >>> 10;
            case 25:
                return (rc.readSharedArray(17) & 1008) >>> 4;
            case 26:
                return ((rc.readSharedArray(17) & 15) << 2) + ((rc.readSharedArray(18) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(18) & 16128) >>> 8;
            case 28:
                return (rc.readSharedArray(18) & 252) >>> 2;
            case 29:
                return ((rc.readSharedArray(18) & 3) << 4) + ((rc.readSharedArray(19) & 61440) >>> 12);
            case 30:
                return (rc.readSharedArray(19) & 4032) >>> 6;
            case 31:
                return (rc.readSharedArray(19) & 63);
            case 32:
                return (rc.readSharedArray(20) & 64512) >>> 10;
            case 33:
                return (rc.readSharedArray(20) & 1008) >>> 4;
            case 34:
                return ((rc.readSharedArray(20) & 15) << 2) + ((rc.readSharedArray(21) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(21) & 16128) >>> 8;
            case 36:
                return (rc.readSharedArray(21) & 252) >>> 2;
            case 37:
                return ((rc.readSharedArray(21) & 3) << 4) + ((rc.readSharedArray(22) & 61440) >>> 12);
            case 38:
                return (rc.readSharedArray(22) & 4032) >>> 6;
            case 39:
                return (rc.readSharedArray(22) & 63);
            case 40:
                return (rc.readSharedArray(23) & 64512) >>> 10;
            case 41:
                return (rc.readSharedArray(23) & 1008) >>> 4;
            case 42:
                return ((rc.readSharedArray(23) & 15) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(24) & 16128) >>> 8;
            case 44:
                return (rc.readSharedArray(24) & 252) >>> 2;
            case 45:
                return ((rc.readSharedArray(24) & 3) << 4) + ((rc.readSharedArray(25) & 61440) >>> 12);
            case 46:
                return (rc.readSharedArray(25) & 4032) >>> 6;
            case 47:
                return (rc.readSharedArray(25) & 63);
            case 48:
                return (rc.readSharedArray(26) & 64512) >>> 10;
            case 49:
                return (rc.readSharedArray(26) & 1008) >>> 4;
            case 50:
                return ((rc.readSharedArray(26) & 15) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(27) & 16128) >>> 8;
            case 52:
                return (rc.readSharedArray(27) & 252) >>> 2;
            case 53:
                return ((rc.readSharedArray(27) & 3) << 4) + ((rc.readSharedArray(28) & 61440) >>> 12);
            case 54:
                return (rc.readSharedArray(28) & 4032) >>> 6;
            case 55:
                return (rc.readSharedArray(28) & 63);
            case 56:
                return (rc.readSharedArray(29) & 64512) >>> 10;
            case 57:
                return (rc.readSharedArray(29) & 1008) >>> 4;
            case 58:
                return ((rc.readSharedArray(29) & 15) << 2) + ((rc.readSharedArray(30) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(30) & 16128) >>> 8;
            case 60:
                return (rc.readSharedArray(30) & 252) >>> 2;
            case 61:
                return ((rc.readSharedArray(30) & 3) << 4) + ((rc.readSharedArray(31) & 61440) >>> 12);
            case 62:
                return (rc.readSharedArray(31) & 4032) >>> 6;
            case 63:
                return (rc.readSharedArray(31) & 63);
            case 64:
                return (rc.readSharedArray(32) & 64512) >>> 10;
            case 65:
                return (rc.readSharedArray(32) & 1008) >>> 4;
            case 66:
                return ((rc.readSharedArray(32) & 15) << 2) + ((rc.readSharedArray(33) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(33) & 16128) >>> 8;
            case 68:
                return (rc.readSharedArray(33) & 252) >>> 2;
            case 69:
                return ((rc.readSharedArray(33) & 3) << 4) + ((rc.readSharedArray(34) & 61440) >>> 12);
            case 70:
                return (rc.readSharedArray(34) & 4032) >>> 6;
            case 71:
                return (rc.readSharedArray(34) & 63);
            case 72:
                return (rc.readSharedArray(35) & 64512) >>> 10;
            case 73:
                return (rc.readSharedArray(35) & 1008) >>> 4;
            case 74:
                return ((rc.readSharedArray(35) & 15) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(36) & 16128) >>> 8;
            case 76:
                return (rc.readSharedArray(36) & 252) >>> 2;
            case 77:
                return ((rc.readSharedArray(36) & 3) << 4) + ((rc.readSharedArray(37) & 61440) >>> 12);
            case 78:
                return (rc.readSharedArray(37) & 4032) >>> 6;
            case 79:
                return (rc.readSharedArray(37) & 63);
            case 80:
                return (rc.readSharedArray(38) & 64512) >>> 10;
            case 81:
                return (rc.readSharedArray(38) & 1008) >>> 4;
            case 82:
                return ((rc.readSharedArray(38) & 15) << 2) + ((rc.readSharedArray(39) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(39) & 16128) >>> 8;
            case 84:
                return (rc.readSharedArray(39) & 252) >>> 2;
            case 85:
                return ((rc.readSharedArray(39) & 3) << 4) + ((rc.readSharedArray(40) & 61440) >>> 12);
            case 86:
                return (rc.readSharedArray(40) & 4032) >>> 6;
            case 87:
                return (rc.readSharedArray(40) & 63);
            case 88:
                return (rc.readSharedArray(41) & 64512) >>> 10;
            case 89:
                return (rc.readSharedArray(41) & 1008) >>> 4;
            case 90:
                return ((rc.readSharedArray(41) & 15) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(42) & 16128) >>> 8;
            case 92:
                return (rc.readSharedArray(42) & 252) >>> 2;
            case 93:
                return ((rc.readSharedArray(42) & 3) << 4) + ((rc.readSharedArray(43) & 61440) >>> 12);
            case 94:
                return (rc.readSharedArray(43) & 4032) >>> 6;
            case 95:
                return (rc.readSharedArray(43) & 63);
            case 96:
                return (rc.readSharedArray(44) & 64512) >>> 10;
            case 97:
                return (rc.readSharedArray(44) & 1008) >>> 4;
            case 98:
                return ((rc.readSharedArray(44) & 15) << 2) + ((rc.readSharedArray(45) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(45) & 16128) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 1023) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64527) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 49407) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65283) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 4095) | ((value & 15) << 12));
                break;
            case 6:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 61503) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65472) | (value));
                break;
            case 8:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 1023) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 64527) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 49407) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65283) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 4095) | ((value & 15) << 12));
                break;
            case 14:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 61503) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65472) | (value));
                break;
            case 16:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 1023) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 64527) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 49407) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65283) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 4095) | ((value & 15) << 12));
                break;
            case 22:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 61503) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65472) | (value));
                break;
            case 24:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 1023) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 64527) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 49407) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65283) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 4095) | ((value & 15) << 12));
                break;
            case 30:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 61503) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65472) | (value));
                break;
            case 32:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 1023) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64527) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 49407) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65283) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 4095) | ((value & 15) << 12));
                break;
            case 38:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61503) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65472) | (value));
                break;
            case 40:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 1023) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64527) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 49407) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65283) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 4095) | ((value & 15) << 12));
                break;
            case 46:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 61503) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65472) | (value));
                break;
            case 48:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 1023) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 64527) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 49407) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65283) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 4095) | ((value & 15) << 12));
                break;
            case 54:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 61503) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65472) | (value));
                break;
            case 56:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 1023) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 64527) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 49407) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65283) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 4095) | ((value & 15) << 12));
                break;
            case 62:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 61503) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65472) | (value));
                break;
            case 64:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 1023) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 64527) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 49407) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65283) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 4095) | ((value & 15) << 12));
                break;
            case 70:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 61503) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65472) | (value));
                break;
            case 72:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 1023) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64527) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 49407) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65283) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 4095) | ((value & 15) << 12));
                break;
            case 78:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 61503) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65472) | (value));
                break;
            case 80:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 1023) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 64527) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 49407) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65283) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 4095) | ((value & 15) << 12));
                break;
            case 86:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 61503) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65472) | (value));
                break;
            case 88:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 1023) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 64527) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 49407) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65283) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 4095) | ((value & 15) << 12));
                break;
            case 94:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 61503) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65472) | (value));
                break;
            case 96:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 1023) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 64527) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 49407) | (value << 8));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(45) & 254) >>> 1;
            case 1:
                return ((rc.readSharedArray(45) & 1) << 6) + ((rc.readSharedArray(46) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(46) & 1016) >>> 3;
            case 3:
                return ((rc.readSharedArray(46) & 7) << 4) + ((rc.readSharedArray(47) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(47) & 4064) >>> 5;
            case 5:
                return ((rc.readSharedArray(47) & 31) << 2) + ((rc.readSharedArray(48) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(48) & 16256) >>> 7;
            case 7:
                return (rc.readSharedArray(48) & 127);
            case 8:
                return (rc.readSharedArray(49) & 65024) >>> 9;
            case 9:
                return (rc.readSharedArray(49) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65281) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 64519) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 61471) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 49279) | (value << 7));
                break;
            case 7:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65408) | (value));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 511) | (value << 9));
                break;
            case 9:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65027) | (value << 2));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(45) & 254) >>> 1;
            case 1:
                return ((rc.readSharedArray(45) & 1) << 6) + ((rc.readSharedArray(46) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(46) & 1016) >>> 3;
            case 3:
                return ((rc.readSharedArray(46) & 7) << 4) + ((rc.readSharedArray(47) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(47) & 4064) >>> 5;
            case 5:
                return ((rc.readSharedArray(47) & 31) << 2) + ((rc.readSharedArray(48) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(48) & 16256) >>> 7;
            case 7:
                return (rc.readSharedArray(48) & 127);
            case 8:
                return (rc.readSharedArray(49) & 65024) >>> 9;
            case 9:
                return (rc.readSharedArray(49) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65281) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 64519) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 61471) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 49279) | (value << 7));
                break;
            case 7:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65408) | (value));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 511) | (value << 9));
                break;
            case 9:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65027) | (value << 2));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(49) & 2) >>> 1;
            case 1:
                return (rc.readSharedArray(50) & 512) >>> 9;
            case 2:
                return (rc.readSharedArray(50) & 2) >>> 1;
            case 3:
                return (rc.readSharedArray(51) & 512) >>> 9;
            case 4:
                return (rc.readSharedArray(51) & 2) >>> 1;
            case 5:
                return (rc.readSharedArray(52) & 512) >>> 9;
            case 6:
                return (rc.readSharedArray(52) & 2) >>> 1;
            case 7:
                return (rc.readSharedArray(53) & 512) >>> 9;
            case 8:
                return (rc.readSharedArray(53) & 2) >>> 1;
            case 9:
                return (rc.readSharedArray(54) & 512) >>> 9;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65533) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65023) | (value << 9));
                break;
            case 2:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65533) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65023) | (value << 9));
                break;
            case 4:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65533) | (value << 1));
                break;
            case 5:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65023) | (value << 9));
                break;
            case 6:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65533) | (value << 1));
                break;
            case 7:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65023) | (value << 9));
                break;
            case 8:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65533) | (value << 1));
                break;
            case 9:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65023) | (value << 9));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(49) & 1) << 6) + ((rc.readSharedArray(50) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(50) & 508) >>> 2;
            case 2:
                return ((rc.readSharedArray(50) & 1) << 6) + ((rc.readSharedArray(51) & 64512) >>> 10);
            case 3:
                return (rc.readSharedArray(51) & 508) >>> 2;
            case 4:
                return ((rc.readSharedArray(51) & 1) << 6) + ((rc.readSharedArray(52) & 64512) >>> 10);
            case 5:
                return (rc.readSharedArray(52) & 508) >>> 2;
            case 6:
                return ((rc.readSharedArray(52) & 1) << 6) + ((rc.readSharedArray(53) & 64512) >>> 10);
            case 7:
                return (rc.readSharedArray(53) & 508) >>> 2;
            case 8:
                return ((rc.readSharedArray(53) & 1) << 6) + ((rc.readSharedArray(54) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(54) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65027) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 1023) | ((value & 63) << 10));
                break;
            case 3:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65027) | (value << 2));
                break;
            case 4:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 1023) | ((value & 63) << 10));
                break;
            case 5:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 1023) | ((value & 63) << 10));
                break;
            case 7:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65027) | (value << 2));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(49) & 3) << 6) + ((rc.readSharedArray(50) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(50) & 1020) >>> 2;
            case 2:
                return ((rc.readSharedArray(50) & 3) << 6) + ((rc.readSharedArray(51) & 64512) >>> 10);
            case 3:
                return (rc.readSharedArray(51) & 1020) >>> 2;
            case 4:
                return ((rc.readSharedArray(51) & 3) << 6) + ((rc.readSharedArray(52) & 64512) >>> 10);
            case 5:
                return (rc.readSharedArray(52) & 1020) >>> 2;
            case 6:
                return ((rc.readSharedArray(52) & 3) << 6) + ((rc.readSharedArray(53) & 64512) >>> 10);
            case 7:
                return (rc.readSharedArray(53) & 1020) >>> 2;
            case 8:
                return ((rc.readSharedArray(53) & 3) << 6) + ((rc.readSharedArray(54) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(54) & 1020) >>> 2;
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 64515) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 1023) | ((value & 63) << 10));
                break;
            case 3:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 64515) | (value << 2));
                break;
            case 4:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 1023) | ((value & 63) << 10));
                break;
            case 5:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 64515) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 1023) | ((value & 63) << 10));
                break;
            case 7:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 64515) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 64515) | (value << 2));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(54) & 3) << 1) + ((rc.readSharedArray(55) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(55) & 224) >>> 5;
            case 2:
                return (rc.readSharedArray(56) & 14336) >>> 11;
            case 3:
                return (rc.readSharedArray(56) & 14) >>> 1;
            case 4:
                return (rc.readSharedArray(57) & 896) >>> 7;
            case 5:
                return (rc.readSharedArray(58) & 57344) >>> 13;
            case 6:
                return (rc.readSharedArray(58) & 56) >>> 3;
            case 7:
                return (rc.readSharedArray(59) & 3584) >>> 9;
            case 8:
                return ((rc.readSharedArray(59) & 3) << 1) + ((rc.readSharedArray(60) & 32768) >>> 15);
            case 9:
                return (rc.readSharedArray(60) & 224) >>> 5;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65311) | (value << 5));
                break;
            case 2:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 51199) | (value << 11));
                break;
            case 3:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65521) | (value << 1));
                break;
            case 4:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 64639) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 8191) | (value << 13));
                break;
            case 6:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65479) | (value << 3));
                break;
            case 7:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 61951) | (value << 9));
                break;
            case 8:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 32767) | ((value & 1) << 15));
                break;
            case 9:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65311) | (value << 5));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(55) & 32512) >>> 8;
            case 1:
                return ((rc.readSharedArray(55) & 31) << 2) + ((rc.readSharedArray(56) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(56) & 2032) >>> 4;
            case 3:
                return ((rc.readSharedArray(56) & 1) << 6) + ((rc.readSharedArray(57) & 64512) >>> 10);
            case 4:
                return (rc.readSharedArray(57) & 127);
            case 5:
                return (rc.readSharedArray(58) & 8128) >>> 6;
            case 6:
                return ((rc.readSharedArray(58) & 7) << 4) + ((rc.readSharedArray(59) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(59) & 508) >>> 2;
            case 8:
                return (rc.readSharedArray(60) & 32512) >>> 8;
            case 9:
                return ((rc.readSharedArray(60) & 31) << 2) + ((rc.readSharedArray(61) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 33023) | (value << 8));
                break;
            case 1:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 63503) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 1023) | ((value & 63) << 10));
                break;
            case 4:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65408) | (value));
                break;
            case 5:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 57407) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 33023) | (value << 8));
                break;
            case 9:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(54) & 3) << 8) + ((rc.readSharedArray(55) & 65280) >>> 8);
            case 1:
                return ((rc.readSharedArray(55) & 255) << 2) + ((rc.readSharedArray(56) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(56) & 16368) >>> 4;
            case 3:
                return ((rc.readSharedArray(56) & 15) << 6) + ((rc.readSharedArray(57) & 64512) >>> 10);
            case 4:
                return (rc.readSharedArray(57) & 1023);
            case 5:
                return (rc.readSharedArray(58) & 65472) >>> 6;
            case 6:
                return ((rc.readSharedArray(58) & 63) << 4) + ((rc.readSharedArray(59) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(59) & 4092) >>> 2;
            case 8:
                return ((rc.readSharedArray(59) & 3) << 8) + ((rc.readSharedArray(60) & 65280) >>> 8);
            case 9:
                return ((rc.readSharedArray(60) & 255) << 2) + ((rc.readSharedArray(61) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 255) | ((value & 255) << 8));
                break;
            case 1:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 49167) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 1023) | ((value & 63) << 10));
                break;
            case 4:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 64512) | (value));
                break;
            case 5:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 63) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 61443) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 255) | ((value & 255) << 8));
                break;
            case 9:
                rc.writeSharedArray(60, (rc.readSharedArray(60) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(61, (rc.readSharedArray(61) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readLastArchonNum() throws GameActionException {
        return (rc.readSharedArray(61) & 12288) >>> 12;
    }

    public void writeLastArchonNum(int value) throws GameActionException {
        rc.writeSharedArray(61, (rc.readSharedArray(61) & 53247) | (value << 12));
    }

    public int readLastArchonAll() throws GameActionException {
        return (rc.readSharedArray(61) & 12288) >>> 12;
    }

    public void writeLastArchonAll(int value) throws GameActionException {
        rc.writeSharedArray(61, (rc.readSharedArray(61) & 53247) | (value << 12));
    }

    public int readReservedResourcesLead() throws GameActionException {
        return (rc.readSharedArray(61) & 4092) >>> 2;
    }

    public void writeReservedResourcesLead(int value) throws GameActionException {
        rc.writeSharedArray(61, (rc.readSharedArray(61) & 61443) | (value << 2));
    }

    public int readReservedResourcesGold() throws GameActionException {
        return ((rc.readSharedArray(61) & 3) << 4) + ((rc.readSharedArray(62) & 61440) >>> 12);
    }

    public void writeReservedResourcesGold(int value) throws GameActionException {
        rc.writeSharedArray(61, (rc.readSharedArray(61) & 65532) | ((value & 48) >>> 4));
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 4095) | ((value & 15) << 12));
    }

    public int readReservedResourcesAll() throws GameActionException {
        return ((rc.readSharedArray(61) & 4095) << 4) + ((rc.readSharedArray(62) & 61440) >>> 12);
    }

    public void writeReservedResourcesAll(int value) throws GameActionException {
        rc.writeSharedArray(61, (rc.readSharedArray(61) & 61440) | ((value & 65520) >>> 4));
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 4095) | ((value & 15) << 12));
    }

    public int readMapSymmetry() throws GameActionException {
        return (rc.readSharedArray(62) & 3072) >>> 10;
    }

    public void writeMapSymmetry(int value) throws GameActionException {
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 62463) | (value << 10));
    }

    public int readMapAll() throws GameActionException {
        return (rc.readSharedArray(62) & 3072) >>> 10;
    }

    public void writeMapAll(int value) throws GameActionException {
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 62463) | (value << 10));
    }

    public int readProductionControlGold() throws GameActionException {
        return (rc.readSharedArray(62) & 512) >>> 9;
    }

    public void writeProductionControlGold(int value) throws GameActionException {
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 65023) | (value << 9));
    }

    public int readProductionControlAll() throws GameActionException {
        return (rc.readSharedArray(62) & 512) >>> 9;
    }

    public void writeProductionControlAll(int value) throws GameActionException {
        rc.writeSharedArray(62, (rc.readSharedArray(62) & 65023) | (value << 9));
    }
}