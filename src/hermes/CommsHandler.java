package hermes;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    // Max chunk size: 16 bits
    // TODO (Nikhil): support up to 32 bit chunks (requires modifying low-level functions to cover split over 3 case)
    // Chunk schema:
    int OUR_ARCHON_BITS = 16; // 4 bits: status; 6 bits: x coordinate; 6 bits: y coordinate
    int ENEMY_ARCHON_BITS = 13;
    int MAP_SYMMETRY_BITS = 2; // 1 bit: horizontal symmetry; 1 bit: vertical symmetry
    int CLUSTER_BITS = 8; // 2 bits: cluster control status; 2 bits: how actively are we reinforcing; 4 bits: resource count
    int[] CHUNK_SIZES = {
        OUR_ARCHON_BITS, OUR_ARCHON_BITS, OUR_ARCHON_BITS, OUR_ARCHON_BITS,             // our 4 archons
        ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS, ENEMY_ARCHON_BITS,     // enemy 4 archons
        MAP_SYMMETRY_BITS,
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, // 32 clusters
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, CLUSTER_BITS, 
        // TODO: add more
    };
    int[] CHUNK_OFFSETS = new int[CHUNK_SIZES.length]; // TODO: precompute prefix sums of CHUNK_SIZES

    final int SHARED_ARRAY_ELEM_SIZE = 16;
    final int SHARED_ARRAY_ELEM_LOG2 = 4;

    boolean unitTest = false;
    // for unit test only
    int[] sharedArray;

    public CommsHandler(RobotController rc) {
        this.rc = rc;
        for (int i = 0; i < CHUNK_SIZES.length; i++) { // TODO: remove once we precompute CHUNK_OFFSETS
            CHUNK_OFFSETS[i] = (i == 0) ? 0 : CHUNK_OFFSETS[i-1] + CHUNK_SIZES[i-1];
        }
    }

    public CommsHandler() { // for unit test only
        unitTest = true;
        sharedArray = new int[GameConstants.SHARED_ARRAY_LENGTH];
        for (int j = 0; j < sharedArray.length; j++) {
            sharedArray[j] = 0;
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
        return readChunkPortion(archonNum, 0, 4);
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
        return writeChunkPortion(status, archonNum, 0, 4);
    }

    /**
     * Returns the MapLocation of the specified friendly archon.
     *
     * @param archonNum the archon number
     * @return the MapLocation of the specified archon
     * @throws GameActionException
     */
    public MapLocation readOurArchonLocation(int archonNum) throws GameActionException {
        return new MapLocation(readChunkPortion(archonNum, 4, 6), readChunkPortion(archonNum, 10, 6));
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
        return writeChunkPortion(loc.x, archonNum, 4, 6) && writeChunkPortion(loc.y, archonNum, 10, 6);
    }

    /**
     * Returns the symmetry of the map, encoded as follows:
     * 0: unknown; 1: vertical; 2: horizontal; 3: diagonal.
     *
     * @return the symmetry of the map
     * @throws GameActionException
     */
    public int readSymmetry() throws GameActionException {
        return readChunk(8);
    }

    /**
     * Writes the symmetry of the map, encoded as follows:
     * 0: unknown; 1: vertical; 2: horizontal; 3: diagonal.
     *
     * @param symmetry the symmetry to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeSymmetry(int symmetry) throws GameActionException {
        return writeChunk(symmetry, 8);
    }

    /**
     * Returns the cluster control status of the specified cluster, encoded as follows:
     * 0: unknown; 1: we control; 2: enemy controls; 3: ??.
     *
     * @param clusterNum the cluster number
     * @return the cluster control status of the specified cluster
     * @throws GameActionException
     */
    public int readClusterControlStatus(int clusterIdx) throws GameActionException {
        return readChunkPortion(9 + clusterIdx, 0, 2);
    }

    /**
     * Writes the cluster control status of the specified cluster, encoded as follows:
     * 0: unknown; 1: we control; 2: enemy controls; 3: ??.
     *
     * @param clusterNum the cluster number
     * @param status the cluster control status to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeClusterControlStatus(int clusterIdx, int status) throws GameActionException {
        return writeChunkPortion(status, 9 + clusterIdx, 0, 2);
    }

    /**
     * Returns the reinforcement status of the specified cluster, encoded as follows:
     * 0: no current reinforcement; 1: eco mission on the way; 2: sending army; 3: heavily sending army.
     *
     * @param clusterNum the cluster number
     * @return the reinforcement status of the specified cluster
     * @throws GameActionException
     */
    public int readClusterReinforcementStatus(int clusterIdx) throws GameActionException {
        return readChunkPortion(9 + clusterIdx, 2, 2);
    }

    /**
     * Returns the number of resources in the specified cluster, encoded in the range [1, 15].
     * Returns 0 if the resource count is unknown.
     *
     * @param clusterNum the cluster number
     * @return the encoded number of resources in the specified cluster
     * @throws GameActionException
     */
    public int readClusterResourceCount(int clusterIdx) throws GameActionException {
        return readChunkPortion(9 + clusterIdx, 4, 4);
    }

    /**
     * Writes the number of resources in the specified cluster, encoded in the range [1, 15].
     * Returns 0 if the resource count is unknown.
     * 
     * @param clusterNum the cluster number
     * @param count the encoded number of resources to write
     * @return true if the write was successful
     * @throws GameActionException
     */
    public boolean writeClusterResourceCount(int clusterIdx, int count) throws GameActionException {
        return writeChunkPortion(count, 9 + clusterIdx, 4, 4);
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
        } else {
            return rc.readSharedArray(index);
        }
    }

    // TODO: after unit tests pass, remove this and replace all writeSharedArray with rc.writeSharedArray to save bytecode
    private void writeSharedArray(int index, int value) throws GameActionException {
        if (unitTest) {
            sharedArray[index] = value;
        } else {
            rc.writeSharedArray(index, value);
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
    public boolean write(int value, int startBit, int numBits) throws GameActionException {
        int arrIndexStart = (startBit)>>>SHARED_ARRAY_ELEM_LOG2;
        int arrIndexEnd = (numBits+startBit-1)>>>SHARED_ARRAY_ELEM_LOG2;
        int integerBitBegin = whichBit(arrIndexStart, startBit);
        int integerBitEnd = whichBit(arrIndexEnd, numBits+startBit-1);
        //if write is contained in single integer
        if(arrIndexStart == arrIndexEnd) {
            int bitm = bitmask(integerBitBegin, integerBitEnd, false);
            value = value << (SHARED_ARRAY_ELEM_SIZE-integerBitBegin-numBits);
            // read value from shared array at arrIndexStart
            int entry = readSharedArray(arrIndexStart);
            writeSharedArray(arrIndexStart, (entry & bitm) | value);
        } else {
            //if write spans two integers
            int bitm1 = bitmask(integerBitBegin, SHARED_ARRAY_ELEM_SIZE-1, false);
            int bitm2 = bitmask(0, integerBitEnd, false);

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
        int arrIndexEnd = (numBits+beginBit-1)>>>SHARED_ARRAY_ELEM_LOG2;
        int integerBitBegin = whichBit(arrIndexStart, beginBit);
        int integerBitEnd = whichBit(arrIndexEnd, numBits+beginBit-1);
        int output = 0;
        //if read is contained in a single integer
        if(arrIndexStart==arrIndexEnd) {
            int bitm = bitmask(integerBitBegin, integerBitEnd, true);
            output = (readSharedArray(arrIndexStart) & bitm) >>> (SHARED_ARRAY_ELEM_SIZE - integerBitBegin - numBits);
        } else {
                //if the read spans two integers
                int bitm = bitmask(integerBitBegin, SHARED_ARRAY_ELEM_SIZE-1, true);
                int bitm2 = bitmask(0, integerBitEnd, true);
                output = (readSharedArray(arrIndexStart) & bitm) >>> (SHARED_ARRAY_ELEM_SIZE - integerBitBegin - numBits + integerBitEnd + 1);
                output = output << integerBitEnd + 1;
                output |= (readSharedArray(arrIndexEnd) & bitm2) >>> (SHARED_ARRAY_ELEM_SIZE - numBits + SHARED_ARRAY_ELEM_SIZE - integerBitBegin);
        }
        return output;
    }

    // TODO (Nikhil): replace with giant switch statement
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

    // TODO (Nikhil): replace with giant switch statement
    private int whichBit(int index, int bitloc) {
        if(bitloc < index * SHARED_ARRAY_ELEM_SIZE) {
            return 0;    //first bit of the number
        }
        if(bitloc >= (index+1)*SHARED_ARRAY_ELEM_SIZE) {
            return SHARED_ARRAY_ELEM_SIZE-1;  //last bit of the number
        }
        return bitloc % SHARED_ARRAY_ELEM_SIZE;
    }

}