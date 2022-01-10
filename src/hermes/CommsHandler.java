package hermes;

import battlecode.common.*;


public class CommsHandler {
    
    RobotController rc;

    // Max chunk size: 16 bits
    // TODO (Nikhil): support up to 32 bit chunks (requires modifying low-level functions to cover split over 3 case)
    // Chunk schema:
    int OUR_ARCHON_BITS = 16; // 4 bits: status; 6 bits: x coordinate; 6 bits: y coordinate
    int ENEMY_ARCHON_BITS = 13;
    int MAP_SYMMETRY_BITS = 12; // 1 bit: horizontal symmetry; 1 bit: vertical symmetry
    int CLUSTER_BITS = 5; // 2 bits: cluster control status; 3 bits: resource count.
    int ARCHON_INSTRUCTION_BITS = 16;
    int PRIORITY_CLUSTER_BITS = 16;
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
        PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, // up to 20 priority clusters
        PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, 
        PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, 
        PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, PRIORITY_CLUSTER_BITS, 
        // TODO: add more
    };
    int[] CHUNK_OFFSETS = new int[CHUNK_SIZES.length]; // TODO: precompute prefix sums of CHUNK_SIZES

    final int SHARED_ARRAY_ELEM_SIZE = 16;
    final int SHARED_ARRAY_ELEM_LOG2 = 4;
    final int MAX_SHARED_ARRAY_ELEM = 65535;

    // for unit test only
    boolean unitTest = false;
    int[] sharedArray;

    public CommsHandler(RobotController rc) {
        this.rc = rc;
        for (int i = 0; i < CHUNK_SIZES.length; i++) { // TODO: remove once we precompute CHUNK_OFFSETS
            CHUNK_OFFSETS[i] = (i == 0) ? 0 : CHUNK_OFFSETS[i-1] + CHUNK_SIZES[i-1];
        }
        // System.out.println("Total bits used: " + (CHUNK_OFFSETS[CHUNK_OFFSETS.length-1] + CHUNK_SIZES[CHUNK_SIZES.length-1]));
    }

    public CommsHandler() { // for unit test only
        unitTest = true;
        sharedArray = new int[GameConstants.SHARED_ARRAY_LENGTH];
        for (int j = 0; j < sharedArray.length; j++) {
            sharedArray[j] = 0;
        }
        for (int i = 0; i < CHUNK_SIZES.length; i++) { // TODO: remove once we precompute CHUNK_OFFSETS
            CHUNK_OFFSETS[i] = (i == 0) ? 0 : CHUNK_OFFSETS[i-1] + CHUNK_SIZES[i-1];
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
     * Returns the number of resources in the specified cluster, encoded in the range [1, 7].
     * Returns 0 if the resource count is unknown.
     *
     * @param clusterNum the cluster number
     * @return the encoded number of resources in the specified cluster
     * @throws GameActionException
     */
    public int readClusterResourceCount(int clusterIdx) throws GameActionException {
        return readChunkPortion(9 + clusterIdx, 2, 3);
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
        return writeChunkPortion(count, 9 + clusterIdx, 2, 3);
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
        // System.out.println("Writing " + (value & MAX_SHARED_ARRAY_ELEM) + " to shared array at index " + index);
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
    public boolean write(int value, int startBit, int numBits) throws GameActionException {
        int arrIndexStart = (startBit)>>>SHARED_ARRAY_ELEM_LOG2;
        int arrIndexEnd = (numBits+startBit-1)>>>SHARED_ARRAY_ELEM_LOG2;
        int integerBitBegin = whichBit(arrIndexStart, startBit);
        int integerBitEnd = whichBit(arrIndexEnd, numBits+startBit-1);
        //if write is contained in single integer
        if(arrIndexStart == arrIndexEnd) {
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
        int arrIndexEnd = (numBits+beginBit-1)>>>SHARED_ARRAY_ELEM_LOG2;
        int integerBitBegin = whichBit(arrIndexStart, beginBit);
        int integerBitEnd = whichBit(arrIndexEnd, numBits+beginBit-1);
        int output = 0;
        //if read is contained in a single integer
        if(arrIndexStart==arrIndexEnd) {
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

}