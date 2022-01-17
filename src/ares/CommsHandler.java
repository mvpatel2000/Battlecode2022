package ares;

import battlecode.common.*;

public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;
    final int FILLER_DO_NOT_USE_SLOTS = 1;
    final int MINER_COUNT_SLOTS = 1;
    final int SOLDIER_COUNT_SLOTS = 1;
    final int LAST_ARCHON_SLOTS = 1;
    final int RESERVED_RESOURCES_SLOTS = 1;

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
        public static final int THEIRS = 2;
        public static final int EXPLORING = 3;
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
    
    public void initPriorityClusters() throws GameActionException {
        rc.writeSharedArray(41, 255);
        rc.writeSharedArray(42, 65535);
        rc.writeSharedArray(43, 65535);
        rc.writeSharedArray(44, 65535);
        rc.writeSharedArray(45, 65533);
        rc.writeSharedArray(46, 65021);
        rc.writeSharedArray(47, 65021);
        rc.writeSharedArray(48, 65021);
        rc.writeSharedArray(49, 65021);
        rc.writeSharedArray(50, 65020);
        rc.writeSharedArray(51, 32543);
        rc.writeSharedArray(52, 51185);
        rc.writeSharedArray(53, 64639);
        rc.writeSharedArray(54, 8135);
        rc.writeSharedArray(55, 61948);
        rc.writeSharedArray(56, 32543);
        rc.writeSharedArray(57, 49152);
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

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 57344) >>> 13;
            case 1:
                return (rc.readSharedArray(4) & 896) >>> 7;
            case 2:
                return (rc.readSharedArray(4) & 14) >>> 1;
            case 3:
                return (rc.readSharedArray(5) & 14336) >>> 11;
            case 4:
                return (rc.readSharedArray(5) & 224) >>> 5;
            case 5:
                return ((rc.readSharedArray(5) & 3) << 1) + ((rc.readSharedArray(6) & 32768) >>> 15);
            case 6:
                return (rc.readSharedArray(6) & 3584) >>> 9;
            case 7:
                return (rc.readSharedArray(6) & 56) >>> 3;
            case 8:
                return (rc.readSharedArray(7) & 57344) >>> 13;
            case 9:
                return (rc.readSharedArray(7) & 896) >>> 7;
            case 10:
                return (rc.readSharedArray(7) & 14) >>> 1;
            case 11:
                return (rc.readSharedArray(8) & 14336) >>> 11;
            case 12:
                return (rc.readSharedArray(8) & 224) >>> 5;
            case 13:
                return ((rc.readSharedArray(8) & 3) << 1) + ((rc.readSharedArray(9) & 32768) >>> 15);
            case 14:
                return (rc.readSharedArray(9) & 3584) >>> 9;
            case 15:
                return (rc.readSharedArray(9) & 56) >>> 3;
            case 16:
                return (rc.readSharedArray(10) & 57344) >>> 13;
            case 17:
                return (rc.readSharedArray(10) & 896) >>> 7;
            case 18:
                return (rc.readSharedArray(10) & 14) >>> 1;
            case 19:
                return (rc.readSharedArray(11) & 14336) >>> 11;
            case 20:
                return (rc.readSharedArray(11) & 224) >>> 5;
            case 21:
                return ((rc.readSharedArray(11) & 3) << 1) + ((rc.readSharedArray(12) & 32768) >>> 15);
            case 22:
                return (rc.readSharedArray(12) & 3584) >>> 9;
            case 23:
                return (rc.readSharedArray(12) & 56) >>> 3;
            case 24:
                return (rc.readSharedArray(13) & 57344) >>> 13;
            case 25:
                return (rc.readSharedArray(13) & 896) >>> 7;
            case 26:
                return (rc.readSharedArray(13) & 14) >>> 1;
            case 27:
                return (rc.readSharedArray(14) & 14336) >>> 11;
            case 28:
                return (rc.readSharedArray(14) & 224) >>> 5;
            case 29:
                return ((rc.readSharedArray(14) & 3) << 1) + ((rc.readSharedArray(15) & 32768) >>> 15);
            case 30:
                return (rc.readSharedArray(15) & 3584) >>> 9;
            case 31:
                return (rc.readSharedArray(15) & 56) >>> 3;
            case 32:
                return (rc.readSharedArray(16) & 57344) >>> 13;
            case 33:
                return (rc.readSharedArray(16) & 896) >>> 7;
            case 34:
                return (rc.readSharedArray(16) & 14) >>> 1;
            case 35:
                return (rc.readSharedArray(17) & 14336) >>> 11;
            case 36:
                return (rc.readSharedArray(17) & 224) >>> 5;
            case 37:
                return ((rc.readSharedArray(17) & 3) << 1) + ((rc.readSharedArray(18) & 32768) >>> 15);
            case 38:
                return (rc.readSharedArray(18) & 3584) >>> 9;
            case 39:
                return (rc.readSharedArray(18) & 56) >>> 3;
            case 40:
                return (rc.readSharedArray(19) & 57344) >>> 13;
            case 41:
                return (rc.readSharedArray(19) & 896) >>> 7;
            case 42:
                return (rc.readSharedArray(19) & 14) >>> 1;
            case 43:
                return (rc.readSharedArray(20) & 14336) >>> 11;
            case 44:
                return (rc.readSharedArray(20) & 224) >>> 5;
            case 45:
                return ((rc.readSharedArray(20) & 3) << 1) + ((rc.readSharedArray(21) & 32768) >>> 15);
            case 46:
                return (rc.readSharedArray(21) & 3584) >>> 9;
            case 47:
                return (rc.readSharedArray(21) & 56) >>> 3;
            case 48:
                return (rc.readSharedArray(22) & 57344) >>> 13;
            case 49:
                return (rc.readSharedArray(22) & 896) >>> 7;
            case 50:
                return (rc.readSharedArray(22) & 14) >>> 1;
            case 51:
                return (rc.readSharedArray(23) & 14336) >>> 11;
            case 52:
                return (rc.readSharedArray(23) & 224) >>> 5;
            case 53:
                return ((rc.readSharedArray(23) & 3) << 1) + ((rc.readSharedArray(24) & 32768) >>> 15);
            case 54:
                return (rc.readSharedArray(24) & 3584) >>> 9;
            case 55:
                return (rc.readSharedArray(24) & 56) >>> 3;
            case 56:
                return (rc.readSharedArray(25) & 57344) >>> 13;
            case 57:
                return (rc.readSharedArray(25) & 896) >>> 7;
            case 58:
                return (rc.readSharedArray(25) & 14) >>> 1;
            case 59:
                return (rc.readSharedArray(26) & 14336) >>> 11;
            case 60:
                return (rc.readSharedArray(26) & 224) >>> 5;
            case 61:
                return ((rc.readSharedArray(26) & 3) << 1) + ((rc.readSharedArray(27) & 32768) >>> 15);
            case 62:
                return (rc.readSharedArray(27) & 3584) >>> 9;
            case 63:
                return (rc.readSharedArray(27) & 56) >>> 3;
            case 64:
                return (rc.readSharedArray(28) & 57344) >>> 13;
            case 65:
                return (rc.readSharedArray(28) & 896) >>> 7;
            case 66:
                return (rc.readSharedArray(28) & 14) >>> 1;
            case 67:
                return (rc.readSharedArray(29) & 14336) >>> 11;
            case 68:
                return (rc.readSharedArray(29) & 224) >>> 5;
            case 69:
                return ((rc.readSharedArray(29) & 3) << 1) + ((rc.readSharedArray(30) & 32768) >>> 15);
            case 70:
                return (rc.readSharedArray(30) & 3584) >>> 9;
            case 71:
                return (rc.readSharedArray(30) & 56) >>> 3;
            case 72:
                return (rc.readSharedArray(31) & 57344) >>> 13;
            case 73:
                return (rc.readSharedArray(31) & 896) >>> 7;
            case 74:
                return (rc.readSharedArray(31) & 14) >>> 1;
            case 75:
                return (rc.readSharedArray(32) & 14336) >>> 11;
            case 76:
                return (rc.readSharedArray(32) & 224) >>> 5;
            case 77:
                return ((rc.readSharedArray(32) & 3) << 1) + ((rc.readSharedArray(33) & 32768) >>> 15);
            case 78:
                return (rc.readSharedArray(33) & 3584) >>> 9;
            case 79:
                return (rc.readSharedArray(33) & 56) >>> 3;
            case 80:
                return (rc.readSharedArray(34) & 57344) >>> 13;
            case 81:
                return (rc.readSharedArray(34) & 896) >>> 7;
            case 82:
                return (rc.readSharedArray(34) & 14) >>> 1;
            case 83:
                return (rc.readSharedArray(35) & 14336) >>> 11;
            case 84:
                return (rc.readSharedArray(35) & 224) >>> 5;
            case 85:
                return ((rc.readSharedArray(35) & 3) << 1) + ((rc.readSharedArray(36) & 32768) >>> 15);
            case 86:
                return (rc.readSharedArray(36) & 3584) >>> 9;
            case 87:
                return (rc.readSharedArray(36) & 56) >>> 3;
            case 88:
                return (rc.readSharedArray(37) & 57344) >>> 13;
            case 89:
                return (rc.readSharedArray(37) & 896) >>> 7;
            case 90:
                return (rc.readSharedArray(37) & 14) >>> 1;
            case 91:
                return (rc.readSharedArray(38) & 14336) >>> 11;
            case 92:
                return (rc.readSharedArray(38) & 224) >>> 5;
            case 93:
                return ((rc.readSharedArray(38) & 3) << 1) + ((rc.readSharedArray(39) & 32768) >>> 15);
            case 94:
                return (rc.readSharedArray(39) & 3584) >>> 9;
            case 95:
                return (rc.readSharedArray(39) & 56) >>> 3;
            case 96:
                return (rc.readSharedArray(40) & 57344) >>> 13;
            case 97:
                return (rc.readSharedArray(40) & 896) >>> 7;
            case 98:
                return (rc.readSharedArray(40) & 14) >>> 1;
            case 99:
                return (rc.readSharedArray(41) & 14336) >>> 11;
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 8191) | (value << 13));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 64639) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65521) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 51199) | (value << 11));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65311) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 32767) | ((value & 1) << 15));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 61951) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65479) | (value << 3));
                break;
            case 8:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 8191) | (value << 13));
                break;
            case 9:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 64639) | (value << 7));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65521) | (value << 1));
                break;
            case 11:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 51199) | (value << 11));
                break;
            case 12:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65311) | (value << 5));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 32767) | ((value & 1) << 15));
                break;
            case 14:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 61951) | (value << 9));
                break;
            case 15:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65479) | (value << 3));
                break;
            case 16:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 8191) | (value << 13));
                break;
            case 17:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 64639) | (value << 7));
                break;
            case 18:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65521) | (value << 1));
                break;
            case 19:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 51199) | (value << 11));
                break;
            case 20:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65311) | (value << 5));
                break;
            case 21:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 32767) | ((value & 1) << 15));
                break;
            case 22:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 61951) | (value << 9));
                break;
            case 23:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65479) | (value << 3));
                break;
            case 24:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 8191) | (value << 13));
                break;
            case 25:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 64639) | (value << 7));
                break;
            case 26:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65521) | (value << 1));
                break;
            case 27:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 51199) | (value << 11));
                break;
            case 28:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65311) | (value << 5));
                break;
            case 29:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 32767) | ((value & 1) << 15));
                break;
            case 30:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 61951) | (value << 9));
                break;
            case 31:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65479) | (value << 3));
                break;
            case 32:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 8191) | (value << 13));
                break;
            case 33:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 64639) | (value << 7));
                break;
            case 34:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65521) | (value << 1));
                break;
            case 35:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 51199) | (value << 11));
                break;
            case 36:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65311) | (value << 5));
                break;
            case 37:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 32767) | ((value & 1) << 15));
                break;
            case 38:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 61951) | (value << 9));
                break;
            case 39:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65479) | (value << 3));
                break;
            case 40:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 8191) | (value << 13));
                break;
            case 41:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 64639) | (value << 7));
                break;
            case 42:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65521) | (value << 1));
                break;
            case 43:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 51199) | (value << 11));
                break;
            case 44:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65311) | (value << 5));
                break;
            case 45:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 32767) | ((value & 1) << 15));
                break;
            case 46:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 61951) | (value << 9));
                break;
            case 47:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65479) | (value << 3));
                break;
            case 48:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 8191) | (value << 13));
                break;
            case 49:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 64639) | (value << 7));
                break;
            case 50:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65521) | (value << 1));
                break;
            case 51:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 51199) | (value << 11));
                break;
            case 52:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65311) | (value << 5));
                break;
            case 53:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 32767) | ((value & 1) << 15));
                break;
            case 54:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 61951) | (value << 9));
                break;
            case 55:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65479) | (value << 3));
                break;
            case 56:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 8191) | (value << 13));
                break;
            case 57:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 64639) | (value << 7));
                break;
            case 58:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65521) | (value << 1));
                break;
            case 59:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 51199) | (value << 11));
                break;
            case 60:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65311) | (value << 5));
                break;
            case 61:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 32767) | ((value & 1) << 15));
                break;
            case 62:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 61951) | (value << 9));
                break;
            case 63:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65479) | (value << 3));
                break;
            case 64:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 8191) | (value << 13));
                break;
            case 65:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 64639) | (value << 7));
                break;
            case 66:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65521) | (value << 1));
                break;
            case 67:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 51199) | (value << 11));
                break;
            case 68:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65311) | (value << 5));
                break;
            case 69:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 32767) | ((value & 1) << 15));
                break;
            case 70:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 61951) | (value << 9));
                break;
            case 71:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65479) | (value << 3));
                break;
            case 72:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 8191) | (value << 13));
                break;
            case 73:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 64639) | (value << 7));
                break;
            case 74:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65521) | (value << 1));
                break;
            case 75:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 51199) | (value << 11));
                break;
            case 76:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65311) | (value << 5));
                break;
            case 77:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 32767) | ((value & 1) << 15));
                break;
            case 78:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 61951) | (value << 9));
                break;
            case 79:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65479) | (value << 3));
                break;
            case 80:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 8191) | (value << 13));
                break;
            case 81:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 64639) | (value << 7));
                break;
            case 82:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65521) | (value << 1));
                break;
            case 83:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 51199) | (value << 11));
                break;
            case 84:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65311) | (value << 5));
                break;
            case 85:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 32767) | ((value & 1) << 15));
                break;
            case 86:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 61951) | (value << 9));
                break;
            case 87:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65479) | (value << 3));
                break;
            case 88:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 8191) | (value << 13));
                break;
            case 89:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 64639) | (value << 7));
                break;
            case 90:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65521) | (value << 1));
                break;
            case 91:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 51199) | (value << 11));
                break;
            case 92:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65311) | (value << 5));
                break;
            case 93:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 32767) | ((value & 1) << 15));
                break;
            case 94:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 61951) | (value << 9));
                break;
            case 95:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65479) | (value << 3));
                break;
            case 96:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 8191) | (value << 13));
                break;
            case 97:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 64639) | (value << 7));
                break;
            case 98:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65521) | (value << 1));
                break;
            case 99:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 51199) | (value << 11));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 7168) >>> 10;
            case 1:
                return (rc.readSharedArray(4) & 112) >>> 4;
            case 2:
                return ((rc.readSharedArray(4) & 1) << 2) + ((rc.readSharedArray(5) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(5) & 1792) >>> 8;
            case 4:
                return (rc.readSharedArray(5) & 28) >>> 2;
            case 5:
                return (rc.readSharedArray(6) & 28672) >>> 12;
            case 6:
                return (rc.readSharedArray(6) & 448) >>> 6;
            case 7:
                return (rc.readSharedArray(6) & 7);
            case 8:
                return (rc.readSharedArray(7) & 7168) >>> 10;
            case 9:
                return (rc.readSharedArray(7) & 112) >>> 4;
            case 10:
                return ((rc.readSharedArray(7) & 1) << 2) + ((rc.readSharedArray(8) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(8) & 1792) >>> 8;
            case 12:
                return (rc.readSharedArray(8) & 28) >>> 2;
            case 13:
                return (rc.readSharedArray(9) & 28672) >>> 12;
            case 14:
                return (rc.readSharedArray(9) & 448) >>> 6;
            case 15:
                return (rc.readSharedArray(9) & 7);
            case 16:
                return (rc.readSharedArray(10) & 7168) >>> 10;
            case 17:
                return (rc.readSharedArray(10) & 112) >>> 4;
            case 18:
                return ((rc.readSharedArray(10) & 1) << 2) + ((rc.readSharedArray(11) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(11) & 1792) >>> 8;
            case 20:
                return (rc.readSharedArray(11) & 28) >>> 2;
            case 21:
                return (rc.readSharedArray(12) & 28672) >>> 12;
            case 22:
                return (rc.readSharedArray(12) & 448) >>> 6;
            case 23:
                return (rc.readSharedArray(12) & 7);
            case 24:
                return (rc.readSharedArray(13) & 7168) >>> 10;
            case 25:
                return (rc.readSharedArray(13) & 112) >>> 4;
            case 26:
                return ((rc.readSharedArray(13) & 1) << 2) + ((rc.readSharedArray(14) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(14) & 1792) >>> 8;
            case 28:
                return (rc.readSharedArray(14) & 28) >>> 2;
            case 29:
                return (rc.readSharedArray(15) & 28672) >>> 12;
            case 30:
                return (rc.readSharedArray(15) & 448) >>> 6;
            case 31:
                return (rc.readSharedArray(15) & 7);
            case 32:
                return (rc.readSharedArray(16) & 7168) >>> 10;
            case 33:
                return (rc.readSharedArray(16) & 112) >>> 4;
            case 34:
                return ((rc.readSharedArray(16) & 1) << 2) + ((rc.readSharedArray(17) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(17) & 1792) >>> 8;
            case 36:
                return (rc.readSharedArray(17) & 28) >>> 2;
            case 37:
                return (rc.readSharedArray(18) & 28672) >>> 12;
            case 38:
                return (rc.readSharedArray(18) & 448) >>> 6;
            case 39:
                return (rc.readSharedArray(18) & 7);
            case 40:
                return (rc.readSharedArray(19) & 7168) >>> 10;
            case 41:
                return (rc.readSharedArray(19) & 112) >>> 4;
            case 42:
                return ((rc.readSharedArray(19) & 1) << 2) + ((rc.readSharedArray(20) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(20) & 1792) >>> 8;
            case 44:
                return (rc.readSharedArray(20) & 28) >>> 2;
            case 45:
                return (rc.readSharedArray(21) & 28672) >>> 12;
            case 46:
                return (rc.readSharedArray(21) & 448) >>> 6;
            case 47:
                return (rc.readSharedArray(21) & 7);
            case 48:
                return (rc.readSharedArray(22) & 7168) >>> 10;
            case 49:
                return (rc.readSharedArray(22) & 112) >>> 4;
            case 50:
                return ((rc.readSharedArray(22) & 1) << 2) + ((rc.readSharedArray(23) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(23) & 1792) >>> 8;
            case 52:
                return (rc.readSharedArray(23) & 28) >>> 2;
            case 53:
                return (rc.readSharedArray(24) & 28672) >>> 12;
            case 54:
                return (rc.readSharedArray(24) & 448) >>> 6;
            case 55:
                return (rc.readSharedArray(24) & 7);
            case 56:
                return (rc.readSharedArray(25) & 7168) >>> 10;
            case 57:
                return (rc.readSharedArray(25) & 112) >>> 4;
            case 58:
                return ((rc.readSharedArray(25) & 1) << 2) + ((rc.readSharedArray(26) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(26) & 1792) >>> 8;
            case 60:
                return (rc.readSharedArray(26) & 28) >>> 2;
            case 61:
                return (rc.readSharedArray(27) & 28672) >>> 12;
            case 62:
                return (rc.readSharedArray(27) & 448) >>> 6;
            case 63:
                return (rc.readSharedArray(27) & 7);
            case 64:
                return (rc.readSharedArray(28) & 7168) >>> 10;
            case 65:
                return (rc.readSharedArray(28) & 112) >>> 4;
            case 66:
                return ((rc.readSharedArray(28) & 1) << 2) + ((rc.readSharedArray(29) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(29) & 1792) >>> 8;
            case 68:
                return (rc.readSharedArray(29) & 28) >>> 2;
            case 69:
                return (rc.readSharedArray(30) & 28672) >>> 12;
            case 70:
                return (rc.readSharedArray(30) & 448) >>> 6;
            case 71:
                return (rc.readSharedArray(30) & 7);
            case 72:
                return (rc.readSharedArray(31) & 7168) >>> 10;
            case 73:
                return (rc.readSharedArray(31) & 112) >>> 4;
            case 74:
                return ((rc.readSharedArray(31) & 1) << 2) + ((rc.readSharedArray(32) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(32) & 1792) >>> 8;
            case 76:
                return (rc.readSharedArray(32) & 28) >>> 2;
            case 77:
                return (rc.readSharedArray(33) & 28672) >>> 12;
            case 78:
                return (rc.readSharedArray(33) & 448) >>> 6;
            case 79:
                return (rc.readSharedArray(33) & 7);
            case 80:
                return (rc.readSharedArray(34) & 7168) >>> 10;
            case 81:
                return (rc.readSharedArray(34) & 112) >>> 4;
            case 82:
                return ((rc.readSharedArray(34) & 1) << 2) + ((rc.readSharedArray(35) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(35) & 1792) >>> 8;
            case 84:
                return (rc.readSharedArray(35) & 28) >>> 2;
            case 85:
                return (rc.readSharedArray(36) & 28672) >>> 12;
            case 86:
                return (rc.readSharedArray(36) & 448) >>> 6;
            case 87:
                return (rc.readSharedArray(36) & 7);
            case 88:
                return (rc.readSharedArray(37) & 7168) >>> 10;
            case 89:
                return (rc.readSharedArray(37) & 112) >>> 4;
            case 90:
                return ((rc.readSharedArray(37) & 1) << 2) + ((rc.readSharedArray(38) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(38) & 1792) >>> 8;
            case 92:
                return (rc.readSharedArray(38) & 28) >>> 2;
            case 93:
                return (rc.readSharedArray(39) & 28672) >>> 12;
            case 94:
                return (rc.readSharedArray(39) & 448) >>> 6;
            case 95:
                return (rc.readSharedArray(39) & 7);
            case 96:
                return (rc.readSharedArray(40) & 7168) >>> 10;
            case 97:
                return (rc.readSharedArray(40) & 112) >>> 4;
            case 98:
                return ((rc.readSharedArray(40) & 1) << 2) + ((rc.readSharedArray(41) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(41) & 1792) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 58367) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65423) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 63743) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65507) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 36863) | (value << 12));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65087) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65528) | (value));
                break;
            case 8:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 58367) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65423) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 63743) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65507) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 36863) | (value << 12));
                break;
            case 14:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65087) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65528) | (value));
                break;
            case 16:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 58367) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65423) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 63743) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65507) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 36863) | (value << 12));
                break;
            case 22:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65087) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65528) | (value));
                break;
            case 24:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 58367) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65423) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 63743) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65507) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 36863) | (value << 12));
                break;
            case 30:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65087) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65528) | (value));
                break;
            case 32:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 58367) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65423) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 63743) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65507) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 36863) | (value << 12));
                break;
            case 38:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65087) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65528) | (value));
                break;
            case 40:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 58367) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65423) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 63743) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65507) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 36863) | (value << 12));
                break;
            case 46:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65087) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65528) | (value));
                break;
            case 48:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 58367) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65423) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 63743) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65507) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 36863) | (value << 12));
                break;
            case 54:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65087) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65528) | (value));
                break;
            case 56:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 58367) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65423) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 63743) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65507) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 36863) | (value << 12));
                break;
            case 62:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65087) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65528) | (value));
                break;
            case 64:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 58367) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65423) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 63743) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65507) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 36863) | (value << 12));
                break;
            case 70:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65087) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65528) | (value));
                break;
            case 72:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 58367) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65423) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 63743) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65507) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 36863) | (value << 12));
                break;
            case 78:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65087) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65528) | (value));
                break;
            case 80:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 58367) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65423) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 63743) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65507) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 36863) | (value << 12));
                break;
            case 86:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65087) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65528) | (value));
                break;
            case 88:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 58367) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65423) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 63743) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65507) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 36863) | (value << 12));
                break;
            case 94:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65087) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65528) | (value));
                break;
            case 96:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 58367) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65423) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 63743) | (value << 8));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 64512) >>> 10;
            case 1:
                return (rc.readSharedArray(4) & 1008) >>> 4;
            case 2:
                return ((rc.readSharedArray(4) & 15) << 2) + ((rc.readSharedArray(5) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(5) & 16128) >>> 8;
            case 4:
                return (rc.readSharedArray(5) & 252) >>> 2;
            case 5:
                return ((rc.readSharedArray(5) & 3) << 4) + ((rc.readSharedArray(6) & 61440) >>> 12);
            case 6:
                return (rc.readSharedArray(6) & 4032) >>> 6;
            case 7:
                return (rc.readSharedArray(6) & 63);
            case 8:
                return (rc.readSharedArray(7) & 64512) >>> 10;
            case 9:
                return (rc.readSharedArray(7) & 1008) >>> 4;
            case 10:
                return ((rc.readSharedArray(7) & 15) << 2) + ((rc.readSharedArray(8) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(8) & 16128) >>> 8;
            case 12:
                return (rc.readSharedArray(8) & 252) >>> 2;
            case 13:
                return ((rc.readSharedArray(8) & 3) << 4) + ((rc.readSharedArray(9) & 61440) >>> 12);
            case 14:
                return (rc.readSharedArray(9) & 4032) >>> 6;
            case 15:
                return (rc.readSharedArray(9) & 63);
            case 16:
                return (rc.readSharedArray(10) & 64512) >>> 10;
            case 17:
                return (rc.readSharedArray(10) & 1008) >>> 4;
            case 18:
                return ((rc.readSharedArray(10) & 15) << 2) + ((rc.readSharedArray(11) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(11) & 16128) >>> 8;
            case 20:
                return (rc.readSharedArray(11) & 252) >>> 2;
            case 21:
                return ((rc.readSharedArray(11) & 3) << 4) + ((rc.readSharedArray(12) & 61440) >>> 12);
            case 22:
                return (rc.readSharedArray(12) & 4032) >>> 6;
            case 23:
                return (rc.readSharedArray(12) & 63);
            case 24:
                return (rc.readSharedArray(13) & 64512) >>> 10;
            case 25:
                return (rc.readSharedArray(13) & 1008) >>> 4;
            case 26:
                return ((rc.readSharedArray(13) & 15) << 2) + ((rc.readSharedArray(14) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(14) & 16128) >>> 8;
            case 28:
                return (rc.readSharedArray(14) & 252) >>> 2;
            case 29:
                return ((rc.readSharedArray(14) & 3) << 4) + ((rc.readSharedArray(15) & 61440) >>> 12);
            case 30:
                return (rc.readSharedArray(15) & 4032) >>> 6;
            case 31:
                return (rc.readSharedArray(15) & 63);
            case 32:
                return (rc.readSharedArray(16) & 64512) >>> 10;
            case 33:
                return (rc.readSharedArray(16) & 1008) >>> 4;
            case 34:
                return ((rc.readSharedArray(16) & 15) << 2) + ((rc.readSharedArray(17) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(17) & 16128) >>> 8;
            case 36:
                return (rc.readSharedArray(17) & 252) >>> 2;
            case 37:
                return ((rc.readSharedArray(17) & 3) << 4) + ((rc.readSharedArray(18) & 61440) >>> 12);
            case 38:
                return (rc.readSharedArray(18) & 4032) >>> 6;
            case 39:
                return (rc.readSharedArray(18) & 63);
            case 40:
                return (rc.readSharedArray(19) & 64512) >>> 10;
            case 41:
                return (rc.readSharedArray(19) & 1008) >>> 4;
            case 42:
                return ((rc.readSharedArray(19) & 15) << 2) + ((rc.readSharedArray(20) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(20) & 16128) >>> 8;
            case 44:
                return (rc.readSharedArray(20) & 252) >>> 2;
            case 45:
                return ((rc.readSharedArray(20) & 3) << 4) + ((rc.readSharedArray(21) & 61440) >>> 12);
            case 46:
                return (rc.readSharedArray(21) & 4032) >>> 6;
            case 47:
                return (rc.readSharedArray(21) & 63);
            case 48:
                return (rc.readSharedArray(22) & 64512) >>> 10;
            case 49:
                return (rc.readSharedArray(22) & 1008) >>> 4;
            case 50:
                return ((rc.readSharedArray(22) & 15) << 2) + ((rc.readSharedArray(23) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(23) & 16128) >>> 8;
            case 52:
                return (rc.readSharedArray(23) & 252) >>> 2;
            case 53:
                return ((rc.readSharedArray(23) & 3) << 4) + ((rc.readSharedArray(24) & 61440) >>> 12);
            case 54:
                return (rc.readSharedArray(24) & 4032) >>> 6;
            case 55:
                return (rc.readSharedArray(24) & 63);
            case 56:
                return (rc.readSharedArray(25) & 64512) >>> 10;
            case 57:
                return (rc.readSharedArray(25) & 1008) >>> 4;
            case 58:
                return ((rc.readSharedArray(25) & 15) << 2) + ((rc.readSharedArray(26) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(26) & 16128) >>> 8;
            case 60:
                return (rc.readSharedArray(26) & 252) >>> 2;
            case 61:
                return ((rc.readSharedArray(26) & 3) << 4) + ((rc.readSharedArray(27) & 61440) >>> 12);
            case 62:
                return (rc.readSharedArray(27) & 4032) >>> 6;
            case 63:
                return (rc.readSharedArray(27) & 63);
            case 64:
                return (rc.readSharedArray(28) & 64512) >>> 10;
            case 65:
                return (rc.readSharedArray(28) & 1008) >>> 4;
            case 66:
                return ((rc.readSharedArray(28) & 15) << 2) + ((rc.readSharedArray(29) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(29) & 16128) >>> 8;
            case 68:
                return (rc.readSharedArray(29) & 252) >>> 2;
            case 69:
                return ((rc.readSharedArray(29) & 3) << 4) + ((rc.readSharedArray(30) & 61440) >>> 12);
            case 70:
                return (rc.readSharedArray(30) & 4032) >>> 6;
            case 71:
                return (rc.readSharedArray(30) & 63);
            case 72:
                return (rc.readSharedArray(31) & 64512) >>> 10;
            case 73:
                return (rc.readSharedArray(31) & 1008) >>> 4;
            case 74:
                return ((rc.readSharedArray(31) & 15) << 2) + ((rc.readSharedArray(32) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(32) & 16128) >>> 8;
            case 76:
                return (rc.readSharedArray(32) & 252) >>> 2;
            case 77:
                return ((rc.readSharedArray(32) & 3) << 4) + ((rc.readSharedArray(33) & 61440) >>> 12);
            case 78:
                return (rc.readSharedArray(33) & 4032) >>> 6;
            case 79:
                return (rc.readSharedArray(33) & 63);
            case 80:
                return (rc.readSharedArray(34) & 64512) >>> 10;
            case 81:
                return (rc.readSharedArray(34) & 1008) >>> 4;
            case 82:
                return ((rc.readSharedArray(34) & 15) << 2) + ((rc.readSharedArray(35) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(35) & 16128) >>> 8;
            case 84:
                return (rc.readSharedArray(35) & 252) >>> 2;
            case 85:
                return ((rc.readSharedArray(35) & 3) << 4) + ((rc.readSharedArray(36) & 61440) >>> 12);
            case 86:
                return (rc.readSharedArray(36) & 4032) >>> 6;
            case 87:
                return (rc.readSharedArray(36) & 63);
            case 88:
                return (rc.readSharedArray(37) & 64512) >>> 10;
            case 89:
                return (rc.readSharedArray(37) & 1008) >>> 4;
            case 90:
                return ((rc.readSharedArray(37) & 15) << 2) + ((rc.readSharedArray(38) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(38) & 16128) >>> 8;
            case 92:
                return (rc.readSharedArray(38) & 252) >>> 2;
            case 93:
                return ((rc.readSharedArray(38) & 3) << 4) + ((rc.readSharedArray(39) & 61440) >>> 12);
            case 94:
                return (rc.readSharedArray(39) & 4032) >>> 6;
            case 95:
                return (rc.readSharedArray(39) & 63);
            case 96:
                return (rc.readSharedArray(40) & 64512) >>> 10;
            case 97:
                return (rc.readSharedArray(40) & 1008) >>> 4;
            case 98:
                return ((rc.readSharedArray(40) & 15) << 2) + ((rc.readSharedArray(41) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(41) & 16128) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 1023) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 64527) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 49407) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65283) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 4095) | ((value & 15) << 12));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 61503) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65472) | (value));
                break;
            case 8:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 1023) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 64527) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 49407) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65283) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 4095) | ((value & 15) << 12));
                break;
            case 14:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 61503) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65472) | (value));
                break;
            case 16:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 1023) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 64527) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 49407) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65283) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 4095) | ((value & 15) << 12));
                break;
            case 22:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 61503) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65472) | (value));
                break;
            case 24:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 1023) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 64527) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 49407) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65283) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 4095) | ((value & 15) << 12));
                break;
            case 30:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 61503) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65472) | (value));
                break;
            case 32:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 1023) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 64527) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 49407) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65283) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 4095) | ((value & 15) << 12));
                break;
            case 38:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 61503) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65472) | (value));
                break;
            case 40:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 1023) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 64527) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 49407) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65283) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 4095) | ((value & 15) << 12));
                break;
            case 46:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 61503) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65472) | (value));
                break;
            case 48:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 1023) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 64527) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 49407) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65283) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 4095) | ((value & 15) << 12));
                break;
            case 54:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 61503) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65472) | (value));
                break;
            case 56:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 1023) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 64527) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 49407) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65283) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 4095) | ((value & 15) << 12));
                break;
            case 62:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 61503) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65472) | (value));
                break;
            case 64:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 1023) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 64527) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 49407) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65283) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 4095) | ((value & 15) << 12));
                break;
            case 70:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 61503) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65472) | (value));
                break;
            case 72:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 1023) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 64527) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 49407) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65283) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 4095) | ((value & 15) << 12));
                break;
            case 78:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 61503) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65472) | (value));
                break;
            case 80:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 1023) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 64527) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 49407) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65283) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 4095) | ((value & 15) << 12));
                break;
            case 86:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 61503) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65472) | (value));
                break;
            case 88:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 1023) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 64527) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 49407) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65283) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 4095) | ((value & 15) << 12));
                break;
            case 94:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 61503) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65472) | (value));
                break;
            case 96:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 1023) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 64527) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 49407) | (value << 8));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(41) & 254) >>> 1;
            case 1:
                return ((rc.readSharedArray(41) & 1) << 6) + ((rc.readSharedArray(42) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(42) & 1016) >>> 3;
            case 3:
                return ((rc.readSharedArray(42) & 7) << 4) + ((rc.readSharedArray(43) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(43) & 4064) >>> 5;
            case 5:
                return ((rc.readSharedArray(43) & 31) << 2) + ((rc.readSharedArray(44) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(44) & 16256) >>> 7;
            case 7:
                return (rc.readSharedArray(44) & 127);
            case 8:
                return (rc.readSharedArray(45) & 65024) >>> 9;
            case 9:
                return (rc.readSharedArray(45) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65281) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 64519) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 61471) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 49279) | (value << 7));
                break;
            case 7:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65408) | (value));
                break;
            case 8:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 511) | (value << 9));
                break;
            case 9:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65027) | (value << 2));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(41) & 254) >>> 1;
            case 1:
                return ((rc.readSharedArray(41) & 1) << 6) + ((rc.readSharedArray(42) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(42) & 1016) >>> 3;
            case 3:
                return ((rc.readSharedArray(42) & 7) << 4) + ((rc.readSharedArray(43) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(43) & 4064) >>> 5;
            case 5:
                return ((rc.readSharedArray(43) & 31) << 2) + ((rc.readSharedArray(44) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(44) & 16256) >>> 7;
            case 7:
                return (rc.readSharedArray(44) & 127);
            case 8:
                return (rc.readSharedArray(45) & 65024) >>> 9;
            case 9:
                return (rc.readSharedArray(45) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65281) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 64519) | (value << 3));
                break;
            case 3:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 61471) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 49279) | (value << 7));
                break;
            case 7:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65408) | (value));
                break;
            case 8:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 511) | (value << 9));
                break;
            case 9:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65027) | (value << 2));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(45) & 2) >>> 1;
            case 1:
                return (rc.readSharedArray(46) & 512) >>> 9;
            case 2:
                return (rc.readSharedArray(46) & 2) >>> 1;
            case 3:
                return (rc.readSharedArray(47) & 512) >>> 9;
            case 4:
                return (rc.readSharedArray(47) & 2) >>> 1;
            case 5:
                return (rc.readSharedArray(48) & 512) >>> 9;
            case 6:
                return (rc.readSharedArray(48) & 2) >>> 1;
            case 7:
                return (rc.readSharedArray(49) & 512) >>> 9;
            case 8:
                return (rc.readSharedArray(49) & 2) >>> 1;
            case 9:
                return (rc.readSharedArray(50) & 512) >>> 9;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65533) | (value << 1));
                break;
            case 1:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65023) | (value << 9));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65533) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65023) | (value << 9));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65533) | (value << 1));
                break;
            case 5:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65023) | (value << 9));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65533) | (value << 1));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65023) | (value << 9));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65533) | (value << 1));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65023) | (value << 9));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(45) & 1) << 6) + ((rc.readSharedArray(46) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(46) & 508) >>> 2;
            case 2:
                return ((rc.readSharedArray(46) & 1) << 6) + ((rc.readSharedArray(47) & 64512) >>> 10);
            case 3:
                return (rc.readSharedArray(47) & 508) >>> 2;
            case 4:
                return ((rc.readSharedArray(47) & 1) << 6) + ((rc.readSharedArray(48) & 64512) >>> 10);
            case 5:
                return (rc.readSharedArray(48) & 508) >>> 2;
            case 6:
                return ((rc.readSharedArray(48) & 1) << 6) + ((rc.readSharedArray(49) & 64512) >>> 10);
            case 7:
                return (rc.readSharedArray(49) & 508) >>> 2;
            case 8:
                return ((rc.readSharedArray(49) & 1) << 6) + ((rc.readSharedArray(50) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(50) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65027) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 1023) | ((value & 63) << 10));
                break;
            case 3:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65027) | (value << 2));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 1023) | ((value & 63) << 10));
                break;
            case 5:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 1023) | ((value & 63) << 10));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65027) | (value << 2));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(45) & 3) << 6) + ((rc.readSharedArray(46) & 64512) >>> 10);
            case 1:
                return (rc.readSharedArray(46) & 1020) >>> 2;
            case 2:
                return ((rc.readSharedArray(46) & 3) << 6) + ((rc.readSharedArray(47) & 64512) >>> 10);
            case 3:
                return (rc.readSharedArray(47) & 1020) >>> 2;
            case 4:
                return ((rc.readSharedArray(47) & 3) << 6) + ((rc.readSharedArray(48) & 64512) >>> 10);
            case 5:
                return (rc.readSharedArray(48) & 1020) >>> 2;
            case 6:
                return ((rc.readSharedArray(48) & 3) << 6) + ((rc.readSharedArray(49) & 64512) >>> 10);
            case 7:
                return (rc.readSharedArray(49) & 1020) >>> 2;
            case 8:
                return ((rc.readSharedArray(49) & 3) << 6) + ((rc.readSharedArray(50) & 64512) >>> 10);
            case 9:
                return (rc.readSharedArray(50) & 1020) >>> 2;
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 1023) | ((value & 63) << 10));
                break;
            case 1:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 64515) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 1023) | ((value & 63) << 10));
                break;
            case 3:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 64515) | (value << 2));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 1023) | ((value & 63) << 10));
                break;
            case 5:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64515) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 1023) | ((value & 63) << 10));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 64515) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65532) | ((value & 192) >>> 6));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 1023) | ((value & 63) << 10));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 64515) | (value << 2));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(50) & 3) << 1) + ((rc.readSharedArray(51) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(51) & 224) >>> 5;
            case 2:
                return (rc.readSharedArray(52) & 14336) >>> 11;
            case 3:
                return (rc.readSharedArray(52) & 14) >>> 1;
            case 4:
                return (rc.readSharedArray(53) & 896) >>> 7;
            case 5:
                return (rc.readSharedArray(54) & 57344) >>> 13;
            case 6:
                return (rc.readSharedArray(54) & 56) >>> 3;
            case 7:
                return (rc.readSharedArray(55) & 3584) >>> 9;
            case 8:
                return ((rc.readSharedArray(55) & 3) << 1) + ((rc.readSharedArray(56) & 32768) >>> 15);
            case 9:
                return (rc.readSharedArray(56) & 224) >>> 5;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65311) | (value << 5));
                break;
            case 2:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 51199) | (value << 11));
                break;
            case 3:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65521) | (value << 1));
                break;
            case 4:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 64639) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 8191) | (value << 13));
                break;
            case 6:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65479) | (value << 3));
                break;
            case 7:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 61951) | (value << 9));
                break;
            case 8:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 32767) | ((value & 1) << 15));
                break;
            case 9:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65311) | (value << 5));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(51) & 32512) >>> 8;
            case 1:
                return ((rc.readSharedArray(51) & 31) << 2) + ((rc.readSharedArray(52) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(52) & 2032) >>> 4;
            case 3:
                return ((rc.readSharedArray(52) & 1) << 6) + ((rc.readSharedArray(53) & 64512) >>> 10);
            case 4:
                return (rc.readSharedArray(53) & 127);
            case 5:
                return (rc.readSharedArray(54) & 8128) >>> 6;
            case 6:
                return ((rc.readSharedArray(54) & 7) << 4) + ((rc.readSharedArray(55) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(55) & 508) >>> 2;
            case 8:
                return (rc.readSharedArray(56) & 32512) >>> 8;
            case 9:
                return ((rc.readSharedArray(56) & 31) << 2) + ((rc.readSharedArray(57) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 33023) | (value << 8));
                break;
            case 1:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 63503) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 1023) | ((value & 63) << 10));
                break;
            case 4:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65408) | (value));
                break;
            case 5:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 57407) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65027) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 33023) | (value << 8));
                break;
            case 9:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(50) & 3) << 8) + ((rc.readSharedArray(51) & 65280) >>> 8);
            case 1:
                return ((rc.readSharedArray(51) & 255) << 2) + ((rc.readSharedArray(52) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(52) & 16368) >>> 4;
            case 3:
                return ((rc.readSharedArray(52) & 15) << 6) + ((rc.readSharedArray(53) & 64512) >>> 10);
            case 4:
                return (rc.readSharedArray(53) & 1023);
            case 5:
                return (rc.readSharedArray(54) & 65472) >>> 6;
            case 6:
                return ((rc.readSharedArray(54) & 63) << 4) + ((rc.readSharedArray(55) & 61440) >>> 12);
            case 7:
                return (rc.readSharedArray(55) & 4092) >>> 2;
            case 8:
                return ((rc.readSharedArray(55) & 3) << 8) + ((rc.readSharedArray(56) & 65280) >>> 8);
            case 9:
                return ((rc.readSharedArray(56) & 255) << 2) + ((rc.readSharedArray(57) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 255) | ((value & 255) << 8));
                break;
            case 1:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 49167) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 1023) | ((value & 63) << 10));
                break;
            case 4:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 64512) | (value));
                break;
            case 5:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 63) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 4095) | ((value & 15) << 12));
                break;
            case 7:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 61443) | (value << 2));
                break;
            case 8:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 255) | ((value & 255) << 8));
                break;
            case 9:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readFillerDoNotUse() throws GameActionException {
        return (rc.readSharedArray(57) & 16128) >>> 8;
    }

    public void writeFillerDoNotUse(int value) throws GameActionException {
        rc.writeSharedArray(57, (rc.readSharedArray(57) & 49407) | (value << 8));
    }

    public int readFillerDoNotUseAll() throws GameActionException {
        return (rc.readSharedArray(57) & 16128) >>> 8;
    }

    public void writeFillerDoNotUseAll(int value) throws GameActionException {
        rc.writeSharedArray(57, (rc.readSharedArray(57) & 49407) | (value << 8));
    }

    public int readMinerCount() throws GameActionException {
        return (rc.readSharedArray(57) & 255);
    }

    public void writeMinerCount(int value) throws GameActionException {
        rc.writeSharedArray(57, (rc.readSharedArray(57) & 65280) | (value));
    }

    public int readMinerCountAll() throws GameActionException {
        return (rc.readSharedArray(57) & 255);
    }

    public void writeMinerCountAll(int value) throws GameActionException {
        rc.writeSharedArray(57, (rc.readSharedArray(57) & 65280) | (value));
    }

    public int readSoldierCount() throws GameActionException {
        return (rc.readSharedArray(58) & 65280) >>> 8;
    }

    public void writeSoldierCount(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 255) | (value << 8));
    }

    public int readSoldierCountAll() throws GameActionException {
        return (rc.readSharedArray(58) & 65280) >>> 8;
    }

    public void writeSoldierCountAll(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 255) | (value << 8));
    }

    public int readLastArchonNum() throws GameActionException {
        return (rc.readSharedArray(58) & 192) >>> 6;
    }

    public void writeLastArchonNum(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 65343) | (value << 6));
    }

    public int readLastArchonAll() throws GameActionException {
        return (rc.readSharedArray(58) & 192) >>> 6;
    }

    public void writeLastArchonAll(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 65343) | (value << 6));
    }

    public int readReservedResourcesLead() throws GameActionException {
        return ((rc.readSharedArray(58) & 63) << 4) + ((rc.readSharedArray(59) & 61440) >>> 12);
    }

    public void writeReservedResourcesLead(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 65472) | ((value & 1008) >>> 4));
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 4095) | ((value & 15) << 12));
    }

    public int readReservedResourcesGold() throws GameActionException {
        return (rc.readSharedArray(59) & 4032) >>> 6;
    }

    public void writeReservedResourcesGold(int value) throws GameActionException {
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 61503) | (value << 6));
    }

    public int readReservedResourcesAll() throws GameActionException {
        return ((rc.readSharedArray(58) & 63) << 10) + ((rc.readSharedArray(59) & 65472) >>> 6);
    }

    public void writeReservedResourcesAll(int value) throws GameActionException {
        rc.writeSharedArray(58, (rc.readSharedArray(58) & 65472) | ((value & 64512) >>> 10));
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 63) | ((value & 1023) << 6));
    }
}