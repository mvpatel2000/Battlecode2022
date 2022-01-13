package hermes;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    final int OUR_ARCHON_SLOTS = 4;
    final int MAP_SLOTS = 1;
    final int CLUSTER_SLOTS = 100;
    final int COMBAT_CLUSTER_SLOTS = 5;
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_SLOTS = 10;

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

    public int readMapSymmetry() throws GameActionException {
        return (rc.readSharedArray(4) & 49152) >>> 14;
    }

    public void writeMapSymmetry(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 16383) | (value << 14));
    }

    public int readMapAll() throws GameActionException {
        return (rc.readSharedArray(4) & 49152) >>> 14;
    }

    public void writeMapAll(int value) throws GameActionException {
        rc.writeSharedArray(4, (rc.readSharedArray(4) & 16383) | (value << 14));
    }

    public int readClusterControlStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 12288) >>> 12;
            case 1:
                return (rc.readSharedArray(4) & 384) >>> 7;
            case 2:
                return (rc.readSharedArray(4) & 12) >>> 2;
            case 3:
                return (rc.readSharedArray(5) & 24576) >>> 13;
            case 4:
                return (rc.readSharedArray(5) & 768) >>> 8;
            case 5:
                return (rc.readSharedArray(5) & 24) >>> 3;
            case 6:
                return (rc.readSharedArray(6) & 49152) >>> 14;
            case 7:
                return (rc.readSharedArray(6) & 1536) >>> 9;
            case 8:
                return (rc.readSharedArray(6) & 48) >>> 4;
            case 9:
                return ((rc.readSharedArray(6) & 1) << 1) + ((rc.readSharedArray(7) & 32768) >>> 15);
            case 10:
                return (rc.readSharedArray(7) & 3072) >>> 10;
            case 11:
                return (rc.readSharedArray(7) & 96) >>> 5;
            case 12:
                return (rc.readSharedArray(7) & 3);
            case 13:
                return (rc.readSharedArray(8) & 6144) >>> 11;
            case 14:
                return (rc.readSharedArray(8) & 192) >>> 6;
            case 15:
                return (rc.readSharedArray(8) & 6) >>> 1;
            case 16:
                return (rc.readSharedArray(9) & 12288) >>> 12;
            case 17:
                return (rc.readSharedArray(9) & 384) >>> 7;
            case 18:
                return (rc.readSharedArray(9) & 12) >>> 2;
            case 19:
                return (rc.readSharedArray(10) & 24576) >>> 13;
            case 20:
                return (rc.readSharedArray(10) & 768) >>> 8;
            case 21:
                return (rc.readSharedArray(10) & 24) >>> 3;
            case 22:
                return (rc.readSharedArray(11) & 49152) >>> 14;
            case 23:
                return (rc.readSharedArray(11) & 1536) >>> 9;
            case 24:
                return (rc.readSharedArray(11) & 48) >>> 4;
            case 25:
                return ((rc.readSharedArray(11) & 1) << 1) + ((rc.readSharedArray(12) & 32768) >>> 15);
            case 26:
                return (rc.readSharedArray(12) & 3072) >>> 10;
            case 27:
                return (rc.readSharedArray(12) & 96) >>> 5;
            case 28:
                return (rc.readSharedArray(12) & 3);
            case 29:
                return (rc.readSharedArray(13) & 6144) >>> 11;
            case 30:
                return (rc.readSharedArray(13) & 192) >>> 6;
            case 31:
                return (rc.readSharedArray(13) & 6) >>> 1;
            case 32:
                return (rc.readSharedArray(14) & 12288) >>> 12;
            case 33:
                return (rc.readSharedArray(14) & 384) >>> 7;
            case 34:
                return (rc.readSharedArray(14) & 12) >>> 2;
            case 35:
                return (rc.readSharedArray(15) & 24576) >>> 13;
            case 36:
                return (rc.readSharedArray(15) & 768) >>> 8;
            case 37:
                return (rc.readSharedArray(15) & 24) >>> 3;
            case 38:
                return (rc.readSharedArray(16) & 49152) >>> 14;
            case 39:
                return (rc.readSharedArray(16) & 1536) >>> 9;
            case 40:
                return (rc.readSharedArray(16) & 48) >>> 4;
            case 41:
                return ((rc.readSharedArray(16) & 1) << 1) + ((rc.readSharedArray(17) & 32768) >>> 15);
            case 42:
                return (rc.readSharedArray(17) & 3072) >>> 10;
            case 43:
                return (rc.readSharedArray(17) & 96) >>> 5;
            case 44:
                return (rc.readSharedArray(17) & 3);
            case 45:
                return (rc.readSharedArray(18) & 6144) >>> 11;
            case 46:
                return (rc.readSharedArray(18) & 192) >>> 6;
            case 47:
                return (rc.readSharedArray(18) & 6) >>> 1;
            case 48:
                return (rc.readSharedArray(19) & 12288) >>> 12;
            case 49:
                return (rc.readSharedArray(19) & 384) >>> 7;
            case 50:
                return (rc.readSharedArray(19) & 12) >>> 2;
            case 51:
                return (rc.readSharedArray(20) & 24576) >>> 13;
            case 52:
                return (rc.readSharedArray(20) & 768) >>> 8;
            case 53:
                return (rc.readSharedArray(20) & 24) >>> 3;
            case 54:
                return (rc.readSharedArray(21) & 49152) >>> 14;
            case 55:
                return (rc.readSharedArray(21) & 1536) >>> 9;
            case 56:
                return (rc.readSharedArray(21) & 48) >>> 4;
            case 57:
                return ((rc.readSharedArray(21) & 1) << 1) + ((rc.readSharedArray(22) & 32768) >>> 15);
            case 58:
                return (rc.readSharedArray(22) & 3072) >>> 10;
            case 59:
                return (rc.readSharedArray(22) & 96) >>> 5;
            case 60:
                return (rc.readSharedArray(22) & 3);
            case 61:
                return (rc.readSharedArray(23) & 6144) >>> 11;
            case 62:
                return (rc.readSharedArray(23) & 192) >>> 6;
            case 63:
                return (rc.readSharedArray(23) & 6) >>> 1;
            case 64:
                return (rc.readSharedArray(24) & 12288) >>> 12;
            case 65:
                return (rc.readSharedArray(24) & 384) >>> 7;
            case 66:
                return (rc.readSharedArray(24) & 12) >>> 2;
            case 67:
                return (rc.readSharedArray(25) & 24576) >>> 13;
            case 68:
                return (rc.readSharedArray(25) & 768) >>> 8;
            case 69:
                return (rc.readSharedArray(25) & 24) >>> 3;
            case 70:
                return (rc.readSharedArray(26) & 49152) >>> 14;
            case 71:
                return (rc.readSharedArray(26) & 1536) >>> 9;
            case 72:
                return (rc.readSharedArray(26) & 48) >>> 4;
            case 73:
                return ((rc.readSharedArray(26) & 1) << 1) + ((rc.readSharedArray(27) & 32768) >>> 15);
            case 74:
                return (rc.readSharedArray(27) & 3072) >>> 10;
            case 75:
                return (rc.readSharedArray(27) & 96) >>> 5;
            case 76:
                return (rc.readSharedArray(27) & 3);
            case 77:
                return (rc.readSharedArray(28) & 6144) >>> 11;
            case 78:
                return (rc.readSharedArray(28) & 192) >>> 6;
            case 79:
                return (rc.readSharedArray(28) & 6) >>> 1;
            case 80:
                return (rc.readSharedArray(29) & 12288) >>> 12;
            case 81:
                return (rc.readSharedArray(29) & 384) >>> 7;
            case 82:
                return (rc.readSharedArray(29) & 12) >>> 2;
            case 83:
                return (rc.readSharedArray(30) & 24576) >>> 13;
            case 84:
                return (rc.readSharedArray(30) & 768) >>> 8;
            case 85:
                return (rc.readSharedArray(30) & 24) >>> 3;
            case 86:
                return (rc.readSharedArray(31) & 49152) >>> 14;
            case 87:
                return (rc.readSharedArray(31) & 1536) >>> 9;
            case 88:
                return (rc.readSharedArray(31) & 48) >>> 4;
            case 89:
                return ((rc.readSharedArray(31) & 1) << 1) + ((rc.readSharedArray(32) & 32768) >>> 15);
            case 90:
                return (rc.readSharedArray(32) & 3072) >>> 10;
            case 91:
                return (rc.readSharedArray(32) & 96) >>> 5;
            case 92:
                return (rc.readSharedArray(32) & 3);
            case 93:
                return (rc.readSharedArray(33) & 6144) >>> 11;
            case 94:
                return (rc.readSharedArray(33) & 192) >>> 6;
            case 95:
                return (rc.readSharedArray(33) & 6) >>> 1;
            case 96:
                return (rc.readSharedArray(34) & 12288) >>> 12;
            case 97:
                return (rc.readSharedArray(34) & 384) >>> 7;
            case 98:
                return (rc.readSharedArray(34) & 12) >>> 2;
            case 99:
                return (rc.readSharedArray(35) & 24576) >>> 13;
            default:
                return -1;
        }
    }

    public void writeClusterControlStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 53247) | (value << 12));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65151) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65523) | (value << 2));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 40959) | (value << 13));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 64767) | (value << 8));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65511) | (value << 3));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 16383) | (value << 14));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 63999) | (value << 9));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65487) | (value << 4));
                break;
            case 9:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 32767) | ((value & 1) << 15));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 62463) | (value << 10));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65439) | (value << 5));
                break;
            case 12:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65532) | (value));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 59391) | (value << 11));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65343) | (value << 6));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65529) | (value << 1));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 53247) | (value << 12));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65151) | (value << 7));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65523) | (value << 2));
                break;
            case 19:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 40959) | (value << 13));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 64767) | (value << 8));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65511) | (value << 3));
                break;
            case 22:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 16383) | (value << 14));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 63999) | (value << 9));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65487) | (value << 4));
                break;
            case 25:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 32767) | ((value & 1) << 15));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 62463) | (value << 10));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65439) | (value << 5));
                break;
            case 28:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | (value));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 59391) | (value << 11));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65343) | (value << 6));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65529) | (value << 1));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 53247) | (value << 12));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65151) | (value << 7));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65523) | (value << 2));
                break;
            case 35:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 40959) | (value << 13));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 64767) | (value << 8));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65511) | (value << 3));
                break;
            case 38:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 16383) | (value << 14));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 63999) | (value << 9));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65487) | (value << 4));
                break;
            case 41:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 32767) | ((value & 1) << 15));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 62463) | (value << 10));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65439) | (value << 5));
                break;
            case 44:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65532) | (value));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 59391) | (value << 11));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65343) | (value << 6));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65529) | (value << 1));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 53247) | (value << 12));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65151) | (value << 7));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65523) | (value << 2));
                break;
            case 51:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 40959) | (value << 13));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64767) | (value << 8));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65511) | (value << 3));
                break;
            case 54:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 16383) | (value << 14));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 63999) | (value << 9));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65487) | (value << 4));
                break;
            case 57:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 32767) | ((value & 1) << 15));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 62463) | (value << 10));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65439) | (value << 5));
                break;
            case 60:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65532) | (value));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 59391) | (value << 11));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65343) | (value << 6));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65529) | (value << 1));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 53247) | (value << 12));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65151) | (value << 7));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65523) | (value << 2));
                break;
            case 67:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 40959) | (value << 13));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 64767) | (value << 8));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65511) | (value << 3));
                break;
            case 70:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 16383) | (value << 14));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 63999) | (value << 9));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65487) | (value << 4));
                break;
            case 73:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 32767) | ((value & 1) << 15));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 62463) | (value << 10));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65439) | (value << 5));
                break;
            case 76:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | (value));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 59391) | (value << 11));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65343) | (value << 6));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65529) | (value << 1));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 53247) | (value << 12));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65151) | (value << 7));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65523) | (value << 2));
                break;
            case 83:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 40959) | (value << 13));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 64767) | (value << 8));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65511) | (value << 3));
                break;
            case 86:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 16383) | (value << 14));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 63999) | (value << 9));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65487) | (value << 4));
                break;
            case 89:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65534) | ((value & 2) >>> 1));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 32767) | ((value & 1) << 15));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 62463) | (value << 10));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65439) | (value << 5));
                break;
            case 92:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65532) | (value));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 59391) | (value << 11));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65343) | (value << 6));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65529) | (value << 1));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 53247) | (value << 12));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65151) | (value << 7));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65523) | (value << 2));
                break;
            case 99:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 40959) | (value << 13));
                break;
        }
    }

    public int readClusterResourceCount(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 3584) >>> 9;
            case 1:
                return (rc.readSharedArray(4) & 112) >>> 4;
            case 2:
                return ((rc.readSharedArray(4) & 3) << 1) + ((rc.readSharedArray(5) & 32768) >>> 15);
            case 3:
                return (rc.readSharedArray(5) & 7168) >>> 10;
            case 4:
                return (rc.readSharedArray(5) & 224) >>> 5;
            case 5:
                return (rc.readSharedArray(5) & 7);
            case 6:
                return (rc.readSharedArray(6) & 14336) >>> 11;
            case 7:
                return (rc.readSharedArray(6) & 448) >>> 6;
            case 8:
                return (rc.readSharedArray(6) & 14) >>> 1;
            case 9:
                return (rc.readSharedArray(7) & 28672) >>> 12;
            case 10:
                return (rc.readSharedArray(7) & 896) >>> 7;
            case 11:
                return (rc.readSharedArray(7) & 28) >>> 2;
            case 12:
                return (rc.readSharedArray(8) & 57344) >>> 13;
            case 13:
                return (rc.readSharedArray(8) & 1792) >>> 8;
            case 14:
                return (rc.readSharedArray(8) & 56) >>> 3;
            case 15:
                return ((rc.readSharedArray(8) & 1) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 16:
                return (rc.readSharedArray(9) & 3584) >>> 9;
            case 17:
                return (rc.readSharedArray(9) & 112) >>> 4;
            case 18:
                return ((rc.readSharedArray(9) & 3) << 1) + ((rc.readSharedArray(10) & 32768) >>> 15);
            case 19:
                return (rc.readSharedArray(10) & 7168) >>> 10;
            case 20:
                return (rc.readSharedArray(10) & 224) >>> 5;
            case 21:
                return (rc.readSharedArray(10) & 7);
            case 22:
                return (rc.readSharedArray(11) & 14336) >>> 11;
            case 23:
                return (rc.readSharedArray(11) & 448) >>> 6;
            case 24:
                return (rc.readSharedArray(11) & 14) >>> 1;
            case 25:
                return (rc.readSharedArray(12) & 28672) >>> 12;
            case 26:
                return (rc.readSharedArray(12) & 896) >>> 7;
            case 27:
                return (rc.readSharedArray(12) & 28) >>> 2;
            case 28:
                return (rc.readSharedArray(13) & 57344) >>> 13;
            case 29:
                return (rc.readSharedArray(13) & 1792) >>> 8;
            case 30:
                return (rc.readSharedArray(13) & 56) >>> 3;
            case 31:
                return ((rc.readSharedArray(13) & 1) << 2) + ((rc.readSharedArray(14) & 49152) >>> 14);
            case 32:
                return (rc.readSharedArray(14) & 3584) >>> 9;
            case 33:
                return (rc.readSharedArray(14) & 112) >>> 4;
            case 34:
                return ((rc.readSharedArray(14) & 3) << 1) + ((rc.readSharedArray(15) & 32768) >>> 15);
            case 35:
                return (rc.readSharedArray(15) & 7168) >>> 10;
            case 36:
                return (rc.readSharedArray(15) & 224) >>> 5;
            case 37:
                return (rc.readSharedArray(15) & 7);
            case 38:
                return (rc.readSharedArray(16) & 14336) >>> 11;
            case 39:
                return (rc.readSharedArray(16) & 448) >>> 6;
            case 40:
                return (rc.readSharedArray(16) & 14) >>> 1;
            case 41:
                return (rc.readSharedArray(17) & 28672) >>> 12;
            case 42:
                return (rc.readSharedArray(17) & 896) >>> 7;
            case 43:
                return (rc.readSharedArray(17) & 28) >>> 2;
            case 44:
                return (rc.readSharedArray(18) & 57344) >>> 13;
            case 45:
                return (rc.readSharedArray(18) & 1792) >>> 8;
            case 46:
                return (rc.readSharedArray(18) & 56) >>> 3;
            case 47:
                return ((rc.readSharedArray(18) & 1) << 2) + ((rc.readSharedArray(19) & 49152) >>> 14);
            case 48:
                return (rc.readSharedArray(19) & 3584) >>> 9;
            case 49:
                return (rc.readSharedArray(19) & 112) >>> 4;
            case 50:
                return ((rc.readSharedArray(19) & 3) << 1) + ((rc.readSharedArray(20) & 32768) >>> 15);
            case 51:
                return (rc.readSharedArray(20) & 7168) >>> 10;
            case 52:
                return (rc.readSharedArray(20) & 224) >>> 5;
            case 53:
                return (rc.readSharedArray(20) & 7);
            case 54:
                return (rc.readSharedArray(21) & 14336) >>> 11;
            case 55:
                return (rc.readSharedArray(21) & 448) >>> 6;
            case 56:
                return (rc.readSharedArray(21) & 14) >>> 1;
            case 57:
                return (rc.readSharedArray(22) & 28672) >>> 12;
            case 58:
                return (rc.readSharedArray(22) & 896) >>> 7;
            case 59:
                return (rc.readSharedArray(22) & 28) >>> 2;
            case 60:
                return (rc.readSharedArray(23) & 57344) >>> 13;
            case 61:
                return (rc.readSharedArray(23) & 1792) >>> 8;
            case 62:
                return (rc.readSharedArray(23) & 56) >>> 3;
            case 63:
                return ((rc.readSharedArray(23) & 1) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 64:
                return (rc.readSharedArray(24) & 3584) >>> 9;
            case 65:
                return (rc.readSharedArray(24) & 112) >>> 4;
            case 66:
                return ((rc.readSharedArray(24) & 3) << 1) + ((rc.readSharedArray(25) & 32768) >>> 15);
            case 67:
                return (rc.readSharedArray(25) & 7168) >>> 10;
            case 68:
                return (rc.readSharedArray(25) & 224) >>> 5;
            case 69:
                return (rc.readSharedArray(25) & 7);
            case 70:
                return (rc.readSharedArray(26) & 14336) >>> 11;
            case 71:
                return (rc.readSharedArray(26) & 448) >>> 6;
            case 72:
                return (rc.readSharedArray(26) & 14) >>> 1;
            case 73:
                return (rc.readSharedArray(27) & 28672) >>> 12;
            case 74:
                return (rc.readSharedArray(27) & 896) >>> 7;
            case 75:
                return (rc.readSharedArray(27) & 28) >>> 2;
            case 76:
                return (rc.readSharedArray(28) & 57344) >>> 13;
            case 77:
                return (rc.readSharedArray(28) & 1792) >>> 8;
            case 78:
                return (rc.readSharedArray(28) & 56) >>> 3;
            case 79:
                return ((rc.readSharedArray(28) & 1) << 2) + ((rc.readSharedArray(29) & 49152) >>> 14);
            case 80:
                return (rc.readSharedArray(29) & 3584) >>> 9;
            case 81:
                return (rc.readSharedArray(29) & 112) >>> 4;
            case 82:
                return ((rc.readSharedArray(29) & 3) << 1) + ((rc.readSharedArray(30) & 32768) >>> 15);
            case 83:
                return (rc.readSharedArray(30) & 7168) >>> 10;
            case 84:
                return (rc.readSharedArray(30) & 224) >>> 5;
            case 85:
                return (rc.readSharedArray(30) & 7);
            case 86:
                return (rc.readSharedArray(31) & 14336) >>> 11;
            case 87:
                return (rc.readSharedArray(31) & 448) >>> 6;
            case 88:
                return (rc.readSharedArray(31) & 14) >>> 1;
            case 89:
                return (rc.readSharedArray(32) & 28672) >>> 12;
            case 90:
                return (rc.readSharedArray(32) & 896) >>> 7;
            case 91:
                return (rc.readSharedArray(32) & 28) >>> 2;
            case 92:
                return (rc.readSharedArray(33) & 57344) >>> 13;
            case 93:
                return (rc.readSharedArray(33) & 1792) >>> 8;
            case 94:
                return (rc.readSharedArray(33) & 56) >>> 3;
            case 95:
                return ((rc.readSharedArray(33) & 1) << 2) + ((rc.readSharedArray(34) & 49152) >>> 14);
            case 96:
                return (rc.readSharedArray(34) & 3584) >>> 9;
            case 97:
                return (rc.readSharedArray(34) & 112) >>> 4;
            case 98:
                return ((rc.readSharedArray(34) & 3) << 1) + ((rc.readSharedArray(35) & 32768) >>> 15);
            case 99:
                return (rc.readSharedArray(35) & 7168) >>> 10;
            default:
                return -1;
        }
    }

    public void writeClusterResourceCount(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 61951) | (value << 9));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65423) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 32767) | ((value & 1) << 15));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 58367) | (value << 10));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65311) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65528) | (value));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 51199) | (value << 11));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65087) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65521) | (value << 1));
                break;
            case 9:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 36863) | (value << 12));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 64639) | (value << 7));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65507) | (value << 2));
                break;
            case 12:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 8191) | (value << 13));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 63743) | (value << 8));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65479) | (value << 3));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 61951) | (value << 9));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65423) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 32767) | ((value & 1) << 15));
                break;
            case 19:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 58367) | (value << 10));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65311) | (value << 5));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65528) | (value));
                break;
            case 22:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 51199) | (value << 11));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65087) | (value << 6));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65521) | (value << 1));
                break;
            case 25:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 36863) | (value << 12));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 64639) | (value << 7));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65507) | (value << 2));
                break;
            case 28:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 8191) | (value << 13));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 63743) | (value << 8));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65479) | (value << 3));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 16383) | ((value & 3) << 14));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 61951) | (value << 9));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65423) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 32767) | ((value & 1) << 15));
                break;
            case 35:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 58367) | (value << 10));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65311) | (value << 5));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65528) | (value));
                break;
            case 38:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 51199) | (value << 11));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65087) | (value << 6));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65521) | (value << 1));
                break;
            case 41:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 36863) | (value << 12));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 64639) | (value << 7));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65507) | (value << 2));
                break;
            case 44:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 8191) | (value << 13));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 63743) | (value << 8));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65479) | (value << 3));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 16383) | ((value & 3) << 14));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 61951) | (value << 9));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65423) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 32767) | ((value & 1) << 15));
                break;
            case 51:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 58367) | (value << 10));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65311) | (value << 5));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65528) | (value));
                break;
            case 54:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 51199) | (value << 11));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65087) | (value << 6));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65521) | (value << 1));
                break;
            case 57:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 36863) | (value << 12));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 64639) | (value << 7));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65507) | (value << 2));
                break;
            case 60:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 8191) | (value << 13));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 63743) | (value << 8));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65479) | (value << 3));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 61951) | (value << 9));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65423) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 32767) | ((value & 1) << 15));
                break;
            case 67:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 58367) | (value << 10));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65311) | (value << 5));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65528) | (value));
                break;
            case 70:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 51199) | (value << 11));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65087) | (value << 6));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65521) | (value << 1));
                break;
            case 73:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 36863) | (value << 12));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 64639) | (value << 7));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65507) | (value << 2));
                break;
            case 76:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 8191) | (value << 13));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 63743) | (value << 8));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65479) | (value << 3));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 16383) | ((value & 3) << 14));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 61951) | (value << 9));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65423) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 32767) | ((value & 1) << 15));
                break;
            case 83:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 58367) | (value << 10));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65311) | (value << 5));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65528) | (value));
                break;
            case 86:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 51199) | (value << 11));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65087) | (value << 6));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65521) | (value << 1));
                break;
            case 89:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 36863) | (value << 12));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 64639) | (value << 7));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65507) | (value << 2));
                break;
            case 92:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 8191) | (value << 13));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 63743) | (value << 8));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65479) | (value << 3));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 16383) | ((value & 3) << 14));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 61951) | (value << 9));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65423) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65532) | ((value & 6) >>> 1));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 32767) | ((value & 1) << 15));
                break;
            case 99:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 58367) | (value << 10));
                break;
        }
    }

    public int readClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(4) & 15872) >>> 9;
            case 1:
                return (rc.readSharedArray(4) & 496) >>> 4;
            case 2:
                return ((rc.readSharedArray(4) & 15) << 1) + ((rc.readSharedArray(5) & 32768) >>> 15);
            case 3:
                return (rc.readSharedArray(5) & 31744) >>> 10;
            case 4:
                return (rc.readSharedArray(5) & 992) >>> 5;
            case 5:
                return (rc.readSharedArray(5) & 31);
            case 6:
                return (rc.readSharedArray(6) & 63488) >>> 11;
            case 7:
                return (rc.readSharedArray(6) & 1984) >>> 6;
            case 8:
                return (rc.readSharedArray(6) & 62) >>> 1;
            case 9:
                return ((rc.readSharedArray(6) & 1) << 4) + ((rc.readSharedArray(7) & 61440) >>> 12);
            case 10:
                return (rc.readSharedArray(7) & 3968) >>> 7;
            case 11:
                return (rc.readSharedArray(7) & 124) >>> 2;
            case 12:
                return ((rc.readSharedArray(7) & 3) << 3) + ((rc.readSharedArray(8) & 57344) >>> 13);
            case 13:
                return (rc.readSharedArray(8) & 7936) >>> 8;
            case 14:
                return (rc.readSharedArray(8) & 248) >>> 3;
            case 15:
                return ((rc.readSharedArray(8) & 7) << 2) + ((rc.readSharedArray(9) & 49152) >>> 14);
            case 16:
                return (rc.readSharedArray(9) & 15872) >>> 9;
            case 17:
                return (rc.readSharedArray(9) & 496) >>> 4;
            case 18:
                return ((rc.readSharedArray(9) & 15) << 1) + ((rc.readSharedArray(10) & 32768) >>> 15);
            case 19:
                return (rc.readSharedArray(10) & 31744) >>> 10;
            case 20:
                return (rc.readSharedArray(10) & 992) >>> 5;
            case 21:
                return (rc.readSharedArray(10) & 31);
            case 22:
                return (rc.readSharedArray(11) & 63488) >>> 11;
            case 23:
                return (rc.readSharedArray(11) & 1984) >>> 6;
            case 24:
                return (rc.readSharedArray(11) & 62) >>> 1;
            case 25:
                return ((rc.readSharedArray(11) & 1) << 4) + ((rc.readSharedArray(12) & 61440) >>> 12);
            case 26:
                return (rc.readSharedArray(12) & 3968) >>> 7;
            case 27:
                return (rc.readSharedArray(12) & 124) >>> 2;
            case 28:
                return ((rc.readSharedArray(12) & 3) << 3) + ((rc.readSharedArray(13) & 57344) >>> 13);
            case 29:
                return (rc.readSharedArray(13) & 7936) >>> 8;
            case 30:
                return (rc.readSharedArray(13) & 248) >>> 3;
            case 31:
                return ((rc.readSharedArray(13) & 7) << 2) + ((rc.readSharedArray(14) & 49152) >>> 14);
            case 32:
                return (rc.readSharedArray(14) & 15872) >>> 9;
            case 33:
                return (rc.readSharedArray(14) & 496) >>> 4;
            case 34:
                return ((rc.readSharedArray(14) & 15) << 1) + ((rc.readSharedArray(15) & 32768) >>> 15);
            case 35:
                return (rc.readSharedArray(15) & 31744) >>> 10;
            case 36:
                return (rc.readSharedArray(15) & 992) >>> 5;
            case 37:
                return (rc.readSharedArray(15) & 31);
            case 38:
                return (rc.readSharedArray(16) & 63488) >>> 11;
            case 39:
                return (rc.readSharedArray(16) & 1984) >>> 6;
            case 40:
                return (rc.readSharedArray(16) & 62) >>> 1;
            case 41:
                return ((rc.readSharedArray(16) & 1) << 4) + ((rc.readSharedArray(17) & 61440) >>> 12);
            case 42:
                return (rc.readSharedArray(17) & 3968) >>> 7;
            case 43:
                return (rc.readSharedArray(17) & 124) >>> 2;
            case 44:
                return ((rc.readSharedArray(17) & 3) << 3) + ((rc.readSharedArray(18) & 57344) >>> 13);
            case 45:
                return (rc.readSharedArray(18) & 7936) >>> 8;
            case 46:
                return (rc.readSharedArray(18) & 248) >>> 3;
            case 47:
                return ((rc.readSharedArray(18) & 7) << 2) + ((rc.readSharedArray(19) & 49152) >>> 14);
            case 48:
                return (rc.readSharedArray(19) & 15872) >>> 9;
            case 49:
                return (rc.readSharedArray(19) & 496) >>> 4;
            case 50:
                return ((rc.readSharedArray(19) & 15) << 1) + ((rc.readSharedArray(20) & 32768) >>> 15);
            case 51:
                return (rc.readSharedArray(20) & 31744) >>> 10;
            case 52:
                return (rc.readSharedArray(20) & 992) >>> 5;
            case 53:
                return (rc.readSharedArray(20) & 31);
            case 54:
                return (rc.readSharedArray(21) & 63488) >>> 11;
            case 55:
                return (rc.readSharedArray(21) & 1984) >>> 6;
            case 56:
                return (rc.readSharedArray(21) & 62) >>> 1;
            case 57:
                return ((rc.readSharedArray(21) & 1) << 4) + ((rc.readSharedArray(22) & 61440) >>> 12);
            case 58:
                return (rc.readSharedArray(22) & 3968) >>> 7;
            case 59:
                return (rc.readSharedArray(22) & 124) >>> 2;
            case 60:
                return ((rc.readSharedArray(22) & 3) << 3) + ((rc.readSharedArray(23) & 57344) >>> 13);
            case 61:
                return (rc.readSharedArray(23) & 7936) >>> 8;
            case 62:
                return (rc.readSharedArray(23) & 248) >>> 3;
            case 63:
                return ((rc.readSharedArray(23) & 7) << 2) + ((rc.readSharedArray(24) & 49152) >>> 14);
            case 64:
                return (rc.readSharedArray(24) & 15872) >>> 9;
            case 65:
                return (rc.readSharedArray(24) & 496) >>> 4;
            case 66:
                return ((rc.readSharedArray(24) & 15) << 1) + ((rc.readSharedArray(25) & 32768) >>> 15);
            case 67:
                return (rc.readSharedArray(25) & 31744) >>> 10;
            case 68:
                return (rc.readSharedArray(25) & 992) >>> 5;
            case 69:
                return (rc.readSharedArray(25) & 31);
            case 70:
                return (rc.readSharedArray(26) & 63488) >>> 11;
            case 71:
                return (rc.readSharedArray(26) & 1984) >>> 6;
            case 72:
                return (rc.readSharedArray(26) & 62) >>> 1;
            case 73:
                return ((rc.readSharedArray(26) & 1) << 4) + ((rc.readSharedArray(27) & 61440) >>> 12);
            case 74:
                return (rc.readSharedArray(27) & 3968) >>> 7;
            case 75:
                return (rc.readSharedArray(27) & 124) >>> 2;
            case 76:
                return ((rc.readSharedArray(27) & 3) << 3) + ((rc.readSharedArray(28) & 57344) >>> 13);
            case 77:
                return (rc.readSharedArray(28) & 7936) >>> 8;
            case 78:
                return (rc.readSharedArray(28) & 248) >>> 3;
            case 79:
                return ((rc.readSharedArray(28) & 7) << 2) + ((rc.readSharedArray(29) & 49152) >>> 14);
            case 80:
                return (rc.readSharedArray(29) & 15872) >>> 9;
            case 81:
                return (rc.readSharedArray(29) & 496) >>> 4;
            case 82:
                return ((rc.readSharedArray(29) & 15) << 1) + ((rc.readSharedArray(30) & 32768) >>> 15);
            case 83:
                return (rc.readSharedArray(30) & 31744) >>> 10;
            case 84:
                return (rc.readSharedArray(30) & 992) >>> 5;
            case 85:
                return (rc.readSharedArray(30) & 31);
            case 86:
                return (rc.readSharedArray(31) & 63488) >>> 11;
            case 87:
                return (rc.readSharedArray(31) & 1984) >>> 6;
            case 88:
                return (rc.readSharedArray(31) & 62) >>> 1;
            case 89:
                return ((rc.readSharedArray(31) & 1) << 4) + ((rc.readSharedArray(32) & 61440) >>> 12);
            case 90:
                return (rc.readSharedArray(32) & 3968) >>> 7;
            case 91:
                return (rc.readSharedArray(32) & 124) >>> 2;
            case 92:
                return ((rc.readSharedArray(32) & 3) << 3) + ((rc.readSharedArray(33) & 57344) >>> 13);
            case 93:
                return (rc.readSharedArray(33) & 7936) >>> 8;
            case 94:
                return (rc.readSharedArray(33) & 248) >>> 3;
            case 95:
                return ((rc.readSharedArray(33) & 7) << 2) + ((rc.readSharedArray(34) & 49152) >>> 14);
            case 96:
                return (rc.readSharedArray(34) & 15872) >>> 9;
            case 97:
                return (rc.readSharedArray(34) & 496) >>> 4;
            case 98:
                return ((rc.readSharedArray(34) & 15) << 1) + ((rc.readSharedArray(35) & 32768) >>> 15);
            case 99:
                return (rc.readSharedArray(35) & 31744) >>> 10;
            default:
                return -1;
        }
    }

    public void writeClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 49663) | (value << 9));
                break;
            case 1:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65039) | (value << 4));
                break;
            case 2:
                rc.writeSharedArray(4, (rc.readSharedArray(4) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 32767) | ((value & 1) << 15));
                break;
            case 3:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 33791) | (value << 10));
                break;
            case 4:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 64543) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(5, (rc.readSharedArray(5) & 65504) | (value));
                break;
            case 6:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 2047) | (value << 11));
                break;
            case 7:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 63551) | (value << 6));
                break;
            case 8:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65473) | (value << 1));
                break;
            case 9:
                rc.writeSharedArray(6, (rc.readSharedArray(6) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 4095) | ((value & 15) << 12));
                break;
            case 10:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 61567) | (value << 7));
                break;
            case 11:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65411) | (value << 2));
                break;
            case 12:
                rc.writeSharedArray(7, (rc.readSharedArray(7) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 8191) | ((value & 7) << 13));
                break;
            case 13:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 57599) | (value << 8));
                break;
            case 14:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65287) | (value << 3));
                break;
            case 15:
                rc.writeSharedArray(8, (rc.readSharedArray(8) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 16383) | ((value & 3) << 14));
                break;
            case 16:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 49663) | (value << 9));
                break;
            case 17:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65039) | (value << 4));
                break;
            case 18:
                rc.writeSharedArray(9, (rc.readSharedArray(9) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 32767) | ((value & 1) << 15));
                break;
            case 19:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 33791) | (value << 10));
                break;
            case 20:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 64543) | (value << 5));
                break;
            case 21:
                rc.writeSharedArray(10, (rc.readSharedArray(10) & 65504) | (value));
                break;
            case 22:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 2047) | (value << 11));
                break;
            case 23:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 63551) | (value << 6));
                break;
            case 24:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65473) | (value << 1));
                break;
            case 25:
                rc.writeSharedArray(11, (rc.readSharedArray(11) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 4095) | ((value & 15) << 12));
                break;
            case 26:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 61567) | (value << 7));
                break;
            case 27:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65411) | (value << 2));
                break;
            case 28:
                rc.writeSharedArray(12, (rc.readSharedArray(12) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 8191) | ((value & 7) << 13));
                break;
            case 29:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 57599) | (value << 8));
                break;
            case 30:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65287) | (value << 3));
                break;
            case 31:
                rc.writeSharedArray(13, (rc.readSharedArray(13) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 16383) | ((value & 3) << 14));
                break;
            case 32:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 49663) | (value << 9));
                break;
            case 33:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65039) | (value << 4));
                break;
            case 34:
                rc.writeSharedArray(14, (rc.readSharedArray(14) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 32767) | ((value & 1) << 15));
                break;
            case 35:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 33791) | (value << 10));
                break;
            case 36:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 64543) | (value << 5));
                break;
            case 37:
                rc.writeSharedArray(15, (rc.readSharedArray(15) & 65504) | (value));
                break;
            case 38:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 2047) | (value << 11));
                break;
            case 39:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 63551) | (value << 6));
                break;
            case 40:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65473) | (value << 1));
                break;
            case 41:
                rc.writeSharedArray(16, (rc.readSharedArray(16) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 4095) | ((value & 15) << 12));
                break;
            case 42:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 61567) | (value << 7));
                break;
            case 43:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65411) | (value << 2));
                break;
            case 44:
                rc.writeSharedArray(17, (rc.readSharedArray(17) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 8191) | ((value & 7) << 13));
                break;
            case 45:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 57599) | (value << 8));
                break;
            case 46:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65287) | (value << 3));
                break;
            case 47:
                rc.writeSharedArray(18, (rc.readSharedArray(18) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 16383) | ((value & 3) << 14));
                break;
            case 48:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 49663) | (value << 9));
                break;
            case 49:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65039) | (value << 4));
                break;
            case 50:
                rc.writeSharedArray(19, (rc.readSharedArray(19) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 32767) | ((value & 1) << 15));
                break;
            case 51:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 33791) | (value << 10));
                break;
            case 52:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 64543) | (value << 5));
                break;
            case 53:
                rc.writeSharedArray(20, (rc.readSharedArray(20) & 65504) | (value));
                break;
            case 54:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 2047) | (value << 11));
                break;
            case 55:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 63551) | (value << 6));
                break;
            case 56:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65473) | (value << 1));
                break;
            case 57:
                rc.writeSharedArray(21, (rc.readSharedArray(21) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 4095) | ((value & 15) << 12));
                break;
            case 58:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 61567) | (value << 7));
                break;
            case 59:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65411) | (value << 2));
                break;
            case 60:
                rc.writeSharedArray(22, (rc.readSharedArray(22) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 8191) | ((value & 7) << 13));
                break;
            case 61:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 57599) | (value << 8));
                break;
            case 62:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65287) | (value << 3));
                break;
            case 63:
                rc.writeSharedArray(23, (rc.readSharedArray(23) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 16383) | ((value & 3) << 14));
                break;
            case 64:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 49663) | (value << 9));
                break;
            case 65:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65039) | (value << 4));
                break;
            case 66:
                rc.writeSharedArray(24, (rc.readSharedArray(24) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 32767) | ((value & 1) << 15));
                break;
            case 67:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 33791) | (value << 10));
                break;
            case 68:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 64543) | (value << 5));
                break;
            case 69:
                rc.writeSharedArray(25, (rc.readSharedArray(25) & 65504) | (value));
                break;
            case 70:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 2047) | (value << 11));
                break;
            case 71:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 63551) | (value << 6));
                break;
            case 72:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65473) | (value << 1));
                break;
            case 73:
                rc.writeSharedArray(26, (rc.readSharedArray(26) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 4095) | ((value & 15) << 12));
                break;
            case 74:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 61567) | (value << 7));
                break;
            case 75:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65411) | (value << 2));
                break;
            case 76:
                rc.writeSharedArray(27, (rc.readSharedArray(27) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 8191) | ((value & 7) << 13));
                break;
            case 77:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 57599) | (value << 8));
                break;
            case 78:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65287) | (value << 3));
                break;
            case 79:
                rc.writeSharedArray(28, (rc.readSharedArray(28) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 16383) | ((value & 3) << 14));
                break;
            case 80:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 49663) | (value << 9));
                break;
            case 81:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65039) | (value << 4));
                break;
            case 82:
                rc.writeSharedArray(29, (rc.readSharedArray(29) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 32767) | ((value & 1) << 15));
                break;
            case 83:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 33791) | (value << 10));
                break;
            case 84:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 64543) | (value << 5));
                break;
            case 85:
                rc.writeSharedArray(30, (rc.readSharedArray(30) & 65504) | (value));
                break;
            case 86:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 2047) | (value << 11));
                break;
            case 87:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 63551) | (value << 6));
                break;
            case 88:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65473) | (value << 1));
                break;
            case 89:
                rc.writeSharedArray(31, (rc.readSharedArray(31) & 65534) | ((value & 16) >>> 4));
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 4095) | ((value & 15) << 12));
                break;
            case 90:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 61567) | (value << 7));
                break;
            case 91:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65411) | (value << 2));
                break;
            case 92:
                rc.writeSharedArray(32, (rc.readSharedArray(32) & 65532) | ((value & 24) >>> 3));
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 8191) | ((value & 7) << 13));
                break;
            case 93:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 57599) | (value << 8));
                break;
            case 94:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65287) | (value << 3));
                break;
            case 95:
                rc.writeSharedArray(33, (rc.readSharedArray(33) & 65528) | ((value & 28) >>> 2));
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 16383) | ((value & 3) << 14));
                break;
            case 96:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 49663) | (value << 9));
                break;
            case 97:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65039) | (value << 4));
                break;
            case 98:
                rc.writeSharedArray(34, (rc.readSharedArray(34) & 65520) | ((value & 30) >>> 1));
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 32767) | ((value & 1) << 15));
                break;
            case 99:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 33791) | (value << 10));
                break;
        }
    }

    public int readCombatClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(35) & 1016) >>> 3;
            case 1:
                return ((rc.readSharedArray(35) & 7) << 4) + ((rc.readSharedArray(36) & 61440) >>> 12);
            case 2:
                return (rc.readSharedArray(36) & 4064) >>> 5;
            case 3:
                return ((rc.readSharedArray(36) & 31) << 2) + ((rc.readSharedArray(37) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(37) & 16256) >>> 7;
            default:
                return -1;
        }
    }

    public void writeCombatClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64519) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 4095) | ((value & 15) << 12));
                break;
            case 2:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 61471) | (value << 5));
                break;
            case 3:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 49279) | (value << 7));
                break;
        }
    }

    public int readCombatClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(35) & 1016) >>> 3;
            case 1:
                return ((rc.readSharedArray(35) & 7) << 4) + ((rc.readSharedArray(36) & 61440) >>> 12);
            case 2:
                return (rc.readSharedArray(36) & 4064) >>> 5;
            case 3:
                return ((rc.readSharedArray(36) & 31) << 2) + ((rc.readSharedArray(37) & 49152) >>> 14);
            case 4:
                return (rc.readSharedArray(37) & 16256) >>> 7;
            default:
                return -1;
        }
    }

    public void writeCombatClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 64519) | (value << 3));
                break;
            case 1:
                rc.writeSharedArray(35, (rc.readSharedArray(35) & 65528) | ((value & 112) >>> 4));
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 4095) | ((value & 15) << 12));
                break;
            case 2:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 61471) | (value << 5));
                break;
            case 3:
                rc.writeSharedArray(36, (rc.readSharedArray(36) & 65504) | ((value & 124) >>> 2));
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 16383) | ((value & 3) << 14));
                break;
            case 4:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 49279) | (value << 7));
                break;
        }
    }

    public int readExploreClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(37) & 64) >>> 6;
            case 1:
                return (rc.readSharedArray(38) & 16384) >>> 14;
            case 2:
                return (rc.readSharedArray(38) & 64) >>> 6;
            case 3:
                return (rc.readSharedArray(39) & 16384) >>> 14;
            case 4:
                return (rc.readSharedArray(39) & 64) >>> 6;
            case 5:
                return (rc.readSharedArray(40) & 16384) >>> 14;
            case 6:
                return (rc.readSharedArray(40) & 64) >>> 6;
            case 7:
                return (rc.readSharedArray(41) & 16384) >>> 14;
            case 8:
                return (rc.readSharedArray(41) & 64) >>> 6;
            case 9:
                return (rc.readSharedArray(42) & 16384) >>> 14;
            default:
                return -1;
        }
    }

    public void writeExploreClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65471) | (value << 6));
                break;
            case 1:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 49151) | (value << 14));
                break;
            case 2:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65471) | (value << 6));
                break;
            case 3:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 49151) | (value << 14));
                break;
            case 4:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65471) | (value << 6));
                break;
            case 5:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 49151) | (value << 14));
                break;
            case 6:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65471) | (value << 6));
                break;
            case 7:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 49151) | (value << 14));
                break;
            case 8:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65471) | (value << 6));
                break;
            case 9:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 49151) | (value << 14));
                break;
        }
    }

    public int readExploreClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(37) & 63) << 1) + ((rc.readSharedArray(38) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(38) & 16256) >>> 7;
            case 2:
                return ((rc.readSharedArray(38) & 63) << 1) + ((rc.readSharedArray(39) & 32768) >>> 15);
            case 3:
                return (rc.readSharedArray(39) & 16256) >>> 7;
            case 4:
                return ((rc.readSharedArray(39) & 63) << 1) + ((rc.readSharedArray(40) & 32768) >>> 15);
            case 5:
                return (rc.readSharedArray(40) & 16256) >>> 7;
            case 6:
                return ((rc.readSharedArray(40) & 63) << 1) + ((rc.readSharedArray(41) & 32768) >>> 15);
            case 7:
                return (rc.readSharedArray(41) & 16256) >>> 7;
            case 8:
                return ((rc.readSharedArray(41) & 63) << 1) + ((rc.readSharedArray(42) & 32768) >>> 15);
            case 9:
                return (rc.readSharedArray(42) & 16256) >>> 7;
            default:
                return -1;
        }
    }

    public void writeExploreClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 49279) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 32767) | ((value & 1) << 15));
                break;
            case 3:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 49279) | (value << 7));
                break;
            case 4:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 32767) | ((value & 1) << 15));
                break;
            case 5:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 49279) | (value << 7));
                break;
            case 6:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 32767) | ((value & 1) << 15));
                break;
            case 7:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 49279) | (value << 7));
                break;
            case 8:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 32767) | ((value & 1) << 15));
                break;
            case 9:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 49279) | (value << 7));
                break;
        }
    }

    public int readExploreClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(37) & 127) << 1) + ((rc.readSharedArray(38) & 32768) >>> 15);
            case 1:
                return (rc.readSharedArray(38) & 32640) >>> 7;
            case 2:
                return ((rc.readSharedArray(38) & 127) << 1) + ((rc.readSharedArray(39) & 32768) >>> 15);
            case 3:
                return (rc.readSharedArray(39) & 32640) >>> 7;
            case 4:
                return ((rc.readSharedArray(39) & 127) << 1) + ((rc.readSharedArray(40) & 32768) >>> 15);
            case 5:
                return (rc.readSharedArray(40) & 32640) >>> 7;
            case 6:
                return ((rc.readSharedArray(40) & 127) << 1) + ((rc.readSharedArray(41) & 32768) >>> 15);
            case 7:
                return (rc.readSharedArray(41) & 32640) >>> 7;
            case 8:
                return ((rc.readSharedArray(41) & 127) << 1) + ((rc.readSharedArray(42) & 32768) >>> 15);
            case 9:
                return (rc.readSharedArray(42) & 32640) >>> 7;
            default:
                return -1;
        }
    }

    public void writeExploreClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(37, (rc.readSharedArray(37) & 65408) | ((value & 254) >>> 1));
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 32767) | ((value & 1) << 15));
                break;
            case 1:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 32895) | (value << 7));
                break;
            case 2:
                rc.writeSharedArray(38, (rc.readSharedArray(38) & 65408) | ((value & 254) >>> 1));
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 32767) | ((value & 1) << 15));
                break;
            case 3:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 32895) | (value << 7));
                break;
            case 4:
                rc.writeSharedArray(39, (rc.readSharedArray(39) & 65408) | ((value & 254) >>> 1));
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 32767) | ((value & 1) << 15));
                break;
            case 5:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 32895) | (value << 7));
                break;
            case 6:
                rc.writeSharedArray(40, (rc.readSharedArray(40) & 65408) | ((value & 254) >>> 1));
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 32767) | ((value & 1) << 15));
                break;
            case 7:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 32895) | (value << 7));
                break;
            case 8:
                rc.writeSharedArray(41, (rc.readSharedArray(41) & 65408) | ((value & 254) >>> 1));
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 32767) | ((value & 1) << 15));
                break;
            case 9:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 32895) | (value << 7));
                break;
        }
    }

    public int readMineClusterClaimStatus(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return (rc.readSharedArray(42) & 112) >>> 4;
            case 1:
                return (rc.readSharedArray(43) & 7168) >>> 10;
            case 2:
                return (rc.readSharedArray(43) & 7);
            case 3:
                return (rc.readSharedArray(44) & 448) >>> 6;
            case 4:
                return (rc.readSharedArray(45) & 28672) >>> 12;
            case 5:
                return (rc.readSharedArray(45) & 28) >>> 2;
            case 6:
                return (rc.readSharedArray(46) & 1792) >>> 8;
            case 7:
                return ((rc.readSharedArray(46) & 1) << 2) + ((rc.readSharedArray(47) & 49152) >>> 14);
            case 8:
                return (rc.readSharedArray(47) & 112) >>> 4;
            case 9:
                return (rc.readSharedArray(48) & 7168) >>> 10;
            default:
                return -1;
        }
    }

    public void writeMineClusterClaimStatus(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65423) | (value << 4));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 58367) | (value << 10));
                break;
            case 2:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65528) | (value));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65087) | (value << 6));
                break;
            case 4:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 36863) | (value << 12));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65507) | (value << 2));
                break;
            case 6:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 63743) | (value << 8));
                break;
            case 7:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65534) | ((value & 4) >>> 2));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 16383) | ((value & 3) << 14));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65423) | (value << 4));
                break;
            case 9:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 58367) | (value << 10));
                break;
        }
    }

    public int readMineClusterIndex(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(42) & 15) << 3) + ((rc.readSharedArray(43) & 57344) >>> 13);
            case 1:
                return (rc.readSharedArray(43) & 1016) >>> 3;
            case 2:
                return (rc.readSharedArray(44) & 65024) >>> 9;
            case 3:
                return ((rc.readSharedArray(44) & 63) << 1) + ((rc.readSharedArray(45) & 32768) >>> 15);
            case 4:
                return (rc.readSharedArray(45) & 4064) >>> 5;
            case 5:
                return ((rc.readSharedArray(45) & 3) << 5) + ((rc.readSharedArray(46) & 63488) >>> 11);
            case 6:
                return (rc.readSharedArray(46) & 254) >>> 1;
            case 7:
                return (rc.readSharedArray(47) & 16256) >>> 7;
            case 8:
                return ((rc.readSharedArray(47) & 15) << 3) + ((rc.readSharedArray(48) & 57344) >>> 13);
            case 9:
                return (rc.readSharedArray(48) & 1016) >>> 3;
            default:
                return -1;
        }
    }

    public void writeMineClusterIndex(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65520) | ((value & 120) >>> 3));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 8191) | ((value & 7) << 13));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 64519) | (value << 3));
                break;
            case 2:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 511) | (value << 9));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65472) | ((value & 126) >>> 1));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 32767) | ((value & 1) << 15));
                break;
            case 4:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 61471) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65532) | ((value & 96) >>> 5));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 2047) | ((value & 31) << 11));
                break;
            case 6:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65281) | (value << 1));
                break;
            case 7:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 49279) | (value << 7));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65520) | ((value & 120) >>> 3));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 8191) | ((value & 7) << 13));
                break;
            case 9:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 64519) | (value << 3));
                break;
        }
    }

    public int readMineClusterAll(int idx) throws GameActionException {
        switch (idx) {
            case 0:
                return ((rc.readSharedArray(42) & 127) << 3) + ((rc.readSharedArray(43) & 57344) >>> 13);
            case 1:
                return (rc.readSharedArray(43) & 8184) >>> 3;
            case 2:
                return ((rc.readSharedArray(43) & 7) << 7) + ((rc.readSharedArray(44) & 65024) >>> 9);
            case 3:
                return ((rc.readSharedArray(44) & 511) << 1) + ((rc.readSharedArray(45) & 32768) >>> 15);
            case 4:
                return (rc.readSharedArray(45) & 32736) >>> 5;
            case 5:
                return ((rc.readSharedArray(45) & 31) << 5) + ((rc.readSharedArray(46) & 63488) >>> 11);
            case 6:
                return (rc.readSharedArray(46) & 2046) >>> 1;
            case 7:
                return ((rc.readSharedArray(46) & 1) << 9) + ((rc.readSharedArray(47) & 65408) >>> 7);
            case 8:
                return ((rc.readSharedArray(47) & 127) << 3) + ((rc.readSharedArray(48) & 57344) >>> 13);
            case 9:
                return (rc.readSharedArray(48) & 8184) >>> 3;
            default:
                return -1;
        }
    }

    public void writeMineClusterAll(int idx, int value) throws GameActionException {
        switch (idx) {
            case 0:
                rc.writeSharedArray(42, (rc.readSharedArray(42) & 65408) | ((value & 1016) >>> 3));
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 8191) | ((value & 7) << 13));
                break;
            case 1:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 57351) | (value << 3));
                break;
            case 2:
                rc.writeSharedArray(43, (rc.readSharedArray(43) & 65528) | ((value & 896) >>> 7));
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 511) | ((value & 127) << 9));
                break;
            case 3:
                rc.writeSharedArray(44, (rc.readSharedArray(44) & 65024) | ((value & 1022) >>> 1));
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 32767) | ((value & 1) << 15));
                break;
            case 4:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 32799) | (value << 5));
                break;
            case 5:
                rc.writeSharedArray(45, (rc.readSharedArray(45) & 65504) | ((value & 992) >>> 5));
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 2047) | ((value & 31) << 11));
                break;
            case 6:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 63489) | (value << 1));
                break;
            case 7:
                rc.writeSharedArray(46, (rc.readSharedArray(46) & 65534) | ((value & 512) >>> 9));
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 127) | ((value & 511) << 7));
                break;
            case 8:
                rc.writeSharedArray(47, (rc.readSharedArray(47) & 65408) | ((value & 1016) >>> 3));
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 8191) | ((value & 7) << 13));
                break;
            case 9:
                rc.writeSharedArray(48, (rc.readSharedArray(48) & 57351) | (value << 3));
                break;
        }
    }
}