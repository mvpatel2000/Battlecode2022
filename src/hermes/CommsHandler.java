package hermes;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    // Max chunk size: 16 bits

    // ********** CHUNK SCHEMA **********

    final int OUR_ARCHON_BITS = 16; // 4 bits: status; 6 bits: x coordinate; 6 bits: y coordinate
    final int OUR_ARCHON_SLOTS = 4;
    final int OUR_ARCHON_OFFSET = 0;
    public class ArchonStatus {
        public static final int DEAD = 0;
        public static final int STANDBY = 1;
        public static final int UNDER_ATTACK = 2;
    }

    final int ENEMY_ARCHON_BITS = 13; // schema TBD
    final int ENEMY_ARCHON_SLOTS = 4;
    final int ENEMY_ARCHON_OFFSET = OUR_ARCHON_OFFSET + OUR_ARCHON_SLOTS;

    final int MAP_SYMMETRY_BITS = 2; // 2 bits: symmetry
    final int MAP_SYMMETRY_SLOTS = 1;
    final int MAP_SYMMETRY_OFFSET = ENEMY_ARCHON_OFFSET + ENEMY_ARCHON_SLOTS;
    public class MapSymmetry {
        public static final int UNKNOWN = 0;
        public static final int HORIZONTAL = 1;
        public static final int VERTICAL = 2;
        public static final int ROTATIONAL = 3;
    }

    final int CLUSTER_BITS = 5; // 2 bits: cluster control status; 3 bits: resource count.
    final int CLUSTER_SLOTS = 100;
    final int CLUSTER_OFFSET = MAP_SYMMETRY_OFFSET + MAP_SYMMETRY_SLOTS;
    public class ControlStatus {
        public static final int UNKNOWN = 0;
        public static final int OURS = 1;
        public static final int THEIRS = 2;
        public static final int EXPLORING = 3;
    }

    final int ARCHON_INSTRUCTION_BITS = 16; // schema TBD
    final int ARCHON_INSTRUCTION_SLOTS = 4;
    final int ARCHON_INSTRUCTION_OFFSET = CLUSTER_OFFSET + CLUSTER_SLOTS;

    final int COMBAT_CLUSTER_BITS = 7; // 7 bits: cluster index
    final int COMBAT_CLUSTER_SLOTS = 5;
    final int COMBAT_CLUSTER_OFFSET = ARCHON_INSTRUCTION_OFFSET + ARCHON_INSTRUCTION_SLOTS;

    final int EXPLORE_CLUSTER_BITS = 8; // 1 bit: claim status, 7 bits: cluster index
    final int EXPLORE_CLUSTER_SLOTS = 10;
    final int EXPLORE_CLUSTER_OFFSET = COMBAT_CLUSTER_OFFSET + COMBAT_CLUSTER_SLOTS;
    public class ClaimStatus {
        public static final int UNCLAIMED = 0;
        public static final int CLAIMED = 1;
    }

    final int MINE_CLUSTER_BITS = 10; // 3 bits: claim status, 7 bits: cluster index
    final int MINE_CLUSTER_SLOTS = 10;
    final int MINE_CLUSTER_OFFSET = EXPLORE_CLUSTER_OFFSET + EXPLORE_CLUSTER_SLOTS;

    int[] CHUNK_SIZES = {
        OUR_ARCHON_BITS, OUR_ARCHON_BITS, OUR_ARCHON_BITS, OUR_ARCHON_BITS,             // our 4 archons
        ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS,     // enemy 4 archons
        MAP_SYMMETRY_BITS,
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, // up to 100 6x6 clusters
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        ARCHON_INSTRUCTION_BITS, ARCHON_INSTRUCTION_BITS, ARCHON_INSTRUCTION_BITS, ARCHON_INSTRUCTION_BITS, 
        COMBAT_CLUSTER_BITS, COMBAT_CLUSTER_BITS, COMBAT_CLUSTER_BITS, COMBAT_CLUSTER_BITS, COMBAT_CLUSTER_BITS, // up to 5 active combat clusters
        EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, // up to 10 active exploration clusters
        EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, EXPLORE_CLUSTER_BITS, 
        MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, // up to 10 active mining clusters
        MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, MINE_CLUSTER_BITS, 
        // TODO: add more
    };
    int[] CHUNK_OFFSETS = new int[CHUNK_SIZES.length]; // TODO: precompute prefix sums of CHUNK_SIZES

    final int SHARED_ARRAY_ELEM_SIZE = 16;
    final int SHARED_ARRAY_ELEM_LOG2 = 4;
    final int MAX_SHARED_ARRAY_ELEM = 65535;

    final int UNDEFINED_CLUSTER_INDEX = 127;

    // for unit test only
    boolean unitTest = false;
    int[] sharedArray;

    public CommsHandler(RobotController rc) throws GameActionException {
        this.rc = rc;
        init();
        // System.out.println("Total bits used: " + (CHUNK_OFFSETS[CHUNK_OFFSETS.length-1] + CHUNK_SIZES[CHUNK_SIZES.length-1]));
    }

    public CommsHandler() throws GameActionException { // for unit test only
        unitTest = true;
        sharedArray = new int[GameConstants.SHARED_ARRAY_LENGTH];
        for (int j = 0; j < sharedArray.length; j++) {
            sharedArray[j] = 0;
        }
        init();
    }

    private void init() throws GameActionException {
        for (int i = 0; i < CHUNK_SIZES.length; i++) { // TODO: remove once we precompute CHUNK_OFFSETS
            CHUNK_OFFSETS[i] = (i == 0) ? 0 : CHUNK_OFFSETS[i-1] + CHUNK_SIZES[i-1];
        }
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

    /**
     * Returns the status of the given friendly archon, encoded as follows:
     * 0: archon does not exist; 1: archon alive and on standby; 2-15: TBD
     * 
     * @return status of the given archon
     * @param archonNum the archon number
     */
    public int readOurArchonStatus(int archonNum) throws GameActionException {
        return readChunkPortion(OUR_ARCHON_OFFSET + archonNum, 0, 4);
    }

    /**
     * Writes the status of the given friendly archon, encoded as follows:
     * 0: archon does not exist; 1: archon alive and on standby; 2-15: TBD
     * 
     * @return true if the write was successful
     * @param archonNum the archon number
     * @param status the status to write
     */
    public boolean writeOurArchonStatus(int archonNum, int status) throws GameActionException {
        return writeChunkPortion(status, OUR_ARCHON_OFFSET + archonNum, 0, 4);
    }

    /**
     * Returns the MapLocation of the specified friendly archon.
     *
     * @param archonNum the archon number
     * @return the MapLocation of the specified archon
     * @throws GameActionException
     */
    public MapLocation readOurArchonLocation(int archonNum) throws GameActionException {
        return new MapLocation(readChunkPortion(OUR_ARCHON_OFFSET + archonNum, 4, 6), readChunkPortion(OUR_ARCHON_OFFSET + archonNum, 10, 6));
    }

    /**
     * Writes the MapLocation of the specified friendly archon.
     *
     * @param archonNum the archon number
     * @param loc the MapLocation to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeOurArchonLocation(int archonNum, MapLocation loc) throws GameActionException {
        return writeChunkPortion(loc.x, OUR_ARCHON_OFFSET + archonNum, 4, 6) && writeChunkPortion(loc.y, OUR_ARCHON_OFFSET + archonNum, 10, 6);
    }

    /**
     * Returns the symmetry of the map, encoded via MapSymmetry enum.
     *
     * @return the symmetry of the map
     * @throws GameActionException
     */
    public int readMapSymmetry() throws GameActionException {
        return readChunkPortion(MAP_SYMMETRY_OFFSET, 0, 2);
    }

    /**
     * Writes the symmetry of the map, encoded via MapSymmetry enum.
     *
     * @param symmetry the symmetry to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeMapSymmetry(int symmetry) throws GameActionException {
        return writeChunkPortion(symmetry, MAP_SYMMETRY_OFFSET, 0, 2);
    }

    /**
     * Returns the cluster control status of the specified cluster, encoded via ControlStatus enum.
     *
     * @param clusterNum the cluster number
     * @return the cluster control status of the specified cluster
     * @throws GameActionException
     */
    public int readClusterControlStatus(int clusterIdx) throws GameActionException {
        return readChunkPortion(CLUSTER_OFFSET + clusterIdx, 0, 2);
    }

    /**
     * Writes the cluster control status of the specified cluster, encoded via ControlStatus enum.
     *
     * @param clusterNum the cluster number
     * @param status the cluster control status to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeClusterControlStatus(int clusterIdx, int status) throws GameActionException {
        return writeChunkPortion(status, CLUSTER_OFFSET + clusterIdx, 0, 2);
    }

    /**
     * Returns the number of resources in the specified cluster, encoded in the range [1, 7].
     * Returns 0 if the resource count is unknown.
     *
     * @param clusterNum the cluster number
     * @return the encoded number of resources in the specified cluster
     * @throws GameActionException
     */
    public int readClusterResourceCount(int clusterIdx) throws GameActionException {
        return readChunkPortion(CLUSTER_OFFSET + clusterIdx, 2, 3);
    }

    /**
     * Writes the number of resources in the specified cluster, encoded in the range [1, 7].
     * Returns 0 if the resource count is unknown.
     * 
     * @param clusterNum the cluster number
     * @param count the encoded number of resources to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeClusterResourceCount(int clusterIdx, int count) throws GameActionException {
        return writeChunkPortion(count, CLUSTER_OFFSET + clusterIdx, 2, 3);
    }

    /**
     * Returns the cluster index of the specified combat cluster.
     * Returns UNDEFINED_CLUSTER_INDEX if the combat cluster is not specified.
     * 
     * @param combatClusterIndex the combat cluster index, in the range [0, COMBAT_CLUSTER_SLOTS - 1]
     * @return the cluster index of the specified combat cluster
     * @throws GameActionException
     */
    public int readCombatClusterIndex(int combatClusterIndex) throws GameActionException {
        return readChunkPortion(COMBAT_CLUSTER_OFFSET + combatClusterIndex, 0, 7);
    }

    /**
     * Writes the cluster index of the specified combat cluster.
     * 
     * @param combatClusterIndex the combat cluster index, in the range [0, COMBAT_CLUSTER_SLOTS - 1]
     * @param clusterIndex the cluster index to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeCombatClusterIndex(int combatClusterIndex, int clusterIndex) throws GameActionException {
        return writeChunkPortion(clusterIndex, COMBAT_CLUSTER_OFFSET + combatClusterIndex, 0, 7);
    }

    /**
     * Returns the claim status of the specified exploration cluster; 0: unclaimed, 1: claimed.
     * 
     * @param exploreClusterIndex the exploration cluster index, in the range [0, EXPLORE_CLUSTER_SLOTS - 1]
     * @return the claim status of the specified exploration cluster
     * @throws GameActionException
     */
    public int readExploreClusterClaimStatus(int exploreClusterIndex) throws GameActionException {
        return readChunkPortion(EXPLORE_CLUSTER_OFFSET + exploreClusterIndex, 0, 1);
    }

    /**
     * Writes the claim status of the specified exploration cluster; 0: unclaimed, 1: claimed.
     * 
     * @param exploreClusterIndex the exploration cluster index, in the range [0, EXPLORE_CLUSTER_SLOTS - 1]
     * @param claimStatus the claim status to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeExploreClusterClaimStatus(int exploreClusterIndex, int claimStatus) throws GameActionException {
        return writeChunkPortion(claimStatus, EXPLORE_CLUSTER_OFFSET + exploreClusterIndex, 0, 1);
    }

    /** 
     * Returns the cluster index of the specified exploration cluster.
     * Returns UNDEFINED_CLUSTER_INDEX if the exploration cluster is not specified.
     * 
     * @param exploreClusterIndex the exploration cluster index, in the range [0, EXPLORE_CLUSTER_SLOTS - 1]
     * @return the cluster index of the specified exploration cluster
     * @throws GameActionException
     */
    public int readExploreClusterIndex(int exploreClusterIndex) throws GameActionException {
        return readChunkPortion(EXPLORE_CLUSTER_OFFSET + exploreClusterIndex, 1, 7);
    }

    /**
     * Writes the cluster index of the specified exploration cluster.
     * 
     * @param exploreClusterIndex the exploration cluster index, in the range [0, EXPLORE_CLUSTER_SLOTS - 1]
     * @param clusterIndex the cluster index to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeExploreClusterIndex(int exploreClusterIndex, int clusterIndex) throws GameActionException {
        return writeChunkPortion(clusterIndex, EXPLORE_CLUSTER_OFFSET + exploreClusterIndex, 1, 7);
    }

    /** 
     * Returns the claim status of the specified mining cluster, in the range [0, 7].
     * 
     * @param mineClusterIndex the mining cluster index, in the range [0, MINE_CLUSTER_SLOTS - 1]
     * @return the claim status of the specified mining cluster
     * @throws GameActionException
     */
    public int readMineClusterClaimStatus(int mineClusterIndex) throws GameActionException {
        return readChunkPortion(MINE_CLUSTER_OFFSET + mineClusterIndex, 0, 3);
    }

    /**
     * Writes the claim status of the specified mining cluster, in the range [0, 7].
     * 
     * @param mineClusterIndex the mining cluster index, in the range [0, MINE_CLUSTER_SLOTS - 1]
     * @param claimStatus the claim status to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeMineClusterClaimStatus(int mineClusterIndex, int claimStatus) throws GameActionException {
        return writeChunkPortion(claimStatus, MINE_CLUSTER_OFFSET + mineClusterIndex, 0, 3);
    }

    /**
     * Returns the cluster index of the specified mining cluster.
     * Returns UNDEFINED_CLUSTER_INDEX if the mining cluster is not specified.
     * 
     * @param mineClusterIndex the mining cluster index, in the range [0, MINE_CLUSTER_SLOTS - 1]
     * @return the cluster index of the specified mining cluster
     * @throws GameActionException
     */
    public int readMineClusterIndex(int mineClusterIndex) throws GameActionException {
        return readChunkPortion(MINE_CLUSTER_OFFSET + mineClusterIndex, 3, 7);
    }

    /**
     * Writes the cluster index of the specified mining cluster.
     * 
     * @param mineClusterIndex the mining cluster index, in the range [0, MINE_CLUSTER_SLOTS - 1]
     * @param clusterIndex the cluster index to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeMineClusterIndex(int mineClusterIndex, int clusterIndex) throws GameActionException {
        return writeChunkPortion(clusterIndex, MINE_CLUSTER_OFFSET + mineClusterIndex, 3, 7);
    }

    private int readChunk(int chunkIndex) throws GameActionException { // Implements lazy reading from the main shared array
        return read(CHUNK_OFFSETS[chunkIndex], CHUNK_SIZES[chunkIndex]);
    }

    private boolean writeChunk(int chunkIndex, int value) throws GameActionException {
        return write(value, CHUNK_OFFSETS[chunkIndex], CHUNK_SIZES[chunkIndex]);
    }

    private int readChunkPortion(int chunkIndex, int beginBit, int numBits) throws GameActionException { // Implements lazy reading from the main shared array
        return read(CHUNK_OFFSETS[chunkIndex] + beginBit, numBits);
    }

    private boolean writeChunkPortion(int value, int chunkIndex, int beginBit, int numBits) throws GameActionException {
        return write(value, CHUNK_OFFSETS[chunkIndex] + beginBit, numBits);
    }

    // TODO: after unit tests pass, remove this and replace all readSharedArray with rc.readSharedArray to save bytecode
    private int readSharedArray(int index) throws GameActionException {
        if (unitTest) {
            return sharedArray[index];
        }
        return rc.readSharedArray(index);
    }

    // TODO: after unit tests pass, remove this and replace all writeSharedArray with rc.writeSharedArray to save bytecode
    private void writeSharedArray(int index, int value) throws GameActionException {
        if (unitTest) {
            sharedArray[index] = value & MAX_SHARED_ARRAY_ELEM;
        } else {
            rc.writeSharedArray(index, value & MAX_SHARED_ARRAY_ELEM);
        }
    }

    /*
     * Low-level read and write methods based on bit masking.
     * Reading and writing are supported for any number of length 0-SHARED_ARRAY_ELEM_SIZE bits (inclusive)
     * Takes constant time regardless of length of number written.
     */

    //Can only write numbers of length 0 to SHARED_ARRAY_ELEM_SIZE
    //It is up to the caller to provide enough bits to write the number
    //Otherwise, the function will not work. It will only write the first numBits
    //digits.
    //If providing a number with excess bits (value >> 2^numBits), the number will be
    //at the right end of the slot (the excess bits will be turned into leading zeros).
    public boolean write(int value, int beginBit, int numBits) throws GameActionException {
        int arrIndexStart = (beginBit)>>>SHARED_ARRAY_ELEM_LOG2;
        int sumMinusOne = numBits + beginBit - 1;
        int arrIndexEnd = sumMinusOne>>>SHARED_ARRAY_ELEM_LOG2;

        // integerBitBegin and integerBitEnd will be set in the switch statements, in-line for speedup
        // int integerBitBegin = whichBit2(arrIndexStart, beginBit);
        // int integerBitEnd = whichBit2(arrIndexEnd, sumMinusOne);
        int integerBitBegin = 0;
        int integerBitEnd = 0;
        int bit1 = beginBit + arrIndexStart*2000;
        int bit2 = sumMinusOne + arrIndexEnd*2000;
        switch (bit1) {
            case 0: integerBitBegin = 0; break;
            case 1: integerBitBegin = 1; break;
            case 2: integerBitBegin = 2; break;
            case 3: integerBitBegin = 3; break;
            case 4: integerBitBegin = 4; break;
            case 5: integerBitBegin = 5; break;
            case 6: integerBitBegin = 6; break;
            case 7: integerBitBegin = 7; break;
            case 8: integerBitBegin = 8; break;
            case 9: integerBitBegin = 9; break;
            case 10: integerBitBegin = 10; break;
            case 11: integerBitBegin = 11; break;
            case 12: integerBitBegin = 12; break;
            case 13: integerBitBegin = 13; break;
            case 14: integerBitBegin = 14; break;
            case 15: integerBitBegin = 15; break;
            case 2016: integerBitBegin = 0; break;
            case 2017: integerBitBegin = 1; break;
            case 2018: integerBitBegin = 2; break;
            case 2019: integerBitBegin = 3; break;
            case 2020: integerBitBegin = 4; break;
            case 2021: integerBitBegin = 5; break;
            case 2022: integerBitBegin = 6; break;
            case 2023: integerBitBegin = 7; break;
            case 2024: integerBitBegin = 8; break;
            case 2025: integerBitBegin = 9; break;
            case 2026: integerBitBegin = 10; break;
            case 2027: integerBitBegin = 11; break;
            case 2028: integerBitBegin = 12; break;
            case 2029: integerBitBegin = 13; break;
            case 2030: integerBitBegin = 14; break;
            case 2031: integerBitBegin = 15; break;
            case 4032: integerBitBegin = 0; break;
            case 4033: integerBitBegin = 1; break;
            case 4034: integerBitBegin = 2; break;
            case 4035: integerBitBegin = 3; break;
            case 4036: integerBitBegin = 4; break;
            case 4037: integerBitBegin = 5; break;
            case 4038: integerBitBegin = 6; break;
            case 4039: integerBitBegin = 7; break;
            case 4040: integerBitBegin = 8; break;
            case 4041: integerBitBegin = 9; break;
            case 4042: integerBitBegin = 10; break;
            case 4043: integerBitBegin = 11; break;
            case 4044: integerBitBegin = 12; break;
            case 4045: integerBitBegin = 13; break;
            case 4046: integerBitBegin = 14; break;
            case 4047: integerBitBegin = 15; break;
            case 6048: integerBitBegin = 0; break;
            case 6049: integerBitBegin = 1; break;
            case 6050: integerBitBegin = 2; break;
            case 6051: integerBitBegin = 3; break;
            case 6052: integerBitBegin = 4; break;
            case 6053: integerBitBegin = 5; break;
            case 6054: integerBitBegin = 6; break;
            case 6055: integerBitBegin = 7; break;
            case 6056: integerBitBegin = 8; break;
            case 6057: integerBitBegin = 9; break;
            case 6058: integerBitBegin = 10; break;
            case 6059: integerBitBegin = 11; break;
            case 6060: integerBitBegin = 12; break;
            case 6061: integerBitBegin = 13; break;
            case 6062: integerBitBegin = 14; break;
            case 6063: integerBitBegin = 15; break;
            case 8064: integerBitBegin = 0; break;
            case 8065: integerBitBegin = 1; break;
            case 8066: integerBitBegin = 2; break;
            case 8067: integerBitBegin = 3; break;
            case 8068: integerBitBegin = 4; break;
            case 8069: integerBitBegin = 5; break;
            case 8070: integerBitBegin = 6; break;
            case 8071: integerBitBegin = 7; break;
            case 8072: integerBitBegin = 8; break;
            case 8073: integerBitBegin = 9; break;
            case 8074: integerBitBegin = 10; break;
            case 8075: integerBitBegin = 11; break;
            case 8076: integerBitBegin = 12; break;
            case 8077: integerBitBegin = 13; break;
            case 8078: integerBitBegin = 14; break;
            case 8079: integerBitBegin = 15; break;
            case 10080: integerBitBegin = 0; break;
            case 10081: integerBitBegin = 1; break;
            case 10082: integerBitBegin = 2; break;
            case 10083: integerBitBegin = 3; break;
            case 10084: integerBitBegin = 4; break;
            case 10085: integerBitBegin = 5; break;
            case 10086: integerBitBegin = 6; break;
            case 10087: integerBitBegin = 7; break;
            case 10088: integerBitBegin = 8; break;
            case 10089: integerBitBegin = 9; break;
            case 10090: integerBitBegin = 10; break;
            case 10091: integerBitBegin = 11; break;
            case 10092: integerBitBegin = 12; break;
            case 10093: integerBitBegin = 13; break;
            case 10094: integerBitBegin = 14; break;
            case 10095: integerBitBegin = 15; break;
            case 12096: integerBitBegin = 0; break;
            case 12097: integerBitBegin = 1; break;
            case 12098: integerBitBegin = 2; break;
            case 12099: integerBitBegin = 3; break;
            case 12100: integerBitBegin = 4; break;
            case 12101: integerBitBegin = 5; break;
            case 12102: integerBitBegin = 6; break;
            case 12103: integerBitBegin = 7; break;
            case 12104: integerBitBegin = 8; break;
            case 12105: integerBitBegin = 9; break;
            case 12106: integerBitBegin = 10; break;
            case 12107: integerBitBegin = 11; break;
            case 12108: integerBitBegin = 12; break;
            case 12109: integerBitBegin = 13; break;
            case 12110: integerBitBegin = 14; break;
            case 12111: integerBitBegin = 15; break;
            case 14112: integerBitBegin = 0; break;
            case 14113: integerBitBegin = 1; break;
            case 14114: integerBitBegin = 2; break;
            case 14115: integerBitBegin = 3; break;
            case 14116: integerBitBegin = 4; break;
            case 14117: integerBitBegin = 5; break;
            case 14118: integerBitBegin = 6; break;
            case 14119: integerBitBegin = 7; break;
            case 14120: integerBitBegin = 8; break;
            case 14121: integerBitBegin = 9; break;
            case 14122: integerBitBegin = 10; break;
            case 14123: integerBitBegin = 11; break;
            case 14124: integerBitBegin = 12; break;
            case 14125: integerBitBegin = 13; break;
            case 14126: integerBitBegin = 14; break;
            case 14127: integerBitBegin = 15; break;
            case 16128: integerBitBegin = 0; break;
            case 16129: integerBitBegin = 1; break;
            case 16130: integerBitBegin = 2; break;
            case 16131: integerBitBegin = 3; break;
            case 16132: integerBitBegin = 4; break;
            case 16133: integerBitBegin = 5; break;
            case 16134: integerBitBegin = 6; break;
            case 16135: integerBitBegin = 7; break;
            case 16136: integerBitBegin = 8; break;
            case 16137: integerBitBegin = 9; break;
            case 16138: integerBitBegin = 10; break;
            case 16139: integerBitBegin = 11; break;
            case 16140: integerBitBegin = 12; break;
            case 16141: integerBitBegin = 13; break;
            case 16142: integerBitBegin = 14; break;
            case 16143: integerBitBegin = 15; break;
            case 18144: integerBitBegin = 0; break;
            case 18145: integerBitBegin = 1; break;
            case 18146: integerBitBegin = 2; break;
            case 18147: integerBitBegin = 3; break;
            case 18148: integerBitBegin = 4; break;
            case 18149: integerBitBegin = 5; break;
            case 18150: integerBitBegin = 6; break;
            case 18151: integerBitBegin = 7; break;
            case 18152: integerBitBegin = 8; break;
            case 18153: integerBitBegin = 9; break;
            case 18154: integerBitBegin = 10; break;
            case 18155: integerBitBegin = 11; break;
            case 18156: integerBitBegin = 12; break;
            case 18157: integerBitBegin = 13; break;
            case 18158: integerBitBegin = 14; break;
            case 18159: integerBitBegin = 15; break;
            case 20160: integerBitBegin = 0; break;
            case 20161: integerBitBegin = 1; break;
            case 20162: integerBitBegin = 2; break;
            case 20163: integerBitBegin = 3; break;
            case 20164: integerBitBegin = 4; break;
            case 20165: integerBitBegin = 5; break;
            case 20166: integerBitBegin = 6; break;
            case 20167: integerBitBegin = 7; break;
            case 20168: integerBitBegin = 8; break;
            case 20169: integerBitBegin = 9; break;
            case 20170: integerBitBegin = 10; break;
            case 20171: integerBitBegin = 11; break;
            case 20172: integerBitBegin = 12; break;
            case 20173: integerBitBegin = 13; break;
            case 20174: integerBitBegin = 14; break;
            case 20175: integerBitBegin = 15; break;
            case 22176: integerBitBegin = 0; break;
            case 22177: integerBitBegin = 1; break;
            case 22178: integerBitBegin = 2; break;
            case 22179: integerBitBegin = 3; break;
            case 22180: integerBitBegin = 4; break;
            case 22181: integerBitBegin = 5; break;
            case 22182: integerBitBegin = 6; break;
            case 22183: integerBitBegin = 7; break;
            case 22184: integerBitBegin = 8; break;
            case 22185: integerBitBegin = 9; break;
            case 22186: integerBitBegin = 10; break;
            case 22187: integerBitBegin = 11; break;
            case 22188: integerBitBegin = 12; break;
            case 22189: integerBitBegin = 13; break;
            case 22190: integerBitBegin = 14; break;
            case 22191: integerBitBegin = 15; break;
            case 24192: integerBitBegin = 0; break;
            case 24193: integerBitBegin = 1; break;
            case 24194: integerBitBegin = 2; break;
            case 24195: integerBitBegin = 3; break;
            case 24196: integerBitBegin = 4; break;
            case 24197: integerBitBegin = 5; break;
            case 24198: integerBitBegin = 6; break;
            case 24199: integerBitBegin = 7; break;
            case 24200: integerBitBegin = 8; break;
            case 24201: integerBitBegin = 9; break;
            case 24202: integerBitBegin = 10; break;
            case 24203: integerBitBegin = 11; break;
            case 24204: integerBitBegin = 12; break;
            case 24205: integerBitBegin = 13; break;
            case 24206: integerBitBegin = 14; break;
            case 24207: integerBitBegin = 15; break;
            case 26208: integerBitBegin = 0; break;
            case 26209: integerBitBegin = 1; break;
            case 26210: integerBitBegin = 2; break;
            case 26211: integerBitBegin = 3; break;
            case 26212: integerBitBegin = 4; break;
            case 26213: integerBitBegin = 5; break;
            case 26214: integerBitBegin = 6; break;
            case 26215: integerBitBegin = 7; break;
            case 26216: integerBitBegin = 8; break;
            case 26217: integerBitBegin = 9; break;
            case 26218: integerBitBegin = 10; break;
            case 26219: integerBitBegin = 11; break;
            case 26220: integerBitBegin = 12; break;
            case 26221: integerBitBegin = 13; break;
            case 26222: integerBitBegin = 14; break;
            case 26223: integerBitBegin = 15; break;
            case 28224: integerBitBegin = 0; break;
            case 28225: integerBitBegin = 1; break;
            case 28226: integerBitBegin = 2; break;
            case 28227: integerBitBegin = 3; break;
            case 28228: integerBitBegin = 4; break;
            case 28229: integerBitBegin = 5; break;
            case 28230: integerBitBegin = 6; break;
            case 28231: integerBitBegin = 7; break;
            case 28232: integerBitBegin = 8; break;
            case 28233: integerBitBegin = 9; break;
            case 28234: integerBitBegin = 10; break;
            case 28235: integerBitBegin = 11; break;
            case 28236: integerBitBegin = 12; break;
            case 28237: integerBitBegin = 13; break;
            case 28238: integerBitBegin = 14; break;
            case 28239: integerBitBegin = 15; break;
            case 30240: integerBitBegin = 0; break;
            case 30241: integerBitBegin = 1; break;
            case 30242: integerBitBegin = 2; break;
            case 30243: integerBitBegin = 3; break;
            case 30244: integerBitBegin = 4; break;
            case 30245: integerBitBegin = 5; break;
            case 30246: integerBitBegin = 6; break;
            case 30247: integerBitBegin = 7; break;
            case 30248: integerBitBegin = 8; break;
            case 30249: integerBitBegin = 9; break;
            case 30250: integerBitBegin = 10; break;
            case 30251: integerBitBegin = 11; break;
            case 30252: integerBitBegin = 12; break;
            case 30253: integerBitBegin = 13; break;
            case 30254: integerBitBegin = 14; break;
            case 30255: integerBitBegin = 15; break;
            case 32256: integerBitBegin = 0; break;
            case 32257: integerBitBegin = 1; break;
            case 32258: integerBitBegin = 2; break;
            case 32259: integerBitBegin = 3; break;
            case 32260: integerBitBegin = 4; break;
            case 32261: integerBitBegin = 5; break;
            case 32262: integerBitBegin = 6; break;
            case 32263: integerBitBegin = 7; break;
            case 32264: integerBitBegin = 8; break;
            case 32265: integerBitBegin = 9; break;
            case 32266: integerBitBegin = 10; break;
            case 32267: integerBitBegin = 11; break;
            case 32268: integerBitBegin = 12; break;
            case 32269: integerBitBegin = 13; break;
            case 32270: integerBitBegin = 14; break;
            case 32271: integerBitBegin = 15; break;
            case 34272: integerBitBegin = 0; break;
            case 34273: integerBitBegin = 1; break;
            case 34274: integerBitBegin = 2; break;
            case 34275: integerBitBegin = 3; break;
            case 34276: integerBitBegin = 4; break;
            case 34277: integerBitBegin = 5; break;
            case 34278: integerBitBegin = 6; break;
            case 34279: integerBitBegin = 7; break;
            case 34280: integerBitBegin = 8; break;
            case 34281: integerBitBegin = 9; break;
            case 34282: integerBitBegin = 10; break;
            case 34283: integerBitBegin = 11; break;
            case 34284: integerBitBegin = 12; break;
            case 34285: integerBitBegin = 13; break;
            case 34286: integerBitBegin = 14; break;
            case 34287: integerBitBegin = 15; break;
            case 36288: integerBitBegin = 0; break;
            case 36289: integerBitBegin = 1; break;
            case 36290: integerBitBegin = 2; break;
            case 36291: integerBitBegin = 3; break;
            case 36292: integerBitBegin = 4; break;
            case 36293: integerBitBegin = 5; break;
            case 36294: integerBitBegin = 6; break;
            case 36295: integerBitBegin = 7; break;
            case 36296: integerBitBegin = 8; break;
            case 36297: integerBitBegin = 9; break;
            case 36298: integerBitBegin = 10; break;
            case 36299: integerBitBegin = 11; break;
            case 36300: integerBitBegin = 12; break;
            case 36301: integerBitBegin = 13; break;
            case 36302: integerBitBegin = 14; break;
            case 36303: integerBitBegin = 15; break;
            case 38304: integerBitBegin = 0; break;
            case 38305: integerBitBegin = 1; break;
            case 38306: integerBitBegin = 2; break;
            case 38307: integerBitBegin = 3; break;
            case 38308: integerBitBegin = 4; break;
            case 38309: integerBitBegin = 5; break;
            case 38310: integerBitBegin = 6; break;
            case 38311: integerBitBegin = 7; break;
            case 38312: integerBitBegin = 8; break;
            case 38313: integerBitBegin = 9; break;
            case 38314: integerBitBegin = 10; break;
            case 38315: integerBitBegin = 11; break;
            case 38316: integerBitBegin = 12; break;
            case 38317: integerBitBegin = 13; break;
            case 38318: integerBitBegin = 14; break;
            case 38319: integerBitBegin = 15; break;
            case 40320: integerBitBegin = 0; break;
            case 40321: integerBitBegin = 1; break;
            case 40322: integerBitBegin = 2; break;
            case 40323: integerBitBegin = 3; break;
            case 40324: integerBitBegin = 4; break;
            case 40325: integerBitBegin = 5; break;
            case 40326: integerBitBegin = 6; break;
            case 40327: integerBitBegin = 7; break;
            case 40328: integerBitBegin = 8; break;
            case 40329: integerBitBegin = 9; break;
            case 40330: integerBitBegin = 10; break;
            case 40331: integerBitBegin = 11; break;
            case 40332: integerBitBegin = 12; break;
            case 40333: integerBitBegin = 13; break;
            case 40334: integerBitBegin = 14; break;
            case 40335: integerBitBegin = 15; break;
            case 42336: integerBitBegin = 0; break;
            case 42337: integerBitBegin = 1; break;
            case 42338: integerBitBegin = 2; break;
            case 42339: integerBitBegin = 3; break;
            case 42340: integerBitBegin = 4; break;
            case 42341: integerBitBegin = 5; break;
            case 42342: integerBitBegin = 6; break;
            case 42343: integerBitBegin = 7; break;
            case 42344: integerBitBegin = 8; break;
            case 42345: integerBitBegin = 9; break;
            case 42346: integerBitBegin = 10; break;
            case 42347: integerBitBegin = 11; break;
            case 42348: integerBitBegin = 12; break;
            case 42349: integerBitBegin = 13; break;
            case 42350: integerBitBegin = 14; break;
            case 42351: integerBitBegin = 15; break;
            case 44352: integerBitBegin = 0; break;
            case 44353: integerBitBegin = 1; break;
            case 44354: integerBitBegin = 2; break;
            case 44355: integerBitBegin = 3; break;
            case 44356: integerBitBegin = 4; break;
            case 44357: integerBitBegin = 5; break;
            case 44358: integerBitBegin = 6; break;
            case 44359: integerBitBegin = 7; break;
            case 44360: integerBitBegin = 8; break;
            case 44361: integerBitBegin = 9; break;
            case 44362: integerBitBegin = 10; break;
            case 44363: integerBitBegin = 11; break;
            case 44364: integerBitBegin = 12; break;
            case 44365: integerBitBegin = 13; break;
            case 44366: integerBitBegin = 14; break;
            case 44367: integerBitBegin = 15; break;
            case 46368: integerBitBegin = 0; break;
            case 46369: integerBitBegin = 1; break;
            case 46370: integerBitBegin = 2; break;
            case 46371: integerBitBegin = 3; break;
            case 46372: integerBitBegin = 4; break;
            case 46373: integerBitBegin = 5; break;
            case 46374: integerBitBegin = 6; break;
            case 46375: integerBitBegin = 7; break;
            case 46376: integerBitBegin = 8; break;
            case 46377: integerBitBegin = 9; break;
            case 46378: integerBitBegin = 10; break;
            case 46379: integerBitBegin = 11; break;
            case 46380: integerBitBegin = 12; break;
            case 46381: integerBitBegin = 13; break;
            case 46382: integerBitBegin = 14; break;
            case 46383: integerBitBegin = 15; break;
            case 48384: integerBitBegin = 0; break;
            case 48385: integerBitBegin = 1; break;
            case 48386: integerBitBegin = 2; break;
            case 48387: integerBitBegin = 3; break;
            case 48388: integerBitBegin = 4; break;
            case 48389: integerBitBegin = 5; break;
            case 48390: integerBitBegin = 6; break;
            case 48391: integerBitBegin = 7; break;
            case 48392: integerBitBegin = 8; break;
            case 48393: integerBitBegin = 9; break;
            case 48394: integerBitBegin = 10; break;
            case 48395: integerBitBegin = 11; break;
            case 48396: integerBitBegin = 12; break;
            case 48397: integerBitBegin = 13; break;
            case 48398: integerBitBegin = 14; break;
            case 48399: integerBitBegin = 15; break;
            case 50400: integerBitBegin = 0; break;
            case 50401: integerBitBegin = 1; break;
            case 50402: integerBitBegin = 2; break;
            case 50403: integerBitBegin = 3; break;
            case 50404: integerBitBegin = 4; break;
            case 50405: integerBitBegin = 5; break;
            case 50406: integerBitBegin = 6; break;
            case 50407: integerBitBegin = 7; break;
            case 50408: integerBitBegin = 8; break;
            case 50409: integerBitBegin = 9; break;
            case 50410: integerBitBegin = 10; break;
            case 50411: integerBitBegin = 11; break;
            case 50412: integerBitBegin = 12; break;
            case 50413: integerBitBegin = 13; break;
            case 50414: integerBitBegin = 14; break;
            case 50415: integerBitBegin = 15; break;
            case 52416: integerBitBegin = 0; break;
            case 52417: integerBitBegin = 1; break;
            case 52418: integerBitBegin = 2; break;
            case 52419: integerBitBegin = 3; break;
            case 52420: integerBitBegin = 4; break;
            case 52421: integerBitBegin = 5; break;
            case 52422: integerBitBegin = 6; break;
            case 52423: integerBitBegin = 7; break;
            case 52424: integerBitBegin = 8; break;
            case 52425: integerBitBegin = 9; break;
            case 52426: integerBitBegin = 10; break;
            case 52427: integerBitBegin = 11; break;
            case 52428: integerBitBegin = 12; break;
            case 52429: integerBitBegin = 13; break;
            case 52430: integerBitBegin = 14; break;
            case 52431: integerBitBegin = 15; break;
            case 54432: integerBitBegin = 0; break;
            case 54433: integerBitBegin = 1; break;
            case 54434: integerBitBegin = 2; break;
            case 54435: integerBitBegin = 3; break;
            case 54436: integerBitBegin = 4; break;
            case 54437: integerBitBegin = 5; break;
            case 54438: integerBitBegin = 6; break;
            case 54439: integerBitBegin = 7; break;
            case 54440: integerBitBegin = 8; break;
            case 54441: integerBitBegin = 9; break;
            case 54442: integerBitBegin = 10; break;
            case 54443: integerBitBegin = 11; break;
            case 54444: integerBitBegin = 12; break;
            case 54445: integerBitBegin = 13; break;
            case 54446: integerBitBegin = 14; break;
            case 54447: integerBitBegin = 15; break;
            case 56448: integerBitBegin = 0; break;
            case 56449: integerBitBegin = 1; break;
            case 56450: integerBitBegin = 2; break;
            case 56451: integerBitBegin = 3; break;
            case 56452: integerBitBegin = 4; break;
            case 56453: integerBitBegin = 5; break;
            case 56454: integerBitBegin = 6; break;
            case 56455: integerBitBegin = 7; break;
            case 56456: integerBitBegin = 8; break;
            case 56457: integerBitBegin = 9; break;
            case 56458: integerBitBegin = 10; break;
            case 56459: integerBitBegin = 11; break;
            case 56460: integerBitBegin = 12; break;
            case 56461: integerBitBegin = 13; break;
            case 56462: integerBitBegin = 14; break;
            case 56463: integerBitBegin = 15; break;
            case 58464: integerBitBegin = 0; break;
            case 58465: integerBitBegin = 1; break;
            case 58466: integerBitBegin = 2; break;
            case 58467: integerBitBegin = 3; break;
            case 58468: integerBitBegin = 4; break;
            case 58469: integerBitBegin = 5; break;
            case 58470: integerBitBegin = 6; break;
            case 58471: integerBitBegin = 7; break;
            case 58472: integerBitBegin = 8; break;
            case 58473: integerBitBegin = 9; break;
            case 58474: integerBitBegin = 10; break;
            case 58475: integerBitBegin = 11; break;
            case 58476: integerBitBegin = 12; break;
            case 58477: integerBitBegin = 13; break;
            case 58478: integerBitBegin = 14; break;
            case 58479: integerBitBegin = 15; break;
            case 60480: integerBitBegin = 0; break;
            case 60481: integerBitBegin = 1; break;
            case 60482: integerBitBegin = 2; break;
            case 60483: integerBitBegin = 3; break;
            case 60484: integerBitBegin = 4; break;
            case 60485: integerBitBegin = 5; break;
            case 60486: integerBitBegin = 6; break;
            case 60487: integerBitBegin = 7; break;
            case 60488: integerBitBegin = 8; break;
            case 60489: integerBitBegin = 9; break;
            case 60490: integerBitBegin = 10; break;
            case 60491: integerBitBegin = 11; break;
            case 60492: integerBitBegin = 12; break;
            case 60493: integerBitBegin = 13; break;
            case 60494: integerBitBegin = 14; break;
            case 60495: integerBitBegin = 15; break;
            case 62496: integerBitBegin = 0; break;
            case 62497: integerBitBegin = 1; break;
            case 62498: integerBitBegin = 2; break;
            case 62499: integerBitBegin = 3; break;
            case 62500: integerBitBegin = 4; break;
            case 62501: integerBitBegin = 5; break;
            case 62502: integerBitBegin = 6; break;
            case 62503: integerBitBegin = 7; break;
            case 62504: integerBitBegin = 8; break;
            case 62505: integerBitBegin = 9; break;
            case 62506: integerBitBegin = 10; break;
            case 62507: integerBitBegin = 11; break;
            case 62508: integerBitBegin = 12; break;
            case 62509: integerBitBegin = 13; break;
            case 62510: integerBitBegin = 14; break;
            case 62511: integerBitBegin = 15; break;
            case 64512: integerBitBegin = 0; break;
            case 64513: integerBitBegin = 1; break;
            case 64514: integerBitBegin = 2; break;
            case 64515: integerBitBegin = 3; break;
            case 64516: integerBitBegin = 4; break;
            case 64517: integerBitBegin = 5; break;
            case 64518: integerBitBegin = 6; break;
            case 64519: integerBitBegin = 7; break;
            case 64520: integerBitBegin = 8; break;
            case 64521: integerBitBegin = 9; break;
            case 64522: integerBitBegin = 10; break;
            case 64523: integerBitBegin = 11; break;
            case 64524: integerBitBegin = 12; break;
            case 64525: integerBitBegin = 13; break;
            case 64526: integerBitBegin = 14; break;
            case 64527: integerBitBegin = 15; break;
            case 66528: integerBitBegin = 0; break;
            case 66529: integerBitBegin = 1; break;
            case 66530: integerBitBegin = 2; break;
            case 66531: integerBitBegin = 3; break;
            case 66532: integerBitBegin = 4; break;
            case 66533: integerBitBegin = 5; break;
            case 66534: integerBitBegin = 6; break;
            case 66535: integerBitBegin = 7; break;
            case 66536: integerBitBegin = 8; break;
            case 66537: integerBitBegin = 9; break;
            case 66538: integerBitBegin = 10; break;
            case 66539: integerBitBegin = 11; break;
            case 66540: integerBitBegin = 12; break;
            case 66541: integerBitBegin = 13; break;
            case 66542: integerBitBegin = 14; break;
            case 66543: integerBitBegin = 15; break;
            case 68544: integerBitBegin = 0; break;
            case 68545: integerBitBegin = 1; break;
            case 68546: integerBitBegin = 2; break;
            case 68547: integerBitBegin = 3; break;
            case 68548: integerBitBegin = 4; break;
            case 68549: integerBitBegin = 5; break;
            case 68550: integerBitBegin = 6; break;
            case 68551: integerBitBegin = 7; break;
            case 68552: integerBitBegin = 8; break;
            case 68553: integerBitBegin = 9; break;
            case 68554: integerBitBegin = 10; break;
            case 68555: integerBitBegin = 11; break;
            case 68556: integerBitBegin = 12; break;
            case 68557: integerBitBegin = 13; break;
            case 68558: integerBitBegin = 14; break;
            case 68559: integerBitBegin = 15; break;
            case 70560: integerBitBegin = 0; break;
            case 70561: integerBitBegin = 1; break;
            case 70562: integerBitBegin = 2; break;
            case 70563: integerBitBegin = 3; break;
            case 70564: integerBitBegin = 4; break;
            case 70565: integerBitBegin = 5; break;
            case 70566: integerBitBegin = 6; break;
            case 70567: integerBitBegin = 7; break;
            case 70568: integerBitBegin = 8; break;
            case 70569: integerBitBegin = 9; break;
            case 70570: integerBitBegin = 10; break;
            case 70571: integerBitBegin = 11; break;
            case 70572: integerBitBegin = 12; break;
            case 70573: integerBitBegin = 13; break;
            case 70574: integerBitBegin = 14; break;
            case 70575: integerBitBegin = 15; break;
            case 72576: integerBitBegin = 0; break;
            case 72577: integerBitBegin = 1; break;
            case 72578: integerBitBegin = 2; break;
            case 72579: integerBitBegin = 3; break;
            case 72580: integerBitBegin = 4; break;
            case 72581: integerBitBegin = 5; break;
            case 72582: integerBitBegin = 6; break;
            case 72583: integerBitBegin = 7; break;
            case 72584: integerBitBegin = 8; break;
            case 72585: integerBitBegin = 9; break;
            case 72586: integerBitBegin = 10; break;
            case 72587: integerBitBegin = 11; break;
            case 72588: integerBitBegin = 12; break;
            case 72589: integerBitBegin = 13; break;
            case 72590: integerBitBegin = 14; break;
            case 72591: integerBitBegin = 15; break;
            case 74592: integerBitBegin = 0; break;
            case 74593: integerBitBegin = 1; break;
            case 74594: integerBitBegin = 2; break;
            case 74595: integerBitBegin = 3; break;
            case 74596: integerBitBegin = 4; break;
            case 74597: integerBitBegin = 5; break;
            case 74598: integerBitBegin = 6; break;
            case 74599: integerBitBegin = 7; break;
            case 74600: integerBitBegin = 8; break;
            case 74601: integerBitBegin = 9; break;
            case 74602: integerBitBegin = 10; break;
            case 74603: integerBitBegin = 11; break;
            case 74604: integerBitBegin = 12; break;
            case 74605: integerBitBegin = 13; break;
            case 74606: integerBitBegin = 14; break;
            case 74607: integerBitBegin = 15; break;
            case 76608: integerBitBegin = 0; break;
            case 76609: integerBitBegin = 1; break;
            case 76610: integerBitBegin = 2; break;
            case 76611: integerBitBegin = 3; break;
            case 76612: integerBitBegin = 4; break;
            case 76613: integerBitBegin = 5; break;
            case 76614: integerBitBegin = 6; break;
            case 76615: integerBitBegin = 7; break;
            case 76616: integerBitBegin = 8; break;
            case 76617: integerBitBegin = 9; break;
            case 76618: integerBitBegin = 10; break;
            case 76619: integerBitBegin = 11; break;
            case 76620: integerBitBegin = 12; break;
            case 76621: integerBitBegin = 13; break;
            case 76622: integerBitBegin = 14; break;
            case 76623: integerBitBegin = 15; break;
            case 78624: integerBitBegin = 0; break;
            case 78625: integerBitBegin = 1; break;
            case 78626: integerBitBegin = 2; break;
            case 78627: integerBitBegin = 3; break;
            case 78628: integerBitBegin = 4; break;
            case 78629: integerBitBegin = 5; break;
            case 78630: integerBitBegin = 6; break;
            case 78631: integerBitBegin = 7; break;
            case 78632: integerBitBegin = 8; break;
            case 78633: integerBitBegin = 9; break;
            case 78634: integerBitBegin = 10; break;
            case 78635: integerBitBegin = 11; break;
            case 78636: integerBitBegin = 12; break;
            case 78637: integerBitBegin = 13; break;
            case 78638: integerBitBegin = 14; break;
            case 78639: integerBitBegin = 15; break;
            case 80640: integerBitBegin = 0; break;
            case 80641: integerBitBegin = 1; break;
            case 80642: integerBitBegin = 2; break;
            case 80643: integerBitBegin = 3; break;
            case 80644: integerBitBegin = 4; break;
            case 80645: integerBitBegin = 5; break;
            case 80646: integerBitBegin = 6; break;
            case 80647: integerBitBegin = 7; break;
            case 80648: integerBitBegin = 8; break;
            case 80649: integerBitBegin = 9; break;
            case 80650: integerBitBegin = 10; break;
            case 80651: integerBitBegin = 11; break;
            case 80652: integerBitBegin = 12; break;
            case 80653: integerBitBegin = 13; break;
            case 80654: integerBitBegin = 14; break;
            case 80655: integerBitBegin = 15; break;
            case 82656: integerBitBegin = 0; break;
            case 82657: integerBitBegin = 1; break;
            case 82658: integerBitBegin = 2; break;
            case 82659: integerBitBegin = 3; break;
            case 82660: integerBitBegin = 4; break;
            case 82661: integerBitBegin = 5; break;
            case 82662: integerBitBegin = 6; break;
            case 82663: integerBitBegin = 7; break;
            case 82664: integerBitBegin = 8; break;
            case 82665: integerBitBegin = 9; break;
            case 82666: integerBitBegin = 10; break;
            case 82667: integerBitBegin = 11; break;
            case 82668: integerBitBegin = 12; break;
            case 82669: integerBitBegin = 13; break;
            case 82670: integerBitBegin = 14; break;
            case 82671: integerBitBegin = 15; break;
            case 84672: integerBitBegin = 0; break;
            case 84673: integerBitBegin = 1; break;
            case 84674: integerBitBegin = 2; break;
            case 84675: integerBitBegin = 3; break;
            case 84676: integerBitBegin = 4; break;
            case 84677: integerBitBegin = 5; break;
            case 84678: integerBitBegin = 6; break;
            case 84679: integerBitBegin = 7; break;
            case 84680: integerBitBegin = 8; break;
            case 84681: integerBitBegin = 9; break;
            case 84682: integerBitBegin = 10; break;
            case 84683: integerBitBegin = 11; break;
            case 84684: integerBitBegin = 12; break;
            case 84685: integerBitBegin = 13; break;
            case 84686: integerBitBegin = 14; break;
            case 84687: integerBitBegin = 15; break;
            case 86688: integerBitBegin = 0; break;
            case 86689: integerBitBegin = 1; break;
            case 86690: integerBitBegin = 2; break;
            case 86691: integerBitBegin = 3; break;
            case 86692: integerBitBegin = 4; break;
            case 86693: integerBitBegin = 5; break;
            case 86694: integerBitBegin = 6; break;
            case 86695: integerBitBegin = 7; break;
            case 86696: integerBitBegin = 8; break;
            case 86697: integerBitBegin = 9; break;
            case 86698: integerBitBegin = 10; break;
            case 86699: integerBitBegin = 11; break;
            case 86700: integerBitBegin = 12; break;
            case 86701: integerBitBegin = 13; break;
            case 86702: integerBitBegin = 14; break;
            case 86703: integerBitBegin = 15; break;
            case 88704: integerBitBegin = 0; break;
            case 88705: integerBitBegin = 1; break;
            case 88706: integerBitBegin = 2; break;
            case 88707: integerBitBegin = 3; break;
            case 88708: integerBitBegin = 4; break;
            case 88709: integerBitBegin = 5; break;
            case 88710: integerBitBegin = 6; break;
            case 88711: integerBitBegin = 7; break;
            case 88712: integerBitBegin = 8; break;
            case 88713: integerBitBegin = 9; break;
            case 88714: integerBitBegin = 10; break;
            case 88715: integerBitBegin = 11; break;
            case 88716: integerBitBegin = 12; break;
            case 88717: integerBitBegin = 13; break;
            case 88718: integerBitBegin = 14; break;
            case 88719: integerBitBegin = 15; break;
            case 90720: integerBitBegin = 0; break;
            case 90721: integerBitBegin = 1; break;
            case 90722: integerBitBegin = 2; break;
            case 90723: integerBitBegin = 3; break;
            case 90724: integerBitBegin = 4; break;
            case 90725: integerBitBegin = 5; break;
            case 90726: integerBitBegin = 6; break;
            case 90727: integerBitBegin = 7; break;
            case 90728: integerBitBegin = 8; break;
            case 90729: integerBitBegin = 9; break;
            case 90730: integerBitBegin = 10; break;
            case 90731: integerBitBegin = 11; break;
            case 90732: integerBitBegin = 12; break;
            case 90733: integerBitBegin = 13; break;
            case 90734: integerBitBegin = 14; break;
            case 90735: integerBitBegin = 15; break;
            case 92736: integerBitBegin = 0; break;
            case 92737: integerBitBegin = 1; break;
            case 92738: integerBitBegin = 2; break;
            case 92739: integerBitBegin = 3; break;
            case 92740: integerBitBegin = 4; break;
            case 92741: integerBitBegin = 5; break;
            case 92742: integerBitBegin = 6; break;
            case 92743: integerBitBegin = 7; break;
            case 92744: integerBitBegin = 8; break;
            case 92745: integerBitBegin = 9; break;
            case 92746: integerBitBegin = 10; break;
            case 92747: integerBitBegin = 11; break;
            case 92748: integerBitBegin = 12; break;
            case 92749: integerBitBegin = 13; break;
            case 92750: integerBitBegin = 14; break;
            case 92751: integerBitBegin = 15; break;
            case 94752: integerBitBegin = 0; break;
            case 94753: integerBitBegin = 1; break;
            case 94754: integerBitBegin = 2; break;
            case 94755: integerBitBegin = 3; break;
            case 94756: integerBitBegin = 4; break;
            case 94757: integerBitBegin = 5; break;
            case 94758: integerBitBegin = 6; break;
            case 94759: integerBitBegin = 7; break;
            case 94760: integerBitBegin = 8; break;
            case 94761: integerBitBegin = 9; break;
            case 94762: integerBitBegin = 10; break;
            case 94763: integerBitBegin = 11; break;
            case 94764: integerBitBegin = 12; break;
            case 94765: integerBitBegin = 13; break;
            case 94766: integerBitBegin = 14; break;
            case 94767: integerBitBegin = 15; break;
            case 96768: integerBitBegin = 0; break;
            case 96769: integerBitBegin = 1; break;
            case 96770: integerBitBegin = 2; break;
            case 96771: integerBitBegin = 3; break;
            case 96772: integerBitBegin = 4; break;
            case 96773: integerBitBegin = 5; break;
            case 96774: integerBitBegin = 6; break;
            case 96775: integerBitBegin = 7; break;
            case 96776: integerBitBegin = 8; break;
            case 96777: integerBitBegin = 9; break;
            case 96778: integerBitBegin = 10; break;
            case 96779: integerBitBegin = 11; break;
            case 96780: integerBitBegin = 12; break;
            case 96781: integerBitBegin = 13; break;
            case 96782: integerBitBegin = 14; break;
            case 96783: integerBitBegin = 15; break;
            case 98784: integerBitBegin = 0; break;
            case 98785: integerBitBegin = 1; break;
            case 98786: integerBitBegin = 2; break;
            case 98787: integerBitBegin = 3; break;
            case 98788: integerBitBegin = 4; break;
            case 98789: integerBitBegin = 5; break;
            case 98790: integerBitBegin = 6; break;
            case 98791: integerBitBegin = 7; break;
            case 98792: integerBitBegin = 8; break;
            case 98793: integerBitBegin = 9; break;
            case 98794: integerBitBegin = 10; break;
            case 98795: integerBitBegin = 11; break;
            case 98796: integerBitBegin = 12; break;
            case 98797: integerBitBegin = 13; break;
            case 98798: integerBitBegin = 14; break;
            case 98799: integerBitBegin = 15; break;
            case 100800: integerBitBegin = 0; break;
            case 100801: integerBitBegin = 1; break;
            case 100802: integerBitBegin = 2; break;
            case 100803: integerBitBegin = 3; break;
            case 100804: integerBitBegin = 4; break;
            case 100805: integerBitBegin = 5; break;
            case 100806: integerBitBegin = 6; break;
            case 100807: integerBitBegin = 7; break;
            case 100808: integerBitBegin = 8; break;
            case 100809: integerBitBegin = 9; break;
            case 100810: integerBitBegin = 10; break;
            case 100811: integerBitBegin = 11; break;
            case 100812: integerBitBegin = 12; break;
            case 100813: integerBitBegin = 13; break;
            case 100814: integerBitBegin = 14; break;
            case 100815: integerBitBegin = 15; break;
            case 102816: integerBitBegin = 0; break;
            case 102817: integerBitBegin = 1; break;
            case 102818: integerBitBegin = 2; break;
            case 102819: integerBitBegin = 3; break;
            case 102820: integerBitBegin = 4; break;
            case 102821: integerBitBegin = 5; break;
            case 102822: integerBitBegin = 6; break;
            case 102823: integerBitBegin = 7; break;
            case 102824: integerBitBegin = 8; break;
            case 102825: integerBitBegin = 9; break;
            case 102826: integerBitBegin = 10; break;
            case 102827: integerBitBegin = 11; break;
            case 102828: integerBitBegin = 12; break;
            case 102829: integerBitBegin = 13; break;
            case 102830: integerBitBegin = 14; break;
            case 102831: integerBitBegin = 15; break;
            case 104832: integerBitBegin = 0; break;
            case 104833: integerBitBegin = 1; break;
            case 104834: integerBitBegin = 2; break;
            case 104835: integerBitBegin = 3; break;
            case 104836: integerBitBegin = 4; break;
            case 104837: integerBitBegin = 5; break;
            case 104838: integerBitBegin = 6; break;
            case 104839: integerBitBegin = 7; break;
            case 104840: integerBitBegin = 8; break;
            case 104841: integerBitBegin = 9; break;
            case 104842: integerBitBegin = 10; break;
            case 104843: integerBitBegin = 11; break;
            case 104844: integerBitBegin = 12; break;
            case 104845: integerBitBegin = 13; break;
            case 104846: integerBitBegin = 14; break;
            case 104847: integerBitBegin = 15; break;
            case 106848: integerBitBegin = 0; break;
            case 106849: integerBitBegin = 1; break;
            case 106850: integerBitBegin = 2; break;
            case 106851: integerBitBegin = 3; break;
            case 106852: integerBitBegin = 4; break;
            case 106853: integerBitBegin = 5; break;
            case 106854: integerBitBegin = 6; break;
            case 106855: integerBitBegin = 7; break;
            case 106856: integerBitBegin = 8; break;
            case 106857: integerBitBegin = 9; break;
            case 106858: integerBitBegin = 10; break;
            case 106859: integerBitBegin = 11; break;
            case 106860: integerBitBegin = 12; break;
            case 106861: integerBitBegin = 13; break;
            case 106862: integerBitBegin = 14; break;
            case 106863: integerBitBegin = 15; break;
            case 108864: integerBitBegin = 0; break;
            case 108865: integerBitBegin = 1; break;
            case 108866: integerBitBegin = 2; break;
            case 108867: integerBitBegin = 3; break;
            case 108868: integerBitBegin = 4; break;
            case 108869: integerBitBegin = 5; break;
            case 108870: integerBitBegin = 6; break;
            case 108871: integerBitBegin = 7; break;
            case 108872: integerBitBegin = 8; break;
            case 108873: integerBitBegin = 9; break;
            case 108874: integerBitBegin = 10; break;
            case 108875: integerBitBegin = 11; break;
            case 108876: integerBitBegin = 12; break;
            case 108877: integerBitBegin = 13; break;
            case 108878: integerBitBegin = 14; break;
            case 108879: integerBitBegin = 15; break;
            case 110880: integerBitBegin = 0; break;
            case 110881: integerBitBegin = 1; break;
            case 110882: integerBitBegin = 2; break;
            case 110883: integerBitBegin = 3; break;
            case 110884: integerBitBegin = 4; break;
            case 110885: integerBitBegin = 5; break;
            case 110886: integerBitBegin = 6; break;
            case 110887: integerBitBegin = 7; break;
            case 110888: integerBitBegin = 8; break;
            case 110889: integerBitBegin = 9; break;
            case 110890: integerBitBegin = 10; break;
            case 110891: integerBitBegin = 11; break;
            case 110892: integerBitBegin = 12; break;
            case 110893: integerBitBegin = 13; break;
            case 110894: integerBitBegin = 14; break;
            case 110895: integerBitBegin = 15; break;
            case 112896: integerBitBegin = 0; break;
            case 112897: integerBitBegin = 1; break;
            case 112898: integerBitBegin = 2; break;
            case 112899: integerBitBegin = 3; break;
            case 112900: integerBitBegin = 4; break;
            case 112901: integerBitBegin = 5; break;
            case 112902: integerBitBegin = 6; break;
            case 112903: integerBitBegin = 7; break;
            case 112904: integerBitBegin = 8; break;
            case 112905: integerBitBegin = 9; break;
            case 112906: integerBitBegin = 10; break;
            case 112907: integerBitBegin = 11; break;
            case 112908: integerBitBegin = 12; break;
            case 112909: integerBitBegin = 13; break;
            case 112910: integerBitBegin = 14; break;
            case 112911: integerBitBegin = 15; break;
            case 114912: integerBitBegin = 0; break;
            case 114913: integerBitBegin = 1; break;
            case 114914: integerBitBegin = 2; break;
            case 114915: integerBitBegin = 3; break;
            case 114916: integerBitBegin = 4; break;
            case 114917: integerBitBegin = 5; break;
            case 114918: integerBitBegin = 6; break;
            case 114919: integerBitBegin = 7; break;
            case 114920: integerBitBegin = 8; break;
            case 114921: integerBitBegin = 9; break;
            case 114922: integerBitBegin = 10; break;
            case 114923: integerBitBegin = 11; break;
            case 114924: integerBitBegin = 12; break;
            case 114925: integerBitBegin = 13; break;
            case 114926: integerBitBegin = 14; break;
            case 114927: integerBitBegin = 15; break;
            case 116928: integerBitBegin = 0; break;
            case 116929: integerBitBegin = 1; break;
            case 116930: integerBitBegin = 2; break;
            case 116931: integerBitBegin = 3; break;
            case 116932: integerBitBegin = 4; break;
            case 116933: integerBitBegin = 5; break;
            case 116934: integerBitBegin = 6; break;
            case 116935: integerBitBegin = 7; break;
            case 116936: integerBitBegin = 8; break;
            case 116937: integerBitBegin = 9; break;
            case 116938: integerBitBegin = 10; break;
            case 116939: integerBitBegin = 11; break;
            case 116940: integerBitBegin = 12; break;
            case 116941: integerBitBegin = 13; break;
            case 116942: integerBitBegin = 14; break;
            case 116943: integerBitBegin = 15; break;
            case 118944: integerBitBegin = 0; break;
            case 118945: integerBitBegin = 1; break;
            case 118946: integerBitBegin = 2; break;
            case 118947: integerBitBegin = 3; break;
            case 118948: integerBitBegin = 4; break;
            case 118949: integerBitBegin = 5; break;
            case 118950: integerBitBegin = 6; break;
            case 118951: integerBitBegin = 7; break;
            case 118952: integerBitBegin = 8; break;
            case 118953: integerBitBegin = 9; break;
            case 118954: integerBitBegin = 10; break;
            case 118955: integerBitBegin = 11; break;
            case 118956: integerBitBegin = 12; break;
            case 118957: integerBitBegin = 13; break;
            case 118958: integerBitBegin = 14; break;
            case 118959: integerBitBegin = 15; break;
            case 120960: integerBitBegin = 0; break;
            case 120961: integerBitBegin = 1; break;
            case 120962: integerBitBegin = 2; break;
            case 120963: integerBitBegin = 3; break;
            case 120964: integerBitBegin = 4; break;
            case 120965: integerBitBegin = 5; break;
            case 120966: integerBitBegin = 6; break;
            case 120967: integerBitBegin = 7; break;
            case 120968: integerBitBegin = 8; break;
            case 120969: integerBitBegin = 9; break;
            case 120970: integerBitBegin = 10; break;
            case 120971: integerBitBegin = 11; break;
            case 120972: integerBitBegin = 12; break;
            case 120973: integerBitBegin = 13; break;
            case 120974: integerBitBegin = 14; break;
            case 120975: integerBitBegin = 15; break;
            case 122976: integerBitBegin = 0; break;
            case 122977: integerBitBegin = 1; break;
            case 122978: integerBitBegin = 2; break;
            case 122979: integerBitBegin = 3; break;
            case 122980: integerBitBegin = 4; break;
            case 122981: integerBitBegin = 5; break;
            case 122982: integerBitBegin = 6; break;
            case 122983: integerBitBegin = 7; break;
            case 122984: integerBitBegin = 8; break;
            case 122985: integerBitBegin = 9; break;
            case 122986: integerBitBegin = 10; break;
            case 122987: integerBitBegin = 11; break;
            case 122988: integerBitBegin = 12; break;
            case 122989: integerBitBegin = 13; break;
            case 122990: integerBitBegin = 14; break;
            case 122991: integerBitBegin = 15; break;
            case 124992: integerBitBegin = 0; break;
            case 124993: integerBitBegin = 1; break;
            case 124994: integerBitBegin = 2; break;
            case 124995: integerBitBegin = 3; break;
            case 124996: integerBitBegin = 4; break;
            case 124997: integerBitBegin = 5; break;
            case 124998: integerBitBegin = 6; break;
            case 124999: integerBitBegin = 7; break;
            case 125000: integerBitBegin = 8; break;
            case 125001: integerBitBegin = 9; break;
            case 125002: integerBitBegin = 10; break;
            case 125003: integerBitBegin = 11; break;
            case 125004: integerBitBegin = 12; break;
            case 125005: integerBitBegin = 13; break;
            case 125006: integerBitBegin = 14; break;
            case 125007: integerBitBegin = 15; break;
            case 127008: integerBitBegin = 0; break;
            case 127009: integerBitBegin = 1; break;
            case 127010: integerBitBegin = 2; break;
            case 127011: integerBitBegin = 3; break;
            case 127012: integerBitBegin = 4; break;
            case 127013: integerBitBegin = 5; break;
            case 127014: integerBitBegin = 6; break;
            case 127015: integerBitBegin = 7; break;
            case 127016: integerBitBegin = 8; break;
            case 127017: integerBitBegin = 9; break;
            case 127018: integerBitBegin = 10; break;
            case 127019: integerBitBegin = 11; break;
            case 127020: integerBitBegin = 12; break;
            case 127021: integerBitBegin = 13; break;
            case 127022: integerBitBegin = 14; break;
            case 127023: integerBitBegin = 15; break;
        }       
        switch (bit2) {
            case 0: integerBitEnd = 0; break;
            case 1: integerBitEnd = 1; break;
            case 2: integerBitEnd = 2; break;
            case 3: integerBitEnd = 3; break;
            case 4: integerBitEnd = 4; break;
            case 5: integerBitEnd = 5; break;
            case 6: integerBitEnd = 6; break;
            case 7: integerBitEnd = 7; break;
            case 8: integerBitEnd = 8; break;
            case 9: integerBitEnd = 9; break;
            case 10: integerBitEnd = 10; break;
            case 11: integerBitEnd = 11; break;
            case 12: integerBitEnd = 12; break;
            case 13: integerBitEnd = 13; break;
            case 14: integerBitEnd = 14; break;
            case 15: integerBitEnd = 15; break;
            case 2016: integerBitEnd = 0; break;
            case 2017: integerBitEnd = 1; break;
            case 2018: integerBitEnd = 2; break;
            case 2019: integerBitEnd = 3; break;
            case 2020: integerBitEnd = 4; break;
            case 2021: integerBitEnd = 5; break;
            case 2022: integerBitEnd = 6; break;
            case 2023: integerBitEnd = 7; break;
            case 2024: integerBitEnd = 8; break;
            case 2025: integerBitEnd = 9; break;
            case 2026: integerBitEnd = 10; break;
            case 2027: integerBitEnd = 11; break;
            case 2028: integerBitEnd = 12; break;
            case 2029: integerBitEnd = 13; break;
            case 2030: integerBitEnd = 14; break;
            case 2031: integerBitEnd = 15; break;
            case 4032: integerBitEnd = 0; break;
            case 4033: integerBitEnd = 1; break;
            case 4034: integerBitEnd = 2; break;
            case 4035: integerBitEnd = 3; break;
            case 4036: integerBitEnd = 4; break;
            case 4037: integerBitEnd = 5; break;
            case 4038: integerBitEnd = 6; break;
            case 4039: integerBitEnd = 7; break;
            case 4040: integerBitEnd = 8; break;
            case 4041: integerBitEnd = 9; break;
            case 4042: integerBitEnd = 10; break;
            case 4043: integerBitEnd = 11; break;
            case 4044: integerBitEnd = 12; break;
            case 4045: integerBitEnd = 13; break;
            case 4046: integerBitEnd = 14; break;
            case 4047: integerBitEnd = 15; break;
            case 6048: integerBitEnd = 0; break;
            case 6049: integerBitEnd = 1; break;
            case 6050: integerBitEnd = 2; break;
            case 6051: integerBitEnd = 3; break;
            case 6052: integerBitEnd = 4; break;
            case 6053: integerBitEnd = 5; break;
            case 6054: integerBitEnd = 6; break;
            case 6055: integerBitEnd = 7; break;
            case 6056: integerBitEnd = 8; break;
            case 6057: integerBitEnd = 9; break;
            case 6058: integerBitEnd = 10; break;
            case 6059: integerBitEnd = 11; break;
            case 6060: integerBitEnd = 12; break;
            case 6061: integerBitEnd = 13; break;
            case 6062: integerBitEnd = 14; break;
            case 6063: integerBitEnd = 15; break;
            case 8064: integerBitEnd = 0; break;
            case 8065: integerBitEnd = 1; break;
            case 8066: integerBitEnd = 2; break;
            case 8067: integerBitEnd = 3; break;
            case 8068: integerBitEnd = 4; break;
            case 8069: integerBitEnd = 5; break;
            case 8070: integerBitEnd = 6; break;
            case 8071: integerBitEnd = 7; break;
            case 8072: integerBitEnd = 8; break;
            case 8073: integerBitEnd = 9; break;
            case 8074: integerBitEnd = 10; break;
            case 8075: integerBitEnd = 11; break;
            case 8076: integerBitEnd = 12; break;
            case 8077: integerBitEnd = 13; break;
            case 8078: integerBitEnd = 14; break;
            case 8079: integerBitEnd = 15; break;
            case 10080: integerBitEnd = 0; break;
            case 10081: integerBitEnd = 1; break;
            case 10082: integerBitEnd = 2; break;
            case 10083: integerBitEnd = 3; break;
            case 10084: integerBitEnd = 4; break;
            case 10085: integerBitEnd = 5; break;
            case 10086: integerBitEnd = 6; break;
            case 10087: integerBitEnd = 7; break;
            case 10088: integerBitEnd = 8; break;
            case 10089: integerBitEnd = 9; break;
            case 10090: integerBitEnd = 10; break;
            case 10091: integerBitEnd = 11; break;
            case 10092: integerBitEnd = 12; break;
            case 10093: integerBitEnd = 13; break;
            case 10094: integerBitEnd = 14; break;
            case 10095: integerBitEnd = 15; break;
            case 12096: integerBitEnd = 0; break;
            case 12097: integerBitEnd = 1; break;
            case 12098: integerBitEnd = 2; break;
            case 12099: integerBitEnd = 3; break;
            case 12100: integerBitEnd = 4; break;
            case 12101: integerBitEnd = 5; break;
            case 12102: integerBitEnd = 6; break;
            case 12103: integerBitEnd = 7; break;
            case 12104: integerBitEnd = 8; break;
            case 12105: integerBitEnd = 9; break;
            case 12106: integerBitEnd = 10; break;
            case 12107: integerBitEnd = 11; break;
            case 12108: integerBitEnd = 12; break;
            case 12109: integerBitEnd = 13; break;
            case 12110: integerBitEnd = 14; break;
            case 12111: integerBitEnd = 15; break;
            case 14112: integerBitEnd = 0; break;
            case 14113: integerBitEnd = 1; break;
            case 14114: integerBitEnd = 2; break;
            case 14115: integerBitEnd = 3; break;
            case 14116: integerBitEnd = 4; break;
            case 14117: integerBitEnd = 5; break;
            case 14118: integerBitEnd = 6; break;
            case 14119: integerBitEnd = 7; break;
            case 14120: integerBitEnd = 8; break;
            case 14121: integerBitEnd = 9; break;
            case 14122: integerBitEnd = 10; break;
            case 14123: integerBitEnd = 11; break;
            case 14124: integerBitEnd = 12; break;
            case 14125: integerBitEnd = 13; break;
            case 14126: integerBitEnd = 14; break;
            case 14127: integerBitEnd = 15; break;
            case 16128: integerBitEnd = 0; break;
            case 16129: integerBitEnd = 1; break;
            case 16130: integerBitEnd = 2; break;
            case 16131: integerBitEnd = 3; break;
            case 16132: integerBitEnd = 4; break;
            case 16133: integerBitEnd = 5; break;
            case 16134: integerBitEnd = 6; break;
            case 16135: integerBitEnd = 7; break;
            case 16136: integerBitEnd = 8; break;
            case 16137: integerBitEnd = 9; break;
            case 16138: integerBitEnd = 10; break;
            case 16139: integerBitEnd = 11; break;
            case 16140: integerBitEnd = 12; break;
            case 16141: integerBitEnd = 13; break;
            case 16142: integerBitEnd = 14; break;
            case 16143: integerBitEnd = 15; break;
            case 18144: integerBitEnd = 0; break;
            case 18145: integerBitEnd = 1; break;
            case 18146: integerBitEnd = 2; break;
            case 18147: integerBitEnd = 3; break;
            case 18148: integerBitEnd = 4; break;
            case 18149: integerBitEnd = 5; break;
            case 18150: integerBitEnd = 6; break;
            case 18151: integerBitEnd = 7; break;
            case 18152: integerBitEnd = 8; break;
            case 18153: integerBitEnd = 9; break;
            case 18154: integerBitEnd = 10; break;
            case 18155: integerBitEnd = 11; break;
            case 18156: integerBitEnd = 12; break;
            case 18157: integerBitEnd = 13; break;
            case 18158: integerBitEnd = 14; break;
            case 18159: integerBitEnd = 15; break;
            case 20160: integerBitEnd = 0; break;
            case 20161: integerBitEnd = 1; break;
            case 20162: integerBitEnd = 2; break;
            case 20163: integerBitEnd = 3; break;
            case 20164: integerBitEnd = 4; break;
            case 20165: integerBitEnd = 5; break;
            case 20166: integerBitEnd = 6; break;
            case 20167: integerBitEnd = 7; break;
            case 20168: integerBitEnd = 8; break;
            case 20169: integerBitEnd = 9; break;
            case 20170: integerBitEnd = 10; break;
            case 20171: integerBitEnd = 11; break;
            case 20172: integerBitEnd = 12; break;
            case 20173: integerBitEnd = 13; break;
            case 20174: integerBitEnd = 14; break;
            case 20175: integerBitEnd = 15; break;
            case 22176: integerBitEnd = 0; break;
            case 22177: integerBitEnd = 1; break;
            case 22178: integerBitEnd = 2; break;
            case 22179: integerBitEnd = 3; break;
            case 22180: integerBitEnd = 4; break;
            case 22181: integerBitEnd = 5; break;
            case 22182: integerBitEnd = 6; break;
            case 22183: integerBitEnd = 7; break;
            case 22184: integerBitEnd = 8; break;
            case 22185: integerBitEnd = 9; break;
            case 22186: integerBitEnd = 10; break;
            case 22187: integerBitEnd = 11; break;
            case 22188: integerBitEnd = 12; break;
            case 22189: integerBitEnd = 13; break;
            case 22190: integerBitEnd = 14; break;
            case 22191: integerBitEnd = 15; break;
            case 24192: integerBitEnd = 0; break;
            case 24193: integerBitEnd = 1; break;
            case 24194: integerBitEnd = 2; break;
            case 24195: integerBitEnd = 3; break;
            case 24196: integerBitEnd = 4; break;
            case 24197: integerBitEnd = 5; break;
            case 24198: integerBitEnd = 6; break;
            case 24199: integerBitEnd = 7; break;
            case 24200: integerBitEnd = 8; break;
            case 24201: integerBitEnd = 9; break;
            case 24202: integerBitEnd = 10; break;
            case 24203: integerBitEnd = 11; break;
            case 24204: integerBitEnd = 12; break;
            case 24205: integerBitEnd = 13; break;
            case 24206: integerBitEnd = 14; break;
            case 24207: integerBitEnd = 15; break;
            case 26208: integerBitEnd = 0; break;
            case 26209: integerBitEnd = 1; break;
            case 26210: integerBitEnd = 2; break;
            case 26211: integerBitEnd = 3; break;
            case 26212: integerBitEnd = 4; break;
            case 26213: integerBitEnd = 5; break;
            case 26214: integerBitEnd = 6; break;
            case 26215: integerBitEnd = 7; break;
            case 26216: integerBitEnd = 8; break;
            case 26217: integerBitEnd = 9; break;
            case 26218: integerBitEnd = 10; break;
            case 26219: integerBitEnd = 11; break;
            case 26220: integerBitEnd = 12; break;
            case 26221: integerBitEnd = 13; break;
            case 26222: integerBitEnd = 14; break;
            case 26223: integerBitEnd = 15; break;
            case 28224: integerBitEnd = 0; break;
            case 28225: integerBitEnd = 1; break;
            case 28226: integerBitEnd = 2; break;
            case 28227: integerBitEnd = 3; break;
            case 28228: integerBitEnd = 4; break;
            case 28229: integerBitEnd = 5; break;
            case 28230: integerBitEnd = 6; break;
            case 28231: integerBitEnd = 7; break;
            case 28232: integerBitEnd = 8; break;
            case 28233: integerBitEnd = 9; break;
            case 28234: integerBitEnd = 10; break;
            case 28235: integerBitEnd = 11; break;
            case 28236: integerBitEnd = 12; break;
            case 28237: integerBitEnd = 13; break;
            case 28238: integerBitEnd = 14; break;
            case 28239: integerBitEnd = 15; break;
            case 30240: integerBitEnd = 0; break;
            case 30241: integerBitEnd = 1; break;
            case 30242: integerBitEnd = 2; break;
            case 30243: integerBitEnd = 3; break;
            case 30244: integerBitEnd = 4; break;
            case 30245: integerBitEnd = 5; break;
            case 30246: integerBitEnd = 6; break;
            case 30247: integerBitEnd = 7; break;
            case 30248: integerBitEnd = 8; break;
            case 30249: integerBitEnd = 9; break;
            case 30250: integerBitEnd = 10; break;
            case 30251: integerBitEnd = 11; break;
            case 30252: integerBitEnd = 12; break;
            case 30253: integerBitEnd = 13; break;
            case 30254: integerBitEnd = 14; break;
            case 30255: integerBitEnd = 15; break;
            case 32256: integerBitEnd = 0; break;
            case 32257: integerBitEnd = 1; break;
            case 32258: integerBitEnd = 2; break;
            case 32259: integerBitEnd = 3; break;
            case 32260: integerBitEnd = 4; break;
            case 32261: integerBitEnd = 5; break;
            case 32262: integerBitEnd = 6; break;
            case 32263: integerBitEnd = 7; break;
            case 32264: integerBitEnd = 8; break;
            case 32265: integerBitEnd = 9; break;
            case 32266: integerBitEnd = 10; break;
            case 32267: integerBitEnd = 11; break;
            case 32268: integerBitEnd = 12; break;
            case 32269: integerBitEnd = 13; break;
            case 32270: integerBitEnd = 14; break;
            case 32271: integerBitEnd = 15; break;
            case 34272: integerBitEnd = 0; break;
            case 34273: integerBitEnd = 1; break;
            case 34274: integerBitEnd = 2; break;
            case 34275: integerBitEnd = 3; break;
            case 34276: integerBitEnd = 4; break;
            case 34277: integerBitEnd = 5; break;
            case 34278: integerBitEnd = 6; break;
            case 34279: integerBitEnd = 7; break;
            case 34280: integerBitEnd = 8; break;
            case 34281: integerBitEnd = 9; break;
            case 34282: integerBitEnd = 10; break;
            case 34283: integerBitEnd = 11; break;
            case 34284: integerBitEnd = 12; break;
            case 34285: integerBitEnd = 13; break;
            case 34286: integerBitEnd = 14; break;
            case 34287: integerBitEnd = 15; break;
            case 36288: integerBitEnd = 0; break;
            case 36289: integerBitEnd = 1; break;
            case 36290: integerBitEnd = 2; break;
            case 36291: integerBitEnd = 3; break;
            case 36292: integerBitEnd = 4; break;
            case 36293: integerBitEnd = 5; break;
            case 36294: integerBitEnd = 6; break;
            case 36295: integerBitEnd = 7; break;
            case 36296: integerBitEnd = 8; break;
            case 36297: integerBitEnd = 9; break;
            case 36298: integerBitEnd = 10; break;
            case 36299: integerBitEnd = 11; break;
            case 36300: integerBitEnd = 12; break;
            case 36301: integerBitEnd = 13; break;
            case 36302: integerBitEnd = 14; break;
            case 36303: integerBitEnd = 15; break;
            case 38304: integerBitEnd = 0; break;
            case 38305: integerBitEnd = 1; break;
            case 38306: integerBitEnd = 2; break;
            case 38307: integerBitEnd = 3; break;
            case 38308: integerBitEnd = 4; break;
            case 38309: integerBitEnd = 5; break;
            case 38310: integerBitEnd = 6; break;
            case 38311: integerBitEnd = 7; break;
            case 38312: integerBitEnd = 8; break;
            case 38313: integerBitEnd = 9; break;
            case 38314: integerBitEnd = 10; break;
            case 38315: integerBitEnd = 11; break;
            case 38316: integerBitEnd = 12; break;
            case 38317: integerBitEnd = 13; break;
            case 38318: integerBitEnd = 14; break;
            case 38319: integerBitEnd = 15; break;
            case 40320: integerBitEnd = 0; break;
            case 40321: integerBitEnd = 1; break;
            case 40322: integerBitEnd = 2; break;
            case 40323: integerBitEnd = 3; break;
            case 40324: integerBitEnd = 4; break;
            case 40325: integerBitEnd = 5; break;
            case 40326: integerBitEnd = 6; break;
            case 40327: integerBitEnd = 7; break;
            case 40328: integerBitEnd = 8; break;
            case 40329: integerBitEnd = 9; break;
            case 40330: integerBitEnd = 10; break;
            case 40331: integerBitEnd = 11; break;
            case 40332: integerBitEnd = 12; break;
            case 40333: integerBitEnd = 13; break;
            case 40334: integerBitEnd = 14; break;
            case 40335: integerBitEnd = 15; break;
            case 42336: integerBitEnd = 0; break;
            case 42337: integerBitEnd = 1; break;
            case 42338: integerBitEnd = 2; break;
            case 42339: integerBitEnd = 3; break;
            case 42340: integerBitEnd = 4; break;
            case 42341: integerBitEnd = 5; break;
            case 42342: integerBitEnd = 6; break;
            case 42343: integerBitEnd = 7; break;
            case 42344: integerBitEnd = 8; break;
            case 42345: integerBitEnd = 9; break;
            case 42346: integerBitEnd = 10; break;
            case 42347: integerBitEnd = 11; break;
            case 42348: integerBitEnd = 12; break;
            case 42349: integerBitEnd = 13; break;
            case 42350: integerBitEnd = 14; break;
            case 42351: integerBitEnd = 15; break;
            case 44352: integerBitEnd = 0; break;
            case 44353: integerBitEnd = 1; break;
            case 44354: integerBitEnd = 2; break;
            case 44355: integerBitEnd = 3; break;
            case 44356: integerBitEnd = 4; break;
            case 44357: integerBitEnd = 5; break;
            case 44358: integerBitEnd = 6; break;
            case 44359: integerBitEnd = 7; break;
            case 44360: integerBitEnd = 8; break;
            case 44361: integerBitEnd = 9; break;
            case 44362: integerBitEnd = 10; break;
            case 44363: integerBitEnd = 11; break;
            case 44364: integerBitEnd = 12; break;
            case 44365: integerBitEnd = 13; break;
            case 44366: integerBitEnd = 14; break;
            case 44367: integerBitEnd = 15; break;
            case 46368: integerBitEnd = 0; break;
            case 46369: integerBitEnd = 1; break;
            case 46370: integerBitEnd = 2; break;
            case 46371: integerBitEnd = 3; break;
            case 46372: integerBitEnd = 4; break;
            case 46373: integerBitEnd = 5; break;
            case 46374: integerBitEnd = 6; break;
            case 46375: integerBitEnd = 7; break;
            case 46376: integerBitEnd = 8; break;
            case 46377: integerBitEnd = 9; break;
            case 46378: integerBitEnd = 10; break;
            case 46379: integerBitEnd = 11; break;
            case 46380: integerBitEnd = 12; break;
            case 46381: integerBitEnd = 13; break;
            case 46382: integerBitEnd = 14; break;
            case 46383: integerBitEnd = 15; break;
            case 48384: integerBitEnd = 0; break;
            case 48385: integerBitEnd = 1; break;
            case 48386: integerBitEnd = 2; break;
            case 48387: integerBitEnd = 3; break;
            case 48388: integerBitEnd = 4; break;
            case 48389: integerBitEnd = 5; break;
            case 48390: integerBitEnd = 6; break;
            case 48391: integerBitEnd = 7; break;
            case 48392: integerBitEnd = 8; break;
            case 48393: integerBitEnd = 9; break;
            case 48394: integerBitEnd = 10; break;
            case 48395: integerBitEnd = 11; break;
            case 48396: integerBitEnd = 12; break;
            case 48397: integerBitEnd = 13; break;
            case 48398: integerBitEnd = 14; break;
            case 48399: integerBitEnd = 15; break;
            case 50400: integerBitEnd = 0; break;
            case 50401: integerBitEnd = 1; break;
            case 50402: integerBitEnd = 2; break;
            case 50403: integerBitEnd = 3; break;
            case 50404: integerBitEnd = 4; break;
            case 50405: integerBitEnd = 5; break;
            case 50406: integerBitEnd = 6; break;
            case 50407: integerBitEnd = 7; break;
            case 50408: integerBitEnd = 8; break;
            case 50409: integerBitEnd = 9; break;
            case 50410: integerBitEnd = 10; break;
            case 50411: integerBitEnd = 11; break;
            case 50412: integerBitEnd = 12; break;
            case 50413: integerBitEnd = 13; break;
            case 50414: integerBitEnd = 14; break;
            case 50415: integerBitEnd = 15; break;
            case 52416: integerBitEnd = 0; break;
            case 52417: integerBitEnd = 1; break;
            case 52418: integerBitEnd = 2; break;
            case 52419: integerBitEnd = 3; break;
            case 52420: integerBitEnd = 4; break;
            case 52421: integerBitEnd = 5; break;
            case 52422: integerBitEnd = 6; break;
            case 52423: integerBitEnd = 7; break;
            case 52424: integerBitEnd = 8; break;
            case 52425: integerBitEnd = 9; break;
            case 52426: integerBitEnd = 10; break;
            case 52427: integerBitEnd = 11; break;
            case 52428: integerBitEnd = 12; break;
            case 52429: integerBitEnd = 13; break;
            case 52430: integerBitEnd = 14; break;
            case 52431: integerBitEnd = 15; break;
            case 54432: integerBitEnd = 0; break;
            case 54433: integerBitEnd = 1; break;
            case 54434: integerBitEnd = 2; break;
            case 54435: integerBitEnd = 3; break;
            case 54436: integerBitEnd = 4; break;
            case 54437: integerBitEnd = 5; break;
            case 54438: integerBitEnd = 6; break;
            case 54439: integerBitEnd = 7; break;
            case 54440: integerBitEnd = 8; break;
            case 54441: integerBitEnd = 9; break;
            case 54442: integerBitEnd = 10; break;
            case 54443: integerBitEnd = 11; break;
            case 54444: integerBitEnd = 12; break;
            case 54445: integerBitEnd = 13; break;
            case 54446: integerBitEnd = 14; break;
            case 54447: integerBitEnd = 15; break;
            case 56448: integerBitEnd = 0; break;
            case 56449: integerBitEnd = 1; break;
            case 56450: integerBitEnd = 2; break;
            case 56451: integerBitEnd = 3; break;
            case 56452: integerBitEnd = 4; break;
            case 56453: integerBitEnd = 5; break;
            case 56454: integerBitEnd = 6; break;
            case 56455: integerBitEnd = 7; break;
            case 56456: integerBitEnd = 8; break;
            case 56457: integerBitEnd = 9; break;
            case 56458: integerBitEnd = 10; break;
            case 56459: integerBitEnd = 11; break;
            case 56460: integerBitEnd = 12; break;
            case 56461: integerBitEnd = 13; break;
            case 56462: integerBitEnd = 14; break;
            case 56463: integerBitEnd = 15; break;
            case 58464: integerBitEnd = 0; break;
            case 58465: integerBitEnd = 1; break;
            case 58466: integerBitEnd = 2; break;
            case 58467: integerBitEnd = 3; break;
            case 58468: integerBitEnd = 4; break;
            case 58469: integerBitEnd = 5; break;
            case 58470: integerBitEnd = 6; break;
            case 58471: integerBitEnd = 7; break;
            case 58472: integerBitEnd = 8; break;
            case 58473: integerBitEnd = 9; break;
            case 58474: integerBitEnd = 10; break;
            case 58475: integerBitEnd = 11; break;
            case 58476: integerBitEnd = 12; break;
            case 58477: integerBitEnd = 13; break;
            case 58478: integerBitEnd = 14; break;
            case 58479: integerBitEnd = 15; break;
            case 60480: integerBitEnd = 0; break;
            case 60481: integerBitEnd = 1; break;
            case 60482: integerBitEnd = 2; break;
            case 60483: integerBitEnd = 3; break;
            case 60484: integerBitEnd = 4; break;
            case 60485: integerBitEnd = 5; break;
            case 60486: integerBitEnd = 6; break;
            case 60487: integerBitEnd = 7; break;
            case 60488: integerBitEnd = 8; break;
            case 60489: integerBitEnd = 9; break;
            case 60490: integerBitEnd = 10; break;
            case 60491: integerBitEnd = 11; break;
            case 60492: integerBitEnd = 12; break;
            case 60493: integerBitEnd = 13; break;
            case 60494: integerBitEnd = 14; break;
            case 60495: integerBitEnd = 15; break;
            case 62496: integerBitEnd = 0; break;
            case 62497: integerBitEnd = 1; break;
            case 62498: integerBitEnd = 2; break;
            case 62499: integerBitEnd = 3; break;
            case 62500: integerBitEnd = 4; break;
            case 62501: integerBitEnd = 5; break;
            case 62502: integerBitEnd = 6; break;
            case 62503: integerBitEnd = 7; break;
            case 62504: integerBitEnd = 8; break;
            case 62505: integerBitEnd = 9; break;
            case 62506: integerBitEnd = 10; break;
            case 62507: integerBitEnd = 11; break;
            case 62508: integerBitEnd = 12; break;
            case 62509: integerBitEnd = 13; break;
            case 62510: integerBitEnd = 14; break;
            case 62511: integerBitEnd = 15; break;
            case 64512: integerBitEnd = 0; break;
            case 64513: integerBitEnd = 1; break;
            case 64514: integerBitEnd = 2; break;
            case 64515: integerBitEnd = 3; break;
            case 64516: integerBitEnd = 4; break;
            case 64517: integerBitEnd = 5; break;
            case 64518: integerBitEnd = 6; break;
            case 64519: integerBitEnd = 7; break;
            case 64520: integerBitEnd = 8; break;
            case 64521: integerBitEnd = 9; break;
            case 64522: integerBitEnd = 10; break;
            case 64523: integerBitEnd = 11; break;
            case 64524: integerBitEnd = 12; break;
            case 64525: integerBitEnd = 13; break;
            case 64526: integerBitEnd = 14; break;
            case 64527: integerBitEnd = 15; break;
            case 66528: integerBitEnd = 0; break;
            case 66529: integerBitEnd = 1; break;
            case 66530: integerBitEnd = 2; break;
            case 66531: integerBitEnd = 3; break;
            case 66532: integerBitEnd = 4; break;
            case 66533: integerBitEnd = 5; break;
            case 66534: integerBitEnd = 6; break;
            case 66535: integerBitEnd = 7; break;
            case 66536: integerBitEnd = 8; break;
            case 66537: integerBitEnd = 9; break;
            case 66538: integerBitEnd = 10; break;
            case 66539: integerBitEnd = 11; break;
            case 66540: integerBitEnd = 12; break;
            case 66541: integerBitEnd = 13; break;
            case 66542: integerBitEnd = 14; break;
            case 66543: integerBitEnd = 15; break;
            case 68544: integerBitEnd = 0; break;
            case 68545: integerBitEnd = 1; break;
            case 68546: integerBitEnd = 2; break;
            case 68547: integerBitEnd = 3; break;
            case 68548: integerBitEnd = 4; break;
            case 68549: integerBitEnd = 5; break;
            case 68550: integerBitEnd = 6; break;
            case 68551: integerBitEnd = 7; break;
            case 68552: integerBitEnd = 8; break;
            case 68553: integerBitEnd = 9; break;
            case 68554: integerBitEnd = 10; break;
            case 68555: integerBitEnd = 11; break;
            case 68556: integerBitEnd = 12; break;
            case 68557: integerBitEnd = 13; break;
            case 68558: integerBitEnd = 14; break;
            case 68559: integerBitEnd = 15; break;
            case 70560: integerBitEnd = 0; break;
            case 70561: integerBitEnd = 1; break;
            case 70562: integerBitEnd = 2; break;
            case 70563: integerBitEnd = 3; break;
            case 70564: integerBitEnd = 4; break;
            case 70565: integerBitEnd = 5; break;
            case 70566: integerBitEnd = 6; break;
            case 70567: integerBitEnd = 7; break;
            case 70568: integerBitEnd = 8; break;
            case 70569: integerBitEnd = 9; break;
            case 70570: integerBitEnd = 10; break;
            case 70571: integerBitEnd = 11; break;
            case 70572: integerBitEnd = 12; break;
            case 70573: integerBitEnd = 13; break;
            case 70574: integerBitEnd = 14; break;
            case 70575: integerBitEnd = 15; break;
            case 72576: integerBitEnd = 0; break;
            case 72577: integerBitEnd = 1; break;
            case 72578: integerBitEnd = 2; break;
            case 72579: integerBitEnd = 3; break;
            case 72580: integerBitEnd = 4; break;
            case 72581: integerBitEnd = 5; break;
            case 72582: integerBitEnd = 6; break;
            case 72583: integerBitEnd = 7; break;
            case 72584: integerBitEnd = 8; break;
            case 72585: integerBitEnd = 9; break;
            case 72586: integerBitEnd = 10; break;
            case 72587: integerBitEnd = 11; break;
            case 72588: integerBitEnd = 12; break;
            case 72589: integerBitEnd = 13; break;
            case 72590: integerBitEnd = 14; break;
            case 72591: integerBitEnd = 15; break;
            case 74592: integerBitEnd = 0; break;
            case 74593: integerBitEnd = 1; break;
            case 74594: integerBitEnd = 2; break;
            case 74595: integerBitEnd = 3; break;
            case 74596: integerBitEnd = 4; break;
            case 74597: integerBitEnd = 5; break;
            case 74598: integerBitEnd = 6; break;
            case 74599: integerBitEnd = 7; break;
            case 74600: integerBitEnd = 8; break;
            case 74601: integerBitEnd = 9; break;
            case 74602: integerBitEnd = 10; break;
            case 74603: integerBitEnd = 11; break;
            case 74604: integerBitEnd = 12; break;
            case 74605: integerBitEnd = 13; break;
            case 74606: integerBitEnd = 14; break;
            case 74607: integerBitEnd = 15; break;
            case 76608: integerBitEnd = 0; break;
            case 76609: integerBitEnd = 1; break;
            case 76610: integerBitEnd = 2; break;
            case 76611: integerBitEnd = 3; break;
            case 76612: integerBitEnd = 4; break;
            case 76613: integerBitEnd = 5; break;
            case 76614: integerBitEnd = 6; break;
            case 76615: integerBitEnd = 7; break;
            case 76616: integerBitEnd = 8; break;
            case 76617: integerBitEnd = 9; break;
            case 76618: integerBitEnd = 10; break;
            case 76619: integerBitEnd = 11; break;
            case 76620: integerBitEnd = 12; break;
            case 76621: integerBitEnd = 13; break;
            case 76622: integerBitEnd = 14; break;
            case 76623: integerBitEnd = 15; break;
            case 78624: integerBitEnd = 0; break;
            case 78625: integerBitEnd = 1; break;
            case 78626: integerBitEnd = 2; break;
            case 78627: integerBitEnd = 3; break;
            case 78628: integerBitEnd = 4; break;
            case 78629: integerBitEnd = 5; break;
            case 78630: integerBitEnd = 6; break;
            case 78631: integerBitEnd = 7; break;
            case 78632: integerBitEnd = 8; break;
            case 78633: integerBitEnd = 9; break;
            case 78634: integerBitEnd = 10; break;
            case 78635: integerBitEnd = 11; break;
            case 78636: integerBitEnd = 12; break;
            case 78637: integerBitEnd = 13; break;
            case 78638: integerBitEnd = 14; break;
            case 78639: integerBitEnd = 15; break;
            case 80640: integerBitEnd = 0; break;
            case 80641: integerBitEnd = 1; break;
            case 80642: integerBitEnd = 2; break;
            case 80643: integerBitEnd = 3; break;
            case 80644: integerBitEnd = 4; break;
            case 80645: integerBitEnd = 5; break;
            case 80646: integerBitEnd = 6; break;
            case 80647: integerBitEnd = 7; break;
            case 80648: integerBitEnd = 8; break;
            case 80649: integerBitEnd = 9; break;
            case 80650: integerBitEnd = 10; break;
            case 80651: integerBitEnd = 11; break;
            case 80652: integerBitEnd = 12; break;
            case 80653: integerBitEnd = 13; break;
            case 80654: integerBitEnd = 14; break;
            case 80655: integerBitEnd = 15; break;
            case 82656: integerBitEnd = 0; break;
            case 82657: integerBitEnd = 1; break;
            case 82658: integerBitEnd = 2; break;
            case 82659: integerBitEnd = 3; break;
            case 82660: integerBitEnd = 4; break;
            case 82661: integerBitEnd = 5; break;
            case 82662: integerBitEnd = 6; break;
            case 82663: integerBitEnd = 7; break;
            case 82664: integerBitEnd = 8; break;
            case 82665: integerBitEnd = 9; break;
            case 82666: integerBitEnd = 10; break;
            case 82667: integerBitEnd = 11; break;
            case 82668: integerBitEnd = 12; break;
            case 82669: integerBitEnd = 13; break;
            case 82670: integerBitEnd = 14; break;
            case 82671: integerBitEnd = 15; break;
            case 84672: integerBitEnd = 0; break;
            case 84673: integerBitEnd = 1; break;
            case 84674: integerBitEnd = 2; break;
            case 84675: integerBitEnd = 3; break;
            case 84676: integerBitEnd = 4; break;
            case 84677: integerBitEnd = 5; break;
            case 84678: integerBitEnd = 6; break;
            case 84679: integerBitEnd = 7; break;
            case 84680: integerBitEnd = 8; break;
            case 84681: integerBitEnd = 9; break;
            case 84682: integerBitEnd = 10; break;
            case 84683: integerBitEnd = 11; break;
            case 84684: integerBitEnd = 12; break;
            case 84685: integerBitEnd = 13; break;
            case 84686: integerBitEnd = 14; break;
            case 84687: integerBitEnd = 15; break;
            case 86688: integerBitEnd = 0; break;
            case 86689: integerBitEnd = 1; break;
            case 86690: integerBitEnd = 2; break;
            case 86691: integerBitEnd = 3; break;
            case 86692: integerBitEnd = 4; break;
            case 86693: integerBitEnd = 5; break;
            case 86694: integerBitEnd = 6; break;
            case 86695: integerBitEnd = 7; break;
            case 86696: integerBitEnd = 8; break;
            case 86697: integerBitEnd = 9; break;
            case 86698: integerBitEnd = 10; break;
            case 86699: integerBitEnd = 11; break;
            case 86700: integerBitEnd = 12; break;
            case 86701: integerBitEnd = 13; break;
            case 86702: integerBitEnd = 14; break;
            case 86703: integerBitEnd = 15; break;
            case 88704: integerBitEnd = 0; break;
            case 88705: integerBitEnd = 1; break;
            case 88706: integerBitEnd = 2; break;
            case 88707: integerBitEnd = 3; break;
            case 88708: integerBitEnd = 4; break;
            case 88709: integerBitEnd = 5; break;
            case 88710: integerBitEnd = 6; break;
            case 88711: integerBitEnd = 7; break;
            case 88712: integerBitEnd = 8; break;
            case 88713: integerBitEnd = 9; break;
            case 88714: integerBitEnd = 10; break;
            case 88715: integerBitEnd = 11; break;
            case 88716: integerBitEnd = 12; break;
            case 88717: integerBitEnd = 13; break;
            case 88718: integerBitEnd = 14; break;
            case 88719: integerBitEnd = 15; break;
            case 90720: integerBitEnd = 0; break;
            case 90721: integerBitEnd = 1; break;
            case 90722: integerBitEnd = 2; break;
            case 90723: integerBitEnd = 3; break;
            case 90724: integerBitEnd = 4; break;
            case 90725: integerBitEnd = 5; break;
            case 90726: integerBitEnd = 6; break;
            case 90727: integerBitEnd = 7; break;
            case 90728: integerBitEnd = 8; break;
            case 90729: integerBitEnd = 9; break;
            case 90730: integerBitEnd = 10; break;
            case 90731: integerBitEnd = 11; break;
            case 90732: integerBitEnd = 12; break;
            case 90733: integerBitEnd = 13; break;
            case 90734: integerBitEnd = 14; break;
            case 90735: integerBitEnd = 15; break;
            case 92736: integerBitEnd = 0; break;
            case 92737: integerBitEnd = 1; break;
            case 92738: integerBitEnd = 2; break;
            case 92739: integerBitEnd = 3; break;
            case 92740: integerBitEnd = 4; break;
            case 92741: integerBitEnd = 5; break;
            case 92742: integerBitEnd = 6; break;
            case 92743: integerBitEnd = 7; break;
            case 92744: integerBitEnd = 8; break;
            case 92745: integerBitEnd = 9; break;
            case 92746: integerBitEnd = 10; break;
            case 92747: integerBitEnd = 11; break;
            case 92748: integerBitEnd = 12; break;
            case 92749: integerBitEnd = 13; break;
            case 92750: integerBitEnd = 14; break;
            case 92751: integerBitEnd = 15; break;
            case 94752: integerBitEnd = 0; break;
            case 94753: integerBitEnd = 1; break;
            case 94754: integerBitEnd = 2; break;
            case 94755: integerBitEnd = 3; break;
            case 94756: integerBitEnd = 4; break;
            case 94757: integerBitEnd = 5; break;
            case 94758: integerBitEnd = 6; break;
            case 94759: integerBitEnd = 7; break;
            case 94760: integerBitEnd = 8; break;
            case 94761: integerBitEnd = 9; break;
            case 94762: integerBitEnd = 10; break;
            case 94763: integerBitEnd = 11; break;
            case 94764: integerBitEnd = 12; break;
            case 94765: integerBitEnd = 13; break;
            case 94766: integerBitEnd = 14; break;
            case 94767: integerBitEnd = 15; break;
            case 96768: integerBitEnd = 0; break;
            case 96769: integerBitEnd = 1; break;
            case 96770: integerBitEnd = 2; break;
            case 96771: integerBitEnd = 3; break;
            case 96772: integerBitEnd = 4; break;
            case 96773: integerBitEnd = 5; break;
            case 96774: integerBitEnd = 6; break;
            case 96775: integerBitEnd = 7; break;
            case 96776: integerBitEnd = 8; break;
            case 96777: integerBitEnd = 9; break;
            case 96778: integerBitEnd = 10; break;
            case 96779: integerBitEnd = 11; break;
            case 96780: integerBitEnd = 12; break;
            case 96781: integerBitEnd = 13; break;
            case 96782: integerBitEnd = 14; break;
            case 96783: integerBitEnd = 15; break;
            case 98784: integerBitEnd = 0; break;
            case 98785: integerBitEnd = 1; break;
            case 98786: integerBitEnd = 2; break;
            case 98787: integerBitEnd = 3; break;
            case 98788: integerBitEnd = 4; break;
            case 98789: integerBitEnd = 5; break;
            case 98790: integerBitEnd = 6; break;
            case 98791: integerBitEnd = 7; break;
            case 98792: integerBitEnd = 8; break;
            case 98793: integerBitEnd = 9; break;
            case 98794: integerBitEnd = 10; break;
            case 98795: integerBitEnd = 11; break;
            case 98796: integerBitEnd = 12; break;
            case 98797: integerBitEnd = 13; break;
            case 98798: integerBitEnd = 14; break;
            case 98799: integerBitEnd = 15; break;
            case 100800: integerBitEnd = 0; break;
            case 100801: integerBitEnd = 1; break;
            case 100802: integerBitEnd = 2; break;
            case 100803: integerBitEnd = 3; break;
            case 100804: integerBitEnd = 4; break;
            case 100805: integerBitEnd = 5; break;
            case 100806: integerBitEnd = 6; break;
            case 100807: integerBitEnd = 7; break;
            case 100808: integerBitEnd = 8; break;
            case 100809: integerBitEnd = 9; break;
            case 100810: integerBitEnd = 10; break;
            case 100811: integerBitEnd = 11; break;
            case 100812: integerBitEnd = 12; break;
            case 100813: integerBitEnd = 13; break;
            case 100814: integerBitEnd = 14; break;
            case 100815: integerBitEnd = 15; break;
            case 102816: integerBitEnd = 0; break;
            case 102817: integerBitEnd = 1; break;
            case 102818: integerBitEnd = 2; break;
            case 102819: integerBitEnd = 3; break;
            case 102820: integerBitEnd = 4; break;
            case 102821: integerBitEnd = 5; break;
            case 102822: integerBitEnd = 6; break;
            case 102823: integerBitEnd = 7; break;
            case 102824: integerBitEnd = 8; break;
            case 102825: integerBitEnd = 9; break;
            case 102826: integerBitEnd = 10; break;
            case 102827: integerBitEnd = 11; break;
            case 102828: integerBitEnd = 12; break;
            case 102829: integerBitEnd = 13; break;
            case 102830: integerBitEnd = 14; break;
            case 102831: integerBitEnd = 15; break;
            case 104832: integerBitEnd = 0; break;
            case 104833: integerBitEnd = 1; break;
            case 104834: integerBitEnd = 2; break;
            case 104835: integerBitEnd = 3; break;
            case 104836: integerBitEnd = 4; break;
            case 104837: integerBitEnd = 5; break;
            case 104838: integerBitEnd = 6; break;
            case 104839: integerBitEnd = 7; break;
            case 104840: integerBitEnd = 8; break;
            case 104841: integerBitEnd = 9; break;
            case 104842: integerBitEnd = 10; break;
            case 104843: integerBitEnd = 11; break;
            case 104844: integerBitEnd = 12; break;
            case 104845: integerBitEnd = 13; break;
            case 104846: integerBitEnd = 14; break;
            case 104847: integerBitEnd = 15; break;
            case 106848: integerBitEnd = 0; break;
            case 106849: integerBitEnd = 1; break;
            case 106850: integerBitEnd = 2; break;
            case 106851: integerBitEnd = 3; break;
            case 106852: integerBitEnd = 4; break;
            case 106853: integerBitEnd = 5; break;
            case 106854: integerBitEnd = 6; break;
            case 106855: integerBitEnd = 7; break;
            case 106856: integerBitEnd = 8; break;
            case 106857: integerBitEnd = 9; break;
            case 106858: integerBitEnd = 10; break;
            case 106859: integerBitEnd = 11; break;
            case 106860: integerBitEnd = 12; break;
            case 106861: integerBitEnd = 13; break;
            case 106862: integerBitEnd = 14; break;
            case 106863: integerBitEnd = 15; break;
            case 108864: integerBitEnd = 0; break;
            case 108865: integerBitEnd = 1; break;
            case 108866: integerBitEnd = 2; break;
            case 108867: integerBitEnd = 3; break;
            case 108868: integerBitEnd = 4; break;
            case 108869: integerBitEnd = 5; break;
            case 108870: integerBitEnd = 6; break;
            case 108871: integerBitEnd = 7; break;
            case 108872: integerBitEnd = 8; break;
            case 108873: integerBitEnd = 9; break;
            case 108874: integerBitEnd = 10; break;
            case 108875: integerBitEnd = 11; break;
            case 108876: integerBitEnd = 12; break;
            case 108877: integerBitEnd = 13; break;
            case 108878: integerBitEnd = 14; break;
            case 108879: integerBitEnd = 15; break;
            case 110880: integerBitEnd = 0; break;
            case 110881: integerBitEnd = 1; break;
            case 110882: integerBitEnd = 2; break;
            case 110883: integerBitEnd = 3; break;
            case 110884: integerBitEnd = 4; break;
            case 110885: integerBitEnd = 5; break;
            case 110886: integerBitEnd = 6; break;
            case 110887: integerBitEnd = 7; break;
            case 110888: integerBitEnd = 8; break;
            case 110889: integerBitEnd = 9; break;
            case 110890: integerBitEnd = 10; break;
            case 110891: integerBitEnd = 11; break;
            case 110892: integerBitEnd = 12; break;
            case 110893: integerBitEnd = 13; break;
            case 110894: integerBitEnd = 14; break;
            case 110895: integerBitEnd = 15; break;
            case 112896: integerBitEnd = 0; break;
            case 112897: integerBitEnd = 1; break;
            case 112898: integerBitEnd = 2; break;
            case 112899: integerBitEnd = 3; break;
            case 112900: integerBitEnd = 4; break;
            case 112901: integerBitEnd = 5; break;
            case 112902: integerBitEnd = 6; break;
            case 112903: integerBitEnd = 7; break;
            case 112904: integerBitEnd = 8; break;
            case 112905: integerBitEnd = 9; break;
            case 112906: integerBitEnd = 10; break;
            case 112907: integerBitEnd = 11; break;
            case 112908: integerBitEnd = 12; break;
            case 112909: integerBitEnd = 13; break;
            case 112910: integerBitEnd = 14; break;
            case 112911: integerBitEnd = 15; break;
            case 114912: integerBitEnd = 0; break;
            case 114913: integerBitEnd = 1; break;
            case 114914: integerBitEnd = 2; break;
            case 114915: integerBitEnd = 3; break;
            case 114916: integerBitEnd = 4; break;
            case 114917: integerBitEnd = 5; break;
            case 114918: integerBitEnd = 6; break;
            case 114919: integerBitEnd = 7; break;
            case 114920: integerBitEnd = 8; break;
            case 114921: integerBitEnd = 9; break;
            case 114922: integerBitEnd = 10; break;
            case 114923: integerBitEnd = 11; break;
            case 114924: integerBitEnd = 12; break;
            case 114925: integerBitEnd = 13; break;
            case 114926: integerBitEnd = 14; break;
            case 114927: integerBitEnd = 15; break;
            case 116928: integerBitEnd = 0; break;
            case 116929: integerBitEnd = 1; break;
            case 116930: integerBitEnd = 2; break;
            case 116931: integerBitEnd = 3; break;
            case 116932: integerBitEnd = 4; break;
            case 116933: integerBitEnd = 5; break;
            case 116934: integerBitEnd = 6; break;
            case 116935: integerBitEnd = 7; break;
            case 116936: integerBitEnd = 8; break;
            case 116937: integerBitEnd = 9; break;
            case 116938: integerBitEnd = 10; break;
            case 116939: integerBitEnd = 11; break;
            case 116940: integerBitEnd = 12; break;
            case 116941: integerBitEnd = 13; break;
            case 116942: integerBitEnd = 14; break;
            case 116943: integerBitEnd = 15; break;
            case 118944: integerBitEnd = 0; break;
            case 118945: integerBitEnd = 1; break;
            case 118946: integerBitEnd = 2; break;
            case 118947: integerBitEnd = 3; break;
            case 118948: integerBitEnd = 4; break;
            case 118949: integerBitEnd = 5; break;
            case 118950: integerBitEnd = 6; break;
            case 118951: integerBitEnd = 7; break;
            case 118952: integerBitEnd = 8; break;
            case 118953: integerBitEnd = 9; break;
            case 118954: integerBitEnd = 10; break;
            case 118955: integerBitEnd = 11; break;
            case 118956: integerBitEnd = 12; break;
            case 118957: integerBitEnd = 13; break;
            case 118958: integerBitEnd = 14; break;
            case 118959: integerBitEnd = 15; break;
            case 120960: integerBitEnd = 0; break;
            case 120961: integerBitEnd = 1; break;
            case 120962: integerBitEnd = 2; break;
            case 120963: integerBitEnd = 3; break;
            case 120964: integerBitEnd = 4; break;
            case 120965: integerBitEnd = 5; break;
            case 120966: integerBitEnd = 6; break;
            case 120967: integerBitEnd = 7; break;
            case 120968: integerBitEnd = 8; break;
            case 120969: integerBitEnd = 9; break;
            case 120970: integerBitEnd = 10; break;
            case 120971: integerBitEnd = 11; break;
            case 120972: integerBitEnd = 12; break;
            case 120973: integerBitEnd = 13; break;
            case 120974: integerBitEnd = 14; break;
            case 120975: integerBitEnd = 15; break;
            case 122976: integerBitEnd = 0; break;
            case 122977: integerBitEnd = 1; break;
            case 122978: integerBitEnd = 2; break;
            case 122979: integerBitEnd = 3; break;
            case 122980: integerBitEnd = 4; break;
            case 122981: integerBitEnd = 5; break;
            case 122982: integerBitEnd = 6; break;
            case 122983: integerBitEnd = 7; break;
            case 122984: integerBitEnd = 8; break;
            case 122985: integerBitEnd = 9; break;
            case 122986: integerBitEnd = 10; break;
            case 122987: integerBitEnd = 11; break;
            case 122988: integerBitEnd = 12; break;
            case 122989: integerBitEnd = 13; break;
            case 122990: integerBitEnd = 14; break;
            case 122991: integerBitEnd = 15; break;
            case 124992: integerBitEnd = 0; break;
            case 124993: integerBitEnd = 1; break;
            case 124994: integerBitEnd = 2; break;
            case 124995: integerBitEnd = 3; break;
            case 124996: integerBitEnd = 4; break;
            case 124997: integerBitEnd = 5; break;
            case 124998: integerBitEnd = 6; break;
            case 124999: integerBitEnd = 7; break;
            case 125000: integerBitEnd = 8; break;
            case 125001: integerBitEnd = 9; break;
            case 125002: integerBitEnd = 10; break;
            case 125003: integerBitEnd = 11; break;
            case 125004: integerBitEnd = 12; break;
            case 125005: integerBitEnd = 13; break;
            case 125006: integerBitEnd = 14; break;
            case 125007: integerBitEnd = 15; break;
            case 127008: integerBitEnd = 0; break;
            case 127009: integerBitEnd = 1; break;
            case 127010: integerBitEnd = 2; break;
            case 127011: integerBitEnd = 3; break;
            case 127012: integerBitEnd = 4; break;
            case 127013: integerBitEnd = 5; break;
            case 127014: integerBitEnd = 6; break;
            case 127015: integerBitEnd = 7; break;
            case 127016: integerBitEnd = 8; break;
            case 127017: integerBitEnd = 9; break;
            case 127018: integerBitEnd = 10; break;
            case 127019: integerBitEnd = 11; break;
            case 127020: integerBitEnd = 12; break;
            case 127021: integerBitEnd = 13; break;
            case 127022: integerBitEnd = 14; break;
            case 127023: integerBitEnd = 15; break;
        }


        //if write is contained in single integer
        if (arrIndexStart == arrIndexEnd) {
            int bitm = bitmask2(integerBitBegin, integerBitEnd, false);
            value = value << (SHARED_ARRAY_ELEM_SIZE-integerBitBegin-numBits);
            // read value from shared array at arrIndexStart
            int entry = readSharedArray(arrIndexStart);
            writeSharedArray(arrIndexStart, (entry & bitm) | value);
        } else {
            //if write spans two integers
            int bitm1 = bitmask2(integerBitBegin, SHARED_ARRAY_ELEM_SIZE-1, false);
            int bitm2 = bitmask2(0, integerBitEnd, false);

            int part1 = value;
            int part2 = value;
            int part2len = integerBitEnd+1;

            part1 = part1 >>> part2len;
            part2 = part2 << (SHARED_ARRAY_ELEM_SIZE-part2len);
            
            int entry1 = readSharedArray(arrIndexStart);
            int entry2 = readSharedArray(arrIndexEnd);
            writeSharedArray(arrIndexStart, (entry1 & bitm1) | part1);
            writeSharedArray(arrIndexEnd, (entry2 & bitm2) | part2);
        }
        return true;
    }

    // Bits are zero indexed. Put the bit you want to begin reading read from,
    // _ _ _ 0 1 0 _ , so reading 010, starting from the 0 would be readfromArray(3, 3)
    // beginBit can be anywhere in [0, SHARED_ARRAY_TOTAL_BITS-1].
    // numBits should be at most SHARED_ARRAY_ELEM_SIZE.
    public int read(int beginBit, int numBits) throws GameActionException {
        int arrIndexStart = beginBit>>>SHARED_ARRAY_ELEM_LOG2;
        int sumMinusOne = numBits + beginBit - 1;
        int arrIndexEnd = (sumMinusOne)>>>SHARED_ARRAY_ELEM_LOG2;

        // integerBitBegin and integerBitEnd will be set in the switch statements, in-line for speedup
        // int integerBitBegin = whichBit2(arrIndexStart, beginBit);
        // int integerBitEnd = whichBit2(arrIndexEnd, sumMinusOne);
        int integerBitBegin = 0;
        int integerBitEnd = 0;
        int bit1 = beginBit + arrIndexStart*2000;
        int bit2 = sumMinusOne + arrIndexEnd*2000;
        switch (bit1) {
            case 0: integerBitBegin = 0; break;
            case 1: integerBitBegin = 1; break;
            case 2: integerBitBegin = 2; break;
            case 3: integerBitBegin = 3; break;
            case 4: integerBitBegin = 4; break;
            case 5: integerBitBegin = 5; break;
            case 6: integerBitBegin = 6; break;
            case 7: integerBitBegin = 7; break;
            case 8: integerBitBegin = 8; break;
            case 9: integerBitBegin = 9; break;
            case 10: integerBitBegin = 10; break;
            case 11: integerBitBegin = 11; break;
            case 12: integerBitBegin = 12; break;
            case 13: integerBitBegin = 13; break;
            case 14: integerBitBegin = 14; break;
            case 15: integerBitBegin = 15; break;
            case 2016: integerBitBegin = 0; break;
            case 2017: integerBitBegin = 1; break;
            case 2018: integerBitBegin = 2; break;
            case 2019: integerBitBegin = 3; break;
            case 2020: integerBitBegin = 4; break;
            case 2021: integerBitBegin = 5; break;
            case 2022: integerBitBegin = 6; break;
            case 2023: integerBitBegin = 7; break;
            case 2024: integerBitBegin = 8; break;
            case 2025: integerBitBegin = 9; break;
            case 2026: integerBitBegin = 10; break;
            case 2027: integerBitBegin = 11; break;
            case 2028: integerBitBegin = 12; break;
            case 2029: integerBitBegin = 13; break;
            case 2030: integerBitBegin = 14; break;
            case 2031: integerBitBegin = 15; break;
            case 4032: integerBitBegin = 0; break;
            case 4033: integerBitBegin = 1; break;
            case 4034: integerBitBegin = 2; break;
            case 4035: integerBitBegin = 3; break;
            case 4036: integerBitBegin = 4; break;
            case 4037: integerBitBegin = 5; break;
            case 4038: integerBitBegin = 6; break;
            case 4039: integerBitBegin = 7; break;
            case 4040: integerBitBegin = 8; break;
            case 4041: integerBitBegin = 9; break;
            case 4042: integerBitBegin = 10; break;
            case 4043: integerBitBegin = 11; break;
            case 4044: integerBitBegin = 12; break;
            case 4045: integerBitBegin = 13; break;
            case 4046: integerBitBegin = 14; break;
            case 4047: integerBitBegin = 15; break;
            case 6048: integerBitBegin = 0; break;
            case 6049: integerBitBegin = 1; break;
            case 6050: integerBitBegin = 2; break;
            case 6051: integerBitBegin = 3; break;
            case 6052: integerBitBegin = 4; break;
            case 6053: integerBitBegin = 5; break;
            case 6054: integerBitBegin = 6; break;
            case 6055: integerBitBegin = 7; break;
            case 6056: integerBitBegin = 8; break;
            case 6057: integerBitBegin = 9; break;
            case 6058: integerBitBegin = 10; break;
            case 6059: integerBitBegin = 11; break;
            case 6060: integerBitBegin = 12; break;
            case 6061: integerBitBegin = 13; break;
            case 6062: integerBitBegin = 14; break;
            case 6063: integerBitBegin = 15; break;
            case 8064: integerBitBegin = 0; break;
            case 8065: integerBitBegin = 1; break;
            case 8066: integerBitBegin = 2; break;
            case 8067: integerBitBegin = 3; break;
            case 8068: integerBitBegin = 4; break;
            case 8069: integerBitBegin = 5; break;
            case 8070: integerBitBegin = 6; break;
            case 8071: integerBitBegin = 7; break;
            case 8072: integerBitBegin = 8; break;
            case 8073: integerBitBegin = 9; break;
            case 8074: integerBitBegin = 10; break;
            case 8075: integerBitBegin = 11; break;
            case 8076: integerBitBegin = 12; break;
            case 8077: integerBitBegin = 13; break;
            case 8078: integerBitBegin = 14; break;
            case 8079: integerBitBegin = 15; break;
            case 10080: integerBitBegin = 0; break;
            case 10081: integerBitBegin = 1; break;
            case 10082: integerBitBegin = 2; break;
            case 10083: integerBitBegin = 3; break;
            case 10084: integerBitBegin = 4; break;
            case 10085: integerBitBegin = 5; break;
            case 10086: integerBitBegin = 6; break;
            case 10087: integerBitBegin = 7; break;
            case 10088: integerBitBegin = 8; break;
            case 10089: integerBitBegin = 9; break;
            case 10090: integerBitBegin = 10; break;
            case 10091: integerBitBegin = 11; break;
            case 10092: integerBitBegin = 12; break;
            case 10093: integerBitBegin = 13; break;
            case 10094: integerBitBegin = 14; break;
            case 10095: integerBitBegin = 15; break;
            case 12096: integerBitBegin = 0; break;
            case 12097: integerBitBegin = 1; break;
            case 12098: integerBitBegin = 2; break;
            case 12099: integerBitBegin = 3; break;
            case 12100: integerBitBegin = 4; break;
            case 12101: integerBitBegin = 5; break;
            case 12102: integerBitBegin = 6; break;
            case 12103: integerBitBegin = 7; break;
            case 12104: integerBitBegin = 8; break;
            case 12105: integerBitBegin = 9; break;
            case 12106: integerBitBegin = 10; break;
            case 12107: integerBitBegin = 11; break;
            case 12108: integerBitBegin = 12; break;
            case 12109: integerBitBegin = 13; break;
            case 12110: integerBitBegin = 14; break;
            case 12111: integerBitBegin = 15; break;
            case 14112: integerBitBegin = 0; break;
            case 14113: integerBitBegin = 1; break;
            case 14114: integerBitBegin = 2; break;
            case 14115: integerBitBegin = 3; break;
            case 14116: integerBitBegin = 4; break;
            case 14117: integerBitBegin = 5; break;
            case 14118: integerBitBegin = 6; break;
            case 14119: integerBitBegin = 7; break;
            case 14120: integerBitBegin = 8; break;
            case 14121: integerBitBegin = 9; break;
            case 14122: integerBitBegin = 10; break;
            case 14123: integerBitBegin = 11; break;
            case 14124: integerBitBegin = 12; break;
            case 14125: integerBitBegin = 13; break;
            case 14126: integerBitBegin = 14; break;
            case 14127: integerBitBegin = 15; break;
            case 16128: integerBitBegin = 0; break;
            case 16129: integerBitBegin = 1; break;
            case 16130: integerBitBegin = 2; break;
            case 16131: integerBitBegin = 3; break;
            case 16132: integerBitBegin = 4; break;
            case 16133: integerBitBegin = 5; break;
            case 16134: integerBitBegin = 6; break;
            case 16135: integerBitBegin = 7; break;
            case 16136: integerBitBegin = 8; break;
            case 16137: integerBitBegin = 9; break;
            case 16138: integerBitBegin = 10; break;
            case 16139: integerBitBegin = 11; break;
            case 16140: integerBitBegin = 12; break;
            case 16141: integerBitBegin = 13; break;
            case 16142: integerBitBegin = 14; break;
            case 16143: integerBitBegin = 15; break;
            case 18144: integerBitBegin = 0; break;
            case 18145: integerBitBegin = 1; break;
            case 18146: integerBitBegin = 2; break;
            case 18147: integerBitBegin = 3; break;
            case 18148: integerBitBegin = 4; break;
            case 18149: integerBitBegin = 5; break;
            case 18150: integerBitBegin = 6; break;
            case 18151: integerBitBegin = 7; break;
            case 18152: integerBitBegin = 8; break;
            case 18153: integerBitBegin = 9; break;
            case 18154: integerBitBegin = 10; break;
            case 18155: integerBitBegin = 11; break;
            case 18156: integerBitBegin = 12; break;
            case 18157: integerBitBegin = 13; break;
            case 18158: integerBitBegin = 14; break;
            case 18159: integerBitBegin = 15; break;
            case 20160: integerBitBegin = 0; break;
            case 20161: integerBitBegin = 1; break;
            case 20162: integerBitBegin = 2; break;
            case 20163: integerBitBegin = 3; break;
            case 20164: integerBitBegin = 4; break;
            case 20165: integerBitBegin = 5; break;
            case 20166: integerBitBegin = 6; break;
            case 20167: integerBitBegin = 7; break;
            case 20168: integerBitBegin = 8; break;
            case 20169: integerBitBegin = 9; break;
            case 20170: integerBitBegin = 10; break;
            case 20171: integerBitBegin = 11; break;
            case 20172: integerBitBegin = 12; break;
            case 20173: integerBitBegin = 13; break;
            case 20174: integerBitBegin = 14; break;
            case 20175: integerBitBegin = 15; break;
            case 22176: integerBitBegin = 0; break;
            case 22177: integerBitBegin = 1; break;
            case 22178: integerBitBegin = 2; break;
            case 22179: integerBitBegin = 3; break;
            case 22180: integerBitBegin = 4; break;
            case 22181: integerBitBegin = 5; break;
            case 22182: integerBitBegin = 6; break;
            case 22183: integerBitBegin = 7; break;
            case 22184: integerBitBegin = 8; break;
            case 22185: integerBitBegin = 9; break;
            case 22186: integerBitBegin = 10; break;
            case 22187: integerBitBegin = 11; break;
            case 22188: integerBitBegin = 12; break;
            case 22189: integerBitBegin = 13; break;
            case 22190: integerBitBegin = 14; break;
            case 22191: integerBitBegin = 15; break;
            case 24192: integerBitBegin = 0; break;
            case 24193: integerBitBegin = 1; break;
            case 24194: integerBitBegin = 2; break;
            case 24195: integerBitBegin = 3; break;
            case 24196: integerBitBegin = 4; break;
            case 24197: integerBitBegin = 5; break;
            case 24198: integerBitBegin = 6; break;
            case 24199: integerBitBegin = 7; break;
            case 24200: integerBitBegin = 8; break;
            case 24201: integerBitBegin = 9; break;
            case 24202: integerBitBegin = 10; break;
            case 24203: integerBitBegin = 11; break;
            case 24204: integerBitBegin = 12; break;
            case 24205: integerBitBegin = 13; break;
            case 24206: integerBitBegin = 14; break;
            case 24207: integerBitBegin = 15; break;
            case 26208: integerBitBegin = 0; break;
            case 26209: integerBitBegin = 1; break;
            case 26210: integerBitBegin = 2; break;
            case 26211: integerBitBegin = 3; break;
            case 26212: integerBitBegin = 4; break;
            case 26213: integerBitBegin = 5; break;
            case 26214: integerBitBegin = 6; break;
            case 26215: integerBitBegin = 7; break;
            case 26216: integerBitBegin = 8; break;
            case 26217: integerBitBegin = 9; break;
            case 26218: integerBitBegin = 10; break;
            case 26219: integerBitBegin = 11; break;
            case 26220: integerBitBegin = 12; break;
            case 26221: integerBitBegin = 13; break;
            case 26222: integerBitBegin = 14; break;
            case 26223: integerBitBegin = 15; break;
            case 28224: integerBitBegin = 0; break;
            case 28225: integerBitBegin = 1; break;
            case 28226: integerBitBegin = 2; break;
            case 28227: integerBitBegin = 3; break;
            case 28228: integerBitBegin = 4; break;
            case 28229: integerBitBegin = 5; break;
            case 28230: integerBitBegin = 6; break;
            case 28231: integerBitBegin = 7; break;
            case 28232: integerBitBegin = 8; break;
            case 28233: integerBitBegin = 9; break;
            case 28234: integerBitBegin = 10; break;
            case 28235: integerBitBegin = 11; break;
            case 28236: integerBitBegin = 12; break;
            case 28237: integerBitBegin = 13; break;
            case 28238: integerBitBegin = 14; break;
            case 28239: integerBitBegin = 15; break;
            case 30240: integerBitBegin = 0; break;
            case 30241: integerBitBegin = 1; break;
            case 30242: integerBitBegin = 2; break;
            case 30243: integerBitBegin = 3; break;
            case 30244: integerBitBegin = 4; break;
            case 30245: integerBitBegin = 5; break;
            case 30246: integerBitBegin = 6; break;
            case 30247: integerBitBegin = 7; break;
            case 30248: integerBitBegin = 8; break;
            case 30249: integerBitBegin = 9; break;
            case 30250: integerBitBegin = 10; break;
            case 30251: integerBitBegin = 11; break;
            case 30252: integerBitBegin = 12; break;
            case 30253: integerBitBegin = 13; break;
            case 30254: integerBitBegin = 14; break;
            case 30255: integerBitBegin = 15; break;
            case 32256: integerBitBegin = 0; break;
            case 32257: integerBitBegin = 1; break;
            case 32258: integerBitBegin = 2; break;
            case 32259: integerBitBegin = 3; break;
            case 32260: integerBitBegin = 4; break;
            case 32261: integerBitBegin = 5; break;
            case 32262: integerBitBegin = 6; break;
            case 32263: integerBitBegin = 7; break;
            case 32264: integerBitBegin = 8; break;
            case 32265: integerBitBegin = 9; break;
            case 32266: integerBitBegin = 10; break;
            case 32267: integerBitBegin = 11; break;
            case 32268: integerBitBegin = 12; break;
            case 32269: integerBitBegin = 13; break;
            case 32270: integerBitBegin = 14; break;
            case 32271: integerBitBegin = 15; break;
            case 34272: integerBitBegin = 0; break;
            case 34273: integerBitBegin = 1; break;
            case 34274: integerBitBegin = 2; break;
            case 34275: integerBitBegin = 3; break;
            case 34276: integerBitBegin = 4; break;
            case 34277: integerBitBegin = 5; break;
            case 34278: integerBitBegin = 6; break;
            case 34279: integerBitBegin = 7; break;
            case 34280: integerBitBegin = 8; break;
            case 34281: integerBitBegin = 9; break;
            case 34282: integerBitBegin = 10; break;
            case 34283: integerBitBegin = 11; break;
            case 34284: integerBitBegin = 12; break;
            case 34285: integerBitBegin = 13; break;
            case 34286: integerBitBegin = 14; break;
            case 34287: integerBitBegin = 15; break;
            case 36288: integerBitBegin = 0; break;
            case 36289: integerBitBegin = 1; break;
            case 36290: integerBitBegin = 2; break;
            case 36291: integerBitBegin = 3; break;
            case 36292: integerBitBegin = 4; break;
            case 36293: integerBitBegin = 5; break;
            case 36294: integerBitBegin = 6; break;
            case 36295: integerBitBegin = 7; break;
            case 36296: integerBitBegin = 8; break;
            case 36297: integerBitBegin = 9; break;
            case 36298: integerBitBegin = 10; break;
            case 36299: integerBitBegin = 11; break;
            case 36300: integerBitBegin = 12; break;
            case 36301: integerBitBegin = 13; break;
            case 36302: integerBitBegin = 14; break;
            case 36303: integerBitBegin = 15; break;
            case 38304: integerBitBegin = 0; break;
            case 38305: integerBitBegin = 1; break;
            case 38306: integerBitBegin = 2; break;
            case 38307: integerBitBegin = 3; break;
            case 38308: integerBitBegin = 4; break;
            case 38309: integerBitBegin = 5; break;
            case 38310: integerBitBegin = 6; break;
            case 38311: integerBitBegin = 7; break;
            case 38312: integerBitBegin = 8; break;
            case 38313: integerBitBegin = 9; break;
            case 38314: integerBitBegin = 10; break;
            case 38315: integerBitBegin = 11; break;
            case 38316: integerBitBegin = 12; break;
            case 38317: integerBitBegin = 13; break;
            case 38318: integerBitBegin = 14; break;
            case 38319: integerBitBegin = 15; break;
            case 40320: integerBitBegin = 0; break;
            case 40321: integerBitBegin = 1; break;
            case 40322: integerBitBegin = 2; break;
            case 40323: integerBitBegin = 3; break;
            case 40324: integerBitBegin = 4; break;
            case 40325: integerBitBegin = 5; break;
            case 40326: integerBitBegin = 6; break;
            case 40327: integerBitBegin = 7; break;
            case 40328: integerBitBegin = 8; break;
            case 40329: integerBitBegin = 9; break;
            case 40330: integerBitBegin = 10; break;
            case 40331: integerBitBegin = 11; break;
            case 40332: integerBitBegin = 12; break;
            case 40333: integerBitBegin = 13; break;
            case 40334: integerBitBegin = 14; break;
            case 40335: integerBitBegin = 15; break;
            case 42336: integerBitBegin = 0; break;
            case 42337: integerBitBegin = 1; break;
            case 42338: integerBitBegin = 2; break;
            case 42339: integerBitBegin = 3; break;
            case 42340: integerBitBegin = 4; break;
            case 42341: integerBitBegin = 5; break;
            case 42342: integerBitBegin = 6; break;
            case 42343: integerBitBegin = 7; break;
            case 42344: integerBitBegin = 8; break;
            case 42345: integerBitBegin = 9; break;
            case 42346: integerBitBegin = 10; break;
            case 42347: integerBitBegin = 11; break;
            case 42348: integerBitBegin = 12; break;
            case 42349: integerBitBegin = 13; break;
            case 42350: integerBitBegin = 14; break;
            case 42351: integerBitBegin = 15; break;
            case 44352: integerBitBegin = 0; break;
            case 44353: integerBitBegin = 1; break;
            case 44354: integerBitBegin = 2; break;
            case 44355: integerBitBegin = 3; break;
            case 44356: integerBitBegin = 4; break;
            case 44357: integerBitBegin = 5; break;
            case 44358: integerBitBegin = 6; break;
            case 44359: integerBitBegin = 7; break;
            case 44360: integerBitBegin = 8; break;
            case 44361: integerBitBegin = 9; break;
            case 44362: integerBitBegin = 10; break;
            case 44363: integerBitBegin = 11; break;
            case 44364: integerBitBegin = 12; break;
            case 44365: integerBitBegin = 13; break;
            case 44366: integerBitBegin = 14; break;
            case 44367: integerBitBegin = 15; break;
            case 46368: integerBitBegin = 0; break;
            case 46369: integerBitBegin = 1; break;
            case 46370: integerBitBegin = 2; break;
            case 46371: integerBitBegin = 3; break;
            case 46372: integerBitBegin = 4; break;
            case 46373: integerBitBegin = 5; break;
            case 46374: integerBitBegin = 6; break;
            case 46375: integerBitBegin = 7; break;
            case 46376: integerBitBegin = 8; break;
            case 46377: integerBitBegin = 9; break;
            case 46378: integerBitBegin = 10; break;
            case 46379: integerBitBegin = 11; break;
            case 46380: integerBitBegin = 12; break;
            case 46381: integerBitBegin = 13; break;
            case 46382: integerBitBegin = 14; break;
            case 46383: integerBitBegin = 15; break;
            case 48384: integerBitBegin = 0; break;
            case 48385: integerBitBegin = 1; break;
            case 48386: integerBitBegin = 2; break;
            case 48387: integerBitBegin = 3; break;
            case 48388: integerBitBegin = 4; break;
            case 48389: integerBitBegin = 5; break;
            case 48390: integerBitBegin = 6; break;
            case 48391: integerBitBegin = 7; break;
            case 48392: integerBitBegin = 8; break;
            case 48393: integerBitBegin = 9; break;
            case 48394: integerBitBegin = 10; break;
            case 48395: integerBitBegin = 11; break;
            case 48396: integerBitBegin = 12; break;
            case 48397: integerBitBegin = 13; break;
            case 48398: integerBitBegin = 14; break;
            case 48399: integerBitBegin = 15; break;
            case 50400: integerBitBegin = 0; break;
            case 50401: integerBitBegin = 1; break;
            case 50402: integerBitBegin = 2; break;
            case 50403: integerBitBegin = 3; break;
            case 50404: integerBitBegin = 4; break;
            case 50405: integerBitBegin = 5; break;
            case 50406: integerBitBegin = 6; break;
            case 50407: integerBitBegin = 7; break;
            case 50408: integerBitBegin = 8; break;
            case 50409: integerBitBegin = 9; break;
            case 50410: integerBitBegin = 10; break;
            case 50411: integerBitBegin = 11; break;
            case 50412: integerBitBegin = 12; break;
            case 50413: integerBitBegin = 13; break;
            case 50414: integerBitBegin = 14; break;
            case 50415: integerBitBegin = 15; break;
            case 52416: integerBitBegin = 0; break;
            case 52417: integerBitBegin = 1; break;
            case 52418: integerBitBegin = 2; break;
            case 52419: integerBitBegin = 3; break;
            case 52420: integerBitBegin = 4; break;
            case 52421: integerBitBegin = 5; break;
            case 52422: integerBitBegin = 6; break;
            case 52423: integerBitBegin = 7; break;
            case 52424: integerBitBegin = 8; break;
            case 52425: integerBitBegin = 9; break;
            case 52426: integerBitBegin = 10; break;
            case 52427: integerBitBegin = 11; break;
            case 52428: integerBitBegin = 12; break;
            case 52429: integerBitBegin = 13; break;
            case 52430: integerBitBegin = 14; break;
            case 52431: integerBitBegin = 15; break;
            case 54432: integerBitBegin = 0; break;
            case 54433: integerBitBegin = 1; break;
            case 54434: integerBitBegin = 2; break;
            case 54435: integerBitBegin = 3; break;
            case 54436: integerBitBegin = 4; break;
            case 54437: integerBitBegin = 5; break;
            case 54438: integerBitBegin = 6; break;
            case 54439: integerBitBegin = 7; break;
            case 54440: integerBitBegin = 8; break;
            case 54441: integerBitBegin = 9; break;
            case 54442: integerBitBegin = 10; break;
            case 54443: integerBitBegin = 11; break;
            case 54444: integerBitBegin = 12; break;
            case 54445: integerBitBegin = 13; break;
            case 54446: integerBitBegin = 14; break;
            case 54447: integerBitBegin = 15; break;
            case 56448: integerBitBegin = 0; break;
            case 56449: integerBitBegin = 1; break;
            case 56450: integerBitBegin = 2; break;
            case 56451: integerBitBegin = 3; break;
            case 56452: integerBitBegin = 4; break;
            case 56453: integerBitBegin = 5; break;
            case 56454: integerBitBegin = 6; break;
            case 56455: integerBitBegin = 7; break;
            case 56456: integerBitBegin = 8; break;
            case 56457: integerBitBegin = 9; break;
            case 56458: integerBitBegin = 10; break;
            case 56459: integerBitBegin = 11; break;
            case 56460: integerBitBegin = 12; break;
            case 56461: integerBitBegin = 13; break;
            case 56462: integerBitBegin = 14; break;
            case 56463: integerBitBegin = 15; break;
            case 58464: integerBitBegin = 0; break;
            case 58465: integerBitBegin = 1; break;
            case 58466: integerBitBegin = 2; break;
            case 58467: integerBitBegin = 3; break;
            case 58468: integerBitBegin = 4; break;
            case 58469: integerBitBegin = 5; break;
            case 58470: integerBitBegin = 6; break;
            case 58471: integerBitBegin = 7; break;
            case 58472: integerBitBegin = 8; break;
            case 58473: integerBitBegin = 9; break;
            case 58474: integerBitBegin = 10; break;
            case 58475: integerBitBegin = 11; break;
            case 58476: integerBitBegin = 12; break;
            case 58477: integerBitBegin = 13; break;
            case 58478: integerBitBegin = 14; break;
            case 58479: integerBitBegin = 15; break;
            case 60480: integerBitBegin = 0; break;
            case 60481: integerBitBegin = 1; break;
            case 60482: integerBitBegin = 2; break;
            case 60483: integerBitBegin = 3; break;
            case 60484: integerBitBegin = 4; break;
            case 60485: integerBitBegin = 5; break;
            case 60486: integerBitBegin = 6; break;
            case 60487: integerBitBegin = 7; break;
            case 60488: integerBitBegin = 8; break;
            case 60489: integerBitBegin = 9; break;
            case 60490: integerBitBegin = 10; break;
            case 60491: integerBitBegin = 11; break;
            case 60492: integerBitBegin = 12; break;
            case 60493: integerBitBegin = 13; break;
            case 60494: integerBitBegin = 14; break;
            case 60495: integerBitBegin = 15; break;
            case 62496: integerBitBegin = 0; break;
            case 62497: integerBitBegin = 1; break;
            case 62498: integerBitBegin = 2; break;
            case 62499: integerBitBegin = 3; break;
            case 62500: integerBitBegin = 4; break;
            case 62501: integerBitBegin = 5; break;
            case 62502: integerBitBegin = 6; break;
            case 62503: integerBitBegin = 7; break;
            case 62504: integerBitBegin = 8; break;
            case 62505: integerBitBegin = 9; break;
            case 62506: integerBitBegin = 10; break;
            case 62507: integerBitBegin = 11; break;
            case 62508: integerBitBegin = 12; break;
            case 62509: integerBitBegin = 13; break;
            case 62510: integerBitBegin = 14; break;
            case 62511: integerBitBegin = 15; break;
            case 64512: integerBitBegin = 0; break;
            case 64513: integerBitBegin = 1; break;
            case 64514: integerBitBegin = 2; break;
            case 64515: integerBitBegin = 3; break;
            case 64516: integerBitBegin = 4; break;
            case 64517: integerBitBegin = 5; break;
            case 64518: integerBitBegin = 6; break;
            case 64519: integerBitBegin = 7; break;
            case 64520: integerBitBegin = 8; break;
            case 64521: integerBitBegin = 9; break;
            case 64522: integerBitBegin = 10; break;
            case 64523: integerBitBegin = 11; break;
            case 64524: integerBitBegin = 12; break;
            case 64525: integerBitBegin = 13; break;
            case 64526: integerBitBegin = 14; break;
            case 64527: integerBitBegin = 15; break;
            case 66528: integerBitBegin = 0; break;
            case 66529: integerBitBegin = 1; break;
            case 66530: integerBitBegin = 2; break;
            case 66531: integerBitBegin = 3; break;
            case 66532: integerBitBegin = 4; break;
            case 66533: integerBitBegin = 5; break;
            case 66534: integerBitBegin = 6; break;
            case 66535: integerBitBegin = 7; break;
            case 66536: integerBitBegin = 8; break;
            case 66537: integerBitBegin = 9; break;
            case 66538: integerBitBegin = 10; break;
            case 66539: integerBitBegin = 11; break;
            case 66540: integerBitBegin = 12; break;
            case 66541: integerBitBegin = 13; break;
            case 66542: integerBitBegin = 14; break;
            case 66543: integerBitBegin = 15; break;
            case 68544: integerBitBegin = 0; break;
            case 68545: integerBitBegin = 1; break;
            case 68546: integerBitBegin = 2; break;
            case 68547: integerBitBegin = 3; break;
            case 68548: integerBitBegin = 4; break;
            case 68549: integerBitBegin = 5; break;
            case 68550: integerBitBegin = 6; break;
            case 68551: integerBitBegin = 7; break;
            case 68552: integerBitBegin = 8; break;
            case 68553: integerBitBegin = 9; break;
            case 68554: integerBitBegin = 10; break;
            case 68555: integerBitBegin = 11; break;
            case 68556: integerBitBegin = 12; break;
            case 68557: integerBitBegin = 13; break;
            case 68558: integerBitBegin = 14; break;
            case 68559: integerBitBegin = 15; break;
            case 70560: integerBitBegin = 0; break;
            case 70561: integerBitBegin = 1; break;
            case 70562: integerBitBegin = 2; break;
            case 70563: integerBitBegin = 3; break;
            case 70564: integerBitBegin = 4; break;
            case 70565: integerBitBegin = 5; break;
            case 70566: integerBitBegin = 6; break;
            case 70567: integerBitBegin = 7; break;
            case 70568: integerBitBegin = 8; break;
            case 70569: integerBitBegin = 9; break;
            case 70570: integerBitBegin = 10; break;
            case 70571: integerBitBegin = 11; break;
            case 70572: integerBitBegin = 12; break;
            case 70573: integerBitBegin = 13; break;
            case 70574: integerBitBegin = 14; break;
            case 70575: integerBitBegin = 15; break;
            case 72576: integerBitBegin = 0; break;
            case 72577: integerBitBegin = 1; break;
            case 72578: integerBitBegin = 2; break;
            case 72579: integerBitBegin = 3; break;
            case 72580: integerBitBegin = 4; break;
            case 72581: integerBitBegin = 5; break;
            case 72582: integerBitBegin = 6; break;
            case 72583: integerBitBegin = 7; break;
            case 72584: integerBitBegin = 8; break;
            case 72585: integerBitBegin = 9; break;
            case 72586: integerBitBegin = 10; break;
            case 72587: integerBitBegin = 11; break;
            case 72588: integerBitBegin = 12; break;
            case 72589: integerBitBegin = 13; break;
            case 72590: integerBitBegin = 14; break;
            case 72591: integerBitBegin = 15; break;
            case 74592: integerBitBegin = 0; break;
            case 74593: integerBitBegin = 1; break;
            case 74594: integerBitBegin = 2; break;
            case 74595: integerBitBegin = 3; break;
            case 74596: integerBitBegin = 4; break;
            case 74597: integerBitBegin = 5; break;
            case 74598: integerBitBegin = 6; break;
            case 74599: integerBitBegin = 7; break;
            case 74600: integerBitBegin = 8; break;
            case 74601: integerBitBegin = 9; break;
            case 74602: integerBitBegin = 10; break;
            case 74603: integerBitBegin = 11; break;
            case 74604: integerBitBegin = 12; break;
            case 74605: integerBitBegin = 13; break;
            case 74606: integerBitBegin = 14; break;
            case 74607: integerBitBegin = 15; break;
            case 76608: integerBitBegin = 0; break;
            case 76609: integerBitBegin = 1; break;
            case 76610: integerBitBegin = 2; break;
            case 76611: integerBitBegin = 3; break;
            case 76612: integerBitBegin = 4; break;
            case 76613: integerBitBegin = 5; break;
            case 76614: integerBitBegin = 6; break;
            case 76615: integerBitBegin = 7; break;
            case 76616: integerBitBegin = 8; break;
            case 76617: integerBitBegin = 9; break;
            case 76618: integerBitBegin = 10; break;
            case 76619: integerBitBegin = 11; break;
            case 76620: integerBitBegin = 12; break;
            case 76621: integerBitBegin = 13; break;
            case 76622: integerBitBegin = 14; break;
            case 76623: integerBitBegin = 15; break;
            case 78624: integerBitBegin = 0; break;
            case 78625: integerBitBegin = 1; break;
            case 78626: integerBitBegin = 2; break;
            case 78627: integerBitBegin = 3; break;
            case 78628: integerBitBegin = 4; break;
            case 78629: integerBitBegin = 5; break;
            case 78630: integerBitBegin = 6; break;
            case 78631: integerBitBegin = 7; break;
            case 78632: integerBitBegin = 8; break;
            case 78633: integerBitBegin = 9; break;
            case 78634: integerBitBegin = 10; break;
            case 78635: integerBitBegin = 11; break;
            case 78636: integerBitBegin = 12; break;
            case 78637: integerBitBegin = 13; break;
            case 78638: integerBitBegin = 14; break;
            case 78639: integerBitBegin = 15; break;
            case 80640: integerBitBegin = 0; break;
            case 80641: integerBitBegin = 1; break;
            case 80642: integerBitBegin = 2; break;
            case 80643: integerBitBegin = 3; break;
            case 80644: integerBitBegin = 4; break;
            case 80645: integerBitBegin = 5; break;
            case 80646: integerBitBegin = 6; break;
            case 80647: integerBitBegin = 7; break;
            case 80648: integerBitBegin = 8; break;
            case 80649: integerBitBegin = 9; break;
            case 80650: integerBitBegin = 10; break;
            case 80651: integerBitBegin = 11; break;
            case 80652: integerBitBegin = 12; break;
            case 80653: integerBitBegin = 13; break;
            case 80654: integerBitBegin = 14; break;
            case 80655: integerBitBegin = 15; break;
            case 82656: integerBitBegin = 0; break;
            case 82657: integerBitBegin = 1; break;
            case 82658: integerBitBegin = 2; break;
            case 82659: integerBitBegin = 3; break;
            case 82660: integerBitBegin = 4; break;
            case 82661: integerBitBegin = 5; break;
            case 82662: integerBitBegin = 6; break;
            case 82663: integerBitBegin = 7; break;
            case 82664: integerBitBegin = 8; break;
            case 82665: integerBitBegin = 9; break;
            case 82666: integerBitBegin = 10; break;
            case 82667: integerBitBegin = 11; break;
            case 82668: integerBitBegin = 12; break;
            case 82669: integerBitBegin = 13; break;
            case 82670: integerBitBegin = 14; break;
            case 82671: integerBitBegin = 15; break;
            case 84672: integerBitBegin = 0; break;
            case 84673: integerBitBegin = 1; break;
            case 84674: integerBitBegin = 2; break;
            case 84675: integerBitBegin = 3; break;
            case 84676: integerBitBegin = 4; break;
            case 84677: integerBitBegin = 5; break;
            case 84678: integerBitBegin = 6; break;
            case 84679: integerBitBegin = 7; break;
            case 84680: integerBitBegin = 8; break;
            case 84681: integerBitBegin = 9; break;
            case 84682: integerBitBegin = 10; break;
            case 84683: integerBitBegin = 11; break;
            case 84684: integerBitBegin = 12; break;
            case 84685: integerBitBegin = 13; break;
            case 84686: integerBitBegin = 14; break;
            case 84687: integerBitBegin = 15; break;
            case 86688: integerBitBegin = 0; break;
            case 86689: integerBitBegin = 1; break;
            case 86690: integerBitBegin = 2; break;
            case 86691: integerBitBegin = 3; break;
            case 86692: integerBitBegin = 4; break;
            case 86693: integerBitBegin = 5; break;
            case 86694: integerBitBegin = 6; break;
            case 86695: integerBitBegin = 7; break;
            case 86696: integerBitBegin = 8; break;
            case 86697: integerBitBegin = 9; break;
            case 86698: integerBitBegin = 10; break;
            case 86699: integerBitBegin = 11; break;
            case 86700: integerBitBegin = 12; break;
            case 86701: integerBitBegin = 13; break;
            case 86702: integerBitBegin = 14; break;
            case 86703: integerBitBegin = 15; break;
            case 88704: integerBitBegin = 0; break;
            case 88705: integerBitBegin = 1; break;
            case 88706: integerBitBegin = 2; break;
            case 88707: integerBitBegin = 3; break;
            case 88708: integerBitBegin = 4; break;
            case 88709: integerBitBegin = 5; break;
            case 88710: integerBitBegin = 6; break;
            case 88711: integerBitBegin = 7; break;
            case 88712: integerBitBegin = 8; break;
            case 88713: integerBitBegin = 9; break;
            case 88714: integerBitBegin = 10; break;
            case 88715: integerBitBegin = 11; break;
            case 88716: integerBitBegin = 12; break;
            case 88717: integerBitBegin = 13; break;
            case 88718: integerBitBegin = 14; break;
            case 88719: integerBitBegin = 15; break;
            case 90720: integerBitBegin = 0; break;
            case 90721: integerBitBegin = 1; break;
            case 90722: integerBitBegin = 2; break;
            case 90723: integerBitBegin = 3; break;
            case 90724: integerBitBegin = 4; break;
            case 90725: integerBitBegin = 5; break;
            case 90726: integerBitBegin = 6; break;
            case 90727: integerBitBegin = 7; break;
            case 90728: integerBitBegin = 8; break;
            case 90729: integerBitBegin = 9; break;
            case 90730: integerBitBegin = 10; break;
            case 90731: integerBitBegin = 11; break;
            case 90732: integerBitBegin = 12; break;
            case 90733: integerBitBegin = 13; break;
            case 90734: integerBitBegin = 14; break;
            case 90735: integerBitBegin = 15; break;
            case 92736: integerBitBegin = 0; break;
            case 92737: integerBitBegin = 1; break;
            case 92738: integerBitBegin = 2; break;
            case 92739: integerBitBegin = 3; break;
            case 92740: integerBitBegin = 4; break;
            case 92741: integerBitBegin = 5; break;
            case 92742: integerBitBegin = 6; break;
            case 92743: integerBitBegin = 7; break;
            case 92744: integerBitBegin = 8; break;
            case 92745: integerBitBegin = 9; break;
            case 92746: integerBitBegin = 10; break;
            case 92747: integerBitBegin = 11; break;
            case 92748: integerBitBegin = 12; break;
            case 92749: integerBitBegin = 13; break;
            case 92750: integerBitBegin = 14; break;
            case 92751: integerBitBegin = 15; break;
            case 94752: integerBitBegin = 0; break;
            case 94753: integerBitBegin = 1; break;
            case 94754: integerBitBegin = 2; break;
            case 94755: integerBitBegin = 3; break;
            case 94756: integerBitBegin = 4; break;
            case 94757: integerBitBegin = 5; break;
            case 94758: integerBitBegin = 6; break;
            case 94759: integerBitBegin = 7; break;
            case 94760: integerBitBegin = 8; break;
            case 94761: integerBitBegin = 9; break;
            case 94762: integerBitBegin = 10; break;
            case 94763: integerBitBegin = 11; break;
            case 94764: integerBitBegin = 12; break;
            case 94765: integerBitBegin = 13; break;
            case 94766: integerBitBegin = 14; break;
            case 94767: integerBitBegin = 15; break;
            case 96768: integerBitBegin = 0; break;
            case 96769: integerBitBegin = 1; break;
            case 96770: integerBitBegin = 2; break;
            case 96771: integerBitBegin = 3; break;
            case 96772: integerBitBegin = 4; break;
            case 96773: integerBitBegin = 5; break;
            case 96774: integerBitBegin = 6; break;
            case 96775: integerBitBegin = 7; break;
            case 96776: integerBitBegin = 8; break;
            case 96777: integerBitBegin = 9; break;
            case 96778: integerBitBegin = 10; break;
            case 96779: integerBitBegin = 11; break;
            case 96780: integerBitBegin = 12; break;
            case 96781: integerBitBegin = 13; break;
            case 96782: integerBitBegin = 14; break;
            case 96783: integerBitBegin = 15; break;
            case 98784: integerBitBegin = 0; break;
            case 98785: integerBitBegin = 1; break;
            case 98786: integerBitBegin = 2; break;
            case 98787: integerBitBegin = 3; break;
            case 98788: integerBitBegin = 4; break;
            case 98789: integerBitBegin = 5; break;
            case 98790: integerBitBegin = 6; break;
            case 98791: integerBitBegin = 7; break;
            case 98792: integerBitBegin = 8; break;
            case 98793: integerBitBegin = 9; break;
            case 98794: integerBitBegin = 10; break;
            case 98795: integerBitBegin = 11; break;
            case 98796: integerBitBegin = 12; break;
            case 98797: integerBitBegin = 13; break;
            case 98798: integerBitBegin = 14; break;
            case 98799: integerBitBegin = 15; break;
            case 100800: integerBitBegin = 0; break;
            case 100801: integerBitBegin = 1; break;
            case 100802: integerBitBegin = 2; break;
            case 100803: integerBitBegin = 3; break;
            case 100804: integerBitBegin = 4; break;
            case 100805: integerBitBegin = 5; break;
            case 100806: integerBitBegin = 6; break;
            case 100807: integerBitBegin = 7; break;
            case 100808: integerBitBegin = 8; break;
            case 100809: integerBitBegin = 9; break;
            case 100810: integerBitBegin = 10; break;
            case 100811: integerBitBegin = 11; break;
            case 100812: integerBitBegin = 12; break;
            case 100813: integerBitBegin = 13; break;
            case 100814: integerBitBegin = 14; break;
            case 100815: integerBitBegin = 15; break;
            case 102816: integerBitBegin = 0; break;
            case 102817: integerBitBegin = 1; break;
            case 102818: integerBitBegin = 2; break;
            case 102819: integerBitBegin = 3; break;
            case 102820: integerBitBegin = 4; break;
            case 102821: integerBitBegin = 5; break;
            case 102822: integerBitBegin = 6; break;
            case 102823: integerBitBegin = 7; break;
            case 102824: integerBitBegin = 8; break;
            case 102825: integerBitBegin = 9; break;
            case 102826: integerBitBegin = 10; break;
            case 102827: integerBitBegin = 11; break;
            case 102828: integerBitBegin = 12; break;
            case 102829: integerBitBegin = 13; break;
            case 102830: integerBitBegin = 14; break;
            case 102831: integerBitBegin = 15; break;
            case 104832: integerBitBegin = 0; break;
            case 104833: integerBitBegin = 1; break;
            case 104834: integerBitBegin = 2; break;
            case 104835: integerBitBegin = 3; break;
            case 104836: integerBitBegin = 4; break;
            case 104837: integerBitBegin = 5; break;
            case 104838: integerBitBegin = 6; break;
            case 104839: integerBitBegin = 7; break;
            case 104840: integerBitBegin = 8; break;
            case 104841: integerBitBegin = 9; break;
            case 104842: integerBitBegin = 10; break;
            case 104843: integerBitBegin = 11; break;
            case 104844: integerBitBegin = 12; break;
            case 104845: integerBitBegin = 13; break;
            case 104846: integerBitBegin = 14; break;
            case 104847: integerBitBegin = 15; break;
            case 106848: integerBitBegin = 0; break;
            case 106849: integerBitBegin = 1; break;
            case 106850: integerBitBegin = 2; break;
            case 106851: integerBitBegin = 3; break;
            case 106852: integerBitBegin = 4; break;
            case 106853: integerBitBegin = 5; break;
            case 106854: integerBitBegin = 6; break;
            case 106855: integerBitBegin = 7; break;
            case 106856: integerBitBegin = 8; break;
            case 106857: integerBitBegin = 9; break;
            case 106858: integerBitBegin = 10; break;
            case 106859: integerBitBegin = 11; break;
            case 106860: integerBitBegin = 12; break;
            case 106861: integerBitBegin = 13; break;
            case 106862: integerBitBegin = 14; break;
            case 106863: integerBitBegin = 15; break;
            case 108864: integerBitBegin = 0; break;
            case 108865: integerBitBegin = 1; break;
            case 108866: integerBitBegin = 2; break;
            case 108867: integerBitBegin = 3; break;
            case 108868: integerBitBegin = 4; break;
            case 108869: integerBitBegin = 5; break;
            case 108870: integerBitBegin = 6; break;
            case 108871: integerBitBegin = 7; break;
            case 108872: integerBitBegin = 8; break;
            case 108873: integerBitBegin = 9; break;
            case 108874: integerBitBegin = 10; break;
            case 108875: integerBitBegin = 11; break;
            case 108876: integerBitBegin = 12; break;
            case 108877: integerBitBegin = 13; break;
            case 108878: integerBitBegin = 14; break;
            case 108879: integerBitBegin = 15; break;
            case 110880: integerBitBegin = 0; break;
            case 110881: integerBitBegin = 1; break;
            case 110882: integerBitBegin = 2; break;
            case 110883: integerBitBegin = 3; break;
            case 110884: integerBitBegin = 4; break;
            case 110885: integerBitBegin = 5; break;
            case 110886: integerBitBegin = 6; break;
            case 110887: integerBitBegin = 7; break;
            case 110888: integerBitBegin = 8; break;
            case 110889: integerBitBegin = 9; break;
            case 110890: integerBitBegin = 10; break;
            case 110891: integerBitBegin = 11; break;
            case 110892: integerBitBegin = 12; break;
            case 110893: integerBitBegin = 13; break;
            case 110894: integerBitBegin = 14; break;
            case 110895: integerBitBegin = 15; break;
            case 112896: integerBitBegin = 0; break;
            case 112897: integerBitBegin = 1; break;
            case 112898: integerBitBegin = 2; break;
            case 112899: integerBitBegin = 3; break;
            case 112900: integerBitBegin = 4; break;
            case 112901: integerBitBegin = 5; break;
            case 112902: integerBitBegin = 6; break;
            case 112903: integerBitBegin = 7; break;
            case 112904: integerBitBegin = 8; break;
            case 112905: integerBitBegin = 9; break;
            case 112906: integerBitBegin = 10; break;
            case 112907: integerBitBegin = 11; break;
            case 112908: integerBitBegin = 12; break;
            case 112909: integerBitBegin = 13; break;
            case 112910: integerBitBegin = 14; break;
            case 112911: integerBitBegin = 15; break;
            case 114912: integerBitBegin = 0; break;
            case 114913: integerBitBegin = 1; break;
            case 114914: integerBitBegin = 2; break;
            case 114915: integerBitBegin = 3; break;
            case 114916: integerBitBegin = 4; break;
            case 114917: integerBitBegin = 5; break;
            case 114918: integerBitBegin = 6; break;
            case 114919: integerBitBegin = 7; break;
            case 114920: integerBitBegin = 8; break;
            case 114921: integerBitBegin = 9; break;
            case 114922: integerBitBegin = 10; break;
            case 114923: integerBitBegin = 11; break;
            case 114924: integerBitBegin = 12; break;
            case 114925: integerBitBegin = 13; break;
            case 114926: integerBitBegin = 14; break;
            case 114927: integerBitBegin = 15; break;
            case 116928: integerBitBegin = 0; break;
            case 116929: integerBitBegin = 1; break;
            case 116930: integerBitBegin = 2; break;
            case 116931: integerBitBegin = 3; break;
            case 116932: integerBitBegin = 4; break;
            case 116933: integerBitBegin = 5; break;
            case 116934: integerBitBegin = 6; break;
            case 116935: integerBitBegin = 7; break;
            case 116936: integerBitBegin = 8; break;
            case 116937: integerBitBegin = 9; break;
            case 116938: integerBitBegin = 10; break;
            case 116939: integerBitBegin = 11; break;
            case 116940: integerBitBegin = 12; break;
            case 116941: integerBitBegin = 13; break;
            case 116942: integerBitBegin = 14; break;
            case 116943: integerBitBegin = 15; break;
            case 118944: integerBitBegin = 0; break;
            case 118945: integerBitBegin = 1; break;
            case 118946: integerBitBegin = 2; break;
            case 118947: integerBitBegin = 3; break;
            case 118948: integerBitBegin = 4; break;
            case 118949: integerBitBegin = 5; break;
            case 118950: integerBitBegin = 6; break;
            case 118951: integerBitBegin = 7; break;
            case 118952: integerBitBegin = 8; break;
            case 118953: integerBitBegin = 9; break;
            case 118954: integerBitBegin = 10; break;
            case 118955: integerBitBegin = 11; break;
            case 118956: integerBitBegin = 12; break;
            case 118957: integerBitBegin = 13; break;
            case 118958: integerBitBegin = 14; break;
            case 118959: integerBitBegin = 15; break;
            case 120960: integerBitBegin = 0; break;
            case 120961: integerBitBegin = 1; break;
            case 120962: integerBitBegin = 2; break;
            case 120963: integerBitBegin = 3; break;
            case 120964: integerBitBegin = 4; break;
            case 120965: integerBitBegin = 5; break;
            case 120966: integerBitBegin = 6; break;
            case 120967: integerBitBegin = 7; break;
            case 120968: integerBitBegin = 8; break;
            case 120969: integerBitBegin = 9; break;
            case 120970: integerBitBegin = 10; break;
            case 120971: integerBitBegin = 11; break;
            case 120972: integerBitBegin = 12; break;
            case 120973: integerBitBegin = 13; break;
            case 120974: integerBitBegin = 14; break;
            case 120975: integerBitBegin = 15; break;
            case 122976: integerBitBegin = 0; break;
            case 122977: integerBitBegin = 1; break;
            case 122978: integerBitBegin = 2; break;
            case 122979: integerBitBegin = 3; break;
            case 122980: integerBitBegin = 4; break;
            case 122981: integerBitBegin = 5; break;
            case 122982: integerBitBegin = 6; break;
            case 122983: integerBitBegin = 7; break;
            case 122984: integerBitBegin = 8; break;
            case 122985: integerBitBegin = 9; break;
            case 122986: integerBitBegin = 10; break;
            case 122987: integerBitBegin = 11; break;
            case 122988: integerBitBegin = 12; break;
            case 122989: integerBitBegin = 13; break;
            case 122990: integerBitBegin = 14; break;
            case 122991: integerBitBegin = 15; break;
            case 124992: integerBitBegin = 0; break;
            case 124993: integerBitBegin = 1; break;
            case 124994: integerBitBegin = 2; break;
            case 124995: integerBitBegin = 3; break;
            case 124996: integerBitBegin = 4; break;
            case 124997: integerBitBegin = 5; break;
            case 124998: integerBitBegin = 6; break;
            case 124999: integerBitBegin = 7; break;
            case 125000: integerBitBegin = 8; break;
            case 125001: integerBitBegin = 9; break;
            case 125002: integerBitBegin = 10; break;
            case 125003: integerBitBegin = 11; break;
            case 125004: integerBitBegin = 12; break;
            case 125005: integerBitBegin = 13; break;
            case 125006: integerBitBegin = 14; break;
            case 125007: integerBitBegin = 15; break;
            case 127008: integerBitBegin = 0; break;
            case 127009: integerBitBegin = 1; break;
            case 127010: integerBitBegin = 2; break;
            case 127011: integerBitBegin = 3; break;
            case 127012: integerBitBegin = 4; break;
            case 127013: integerBitBegin = 5; break;
            case 127014: integerBitBegin = 6; break;
            case 127015: integerBitBegin = 7; break;
            case 127016: integerBitBegin = 8; break;
            case 127017: integerBitBegin = 9; break;
            case 127018: integerBitBegin = 10; break;
            case 127019: integerBitBegin = 11; break;
            case 127020: integerBitBegin = 12; break;
            case 127021: integerBitBegin = 13; break;
            case 127022: integerBitBegin = 14; break;
            case 127023: integerBitBegin = 15; break;
        }       
        switch (bit2) {
            case 0: integerBitEnd = 0; break;
            case 1: integerBitEnd = 1; break;
            case 2: integerBitEnd = 2; break;
            case 3: integerBitEnd = 3; break;
            case 4: integerBitEnd = 4; break;
            case 5: integerBitEnd = 5; break;
            case 6: integerBitEnd = 6; break;
            case 7: integerBitEnd = 7; break;
            case 8: integerBitEnd = 8; break;
            case 9: integerBitEnd = 9; break;
            case 10: integerBitEnd = 10; break;
            case 11: integerBitEnd = 11; break;
            case 12: integerBitEnd = 12; break;
            case 13: integerBitEnd = 13; break;
            case 14: integerBitEnd = 14; break;
            case 15: integerBitEnd = 15; break;
            case 2016: integerBitEnd = 0; break;
            case 2017: integerBitEnd = 1; break;
            case 2018: integerBitEnd = 2; break;
            case 2019: integerBitEnd = 3; break;
            case 2020: integerBitEnd = 4; break;
            case 2021: integerBitEnd = 5; break;
            case 2022: integerBitEnd = 6; break;
            case 2023: integerBitEnd = 7; break;
            case 2024: integerBitEnd = 8; break;
            case 2025: integerBitEnd = 9; break;
            case 2026: integerBitEnd = 10; break;
            case 2027: integerBitEnd = 11; break;
            case 2028: integerBitEnd = 12; break;
            case 2029: integerBitEnd = 13; break;
            case 2030: integerBitEnd = 14; break;
            case 2031: integerBitEnd = 15; break;
            case 4032: integerBitEnd = 0; break;
            case 4033: integerBitEnd = 1; break;
            case 4034: integerBitEnd = 2; break;
            case 4035: integerBitEnd = 3; break;
            case 4036: integerBitEnd = 4; break;
            case 4037: integerBitEnd = 5; break;
            case 4038: integerBitEnd = 6; break;
            case 4039: integerBitEnd = 7; break;
            case 4040: integerBitEnd = 8; break;
            case 4041: integerBitEnd = 9; break;
            case 4042: integerBitEnd = 10; break;
            case 4043: integerBitEnd = 11; break;
            case 4044: integerBitEnd = 12; break;
            case 4045: integerBitEnd = 13; break;
            case 4046: integerBitEnd = 14; break;
            case 4047: integerBitEnd = 15; break;
            case 6048: integerBitEnd = 0; break;
            case 6049: integerBitEnd = 1; break;
            case 6050: integerBitEnd = 2; break;
            case 6051: integerBitEnd = 3; break;
            case 6052: integerBitEnd = 4; break;
            case 6053: integerBitEnd = 5; break;
            case 6054: integerBitEnd = 6; break;
            case 6055: integerBitEnd = 7; break;
            case 6056: integerBitEnd = 8; break;
            case 6057: integerBitEnd = 9; break;
            case 6058: integerBitEnd = 10; break;
            case 6059: integerBitEnd = 11; break;
            case 6060: integerBitEnd = 12; break;
            case 6061: integerBitEnd = 13; break;
            case 6062: integerBitEnd = 14; break;
            case 6063: integerBitEnd = 15; break;
            case 8064: integerBitEnd = 0; break;
            case 8065: integerBitEnd = 1; break;
            case 8066: integerBitEnd = 2; break;
            case 8067: integerBitEnd = 3; break;
            case 8068: integerBitEnd = 4; break;
            case 8069: integerBitEnd = 5; break;
            case 8070: integerBitEnd = 6; break;
            case 8071: integerBitEnd = 7; break;
            case 8072: integerBitEnd = 8; break;
            case 8073: integerBitEnd = 9; break;
            case 8074: integerBitEnd = 10; break;
            case 8075: integerBitEnd = 11; break;
            case 8076: integerBitEnd = 12; break;
            case 8077: integerBitEnd = 13; break;
            case 8078: integerBitEnd = 14; break;
            case 8079: integerBitEnd = 15; break;
            case 10080: integerBitEnd = 0; break;
            case 10081: integerBitEnd = 1; break;
            case 10082: integerBitEnd = 2; break;
            case 10083: integerBitEnd = 3; break;
            case 10084: integerBitEnd = 4; break;
            case 10085: integerBitEnd = 5; break;
            case 10086: integerBitEnd = 6; break;
            case 10087: integerBitEnd = 7; break;
            case 10088: integerBitEnd = 8; break;
            case 10089: integerBitEnd = 9; break;
            case 10090: integerBitEnd = 10; break;
            case 10091: integerBitEnd = 11; break;
            case 10092: integerBitEnd = 12; break;
            case 10093: integerBitEnd = 13; break;
            case 10094: integerBitEnd = 14; break;
            case 10095: integerBitEnd = 15; break;
            case 12096: integerBitEnd = 0; break;
            case 12097: integerBitEnd = 1; break;
            case 12098: integerBitEnd = 2; break;
            case 12099: integerBitEnd = 3; break;
            case 12100: integerBitEnd = 4; break;
            case 12101: integerBitEnd = 5; break;
            case 12102: integerBitEnd = 6; break;
            case 12103: integerBitEnd = 7; break;
            case 12104: integerBitEnd = 8; break;
            case 12105: integerBitEnd = 9; break;
            case 12106: integerBitEnd = 10; break;
            case 12107: integerBitEnd = 11; break;
            case 12108: integerBitEnd = 12; break;
            case 12109: integerBitEnd = 13; break;
            case 12110: integerBitEnd = 14; break;
            case 12111: integerBitEnd = 15; break;
            case 14112: integerBitEnd = 0; break;
            case 14113: integerBitEnd = 1; break;
            case 14114: integerBitEnd = 2; break;
            case 14115: integerBitEnd = 3; break;
            case 14116: integerBitEnd = 4; break;
            case 14117: integerBitEnd = 5; break;
            case 14118: integerBitEnd = 6; break;
            case 14119: integerBitEnd = 7; break;
            case 14120: integerBitEnd = 8; break;
            case 14121: integerBitEnd = 9; break;
            case 14122: integerBitEnd = 10; break;
            case 14123: integerBitEnd = 11; break;
            case 14124: integerBitEnd = 12; break;
            case 14125: integerBitEnd = 13; break;
            case 14126: integerBitEnd = 14; break;
            case 14127: integerBitEnd = 15; break;
            case 16128: integerBitEnd = 0; break;
            case 16129: integerBitEnd = 1; break;
            case 16130: integerBitEnd = 2; break;
            case 16131: integerBitEnd = 3; break;
            case 16132: integerBitEnd = 4; break;
            case 16133: integerBitEnd = 5; break;
            case 16134: integerBitEnd = 6; break;
            case 16135: integerBitEnd = 7; break;
            case 16136: integerBitEnd = 8; break;
            case 16137: integerBitEnd = 9; break;
            case 16138: integerBitEnd = 10; break;
            case 16139: integerBitEnd = 11; break;
            case 16140: integerBitEnd = 12; break;
            case 16141: integerBitEnd = 13; break;
            case 16142: integerBitEnd = 14; break;
            case 16143: integerBitEnd = 15; break;
            case 18144: integerBitEnd = 0; break;
            case 18145: integerBitEnd = 1; break;
            case 18146: integerBitEnd = 2; break;
            case 18147: integerBitEnd = 3; break;
            case 18148: integerBitEnd = 4; break;
            case 18149: integerBitEnd = 5; break;
            case 18150: integerBitEnd = 6; break;
            case 18151: integerBitEnd = 7; break;
            case 18152: integerBitEnd = 8; break;
            case 18153: integerBitEnd = 9; break;
            case 18154: integerBitEnd = 10; break;
            case 18155: integerBitEnd = 11; break;
            case 18156: integerBitEnd = 12; break;
            case 18157: integerBitEnd = 13; break;
            case 18158: integerBitEnd = 14; break;
            case 18159: integerBitEnd = 15; break;
            case 20160: integerBitEnd = 0; break;
            case 20161: integerBitEnd = 1; break;
            case 20162: integerBitEnd = 2; break;
            case 20163: integerBitEnd = 3; break;
            case 20164: integerBitEnd = 4; break;
            case 20165: integerBitEnd = 5; break;
            case 20166: integerBitEnd = 6; break;
            case 20167: integerBitEnd = 7; break;
            case 20168: integerBitEnd = 8; break;
            case 20169: integerBitEnd = 9; break;
            case 20170: integerBitEnd = 10; break;
            case 20171: integerBitEnd = 11; break;
            case 20172: integerBitEnd = 12; break;
            case 20173: integerBitEnd = 13; break;
            case 20174: integerBitEnd = 14; break;
            case 20175: integerBitEnd = 15; break;
            case 22176: integerBitEnd = 0; break;
            case 22177: integerBitEnd = 1; break;
            case 22178: integerBitEnd = 2; break;
            case 22179: integerBitEnd = 3; break;
            case 22180: integerBitEnd = 4; break;
            case 22181: integerBitEnd = 5; break;
            case 22182: integerBitEnd = 6; break;
            case 22183: integerBitEnd = 7; break;
            case 22184: integerBitEnd = 8; break;
            case 22185: integerBitEnd = 9; break;
            case 22186: integerBitEnd = 10; break;
            case 22187: integerBitEnd = 11; break;
            case 22188: integerBitEnd = 12; break;
            case 22189: integerBitEnd = 13; break;
            case 22190: integerBitEnd = 14; break;
            case 22191: integerBitEnd = 15; break;
            case 24192: integerBitEnd = 0; break;
            case 24193: integerBitEnd = 1; break;
            case 24194: integerBitEnd = 2; break;
            case 24195: integerBitEnd = 3; break;
            case 24196: integerBitEnd = 4; break;
            case 24197: integerBitEnd = 5; break;
            case 24198: integerBitEnd = 6; break;
            case 24199: integerBitEnd = 7; break;
            case 24200: integerBitEnd = 8; break;
            case 24201: integerBitEnd = 9; break;
            case 24202: integerBitEnd = 10; break;
            case 24203: integerBitEnd = 11; break;
            case 24204: integerBitEnd = 12; break;
            case 24205: integerBitEnd = 13; break;
            case 24206: integerBitEnd = 14; break;
            case 24207: integerBitEnd = 15; break;
            case 26208: integerBitEnd = 0; break;
            case 26209: integerBitEnd = 1; break;
            case 26210: integerBitEnd = 2; break;
            case 26211: integerBitEnd = 3; break;
            case 26212: integerBitEnd = 4; break;
            case 26213: integerBitEnd = 5; break;
            case 26214: integerBitEnd = 6; break;
            case 26215: integerBitEnd = 7; break;
            case 26216: integerBitEnd = 8; break;
            case 26217: integerBitEnd = 9; break;
            case 26218: integerBitEnd = 10; break;
            case 26219: integerBitEnd = 11; break;
            case 26220: integerBitEnd = 12; break;
            case 26221: integerBitEnd = 13; break;
            case 26222: integerBitEnd = 14; break;
            case 26223: integerBitEnd = 15; break;
            case 28224: integerBitEnd = 0; break;
            case 28225: integerBitEnd = 1; break;
            case 28226: integerBitEnd = 2; break;
            case 28227: integerBitEnd = 3; break;
            case 28228: integerBitEnd = 4; break;
            case 28229: integerBitEnd = 5; break;
            case 28230: integerBitEnd = 6; break;
            case 28231: integerBitEnd = 7; break;
            case 28232: integerBitEnd = 8; break;
            case 28233: integerBitEnd = 9; break;
            case 28234: integerBitEnd = 10; break;
            case 28235: integerBitEnd = 11; break;
            case 28236: integerBitEnd = 12; break;
            case 28237: integerBitEnd = 13; break;
            case 28238: integerBitEnd = 14; break;
            case 28239: integerBitEnd = 15; break;
            case 30240: integerBitEnd = 0; break;
            case 30241: integerBitEnd = 1; break;
            case 30242: integerBitEnd = 2; break;
            case 30243: integerBitEnd = 3; break;
            case 30244: integerBitEnd = 4; break;
            case 30245: integerBitEnd = 5; break;
            case 30246: integerBitEnd = 6; break;
            case 30247: integerBitEnd = 7; break;
            case 30248: integerBitEnd = 8; break;
            case 30249: integerBitEnd = 9; break;
            case 30250: integerBitEnd = 10; break;
            case 30251: integerBitEnd = 11; break;
            case 30252: integerBitEnd = 12; break;
            case 30253: integerBitEnd = 13; break;
            case 30254: integerBitEnd = 14; break;
            case 30255: integerBitEnd = 15; break;
            case 32256: integerBitEnd = 0; break;
            case 32257: integerBitEnd = 1; break;
            case 32258: integerBitEnd = 2; break;
            case 32259: integerBitEnd = 3; break;
            case 32260: integerBitEnd = 4; break;
            case 32261: integerBitEnd = 5; break;
            case 32262: integerBitEnd = 6; break;
            case 32263: integerBitEnd = 7; break;
            case 32264: integerBitEnd = 8; break;
            case 32265: integerBitEnd = 9; break;
            case 32266: integerBitEnd = 10; break;
            case 32267: integerBitEnd = 11; break;
            case 32268: integerBitEnd = 12; break;
            case 32269: integerBitEnd = 13; break;
            case 32270: integerBitEnd = 14; break;
            case 32271: integerBitEnd = 15; break;
            case 34272: integerBitEnd = 0; break;
            case 34273: integerBitEnd = 1; break;
            case 34274: integerBitEnd = 2; break;
            case 34275: integerBitEnd = 3; break;
            case 34276: integerBitEnd = 4; break;
            case 34277: integerBitEnd = 5; break;
            case 34278: integerBitEnd = 6; break;
            case 34279: integerBitEnd = 7; break;
            case 34280: integerBitEnd = 8; break;
            case 34281: integerBitEnd = 9; break;
            case 34282: integerBitEnd = 10; break;
            case 34283: integerBitEnd = 11; break;
            case 34284: integerBitEnd = 12; break;
            case 34285: integerBitEnd = 13; break;
            case 34286: integerBitEnd = 14; break;
            case 34287: integerBitEnd = 15; break;
            case 36288: integerBitEnd = 0; break;
            case 36289: integerBitEnd = 1; break;
            case 36290: integerBitEnd = 2; break;
            case 36291: integerBitEnd = 3; break;
            case 36292: integerBitEnd = 4; break;
            case 36293: integerBitEnd = 5; break;
            case 36294: integerBitEnd = 6; break;
            case 36295: integerBitEnd = 7; break;
            case 36296: integerBitEnd = 8; break;
            case 36297: integerBitEnd = 9; break;
            case 36298: integerBitEnd = 10; break;
            case 36299: integerBitEnd = 11; break;
            case 36300: integerBitEnd = 12; break;
            case 36301: integerBitEnd = 13; break;
            case 36302: integerBitEnd = 14; break;
            case 36303: integerBitEnd = 15; break;
            case 38304: integerBitEnd = 0; break;
            case 38305: integerBitEnd = 1; break;
            case 38306: integerBitEnd = 2; break;
            case 38307: integerBitEnd = 3; break;
            case 38308: integerBitEnd = 4; break;
            case 38309: integerBitEnd = 5; break;
            case 38310: integerBitEnd = 6; break;
            case 38311: integerBitEnd = 7; break;
            case 38312: integerBitEnd = 8; break;
            case 38313: integerBitEnd = 9; break;
            case 38314: integerBitEnd = 10; break;
            case 38315: integerBitEnd = 11; break;
            case 38316: integerBitEnd = 12; break;
            case 38317: integerBitEnd = 13; break;
            case 38318: integerBitEnd = 14; break;
            case 38319: integerBitEnd = 15; break;
            case 40320: integerBitEnd = 0; break;
            case 40321: integerBitEnd = 1; break;
            case 40322: integerBitEnd = 2; break;
            case 40323: integerBitEnd = 3; break;
            case 40324: integerBitEnd = 4; break;
            case 40325: integerBitEnd = 5; break;
            case 40326: integerBitEnd = 6; break;
            case 40327: integerBitEnd = 7; break;
            case 40328: integerBitEnd = 8; break;
            case 40329: integerBitEnd = 9; break;
            case 40330: integerBitEnd = 10; break;
            case 40331: integerBitEnd = 11; break;
            case 40332: integerBitEnd = 12; break;
            case 40333: integerBitEnd = 13; break;
            case 40334: integerBitEnd = 14; break;
            case 40335: integerBitEnd = 15; break;
            case 42336: integerBitEnd = 0; break;
            case 42337: integerBitEnd = 1; break;
            case 42338: integerBitEnd = 2; break;
            case 42339: integerBitEnd = 3; break;
            case 42340: integerBitEnd = 4; break;
            case 42341: integerBitEnd = 5; break;
            case 42342: integerBitEnd = 6; break;
            case 42343: integerBitEnd = 7; break;
            case 42344: integerBitEnd = 8; break;
            case 42345: integerBitEnd = 9; break;
            case 42346: integerBitEnd = 10; break;
            case 42347: integerBitEnd = 11; break;
            case 42348: integerBitEnd = 12; break;
            case 42349: integerBitEnd = 13; break;
            case 42350: integerBitEnd = 14; break;
            case 42351: integerBitEnd = 15; break;
            case 44352: integerBitEnd = 0; break;
            case 44353: integerBitEnd = 1; break;
            case 44354: integerBitEnd = 2; break;
            case 44355: integerBitEnd = 3; break;
            case 44356: integerBitEnd = 4; break;
            case 44357: integerBitEnd = 5; break;
            case 44358: integerBitEnd = 6; break;
            case 44359: integerBitEnd = 7; break;
            case 44360: integerBitEnd = 8; break;
            case 44361: integerBitEnd = 9; break;
            case 44362: integerBitEnd = 10; break;
            case 44363: integerBitEnd = 11; break;
            case 44364: integerBitEnd = 12; break;
            case 44365: integerBitEnd = 13; break;
            case 44366: integerBitEnd = 14; break;
            case 44367: integerBitEnd = 15; break;
            case 46368: integerBitEnd = 0; break;
            case 46369: integerBitEnd = 1; break;
            case 46370: integerBitEnd = 2; break;
            case 46371: integerBitEnd = 3; break;
            case 46372: integerBitEnd = 4; break;
            case 46373: integerBitEnd = 5; break;
            case 46374: integerBitEnd = 6; break;
            case 46375: integerBitEnd = 7; break;
            case 46376: integerBitEnd = 8; break;
            case 46377: integerBitEnd = 9; break;
            case 46378: integerBitEnd = 10; break;
            case 46379: integerBitEnd = 11; break;
            case 46380: integerBitEnd = 12; break;
            case 46381: integerBitEnd = 13; break;
            case 46382: integerBitEnd = 14; break;
            case 46383: integerBitEnd = 15; break;
            case 48384: integerBitEnd = 0; break;
            case 48385: integerBitEnd = 1; break;
            case 48386: integerBitEnd = 2; break;
            case 48387: integerBitEnd = 3; break;
            case 48388: integerBitEnd = 4; break;
            case 48389: integerBitEnd = 5; break;
            case 48390: integerBitEnd = 6; break;
            case 48391: integerBitEnd = 7; break;
            case 48392: integerBitEnd = 8; break;
            case 48393: integerBitEnd = 9; break;
            case 48394: integerBitEnd = 10; break;
            case 48395: integerBitEnd = 11; break;
            case 48396: integerBitEnd = 12; break;
            case 48397: integerBitEnd = 13; break;
            case 48398: integerBitEnd = 14; break;
            case 48399: integerBitEnd = 15; break;
            case 50400: integerBitEnd = 0; break;
            case 50401: integerBitEnd = 1; break;
            case 50402: integerBitEnd = 2; break;
            case 50403: integerBitEnd = 3; break;
            case 50404: integerBitEnd = 4; break;
            case 50405: integerBitEnd = 5; break;
            case 50406: integerBitEnd = 6; break;
            case 50407: integerBitEnd = 7; break;
            case 50408: integerBitEnd = 8; break;
            case 50409: integerBitEnd = 9; break;
            case 50410: integerBitEnd = 10; break;
            case 50411: integerBitEnd = 11; break;
            case 50412: integerBitEnd = 12; break;
            case 50413: integerBitEnd = 13; break;
            case 50414: integerBitEnd = 14; break;
            case 50415: integerBitEnd = 15; break;
            case 52416: integerBitEnd = 0; break;
            case 52417: integerBitEnd = 1; break;
            case 52418: integerBitEnd = 2; break;
            case 52419: integerBitEnd = 3; break;
            case 52420: integerBitEnd = 4; break;
            case 52421: integerBitEnd = 5; break;
            case 52422: integerBitEnd = 6; break;
            case 52423: integerBitEnd = 7; break;
            case 52424: integerBitEnd = 8; break;
            case 52425: integerBitEnd = 9; break;
            case 52426: integerBitEnd = 10; break;
            case 52427: integerBitEnd = 11; break;
            case 52428: integerBitEnd = 12; break;
            case 52429: integerBitEnd = 13; break;
            case 52430: integerBitEnd = 14; break;
            case 52431: integerBitEnd = 15; break;
            case 54432: integerBitEnd = 0; break;
            case 54433: integerBitEnd = 1; break;
            case 54434: integerBitEnd = 2; break;
            case 54435: integerBitEnd = 3; break;
            case 54436: integerBitEnd = 4; break;
            case 54437: integerBitEnd = 5; break;
            case 54438: integerBitEnd = 6; break;
            case 54439: integerBitEnd = 7; break;
            case 54440: integerBitEnd = 8; break;
            case 54441: integerBitEnd = 9; break;
            case 54442: integerBitEnd = 10; break;
            case 54443: integerBitEnd = 11; break;
            case 54444: integerBitEnd = 12; break;
            case 54445: integerBitEnd = 13; break;
            case 54446: integerBitEnd = 14; break;
            case 54447: integerBitEnd = 15; break;
            case 56448: integerBitEnd = 0; break;
            case 56449: integerBitEnd = 1; break;
            case 56450: integerBitEnd = 2; break;
            case 56451: integerBitEnd = 3; break;
            case 56452: integerBitEnd = 4; break;
            case 56453: integerBitEnd = 5; break;
            case 56454: integerBitEnd = 6; break;
            case 56455: integerBitEnd = 7; break;
            case 56456: integerBitEnd = 8; break;
            case 56457: integerBitEnd = 9; break;
            case 56458: integerBitEnd = 10; break;
            case 56459: integerBitEnd = 11; break;
            case 56460: integerBitEnd = 12; break;
            case 56461: integerBitEnd = 13; break;
            case 56462: integerBitEnd = 14; break;
            case 56463: integerBitEnd = 15; break;
            case 58464: integerBitEnd = 0; break;
            case 58465: integerBitEnd = 1; break;
            case 58466: integerBitEnd = 2; break;
            case 58467: integerBitEnd = 3; break;
            case 58468: integerBitEnd = 4; break;
            case 58469: integerBitEnd = 5; break;
            case 58470: integerBitEnd = 6; break;
            case 58471: integerBitEnd = 7; break;
            case 58472: integerBitEnd = 8; break;
            case 58473: integerBitEnd = 9; break;
            case 58474: integerBitEnd = 10; break;
            case 58475: integerBitEnd = 11; break;
            case 58476: integerBitEnd = 12; break;
            case 58477: integerBitEnd = 13; break;
            case 58478: integerBitEnd = 14; break;
            case 58479: integerBitEnd = 15; break;
            case 60480: integerBitEnd = 0; break;
            case 60481: integerBitEnd = 1; break;
            case 60482: integerBitEnd = 2; break;
            case 60483: integerBitEnd = 3; break;
            case 60484: integerBitEnd = 4; break;
            case 60485: integerBitEnd = 5; break;
            case 60486: integerBitEnd = 6; break;
            case 60487: integerBitEnd = 7; break;
            case 60488: integerBitEnd = 8; break;
            case 60489: integerBitEnd = 9; break;
            case 60490: integerBitEnd = 10; break;
            case 60491: integerBitEnd = 11; break;
            case 60492: integerBitEnd = 12; break;
            case 60493: integerBitEnd = 13; break;
            case 60494: integerBitEnd = 14; break;
            case 60495: integerBitEnd = 15; break;
            case 62496: integerBitEnd = 0; break;
            case 62497: integerBitEnd = 1; break;
            case 62498: integerBitEnd = 2; break;
            case 62499: integerBitEnd = 3; break;
            case 62500: integerBitEnd = 4; break;
            case 62501: integerBitEnd = 5; break;
            case 62502: integerBitEnd = 6; break;
            case 62503: integerBitEnd = 7; break;
            case 62504: integerBitEnd = 8; break;
            case 62505: integerBitEnd = 9; break;
            case 62506: integerBitEnd = 10; break;
            case 62507: integerBitEnd = 11; break;
            case 62508: integerBitEnd = 12; break;
            case 62509: integerBitEnd = 13; break;
            case 62510: integerBitEnd = 14; break;
            case 62511: integerBitEnd = 15; break;
            case 64512: integerBitEnd = 0; break;
            case 64513: integerBitEnd = 1; break;
            case 64514: integerBitEnd = 2; break;
            case 64515: integerBitEnd = 3; break;
            case 64516: integerBitEnd = 4; break;
            case 64517: integerBitEnd = 5; break;
            case 64518: integerBitEnd = 6; break;
            case 64519: integerBitEnd = 7; break;
            case 64520: integerBitEnd = 8; break;
            case 64521: integerBitEnd = 9; break;
            case 64522: integerBitEnd = 10; break;
            case 64523: integerBitEnd = 11; break;
            case 64524: integerBitEnd = 12; break;
            case 64525: integerBitEnd = 13; break;
            case 64526: integerBitEnd = 14; break;
            case 64527: integerBitEnd = 15; break;
            case 66528: integerBitEnd = 0; break;
            case 66529: integerBitEnd = 1; break;
            case 66530: integerBitEnd = 2; break;
            case 66531: integerBitEnd = 3; break;
            case 66532: integerBitEnd = 4; break;
            case 66533: integerBitEnd = 5; break;
            case 66534: integerBitEnd = 6; break;
            case 66535: integerBitEnd = 7; break;
            case 66536: integerBitEnd = 8; break;
            case 66537: integerBitEnd = 9; break;
            case 66538: integerBitEnd = 10; break;
            case 66539: integerBitEnd = 11; break;
            case 66540: integerBitEnd = 12; break;
            case 66541: integerBitEnd = 13; break;
            case 66542: integerBitEnd = 14; break;
            case 66543: integerBitEnd = 15; break;
            case 68544: integerBitEnd = 0; break;
            case 68545: integerBitEnd = 1; break;
            case 68546: integerBitEnd = 2; break;
            case 68547: integerBitEnd = 3; break;
            case 68548: integerBitEnd = 4; break;
            case 68549: integerBitEnd = 5; break;
            case 68550: integerBitEnd = 6; break;
            case 68551: integerBitEnd = 7; break;
            case 68552: integerBitEnd = 8; break;
            case 68553: integerBitEnd = 9; break;
            case 68554: integerBitEnd = 10; break;
            case 68555: integerBitEnd = 11; break;
            case 68556: integerBitEnd = 12; break;
            case 68557: integerBitEnd = 13; break;
            case 68558: integerBitEnd = 14; break;
            case 68559: integerBitEnd = 15; break;
            case 70560: integerBitEnd = 0; break;
            case 70561: integerBitEnd = 1; break;
            case 70562: integerBitEnd = 2; break;
            case 70563: integerBitEnd = 3; break;
            case 70564: integerBitEnd = 4; break;
            case 70565: integerBitEnd = 5; break;
            case 70566: integerBitEnd = 6; break;
            case 70567: integerBitEnd = 7; break;
            case 70568: integerBitEnd = 8; break;
            case 70569: integerBitEnd = 9; break;
            case 70570: integerBitEnd = 10; break;
            case 70571: integerBitEnd = 11; break;
            case 70572: integerBitEnd = 12; break;
            case 70573: integerBitEnd = 13; break;
            case 70574: integerBitEnd = 14; break;
            case 70575: integerBitEnd = 15; break;
            case 72576: integerBitEnd = 0; break;
            case 72577: integerBitEnd = 1; break;
            case 72578: integerBitEnd = 2; break;
            case 72579: integerBitEnd = 3; break;
            case 72580: integerBitEnd = 4; break;
            case 72581: integerBitEnd = 5; break;
            case 72582: integerBitEnd = 6; break;
            case 72583: integerBitEnd = 7; break;
            case 72584: integerBitEnd = 8; break;
            case 72585: integerBitEnd = 9; break;
            case 72586: integerBitEnd = 10; break;
            case 72587: integerBitEnd = 11; break;
            case 72588: integerBitEnd = 12; break;
            case 72589: integerBitEnd = 13; break;
            case 72590: integerBitEnd = 14; break;
            case 72591: integerBitEnd = 15; break;
            case 74592: integerBitEnd = 0; break;
            case 74593: integerBitEnd = 1; break;
            case 74594: integerBitEnd = 2; break;
            case 74595: integerBitEnd = 3; break;
            case 74596: integerBitEnd = 4; break;
            case 74597: integerBitEnd = 5; break;
            case 74598: integerBitEnd = 6; break;
            case 74599: integerBitEnd = 7; break;
            case 74600: integerBitEnd = 8; break;
            case 74601: integerBitEnd = 9; break;
            case 74602: integerBitEnd = 10; break;
            case 74603: integerBitEnd = 11; break;
            case 74604: integerBitEnd = 12; break;
            case 74605: integerBitEnd = 13; break;
            case 74606: integerBitEnd = 14; break;
            case 74607: integerBitEnd = 15; break;
            case 76608: integerBitEnd = 0; break;
            case 76609: integerBitEnd = 1; break;
            case 76610: integerBitEnd = 2; break;
            case 76611: integerBitEnd = 3; break;
            case 76612: integerBitEnd = 4; break;
            case 76613: integerBitEnd = 5; break;
            case 76614: integerBitEnd = 6; break;
            case 76615: integerBitEnd = 7; break;
            case 76616: integerBitEnd = 8; break;
            case 76617: integerBitEnd = 9; break;
            case 76618: integerBitEnd = 10; break;
            case 76619: integerBitEnd = 11; break;
            case 76620: integerBitEnd = 12; break;
            case 76621: integerBitEnd = 13; break;
            case 76622: integerBitEnd = 14; break;
            case 76623: integerBitEnd = 15; break;
            case 78624: integerBitEnd = 0; break;
            case 78625: integerBitEnd = 1; break;
            case 78626: integerBitEnd = 2; break;
            case 78627: integerBitEnd = 3; break;
            case 78628: integerBitEnd = 4; break;
            case 78629: integerBitEnd = 5; break;
            case 78630: integerBitEnd = 6; break;
            case 78631: integerBitEnd = 7; break;
            case 78632: integerBitEnd = 8; break;
            case 78633: integerBitEnd = 9; break;
            case 78634: integerBitEnd = 10; break;
            case 78635: integerBitEnd = 11; break;
            case 78636: integerBitEnd = 12; break;
            case 78637: integerBitEnd = 13; break;
            case 78638: integerBitEnd = 14; break;
            case 78639: integerBitEnd = 15; break;
            case 80640: integerBitEnd = 0; break;
            case 80641: integerBitEnd = 1; break;
            case 80642: integerBitEnd = 2; break;
            case 80643: integerBitEnd = 3; break;
            case 80644: integerBitEnd = 4; break;
            case 80645: integerBitEnd = 5; break;
            case 80646: integerBitEnd = 6; break;
            case 80647: integerBitEnd = 7; break;
            case 80648: integerBitEnd = 8; break;
            case 80649: integerBitEnd = 9; break;
            case 80650: integerBitEnd = 10; break;
            case 80651: integerBitEnd = 11; break;
            case 80652: integerBitEnd = 12; break;
            case 80653: integerBitEnd = 13; break;
            case 80654: integerBitEnd = 14; break;
            case 80655: integerBitEnd = 15; break;
            case 82656: integerBitEnd = 0; break;
            case 82657: integerBitEnd = 1; break;
            case 82658: integerBitEnd = 2; break;
            case 82659: integerBitEnd = 3; break;
            case 82660: integerBitEnd = 4; break;
            case 82661: integerBitEnd = 5; break;
            case 82662: integerBitEnd = 6; break;
            case 82663: integerBitEnd = 7; break;
            case 82664: integerBitEnd = 8; break;
            case 82665: integerBitEnd = 9; break;
            case 82666: integerBitEnd = 10; break;
            case 82667: integerBitEnd = 11; break;
            case 82668: integerBitEnd = 12; break;
            case 82669: integerBitEnd = 13; break;
            case 82670: integerBitEnd = 14; break;
            case 82671: integerBitEnd = 15; break;
            case 84672: integerBitEnd = 0; break;
            case 84673: integerBitEnd = 1; break;
            case 84674: integerBitEnd = 2; break;
            case 84675: integerBitEnd = 3; break;
            case 84676: integerBitEnd = 4; break;
            case 84677: integerBitEnd = 5; break;
            case 84678: integerBitEnd = 6; break;
            case 84679: integerBitEnd = 7; break;
            case 84680: integerBitEnd = 8; break;
            case 84681: integerBitEnd = 9; break;
            case 84682: integerBitEnd = 10; break;
            case 84683: integerBitEnd = 11; break;
            case 84684: integerBitEnd = 12; break;
            case 84685: integerBitEnd = 13; break;
            case 84686: integerBitEnd = 14; break;
            case 84687: integerBitEnd = 15; break;
            case 86688: integerBitEnd = 0; break;
            case 86689: integerBitEnd = 1; break;
            case 86690: integerBitEnd = 2; break;
            case 86691: integerBitEnd = 3; break;
            case 86692: integerBitEnd = 4; break;
            case 86693: integerBitEnd = 5; break;
            case 86694: integerBitEnd = 6; break;
            case 86695: integerBitEnd = 7; break;
            case 86696: integerBitEnd = 8; break;
            case 86697: integerBitEnd = 9; break;
            case 86698: integerBitEnd = 10; break;
            case 86699: integerBitEnd = 11; break;
            case 86700: integerBitEnd = 12; break;
            case 86701: integerBitEnd = 13; break;
            case 86702: integerBitEnd = 14; break;
            case 86703: integerBitEnd = 15; break;
            case 88704: integerBitEnd = 0; break;
            case 88705: integerBitEnd = 1; break;
            case 88706: integerBitEnd = 2; break;
            case 88707: integerBitEnd = 3; break;
            case 88708: integerBitEnd = 4; break;
            case 88709: integerBitEnd = 5; break;
            case 88710: integerBitEnd = 6; break;
            case 88711: integerBitEnd = 7; break;
            case 88712: integerBitEnd = 8; break;
            case 88713: integerBitEnd = 9; break;
            case 88714: integerBitEnd = 10; break;
            case 88715: integerBitEnd = 11; break;
            case 88716: integerBitEnd = 12; break;
            case 88717: integerBitEnd = 13; break;
            case 88718: integerBitEnd = 14; break;
            case 88719: integerBitEnd = 15; break;
            case 90720: integerBitEnd = 0; break;
            case 90721: integerBitEnd = 1; break;
            case 90722: integerBitEnd = 2; break;
            case 90723: integerBitEnd = 3; break;
            case 90724: integerBitEnd = 4; break;
            case 90725: integerBitEnd = 5; break;
            case 90726: integerBitEnd = 6; break;
            case 90727: integerBitEnd = 7; break;
            case 90728: integerBitEnd = 8; break;
            case 90729: integerBitEnd = 9; break;
            case 90730: integerBitEnd = 10; break;
            case 90731: integerBitEnd = 11; break;
            case 90732: integerBitEnd = 12; break;
            case 90733: integerBitEnd = 13; break;
            case 90734: integerBitEnd = 14; break;
            case 90735: integerBitEnd = 15; break;
            case 92736: integerBitEnd = 0; break;
            case 92737: integerBitEnd = 1; break;
            case 92738: integerBitEnd = 2; break;
            case 92739: integerBitEnd = 3; break;
            case 92740: integerBitEnd = 4; break;
            case 92741: integerBitEnd = 5; break;
            case 92742: integerBitEnd = 6; break;
            case 92743: integerBitEnd = 7; break;
            case 92744: integerBitEnd = 8; break;
            case 92745: integerBitEnd = 9; break;
            case 92746: integerBitEnd = 10; break;
            case 92747: integerBitEnd = 11; break;
            case 92748: integerBitEnd = 12; break;
            case 92749: integerBitEnd = 13; break;
            case 92750: integerBitEnd = 14; break;
            case 92751: integerBitEnd = 15; break;
            case 94752: integerBitEnd = 0; break;
            case 94753: integerBitEnd = 1; break;
            case 94754: integerBitEnd = 2; break;
            case 94755: integerBitEnd = 3; break;
            case 94756: integerBitEnd = 4; break;
            case 94757: integerBitEnd = 5; break;
            case 94758: integerBitEnd = 6; break;
            case 94759: integerBitEnd = 7; break;
            case 94760: integerBitEnd = 8; break;
            case 94761: integerBitEnd = 9; break;
            case 94762: integerBitEnd = 10; break;
            case 94763: integerBitEnd = 11; break;
            case 94764: integerBitEnd = 12; break;
            case 94765: integerBitEnd = 13; break;
            case 94766: integerBitEnd = 14; break;
            case 94767: integerBitEnd = 15; break;
            case 96768: integerBitEnd = 0; break;
            case 96769: integerBitEnd = 1; break;
            case 96770: integerBitEnd = 2; break;
            case 96771: integerBitEnd = 3; break;
            case 96772: integerBitEnd = 4; break;
            case 96773: integerBitEnd = 5; break;
            case 96774: integerBitEnd = 6; break;
            case 96775: integerBitEnd = 7; break;
            case 96776: integerBitEnd = 8; break;
            case 96777: integerBitEnd = 9; break;
            case 96778: integerBitEnd = 10; break;
            case 96779: integerBitEnd = 11; break;
            case 96780: integerBitEnd = 12; break;
            case 96781: integerBitEnd = 13; break;
            case 96782: integerBitEnd = 14; break;
            case 96783: integerBitEnd = 15; break;
            case 98784: integerBitEnd = 0; break;
            case 98785: integerBitEnd = 1; break;
            case 98786: integerBitEnd = 2; break;
            case 98787: integerBitEnd = 3; break;
            case 98788: integerBitEnd = 4; break;
            case 98789: integerBitEnd = 5; break;
            case 98790: integerBitEnd = 6; break;
            case 98791: integerBitEnd = 7; break;
            case 98792: integerBitEnd = 8; break;
            case 98793: integerBitEnd = 9; break;
            case 98794: integerBitEnd = 10; break;
            case 98795: integerBitEnd = 11; break;
            case 98796: integerBitEnd = 12; break;
            case 98797: integerBitEnd = 13; break;
            case 98798: integerBitEnd = 14; break;
            case 98799: integerBitEnd = 15; break;
            case 100800: integerBitEnd = 0; break;
            case 100801: integerBitEnd = 1; break;
            case 100802: integerBitEnd = 2; break;
            case 100803: integerBitEnd = 3; break;
            case 100804: integerBitEnd = 4; break;
            case 100805: integerBitEnd = 5; break;
            case 100806: integerBitEnd = 6; break;
            case 100807: integerBitEnd = 7; break;
            case 100808: integerBitEnd = 8; break;
            case 100809: integerBitEnd = 9; break;
            case 100810: integerBitEnd = 10; break;
            case 100811: integerBitEnd = 11; break;
            case 100812: integerBitEnd = 12; break;
            case 100813: integerBitEnd = 13; break;
            case 100814: integerBitEnd = 14; break;
            case 100815: integerBitEnd = 15; break;
            case 102816: integerBitEnd = 0; break;
            case 102817: integerBitEnd = 1; break;
            case 102818: integerBitEnd = 2; break;
            case 102819: integerBitEnd = 3; break;
            case 102820: integerBitEnd = 4; break;
            case 102821: integerBitEnd = 5; break;
            case 102822: integerBitEnd = 6; break;
            case 102823: integerBitEnd = 7; break;
            case 102824: integerBitEnd = 8; break;
            case 102825: integerBitEnd = 9; break;
            case 102826: integerBitEnd = 10; break;
            case 102827: integerBitEnd = 11; break;
            case 102828: integerBitEnd = 12; break;
            case 102829: integerBitEnd = 13; break;
            case 102830: integerBitEnd = 14; break;
            case 102831: integerBitEnd = 15; break;
            case 104832: integerBitEnd = 0; break;
            case 104833: integerBitEnd = 1; break;
            case 104834: integerBitEnd = 2; break;
            case 104835: integerBitEnd = 3; break;
            case 104836: integerBitEnd = 4; break;
            case 104837: integerBitEnd = 5; break;
            case 104838: integerBitEnd = 6; break;
            case 104839: integerBitEnd = 7; break;
            case 104840: integerBitEnd = 8; break;
            case 104841: integerBitEnd = 9; break;
            case 104842: integerBitEnd = 10; break;
            case 104843: integerBitEnd = 11; break;
            case 104844: integerBitEnd = 12; break;
            case 104845: integerBitEnd = 13; break;
            case 104846: integerBitEnd = 14; break;
            case 104847: integerBitEnd = 15; break;
            case 106848: integerBitEnd = 0; break;
            case 106849: integerBitEnd = 1; break;
            case 106850: integerBitEnd = 2; break;
            case 106851: integerBitEnd = 3; break;
            case 106852: integerBitEnd = 4; break;
            case 106853: integerBitEnd = 5; break;
            case 106854: integerBitEnd = 6; break;
            case 106855: integerBitEnd = 7; break;
            case 106856: integerBitEnd = 8; break;
            case 106857: integerBitEnd = 9; break;
            case 106858: integerBitEnd = 10; break;
            case 106859: integerBitEnd = 11; break;
            case 106860: integerBitEnd = 12; break;
            case 106861: integerBitEnd = 13; break;
            case 106862: integerBitEnd = 14; break;
            case 106863: integerBitEnd = 15; break;
            case 108864: integerBitEnd = 0; break;
            case 108865: integerBitEnd = 1; break;
            case 108866: integerBitEnd = 2; break;
            case 108867: integerBitEnd = 3; break;
            case 108868: integerBitEnd = 4; break;
            case 108869: integerBitEnd = 5; break;
            case 108870: integerBitEnd = 6; break;
            case 108871: integerBitEnd = 7; break;
            case 108872: integerBitEnd = 8; break;
            case 108873: integerBitEnd = 9; break;
            case 108874: integerBitEnd = 10; break;
            case 108875: integerBitEnd = 11; break;
            case 108876: integerBitEnd = 12; break;
            case 108877: integerBitEnd = 13; break;
            case 108878: integerBitEnd = 14; break;
            case 108879: integerBitEnd = 15; break;
            case 110880: integerBitEnd = 0; break;
            case 110881: integerBitEnd = 1; break;
            case 110882: integerBitEnd = 2; break;
            case 110883: integerBitEnd = 3; break;
            case 110884: integerBitEnd = 4; break;
            case 110885: integerBitEnd = 5; break;
            case 110886: integerBitEnd = 6; break;
            case 110887: integerBitEnd = 7; break;
            case 110888: integerBitEnd = 8; break;
            case 110889: integerBitEnd = 9; break;
            case 110890: integerBitEnd = 10; break;
            case 110891: integerBitEnd = 11; break;
            case 110892: integerBitEnd = 12; break;
            case 110893: integerBitEnd = 13; break;
            case 110894: integerBitEnd = 14; break;
            case 110895: integerBitEnd = 15; break;
            case 112896: integerBitEnd = 0; break;
            case 112897: integerBitEnd = 1; break;
            case 112898: integerBitEnd = 2; break;
            case 112899: integerBitEnd = 3; break;
            case 112900: integerBitEnd = 4; break;
            case 112901: integerBitEnd = 5; break;
            case 112902: integerBitEnd = 6; break;
            case 112903: integerBitEnd = 7; break;
            case 112904: integerBitEnd = 8; break;
            case 112905: integerBitEnd = 9; break;
            case 112906: integerBitEnd = 10; break;
            case 112907: integerBitEnd = 11; break;
            case 112908: integerBitEnd = 12; break;
            case 112909: integerBitEnd = 13; break;
            case 112910: integerBitEnd = 14; break;
            case 112911: integerBitEnd = 15; break;
            case 114912: integerBitEnd = 0; break;
            case 114913: integerBitEnd = 1; break;
            case 114914: integerBitEnd = 2; break;
            case 114915: integerBitEnd = 3; break;
            case 114916: integerBitEnd = 4; break;
            case 114917: integerBitEnd = 5; break;
            case 114918: integerBitEnd = 6; break;
            case 114919: integerBitEnd = 7; break;
            case 114920: integerBitEnd = 8; break;
            case 114921: integerBitEnd = 9; break;
            case 114922: integerBitEnd = 10; break;
            case 114923: integerBitEnd = 11; break;
            case 114924: integerBitEnd = 12; break;
            case 114925: integerBitEnd = 13; break;
            case 114926: integerBitEnd = 14; break;
            case 114927: integerBitEnd = 15; break;
            case 116928: integerBitEnd = 0; break;
            case 116929: integerBitEnd = 1; break;
            case 116930: integerBitEnd = 2; break;
            case 116931: integerBitEnd = 3; break;
            case 116932: integerBitEnd = 4; break;
            case 116933: integerBitEnd = 5; break;
            case 116934: integerBitEnd = 6; break;
            case 116935: integerBitEnd = 7; break;
            case 116936: integerBitEnd = 8; break;
            case 116937: integerBitEnd = 9; break;
            case 116938: integerBitEnd = 10; break;
            case 116939: integerBitEnd = 11; break;
            case 116940: integerBitEnd = 12; break;
            case 116941: integerBitEnd = 13; break;
            case 116942: integerBitEnd = 14; break;
            case 116943: integerBitEnd = 15; break;
            case 118944: integerBitEnd = 0; break;
            case 118945: integerBitEnd = 1; break;
            case 118946: integerBitEnd = 2; break;
            case 118947: integerBitEnd = 3; break;
            case 118948: integerBitEnd = 4; break;
            case 118949: integerBitEnd = 5; break;
            case 118950: integerBitEnd = 6; break;
            case 118951: integerBitEnd = 7; break;
            case 118952: integerBitEnd = 8; break;
            case 118953: integerBitEnd = 9; break;
            case 118954: integerBitEnd = 10; break;
            case 118955: integerBitEnd = 11; break;
            case 118956: integerBitEnd = 12; break;
            case 118957: integerBitEnd = 13; break;
            case 118958: integerBitEnd = 14; break;
            case 118959: integerBitEnd = 15; break;
            case 120960: integerBitEnd = 0; break;
            case 120961: integerBitEnd = 1; break;
            case 120962: integerBitEnd = 2; break;
            case 120963: integerBitEnd = 3; break;
            case 120964: integerBitEnd = 4; break;
            case 120965: integerBitEnd = 5; break;
            case 120966: integerBitEnd = 6; break;
            case 120967: integerBitEnd = 7; break;
            case 120968: integerBitEnd = 8; break;
            case 120969: integerBitEnd = 9; break;
            case 120970: integerBitEnd = 10; break;
            case 120971: integerBitEnd = 11; break;
            case 120972: integerBitEnd = 12; break;
            case 120973: integerBitEnd = 13; break;
            case 120974: integerBitEnd = 14; break;
            case 120975: integerBitEnd = 15; break;
            case 122976: integerBitEnd = 0; break;
            case 122977: integerBitEnd = 1; break;
            case 122978: integerBitEnd = 2; break;
            case 122979: integerBitEnd = 3; break;
            case 122980: integerBitEnd = 4; break;
            case 122981: integerBitEnd = 5; break;
            case 122982: integerBitEnd = 6; break;
            case 122983: integerBitEnd = 7; break;
            case 122984: integerBitEnd = 8; break;
            case 122985: integerBitEnd = 9; break;
            case 122986: integerBitEnd = 10; break;
            case 122987: integerBitEnd = 11; break;
            case 122988: integerBitEnd = 12; break;
            case 122989: integerBitEnd = 13; break;
            case 122990: integerBitEnd = 14; break;
            case 122991: integerBitEnd = 15; break;
            case 124992: integerBitEnd = 0; break;
            case 124993: integerBitEnd = 1; break;
            case 124994: integerBitEnd = 2; break;
            case 124995: integerBitEnd = 3; break;
            case 124996: integerBitEnd = 4; break;
            case 124997: integerBitEnd = 5; break;
            case 124998: integerBitEnd = 6; break;
            case 124999: integerBitEnd = 7; break;
            case 125000: integerBitEnd = 8; break;
            case 125001: integerBitEnd = 9; break;
            case 125002: integerBitEnd = 10; break;
            case 125003: integerBitEnd = 11; break;
            case 125004: integerBitEnd = 12; break;
            case 125005: integerBitEnd = 13; break;
            case 125006: integerBitEnd = 14; break;
            case 125007: integerBitEnd = 15; break;
            case 127008: integerBitEnd = 0; break;
            case 127009: integerBitEnd = 1; break;
            case 127010: integerBitEnd = 2; break;
            case 127011: integerBitEnd = 3; break;
            case 127012: integerBitEnd = 4; break;
            case 127013: integerBitEnd = 5; break;
            case 127014: integerBitEnd = 6; break;
            case 127015: integerBitEnd = 7; break;
            case 127016: integerBitEnd = 8; break;
            case 127017: integerBitEnd = 9; break;
            case 127018: integerBitEnd = 10; break;
            case 127019: integerBitEnd = 11; break;
            case 127020: integerBitEnd = 12; break;
            case 127021: integerBitEnd = 13; break;
            case 127022: integerBitEnd = 14; break;
            case 127023: integerBitEnd = 15; break;
        }

        int output = 0;
        // if read is contained in a single integer
        if (arrIndexStart == arrIndexEnd) {
            int bitm = bitmask2(integerBitBegin, integerBitEnd, true);
            output = (readSharedArray(arrIndexStart) & bitm) >>> (SHARED_ARRAY_ELEM_SIZE - integerBitBegin - numBits);
        } else {
            //if the read spans two integers
            int bitm = bitmask2(integerBitBegin, SHARED_ARRAY_ELEM_SIZE-1, true);
            int bitm2 = bitmask2(0, integerBitEnd, true);
            output = (readSharedArray(arrIndexStart) & bitm) >>> (SHARED_ARRAY_ELEM_SIZE - integerBitBegin - numBits + integerBitEnd + 1);
            output = output << integerBitEnd + 1;
            output |= (readSharedArray(arrIndexEnd) & bitm2) >>> (SHARED_ARRAY_ELEM_SIZE - numBits + SHARED_ARRAY_ELEM_SIZE - integerBitBegin);
        }
        return output;
    }

    // Replaced with giant switch statement
    private int bitmask(int start, int end, boolean read) {
        start += 32 - SHARED_ARRAY_ELEM_SIZE;
        end += 32 - SHARED_ARRAY_ELEM_SIZE;
        int bitmask = -1;
        if(end == 31) {
            bitmask = 0;
        } else {
            bitmask = bitmask >>> (end+1);
        }
        int bitmask2 = -1;
        bitmask2 = bitmask2 >>> (start);
        //for reading
        if (read) {
            return (bitmask ^ bitmask2);
        }
        //for writing
        return ~(bitmask ^ bitmask2);
    }

    // Replaced with giant switch statement
    private int whichBit(int index, int bitloc) {
        if(bitloc < index * SHARED_ARRAY_ELEM_SIZE) {
            return 0;    //first bit of the number
        }
        if(bitloc >= (index+1)*SHARED_ARRAY_ELEM_SIZE) {
            return SHARED_ARRAY_ELEM_SIZE-1;  //last bit of the number
        }
        return bitloc % SHARED_ARRAY_ELEM_SIZE;
    }

    
    private int bitmask2(int start, int end, boolean read) {
        int mask = 32*start + end;
        switch (mask) {
            case 0:  if(read) {return 32768;} else {return -32769;} 
            case 1:  if(read) {return 49152;} else {return -49153;} 
            case 2:  if(read) {return 57344;} else {return -57345;} 
            case 3:  if(read) {return 61440;} else {return -61441;} 
            case 4:  if(read) {return 63488;} else {return -63489;} 
            case 5:  if(read) {return 64512;} else {return -64513;} 
            case 6:  if(read) {return 65024;} else {return -65025;} 
            case 7:  if(read) {return 65280;} else {return -65281;} 
            case 8:  if(read) {return 65408;} else {return -65409;} 
            case 9:  if(read) {return 65472;} else {return -65473;} 
            case 10:  if(read) {return 65504;} else {return -65505;} 
            case 11:  if(read) {return 65520;} else {return -65521;} 
            case 12:  if(read) {return 65528;} else {return -65529;} 
            case 13:  if(read) {return 65532;} else {return -65533;} 
            case 14:  if(read) {return 65534;} else {return -65535;} 
            case 15:  if(read) {return 65535;} else {return -65536;} 
            case 16:  if(read) {return 2147418112;} else {return -2147418113;} 
            case 17:  if(read) {return 1073676288;} else {return -1073676289;} 
            case 18:  if(read) {return 536805376;} else {return -536805377;} 
            case 19:  if(read) {return 268369920;} else {return -268369921;} 
            case 20:  if(read) {return 134152192;} else {return -134152193;} 
            case 21:  if(read) {return 67043328;} else {return -67043329;} 
            case 22:  if(read) {return 33488896;} else {return -33488897;} 
            case 23:  if(read) {return 16711680;} else {return -16711681;} 
            case 24:  if(read) {return 8323072;} else {return -8323073;} 
            case 25:  if(read) {return 4128768;} else {return -4128769;} 
            case 26:  if(read) {return 2031616;} else {return -2031617;} 
            case 27:  if(read) {return 983040;} else {return -983041;} 
            case 28:  if(read) {return 458752;} else {return -458753;} 
            case 29:  if(read) {return 196608;} else {return -196609;} 
            case 30:  if(read) {return 65536;} else {return -65537;} 
            case 31:  if(read) {return 0;} else {return -1;} 
            case 33:  if(read) {return 16384;} else {return -16385;} 
            case 34:  if(read) {return 24576;} else {return -24577;} 
            case 35:  if(read) {return 28672;} else {return -28673;} 
            case 36:  if(read) {return 30720;} else {return -30721;} 
            case 37:  if(read) {return 31744;} else {return -31745;} 
            case 38:  if(read) {return 32256;} else {return -32257;} 
            case 39:  if(read) {return 32512;} else {return -32513;} 
            case 40:  if(read) {return 32640;} else {return -32641;} 
            case 41:  if(read) {return 32704;} else {return -32705;} 
            case 42:  if(read) {return 32736;} else {return -32737;} 
            case 43:  if(read) {return 32752;} else {return -32753;} 
            case 44:  if(read) {return 32760;} else {return -32761;} 
            case 45:  if(read) {return 32764;} else {return -32765;} 
            case 46:  if(read) {return 32766;} else {return -32767;} 
            case 47:  if(read) {return 32767;} else {return -32768;} 
            case 48:  if(read) {return 2147450880;} else {return -2147450881;} 
            case 49:  if(read) {return 1073709056;} else {return -1073709057;} 
            case 50:  if(read) {return 536838144;} else {return -536838145;} 
            case 51:  if(read) {return 268402688;} else {return -268402689;} 
            case 52:  if(read) {return 134184960;} else {return -134184961;} 
            case 53:  if(read) {return 67076096;} else {return -67076097;} 
            case 54:  if(read) {return 33521664;} else {return -33521665;} 
            case 55:  if(read) {return 16744448;} else {return -16744449;} 
            case 56:  if(read) {return 8355840;} else {return -8355841;} 
            case 57:  if(read) {return 4161536;} else {return -4161537;} 
            case 58:  if(read) {return 2064384;} else {return -2064385;} 
            case 59:  if(read) {return 1015808;} else {return -1015809;} 
            case 60:  if(read) {return 491520;} else {return -491521;} 
            case 61:  if(read) {return 229376;} else {return -229377;} 
            case 62:  if(read) {return 98304;} else {return -98305;} 
            case 63:  if(read) {return 32768;} else {return -32769;} 
            case 66:  if(read) {return 8192;} else {return -8193;} 
            case 67:  if(read) {return 12288;} else {return -12289;} 
            case 68:  if(read) {return 14336;} else {return -14337;} 
            case 69:  if(read) {return 15360;} else {return -15361;} 
            case 70:  if(read) {return 15872;} else {return -15873;} 
            case 71:  if(read) {return 16128;} else {return -16129;} 
            case 72:  if(read) {return 16256;} else {return -16257;} 
            case 73:  if(read) {return 16320;} else {return -16321;} 
            case 74:  if(read) {return 16352;} else {return -16353;} 
            case 75:  if(read) {return 16368;} else {return -16369;} 
            case 76:  if(read) {return 16376;} else {return -16377;} 
            case 77:  if(read) {return 16380;} else {return -16381;} 
            case 78:  if(read) {return 16382;} else {return -16383;} 
            case 79:  if(read) {return 16383;} else {return -16384;} 
            case 80:  if(read) {return 2147467264;} else {return -2147467265;} 
            case 81:  if(read) {return 1073725440;} else {return -1073725441;} 
            case 82:  if(read) {return 536854528;} else {return -536854529;} 
            case 83:  if(read) {return 268419072;} else {return -268419073;} 
            case 84:  if(read) {return 134201344;} else {return -134201345;} 
            case 85:  if(read) {return 67092480;} else {return -67092481;} 
            case 86:  if(read) {return 33538048;} else {return -33538049;} 
            case 87:  if(read) {return 16760832;} else {return -16760833;} 
            case 88:  if(read) {return 8372224;} else {return -8372225;} 
            case 89:  if(read) {return 4177920;} else {return -4177921;} 
            case 90:  if(read) {return 2080768;} else {return -2080769;} 
            case 91:  if(read) {return 1032192;} else {return -1032193;} 
            case 92:  if(read) {return 507904;} else {return -507905;} 
            case 93:  if(read) {return 245760;} else {return -245761;} 
            case 94:  if(read) {return 114688;} else {return -114689;} 
            case 95:  if(read) {return 49152;} else {return -49153;} 
            case 99:  if(read) {return 4096;} else {return -4097;} 
            case 100:  if(read) {return 6144;} else {return -6145;} 
            case 101:  if(read) {return 7168;} else {return -7169;} 
            case 102:  if(read) {return 7680;} else {return -7681;} 
            case 103:  if(read) {return 7936;} else {return -7937;} 
            case 104:  if(read) {return 8064;} else {return -8065;} 
            case 105:  if(read) {return 8128;} else {return -8129;} 
            case 106:  if(read) {return 8160;} else {return -8161;} 
            case 107:  if(read) {return 8176;} else {return -8177;} 
            case 108:  if(read) {return 8184;} else {return -8185;} 
            case 109:  if(read) {return 8188;} else {return -8189;} 
            case 110:  if(read) {return 8190;} else {return -8191;} 
            case 111:  if(read) {return 8191;} else {return -8192;} 
            case 112:  if(read) {return 2147475456;} else {return -2147475457;} 
            case 113:  if(read) {return 1073733632;} else {return -1073733633;} 
            case 114:  if(read) {return 536862720;} else {return -536862721;} 
            case 115:  if(read) {return 268427264;} else {return -268427265;} 
            case 116:  if(read) {return 134209536;} else {return -134209537;} 
            case 117:  if(read) {return 67100672;} else {return -67100673;} 
            case 118:  if(read) {return 33546240;} else {return -33546241;} 
            case 119:  if(read) {return 16769024;} else {return -16769025;} 
            case 120:  if(read) {return 8380416;} else {return -8380417;} 
            case 121:  if(read) {return 4186112;} else {return -4186113;} 
            case 122:  if(read) {return 2088960;} else {return -2088961;} 
            case 123:  if(read) {return 1040384;} else {return -1040385;} 
            case 124:  if(read) {return 516096;} else {return -516097;} 
            case 125:  if(read) {return 253952;} else {return -253953;} 
            case 126:  if(read) {return 122880;} else {return -122881;} 
            case 127:  if(read) {return 57344;} else {return -57345;} 
            case 132:  if(read) {return 2048;} else {return -2049;} 
            case 133:  if(read) {return 3072;} else {return -3073;} 
            case 134:  if(read) {return 3584;} else {return -3585;} 
            case 135:  if(read) {return 3840;} else {return -3841;} 
            case 136:  if(read) {return 3968;} else {return -3969;} 
            case 137:  if(read) {return 4032;} else {return -4033;} 
            case 138:  if(read) {return 4064;} else {return -4065;} 
            case 139:  if(read) {return 4080;} else {return -4081;} 
            case 140:  if(read) {return 4088;} else {return -4089;} 
            case 141:  if(read) {return 4092;} else {return -4093;} 
            case 142:  if(read) {return 4094;} else {return -4095;} 
            case 143:  if(read) {return 4095;} else {return -4096;} 
            case 144:  if(read) {return 2147479552;} else {return -2147479553;} 
            case 145:  if(read) {return 1073737728;} else {return -1073737729;} 
            case 146:  if(read) {return 536866816;} else {return -536866817;} 
            case 147:  if(read) {return 268431360;} else {return -268431361;} 
            case 148:  if(read) {return 134213632;} else {return -134213633;} 
            case 149:  if(read) {return 67104768;} else {return -67104769;} 
            case 150:  if(read) {return 33550336;} else {return -33550337;} 
            case 151:  if(read) {return 16773120;} else {return -16773121;} 
            case 152:  if(read) {return 8384512;} else {return -8384513;} 
            case 153:  if(read) {return 4190208;} else {return -4190209;} 
            case 154:  if(read) {return 2093056;} else {return -2093057;} 
            case 155:  if(read) {return 1044480;} else {return -1044481;} 
            case 156:  if(read) {return 520192;} else {return -520193;} 
            case 157:  if(read) {return 258048;} else {return -258049;} 
            case 158:  if(read) {return 126976;} else {return -126977;} 
            case 159:  if(read) {return 61440;} else {return -61441;} 
            case 165:  if(read) {return 1024;} else {return -1025;} 
            case 166:  if(read) {return 1536;} else {return -1537;} 
            case 167:  if(read) {return 1792;} else {return -1793;} 
            case 168:  if(read) {return 1920;} else {return -1921;} 
            case 169:  if(read) {return 1984;} else {return -1985;} 
            case 170:  if(read) {return 2016;} else {return -2017;} 
            case 171:  if(read) {return 2032;} else {return -2033;} 
            case 172:  if(read) {return 2040;} else {return -2041;} 
            case 173:  if(read) {return 2044;} else {return -2045;} 
            case 174:  if(read) {return 2046;} else {return -2047;} 
            case 175:  if(read) {return 2047;} else {return -2048;} 
            case 176:  if(read) {return 2147481600;} else {return -2147481601;} 
            case 177:  if(read) {return 1073739776;} else {return -1073739777;} 
            case 178:  if(read) {return 536868864;} else {return -536868865;} 
            case 179:  if(read) {return 268433408;} else {return -268433409;} 
            case 180:  if(read) {return 134215680;} else {return -134215681;} 
            case 181:  if(read) {return 67106816;} else {return -67106817;} 
            case 182:  if(read) {return 33552384;} else {return -33552385;} 
            case 183:  if(read) {return 16775168;} else {return -16775169;} 
            case 184:  if(read) {return 8386560;} else {return -8386561;} 
            case 185:  if(read) {return 4192256;} else {return -4192257;} 
            case 186:  if(read) {return 2095104;} else {return -2095105;} 
            case 187:  if(read) {return 1046528;} else {return -1046529;} 
            case 188:  if(read) {return 522240;} else {return -522241;} 
            case 189:  if(read) {return 260096;} else {return -260097;} 
            case 190:  if(read) {return 129024;} else {return -129025;} 
            case 191:  if(read) {return 63488;} else {return -63489;} 
            case 198:  if(read) {return 512;} else {return -513;} 
            case 199:  if(read) {return 768;} else {return -769;} 
            case 200:  if(read) {return 896;} else {return -897;} 
            case 201:  if(read) {return 960;} else {return -961;} 
            case 202:  if(read) {return 992;} else {return -993;} 
            case 203:  if(read) {return 1008;} else {return -1009;} 
            case 204:  if(read) {return 1016;} else {return -1017;} 
            case 205:  if(read) {return 1020;} else {return -1021;} 
            case 206:  if(read) {return 1022;} else {return -1023;} 
            case 207:  if(read) {return 1023;} else {return -1024;} 
            case 208:  if(read) {return 2147482624;} else {return -2147482625;} 
            case 209:  if(read) {return 1073740800;} else {return -1073740801;} 
            case 210:  if(read) {return 536869888;} else {return -536869889;} 
            case 211:  if(read) {return 268434432;} else {return -268434433;} 
            case 212:  if(read) {return 134216704;} else {return -134216705;} 
            case 213:  if(read) {return 67107840;} else {return -67107841;} 
            case 214:  if(read) {return 33553408;} else {return -33553409;} 
            case 215:  if(read) {return 16776192;} else {return -16776193;} 
            case 216:  if(read) {return 8387584;} else {return -8387585;} 
            case 217:  if(read) {return 4193280;} else {return -4193281;} 
            case 218:  if(read) {return 2096128;} else {return -2096129;} 
            case 219:  if(read) {return 1047552;} else {return -1047553;} 
            case 220:  if(read) {return 523264;} else {return -523265;} 
            case 221:  if(read) {return 261120;} else {return -261121;} 
            case 222:  if(read) {return 130048;} else {return -130049;} 
            case 223:  if(read) {return 64512;} else {return -64513;} 
            case 231:  if(read) {return 256;} else {return -257;} 
            case 232:  if(read) {return 384;} else {return -385;} 
            case 233:  if(read) {return 448;} else {return -449;} 
            case 234:  if(read) {return 480;} else {return -481;} 
            case 235:  if(read) {return 496;} else {return -497;} 
            case 236:  if(read) {return 504;} else {return -505;} 
            case 237:  if(read) {return 508;} else {return -509;} 
            case 238:  if(read) {return 510;} else {return -511;} 
            case 239:  if(read) {return 511;} else {return -512;} 
            case 240:  if(read) {return 2147483136;} else {return -2147483137;} 
            case 241:  if(read) {return 1073741312;} else {return -1073741313;} 
            case 242:  if(read) {return 536870400;} else {return -536870401;} 
            case 243:  if(read) {return 268434944;} else {return -268434945;} 
            case 244:  if(read) {return 134217216;} else {return -134217217;} 
            case 245:  if(read) {return 67108352;} else {return -67108353;} 
            case 246:  if(read) {return 33553920;} else {return -33553921;} 
            case 247:  if(read) {return 16776704;} else {return -16776705;} 
            case 248:  if(read) {return 8388096;} else {return -8388097;} 
            case 249:  if(read) {return 4193792;} else {return -4193793;} 
            case 250:  if(read) {return 2096640;} else {return -2096641;} 
            case 251:  if(read) {return 1048064;} else {return -1048065;} 
            case 252:  if(read) {return 523776;} else {return -523777;} 
            case 253:  if(read) {return 261632;} else {return -261633;} 
            case 254:  if(read) {return 130560;} else {return -130561;} 
            case 255:  if(read) {return 65024;} else {return -65025;} 
            case 264:  if(read) {return 128;} else {return -129;} 
            case 265:  if(read) {return 192;} else {return -193;} 
            case 266:  if(read) {return 224;} else {return -225;} 
            case 267:  if(read) {return 240;} else {return -241;} 
            case 268:  if(read) {return 248;} else {return -249;} 
            case 269:  if(read) {return 252;} else {return -253;} 
            case 270:  if(read) {return 254;} else {return -255;} 
            case 271:  if(read) {return 255;} else {return -256;} 
            case 272:  if(read) {return 2147483392;} else {return -2147483393;} 
            case 273:  if(read) {return 1073741568;} else {return -1073741569;} 
            case 274:  if(read) {return 536870656;} else {return -536870657;} 
            case 275:  if(read) {return 268435200;} else {return -268435201;} 
            case 276:  if(read) {return 134217472;} else {return -134217473;} 
            case 277:  if(read) {return 67108608;} else {return -67108609;} 
            case 278:  if(read) {return 33554176;} else {return -33554177;} 
            case 279:  if(read) {return 16776960;} else {return -16776961;} 
            case 280:  if(read) {return 8388352;} else {return -8388353;} 
            case 281:  if(read) {return 4194048;} else {return -4194049;} 
            case 282:  if(read) {return 2096896;} else {return -2096897;} 
            case 283:  if(read) {return 1048320;} else {return -1048321;} 
            case 284:  if(read) {return 524032;} else {return -524033;} 
            case 285:  if(read) {return 261888;} else {return -261889;} 
            case 286:  if(read) {return 130816;} else {return -130817;} 
            case 287:  if(read) {return 65280;} else {return -65281;} 
            case 297:  if(read) {return 64;} else {return -65;} 
            case 298:  if(read) {return 96;} else {return -97;} 
            case 299:  if(read) {return 112;} else {return -113;} 
            case 300:  if(read) {return 120;} else {return -121;} 
            case 301:  if(read) {return 124;} else {return -125;} 
            case 302:  if(read) {return 126;} else {return -127;} 
            case 303:  if(read) {return 127;} else {return -128;} 
            case 304:  if(read) {return 2147483520;} else {return -2147483521;} 
            case 305:  if(read) {return 1073741696;} else {return -1073741697;} 
            case 306:  if(read) {return 536870784;} else {return -536870785;} 
            case 307:  if(read) {return 268435328;} else {return -268435329;} 
            case 308:  if(read) {return 134217600;} else {return -134217601;} 
            case 309:  if(read) {return 67108736;} else {return -67108737;} 
            case 310:  if(read) {return 33554304;} else {return -33554305;} 
            case 311:  if(read) {return 16777088;} else {return -16777089;} 
            case 312:  if(read) {return 8388480;} else {return -8388481;} 
            case 313:  if(read) {return 4194176;} else {return -4194177;} 
            case 314:  if(read) {return 2097024;} else {return -2097025;} 
            case 315:  if(read) {return 1048448;} else {return -1048449;} 
            case 316:  if(read) {return 524160;} else {return -524161;} 
            case 317:  if(read) {return 262016;} else {return -262017;} 
            case 318:  if(read) {return 130944;} else {return -130945;} 
            case 319:  if(read) {return 65408;} else {return -65409;} 
            case 330:  if(read) {return 32;} else {return -33;} 
            case 331:  if(read) {return 48;} else {return -49;} 
            case 332:  if(read) {return 56;} else {return -57;} 
            case 333:  if(read) {return 60;} else {return -61;} 
            case 334:  if(read) {return 62;} else {return -63;} 
            case 335:  if(read) {return 63;} else {return -64;} 
            case 336:  if(read) {return 2147483584;} else {return -2147483585;} 
            case 337:  if(read) {return 1073741760;} else {return -1073741761;} 
            case 338:  if(read) {return 536870848;} else {return -536870849;} 
            case 339:  if(read) {return 268435392;} else {return -268435393;} 
            case 340:  if(read) {return 134217664;} else {return -134217665;} 
            case 341:  if(read) {return 67108800;} else {return -67108801;} 
            case 342:  if(read) {return 33554368;} else {return -33554369;} 
            case 343:  if(read) {return 16777152;} else {return -16777153;} 
            case 344:  if(read) {return 8388544;} else {return -8388545;} 
            case 345:  if(read) {return 4194240;} else {return -4194241;} 
            case 346:  if(read) {return 2097088;} else {return -2097089;} 
            case 347:  if(read) {return 1048512;} else {return -1048513;} 
            case 348:  if(read) {return 524224;} else {return -524225;} 
            case 349:  if(read) {return 262080;} else {return -262081;} 
            case 350:  if(read) {return 131008;} else {return -131009;} 
            case 351:  if(read) {return 65472;} else {return -65473;} 
            case 363:  if(read) {return 16;} else {return -17;} 
            case 364:  if(read) {return 24;} else {return -25;} 
            case 365:  if(read) {return 28;} else {return -29;} 
            case 366:  if(read) {return 30;} else {return -31;} 
            case 367:  if(read) {return 31;} else {return -32;} 
            case 368:  if(read) {return 2147483616;} else {return -2147483617;} 
            case 369:  if(read) {return 1073741792;} else {return -1073741793;} 
            case 370:  if(read) {return 536870880;} else {return -536870881;} 
            case 371:  if(read) {return 268435424;} else {return -268435425;} 
            case 372:  if(read) {return 134217696;} else {return -134217697;} 
            case 373:  if(read) {return 67108832;} else {return -67108833;} 
            case 374:  if(read) {return 33554400;} else {return -33554401;} 
            case 375:  if(read) {return 16777184;} else {return -16777185;} 
            case 376:  if(read) {return 8388576;} else {return -8388577;} 
            case 377:  if(read) {return 4194272;} else {return -4194273;} 
            case 378:  if(read) {return 2097120;} else {return -2097121;} 
            case 379:  if(read) {return 1048544;} else {return -1048545;} 
            case 380:  if(read) {return 524256;} else {return -524257;} 
            case 381:  if(read) {return 262112;} else {return -262113;} 
            case 382:  if(read) {return 131040;} else {return -131041;} 
            case 383:  if(read) {return 65504;} else {return -65505;} 
            case 396:  if(read) {return 8;} else {return -9;} 
            case 397:  if(read) {return 12;} else {return -13;} 
            case 398:  if(read) {return 14;} else {return -15;} 
            case 399:  if(read) {return 15;} else {return -16;} 
            case 400:  if(read) {return 2147483632;} else {return -2147483633;} 
            case 401:  if(read) {return 1073741808;} else {return -1073741809;} 
            case 402:  if(read) {return 536870896;} else {return -536870897;} 
            case 403:  if(read) {return 268435440;} else {return -268435441;} 
            case 404:  if(read) {return 134217712;} else {return -134217713;} 
            case 405:  if(read) {return 67108848;} else {return -67108849;} 
            case 406:  if(read) {return 33554416;} else {return -33554417;} 
            case 407:  if(read) {return 16777200;} else {return -16777201;} 
            case 408:  if(read) {return 8388592;} else {return -8388593;} 
            case 409:  if(read) {return 4194288;} else {return -4194289;} 
            case 410:  if(read) {return 2097136;} else {return -2097137;} 
            case 411:  if(read) {return 1048560;} else {return -1048561;} 
            case 412:  if(read) {return 524272;} else {return -524273;} 
            case 413:  if(read) {return 262128;} else {return -262129;} 
            case 414:  if(read) {return 131056;} else {return -131057;} 
            case 415:  if(read) {return 65520;} else {return -65521;} 
            case 429:  if(read) {return 4;} else {return -5;} 
            case 430:  if(read) {return 6;} else {return -7;} 
            case 431:  if(read) {return 7;} else {return -8;} 
            case 432:  if(read) {return 2147483640;} else {return -2147483641;} 
            case 433:  if(read) {return 1073741816;} else {return -1073741817;} 
            case 434:  if(read) {return 536870904;} else {return -536870905;} 
            case 435:  if(read) {return 268435448;} else {return -268435449;} 
            case 436:  if(read) {return 134217720;} else {return -134217721;} 
            case 437:  if(read) {return 67108856;} else {return -67108857;} 
            case 438:  if(read) {return 33554424;} else {return -33554425;} 
            case 439:  if(read) {return 16777208;} else {return -16777209;} 
            case 440:  if(read) {return 8388600;} else {return -8388601;} 
            case 441:  if(read) {return 4194296;} else {return -4194297;} 
            case 442:  if(read) {return 2097144;} else {return -2097145;} 
            case 443:  if(read) {return 1048568;} else {return -1048569;} 
            case 444:  if(read) {return 524280;} else {return -524281;} 
            case 445:  if(read) {return 262136;} else {return -262137;} 
            case 446:  if(read) {return 131064;} else {return -131065;} 
            case 447:  if(read) {return 65528;} else {return -65529;} 
            case 462:  if(read) {return 2;} else {return -3;} 
            case 463:  if(read) {return 3;} else {return -4;} 
            case 464:  if(read) {return 2147483644;} else {return -2147483645;} 
            case 465:  if(read) {return 1073741820;} else {return -1073741821;} 
            case 466:  if(read) {return 536870908;} else {return -536870909;} 
            case 467:  if(read) {return 268435452;} else {return -268435453;} 
            case 468:  if(read) {return 134217724;} else {return -134217725;} 
            case 469:  if(read) {return 67108860;} else {return -67108861;} 
            case 470:  if(read) {return 33554428;} else {return -33554429;} 
            case 471:  if(read) {return 16777212;} else {return -16777213;} 
            case 472:  if(read) {return 8388604;} else {return -8388605;} 
            case 473:  if(read) {return 4194300;} else {return -4194301;} 
            case 474:  if(read) {return 2097148;} else {return -2097149;} 
            case 475:  if(read) {return 1048572;} else {return -1048573;} 
            case 476:  if(read) {return 524284;} else {return -524285;} 
            case 477:  if(read) {return 262140;} else {return -262141;} 
            case 478:  if(read) {return 131068;} else {return -131069;} 
            case 479:  if(read) {return 65532;} else {return -65533;} 
            case 495:  if(read) {return 1;} else {return -2;} 
            case 496:  if(read) {return 2147483646;} else {return -2147483647;} 
            case 497:  if(read) {return 1073741822;} else {return -1073741823;} 
            case 498:  if(read) {return 536870910;} else {return -536870911;} 
            case 499:  if(read) {return 268435454;} else {return -268435455;} 
            case 500:  if(read) {return 134217726;} else {return -134217727;} 
            case 501:  if(read) {return 67108862;} else {return -67108863;} 
            case 502:  if(read) {return 33554430;} else {return -33554431;} 
            case 503:  if(read) {return 16777214;} else {return -16777215;} 
            case 504:  if(read) {return 8388606;} else {return -8388607;} 
            case 505:  if(read) {return 4194302;} else {return -4194303;} 
            case 506:  if(read) {return 2097150;} else {return -2097151;} 
            case 507:  if(read) {return 1048574;} else {return -1048575;} 
            case 508:  if(read) {return 524286;} else {return -524287;} 
            case 509:  if(read) {return 262142;} else {return -262143;} 
            case 510:  if(read) {return 131070;} else {return -131071;} 
            case 511:  if(read) {return 65534;} else {return -65535;} 
            case 528:  if(read) {return -2147483648;} else {return 2147483647;} 
            case 529:  if(read) {return -1073741824;} else {return 1073741823;} 
            case 530:  if(read) {return -536870912;} else {return 536870911;} 
            case 531:  if(read) {return -268435456;} else {return 268435455;} 
            case 532:  if(read) {return -134217728;} else {return 134217727;} 
            case 533:  if(read) {return -67108864;} else {return 67108863;} 
            case 534:  if(read) {return -33554432;} else {return 33554431;} 
            case 535:  if(read) {return -16777216;} else {return 16777215;} 
            case 536:  if(read) {return -8388608;} else {return 8388607;} 
            case 537:  if(read) {return -4194304;} else {return 4194303;} 
            case 538:  if(read) {return -2097152;} else {return 2097151;} 
            case 539:  if(read) {return -1048576;} else {return 1048575;} 
            case 540:  if(read) {return -524288;} else {return 524287;} 
            case 541:  if(read) {return -262144;} else {return 262143;} 
            case 542:  if(read) {return -131072;} else {return 131071;} 
            case 543:  if(read) {return -65536;} else {return 65535;} 
            case 561:  if(read) {return 1073741824;} else {return -1073741825;} 
            case 562:  if(read) {return 1610612736;} else {return -1610612737;} 
            case 563:  if(read) {return 1879048192;} else {return -1879048193;} 
            case 564:  if(read) {return 2013265920;} else {return -2013265921;} 
            case 565:  if(read) {return 2080374784;} else {return -2080374785;} 
            case 566:  if(read) {return 2113929216;} else {return -2113929217;} 
            case 567:  if(read) {return 2130706432;} else {return -2130706433;} 
            case 568:  if(read) {return 2139095040;} else {return -2139095041;} 
            case 569:  if(read) {return 2143289344;} else {return -2143289345;} 
            case 570:  if(read) {return 2145386496;} else {return -2145386497;} 
            case 571:  if(read) {return 2146435072;} else {return -2146435073;} 
            case 572:  if(read) {return 2146959360;} else {return -2146959361;} 
            case 573:  if(read) {return 2147221504;} else {return -2147221505;} 
            case 574:  if(read) {return 2147352576;} else {return -2147352577;} 
            case 575:  if(read) {return 2147418112;} else {return -2147418113;} 
            case 594:  if(read) {return 536870912;} else {return -536870913;} 
            case 595:  if(read) {return 805306368;} else {return -805306369;} 
            case 596:  if(read) {return 939524096;} else {return -939524097;} 
            case 597:  if(read) {return 1006632960;} else {return -1006632961;} 
            case 598:  if(read) {return 1040187392;} else {return -1040187393;} 
            case 599:  if(read) {return 1056964608;} else {return -1056964609;} 
            case 600:  if(read) {return 1065353216;} else {return -1065353217;} 
            case 601:  if(read) {return 1069547520;} else {return -1069547521;} 
            case 602:  if(read) {return 1071644672;} else {return -1071644673;} 
            case 603:  if(read) {return 1072693248;} else {return -1072693249;} 
            case 604:  if(read) {return 1073217536;} else {return -1073217537;} 
            case 605:  if(read) {return 1073479680;} else {return -1073479681;} 
            case 606:  if(read) {return 1073610752;} else {return -1073610753;} 
            case 607:  if(read) {return 1073676288;} else {return -1073676289;} 
            case 627:  if(read) {return 268435456;} else {return -268435457;} 
            case 628:  if(read) {return 402653184;} else {return -402653185;} 
            case 629:  if(read) {return 469762048;} else {return -469762049;} 
            case 630:  if(read) {return 503316480;} else {return -503316481;} 
            case 631:  if(read) {return 520093696;} else {return -520093697;} 
            case 632:  if(read) {return 528482304;} else {return -528482305;} 
            case 633:  if(read) {return 532676608;} else {return -532676609;} 
            case 634:  if(read) {return 534773760;} else {return -534773761;} 
            case 635:  if(read) {return 535822336;} else {return -535822337;} 
            case 636:  if(read) {return 536346624;} else {return -536346625;} 
            case 637:  if(read) {return 536608768;} else {return -536608769;} 
            case 638:  if(read) {return 536739840;} else {return -536739841;} 
            case 639:  if(read) {return 536805376;} else {return -536805377;} 
            case 660:  if(read) {return 134217728;} else {return -134217729;} 
            case 661:  if(read) {return 201326592;} else {return -201326593;} 
            case 662:  if(read) {return 234881024;} else {return -234881025;} 
            case 663:  if(read) {return 251658240;} else {return -251658241;} 
            case 664:  if(read) {return 260046848;} else {return -260046849;} 
            case 665:  if(read) {return 264241152;} else {return -264241153;} 
            case 666:  if(read) {return 266338304;} else {return -266338305;} 
            case 667:  if(read) {return 267386880;} else {return -267386881;} 
            case 668:  if(read) {return 267911168;} else {return -267911169;} 
            case 669:  if(read) {return 268173312;} else {return -268173313;} 
            case 670:  if(read) {return 268304384;} else {return -268304385;} 
            case 671:  if(read) {return 268369920;} else {return -268369921;} 
            case 693:  if(read) {return 67108864;} else {return -67108865;} 
            case 694:  if(read) {return 100663296;} else {return -100663297;} 
            case 695:  if(read) {return 117440512;} else {return -117440513;} 
            case 696:  if(read) {return 125829120;} else {return -125829121;} 
            case 697:  if(read) {return 130023424;} else {return -130023425;} 
            case 698:  if(read) {return 132120576;} else {return -132120577;} 
            case 699:  if(read) {return 133169152;} else {return -133169153;} 
            case 700:  if(read) {return 133693440;} else {return -133693441;} 
            case 701:  if(read) {return 133955584;} else {return -133955585;} 
            case 702:  if(read) {return 134086656;} else {return -134086657;} 
            case 703:  if(read) {return 134152192;} else {return -134152193;} 
            case 726:  if(read) {return 33554432;} else {return -33554433;} 
            case 727:  if(read) {return 50331648;} else {return -50331649;} 
            case 728:  if(read) {return 58720256;} else {return -58720257;} 
            case 729:  if(read) {return 62914560;} else {return -62914561;} 
            case 730:  if(read) {return 65011712;} else {return -65011713;} 
            case 731:  if(read) {return 66060288;} else {return -66060289;} 
            case 732:  if(read) {return 66584576;} else {return -66584577;} 
            case 733:  if(read) {return 66846720;} else {return -66846721;} 
            case 734:  if(read) {return 66977792;} else {return -66977793;} 
            case 735:  if(read) {return 67043328;} else {return -67043329;} 
            case 759:  if(read) {return 16777216;} else {return -16777217;} 
            case 760:  if(read) {return 25165824;} else {return -25165825;} 
            case 761:  if(read) {return 29360128;} else {return -29360129;} 
            case 762:  if(read) {return 31457280;} else {return -31457281;} 
            case 763:  if(read) {return 32505856;} else {return -32505857;} 
            case 764:  if(read) {return 33030144;} else {return -33030145;} 
            case 765:  if(read) {return 33292288;} else {return -33292289;} 
            case 766:  if(read) {return 33423360;} else {return -33423361;} 
            case 767:  if(read) {return 33488896;} else {return -33488897;} 
            case 792:  if(read) {return 8388608;} else {return -8388609;} 
            case 793:  if(read) {return 12582912;} else {return -12582913;} 
            case 794:  if(read) {return 14680064;} else {return -14680065;} 
            case 795:  if(read) {return 15728640;} else {return -15728641;} 
            case 796:  if(read) {return 16252928;} else {return -16252929;} 
            case 797:  if(read) {return 16515072;} else {return -16515073;} 
            case 798:  if(read) {return 16646144;} else {return -16646145;} 
            case 799:  if(read) {return 16711680;} else {return -16711681;} 
            case 825:  if(read) {return 4194304;} else {return -4194305;} 
            case 826:  if(read) {return 6291456;} else {return -6291457;} 
            case 827:  if(read) {return 7340032;} else {return -7340033;} 
            case 828:  if(read) {return 7864320;} else {return -7864321;} 
            case 829:  if(read) {return 8126464;} else {return -8126465;} 
            case 830:  if(read) {return 8257536;} else {return -8257537;} 
            case 831:  if(read) {return 8323072;} else {return -8323073;} 
            case 858:  if(read) {return 2097152;} else {return -2097153;} 
            case 859:  if(read) {return 3145728;} else {return -3145729;} 
            case 860:  if(read) {return 3670016;} else {return -3670017;} 
            case 861:  if(read) {return 3932160;} else {return -3932161;} 
            case 862:  if(read) {return 4063232;} else {return -4063233;} 
            case 863:  if(read) {return 4128768;} else {return -4128769;} 
            case 891:  if(read) {return 1048576;} else {return -1048577;} 
            case 892:  if(read) {return 1572864;} else {return -1572865;} 
            case 893:  if(read) {return 1835008;} else {return -1835009;} 
            case 894:  if(read) {return 1966080;} else {return -1966081;} 
            case 895:  if(read) {return 2031616;} else {return -2031617;} 
            case 924:  if(read) {return 524288;} else {return -524289;} 
            case 925:  if(read) {return 786432;} else {return -786433;} 
            case 926:  if(read) {return 917504;} else {return -917505;} 
            case 927:  if(read) {return 983040;} else {return -983041;} 
            case 957:  if(read) {return 262144;} else {return -262145;} 
            case 958:  if(read) {return 393216;} else {return -393217;} 
            case 959:  if(read) {return 458752;} else {return -458753;} 
            case 990:  if(read) {return 131072;} else {return -131073;} 
            case 991:  if(read) {return 196608;} else {return -196609;} 
            case 1023:  if(read) {return 65536;} else {return -65537;}
        }
        return 0;
    }

    private int whichBit2(int index, int bitloc) {
        int bit = bitloc + index*2000;
        switch (bit) {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 4;
            case 5: return 5;
            case 6: return 6;
            case 7: return 7;
            case 8: return 8;
            case 9: return 9;
            case 10: return 10;
            case 11: return 11;
            case 12: return 12;
            case 13: return 13;
            case 14: return 14;
            case 15: return 15;
            case 2016: return 0;
            case 2017: return 1;
            case 2018: return 2;
            case 2019: return 3;
            case 2020: return 4;
            case 2021: return 5;
            case 2022: return 6;
            case 2023: return 7;
            case 2024: return 8;
            case 2025: return 9;
            case 2026: return 10;
            case 2027: return 11;
            case 2028: return 12;
            case 2029: return 13;
            case 2030: return 14;
            case 2031: return 15;
            case 4032: return 0;
            case 4033: return 1;
            case 4034: return 2;
            case 4035: return 3;
            case 4036: return 4;
            case 4037: return 5;
            case 4038: return 6;
            case 4039: return 7;
            case 4040: return 8;
            case 4041: return 9;
            case 4042: return 10;
            case 4043: return 11;
            case 4044: return 12;
            case 4045: return 13;
            case 4046: return 14;
            case 4047: return 15;
            case 6048: return 0;
            case 6049: return 1;
            case 6050: return 2;
            case 6051: return 3;
            case 6052: return 4;
            case 6053: return 5;
            case 6054: return 6;
            case 6055: return 7;
            case 6056: return 8;
            case 6057: return 9;
            case 6058: return 10;
            case 6059: return 11;
            case 6060: return 12;
            case 6061: return 13;
            case 6062: return 14;
            case 6063: return 15;
            case 8064: return 0;
            case 8065: return 1;
            case 8066: return 2;
            case 8067: return 3;
            case 8068: return 4;
            case 8069: return 5;
            case 8070: return 6;
            case 8071: return 7;
            case 8072: return 8;
            case 8073: return 9;
            case 8074: return 10;
            case 8075: return 11;
            case 8076: return 12;
            case 8077: return 13;
            case 8078: return 14;
            case 8079: return 15;
            case 10080: return 0;
            case 10081: return 1;
            case 10082: return 2;
            case 10083: return 3;
            case 10084: return 4;
            case 10085: return 5;
            case 10086: return 6;
            case 10087: return 7;
            case 10088: return 8;
            case 10089: return 9;
            case 10090: return 10;
            case 10091: return 11;
            case 10092: return 12;
            case 10093: return 13;
            case 10094: return 14;
            case 10095: return 15;
            case 12096: return 0;
            case 12097: return 1;
            case 12098: return 2;
            case 12099: return 3;
            case 12100: return 4;
            case 12101: return 5;
            case 12102: return 6;
            case 12103: return 7;
            case 12104: return 8;
            case 12105: return 9;
            case 12106: return 10;
            case 12107: return 11;
            case 12108: return 12;
            case 12109: return 13;
            case 12110: return 14;
            case 12111: return 15;
            case 14112: return 0;
            case 14113: return 1;
            case 14114: return 2;
            case 14115: return 3;
            case 14116: return 4;
            case 14117: return 5;
            case 14118: return 6;
            case 14119: return 7;
            case 14120: return 8;
            case 14121: return 9;
            case 14122: return 10;
            case 14123: return 11;
            case 14124: return 12;
            case 14125: return 13;
            case 14126: return 14;
            case 14127: return 15;
            case 16128: return 0;
            case 16129: return 1;
            case 16130: return 2;
            case 16131: return 3;
            case 16132: return 4;
            case 16133: return 5;
            case 16134: return 6;
            case 16135: return 7;
            case 16136: return 8;
            case 16137: return 9;
            case 16138: return 10;
            case 16139: return 11;
            case 16140: return 12;
            case 16141: return 13;
            case 16142: return 14;
            case 16143: return 15;
            case 18144: return 0;
            case 18145: return 1;
            case 18146: return 2;
            case 18147: return 3;
            case 18148: return 4;
            case 18149: return 5;
            case 18150: return 6;
            case 18151: return 7;
            case 18152: return 8;
            case 18153: return 9;
            case 18154: return 10;
            case 18155: return 11;
            case 18156: return 12;
            case 18157: return 13;
            case 18158: return 14;
            case 18159: return 15;
            case 20160: return 0;
            case 20161: return 1;
            case 20162: return 2;
            case 20163: return 3;
            case 20164: return 4;
            case 20165: return 5;
            case 20166: return 6;
            case 20167: return 7;
            case 20168: return 8;
            case 20169: return 9;
            case 20170: return 10;
            case 20171: return 11;
            case 20172: return 12;
            case 20173: return 13;
            case 20174: return 14;
            case 20175: return 15;
            case 22176: return 0;
            case 22177: return 1;
            case 22178: return 2;
            case 22179: return 3;
            case 22180: return 4;
            case 22181: return 5;
            case 22182: return 6;
            case 22183: return 7;
            case 22184: return 8;
            case 22185: return 9;
            case 22186: return 10;
            case 22187: return 11;
            case 22188: return 12;
            case 22189: return 13;
            case 22190: return 14;
            case 22191: return 15;
            case 24192: return 0;
            case 24193: return 1;
            case 24194: return 2;
            case 24195: return 3;
            case 24196: return 4;
            case 24197: return 5;
            case 24198: return 6;
            case 24199: return 7;
            case 24200: return 8;
            case 24201: return 9;
            case 24202: return 10;
            case 24203: return 11;
            case 24204: return 12;
            case 24205: return 13;
            case 24206: return 14;
            case 24207: return 15;
            case 26208: return 0;
            case 26209: return 1;
            case 26210: return 2;
            case 26211: return 3;
            case 26212: return 4;
            case 26213: return 5;
            case 26214: return 6;
            case 26215: return 7;
            case 26216: return 8;
            case 26217: return 9;
            case 26218: return 10;
            case 26219: return 11;
            case 26220: return 12;
            case 26221: return 13;
            case 26222: return 14;
            case 26223: return 15;
            case 28224: return 0;
            case 28225: return 1;
            case 28226: return 2;
            case 28227: return 3;
            case 28228: return 4;
            case 28229: return 5;
            case 28230: return 6;
            case 28231: return 7;
            case 28232: return 8;
            case 28233: return 9;
            case 28234: return 10;
            case 28235: return 11;
            case 28236: return 12;
            case 28237: return 13;
            case 28238: return 14;
            case 28239: return 15;
            case 30240: return 0;
            case 30241: return 1;
            case 30242: return 2;
            case 30243: return 3;
            case 30244: return 4;
            case 30245: return 5;
            case 30246: return 6;
            case 30247: return 7;
            case 30248: return 8;
            case 30249: return 9;
            case 30250: return 10;
            case 30251: return 11;
            case 30252: return 12;
            case 30253: return 13;
            case 30254: return 14;
            case 30255: return 15;
            case 32256: return 0;
            case 32257: return 1;
            case 32258: return 2;
            case 32259: return 3;
            case 32260: return 4;
            case 32261: return 5;
            case 32262: return 6;
            case 32263: return 7;
            case 32264: return 8;
            case 32265: return 9;
            case 32266: return 10;
            case 32267: return 11;
            case 32268: return 12;
            case 32269: return 13;
            case 32270: return 14;
            case 32271: return 15;
            case 34272: return 0;
            case 34273: return 1;
            case 34274: return 2;
            case 34275: return 3;
            case 34276: return 4;
            case 34277: return 5;
            case 34278: return 6;
            case 34279: return 7;
            case 34280: return 8;
            case 34281: return 9;
            case 34282: return 10;
            case 34283: return 11;
            case 34284: return 12;
            case 34285: return 13;
            case 34286: return 14;
            case 34287: return 15;
            case 36288: return 0;
            case 36289: return 1;
            case 36290: return 2;
            case 36291: return 3;
            case 36292: return 4;
            case 36293: return 5;
            case 36294: return 6;
            case 36295: return 7;
            case 36296: return 8;
            case 36297: return 9;
            case 36298: return 10;
            case 36299: return 11;
            case 36300: return 12;
            case 36301: return 13;
            case 36302: return 14;
            case 36303: return 15;
            case 38304: return 0;
            case 38305: return 1;
            case 38306: return 2;
            case 38307: return 3;
            case 38308: return 4;
            case 38309: return 5;
            case 38310: return 6;
            case 38311: return 7;
            case 38312: return 8;
            case 38313: return 9;
            case 38314: return 10;
            case 38315: return 11;
            case 38316: return 12;
            case 38317: return 13;
            case 38318: return 14;
            case 38319: return 15;
            case 40320: return 0;
            case 40321: return 1;
            case 40322: return 2;
            case 40323: return 3;
            case 40324: return 4;
            case 40325: return 5;
            case 40326: return 6;
            case 40327: return 7;
            case 40328: return 8;
            case 40329: return 9;
            case 40330: return 10;
            case 40331: return 11;
            case 40332: return 12;
            case 40333: return 13;
            case 40334: return 14;
            case 40335: return 15;
            case 42336: return 0;
            case 42337: return 1;
            case 42338: return 2;
            case 42339: return 3;
            case 42340: return 4;
            case 42341: return 5;
            case 42342: return 6;
            case 42343: return 7;
            case 42344: return 8;
            case 42345: return 9;
            case 42346: return 10;
            case 42347: return 11;
            case 42348: return 12;
            case 42349: return 13;
            case 42350: return 14;
            case 42351: return 15;
            case 44352: return 0;
            case 44353: return 1;
            case 44354: return 2;
            case 44355: return 3;
            case 44356: return 4;
            case 44357: return 5;
            case 44358: return 6;
            case 44359: return 7;
            case 44360: return 8;
            case 44361: return 9;
            case 44362: return 10;
            case 44363: return 11;
            case 44364: return 12;
            case 44365: return 13;
            case 44366: return 14;
            case 44367: return 15;
            case 46368: return 0;
            case 46369: return 1;
            case 46370: return 2;
            case 46371: return 3;
            case 46372: return 4;
            case 46373: return 5;
            case 46374: return 6;
            case 46375: return 7;
            case 46376: return 8;
            case 46377: return 9;
            case 46378: return 10;
            case 46379: return 11;
            case 46380: return 12;
            case 46381: return 13;
            case 46382: return 14;
            case 46383: return 15;
            case 48384: return 0;
            case 48385: return 1;
            case 48386: return 2;
            case 48387: return 3;
            case 48388: return 4;
            case 48389: return 5;
            case 48390: return 6;
            case 48391: return 7;
            case 48392: return 8;
            case 48393: return 9;
            case 48394: return 10;
            case 48395: return 11;
            case 48396: return 12;
            case 48397: return 13;
            case 48398: return 14;
            case 48399: return 15;
            case 50400: return 0;
            case 50401: return 1;
            case 50402: return 2;
            case 50403: return 3;
            case 50404: return 4;
            case 50405: return 5;
            case 50406: return 6;
            case 50407: return 7;
            case 50408: return 8;
            case 50409: return 9;
            case 50410: return 10;
            case 50411: return 11;
            case 50412: return 12;
            case 50413: return 13;
            case 50414: return 14;
            case 50415: return 15;
            case 52416: return 0;
            case 52417: return 1;
            case 52418: return 2;
            case 52419: return 3;
            case 52420: return 4;
            case 52421: return 5;
            case 52422: return 6;
            case 52423: return 7;
            case 52424: return 8;
            case 52425: return 9;
            case 52426: return 10;
            case 52427: return 11;
            case 52428: return 12;
            case 52429: return 13;
            case 52430: return 14;
            case 52431: return 15;
            case 54432: return 0;
            case 54433: return 1;
            case 54434: return 2;
            case 54435: return 3;
            case 54436: return 4;
            case 54437: return 5;
            case 54438: return 6;
            case 54439: return 7;
            case 54440: return 8;
            case 54441: return 9;
            case 54442: return 10;
            case 54443: return 11;
            case 54444: return 12;
            case 54445: return 13;
            case 54446: return 14;
            case 54447: return 15;
            case 56448: return 0;
            case 56449: return 1;
            case 56450: return 2;
            case 56451: return 3;
            case 56452: return 4;
            case 56453: return 5;
            case 56454: return 6;
            case 56455: return 7;
            case 56456: return 8;
            case 56457: return 9;
            case 56458: return 10;
            case 56459: return 11;
            case 56460: return 12;
            case 56461: return 13;
            case 56462: return 14;
            case 56463: return 15;
            case 58464: return 0;
            case 58465: return 1;
            case 58466: return 2;
            case 58467: return 3;
            case 58468: return 4;
            case 58469: return 5;
            case 58470: return 6;
            case 58471: return 7;
            case 58472: return 8;
            case 58473: return 9;
            case 58474: return 10;
            case 58475: return 11;
            case 58476: return 12;
            case 58477: return 13;
            case 58478: return 14;
            case 58479: return 15;
            case 60480: return 0;
            case 60481: return 1;
            case 60482: return 2;
            case 60483: return 3;
            case 60484: return 4;
            case 60485: return 5;
            case 60486: return 6;
            case 60487: return 7;
            case 60488: return 8;
            case 60489: return 9;
            case 60490: return 10;
            case 60491: return 11;
            case 60492: return 12;
            case 60493: return 13;
            case 60494: return 14;
            case 60495: return 15;
            case 62496: return 0;
            case 62497: return 1;
            case 62498: return 2;
            case 62499: return 3;
            case 62500: return 4;
            case 62501: return 5;
            case 62502: return 6;
            case 62503: return 7;
            case 62504: return 8;
            case 62505: return 9;
            case 62506: return 10;
            case 62507: return 11;
            case 62508: return 12;
            case 62509: return 13;
            case 62510: return 14;
            case 62511: return 15;
            case 64512: return 0;
            case 64513: return 1;
            case 64514: return 2;
            case 64515: return 3;
            case 64516: return 4;
            case 64517: return 5;
            case 64518: return 6;
            case 64519: return 7;
            case 64520: return 8;
            case 64521: return 9;
            case 64522: return 10;
            case 64523: return 11;
            case 64524: return 12;
            case 64525: return 13;
            case 64526: return 14;
            case 64527: return 15;
            case 66528: return 0;
            case 66529: return 1;
            case 66530: return 2;
            case 66531: return 3;
            case 66532: return 4;
            case 66533: return 5;
            case 66534: return 6;
            case 66535: return 7;
            case 66536: return 8;
            case 66537: return 9;
            case 66538: return 10;
            case 66539: return 11;
            case 66540: return 12;
            case 66541: return 13;
            case 66542: return 14;
            case 66543: return 15;
            case 68544: return 0;
            case 68545: return 1;
            case 68546: return 2;
            case 68547: return 3;
            case 68548: return 4;
            case 68549: return 5;
            case 68550: return 6;
            case 68551: return 7;
            case 68552: return 8;
            case 68553: return 9;
            case 68554: return 10;
            case 68555: return 11;
            case 68556: return 12;
            case 68557: return 13;
            case 68558: return 14;
            case 68559: return 15;
            case 70560: return 0;
            case 70561: return 1;
            case 70562: return 2;
            case 70563: return 3;
            case 70564: return 4;
            case 70565: return 5;
            case 70566: return 6;
            case 70567: return 7;
            case 70568: return 8;
            case 70569: return 9;
            case 70570: return 10;
            case 70571: return 11;
            case 70572: return 12;
            case 70573: return 13;
            case 70574: return 14;
            case 70575: return 15;
            case 72576: return 0;
            case 72577: return 1;
            case 72578: return 2;
            case 72579: return 3;
            case 72580: return 4;
            case 72581: return 5;
            case 72582: return 6;
            case 72583: return 7;
            case 72584: return 8;
            case 72585: return 9;
            case 72586: return 10;
            case 72587: return 11;
            case 72588: return 12;
            case 72589: return 13;
            case 72590: return 14;
            case 72591: return 15;
            case 74592: return 0;
            case 74593: return 1;
            case 74594: return 2;
            case 74595: return 3;
            case 74596: return 4;
            case 74597: return 5;
            case 74598: return 6;
            case 74599: return 7;
            case 74600: return 8;
            case 74601: return 9;
            case 74602: return 10;
            case 74603: return 11;
            case 74604: return 12;
            case 74605: return 13;
            case 74606: return 14;
            case 74607: return 15;
            case 76608: return 0;
            case 76609: return 1;
            case 76610: return 2;
            case 76611: return 3;
            case 76612: return 4;
            case 76613: return 5;
            case 76614: return 6;
            case 76615: return 7;
            case 76616: return 8;
            case 76617: return 9;
            case 76618: return 10;
            case 76619: return 11;
            case 76620: return 12;
            case 76621: return 13;
            case 76622: return 14;
            case 76623: return 15;
            case 78624: return 0;
            case 78625: return 1;
            case 78626: return 2;
            case 78627: return 3;
            case 78628: return 4;
            case 78629: return 5;
            case 78630: return 6;
            case 78631: return 7;
            case 78632: return 8;
            case 78633: return 9;
            case 78634: return 10;
            case 78635: return 11;
            case 78636: return 12;
            case 78637: return 13;
            case 78638: return 14;
            case 78639: return 15;
            case 80640: return 0;
            case 80641: return 1;
            case 80642: return 2;
            case 80643: return 3;
            case 80644: return 4;
            case 80645: return 5;
            case 80646: return 6;
            case 80647: return 7;
            case 80648: return 8;
            case 80649: return 9;
            case 80650: return 10;
            case 80651: return 11;
            case 80652: return 12;
            case 80653: return 13;
            case 80654: return 14;
            case 80655: return 15;
            case 82656: return 0;
            case 82657: return 1;
            case 82658: return 2;
            case 82659: return 3;
            case 82660: return 4;
            case 82661: return 5;
            case 82662: return 6;
            case 82663: return 7;
            case 82664: return 8;
            case 82665: return 9;
            case 82666: return 10;
            case 82667: return 11;
            case 82668: return 12;
            case 82669: return 13;
            case 82670: return 14;
            case 82671: return 15;
            case 84672: return 0;
            case 84673: return 1;
            case 84674: return 2;
            case 84675: return 3;
            case 84676: return 4;
            case 84677: return 5;
            case 84678: return 6;
            case 84679: return 7;
            case 84680: return 8;
            case 84681: return 9;
            case 84682: return 10;
            case 84683: return 11;
            case 84684: return 12;
            case 84685: return 13;
            case 84686: return 14;
            case 84687: return 15;
            case 86688: return 0;
            case 86689: return 1;
            case 86690: return 2;
            case 86691: return 3;
            case 86692: return 4;
            case 86693: return 5;
            case 86694: return 6;
            case 86695: return 7;
            case 86696: return 8;
            case 86697: return 9;
            case 86698: return 10;
            case 86699: return 11;
            case 86700: return 12;
            case 86701: return 13;
            case 86702: return 14;
            case 86703: return 15;
            case 88704: return 0;
            case 88705: return 1;
            case 88706: return 2;
            case 88707: return 3;
            case 88708: return 4;
            case 88709: return 5;
            case 88710: return 6;
            case 88711: return 7;
            case 88712: return 8;
            case 88713: return 9;
            case 88714: return 10;
            case 88715: return 11;
            case 88716: return 12;
            case 88717: return 13;
            case 88718: return 14;
            case 88719: return 15;
            case 90720: return 0;
            case 90721: return 1;
            case 90722: return 2;
            case 90723: return 3;
            case 90724: return 4;
            case 90725: return 5;
            case 90726: return 6;
            case 90727: return 7;
            case 90728: return 8;
            case 90729: return 9;
            case 90730: return 10;
            case 90731: return 11;
            case 90732: return 12;
            case 90733: return 13;
            case 90734: return 14;
            case 90735: return 15;
            case 92736: return 0;
            case 92737: return 1;
            case 92738: return 2;
            case 92739: return 3;
            case 92740: return 4;
            case 92741: return 5;
            case 92742: return 6;
            case 92743: return 7;
            case 92744: return 8;
            case 92745: return 9;
            case 92746: return 10;
            case 92747: return 11;
            case 92748: return 12;
            case 92749: return 13;
            case 92750: return 14;
            case 92751: return 15;
            case 94752: return 0;
            case 94753: return 1;
            case 94754: return 2;
            case 94755: return 3;
            case 94756: return 4;
            case 94757: return 5;
            case 94758: return 6;
            case 94759: return 7;
            case 94760: return 8;
            case 94761: return 9;
            case 94762: return 10;
            case 94763: return 11;
            case 94764: return 12;
            case 94765: return 13;
            case 94766: return 14;
            case 94767: return 15;
            case 96768: return 0;
            case 96769: return 1;
            case 96770: return 2;
            case 96771: return 3;
            case 96772: return 4;
            case 96773: return 5;
            case 96774: return 6;
            case 96775: return 7;
            case 96776: return 8;
            case 96777: return 9;
            case 96778: return 10;
            case 96779: return 11;
            case 96780: return 12;
            case 96781: return 13;
            case 96782: return 14;
            case 96783: return 15;
            case 98784: return 0;
            case 98785: return 1;
            case 98786: return 2;
            case 98787: return 3;
            case 98788: return 4;
            case 98789: return 5;
            case 98790: return 6;
            case 98791: return 7;
            case 98792: return 8;
            case 98793: return 9;
            case 98794: return 10;
            case 98795: return 11;
            case 98796: return 12;
            case 98797: return 13;
            case 98798: return 14;
            case 98799: return 15;
            case 100800: return 0;
            case 100801: return 1;
            case 100802: return 2;
            case 100803: return 3;
            case 100804: return 4;
            case 100805: return 5;
            case 100806: return 6;
            case 100807: return 7;
            case 100808: return 8;
            case 100809: return 9;
            case 100810: return 10;
            case 100811: return 11;
            case 100812: return 12;
            case 100813: return 13;
            case 100814: return 14;
            case 100815: return 15;
            case 102816: return 0;
            case 102817: return 1;
            case 102818: return 2;
            case 102819: return 3;
            case 102820: return 4;
            case 102821: return 5;
            case 102822: return 6;
            case 102823: return 7;
            case 102824: return 8;
            case 102825: return 9;
            case 102826: return 10;
            case 102827: return 11;
            case 102828: return 12;
            case 102829: return 13;
            case 102830: return 14;
            case 102831: return 15;
            case 104832: return 0;
            case 104833: return 1;
            case 104834: return 2;
            case 104835: return 3;
            case 104836: return 4;
            case 104837: return 5;
            case 104838: return 6;
            case 104839: return 7;
            case 104840: return 8;
            case 104841: return 9;
            case 104842: return 10;
            case 104843: return 11;
            case 104844: return 12;
            case 104845: return 13;
            case 104846: return 14;
            case 104847: return 15;
            case 106848: return 0;
            case 106849: return 1;
            case 106850: return 2;
            case 106851: return 3;
            case 106852: return 4;
            case 106853: return 5;
            case 106854: return 6;
            case 106855: return 7;
            case 106856: return 8;
            case 106857: return 9;
            case 106858: return 10;
            case 106859: return 11;
            case 106860: return 12;
            case 106861: return 13;
            case 106862: return 14;
            case 106863: return 15;
            case 108864: return 0;
            case 108865: return 1;
            case 108866: return 2;
            case 108867: return 3;
            case 108868: return 4;
            case 108869: return 5;
            case 108870: return 6;
            case 108871: return 7;
            case 108872: return 8;
            case 108873: return 9;
            case 108874: return 10;
            case 108875: return 11;
            case 108876: return 12;
            case 108877: return 13;
            case 108878: return 14;
            case 108879: return 15;
            case 110880: return 0;
            case 110881: return 1;
            case 110882: return 2;
            case 110883: return 3;
            case 110884: return 4;
            case 110885: return 5;
            case 110886: return 6;
            case 110887: return 7;
            case 110888: return 8;
            case 110889: return 9;
            case 110890: return 10;
            case 110891: return 11;
            case 110892: return 12;
            case 110893: return 13;
            case 110894: return 14;
            case 110895: return 15;
            case 112896: return 0;
            case 112897: return 1;
            case 112898: return 2;
            case 112899: return 3;
            case 112900: return 4;
            case 112901: return 5;
            case 112902: return 6;
            case 112903: return 7;
            case 112904: return 8;
            case 112905: return 9;
            case 112906: return 10;
            case 112907: return 11;
            case 112908: return 12;
            case 112909: return 13;
            case 112910: return 14;
            case 112911: return 15;
            case 114912: return 0;
            case 114913: return 1;
            case 114914: return 2;
            case 114915: return 3;
            case 114916: return 4;
            case 114917: return 5;
            case 114918: return 6;
            case 114919: return 7;
            case 114920: return 8;
            case 114921: return 9;
            case 114922: return 10;
            case 114923: return 11;
            case 114924: return 12;
            case 114925: return 13;
            case 114926: return 14;
            case 114927: return 15;
            case 116928: return 0;
            case 116929: return 1;
            case 116930: return 2;
            case 116931: return 3;
            case 116932: return 4;
            case 116933: return 5;
            case 116934: return 6;
            case 116935: return 7;
            case 116936: return 8;
            case 116937: return 9;
            case 116938: return 10;
            case 116939: return 11;
            case 116940: return 12;
            case 116941: return 13;
            case 116942: return 14;
            case 116943: return 15;
            case 118944: return 0;
            case 118945: return 1;
            case 118946: return 2;
            case 118947: return 3;
            case 118948: return 4;
            case 118949: return 5;
            case 118950: return 6;
            case 118951: return 7;
            case 118952: return 8;
            case 118953: return 9;
            case 118954: return 10;
            case 118955: return 11;
            case 118956: return 12;
            case 118957: return 13;
            case 118958: return 14;
            case 118959: return 15;
            case 120960: return 0;
            case 120961: return 1;
            case 120962: return 2;
            case 120963: return 3;
            case 120964: return 4;
            case 120965: return 5;
            case 120966: return 6;
            case 120967: return 7;
            case 120968: return 8;
            case 120969: return 9;
            case 120970: return 10;
            case 120971: return 11;
            case 120972: return 12;
            case 120973: return 13;
            case 120974: return 14;
            case 120975: return 15;
            case 122976: return 0;
            case 122977: return 1;
            case 122978: return 2;
            case 122979: return 3;
            case 122980: return 4;
            case 122981: return 5;
            case 122982: return 6;
            case 122983: return 7;
            case 122984: return 8;
            case 122985: return 9;
            case 122986: return 10;
            case 122987: return 11;
            case 122988: return 12;
            case 122989: return 13;
            case 122990: return 14;
            case 122991: return 15;
            case 124992: return 0;
            case 124993: return 1;
            case 124994: return 2;
            case 124995: return 3;
            case 124996: return 4;
            case 124997: return 5;
            case 124998: return 6;
            case 124999: return 7;
            case 125000: return 8;
            case 125001: return 9;
            case 125002: return 10;
            case 125003: return 11;
            case 125004: return 12;
            case 125005: return 13;
            case 125006: return 14;
            case 125007: return 15;
            case 127008: return 0;
            case 127009: return 1;
            case 127010: return 2;
            case 127011: return 3;
            case 127012: return 4;
            case 127013: return 5;
            case 127014: return 6;
            case 127015: return 7;
            case 127016: return 8;
            case 127017: return 9;
            case 127018: return 10;
            case 127019: return 11;
            case 127020: return 12;
            case 127021: return 13;
            case 127022: return 14;
            case 127023: return 15;
        }
        return 0;
    }

}