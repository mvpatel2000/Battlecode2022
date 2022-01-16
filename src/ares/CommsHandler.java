package ares;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int MINER_COUNT_SLOTS = 1;
    final int SOLDIER_COUNT_SLOTS = 1;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;
    final int LAST_ARCHON_SLOTS = 1;
    final int RESERVED_RESOURCES_SLOTS = 1;

    // ENEMY_OFFSET = minimum control status
    // representing enemy control.
    final int CONTROL_STATUS_ENEMY_OFFSET = 3;
    final int CLUSTER_ENEMY_LEVELS = 3;

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
        rc.writeSharedArray(42, 63);
        rc.writeSharedArray(43, 40911);
        rc.writeSharedArray(44, 59379);
        rc.writeSharedArray(45, 63996);
        rc.writeSharedArray(46, 65151);
        rc.writeSharedArray(47, 16287);
        rc.writeSharedArray(48, 57311);
        rc.writeSharedArray(49, 57311);
        rc.writeSharedArray(50, 57311);
        rc.writeSharedArray(51, 57311);
        rc.writeSharedArray(52, 57311);
        rc.writeSharedArray(53, 51185);
        rc.writeSharedArray(54, 64639);
        rc.writeSharedArray(55, 8135);
        rc.writeSharedArray(56, 61948);
        rc.writeSharedArray(57, 32543);
        rc.writeSharedArray(58, 51185);
        rc.writeSharedArray(59, 64512);
    }


    public int readOurArchonStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(0) & 61440) >>> 12;
            case 1:
                return (rc.readSharedArray(1) & 61440) >>> 12;
            case 2:
                return (rc.readSharedArray(2) & 61440) >>> 12;
            case 3:
                return (rc.readSharedArray(3) & 61440) >>> 12;
            default:
                return -1;
        }
    }

    public void writeOurArchonStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(0, (rc.readSharedArray(0) & 4095) | (value << 12));
                break;
            case 1:
                rc.writeSharedArray(1, (rc.readSharedArray(1) & 4095) | (value << 12));
                break;
            case 2:
                rc.writeSharedArray(2, (rc.readSharedArray(2) & 4095) | (value << 12));
                break;
            case 3:
                rc.writeSharedArray(3, (rc.readSharedArray(3) & 4095) | (value << 12));
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

    public int readMinerCount() throws GameActionException {
        return (rc.readSharedArray(4) & 65280) >>> 8;
    }

    public void writeMinerCount(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 255) | (value << 8));
    }

    public int readMinerCountAll() throws GameActionException {
        return (rc.readSharedArray(4) & 65280) >>> 8;
    }

    public void writeMinerCountAll(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 255) | (value << 8));
    }

    public int readSoldierCount() throws GameActionException {
        return (rc.readSharedArray(4) & 255);
    }

    public void writeSoldierCount(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 65280) | (value));
    }

    public int readSoldierCountAll() throws GameActionException {
        return (rc.readSharedArray(4) & 255);
    }

    public void writeSoldierCountAll(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 65280) | (value));
    }

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(5) & 57344) >>> 13;
            case 1:
                return (rc.readSharedArray(5) & 896) >>> 7;
            case 2:
                return (rc.readSharedArray(5) & 14) >>> 1;
            case 3:
                return (rc.readSharedArray(6) & 14336) >>> 11;
            case 4:
                return (rc.readSharedArray(6) & 224) >>> 5;
            case 5:
                return ((rc.readSharedArray(6) & 3) << 1) + ((rc.readSharedArray(7) & 32768) >>> 15);
            case 6:
                return (rc.readSharedArray(7) & 3584) >>> 9;
            case 7:
                return (rc.readSharedArray(7) & 56) >>> 3;
            case 8:
                return (rc.readSharedArray(8) & 57344) >>> 13;
            case 9:
                return (rc.readSharedArray(8) & 896) >>> 7;
            case 10:
                return (rc.readSharedArray(8) & 14) >>> 1;
            case 11:
                return (rc.readSharedArray(9) & 14336) >>> 11;
            case 12:
                return (rc.readSharedArray(9) & 224) >>> 5;
            case 13:
                return ((rc.readSharedArray(9) & 3) << 1) + ((rc.readSharedArray(10) & 32768) >>> 15);
            case 14:
                return (rc.readSharedArray(10) & 3584) >>> 9;
            case 15:
                return (rc.readSharedArray(10) & 56) >>> 3;
            case 16:
                return (rc.readSharedArray(11) & 57344) >>> 13;
            case 17:
                return (rc.readSharedArray(11) & 896) >>> 7;
            case 18:
                return (rc.readSharedArray(11) & 14) >>> 1;
            case 19:
                return (rc.readSharedArray(12) & 14336) >>> 11;
            case 20:
                return (rc.readSharedArray(12) & 224) >>> 5;
            case 21:
                return ((rc.readSharedArray(12) & 3) << 1) + ((rc.readSharedArray(13) & 32768) >>> 15);
            case 22:
                return (rc.readSharedArray(13) & 3584) >>> 9;
            case 23:
                return (rc.readSharedArray(13) & 56) >>> 3;
            case 24:
                return (rc.readSharedArray(14) & 57344) >>> 13;
            case 25:
                return (rc.readSharedArray(14) & 896) >>> 7;
            case 26:
                return (rc.readSharedArray(14) & 14) >>> 1;
            case 27:
                return (rc.readSharedArray(15) & 14336) >>> 11;
            case 28:
                return (rc.readSharedArray(15) & 224) >>> 5;
            case 29:
                return ((rc.readSharedArray(15) & 3) << 1) + ((rc.readSharedArray(16) & 32768) >>> 15);
            case 30:
                return (rc.readSharedArray(16) & 3584) >>> 9;
            case 31:
                return (rc.readSharedArray(16) & 56) >>> 3;
            case 32:
                return (rc.readSharedArray(17) & 57344) >>> 13;
            case 33:
                return (rc.readSharedArray(17) & 896) >>> 7;
            case 34:
                return (rc.readSharedArray(17) & 14) >>> 1;
            case 35:
                return (rc.readSharedArray(18) & 14336) >>> 11;
            case 36:
                return (rc.readSharedArray(18) & 224) >>> 5;
            case 37:
                return ((rc.readSharedArray(18) & 3) << 1) + ((rc.readSharedArray(19) & 32768) >>> 15);
            case 38:
                return (rc.readSharedArray(19) & 3584) >>> 9;
            case 39:
                return (rc.readSharedArray(19) & 56) >>> 3;
            case 40:
                return (rc.readSharedArray(20) & 57344) >>> 13;
            case 41:
                return (rc.readSharedArray(20) & 896) >>> 7;
            case 42:
                return (rc.readSharedArray(20) & 14) >>> 1;
            case 43:
                return (rc.readSharedArray(21) & 14336) >>> 11;
            case 44:
                return (rc.readSharedArray(21) & 224) >>> 5;
            case 45:
                return ((rc.readSharedArray(21) & 3) << 1) + ((rc.readSharedArray(22) & 32768) >>> 15);
            case 46:
                return (rc.readSharedArray(22) & 3584) >>> 9;
            case 47:
                return (rc.readSharedArray(22) & 56) >>> 3;
            case 48:
                return (rc.readSharedArray(23) & 57344) >>> 13;
            case 49:
                return (rc.readSharedArray(23) & 896) >>> 7;
            case 50:
                return (rc.readSharedArray(23) & 14) >>> 1;
            case 51:
                return (rc.readSharedArray(24) & 14336) >>> 11;
            case 52:
                return (rc.readSharedArray(24) & 224) >>> 5;
            case 53:
                return ((rc.readSharedArray(24) & 3) << 1) + ((rc.readSharedArray(25) & 32768) >>> 15);
            case 54:
                return (rc.readSharedArray(25) & 3584) >>> 9;
            case 55:
                return (rc.readSharedArray(25) & 56) >>> 3;
            case 56:
                return (rc.readSharedArray(26) & 57344) >>> 13;
            case 57:
                return (rc.readSharedArray(26) & 896) >>> 7;
            case 58:
                return (rc.readSharedArray(26) & 14) >>> 1;
            case 59:
                return (rc.readSharedArray(27) & 14336) >>> 11;
            case 60:
                return (rc.readSharedArray(27) & 224) >>> 5;
            case 61:
                return ((rc.readSharedArray(27) & 3) << 1) + ((rc.readSharedArray(28) & 32768) >>> 15);
            case 62:
                return (rc.readSharedArray(28) & 3584) >>> 9;
            case 63:
                return (rc.readSharedArray(28) & 56) >>> 3;
            case 64:
                return (rc.readSharedArray(29) & 57344) >>> 13;
            case 65:
                return (rc.readSharedArray(29) & 896) >>> 7;
            case 66:
                return (rc.readSharedArray(29) & 14) >>> 1;
            case 67:
                return (rc.readSharedArray(30) & 14336) >>> 11;
            case 68:
                return (rc.readSharedArray(30) & 224) >>> 5;
            case 69:
                return ((rc.readSharedArray(30) & 3) << 1) + ((rc.readSharedArray(31) & 32768) >>> 15);
            case 70:
                return (rc.readSharedArray(31) & 3584) >>> 9;
            case 71:
                return (rc.readSharedArray(31) & 56) >>> 3;
            case 72:
                return (rc.readSharedArray(32) & 57344) >>> 13;
            case 73:
                return (rc.readSharedArray(32) & 896) >>> 7;
            case 74:
                return (rc.readSharedArray(32) & 14) >>> 1;
            case 75:
                return (rc.readSharedArray(33) & 14336) >>> 11;
            case 76:
                return (rc.readSharedArray(33) & 224) >>> 5;
            case 77:
                return ((rc.readSharedArray(33) & 3) << 1) + ((rc.readSharedArray(34) & 32768) >>> 15);
            case 78:
                return (rc.readSharedArray(34) & 3584) >>> 9;
            case 79:
                return (rc.readSharedArray(34) & 56) >>> 3;
            case 80:
                return (rc.readSharedArray(35) & 57344) >>> 13;
            case 81:
                return (rc.readSharedArray(35) & 896) >>> 7;
            case 82:
                return (rc.readSharedArray(35) & 14) >>> 1;
            case 83:
                return (rc.readSharedArray(36) & 14336) >>> 11;
            case 84:
                return (rc.readSharedArray(36) & 224) >>> 5;
            case 85:
                return ((rc.readSharedArray(36) & 3) << 1) + ((rc.readSharedArray(37) & 32768) >>> 15);
            case 86:
                return (rc.readSharedArray(37) & 3584) >>> 9;
            case 87:
                return (rc.readSharedArray(37) & 56) >>> 3;
            case 88:
                return (rc.readSharedArray(38) & 57344) >>> 13;
            case 89:
                return (rc.readSharedArray(38) & 896) >>> 7;
            case 90:
                return (rc.readSharedArray(38) & 14) >>> 1;
            case 91:
                return (rc.readSharedArray(39) & 14336) >>> 11;
            case 92:
                return (rc.readSharedArray(39) & 224) >>> 5;
            case 93:
                return ((rc.readSharedArray(39) & 3) << 1) + ((rc.readSharedArray(40) & 32768) >>> 15);
            case 94:
                return (rc.readSharedArray(40) & 3584) >>> 9;
            case 95:
                return (rc.readSharedArray(40) & 56) >>> 3;
            case 96:
                return (rc.readSharedArray(41) & 57344) >>> 13;
            case 97:
                return (rc.readSharedArray(41) & 896) >>> 7;
            case 98:
                return (rc.readSharedArray(41) & 14) >>> 1;
            case 99:
                return (rc.readSharedArray(42) & 14336) >>> 11;
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 8191) | (value << 13));
                break;
            case 1:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 64639) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65521) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 51199) | (value << 11));
                break;
            case 4:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65311) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 32767) | ((value & 1) << 15));
                break;
            case 6:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 61951) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65479) | (value << 3));
                break;
            case 8:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 8191) | (value << 13));
                break;
            case 9:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64639) | (value << 7));
                break;
            case 10:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65521) | (value << 1));
                break;
            case 11:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 51199) | (value << 11));
                break;
            case 12:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65311) | (value << 5));
                break;
            case 13:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 32767) | ((value & 1) << 15));
                break;
            case 14:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 61951) | (value << 9));
                break;
            case 15:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65479) | (value << 3));
                break;
            case 16:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 8191) | (value << 13));
                break;
            case 17:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 64639) | (value << 7));
                break;
            case 18:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65521) | (value << 1));
                break;
            case 19:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 51199) | (value << 11));
                break;
            case 20:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65311) | (value << 5));
                break;
            case 21:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 32767) | ((value & 1) << 15));
                break;
            case 22:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 61951) | (value << 9));
                break;
            case 23:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65479) | (value << 3));
                break;
            case 24:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 8191) | (value << 13));
                break;
            case 25:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 64639) | (value << 7));
                break;
            case 26:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65521) | (value << 1));
                break;
            case 27:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 51199) | (value << 11));
                break;
            case 28:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65311) | (value << 5));
                break;
            case 29:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 32767) | ((value & 1) << 15));
                break;
            case 30:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 61951) | (value << 9));
                break;
            case 31:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65479) | (value << 3));
                break;
            case 32:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 8191) | (value << 13));
                break;
            case 33:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 64639) | (value << 7));
                break;
            case 34:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65521) | (value << 1));
                break;
            case 35:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 51199) | (value << 11));
                break;
            case 36:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65311) | (value << 5));
                break;
            case 37:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 32767) | ((value & 1) << 15));
                break;
            case 38:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 61951) | (value << 9));
                break;
            case 39:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65479) | (value << 3));
                break;
            case 40:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 8191) | (value << 13));
                break;
            case 41:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64639) | (value << 7));
                break;
            case 42:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65521) | (value << 1));
                break;
            case 43:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 51199) | (value << 11));
                break;
            case 44:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65311) | (value << 5));
                break;
            case 45:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 32767) | ((value & 1) << 15));
                break;
            case 46:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61951) | (value << 9));
                break;
            case 47:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65479) | (value << 3));
                break;
            case 48:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 8191) | (value << 13));
                break;
            case 49:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64639) | (value << 7));
                break;
            case 50:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65521) | (value << 1));
                break;
            case 51:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 51199) | (value << 11));
                break;
            case 52:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65311) | (value << 5));
                break;
            case 53:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 32767) | ((value & 1) << 15));
                break;
            case 54:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 61951) | (value << 9));
                break;
            case 55:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65479) | (value << 3));
                break;
            case 56:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 8191) | (value << 13));
                break;
            case 57:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 64639) | (value << 7));
                break;
            case 58:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65521) | (value << 1));
                break;
            case 59:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 51199) | (value << 11));
                break;
            case 60:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65311) | (value << 5));
                break;
            case 61:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 32767) | ((value & 1) << 15));
                break;
            case 62:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 61951) | (value << 9));
                break;
            case 63:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65479) | (value << 3));
                break;
            case 64:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 8191) | (value << 13));
                break;
            case 65:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 64639) | (value << 7));
                break;
            case 66:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65521) | (value << 1));
                break;
            case 67:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 51199) | (value << 11));
                break;
            case 68:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65311) | (value << 5));
                break;
            case 69:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 32767) | ((value & 1) << 15));
                break;
            case 70:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 61951) | (value << 9));
                break;
            case 71:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65479) | (value << 3));
                break;
            case 72:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 8191) | (value << 13));
                break;
            case 73:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 64639) | (value << 7));
                break;
            case 74:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65521) | (value << 1));
                break;
            case 75:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 51199) | (value << 11));
                break;
            case 76:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65311) | (value << 5));
                break;
            case 77:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 32767) | ((value & 1) << 15));
                break;
            case 78:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 61951) | (value << 9));
                break;
            case 79:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65479) | (value << 3));
                break;
            case 80:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 8191) | (value << 13));
                break;
            case 81:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64639) | (value << 7));
                break;
            case 82:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65521) | (value << 1));
                break;
            case 83:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 51199) | (value << 11));
                break;
            case 84:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65311) | (value << 5));
                break;
            case 85:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 32767) | ((value & 1) << 15));
                break;
            case 86:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 61951) | (value << 9));
                break;
            case 87:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65479) | (value << 3));
                break;
            case 88:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 8191) | (value << 13));
                break;
            case 89:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 64639) | (value << 7));
                break;
            case 90:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65521) | (value << 1));
                break;
            case 91:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 51199) | (value << 11));
                break;
            case 92:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65311) | (value << 5));
                break;
            case 93:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 32767) | ((value & 1) << 15));
                break;
            case 94:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 61951) | (value << 9));
                break;
            case 95:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65479) | (value << 3));
                break;
            case 96:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 8191) | (value << 13));
                break;
            case 97:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 64639) | (value << 7));
                break;
            case 98:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65521) | (value << 1));
                break;
            case 99:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 51199) | (value << 11));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(5) & 7168) >>> 10;
            case 1:
                return (rc.readSharedArray(5) & 112) >>> 4;
            case 2:
                return ((rc.readSharedArray(5) & 1) << 2) + ((rc.readSharedArray(6) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(6) & 1792) >>> 8;
            case 4:
                return (rc.readSharedArray(6) & 28) >>> 2;
            case 5:
                return (rc.readSharedArray(7) & 28672) >>> 12;
            case 6:
                return (rc.readSharedArray(7) & 448) >>> 6;
            case 7:
                return (rc.readSharedArray(7) & 7);
            case 8:
                return (rc.readSharedArray(8) & 7168) >>> 10;
            case 9:
                return (rc.readSharedArray(8) & 112) >>> 4;
            case 10:
                return ((rc.readSharedArray(8) & 1) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(9) & 1792) >>> 8;
            case 12:
                return (rc.readSharedArray(9) & 28) >>> 2;
            case 13:
                return (rc.readSharedArray(10) & 28672) >>> 12;
            case 14:
                return (rc.readSharedArray(10) & 448) >>> 6;
            case 15:
                return (rc.readSharedArray(10) & 7);
            case 16:
                return (rc.readSharedArray(11) & 7168) >>> 10;
            case 17:
                return (rc.readSharedArray(11) & 112) >>> 4;
            case 18:
                return ((rc.readSharedArray(11) & 1) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(12) & 1792) >>> 8;
            case 20:
                return (rc.readSharedArray(12) & 28) >>> 2;
            case 21:
                return (rc.readSharedArray(13) & 28672) >>> 12;
            case 22:
                return (rc.readSharedArray(13) & 448) >>> 6;
            case 23:
                return (rc.readSharedArray(13) & 7);
            case 24:
                return (rc.readSharedArray(14) & 7168) >>> 10;
            case 25:
                return (rc.readSharedArray(14) & 112) >>> 4;
            case 26:
                return ((rc.readSharedArray(14) & 1) << 2) + ((rc.readSharedArray(15) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(15) & 1792) >>> 8;
            case 28:
                return (rc.readSharedArray(15) & 28) >>> 2;
            case 29:
                return (rc.readSharedArray(16) & 28672) >>> 12;
            case 30:
                return (rc.readSharedArray(16) & 448) >>> 6;
            case 31:
                return (rc.readSharedArray(16) & 7);
            case 32:
                return (rc.readSharedArray(17) & 7168) >>> 10;
            case 33:
                return (rc.readSharedArray(17) & 112) >>> 4;
            case 34:
                return ((rc.readSharedArray(17) & 1) << 2) + ((rc.readSharedArray(18) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(18) & 1792) >>> 8;
            case 36:
                return (rc.readSharedArray(18) & 28) >>> 2;
            case 37:
                return (rc.readSharedArray(19) & 28672) >>> 12;
            case 38:
                return (rc.readSharedArray(19) & 448) >>> 6;
            case 39:
                return (rc.readSharedArray(19) & 7);
            case 40:
                return (rc.readSharedArray(20) & 7168) >>> 10;
            case 41:
                return (rc.readSharedArray(20) & 112) >>> 4;
            case 42:
                return ((rc.readSharedArray(20) & 1) << 2) + ((rc.readSharedArray(21) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(21) & 1792) >>> 8;
            case 44:
                return (rc.readSharedArray(21) & 28) >>> 2;
            case 45:
                return (rc.readSharedArray(22) & 28672) >>> 12;
            case 46:
                return (rc.readSharedArray(22) & 448) >>> 6;
            case 47:
                return (rc.readSharedArray(22) & 7);
            case 48:
                return (rc.readSharedArray(23) & 7168) >>> 10;
            case 49:
                return (rc.readSharedArray(23) & 112) >>> 4;
            case 50:
                return ((rc.readSharedArray(23) & 1) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(24) & 1792) >>> 8;
            case 52:
                return (rc.readSharedArray(24) & 28) >>> 2;
            case 53:
                return (rc.readSharedArray(25) & 28672) >>> 12;
            case 54:
                return (rc.readSharedArray(25) & 448) >>> 6;
            case 55:
                return (rc.readSharedArray(25) & 7);
            case 56:
                return (rc.readSharedArray(26) & 7168) >>> 10;
            case 57:
                return (rc.readSharedArray(26) & 112) >>> 4;
            case 58:
                return ((rc.readSharedArray(26) & 1) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(27) & 1792) >>> 8;
            case 60:
                return (rc.readSharedArray(27) & 28) >>> 2;
            case 61:
                return (rc.readSharedArray(28) & 28672) >>> 12;
            case 62:
                return (rc.readSharedArray(28) & 448) >>> 6;
            case 63:
                return (rc.readSharedArray(28) & 7);
            case 64:
                return (rc.readSharedArray(29) & 7168) >>> 10;
            case 65:
                return (rc.readSharedArray(29) & 112) >>> 4;
            case 66:
                return ((rc.readSharedArray(29) & 1) << 2) + ((rc.readSharedArray(30) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(30) & 1792) >>> 8;
            case 68:
                return (rc.readSharedArray(30) & 28) >>> 2;
            case 69:
                return (rc.readSharedArray(31) & 28672) >>> 12;
            case 70:
                return (rc.readSharedArray(31) & 448) >>> 6;
            case 71:
                return (rc.readSharedArray(31) & 7);
            case 72:
                return (rc.readSharedArray(32) & 7168) >>> 10;
            case 73:
                return (rc.readSharedArray(32) & 112) >>> 4;
            case 74:
                return ((rc.readSharedArray(32) & 1) << 2) + ((rc.readSharedArray(33) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(33) & 1792) >>> 8;
            case 76:
                return (rc.readSharedArray(33) & 28) >>> 2;
            case 77:
                return (rc.readSharedArray(34) & 28672) >>> 12;
            case 78:
                return (rc.readSharedArray(34) & 448) >>> 6;
            case 79:
                return (rc.readSharedArray(34) & 7);
            case 80:
                return (rc.readSharedArray(35) & 7168) >>> 10;
            case 81:
                return (rc.readSharedArray(35) & 112) >>> 4;
            case 82:
                return ((rc.readSharedArray(35) & 1) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(36) & 1792) >>> 8;
            case 84:
                return (rc.readSharedArray(36) & 28) >>> 2;
            case 85:
                return (rc.readSharedArray(37) & 28672) >>> 12;
            case 86:
                return (rc.readSharedArray(37) & 448) >>> 6;
            case 87:
                return (rc.readSharedArray(37) & 7);
            case 88:
                return (rc.readSharedArray(38) & 7168) >>> 10;
            case 89:
                return (rc.readSharedArray(38) & 112) >>> 4;
            case 90:
                return ((rc.readSharedArray(38) & 1) << 2) + ((rc.readSharedArray(39) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(39) & 1792) >>> 8;
            case 92:
                return (rc.readSharedArray(39) & 28) >>> 2;
            case 93:
                return (rc.readSharedArray(40) & 28672) >>> 12;
            case 94:
                return (rc.readSharedArray(40) & 448) >>> 6;
            case 95:
                return (rc.readSharedArray(40) & 7);
            case 96:
                return (rc.readSharedArray(41) & 7168) >>> 10;
            case 97:
                return (rc.readSharedArray(41) & 112) >>> 4;
            case 98:
                return ((rc.readSharedArray(41) & 1) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(42) & 1792) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 58367) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65423) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 63743) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65507) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 36863) | (value << 12));
                break;
            case 6:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65087) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65528) | (value));
                break;
            case 8:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 58367) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65423) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 63743) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65507) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 36863) | (value << 12));
                break;
            case 14:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65087) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65528) | (value));
                break;
            case 16:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 58367) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65423) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 63743) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65507) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 36863) | (value << 12));
                break;
            case 22:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65087) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65528) | (value));
                break;
            case 24:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 58367) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65423) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 63743) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65507) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 36863) | (value << 12));
                break;
            case 30:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65087) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65528) | (value));
                break;
            case 32:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 58367) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65423) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 63743) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65507) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 36863) | (value << 12));
                break;
            case 38:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65087) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65528) | (value));
                break;
            case 40:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 58367) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65423) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 63743) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65507) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 36863) | (value << 12));
                break;
            case 46:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65087) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65528) | (value));
                break;
            case 48:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 58367) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65423) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 63743) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65507) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 36863) | (value << 12));
                break;
            case 54:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65087) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65528) | (value));
                break;
            case 56:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 58367) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65423) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 63743) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65507) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 36863) | (value << 12));
                break;
            case 62:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65087) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65528) | (value));
                break;
            case 64:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 58367) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65423) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 63743) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65507) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 36863) | (value << 12));
                break;
            case 70:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65087) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65528) | (value));
                break;
            case 72:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 58367) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65423) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 63743) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65507) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 36863) | (value << 12));
                break;
            case 78:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65087) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65528) | (value));
                break;
            case 80:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 58367) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65423) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 63743) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65507) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 36863) | (value << 12));
                break;
            case 86:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65087) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65528) | (value));
                break;
            case 88:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 58367) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65423) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 63743) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65507) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 36863) | (value << 12));
                break;
            case 94:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65087) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65528) | (value));
                break;
            case 96:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 58367) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65423) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 63743) | (value << 8));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(5) & 64512) >>> 10;
            case 1:
                return (rc.readSharedArray(5) & 1008) >>> 4;
            case 2:
                return ((rc.readSharedArray(5) & 15) << 2) + ((rc.readSharedArray(6) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(6) & 16128) >>> 8;
            case 4:
                return (rc.readSharedArray(6) & 252) >>> 2;
            case 5:
                return ((rc.readSharedArray(6) & 3) << 4) + ((rc.readSharedArray(7) & 61440) >>> 12);
            case 6:
                return (rc.readSharedArray(7) & 4032) >>> 6;
            case 7:
                return (rc.readSharedArray(7) & 63);
            case 8:
                return (rc.readSharedArray(8) & 64512) >>> 10;
            case 9:
                return (rc.readSharedArray(8) & 1008) >>> 4;
            case 10:
                return ((rc.readSharedArray(8) & 15) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 11:
                return (rc.readSharedArray(9) & 16128) >>> 8;
            case 12:
                return (rc.readSharedArray(9) & 252) >>> 2;
            case 13:
                return ((rc.readSharedArray(9) & 3) << 4) + ((rc.readSharedArray(10) & 61440) >>> 12);
            case 14:
                return (rc.readSharedArray(10) & 4032) >>> 6;
            case 15:
                return (rc.readSharedArray(10) & 63);
            case 16:
                return (rc.readSharedArray(11) & 64512) >>> 10;
            case 17:
                return (rc.readSharedArray(11) & 1008) >>> 4;
            case 18:
                return ((rc.readSharedArray(11) & 15) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 19:
                return (rc.readSharedArray(12) & 16128) >>> 8;
            case 20:
                return (rc.readSharedArray(12) & 252) >>> 2;
            case 21:
                return ((rc.readSharedArray(12) & 3) << 4) + ((rc.readSharedArray(13) & 61440) >>> 12);
            case 22:
                return (rc.readSharedArray(13) & 4032) >>> 6;
            case 23:
                return (rc.readSharedArray(13) & 63);
            case 24:
                return (rc.readSharedArray(14) & 64512) >>> 10;
            case 25:
                return (rc.readSharedArray(14) & 1008) >>> 4;
            case 26:
                return ((rc.readSharedArray(14) & 15) << 2) + ((rc.readSharedArray(15) & 49152) >>> 14);
            case 27:
                return (rc.readSharedArray(15) & 16128) >>> 8;
            case 28:
                return (rc.readSharedArray(15) & 252) >>> 2;
            case 29:
                return ((rc.readSharedArray(15) & 3) << 4) + ((rc.readSharedArray(16) & 61440) >>> 12);
            case 30:
                return (rc.readSharedArray(16) & 4032) >>> 6;
            case 31:
                return (rc.readSharedArray(16) & 63);
            case 32:
                return (rc.readSharedArray(17) & 64512) >>> 10;
            case 33:
                return (rc.readSharedArray(17) & 1008) >>> 4;
            case 34:
                return ((rc.readSharedArray(17) & 15) << 2) + ((rc.readSharedArray(18) & 49152) >>> 14);
            case 35:
                return (rc.readSharedArray(18) & 16128) >>> 8;
            case 36:
                return (rc.readSharedArray(18) & 252) >>> 2;
            case 37:
                return ((rc.readSharedArray(18) & 3) << 4) + ((rc.readSharedArray(19) & 61440) >>> 12);
            case 38:
                return (rc.readSharedArray(19) & 4032) >>> 6;
            case 39:
                return (rc.readSharedArray(19) & 63);
            case 40:
                return (rc.readSharedArray(20) & 64512) >>> 10;
            case 41:
                return (rc.readSharedArray(20) & 1008) >>> 4;
            case 42:
                return ((rc.readSharedArray(20) & 15) << 2) + ((rc.readSharedArray(21) & 49152) >>> 14);
            case 43:
                return (rc.readSharedArray(21) & 16128) >>> 8;
            case 44:
                return (rc.readSharedArray(21) & 252) >>> 2;
            case 45:
                return ((rc.readSharedArray(21) & 3) << 4) + ((rc.readSharedArray(22) & 61440) >>> 12);
            case 46:
                return (rc.readSharedArray(22) & 4032) >>> 6;
            case 47:
                return (rc.readSharedArray(22) & 63);
            case 48:
                return (rc.readSharedArray(23) & 64512) >>> 10;
            case 49:
                return (rc.readSharedArray(23) & 1008) >>> 4;
            case 50:
                return ((rc.readSharedArray(23) & 15) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 51:
                return (rc.readSharedArray(24) & 16128) >>> 8;
            case 52:
                return (rc.readSharedArray(24) & 252) >>> 2;
            case 53:
                return ((rc.readSharedArray(24) & 3) << 4) + ((rc.readSharedArray(25) & 61440) >>> 12);
            case 54:
                return (rc.readSharedArray(25) & 4032) >>> 6;
            case 55:
                return (rc.readSharedArray(25) & 63);
            case 56:
                return (rc.readSharedArray(26) & 64512) >>> 10;
            case 57:
                return (rc.readSharedArray(26) & 1008) >>> 4;
            case 58:
                return ((rc.readSharedArray(26) & 15) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 59:
                return (rc.readSharedArray(27) & 16128) >>> 8;
            case 60:
                return (rc.readSharedArray(27) & 252) >>> 2;
            case 61:
                return ((rc.readSharedArray(27) & 3) << 4) + ((rc.readSharedArray(28) & 61440) >>> 12);
            case 62:
                return (rc.readSharedArray(28) & 4032) >>> 6;
            case 63:
                return (rc.readSharedArray(28) & 63);
            case 64:
                return (rc.readSharedArray(29) & 64512) >>> 10;
            case 65:
                return (rc.readSharedArray(29) & 1008) >>> 4;
            case 66:
                return ((rc.readSharedArray(29) & 15) << 2) + ((rc.readSharedArray(30) & 49152) >>> 14);
            case 67:
                return (rc.readSharedArray(30) & 16128) >>> 8;
            case 68:
                return (rc.readSharedArray(30) & 252) >>> 2;
            case 69:
                return ((rc.readSharedArray(30) & 3) << 4) + ((rc.readSharedArray(31) & 61440) >>> 12);
            case 70:
                return (rc.readSharedArray(31) & 4032) >>> 6;
            case 71:
                return (rc.readSharedArray(31) & 63);
            case 72:
                return (rc.readSharedArray(32) & 64512) >>> 10;
            case 73:
                return (rc.readSharedArray(32) & 1008) >>> 4;
            case 74:
                return ((rc.readSharedArray(32) & 15) << 2) + ((rc.readSharedArray(33) & 49152) >>> 14);
            case 75:
                return (rc.readSharedArray(33) & 16128) >>> 8;
            case 76:
                return (rc.readSharedArray(33) & 252) >>> 2;
            case 77:
                return ((rc.readSharedArray(33) & 3) << 4) + ((rc.readSharedArray(34) & 61440) >>> 12);
            case 78:
                return (rc.readSharedArray(34) & 4032) >>> 6;
            case 79:
                return (rc.readSharedArray(34) & 63);
            case 80:
                return (rc.readSharedArray(35) & 64512) >>> 10;
            case 81:
                return (rc.readSharedArray(35) & 1008) >>> 4;
            case 82:
                return ((rc.readSharedArray(35) & 15) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 83:
                return (rc.readSharedArray(36) & 16128) >>> 8;
            case 84:
                return (rc.readSharedArray(36) & 252) >>> 2;
            case 85:
                return ((rc.readSharedArray(36) & 3) << 4) + ((rc.readSharedArray(37) & 61440) >>> 12);
            case 86:
                return (rc.readSharedArray(37) & 4032) >>> 6;
            case 87:
                return (rc.readSharedArray(37) & 63);
            case 88:
                return (rc.readSharedArray(38) & 64512) >>> 10;
            case 89:
                return (rc.readSharedArray(38) & 1008) >>> 4;
            case 90:
                return ((rc.readSharedArray(38) & 15) << 2) + ((rc.readSharedArray(39) & 49152) >>> 14);
            case 91:
                return (rc.readSharedArray(39) & 16128) >>> 8;
            case 92:
                return (rc.readSharedArray(39) & 252) >>> 2;
            case 93:
                return ((rc.readSharedArray(39) & 3) << 4) + ((rc.readSharedArray(40) & 61440) >>> 12);
            case 94:
                return (rc.readSharedArray(40) & 4032) >>> 6;
            case 95:
                return (rc.readSharedArray(40) & 63);
            case 96:
                return (rc.readSharedArray(41) & 64512) >>> 10;
            case 97:
                return (rc.readSharedArray(41) & 1008) >>> 4;
            case 98:
                return ((rc.readSharedArray(41) & 15) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 99:
                return (rc.readSharedArray(42) & 16128) >>> 8;
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 1023) | (value << 10));
                break;
            case 1:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 64527) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 49407) | (value << 8));
                break;
            case 4:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65283) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 4095) | ((value & 15) << 12));
                break;
            case 6:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 61503) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65472) | (value));
                break;
            case 8:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 1023) | (value << 10));
                break;
            case 9:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64527) | (value << 4));
                break;
            case 10:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 11:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 49407) | (value << 8));
                break;
            case 12:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65283) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 4095) | ((value & 15) << 12));
                break;
            case 14:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 61503) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65472) | (value));
                break;
            case 16:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 1023) | (value << 10));
                break;
            case 17:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 64527) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 19:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 49407) | (value << 8));
                break;
            case 20:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65283) | (value << 2));
                break;
            case 21:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 4095) | ((value & 15) << 12));
                break;
            case 22:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 61503) | (value << 6));
                break;
            case 23:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65472) | (value));
                break;
            case 24:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 1023) | (value << 10));
                break;
            case 25:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 64527) | (value << 4));
                break;
            case 26:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 16383) | ((value & 3) << 14));
                break;
            case 27:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 49407) | (value << 8));
                break;
            case 28:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65283) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 4095) | ((value & 15) << 12));
                break;
            case 30:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 61503) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65472) | (value));
                break;
            case 32:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 1023) | (value << 10));
                break;
            case 33:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 64527) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 16383) | ((value & 3) << 14));
                break;
            case 35:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 49407) | (value << 8));
                break;
            case 36:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65283) | (value << 2));
                break;
            case 37:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 4095) | ((value & 15) << 12));
                break;
            case 38:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 61503) | (value << 6));
                break;
            case 39:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65472) | (value));
                break;
            case 40:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 1023) | (value << 10));
                break;
            case 41:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64527) | (value << 4));
                break;
            case 42:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 16383) | ((value & 3) << 14));
                break;
            case 43:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 49407) | (value << 8));
                break;
            case 44:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65283) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 4095) | ((value & 15) << 12));
                break;
            case 46:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61503) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65472) | (value));
                break;
            case 48:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 1023) | (value << 10));
                break;
            case 49:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64527) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 51:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 49407) | (value << 8));
                break;
            case 52:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65283) | (value << 2));
                break;
            case 53:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 4095) | ((value & 15) << 12));
                break;
            case 54:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 61503) | (value << 6));
                break;
            case 55:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65472) | (value));
                break;
            case 56:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 1023) | (value << 10));
                break;
            case 57:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 64527) | (value << 4));
                break;
            case 58:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 59:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 49407) | (value << 8));
                break;
            case 60:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65283) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 4095) | ((value & 15) << 12));
                break;
            case 62:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 61503) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65472) | (value));
                break;
            case 64:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 1023) | (value << 10));
                break;
            case 65:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 64527) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 16383) | ((value & 3) << 14));
                break;
            case 67:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 49407) | (value << 8));
                break;
            case 68:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65283) | (value << 2));
                break;
            case 69:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 4095) | ((value & 15) << 12));
                break;
            case 70:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 61503) | (value << 6));
                break;
            case 71:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65472) | (value));
                break;
            case 72:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 1023) | (value << 10));
                break;
            case 73:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 64527) | (value << 4));
                break;
            case 74:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 16383) | ((value & 3) << 14));
                break;
            case 75:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 49407) | (value << 8));
                break;
            case 76:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65283) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 4095) | ((value & 15) << 12));
                break;
            case 78:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 61503) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65472) | (value));
                break;
            case 80:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 1023) | (value << 10));
                break;
            case 81:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64527) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 83:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 49407) | (value << 8));
                break;
            case 84:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65283) | (value << 2));
                break;
            case 85:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 4095) | ((value & 15) << 12));
                break;
            case 86:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 61503) | (value << 6));
                break;
            case 87:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65472) | (value));
                break;
            case 88:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 1023) | (value << 10));
                break;
            case 89:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 64527) | (value << 4));
                break;
            case 90:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 16383) | ((value & 3) << 14));
                break;
            case 91:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 49407) | (value << 8));
                break;
            case 92:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65283) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65532) | ((value & 48) >>> 4));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 4095) | ((value & 15) << 12));
                break;
            case 94:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 61503) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65472) | (value));
                break;
            case 96:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 1023) | (value << 10));
                break;
            case 97:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 64527) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65520) | ((value & 60) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 99:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 49407) | (value << 8));
                break;
        }
    }

    public int readCombatClusterPriority(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(42) & 192) >>> 6;
            case 1:
                return (rc.readSharedArray(43) & 24576) >>> 13;
            case 2:
                return (rc.readSharedArray(43) & 48) >>> 4;
            case 3:
                return (rc.readSharedArray(44) & 6144) >>> 11;
            case 4:
                return (rc.readSharedArray(44) & 12) >>> 2;
            case 5:
                return (rc.readSharedArray(45) & 1536) >>> 9;
            case 6:
                return (rc.readSharedArray(45) & 3);
            case 7:
                return (rc.readSharedArray(46) & 384) >>> 7;
            case 8:
                return (rc.readSharedArray(47) & 49152) >>> 14;
            case 9:
                return (rc.readSharedArray(47) & 96) >>> 5;
            default:
                return -1;
        }
    }

    public void writeCombatClusterPriority(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65343) | (value << 6));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 40959) | (value << 13));
                break;
            case 2:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65487) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 59391) | (value << 11));
                break;
            case 4:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65523) | (value << 2));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 63999) | (value << 9));
                break;
            case 6:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | (value));
                break;
            case 7:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65151) | (value << 7));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 16383) | (value << 14));
                break;
            case 9:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65439) | (value << 5));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(42) & 63) << 1) + ((rc.readSharedArray(43) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(43) & 8128) >>> 6;
            case 2:
                return ((rc.readSharedArray(43) & 15) << 3) + ((rc.readSharedArray(44) & 57344) >>> 13);
            case 3:
                return (rc.readSharedArray(44) & 2032) >>> 4;
            case 4:
                return ((rc.readSharedArray(44) & 3) << 5) + ((rc.readSharedArray(45) & 63488) >>> 11);
            case 5:
                return (rc.readSharedArray(45) & 508) >>> 2;
            case 6:
                return (rc.readSharedArray(46) & 65024) >>> 9;
            case 7:
                return (rc.readSharedArray(46) & 127);
            case 8:
                return (rc.readSharedArray(47) & 16256) >>> 7;
            case 9:
                return ((rc.readSharedArray(47) & 31) << 2) + ((rc.readSharedArray(48) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 57407) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65520) | ((value & 120) >>> 3));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 8191) | ((value & 7) << 13));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 63503) | (value << 4));
                break;
            case 4:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 2047) | ((value & 31) << 11));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 511) | (value << 9));
                break;
            case 7:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65408) | (value));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 49279) | (value << 7));
                break;
            case 9:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(42) & 255) << 1) + ((rc.readSharedArray(43) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(43) & 32704) >>> 6;
            case 2:
                return ((rc.readSharedArray(43) & 63) << 3) + ((rc.readSharedArray(44) & 57344) >>> 13);
            case 3:
                return (rc.readSharedArray(44) & 8176) >>> 4;
            case 4:
                return ((rc.readSharedArray(44) & 15) << 5) + ((rc.readSharedArray(45) & 63488) >>> 11);
            case 5:
                return (rc.readSharedArray(45) & 2044) >>> 2;
            case 6:
                return ((rc.readSharedArray(45) & 3) << 7) + ((rc.readSharedArray(46) & 65024) >>> 9);
            case 7:
                return (rc.readSharedArray(46) & 511);
            case 8:
                return (rc.readSharedArray(47) & 65408) >>> 7;
            case 9:
                return ((rc.readSharedArray(47) & 127) << 2) + ((rc.readSharedArray(48) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65280) | ((value & 510) >>> 1));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 32831) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65472) | ((value & 504) >>> 3));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 8191) | ((value & 7) << 13));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 57359) | (value << 4));
                break;
            case 4:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65520) | ((value & 480) >>> 5));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 2047) | ((value & 31) << 11));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 63491) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | ((value & 384) >>> 7));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 511) | ((value & 127) << 9));
                break;
            case 7:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65024) | (value));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 127) | (value << 7));
                break;
            case 9:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65408) | ((value & 508) >>> 2));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(48) & 8192) >>> 13;
            case 1:
                return (rc.readSharedArray(48) & 32) >>> 5;
            case 2:
                return (rc.readSharedArray(49) & 8192) >>> 13;
            case 3:
                return (rc.readSharedArray(49) & 32) >>> 5;
            case 4:
                return (rc.readSharedArray(50) & 8192) >>> 13;
            case 5:
                return (rc.readSharedArray(50) & 32) >>> 5;
            case 6:
                return (rc.readSharedArray(51) & 8192) >>> 13;
            case 7:
                return (rc.readSharedArray(51) & 32) >>> 5;
            case 8:
                return (rc.readSharedArray(52) & 8192) >>> 13;
            case 9:
                return (rc.readSharedArray(52) & 32) >>> 5;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 57343) | (value << 13));
                break;
            case 1:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65503) | (value << 5));
                break;
            case 2:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 57343) | (value << 13));
                break;
            case 3:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65503) | (value << 5));
                break;
            case 4:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 57343) | (value << 13));
                break;
            case 5:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65503) | (value << 5));
                break;
            case 6:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 57343) | (value << 13));
                break;
            case 7:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65503) | (value << 5));
                break;
            case 8:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 57343) | (value << 13));
                break;
            case 9:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65503) | (value << 5));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(48) & 8128) >>> 6;
            case 1:
                return ((rc.readSharedArray(48) & 31) << 2) + ((rc.readSharedArray(49) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(49) & 8128) >>> 6;
            case 3:
                return ((rc.readSharedArray(49) & 31) << 2) + ((rc.readSharedArray(50) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(50) & 8128) >>> 6;
            case 5:
                return ((rc.readSharedArray(50) & 31) << 2) + ((rc.readSharedArray(51) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(51) & 8128) >>> 6;
            case 7:
                return ((rc.readSharedArray(51) & 31) << 2) + ((rc.readSharedArray(52) & 49152) >>> 14);
            case 8:
                return (rc.readSharedArray(52) & 8128) >>> 6;
            case 9:
                return ((rc.readSharedArray(52) & 31) << 2) + ((rc.readSharedArray(53) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 57407) | (value << 6));
                break;
            case 1:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 57407) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 57407) | (value << 6));
                break;
            case 5:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 57407) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 16383) | ((value & 3) << 14));
                break;
            case 8:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 57407) | (value << 6));
                break;
            case 9:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(48) & 16320) >>> 6;
            case 1:
                return ((rc.readSharedArray(48) & 63) << 2) + ((rc.readSharedArray(49) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(49) & 16320) >>> 6;
            case 3:
                return ((rc.readSharedArray(49) & 63) << 2) + ((rc.readSharedArray(50) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(50) & 16320) >>> 6;
            case 5:
                return ((rc.readSharedArray(50) & 63) << 2) + ((rc.readSharedArray(51) & 49152) >>> 14);
            case 6:
                return (rc.readSharedArray(51) & 16320) >>> 6;
            case 7:
                return ((rc.readSharedArray(51) & 63) << 2) + ((rc.readSharedArray(52) & 49152) >>> 14);
            case 8:
                return (rc.readSharedArray(52) & 16320) >>> 6;
            case 9:
                return ((rc.readSharedArray(52) & 63) << 2) + ((rc.readSharedArray(53) & 49152) >>> 14);
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 49215) | (value << 6));
                break;
            case 1:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 49215) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 49215) | (value << 6));
                break;
            case 5:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 16383) | ((value & 3) << 14));
                break;
            case 6:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 49215) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(51, (rc.readSharedArray(51) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 16383) | ((value & 3) << 14));
                break;
            case 8:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 49215) | (value << 6));
                break;
            case 9:
                rc.writeSharedArray(52, (rc.readSharedArray(52) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 16383) | ((value & 3) << 14));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(53) & 14336) >>> 11;
            case 1:
                return (rc.readSharedArray(53) & 14) >>> 1;
            case 2:
                return (rc.readSharedArray(54) & 896) >>> 7;
            case 3:
                return (rc.readSharedArray(55) & 57344) >>> 13;
            case 4:
                return (rc.readSharedArray(55) & 56) >>> 3;
            case 5:
                return (rc.readSharedArray(56) & 3584) >>> 9;
            case 6:
                return ((rc.readSharedArray(56) & 3) << 1) + ((rc.readSharedArray(57) & 32768) >>> 15);
            case 7:
                return (rc.readSharedArray(57) & 224) >>> 5;
            case 8:
                return (rc.readSharedArray(58) & 14336) >>> 11;
            case 9:
                return (rc.readSharedArray(58) & 14) >>> 1;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 51199) | (value << 11));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65521) | (value << 1));
                break;
            case 2:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 64639) | (value << 7));
                break;
            case 3:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 8191) | (value << 13));
                break;
            case 4:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65479) | (value << 3));
                break;
            case 5:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 61951) | (value << 9));
                break;
            case 6:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 32767) | ((value & 1) << 15));
                break;
            case 7:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65311) | (value << 5));
                break;
            case 8:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 51199) | (value << 11));
                break;
            case 9:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65521) | (value << 1));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(53) & 2032) >>> 4;
            case 1:
                return ((rc.readSharedArray(53) & 1) << 6) + ((rc.readSharedArray(54) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(54) & 127);
            case 3:
                return (rc.readSharedArray(55) & 8128) >>> 6;
            case 4:
                return ((rc.readSharedArray(55) & 7) << 4) + ((rc.readSharedArray(56) & 61440) >>> 12);
            case 5:
                return (rc.readSharedArray(56) & 508) >>> 2;
            case 6:
                return (rc.readSharedArray(57) & 32512) >>> 8;
            case 7:
                return ((rc.readSharedArray(57) & 31) << 2) + ((rc.readSharedArray(58) & 49152) >>> 14);
            case 8:
                return (rc.readSharedArray(58) & 2032) >>> 4;
            case 9:
                return ((rc.readSharedArray(58) & 1) << 6) + ((rc.readSharedArray(59) & 64512) >>> 10);
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 63503) | (value << 4));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 65408) | (value));
                break;
            case 3:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 57407) | (value << 6));
                break;
            case 4:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 4095) | ((value & 15) << 12));
                break;
            case 5:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 33023) | (value << 8));
                break;
            case 7:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 16383) | ((value & 3) << 14));
                break;
            case 8:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 63503) | (value << 4));
                break;
            case 9:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 1023) | ((value & 63) << 10));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(53) & 16368) >>> 4;
            case 1:
                return ((rc.readSharedArray(53) & 15) << 6) + ((rc.readSharedArray(54) & 64512) >>> 10);
            case 2:
                return (rc.readSharedArray(54) & 1023);
            case 3:
                return (rc.readSharedArray(55) & 65472) >>> 6;
            case 4:
                return ((rc.readSharedArray(55) & 63) << 4) + ((rc.readSharedArray(56) & 61440) >>> 12);
            case 5:
                return (rc.readSharedArray(56) & 4092) >>> 2;
            case 6:
                return ((rc.readSharedArray(56) & 3) << 8) + ((rc.readSharedArray(57) & 65280) >>> 8);
            case 7:
                return ((rc.readSharedArray(57) & 255) << 2) + ((rc.readSharedArray(58) & 49152) >>> 14);
            case 8:
                return (rc.readSharedArray(58) & 16368) >>> 4;
            case 9:
                return ((rc.readSharedArray(58) & 15) << 6) + ((rc.readSharedArray(59) & 64512) >>> 10);
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 49167) | (value << 4));
                break;
            case 1:
                rc.writeSharedArray(53, (rc.readSharedArray(53) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 1023) | ((value & 63) << 10));
                break;
            case 2:
                rc.writeSharedArray(54, (rc.readSharedArray(54) & 64512) | (value));
                break;
            case 3:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 63) | (value << 6));
                break;
            case 4:
                rc.writeSharedArray(55, (rc.readSharedArray(55) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 4095) | ((value & 15) << 12));
                break;
            case 5:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 61443) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(56, (rc.readSharedArray(56) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 255) | ((value & 255) << 8));
                break;
            case 7:
                rc.writeSharedArray(57, (rc.readSharedArray(57) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 16383) | ((value & 3) << 14));
                break;
            case 8:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 49167) | (value << 4));
                break;
            case 9:
                rc.writeSharedArray(58, (rc.readSharedArray(58) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(59, (rc.readSharedArray(59) & 1023) | ((value & 63) << 10));
                break;
        }
    }

    public int readLastArchonNum() throws GameActionException {
        return (rc.readSharedArray(59) & 768) >>> 8;
    }

    public void writeLastArchonNum(int value) throws GameActionException {
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 64767) | (value << 8));
    }

    public int readLastArchonAll() throws GameActionException {
        return (rc.readSharedArray(59) & 768) >>> 8;
    }

    public void writeLastArchonAll(int value) throws GameActionException {
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 64767) | (value << 8));
    }

    public int readReservedResourcesLead() throws GameActionException {
        return ((rc.readSharedArray(59) & 255) << 2) + ((rc.readSharedArray(60) & 49152) >>> 14);
    }

    public void writeReservedResourcesLead(int value) throws GameActionException {
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 65280) | ((value & 1020) >>> 2));
        rc.writeSharedArray(60, (rc.readSharedArray(60) & 16383) | ((value & 3) << 14));
    }

    public int readReservedResourcesGold() throws GameActionException {
        return (rc.readSharedArray(60) & 16128) >>> 8;
    }

    public void writeReservedResourcesGold(int value) throws GameActionException {
        rc.writeSharedArray(60, (rc.readSharedArray(60) & 49407) | (value << 8));
    }

    public int readReservedResourcesAll() throws GameActionException {
        return ((rc.readSharedArray(59) & 255) << 8) + ((rc.readSharedArray(60) & 65280) >>> 8);
    }

    public void writeReservedResourcesAll(int value) throws GameActionException {
        rc.writeSharedArray(59, (rc.readSharedArray(59) & 65280) | ((value & 65280) >>> 8));
        rc.writeSharedArray(60, (rc.readSharedArray(60) & 255) | ((value & 255) << 8));
    }
}