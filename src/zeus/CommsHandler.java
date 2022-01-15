package zeus;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;
    final int MINER_COUNT_SLOTS = 1;
    final int SOLDIER_COUNT_SLOTS = 1;
    final int LAST_ARCHON_SLOTS = 1;
    final int RESERVED_RESOURCES_SLOTS = 1;

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
        rc.writeSharedArray(35, 4095);
        rc.writeSharedArray(36, 65535);
        rc.writeSharedArray(37, 65535);
        rc.writeSharedArray(38, 65535);
        rc.writeSharedArray(39, 65503);
        rc.writeSharedArray(40, 57311);
        rc.writeSharedArray(41, 57311);
        rc.writeSharedArray(42, 57311);
        rc.writeSharedArray(43, 57311);
        rc.writeSharedArray(44, 57287);
        rc.writeSharedArray(45, 61948);
        rc.writeSharedArray(46, 32543);
        rc.writeSharedArray(47, 51185);
        rc.writeSharedArray(48, 64639);
        rc.writeSharedArray(49, 8135);
        rc.writeSharedArray(50, 61948);
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

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 49152) >>> 14;
            case 1:
                return (rc.readSharedArray(4) & 1536) >>> 9;
            case 2:
                return (rc.readSharedArray(4) & 48) >>> 4;
            case 3:
                return ((rc.readSharedArray(4) & 1) << 1) + ((rc.readSharedArray(5) & 32768) >>> 15);
            case 4:
                return (rc.readSharedArray(5) & 3072) >>> 10;
            case 5:
                return (rc.readSharedArray(5) & 96) >>> 5;
            case 6:
                return (rc.readSharedArray(5) & 3);
            case 7:
                return (rc.readSharedArray(6) & 6144) >>> 11;
            case 8:
                return (rc.readSharedArray(6) & 192) >>> 6;
            case 9:
                return (rc.readSharedArray(6) & 6) >>> 1;
            case 10:
                return (rc.readSharedArray(7) & 12288) >>> 12;
            case 11:
                return (rc.readSharedArray(7) & 384) >>> 7;
            case 12:
                return (rc.readSharedArray(7) & 12) >>> 2;
            case 13:
                return (rc.readSharedArray(8) & 24576) >>> 13;
            case 14:
                return (rc.readSharedArray(8) & 768) >>> 8;
            case 15:
                return (rc.readSharedArray(8) & 24) >>> 3;
            case 16:
                return (rc.readSharedArray(9) & 49152) >>> 14;
            case 17:
                return (rc.readSharedArray(9) & 1536) >>> 9;
            case 18:
                return (rc.readSharedArray(9) & 48) >>> 4;
            case 19:
                return ((rc.readSharedArray(9) & 1) << 1) + ((rc.readSharedArray(10) & 32768) >>> 15);
            case 20:
                return (rc.readSharedArray(10) & 3072) >>> 10;
            case 21:
                return (rc.readSharedArray(10) & 96) >>> 5;
            case 22:
                return (rc.readSharedArray(10) & 3);
            case 23:
                return (rc.readSharedArray(11) & 6144) >>> 11;
            case 24:
                return (rc.readSharedArray(11) & 192) >>> 6;
            case 25:
                return (rc.readSharedArray(11) & 6) >>> 1;
            case 26:
                return (rc.readSharedArray(12) & 12288) >>> 12;
            case 27:
                return (rc.readSharedArray(12) & 384) >>> 7;
            case 28:
                return (rc.readSharedArray(12) & 12) >>> 2;
            case 29:
                return (rc.readSharedArray(13) & 24576) >>> 13;
            case 30:
                return (rc.readSharedArray(13) & 768) >>> 8;
            case 31:
                return (rc.readSharedArray(13) & 24) >>> 3;
            case 32:
                return (rc.readSharedArray(14) & 49152) >>> 14;
            case 33:
                return (rc.readSharedArray(14) & 1536) >>> 9;
            case 34:
                return (rc.readSharedArray(14) & 48) >>> 4;
            case 35:
                return ((rc.readSharedArray(14) & 1) << 1) + ((rc.readSharedArray(15) & 32768) >>> 15);
            case 36:
                return (rc.readSharedArray(15) & 3072) >>> 10;
            case 37:
                return (rc.readSharedArray(15) & 96) >>> 5;
            case 38:
                return (rc.readSharedArray(15) & 3);
            case 39:
                return (rc.readSharedArray(16) & 6144) >>> 11;
            case 40:
                return (rc.readSharedArray(16) & 192) >>> 6;
            case 41:
                return (rc.readSharedArray(16) & 6) >>> 1;
            case 42:
                return (rc.readSharedArray(17) & 12288) >>> 12;
            case 43:
                return (rc.readSharedArray(17) & 384) >>> 7;
            case 44:
                return (rc.readSharedArray(17) & 12) >>> 2;
            case 45:
                return (rc.readSharedArray(18) & 24576) >>> 13;
            case 46:
                return (rc.readSharedArray(18) & 768) >>> 8;
            case 47:
                return (rc.readSharedArray(18) & 24) >>> 3;
            case 48:
                return (rc.readSharedArray(19) & 49152) >>> 14;
            case 49:
                return (rc.readSharedArray(19) & 1536) >>> 9;
            case 50:
                return (rc.readSharedArray(19) & 48) >>> 4;
            case 51:
                return ((rc.readSharedArray(19) & 1) << 1) + ((rc.readSharedArray(20) & 32768) >>> 15);
            case 52:
                return (rc.readSharedArray(20) & 3072) >>> 10;
            case 53:
                return (rc.readSharedArray(20) & 96) >>> 5;
            case 54:
                return (rc.readSharedArray(20) & 3);
            case 55:
                return (rc.readSharedArray(21) & 6144) >>> 11;
            case 56:
                return (rc.readSharedArray(21) & 192) >>> 6;
            case 57:
                return (rc.readSharedArray(21) & 6) >>> 1;
            case 58:
                return (rc.readSharedArray(22) & 12288) >>> 12;
            case 59:
                return (rc.readSharedArray(22) & 384) >>> 7;
            case 60:
                return (rc.readSharedArray(22) & 12) >>> 2;
            case 61:
                return (rc.readSharedArray(23) & 24576) >>> 13;
            case 62:
                return (rc.readSharedArray(23) & 768) >>> 8;
            case 63:
                return (rc.readSharedArray(23) & 24) >>> 3;
            case 64:
                return (rc.readSharedArray(24) & 49152) >>> 14;
            case 65:
                return (rc.readSharedArray(24) & 1536) >>> 9;
            case 66:
                return (rc.readSharedArray(24) & 48) >>> 4;
            case 67:
                return ((rc.readSharedArray(24) & 1) << 1) + ((rc.readSharedArray(25) & 32768) >>> 15);
            case 68:
                return (rc.readSharedArray(25) & 3072) >>> 10;
            case 69:
                return (rc.readSharedArray(25) & 96) >>> 5;
            case 70:
                return (rc.readSharedArray(25) & 3);
            case 71:
                return (rc.readSharedArray(26) & 6144) >>> 11;
            case 72:
                return (rc.readSharedArray(26) & 192) >>> 6;
            case 73:
                return (rc.readSharedArray(26) & 6) >>> 1;
            case 74:
                return (rc.readSharedArray(27) & 12288) >>> 12;
            case 75:
                return (rc.readSharedArray(27) & 384) >>> 7;
            case 76:
                return (rc.readSharedArray(27) & 12) >>> 2;
            case 77:
                return (rc.readSharedArray(28) & 24576) >>> 13;
            case 78:
                return (rc.readSharedArray(28) & 768) >>> 8;
            case 79:
                return (rc.readSharedArray(28) & 24) >>> 3;
            case 80:
                return (rc.readSharedArray(29) & 49152) >>> 14;
            case 81:
                return (rc.readSharedArray(29) & 1536) >>> 9;
            case 82:
                return (rc.readSharedArray(29) & 48) >>> 4;
            case 83:
                return ((rc.readSharedArray(29) & 1) << 1) + ((rc.readSharedArray(30) & 32768) >>> 15);
            case 84:
                return (rc.readSharedArray(30) & 3072) >>> 10;
            case 85:
                return (rc.readSharedArray(30) & 96) >>> 5;
            case 86:
                return (rc.readSharedArray(30) & 3);
            case 87:
                return (rc.readSharedArray(31) & 6144) >>> 11;
            case 88:
                return (rc.readSharedArray(31) & 192) >>> 6;
            case 89:
                return (rc.readSharedArray(31) & 6) >>> 1;
            case 90:
                return (rc.readSharedArray(32) & 12288) >>> 12;
            case 91:
                return (rc.readSharedArray(32) & 384) >>> 7;
            case 92:
                return (rc.readSharedArray(32) & 12) >>> 2;
            case 93:
                return (rc.readSharedArray(33) & 24576) >>> 13;
            case 94:
                return (rc.readSharedArray(33) & 768) >>> 8;
            case 95:
                return (rc.readSharedArray(33) & 24) >>> 3;
            case 96:
                return (rc.readSharedArray(34) & 49152) >>> 14;
            case 97:
                return (rc.readSharedArray(34) & 1536) >>> 9;
            case 98:
                return (rc.readSharedArray(34) & 48) >>> 4;
            case 99:
                return ((rc.readSharedArray(34) & 1) << 1) + ((rc.readSharedArray(35) & 32768) >>> 15);
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 16383) | (value << 14));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 63999) | (value << 9));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65487) | (value << 4));
                break;
            case 3:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 32767) | ((value & 1) << 15));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 62463) | (value << 10));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65439) | (value << 5));
                break;
            case 6:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65532) | (value));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 59391) | (value << 11));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65343) | (value << 6));
                break;
            case 9:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65529) | (value << 1));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 53247) | (value << 12));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65151) | (value << 7));
                break;
            case 12:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65523) | (value << 2));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 40959) | (value << 13));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64767) | (value << 8));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65511) | (value << 3));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | (value << 14));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 63999) | (value << 9));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65487) | (value << 4));
                break;
            case 19:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 32767) | ((value & 1) << 15));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 62463) | (value << 10));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65439) | (value << 5));
                break;
            case 22:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65532) | (value));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 59391) | (value << 11));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65343) | (value << 6));
                break;
            case 25:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65529) | (value << 1));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 53247) | (value << 12));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65151) | (value << 7));
                break;
            case 28:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65523) | (value << 2));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 40959) | (value << 13));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 64767) | (value << 8));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65511) | (value << 3));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 16383) | (value << 14));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 63999) | (value << 9));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65487) | (value << 4));
                break;
            case 35:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 32767) | ((value & 1) << 15));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 62463) | (value << 10));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65439) | (value << 5));
                break;
            case 38:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | (value));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 59391) | (value << 11));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65343) | (value << 6));
                break;
            case 41:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65529) | (value << 1));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 53247) | (value << 12));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65151) | (value << 7));
                break;
            case 44:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65523) | (value << 2));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 40959) | (value << 13));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 64767) | (value << 8));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65511) | (value << 3));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 16383) | (value << 14));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 63999) | (value << 9));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65487) | (value << 4));
                break;
            case 51:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 32767) | ((value & 1) << 15));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 62463) | (value << 10));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65439) | (value << 5));
                break;
            case 54:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65532) | (value));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 59391) | (value << 11));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65343) | (value << 6));
                break;
            case 57:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65529) | (value << 1));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 53247) | (value << 12));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65151) | (value << 7));
                break;
            case 60:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65523) | (value << 2));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 40959) | (value << 13));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64767) | (value << 8));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65511) | (value << 3));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | (value << 14));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 63999) | (value << 9));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65487) | (value << 4));
                break;
            case 67:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 32767) | ((value & 1) << 15));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 62463) | (value << 10));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65439) | (value << 5));
                break;
            case 70:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65532) | (value));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 59391) | (value << 11));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65343) | (value << 6));
                break;
            case 73:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65529) | (value << 1));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 53247) | (value << 12));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65151) | (value << 7));
                break;
            case 76:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65523) | (value << 2));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 40959) | (value << 13));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 64767) | (value << 8));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65511) | (value << 3));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 16383) | (value << 14));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 63999) | (value << 9));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65487) | (value << 4));
                break;
            case 83:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 32767) | ((value & 1) << 15));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 62463) | (value << 10));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65439) | (value << 5));
                break;
            case 86:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | (value));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 59391) | (value << 11));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65343) | (value << 6));
                break;
            case 89:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65529) | (value << 1));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 53247) | (value << 12));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65151) | (value << 7));
                break;
            case 92:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65523) | (value << 2));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 40959) | (value << 13));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 64767) | (value << 8));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65511) | (value << 3));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 16383) | (value << 14));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 63999) | (value << 9));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65487) | (value << 4));
                break;
            case 99:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 32767) | ((value & 1) << 15));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 14336) >>> 11;
            case 1:
                return (rc.readSharedArray(4) & 448) >>> 6;
            case 2:
                return (rc.readSharedArray(4) & 14) >>> 1;
            case 3:
                return (rc.readSharedArray(5) & 28672) >>> 12;
            case 4:
                return (rc.readSharedArray(5) & 896) >>> 7;
            case 5:
                return (rc.readSharedArray(5) & 28) >>> 2;
            case 6:
                return (rc.readSharedArray(6) & 57344) >>> 13;
            case 7:
                return (rc.readSharedArray(6) & 1792) >>> 8;
            case 8:
                return (rc.readSharedArray(6) & 56) >>> 3;
            case 9:
                return ((rc.readSharedArray(6) & 1) << 2) + ((rc.readSharedArray(7) & 49152) >>> 14);
            case 10:
                return (rc.readSharedArray(7) & 3584) >>> 9;
            case 11:
                return (rc.readSharedArray(7) & 112) >>> 4;
            case 12:
                return ((rc.readSharedArray(7) & 3) << 1) + ((rc.readSharedArray(8) & 32768) >>> 15);
            case 13:
                return (rc.readSharedArray(8) & 7168) >>> 10;
            case 14:
                return (rc.readSharedArray(8) & 224) >>> 5;
            case 15:
                return (rc.readSharedArray(8) & 7);
            case 16:
                return (rc.readSharedArray(9) & 14336) >>> 11;
            case 17:
                return (rc.readSharedArray(9) & 448) >>> 6;
            case 18:
                return (rc.readSharedArray(9) & 14) >>> 1;
            case 19:
                return (rc.readSharedArray(10) & 28672) >>> 12;
            case 20:
                return (rc.readSharedArray(10) & 896) >>> 7;
            case 21:
                return (rc.readSharedArray(10) & 28) >>> 2;
            case 22:
                return (rc.readSharedArray(11) & 57344) >>> 13;
            case 23:
                return (rc.readSharedArray(11) & 1792) >>> 8;
            case 24:
                return (rc.readSharedArray(11) & 56) >>> 3;
            case 25:
                return ((rc.readSharedArray(11) & 1) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 26:
                return (rc.readSharedArray(12) & 3584) >>> 9;
            case 27:
                return (rc.readSharedArray(12) & 112) >>> 4;
            case 28:
                return ((rc.readSharedArray(12) & 3) << 1) + ((rc.readSharedArray(13) & 32768) >>> 15);
            case 29:
                return (rc.readSharedArray(13) & 7168) >>> 10;
            case 30:
                return (rc.readSharedArray(13) & 224) >>> 5;
            case 31:
                return (rc.readSharedArray(13) & 7);
            case 32:
                return (rc.readSharedArray(14) & 14336) >>> 11;
            case 33:
                return (rc.readSharedArray(14) & 448) >>> 6;
            case 34:
                return (rc.readSharedArray(14) & 14) >>> 1;
            case 35:
                return (rc.readSharedArray(15) & 28672) >>> 12;
            case 36:
                return (rc.readSharedArray(15) & 896) >>> 7;
            case 37:
                return (rc.readSharedArray(15) & 28) >>> 2;
            case 38:
                return (rc.readSharedArray(16) & 57344) >>> 13;
            case 39:
                return (rc.readSharedArray(16) & 1792) >>> 8;
            case 40:
                return (rc.readSharedArray(16) & 56) >>> 3;
            case 41:
                return ((rc.readSharedArray(16) & 1) << 2) + ((rc.readSharedArray(17) & 49152) >>> 14);
            case 42:
                return (rc.readSharedArray(17) & 3584) >>> 9;
            case 43:
                return (rc.readSharedArray(17) & 112) >>> 4;
            case 44:
                return ((rc.readSharedArray(17) & 3) << 1) + ((rc.readSharedArray(18) & 32768) >>> 15);
            case 45:
                return (rc.readSharedArray(18) & 7168) >>> 10;
            case 46:
                return (rc.readSharedArray(18) & 224) >>> 5;
            case 47:
                return (rc.readSharedArray(18) & 7);
            case 48:
                return (rc.readSharedArray(19) & 14336) >>> 11;
            case 49:
                return (rc.readSharedArray(19) & 448) >>> 6;
            case 50:
                return (rc.readSharedArray(19) & 14) >>> 1;
            case 51:
                return (rc.readSharedArray(20) & 28672) >>> 12;
            case 52:
                return (rc.readSharedArray(20) & 896) >>> 7;
            case 53:
                return (rc.readSharedArray(20) & 28) >>> 2;
            case 54:
                return (rc.readSharedArray(21) & 57344) >>> 13;
            case 55:
                return (rc.readSharedArray(21) & 1792) >>> 8;
            case 56:
                return (rc.readSharedArray(21) & 56) >>> 3;
            case 57:
                return ((rc.readSharedArray(21) & 1) << 2) + ((rc.readSharedArray(22) & 49152) >>> 14);
            case 58:
                return (rc.readSharedArray(22) & 3584) >>> 9;
            case 59:
                return (rc.readSharedArray(22) & 112) >>> 4;
            case 60:
                return ((rc.readSharedArray(22) & 3) << 1) + ((rc.readSharedArray(23) & 32768) >>> 15);
            case 61:
                return (rc.readSharedArray(23) & 7168) >>> 10;
            case 62:
                return (rc.readSharedArray(23) & 224) >>> 5;
            case 63:
                return (rc.readSharedArray(23) & 7);
            case 64:
                return (rc.readSharedArray(24) & 14336) >>> 11;
            case 65:
                return (rc.readSharedArray(24) & 448) >>> 6;
            case 66:
                return (rc.readSharedArray(24) & 14) >>> 1;
            case 67:
                return (rc.readSharedArray(25) & 28672) >>> 12;
            case 68:
                return (rc.readSharedArray(25) & 896) >>> 7;
            case 69:
                return (rc.readSharedArray(25) & 28) >>> 2;
            case 70:
                return (rc.readSharedArray(26) & 57344) >>> 13;
            case 71:
                return (rc.readSharedArray(26) & 1792) >>> 8;
            case 72:
                return (rc.readSharedArray(26) & 56) >>> 3;
            case 73:
                return ((rc.readSharedArray(26) & 1) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 74:
                return (rc.readSharedArray(27) & 3584) >>> 9;
            case 75:
                return (rc.readSharedArray(27) & 112) >>> 4;
            case 76:
                return ((rc.readSharedArray(27) & 3) << 1) + ((rc.readSharedArray(28) & 32768) >>> 15);
            case 77:
                return (rc.readSharedArray(28) & 7168) >>> 10;
            case 78:
                return (rc.readSharedArray(28) & 224) >>> 5;
            case 79:
                return (rc.readSharedArray(28) & 7);
            case 80:
                return (rc.readSharedArray(29) & 14336) >>> 11;
            case 81:
                return (rc.readSharedArray(29) & 448) >>> 6;
            case 82:
                return (rc.readSharedArray(29) & 14) >>> 1;
            case 83:
                return (rc.readSharedArray(30) & 28672) >>> 12;
            case 84:
                return (rc.readSharedArray(30) & 896) >>> 7;
            case 85:
                return (rc.readSharedArray(30) & 28) >>> 2;
            case 86:
                return (rc.readSharedArray(31) & 57344) >>> 13;
            case 87:
                return (rc.readSharedArray(31) & 1792) >>> 8;
            case 88:
                return (rc.readSharedArray(31) & 56) >>> 3;
            case 89:
                return ((rc.readSharedArray(31) & 1) << 2) + ((rc.readSharedArray(32) & 49152) >>> 14);
            case 90:
                return (rc.readSharedArray(32) & 3584) >>> 9;
            case 91:
                return (rc.readSharedArray(32) & 112) >>> 4;
            case 92:
                return ((rc.readSharedArray(32) & 3) << 1) + ((rc.readSharedArray(33) & 32768) >>> 15);
            case 93:
                return (rc.readSharedArray(33) & 7168) >>> 10;
            case 94:
                return (rc.readSharedArray(33) & 224) >>> 5;
            case 95:
                return (rc.readSharedArray(33) & 7);
            case 96:
                return (rc.readSharedArray(34) & 14336) >>> 11;
            case 97:
                return (rc.readSharedArray(34) & 448) >>> 6;
            case 98:
                return (rc.readSharedArray(34) & 14) >>> 1;
            case 99:
                return (rc.readSharedArray(35) & 28672) >>> 12;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 51199) | (value << 11));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65087) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65521) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 36863) | (value << 12));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 64639) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65507) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 8191) | (value << 13));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 63743) | (value << 8));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65479) | (value << 3));
                break;
            case 9:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 16383) | ((value & 3) << 14));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 61951) | (value << 9));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65423) | (value << 4));
                break;
            case 12:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 32767) | ((value & 1) << 15));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 58367) | (value << 10));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65311) | (value << 5));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65528) | (value));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 51199) | (value << 11));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65087) | (value << 6));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65521) | (value << 1));
                break;
            case 19:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 36863) | (value << 12));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 64639) | (value << 7));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65507) | (value << 2));
                break;
            case 22:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 8191) | (value << 13));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 63743) | (value << 8));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65479) | (value << 3));
                break;
            case 25:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 61951) | (value << 9));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65423) | (value << 4));
                break;
            case 28:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 32767) | ((value & 1) << 15));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 58367) | (value << 10));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65311) | (value << 5));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65528) | (value));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 51199) | (value << 11));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65087) | (value << 6));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65521) | (value << 1));
                break;
            case 35:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 36863) | (value << 12));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 64639) | (value << 7));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65507) | (value << 2));
                break;
            case 38:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 8191) | (value << 13));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 63743) | (value << 8));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65479) | (value << 3));
                break;
            case 41:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 16383) | ((value & 3) << 14));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 61951) | (value << 9));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65423) | (value << 4));
                break;
            case 44:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 32767) | ((value & 1) << 15));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 58367) | (value << 10));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65311) | (value << 5));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65528) | (value));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 51199) | (value << 11));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65087) | (value << 6));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65521) | (value << 1));
                break;
            case 51:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 36863) | (value << 12));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64639) | (value << 7));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65507) | (value << 2));
                break;
            case 54:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 8191) | (value << 13));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 63743) | (value << 8));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65479) | (value << 3));
                break;
            case 57:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 16383) | ((value & 3) << 14));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61951) | (value << 9));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65423) | (value << 4));
                break;
            case 60:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 32767) | ((value & 1) << 15));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 58367) | (value << 10));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65311) | (value << 5));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65528) | (value));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 51199) | (value << 11));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65087) | (value << 6));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65521) | (value << 1));
                break;
            case 67:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 36863) | (value << 12));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 64639) | (value << 7));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65507) | (value << 2));
                break;
            case 70:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 8191) | (value << 13));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 63743) | (value << 8));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65479) | (value << 3));
                break;
            case 73:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 61951) | (value << 9));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65423) | (value << 4));
                break;
            case 76:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 32767) | ((value & 1) << 15));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 58367) | (value << 10));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65311) | (value << 5));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65528) | (value));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 51199) | (value << 11));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65087) | (value << 6));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65521) | (value << 1));
                break;
            case 83:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 36863) | (value << 12));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 64639) | (value << 7));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65507) | (value << 2));
                break;
            case 86:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 8191) | (value << 13));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 63743) | (value << 8));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65479) | (value << 3));
                break;
            case 89:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 16383) | ((value & 3) << 14));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 61951) | (value << 9));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65423) | (value << 4));
                break;
            case 92:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 32767) | ((value & 1) << 15));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 58367) | (value << 10));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65311) | (value << 5));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65528) | (value));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 51199) | (value << 11));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65087) | (value << 6));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65521) | (value << 1));
                break;
            case 99:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 36863) | (value << 12));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 63488) >>> 11;
            case 1:
                return (rc.readSharedArray(4) & 1984) >>> 6;
            case 2:
                return (rc.readSharedArray(4) & 62) >>> 1;
            case 3:
                return ((rc.readSharedArray(4) & 1) << 4) + ((rc.readSharedArray(5) & 61440) >>> 12);
            case 4:
                return (rc.readSharedArray(5) & 3968) >>> 7;
            case 5:
                return (rc.readSharedArray(5) & 124) >>> 2;
            case 6:
                return ((rc.readSharedArray(5) & 3) << 3) + ((rc.readSharedArray(6) & 57344) >>> 13);
            case 7:
                return (rc.readSharedArray(6) & 7936) >>> 8;
            case 8:
                return (rc.readSharedArray(6) & 248) >>> 3;
            case 9:
                return ((rc.readSharedArray(6) & 7) << 2) + ((rc.readSharedArray(7) & 49152) >>> 14);
            case 10:
                return (rc.readSharedArray(7) & 15872) >>> 9;
            case 11:
                return (rc.readSharedArray(7) & 496) >>> 4;
            case 12:
                return ((rc.readSharedArray(7) & 15) << 1) + ((rc.readSharedArray(8) & 32768) >>> 15);
            case 13:
                return (rc.readSharedArray(8) & 31744) >>> 10;
            case 14:
                return (rc.readSharedArray(8) & 992) >>> 5;
            case 15:
                return (rc.readSharedArray(8) & 31);
            case 16:
                return (rc.readSharedArray(9) & 63488) >>> 11;
            case 17:
                return (rc.readSharedArray(9) & 1984) >>> 6;
            case 18:
                return (rc.readSharedArray(9) & 62) >>> 1;
            case 19:
                return ((rc.readSharedArray(9) & 1) << 4) + ((rc.readSharedArray(10) & 61440) >>> 12);
            case 20:
                return (rc.readSharedArray(10) & 3968) >>> 7;
            case 21:
                return (rc.readSharedArray(10) & 124) >>> 2;
            case 22:
                return ((rc.readSharedArray(10) & 3) << 3) + ((rc.readSharedArray(11) & 57344) >>> 13);
            case 23:
                return (rc.readSharedArray(11) & 7936) >>> 8;
            case 24:
                return (rc.readSharedArray(11) & 248) >>> 3;
            case 25:
                return ((rc.readSharedArray(11) & 7) << 2) + ((rc.readSharedArray(12) & 49152) >>> 14);
            case 26:
                return (rc.readSharedArray(12) & 15872) >>> 9;
            case 27:
                return (rc.readSharedArray(12) & 496) >>> 4;
            case 28:
                return ((rc.readSharedArray(12) & 15) << 1) + ((rc.readSharedArray(13) & 32768) >>> 15);
            case 29:
                return (rc.readSharedArray(13) & 31744) >>> 10;
            case 30:
                return (rc.readSharedArray(13) & 992) >>> 5;
            case 31:
                return (rc.readSharedArray(13) & 31);
            case 32:
                return (rc.readSharedArray(14) & 63488) >>> 11;
            case 33:
                return (rc.readSharedArray(14) & 1984) >>> 6;
            case 34:
                return (rc.readSharedArray(14) & 62) >>> 1;
            case 35:
                return ((rc.readSharedArray(14) & 1) << 4) + ((rc.readSharedArray(15) & 61440) >>> 12);
            case 36:
                return (rc.readSharedArray(15) & 3968) >>> 7;
            case 37:
                return (rc.readSharedArray(15) & 124) >>> 2;
            case 38:
                return ((rc.readSharedArray(15) & 3) << 3) + ((rc.readSharedArray(16) & 57344) >>> 13);
            case 39:
                return (rc.readSharedArray(16) & 7936) >>> 8;
            case 40:
                return (rc.readSharedArray(16) & 248) >>> 3;
            case 41:
                return ((rc.readSharedArray(16) & 7) << 2) + ((rc.readSharedArray(17) & 49152) >>> 14);
            case 42:
                return (rc.readSharedArray(17) & 15872) >>> 9;
            case 43:
                return (rc.readSharedArray(17) & 496) >>> 4;
            case 44:
                return ((rc.readSharedArray(17) & 15) << 1) + ((rc.readSharedArray(18) & 32768) >>> 15);
            case 45:
                return (rc.readSharedArray(18) & 31744) >>> 10;
            case 46:
                return (rc.readSharedArray(18) & 992) >>> 5;
            case 47:
                return (rc.readSharedArray(18) & 31);
            case 48:
                return (rc.readSharedArray(19) & 63488) >>> 11;
            case 49:
                return (rc.readSharedArray(19) & 1984) >>> 6;
            case 50:
                return (rc.readSharedArray(19) & 62) >>> 1;
            case 51:
                return ((rc.readSharedArray(19) & 1) << 4) + ((rc.readSharedArray(20) & 61440) >>> 12);
            case 52:
                return (rc.readSharedArray(20) & 3968) >>> 7;
            case 53:
                return (rc.readSharedArray(20) & 124) >>> 2;
            case 54:
                return ((rc.readSharedArray(20) & 3) << 3) + ((rc.readSharedArray(21) & 57344) >>> 13);
            case 55:
                return (rc.readSharedArray(21) & 7936) >>> 8;
            case 56:
                return (rc.readSharedArray(21) & 248) >>> 3;
            case 57:
                return ((rc.readSharedArray(21) & 7) << 2) + ((rc.readSharedArray(22) & 49152) >>> 14);
            case 58:
                return (rc.readSharedArray(22) & 15872) >>> 9;
            case 59:
                return (rc.readSharedArray(22) & 496) >>> 4;
            case 60:
                return ((rc.readSharedArray(22) & 15) << 1) + ((rc.readSharedArray(23) & 32768) >>> 15);
            case 61:
                return (rc.readSharedArray(23) & 31744) >>> 10;
            case 62:
                return (rc.readSharedArray(23) & 992) >>> 5;
            case 63:
                return (rc.readSharedArray(23) & 31);
            case 64:
                return (rc.readSharedArray(24) & 63488) >>> 11;
            case 65:
                return (rc.readSharedArray(24) & 1984) >>> 6;
            case 66:
                return (rc.readSharedArray(24) & 62) >>> 1;
            case 67:
                return ((rc.readSharedArray(24) & 1) << 4) + ((rc.readSharedArray(25) & 61440) >>> 12);
            case 68:
                return (rc.readSharedArray(25) & 3968) >>> 7;
            case 69:
                return (rc.readSharedArray(25) & 124) >>> 2;
            case 70:
                return ((rc.readSharedArray(25) & 3) << 3) + ((rc.readSharedArray(26) & 57344) >>> 13);
            case 71:
                return (rc.readSharedArray(26) & 7936) >>> 8;
            case 72:
                return (rc.readSharedArray(26) & 248) >>> 3;
            case 73:
                return ((rc.readSharedArray(26) & 7) << 2) + ((rc.readSharedArray(27) & 49152) >>> 14);
            case 74:
                return (rc.readSharedArray(27) & 15872) >>> 9;
            case 75:
                return (rc.readSharedArray(27) & 496) >>> 4;
            case 76:
                return ((rc.readSharedArray(27) & 15) << 1) + ((rc.readSharedArray(28) & 32768) >>> 15);
            case 77:
                return (rc.readSharedArray(28) & 31744) >>> 10;
            case 78:
                return (rc.readSharedArray(28) & 992) >>> 5;
            case 79:
                return (rc.readSharedArray(28) & 31);
            case 80:
                return (rc.readSharedArray(29) & 63488) >>> 11;
            case 81:
                return (rc.readSharedArray(29) & 1984) >>> 6;
            case 82:
                return (rc.readSharedArray(29) & 62) >>> 1;
            case 83:
                return ((rc.readSharedArray(29) & 1) << 4) + ((rc.readSharedArray(30) & 61440) >>> 12);
            case 84:
                return (rc.readSharedArray(30) & 3968) >>> 7;
            case 85:
                return (rc.readSharedArray(30) & 124) >>> 2;
            case 86:
                return ((rc.readSharedArray(30) & 3) << 3) + ((rc.readSharedArray(31) & 57344) >>> 13);
            case 87:
                return (rc.readSharedArray(31) & 7936) >>> 8;
            case 88:
                return (rc.readSharedArray(31) & 248) >>> 3;
            case 89:
                return ((rc.readSharedArray(31) & 7) << 2) + ((rc.readSharedArray(32) & 49152) >>> 14);
            case 90:
                return (rc.readSharedArray(32) & 15872) >>> 9;
            case 91:
                return (rc.readSharedArray(32) & 496) >>> 4;
            case 92:
                return ((rc.readSharedArray(32) & 15) << 1) + ((rc.readSharedArray(33) & 32768) >>> 15);
            case 93:
                return (rc.readSharedArray(33) & 31744) >>> 10;
            case 94:
                return (rc.readSharedArray(33) & 992) >>> 5;
            case 95:
                return (rc.readSharedArray(33) & 31);
            case 96:
                return (rc.readSharedArray(34) & 63488) >>> 11;
            case 97:
                return (rc.readSharedArray(34) & 1984) >>> 6;
            case 98:
                return (rc.readSharedArray(34) & 62) >>> 1;
            case 99:
                return ((rc.readSharedArray(34) & 1) << 4) + ((rc.readSharedArray(35) & 61440) >>> 12);
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 2047) | (value << 11));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 63551) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65473) | (value << 1));
                break;
            case 3:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 4095) | ((value & 15) << 12));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 61567) | (value << 7));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65411) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 8191) | ((value & 7) << 13));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 57599) | (value << 8));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65287) | (value << 3));
                break;
            case 9:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 16383) | ((value & 3) << 14));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 49663) | (value << 9));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65039) | (value << 4));
                break;
            case 12:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 32767) | ((value & 1) << 15));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 33791) | (value << 10));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 64543) | (value << 5));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65504) | (value));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 2047) | (value << 11));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 63551) | (value << 6));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65473) | (value << 1));
                break;
            case 19:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 4095) | ((value & 15) << 12));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 61567) | (value << 7));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65411) | (value << 2));
                break;
            case 22:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 8191) | ((value & 7) << 13));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 57599) | (value << 8));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65287) | (value << 3));
                break;
            case 25:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 16383) | ((value & 3) << 14));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 49663) | (value << 9));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65039) | (value << 4));
                break;
            case 28:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 32767) | ((value & 1) << 15));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 33791) | (value << 10));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 64543) | (value << 5));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65504) | (value));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 2047) | (value << 11));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 63551) | (value << 6));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65473) | (value << 1));
                break;
            case 35:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 4095) | ((value & 15) << 12));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 61567) | (value << 7));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65411) | (value << 2));
                break;
            case 38:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 8191) | ((value & 7) << 13));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 57599) | (value << 8));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65287) | (value << 3));
                break;
            case 41:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 16383) | ((value & 3) << 14));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 49663) | (value << 9));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65039) | (value << 4));
                break;
            case 44:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 32767) | ((value & 1) << 15));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 33791) | (value << 10));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 64543) | (value << 5));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65504) | (value));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 2047) | (value << 11));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 63551) | (value << 6));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65473) | (value << 1));
                break;
            case 51:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 4095) | ((value & 15) << 12));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 61567) | (value << 7));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65411) | (value << 2));
                break;
            case 54:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 8191) | ((value & 7) << 13));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 57599) | (value << 8));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65287) | (value << 3));
                break;
            case 57:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 16383) | ((value & 3) << 14));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 49663) | (value << 9));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65039) | (value << 4));
                break;
            case 60:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 32767) | ((value & 1) << 15));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 33791) | (value << 10));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 64543) | (value << 5));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65504) | (value));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 2047) | (value << 11));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 63551) | (value << 6));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65473) | (value << 1));
                break;
            case 67:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 4095) | ((value & 15) << 12));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 61567) | (value << 7));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65411) | (value << 2));
                break;
            case 70:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 8191) | ((value & 7) << 13));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 57599) | (value << 8));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65287) | (value << 3));
                break;
            case 73:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 16383) | ((value & 3) << 14));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 49663) | (value << 9));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65039) | (value << 4));
                break;
            case 76:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 32767) | ((value & 1) << 15));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 33791) | (value << 10));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 64543) | (value << 5));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65504) | (value));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 2047) | (value << 11));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 63551) | (value << 6));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65473) | (value << 1));
                break;
            case 83:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 4095) | ((value & 15) << 12));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 61567) | (value << 7));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65411) | (value << 2));
                break;
            case 86:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 8191) | ((value & 7) << 13));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 57599) | (value << 8));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65287) | (value << 3));
                break;
            case 89:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 16383) | ((value & 3) << 14));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 49663) | (value << 9));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65039) | (value << 4));
                break;
            case 92:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 32767) | ((value & 1) << 15));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 33791) | (value << 10));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 64543) | (value << 5));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65504) | (value));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 2047) | (value << 11));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 63551) | (value << 6));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65473) | (value << 1));
                break;
            case 99:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 4095) | ((value & 15) << 12));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(35) & 4064) >>> 5;
            case 1:
                return ((rc.readSharedArray(35) & 31) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(36) & 16256) >>> 7;
            case 3:
                return (rc.readSharedArray(36) & 127);
            case 4:
                return (rc.readSharedArray(37) & 65024) >>> 9;
            case 5:
                return (rc.readSharedArray(37) & 508) >>> 2;
            case 6:
                return ((rc.readSharedArray(37) & 3) << 5) + ((rc.readSharedArray(38) & 63488) >>> 11);
            case 7:
                return (rc.readSharedArray(38) & 2032) >>> 4;
            case 8:
                return ((rc.readSharedArray(38) & 15) << 3) + ((rc.readSharedArray(39) & 57344) >>> 13);
            case 9:
                return (rc.readSharedArray(39) & 8128) >>> 6;
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 61471) | (value << 5));
                break;
            case 1:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 49279) | (value << 7));
                break;
            case 3:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65408) | (value));
                break;
            case 4:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 511) | (value << 9));
                break;
            case 5:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 2047) | ((value & 31) << 11));
                break;
            case 7:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 63503) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65520) | ((value & 120) >>> 3));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 8191) | ((value & 7) << 13));
                break;
            case 9:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 57407) | (value << 6));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(35) & 4064) >>> 5;
            case 1:
                return ((rc.readSharedArray(35) & 31) << 2) + ((rc.readSharedArray(36) & 49152) >>> 14);
            case 2:
                return (rc.readSharedArray(36) & 16256) >>> 7;
            case 3:
                return (rc.readSharedArray(36) & 127);
            case 4:
                return (rc.readSharedArray(37) & 65024) >>> 9;
            case 5:
                return (rc.readSharedArray(37) & 508) >>> 2;
            case 6:
                return ((rc.readSharedArray(37) & 3) << 5) + ((rc.readSharedArray(38) & 63488) >>> 11);
            case 7:
                return (rc.readSharedArray(38) & 2032) >>> 4;
            case 8:
                return ((rc.readSharedArray(38) & 15) << 3) + ((rc.readSharedArray(39) & 57344) >>> 13);
            case 9:
                return (rc.readSharedArray(39) & 8128) >>> 6;
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 61471) | (value << 5));
                break;
            case 1:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 16383) | ((value & 3) << 14));
                break;
            case 2:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 49279) | (value << 7));
                break;
            case 3:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65408) | (value));
                break;
            case 4:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 511) | (value << 9));
                break;
            case 5:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65027) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 2047) | ((value & 31) << 11));
                break;
            case 7:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 63503) | (value << 4));
                break;
            case 8:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65520) | ((value & 120) >>> 3));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 8191) | ((value & 7) << 13));
                break;
            case 9:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 57407) | (value << 6));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(39) & 32) >>> 5;
            case 1:
                return (rc.readSharedArray(40) & 8192) >>> 13;
            case 2:
                return (rc.readSharedArray(40) & 32) >>> 5;
            case 3:
                return (rc.readSharedArray(41) & 8192) >>> 13;
            case 4:
                return (rc.readSharedArray(41) & 32) >>> 5;
            case 5:
                return (rc.readSharedArray(42) & 8192) >>> 13;
            case 6:
                return (rc.readSharedArray(42) & 32) >>> 5;
            case 7:
                return (rc.readSharedArray(43) & 8192) >>> 13;
            case 8:
                return (rc.readSharedArray(43) & 32) >>> 5;
            case 9:
                return (rc.readSharedArray(44) & 8192) >>> 13;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65503) | (value << 5));
                break;
            case 1:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 57343) | (value << 13));
                break;
            case 2:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65503) | (value << 5));
                break;
            case 3:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 57343) | (value << 13));
                break;
            case 4:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65503) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 57343) | (value << 13));
                break;
            case 6:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65503) | (value << 5));
                break;
            case 7:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 57343) | (value << 13));
                break;
            case 8:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65503) | (value << 5));
                break;
            case 9:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 57343) | (value << 13));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(39) & 31) << 2) + ((rc.readSharedArray(40) & 49152) >>> 14);
            case 1:
                return (rc.readSharedArray(40) & 8128) >>> 6;
            case 2:
                return ((rc.readSharedArray(40) & 31) << 2) + ((rc.readSharedArray(41) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(41) & 8128) >>> 6;
            case 4:
                return ((rc.readSharedArray(41) & 31) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 5:
                return (rc.readSharedArray(42) & 8128) >>> 6;
            case 6:
                return ((rc.readSharedArray(42) & 31) << 2) + ((rc.readSharedArray(43) & 49152) >>> 14);
            case 7:
                return (rc.readSharedArray(43) & 8128) >>> 6;
            case 8:
                return ((rc.readSharedArray(43) & 31) << 2) + ((rc.readSharedArray(44) & 49152) >>> 14);
            case 9:
                return (rc.readSharedArray(44) & 8128) >>> 6;
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 16383) | ((value & 3) << 14));
                break;
            case 1:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 57407) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 57407) | (value << 6));
                break;
            case 4:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 5:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 57407) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 16383) | ((value & 3) << 14));
                break;
            case 7:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 57407) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 16383) | ((value & 3) << 14));
                break;
            case 9:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 57407) | (value << 6));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(39) & 63) << 2) + ((rc.readSharedArray(40) & 49152) >>> 14);
            case 1:
                return (rc.readSharedArray(40) & 16320) >>> 6;
            case 2:
                return ((rc.readSharedArray(40) & 63) << 2) + ((rc.readSharedArray(41) & 49152) >>> 14);
            case 3:
                return (rc.readSharedArray(41) & 16320) >>> 6;
            case 4:
                return ((rc.readSharedArray(41) & 63) << 2) + ((rc.readSharedArray(42) & 49152) >>> 14);
            case 5:
                return (rc.readSharedArray(42) & 16320) >>> 6;
            case 6:
                return ((rc.readSharedArray(42) & 63) << 2) + ((rc.readSharedArray(43) & 49152) >>> 14);
            case 7:
                return (rc.readSharedArray(43) & 16320) >>> 6;
            case 8:
                return ((rc.readSharedArray(43) & 63) << 2) + ((rc.readSharedArray(44) & 49152) >>> 14);
            case 9:
                return (rc.readSharedArray(44) & 16320) >>> 6;
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 16383) | ((value & 3) << 14));
                break;
            case 1:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 49215) | (value << 6));
                break;
            case 2:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 16383) | ((value & 3) << 14));
                break;
            case 3:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 49215) | (value << 6));
                break;
            case 4:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 16383) | ((value & 3) << 14));
                break;
            case 5:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 49215) | (value << 6));
                break;
            case 6:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 16383) | ((value & 3) << 14));
                break;
            case 7:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 49215) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65472) | ((value & 252) >>> 2));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 16383) | ((value & 3) << 14));
                break;
            case 9:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 49215) | (value << 6));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(44) & 56) >>> 3;
            case 1:
                return (rc.readSharedArray(45) & 3584) >>> 9;
            case 2:
                return ((rc.readSharedArray(45) & 3) << 1) + ((rc.readSharedArray(46) & 32768) >>> 15);
            case 3:
                return (rc.readSharedArray(46) & 224) >>> 5;
            case 4:
                return (rc.readSharedArray(47) & 14336) >>> 11;
            case 5:
                return (rc.readSharedArray(47) & 14) >>> 1;
            case 6:
                return (rc.readSharedArray(48) & 896) >>> 7;
            case 7:
                return (rc.readSharedArray(49) & 57344) >>> 13;
            case 8:
                return (rc.readSharedArray(49) & 56) >>> 3;
            case 9:
                return (rc.readSharedArray(50) & 3584) >>> 9;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65479) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 61951) | (value << 9));
                break;
            case 2:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 32767) | ((value & 1) << 15));
                break;
            case 3:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65311) | (value << 5));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 51199) | (value << 11));
                break;
            case 5:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65521) | (value << 1));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64639) | (value << 7));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 8191) | (value << 13));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65479) | (value << 3));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 61951) | (value << 9));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(44) & 7) << 4) + ((rc.readSharedArray(45) & 61440) >>> 12);
            case 1:
                return (rc.readSharedArray(45) & 508) >>> 2;
            case 2:
                return (rc.readSharedArray(46) & 32512) >>> 8;
            case 3:
                return ((rc.readSharedArray(46) & 31) << 2) + ((rc.readSharedArray(47) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(47) & 2032) >>> 4;
            case 5:
                return ((rc.readSharedArray(47) & 1) << 6) + ((rc.readSharedArray(48) & 64512) >>> 10);
            case 6:
                return (rc.readSharedArray(48) & 127);
            case 7:
                return (rc.readSharedArray(49) & 8128) >>> 6;
            case 8:
                return ((rc.readSharedArray(49) & 7) << 4) + ((rc.readSharedArray(50) & 61440) >>> 12);
            case 9:
                return (rc.readSharedArray(50) & 508) >>> 2;
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 4095) | ((value & 15) << 12));
                break;
            case 1:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65027) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 33023) | (value << 8));
                break;
            case 3:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 63503) | (value << 4));
                break;
            case 5:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65534) | ((value & 64) >>> 6));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 1023) | ((value & 63) << 10));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 65408) | (value));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 57407) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 4095) | ((value & 15) << 12));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 65027) | (value << 2));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(44) & 63) << 4) + ((rc.readSharedArray(45) & 61440) >>> 12);
            case 1:
                return (rc.readSharedArray(45) & 4092) >>> 2;
            case 2:
                return ((rc.readSharedArray(45) & 3) << 8) + ((rc.readSharedArray(46) & 65280) >>> 8);
            case 3:
                return ((rc.readSharedArray(46) & 255) << 2) + ((rc.readSharedArray(47) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(47) & 16368) >>> 4;
            case 5:
                return ((rc.readSharedArray(47) & 15) << 6) + ((rc.readSharedArray(48) & 64512) >>> 10);
            case 6:
                return (rc.readSharedArray(48) & 1023);
            case 7:
                return (rc.readSharedArray(49) & 65472) >>> 6;
            case 8:
                return ((rc.readSharedArray(49) & 63) << 4) + ((rc.readSharedArray(50) & 61440) >>> 12);
            case 9:
                return (rc.readSharedArray(50) & 4092) >>> 2;
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 4095) | ((value & 15) << 12));
                break;
            case 1:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 61443) | (value << 2));
                break;
            case 2:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | ((value & 768) >>> 8));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 255) | ((value & 255) << 8));
                break;
            case 3:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65280) | ((value & 1020) >>> 2));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 49167) | (value << 4));
                break;
            case 5:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65520) | ((value & 960) >>> 6));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 1023) | ((value & 63) << 10));
                break;
            case 6:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64512) | (value));
                break;
            case 7:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 63) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(49, (rc.readSharedArray(49) & 65472) | ((value & 1008) >>> 4));
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 4095) | ((value & 15) << 12));
                break;
            case 9:
                rc.writeSharedArray(50, (rc.readSharedArray(50) & 61443) | (value << 2));
                break;
        }
    }

    public int readMinerCount() throws GameActionException {
        return ((rc.readSharedArray(50) & 3) << 6) + ((rc.readSharedArray(51) & 64512) >>> 10);
    }

    public void writeMinerCount(int value) throws GameActionException {
        rc.writeSharedArray(50, (rc.readSharedArray(50) & 65532) | ((value & 192) >>> 6));
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 1023) | ((value & 63) << 10));
    }

    public int readMinerCountAll() throws GameActionException {
        return ((rc.readSharedArray(50) & 3) << 6) + ((rc.readSharedArray(51) & 64512) >>> 10);
    }

    public void writeMinerCountAll(int value) throws GameActionException {
        rc.writeSharedArray(50, (rc.readSharedArray(50) & 65532) | ((value & 192) >>> 6));
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 1023) | ((value & 63) << 10));
    }

    public int readSoldierCount() throws GameActionException {
        return (rc.readSharedArray(51) & 1020) >>> 2;
    }

    public void writeSoldierCount(int value) throws GameActionException {
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 64515) | (value << 2));
    }

    public int readSoldierCountAll() throws GameActionException {
        return (rc.readSharedArray(51) & 1020) >>> 2;
    }

    public void writeSoldierCountAll(int value) throws GameActionException {
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 64515) | (value << 2));
    }

    public int readLastArchonNum() throws GameActionException {
        return (rc.readSharedArray(51) & 3);
    }

    public void writeLastArchonNum(int value) throws GameActionException {
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 65532) | (value));
    }

    public int readLastArchonAll() throws GameActionException {
        return (rc.readSharedArray(51) & 3);
    }

    public void writeLastArchonAll(int value) throws GameActionException {
        rc.writeSharedArray(51, (rc.readSharedArray(51) & 65532) | (value));
    }

    public int readReservedResourcesLead() throws GameActionException {
        return (rc.readSharedArray(52) & 65472) >>> 6;
    }

    public void writeReservedResourcesLead(int value) throws GameActionException {
        rc.writeSharedArray(52, (rc.readSharedArray(52) & 63) | (value << 6));
    }

    public int readReservedResourcesGold() throws GameActionException {
        return (rc.readSharedArray(52) & 63);
    }

    public void writeReservedResourcesGold(int value) throws GameActionException {
        rc.writeSharedArray(52, (rc.readSharedArray(52) & 65472) | (value));
    }

    public int readReservedResourcesAll() throws GameActionException {
        return (rc.readSharedArray(52) & 65535);
    }

    public void writeReservedResourcesAll(int value) throws GameActionException {
        rc.writeSharedArray(52, (rc.readSharedArray(52) & 0) | (value));
    }
}