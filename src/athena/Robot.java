package athena;

import battlecode.common.*;

import java.util.Random;

import java.util.ArrayList;

public class Robot {

    RobotController rc;
    int turnCount;
    int currentRound;
    int myID;
    Team allyTeam;
    Team enemyTeam;
    MapLocation myLocation;
    int mapHeight;
    int mapWidth;
    int numOurArchons;
    boolean onOurSide;
    MapLocation ourArchonCentroid;
    MapLocation startingArchonCentroid = null;

    // Cache sensing
    RobotInfo[] nearbyEnemies;

    int[][] shifts = {{0, 3}, {2, 2}, {3, 0}, {2, -2}, {0, -3}, {-2, -2}, {-3, 0}, {-2, 2}};

    // Pathing
    MapLocation baseLocation;
    boolean exploreMode;
    ArrayList<MapLocation> priorDestinations;
    Pathing pathing;

    // Clusters are 6x6, 5x6, 6x5, or 5x5
    int numClusters;
    int[] clusterHeights;
    int[] clusterWidths;
    int clusterWidthsLength;
    float xStep;
    float yStep;
    int[] whichXLoc;
    int[] whichYLoc;
    int[] clusterCentersX;
    int[] clusterCentersY;
    int[] clusterResources;
    int[] clusterControls;
    int[] markedClustersBuffer;
    int[] clusterPermutation;

    boolean isDying;
    final int FLEE_HEALTH = 8;
    final int SAGE_FLEE_HEALTH = 10;
    final double GAMMA = 0.9;

    CommsHandler commsHandler;

    final int LEAD_RESOLUTION = 20; 

    boolean archonZeroAlive = true;
    boolean archonOneAlive = true;
    boolean archonTwoAlive = true;
    boolean archonThreeAlive = true;
    MapLocation archonZeroLocation = null;
    MapLocation archonOneLocation = null;
    MapLocation archonTwoLocation = null;
    MapLocation archonThreeLocation = null;

    int numOurArchonsAlive = 0;

    /** Array containing all the possible movement directions. */
    final Direction[] directionsWithoutCenter = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST
    };

    final Direction[] directionsWithCenter = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
        Direction.CENTER
    };

    final int LEAD_RESERVE_SCALE = 5;
    final int GOLD_RESERVE_SCALE = 5;

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    final Random rng = new Random(6147);

    public Robot(RobotController robotController) throws GameActionException {
        rc = robotController;
        currentRound = rc.getRoundNum();
        turnCount = 0;
        allyTeam = rc.getTeam();
        enemyTeam = allyTeam.opponent();
        myID = rc.getID();
        myLocation = rc.getLocation();
        mapHeight = rc.getMapHeight();
        mapWidth = rc.getMapWidth();
        setupClusters();
        precomputeClusterCenters();
        clusterResources = new int[numClusters];
        clusterControls = new int[numClusters];
        markedClustersBuffer = new int[numClusters];
        exploreMode = false;
        priorDestinations = new ArrayList<MapLocation>();
        commsHandler = new CommsHandler(rc);
        numOurArchons = rc.getArchonCount();
        isDying = false;
        pathing = new Pathing(this);
        pathing.destination = null;

        // Precompute math for whichCluster
        whichXLoc = new int[mapWidth];
        whichYLoc = new int[mapHeight];
        for (int i = 0; i < mapWidth; i++) {
            whichXLoc[i] = (int) (i / xStep);
        }
        for (int i = 0; i < mapHeight; i++) {
            whichYLoc[i] = (int) (i / yStep) * clusterWidths.length;
        }

        // Buildings are their own base
        if (rc.getType() == RobotType.LABORATORY || rc.getType() == RobotType.WATCHTOWER || rc.getType() == RobotType.ARCHON) {
            baseLocation = myLocation;
        }
        // Units treat archon as base location
        else {
            RobotInfo[] adjacentRobots = rc.senseNearbyRobots(2, allyTeam);
            for (RobotInfo robot : adjacentRobots) {
                if (robot.type == RobotType.ARCHON) {
                    baseLocation = robot.location;
                    break;
                }
            }
        }
    }

    public void run() throws GameActionException {
        // Before unit runs
        turnCount++;
        currentRound = rc.getRoundNum();
        // Don't run setup for labs on turn 1, they TLE
        if (rc.getType() == RobotType.LABORATORY && turnCount == 1) {
            return;
        }

        // Rest of setup
        nearbyEnemies = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, enemyTeam);
        setClusterStates();
        archonStatusCheck();
        if (startingArchonCentroid == null && currentRound >= 2) {
            startingArchonCentroid = new MapLocation(commsHandler.readStartingArchonCentroidXCoord(), commsHandler.readStartingArchonCentroidYCoord());
        }
        onOurSide = onOurSide(myLocation);
        // Flee to archon if dying
        int myHealth = rc.getHealth();
        if (myHealth == rc.getType().getMaxHealth(rc.getLevel())) {
            isDying = false;
        }
        else if (myHealth <= FLEE_HEALTH || (rc.getType() == RobotType.SAGE && myHealth <= SAGE_FLEE_HEALTH)) {
            isDying = true;
        }

        // Does turn
        runUnit();
        
        // rc.setIndicatorString("SymDist " + distanceToSymmetryLine);

        // After unit runs
        if (rc.getRoundNum() < 150 && Clock.getBytecodesLeft() > 1000) {
            // System.out.println("Bytecodes left before symmetry: " + Clock.getBytecodesLeft());
            updateSymmetry();
            // System.out.println("Bytecodes left after symmetry: " + Clock.getBytecodesLeft());
        }

        checkTLE();
    }

    /**
     * Function to be overridden by the unit classes. This is where
     * all unit-specific run stuff happens.
     */
    public void runUnit() throws GameActionException {
    }

    public void checkTLE() throws GameActionException {
        if (rc.getRoundNum() > currentRound) {
            System.out.println("I TLE'd");
            currentRound = rc.getRoundNum();
            announceAlive();
        }
    }

    // To be overridden by children
    public void announceAlive() throws GameActionException {
    }

    /**
     * Updates symmetry information. If I can see one of the midlines, check some rubble points to see
     * if I can eliminate a symmetry axis.
     */
    public void updateSymmetry() throws GameActionException {
        int currentSymmetry = commsHandler.readMapSymmetry();
        
        // if I can see the vertical midline
        if ((currentSymmetry & CommsHandler.MapSymmetry.HORIZONTAL) == 0) { // if we have not yet ruled out vertical symmetry
            if (myLocation.x * 2 < mapWidth + 5 && myLocation.x * 2 > mapWidth - 5) { // if we are near the vertical midline
                // check some rubble points to see if we can eliminate the symmetry axis
                MapLocation test1 = new MapLocation(mapWidth/2 - 1, myLocation.y);
                MapLocation test2 = new MapLocation(mapWidth - mapWidth/2, myLocation.y);
                rc.setIndicatorDot(test1, 0, 255, 0);
                rc.setIndicatorDot(test2, 0, 255, 0);
                if (rc.canSenseLocation(test1)) {
                    if (rc.canSenseLocation(test2)) {
                        if (rc.senseRubble(test1) != rc.senseRubble(test2)) {
                            commsHandler.writeMapSymmetry(currentSymmetry | CommsHandler.MapSymmetry.HORIZONTAL); // eliminate vertical symmetry
                        }
                    }
                }
            }
        }

        // same for horizontal
        if ((currentSymmetry & CommsHandler.MapSymmetry.VERTICAL) == 0) { // if we have not yet ruled out horizontal symmetry
            if (myLocation.y * 2 < mapHeight + 5 && myLocation.y * 2 > mapHeight - 5) {
                MapLocation test1 = new MapLocation(myLocation.x, mapHeight/2 - 1);
                MapLocation test2 = new MapLocation(myLocation.x, mapHeight - mapHeight/2);
                rc.setIndicatorDot(test1, 0, 255, 0);
                rc.setIndicatorDot(test2, 0, 255, 0);
                if (rc.canSenseLocation(test1)) {
                    if (rc.canSenseLocation(test2)) {
                        if (rc.senseRubble(test1) != rc.senseRubble(test2)) {
                            commsHandler.writeMapSymmetry(currentSymmetry | CommsHandler.MapSymmetry.VERTICAL); // eliminate horizontal symmetry
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates cluster information. Scans nearby tiles, enemy locations, and nearby resources
     * and aggregates into clusterControls and clusterResoruces as buffers. Uses 
     * markedClustersBuffer to track which buffers have been modified each turn to reset them.
     * Alternates whether control or resources are scanned each turn to conserve bytecode.
     * 
     * Note: This is not set up until turn 2 to save compute on initialization.
     * @throws GameActionException
     */
    public void setClusterStates() throws GameActionException {
        // int bytecodeUsed = Clock.getBytecodeNum();

        // Turrets only run on turns 2 and 3, labs dont run
        if ((rc.getMode() == RobotMode.TURRET && turnCount > 2)
            || rc.getType() == RobotType.LABORATORY) {
            return;
        }
        
        if (turnCount % 2 == 1) {
            setClusterControlStates();
        }
        else {
            setClusterResourceStates();
        }

        // int bytecodeUsed2 = Clock.getBytecodeNum();
        // rc.setIndicatorString("Cluster States: "+(bytecodeUsed2 - bytecodeUsed));
    }

    /**
     * Updates cluster information. Scans nearby tiles and enemy locations and aggregates into 
     * clusterControls as a buffer. Uses markedClustersBuffer to track which buffers have been
     * modified each turn to write them. In clusterControls, we set the 3rd and 4th bits to be
     * the new value and only read/write if it is different from the previous state.
     * 
     * @throws GameActionException
     */
    public void setClusterControlStates() throws GameActionException {
        int markedClustersCount = 0;

        // Mark nearby clusters as explored
        for (int[] shift : shifts) {
            MapLocation shiftedLocation = myLocation.translate(shift[0], shift[1]);
            if (rc.canSenseLocation(shiftedLocation)) {
                // int clusterIdx = whichCluster(shiftedLocation); Note: Inlined to save bytecode
                int clusterIdx = whichXLoc[shiftedLocation.x] + whichYLoc[shiftedLocation.y];
                MapLocation clusterCenter = new MapLocation(clusterCentersX[clusterIdx % clusterWidthsLength], 
                                                clusterCentersY[clusterIdx / clusterWidthsLength]);
                // Write new status to buffer if we haven't yet
                if (rc.canSenseLocation(clusterCenter) && clusterControls[clusterIdx] < 8) {
                    clusterControls[clusterIdx] += 8;
                    markedClustersBuffer[markedClustersCount] = clusterIdx;
                    markedClustersCount++;
                }
            }
        }

        // Mark nearby clusters with enemies as hostile
        // Process at max 10 enemies
        int numEnemies = Math.min(nearbyEnemies.length, 10);
        for (int i = 0; i < numEnemies; i++) {
            RobotInfo enemy = nearbyEnemies[i];
            // int clusterIdx = whichCluster(enemy.location); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[enemy.location.x] + whichYLoc[enemy.location.y];
            // Write new status to buffer if we haven't marked as enemy controlled yet
            if ((clusterControls[clusterIdx] & 32) == 0) {
                // Only add to modified list if we haven't marked this cluster yet
                if (clusterControls[clusterIdx] < 8) {
                    markedClustersBuffer[markedClustersCount] = clusterIdx;
                    markedClustersCount++;
                }
                clusterControls[clusterIdx] = 32 + (clusterControls[clusterIdx] & 7); // 010xxx
            }
        }

        // Flush control buffer and write to comms
        for (int i = 0; i < markedClustersCount; i++) {
            int clusterIdx = markedClustersBuffer[i];
            int oldClusterStatus = clusterControls[clusterIdx] & 7;
            int newClusterStatus = (clusterControls[clusterIdx] - oldClusterStatus) >>> 3;
            if (newClusterStatus != commsHandler.readClusterControlStatus(clusterIdx)) {
                commsHandler.writeClusterControlStatus(clusterIdx, newClusterStatus);
            }
            clusterControls[clusterIdx] = newClusterStatus;
        }
    }

    /**
     * Updates cluster information. Scans nearby resources and aggregates into clusterResoruces as
     * a buffer. Uses markedClustersBuffer to track which buffers have been modified each turn to
     * reset them. In clusterResoruces, we set bits 16-30 to be the old value and only 
     * read/write if it is different from the previous state.
     * 
     * @throws GameActionException
     */
    public void setClusterResourceStates() throws GameActionException {
        // Reset buffer
        int markedClustersCount = 0;

        // Scan nearby resources and aggregate counts. Require at least 2 lead since 1 lead regenerates
        MapLocation[] leadTiles = rc.senseNearbyLocationsWithLead(rc.getType().visionRadiusSquared, 2);
        int leadTilesLength = leadTiles.length;
        MapLocation[] goldTiles;
        int goldTilesLength;
        if (rc.getType() == RobotType.BUILDER) {
            leadTilesLength = Math.min(leadTilesLength, 5);
            goldTiles = new MapLocation[0];
            goldTilesLength = 0;
        }
        else {
            goldTiles = rc.senseNearbyLocationsWithGold(rc.getType().visionRadiusSquared);
            goldTilesLength = goldTiles.length;
        }
        for (int i = 0; i < leadTilesLength; i++) {
            MapLocation tile = leadTiles[i];
            // int clusterIdx = whichCluster(tile); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[tile.x] + whichYLoc[tile.y];
            // Only add to modified list if we haven't marked this cluster yet
            if ((clusterResources[clusterIdx] & 32767) == 0) {
                markedClustersBuffer[markedClustersCount] = clusterIdx;
                markedClustersCount++;
            }
            clusterResources[clusterIdx] += rc.senseLead(tile) - 1;
        }
        for (int i = 0; i < goldTilesLength; i++) {
            MapLocation tile = goldTiles[i];
            // int clusterIdx = whichCluster(tile); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[tile.x] + whichYLoc[tile.y];
            // Only add to modified list if we haven't marked this cluster yet
            if ((clusterResources[clusterIdx] & 32767) == 0) {
                markedClustersBuffer[markedClustersCount] = clusterIdx;
                markedClustersCount++;
            }
            clusterResources[clusterIdx] += rc.senseGold(tile);
        }

        if (rc.getType() != RobotType.BUILDER) {
            // Add surrounding clusters to buffer. This ensures we clear out clusters where all
            // resources have been mined
            for (int[] shift : shifts) {
                MapLocation shiftedLocation = myLocation.translate(shift[0], shift[1]);
                if (rc.canSenseLocation(shiftedLocation)) {
                    // int clusterIdx = whichCluster(shiftedLocation); Note: Inlined to save bytecode
                    int clusterIdx = whichXLoc[shiftedLocation.x] + whichYLoc[shiftedLocation.y];
                    MapLocation clusterCenter = new MapLocation(clusterCentersX[clusterIdx % clusterWidthsLength], 
                                                    clusterCentersY[clusterIdx / clusterWidthsLength]);
                    if (rc.canSenseLocation(clusterCenter) && (clusterResources[clusterIdx] & 32767) == 0) {
                        markedClustersBuffer[markedClustersCount] = clusterIdx;
                        markedClustersCount++;
                    }
                }
            }
        }
        
        // Flush resource buffer and write to comms
        for (int i = 0; i < markedClustersCount; i++) {
            int clusterIdx = markedClustersBuffer[i];
            int rawResourceCount = clusterResources[clusterIdx] & 32767;
            int newResourceCount = compressResourceCount(rawResourceCount);
            // int oldResourceCount = (clusterResources[clusterIdx] - rawResourceCount) >>> 15;
            if (newResourceCount != commsHandler.readClusterResourceCount(clusterIdx)) {
                commsHandler.writeClusterResourceCount(clusterIdx, newResourceCount);
            }
            clusterResources[clusterIdx] = newResourceCount << 15;
        }
    }

    /**
     * Max value of 7 as we only have 3 bits
     * @param resourceCount
     */
    public int compressResourceCount(int resourceCount) {
        return Math.min((resourceCount + LEAD_RESOLUTION - 1) / LEAD_RESOLUTION, 7);
    }

    /**
     * Retreat back into Archon repair range if we're dying
     * @return
     * @throws GameActionException
     */
    public boolean baseRetreat() throws GameActionException {
        if (isDying) {
            // get nearest archon location
            MapLocation nearestArchonLocation = null;
            int nearestArchonDistance = Integer.MAX_VALUE;
            if (archonZeroAlive) {
                int dist = myLocation.distanceSquaredTo(archonZeroLocation);
                if (dist < nearestArchonDistance 
                    && (dist < RobotType.ARCHON.actionRadiusSquared 
                        || commsHandler.readOurArchonAcceptingPatients(0) == CommsHandler.ArchonStatus.ACCEPTING_PATIENTS)) {
                    nearestArchonLocation = archonZeroLocation;
                    nearestArchonDistance = dist;
                }
            }
            if (archonOneAlive) {
                int dist = myLocation.distanceSquaredTo(archonOneLocation);
                if (dist < nearestArchonDistance 
                    && (dist < RobotType.ARCHON.actionRadiusSquared 
                        || commsHandler.readOurArchonAcceptingPatients(1) == CommsHandler.ArchonStatus.ACCEPTING_PATIENTS)) {
                    nearestArchonLocation = archonOneLocation;
                    nearestArchonDistance = dist;
                }
            }
            if (archonTwoAlive) {
                int dist = myLocation.distanceSquaredTo(archonTwoLocation);
                if (dist < nearestArchonDistance 
                    && (dist < RobotType.ARCHON.actionRadiusSquared 
                        || commsHandler.readOurArchonAcceptingPatients(2) == CommsHandler.ArchonStatus.ACCEPTING_PATIENTS)) {
                    nearestArchonLocation = archonTwoLocation;
                    nearestArchonDistance = dist;
                }
            }
            if (archonThreeAlive) {
                int dist = myLocation.distanceSquaredTo(archonThreeLocation);
                if (dist < nearestArchonDistance 
                    && (dist < RobotType.ARCHON.actionRadiusSquared 
                        || commsHandler.readOurArchonAcceptingPatients(3) == CommsHandler.ArchonStatus.ACCEPTING_PATIENTS)) {
                    nearestArchonLocation = archonThreeLocation;
                    nearestArchonDistance = dist;
                }
            }
            // rc.setIndicatorString(nearestArchonLocation + " " + commsHandler.readOurArchonAcceptingPatients(0)
            //     + " " + commsHandler.readOurArchonAcceptingPatients(1) + " " + commsHandler.readOurArchonAcceptingPatients(2)
            //     + " " + commsHandler.readOurArchonAcceptingPatients(3));
            // MapLocation nearestArchonLocation = baseLocation;
            if (nearestArchonLocation != null) {
                if (myLocation.distanceSquaredTo(nearestArchonLocation) > RobotType.ARCHON.actionRadiusSquared) {
                    baseLocation = nearestArchonLocation;
                    pathing.updateDestination(nearestArchonLocation);
                    pathing.pathToDestination();
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Update destination to encourage exploration if destination is off map or destination is not
     * an enemy target. Uses rejection sampling to avoid destinations near already explored areas.
     * @throws GameActionException.
     */
    public void updateDestinationForExploration() throws GameActionException {
        MapLocation nearDestination = myLocation;
        if (pathing.destination != null) {
            for (int i = 0; i < 3; i++) {
                nearDestination = nearDestination.add(nearDestination.directionTo(pathing.destination));
            }
        }
        // Reroute if 1) nearDestination not on map or 2) can sense destination and it's not on the map
        // or it's not occupied (so no Archon)
        if (pathing.destination == null || !rc.onTheMap(nearDestination) ||
            (rc.canSenseLocation(pathing.destination)
            && (!rc.onTheMap(pathing.destination)
                || !rc.isLocationOccupied(pathing.destination)
                || rc.senseRobotAtLocation(pathing.destination).team == allyTeam))) {
            // Rerouting
            if (pathing.destination != null) {
                priorDestinations.add(pathing.destination);
            }
            boolean valid = true;
            int dxexplore = (int)(Math.random()*80);
            int dyexplore = 120 - dxexplore;
            dxexplore = Math.random() < .5 ? dxexplore : -dxexplore;
            dyexplore = Math.random() < .5 ? dyexplore : -dyexplore;
            MapLocation newDestination = new MapLocation(baseLocation.x + dxexplore, baseLocation.y + dyexplore);
            for (int i = 0; i < priorDestinations.size(); i++) {
                if (newDestination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                    valid = false;
                    break;
                }
            }
            while (!valid) {
                valid = true;
                newDestination = new MapLocation(baseLocation.x + (int)(Math.random()*80 - 40),
                                                baseLocation.y + (int)(Math.random()*80 - 40));
                for (int i = 0; i < priorDestinations.size(); i++) {
                    if (newDestination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                        valid = false;
                        break;
                    }
                }
            }
            pathing.updateDestination(newDestination);
        }
    }

    /**
     * Checks if we are on our side of the map (based on symmetry).
     * 
     * @param loc
     * @return whether we are on our side of the map
     * @throws GameActionException
     */
    public boolean onOurSide(MapLocation loc) throws GameActionException {
        if (currentRound == 1) return true;
        if (numOurArchonsAlive == 0) {
            System.out.println("No archons alive, we lost :(");
            return false;
        }
        int symmetry = commsHandler.readMapSymmetry();

        if (symmetry == CommsHandler.MapSymmetry.UNKNOWN || symmetry == CommsHandler.MapSymmetry.ROTATIONAL) {
            double orthoVecX = startingArchonCentroid.x - ((mapWidth - 1) / 2.0);
            double orthoVecY = startingArchonCentroid.y - ((mapHeight - 1) / 2.0);
            double orthoLen = Math.sqrt(orthoVecX*orthoVecX + orthoVecY*orthoVecY);
            orthoVecX /= orthoLen;
            orthoVecY /= orthoLen;
            double myVecX = loc.x - ((mapWidth - 1) / 2.0);
            double myVecY = loc.y - ((mapHeight - 1) / 2.0);
            double dotProd = myVecX * orthoVecX + myVecY * orthoVecY;
            double distToSym = Math.sqrt(myVecX*myVecX + myVecY*myVecY - dotProd);
            double currArchonVecX = ourArchonCentroid.x - ((mapWidth - 1) / 2.0);
            double currArchonVecY = ourArchonCentroid.y - ((mapHeight - 1) / 2.0);
            double currArchonDotProd = currArchonVecX * orthoVecX + currArchonVecY * orthoVecY;
            double currentArchonDistToSym = Math.sqrt(myVecX*myVecX + myVecY*myVecY - currArchonDotProd);
            return dotProd > 0 || currentArchonDistToSym > distToSym;
        } else if (symmetry == CommsHandler.MapSymmetry.HORIZONTAL) {
            if (startingArchonCentroid.y <= ((mapHeight - 1) / 2.0)) {
                return loc.y <= ((mapHeight - 1) / 2.0) || loc.y <= ourArchonCentroid.y;
            } else {
                return loc.y > ((mapHeight - 1) / 2.0) || loc.y >= ourArchonCentroid.y;
            }
        } else if (symmetry == CommsHandler.MapSymmetry.VERTICAL) {
            if (startingArchonCentroid.x <= ((mapWidth - 1) / 2.0)) {
                return loc.x <= ((mapWidth - 1) / 2.0) || loc.x <= ourArchonCentroid.x;
            } else {
                return loc.x > ((mapWidth - 1) / 2.0) || loc.x >= ourArchonCentroid.x;
            }
        } else {
            System.out.println("Unknown map symmetry: " + symmetry);
            throw new IllegalStateException("Unknown symmetry");
        }
    }

    /**
     * Sorts through nearby units to attack. Prioritize killing >> prototypes close to finishing
     * >> combat units >> weakest unit
     * @throws GameActionException
     */
    public void attack() throws GameActionException {
        if (!rc.isActionReady()) {
            return;
        }
        int actionRadius = rc.getType().actionRadiusSquared;
        int damage = rc.getType().getDamage(rc.getLevel());
        // Find attack maximizing score
        MapLocation optimalAttack = null;
        int optimalScore = -1;
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(actionRadius, enemyTeam);
        boolean isTargetArchon = false;
        int kills = 0;
        int damageDealt = 0;
        for (RobotInfo enemy : nearbyEnemies) {
            boolean isEnemyArchon = enemy.type == RobotType.ARCHON;
            int score = 0;
            // Tiebreakers across all priority classes
            // Prioritize archons after opening
            if (currentRound > 500 && enemy.type == RobotType.ARCHON) {
                score += 20000;
            }
            // Prioritize combat units
            switch (enemy.type) {
                case SAGE:
                    score += 19000;
                    break;
                case WATCHTOWER:
                    score += 18000;
                    break;
                case SOLDIER:
                    score += 17000;
                    break;
            }

            // 1) Always prioritize kills, prioritizing highest health unit
            if (enemy.health <= damage) {
                score += 1000000 + enemy.health;
            }
            // 2) Target prototype units closest to being finished
            else if (enemy.getMode() == RobotMode.PROTOTYPE && enemy.getType().getMaxHealth(1) - enemy.health <= 10) {
                // We want to prioritize hitting buildings closest to being finished so they're not completed
                score += 100000 + enemy.getType().getMaxHealth(1) - enemy.health;
            }
            // 3) Target weakest unit
            else {
                score += 1000 - enemy.health;
            }
            if (score > optimalScore) {
                optimalAttack = enemy.location;
                optimalScore = score;
                isTargetArchon = isEnemyArchon;
                kills = enemy.health <= damage ? 1 : 0;
                damageDealt = kills == 1 ? enemy.health : damage;
            }
        }
        if (optimalAttack != null) {
            // If sage, consider anomalies
            if (rc.getType() == RobotType.SAGE) {
                // Use fury on archons
                if (isTargetArchon && rc.canEnvision(AnomalyType.FURY)) {
                    int friendlyKills = 0;
                    int friendlyDamage = 0;
                    RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.SAGE.actionRadiusSquared, allyTeam);
                    int nearbyAlliesLength = Math.min(nearbyAllies.length, 15);
                    for (int i = 0; i < nearbyAlliesLength; i++) {
                        RobotInfo ally = nearbyAllies[i];
                        int allyChargeDamage = ally.type.getMaxHealth(ally.level) * 22 / 100;
                        if (ally.type == RobotType.ARCHON && ally.mode == RobotMode.TURRET) {
                            if (allyChargeDamage >= ally.health) {
                                friendlyKills++;
                                friendlyDamage += ally.health;
                            }
                            else {
                                friendlyDamage += allyChargeDamage;
                            }
                        }
                    }
                    // Require no friendly kills
                    if (friendlyKills == 0) {
                        int enemyKills = 0;
                        int enemyDamage = 0;
                        RobotInfo[] attackEnemies = rc.senseNearbyRobots(RobotType.SAGE.actionRadiusSquared, enemyTeam);
                        int attackEnemiesLength = Math.min(attackEnemies.length, 15);
                        for (int i = 0; i < attackEnemiesLength; i++) {
                            RobotInfo enemy = attackEnemies[i];
                            if (enemy.type == RobotType.ARCHON && enemy.mode == RobotMode.TURRET) {
                                int enemyChargeDamage = enemy.type.getMaxHealth(enemy.level) * 22 / 100;
                                if (enemyChargeDamage >= enemy.health) {
                                    enemyKills++;
                                    enemyDamage += enemy.health;
                                }
                                else {
                                    enemyDamage += enemyChargeDamage;
                                }
                            }
                        }
                        // Require either 2 or more kills or more net damage than a sage attack would do
                        if (enemyKills >= 2 || enemyDamage - friendlyDamage >= RobotType.SAGE.damage) {
                            rc.envision(AnomalyType.FURY);
                        }
                    }
                }
                // Consider charge if it does more kills/damage
                else if (rc.canEnvision(AnomalyType.CHARGE)) {
                    int chargeKills = 0;
                    int chargeDamage = 0;
                    RobotInfo[] attackEnemies = rc.senseNearbyRobots(RobotType.SAGE.actionRadiusSquared, enemyTeam);
                    int nearbyEnemiesLength = Math.min(attackEnemies.length, 15);
                    for (int i = 0; i < nearbyEnemiesLength; i++) {
                        RobotInfo enemy = attackEnemies[i];
                        if (enemy.mode == RobotMode.DROID) {
                            int enemyChargeDamage = enemy.type.getMaxHealth(enemy.level) * 22 / 100;
                            // Charge kills enemy
                            if (enemyChargeDamage >= enemy.health) {
                                chargeKills++;
                                chargeDamage += enemy.health;
                            }
                            else {
                                chargeDamage += enemyChargeDamage;
                            }
                        }
                    }
                    if (chargeKills > kills || (chargeKills == kills && chargeDamage > damageDealt)) {
                        rc.envision(AnomalyType.CHARGE);
                    }
                }
            }
            if (rc.canAttack(optimalAttack)) {
                rc.attack(optimalAttack);
            }
        }
    }

    /**
     * Chases nearest enemy or moves on exploration path
     * @throws GameActionException
     */
    public void combatMove() throws GameActionException {
        // if (!rc.isMovementReady()) {
        //     return;
        // }
        // Flee back to archon to heal
        if (baseRetreat()) {
            exploreMode = false;
        }
        // Combat move. Kites enemy soldiers if harassing, otherwise pushes
        if (nearbyEnemies.length > 0) {
            // Don't try kite moving on turn 1 or we'll TLE
            if (turnCount == 1) {
                return;
            }
            // Combat kite move if you're not dying or if you're baseRetreating but you're inside archon heal
            if (!isDying || (myLocation.distanceSquaredTo(baseLocation) <= RobotType.ARCHON.actionRadiusSquared)) {
                combatKiteMove();
            }
        }
        // Go to enemy or explore if you're not dying
        else if (!isDying) {
            MapLocation newDestination = pathing.destination;
            // Navigate to nearest found enemy
            int nearestCluster = getNearestCombatCluster();
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                resetControlStatus(pathing.destination);
                newDestination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]);
                exploreMode = false;
            }
            // Explore map. Get new cluster if not in explore mode or close to destination. Don't make sages explore
            else if (rc.getType() != RobotType.SAGE && (!exploreMode || myLocation.distanceSquaredTo(pathing.destination) <= 8)) {
                nearestCluster = getNearestExploreCluster();
                if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    newDestination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                    clusterCentersY[nearestCluster / clusterWidthsLength]);
                }
            }
            // Instead, send all sages to cluster in middle. Keep them moving like a pack
            else if (rc.getType() == RobotType.SAGE && (!exploreMode || myLocation.distanceSquaredTo(pathing.destination) <= 8)) {
                newDestination = new MapLocation(mapWidth / 2, mapHeight / 2);
                exploreMode = true;
            }
            
            pathing.updateDestination(newDestination);
            // Fuzzy move on turn 1 to avoid TLE
            if (turnCount == 1) {
                if (pathing.destination != null) {
                    pathing.cautiousGreedyMove(pathing.destination);
                }
            }
            else {
                pathing.pathToDestination();
            }
        }
    }

    /**
     * Move in combat
     * @throws GameActionException
     */
    public void combatKiteMove() throws GameActionException {
        boolean isNotSageOrIsActionReady = rc.getType() != RobotType.SAGE || rc.isActionReady();
        boolean atMaxHealth = rc.getHealth() == rc.getType().getMaxHealth(rc.getLevel());

        double combatAllyHealth = 0.0;
        int allyCount = 0;
        boolean isArchonVisible = false;
        // MapLocation archonLocation = null;
        // double repairPerTurn = 0;
        RobotInfo[] allies = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, allyTeam);
        int alliesLength = Math.min(allies.length, 10);
        for (int i = 0; i < alliesLength; i++) {
            RobotInfo ally = allies[i];
            if (ally.type == RobotType.WATCHTOWER || (ally.type == RobotType.SOLDIER && ally.health > FLEE_HEALTH)) { // && ally.health > FLEE_HEALTH
                combatAllyHealth += ally.health;
                if (myLocation.distanceSquaredTo(ally.location) <= 5) {
                    allyCount++;
                }
            }
            else if (ally.type == RobotType.ARCHON && ally.mode == RobotMode.TURRET) {
                // archonLocation = ally.location;
                // Repair normalized to per turn by rubble
                // repairPerTurn = (2*ally.level) * 10 / (10.0 + rc.senseRubble(archonLocation));
                isArchonVisible = true;
            }
        }
        // boolean isPositionReinforced = rc.getHealth() > 30 && ((archonLocation != null) || (combatAllies - nearbyEnemies.length >= 2));
        boolean isPositionReinforced = isArchonVisible && rc.getHealth() > 10;

        int enemySoldiers = 0;
        for (RobotInfo enemy : nearbyEnemies) {
            if (enemy.type == RobotType.SOLDIER) {
                enemySoldiers++;
            }
        }

        // TODO: Add in archon locations, theyre always communicated

        // Note that allies must be r^2 <= 5 to be counted here
        boolean oneVersusOne = allyCount == 0 && enemySoldiers == 1;
        // System.out.println(myLocation + " " + oneVersusOne + " " + allyCount + " " + enemySoldiers);
        int nearbyEnemiesLength = Math.min(nearbyEnemies.length, 6); // approx 100 bytecode per enemy per dir
        Direction optimalDirection = null;
        double optimalScore = -999.0;
        for (Direction dir : directionsWithCenter) {
            if (rc.canMove(dir) || dir == Direction.CENTER) {
                MapLocation moveLocation = myLocation.add(dir);
                if (!rc.onTheMap(moveLocation)) {
                    continue;
                }
                // Include archon repair benefit, including from archons we know exist that we can't see yet
                double score = 0;
                if (!atMaxHealth) {
                    if (archonZeroAlive && moveLocation.distanceSquaredTo(archonZeroLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                        if (rc.canSenseLocation(archonZeroLocation)) {
                            RobotInfo archonObj = rc.senseRobotAtLocation(archonZeroLocation);
                            if (archonObj != null) {
                                score += (2*archonObj.level) * 10 / (10.0 + rc.senseRubble(archonZeroLocation));
                            }
                            else {
                                score += 2;
                            }
                        } else {
                            score += 2;
                        }
                    }
                    if (archonOneAlive && moveLocation.distanceSquaredTo(archonOneLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                        if (rc.canSenseLocation(archonOneLocation)) {
                            RobotInfo archonObj = rc.senseRobotAtLocation(archonOneLocation);
                            if (archonObj != null) {
                                score += (2*archonObj.level) * 10 / (10.0 + rc.senseRubble(archonOneLocation));
                            }
                            else {
                                score += 2;
                            }
                        } else {
                            score += 2;
                        }
                    }
                    if (archonTwoAlive && moveLocation.distanceSquaredTo(archonTwoLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                        if (rc.canSenseLocation(archonTwoLocation)) {
                            RobotInfo archonObj = rc.senseRobotAtLocation(archonTwoLocation);
                            if (archonObj != null) {
                                score += (2*archonObj.level) * 10 / (10.0 + rc.senseRubble(archonTwoLocation));
                            }
                            else {
                                score += 2;
                            }
                        } else {
                            score += 2;
                        }
                    }
                    if (archonThreeAlive && moveLocation.distanceSquaredTo(archonThreeLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                        if (rc.canSenseLocation(archonThreeLocation)) {
                            RobotInfo archonObj = rc.senseRobotAtLocation(archonThreeLocation);
                            if (archonObj != null) {
                                score += (2*archonObj.level) * 10 / (10.0 + rc.senseRubble(archonThreeLocation));
                            }
                            else {
                                score += 2;
                            }
                        } else {
                            score += 2;
                        }
                    }
                }
                double enemyCombatHealth = 0.0;
                double distToNearestEnemy = 1000000.1;
                boolean canAttack = false;
                boolean canView = false;
                double enemyHeal = 0.0;
                for (int i = 0; i < nearbyEnemiesLength; i++) {
                    RobotInfo enemy = nearbyEnemies[i];
                    // Penalize by their damage per turn times how long I will be there
                    switch (enemy.type) {
                        // Fall through for combat units
                        case WATCHTOWER:
                            if (enemy.mode != RobotMode.TURRET) {
                                break;
                            }
                        case SOLDIER:
                        case SAGE:
                            double enemyRubbleFactor = 10 / (10.0 + (rc.senseRubble(enemy.location)));
                            enemyCombatHealth += enemy.getHealth() * enemyRubbleFactor;
                            double enemyDist = moveLocation.distanceSquaredTo(enemy.location);
                            if (enemyDist < distToNearestEnemy) {
                                distToNearestEnemy = enemyDist;
                            }
                            if (!isPositionReinforced) {
                                // They can hit me, full points off
                                if (enemyDist <= enemy.type.actionRadiusSquared) {
                                    score -= enemy.type.getDamage(enemy.level) * enemyRubbleFactor;
                                    // System.out.println("  hit: " + (-enemy.type.getDamage(enemy.level) * enemyRubbleFactor));
                                }
                                // They can see me. If they step in, I can start shooting but they can too, so normalize by rubble
                                else if (enemyDist <= enemy.type.visionRadiusSquared) {
                                    score -= GAMMA * enemy.type.getDamage(enemy.level) * enemyRubbleFactor;
                                    // System.out.println("  view: " + (-enemy.type.getDamage(enemy.level) * enemyRubbleFactor) + " " + enemy.location);
                                    canView = true;
                                }
                            }
                            break;
                        case ARCHON:
                            if (nearbyEnemies.length > 1) {
                                double enemyRubbleFactorArchon = 10 / (10.0 + rc.senseRubble(enemy.location));
                                enemyHeal += enemy.level * 2 * enemyRubbleFactorArchon;
                            }
                            break;
                    }

                    // See if you can attack anyone
                    if (!canAttack && moveLocation.distanceSquaredTo(enemy.location) <= RobotType.SOLDIER.actionRadiusSquared) {
                        canAttack = true;
                    }
                    // TODO: Stop moving around archon?
                    // TODO: Encourage moving towards targets?
                    // TODO: Points for being able to kill (finish off) someone?
                    // TODO: Don't flee into high rubble terrain?
                    // TODO: encourage aggressiveness if outnumbering?
                    // TODO: pursue enemy if low hp
                }
                // double closeEnemyPenalty = 0.5 * distToNearestEnemy / moveLocation.distanceSquaredTo(nearestEnemyLoc); // multiply by rc.senseRubble(); this didn't work -- Vinjai
                double myRubbleFactor = 10 / (10.0 + (rc.senseRubble(moveLocation)));
                // Add damage normalized to per turn by rubble
                if (isNotSageOrIsActionReady && (canAttack || canView)) {
                    // System.out.println("  Shoot: " + (RobotType.SOLDIER.damage * myRubbleFactor));
                    double viewOnlyMultiplier = canAttack ? 1.0 : GAMMA;
                    score += rc.getType().damage * myRubbleFactor * viewOnlyMultiplier;
                    // System.out.println(myLocation + " " + oneVersusOne + " " + distToNearestEnemy);
                    score -= enemyHeal;
                }
                // 1v1: Pursue if higher health, otherwise flee. Tiebreak in favor of aggression since striking first wins
                if (oneVersusOne && distToNearestEnemy < 1000000.0) {
                    // System.out.println("Chase: " + (((rc.getHealth()+0.01) * myRubbleFactor - enemyCombatHealth) * distToNearestEnemy /10000.0));
                    score += ((rc.getHealth() + 0.01) * myRubbleFactor - enemyCombatHealth) * (-distToNearestEnemy) / 10000.0;
                }
                // Tiebreaker
                if (dir == Direction.CENTER) {
                    score += 0.000000001;
                }
                // if (rc.getType() == RobotType.SAGE) {
                //     System.out.println(myLocation + " " + dir + " " + score);
                // }
                // Add rubble movement factor, often serves as a tiebreak for flee
                score += myRubbleFactor * 10;
                if (score > optimalScore) {
                    optimalDirection = dir;
                    optimalScore = score;
                }
            }
        }
        if (optimalDirection != null && optimalDirection != Direction.CENTER) {
            rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
            // System.out.println("Moving!: " + myLocation + " -> " + myLocation.add(optimalDirection));
            pathing.move(optimalDirection);
        }
    }

    /**
     * Returns nearest combat cluster or UNDEFINED_CLUSTER_INDEX otherwise
     * @return
     * @throws GameActionException
     */
    public int getNearestCombatCluster() throws GameActionException {
        int closestCluster = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < commsHandler.COMBAT_CLUSTER_SLOTS; i++) {
            int nearestCluster = commsHandler.readCombatClusterIndex(i);
            // Skip if no more combat clusters written
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                continue;
            }
            int distance = myLocation.distanceSquaredTo(
                new MapLocation(
                    clusterCentersX[nearestCluster % clusterWidthsLength], 
                    clusterCentersY[nearestCluster / clusterWidthsLength]
                )
            );
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCluster = nearestCluster;
            }
        }
        return closestCluster;
    }

    /**
     * Returns nearest explore cluster or UNDEFINED_CLUSTER_INDEX otherwise
     * @return
     * @throws GameActionException
     */
    public int getNearestExploreCluster() throws GameActionException {
        int closestCluster = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestClusterIndex = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < commsHandler.EXPLORE_CLUSTER_SLOTS; i++) {
            int nearestClusterAll = commsHandler.readExploreClusterAll(i);
            int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
            // Skip if no more combat clusters written
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                continue;
            }
            // Skip clusters which are fully claimed
            int nearestClusterStatus = (nearestClusterAll & 128) >> 7; // 2^7
            if (nearestClusterStatus == CommsHandler.ClaimStatus.CLAIMED) {
                continue;
            }
            int distance = myLocation.distanceSquaredTo(
                new MapLocation(
                    clusterCentersX[nearestCluster % clusterWidthsLength], 
                    clusterCentersY[nearestCluster / clusterWidthsLength]
                )
            );
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCluster = nearestCluster;
                closestClusterIndex = i;
            }
        }
        // Claim cluster
        if (closestClusterIndex != commsHandler.UNDEFINED_CLUSTER_INDEX) {
            commsHandler.writeExploreClusterClaimStatus(closestClusterIndex, CommsHandler.ClaimStatus.CLAIMED);
            commsHandler.writeClusterControlStatus(closestCluster, CommsHandler.ControlStatus.EXPLORING);
            exploreMode = true;
        }
        return closestCluster;
    }

    /**
     * If unit redirects from an exploration, remark the cluster as unknown
     * @param loc MapLocation
     * @throws GameActionException
     */
    public void resetControlStatus(MapLocation loc) throws GameActionException {
        if (exploreMode && loc.x >= 0 && loc.x < mapWidth && loc.y >= 0 && loc.y < mapHeight) {
            int cluster = whichXLoc[loc.x] + whichYLoc[loc.y]; 
            if (commsHandler.readClusterControlStatus(cluster) == CommsHandler.ControlStatus.EXPLORING) {
                commsHandler.writeClusterControlStatus(cluster, CommsHandler.ControlStatus.UNKNOWN);
            }
        };
    }

    /**
     * Get the nearest cluster that satisfies the given control status, encoded as follows:
     * 0: unknown; 1: we control; 2: enemy controls; 3: ??.
     * Get the nearest cluster that satisfies the given control status, encoded by `commsHandler.ControlStatus`.
     * 
     */
    public int getNearestClusterByControlStatus(int status) throws GameActionException {
        int closestCluster = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < numClusters; i++) {
            if (commsHandler.readClusterControlStatus(i) == status) {
                int distance = myLocation.distanceSquaredTo(
                    new MapLocation(
                        clusterCentersX[i % clusterWidthsLength], 
                        clusterCentersY[i / clusterWidthsLength]
                    )
                );
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCluster = i;
                }
            }
        }
        return closestCluster;
    }
    
    public void archonStatusCheck() throws GameActionException {
        numOurArchonsAlive = 0;
        boolean odd = rc.getRoundNum() % 2 == 1;
        int xSum = 0;
        int ySum = 0;
        // update each of archons zero through three
        if (archonZeroAlive) {
            if (commsHandler.readOurArchonStatus(0) != (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN)) {
                archonZeroAlive = false;
                archonZeroLocation = null;
            } else {
                archonZeroLocation = commsHandler.readOurArchonLocation(0);
                xSum += archonZeroLocation.x;
                ySum += archonZeroLocation.y;
                numOurArchonsAlive++;
            }
        }
        if (archonOneAlive) {
            if (commsHandler.readOurArchonStatus(1) != (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN)) {
                archonOneAlive = false;
                archonOneLocation = null;
            } else {
                archonOneLocation = commsHandler.readOurArchonLocation(1);
                xSum += archonOneLocation.x;
                ySum += archonOneLocation.y;
                numOurArchonsAlive++;
            }
        }
        if (archonTwoAlive) {
            if (commsHandler.readOurArchonStatus(2) != (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN)) {
                archonTwoAlive = false;
                archonTwoLocation = null;
            } else {
                archonTwoLocation = commsHandler.readOurArchonLocation(2);
                xSum += archonTwoLocation.x;
                ySum += archonTwoLocation.y;
                numOurArchonsAlive++;
            }
        }
        if (archonThreeAlive) {
            if (commsHandler.readOurArchonStatus(3) != (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN)) {
                archonThreeAlive = false;
                archonThreeLocation = null;
            } else {
                archonThreeLocation = commsHandler.readOurArchonLocation(3);
                xSum += archonThreeLocation.x;
                ySum += archonThreeLocation.y;
                numOurArchonsAlive++;
            }
        }
        // update the centroid
        ourArchonCentroid = new MapLocation(xSum / numOurArchonsAlive, ySum / numOurArchonsAlive);
    }

    public void setupClusters() {
        clusterHeights = computeClusterSizes(mapHeight);
        clusterWidths = computeClusterSizes(mapWidth);
        clusterWidthsLength = clusterWidths.length;
        numClusters = clusterHeights.length * clusterWidthsLength;
        yStep = mapHeight / ((float) clusterHeights.length);
        xStep = mapWidth / ((float) clusterWidths.length);
    }

    /**
     * Precompute x, y coordinates of centers of all clusters
     */
    public void precomputeClusterCenters() {
        clusterCentersX = new int[clusterWidths.length];
        clusterCentersY = new int[clusterHeights.length];
        int xStart = 0;
        for (int i = 0; i < clusterWidths.length; i++) {
            int xCenter = xStart + (clusterWidths[i] / 2);
            clusterCentersX[i] = xCenter;
            xStart += clusterWidths[i];
        }
        int yStart = 0;
        for (int j = 0; j < clusterHeights.length; j++) {
            int yCenter = yStart + (clusterHeights[j] / 2);
            clusterCentersY[j] = yCenter;
            yStart += clusterHeights[j];
        }
    }

    /**
     * Returns cluster given a location.
     * 
     * NOTE: THIS FUNCTION IS ONLY FOR REFERENCE. IF CALLED FREQUENTLY, INLINE THIS FUNCTION!!
     * @param loc
     * @return
     */
    public int whichCluster(MapLocation loc) {
        return whichXLoc[loc.x] + whichYLoc[loc.y];
    }

    /**
     * Returns center MapLocation given a cluster.
     * 
     * NOTE: THIS FUNCTION IS ONLY FOR REFERENCE. IF CALLED FREQUENTLY, INLINE THIS FUNCTION!!
     * @param clusterIdx
     * @return
     */
    public MapLocation clusterToCenter(int clusterIdx) {
        return new MapLocation(clusterCentersX[clusterIdx % clusterWidthsLength], 
                                                clusterCentersY[clusterIdx / clusterWidthsLength]);
    }

    public void initClusterPermutation() {
        switch (clusterWidths.length) {
            case 4:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {10, 5, 3, 12, 0, 15, 1, 7, 13, 9, 6, 4, 11, 14, 2, 8};
                        break;
                    case 5:
                        clusterPermutation = new int[] {10, 9, 3, 16, 0, 19, 6, 5, 18, 13, 17, 11, 2, 1, 4, 15, 12, 8, 7, 14};
                        break;
                    case 6:
                        clusterPermutation = new int[] {14, 9, 3, 20, 0, 23, 2, 7, 1, 12, 11, 22, 8, 16, 21, 15, 13, 17, 4, 19, 18, 5, 10, 6};
                        break;
                    case 7:
                        clusterPermutation = new int[] {14, 13, 3, 24, 0, 27, 9, 15, 22, 21, 26, 6, 18, 10, 20, 19, 4, 2, 1, 23, 11, 12, 16, 8, 17, 7, 5, 25};
                        break;
                    case 8:
                        clusterPermutation = new int[] {18, 13, 3, 28, 0, 31, 14, 27, 30, 23, 5, 20, 12, 2, 17, 21, 19, 29, 7, 24, 4, 15, 26, 6, 25, 9, 22, 10, 8, 1, 11, 16};
                        break;
                    case 9:
                        clusterPermutation = new int[] {18, 17, 3, 32, 0, 35, 31, 28, 24, 10, 4, 8, 13, 25, 5, 7, 12, 16, 30, 20, 23, 1, 22, 33, 6, 21, 14, 34, 11, 29, 26, 15, 19, 27, 2, 9};
                        break;
                    case 10:
                        clusterPermutation = new int[] {22, 17, 3, 36, 0, 39, 34, 29, 6, 23, 16, 13, 4, 20, 1, 14, 30, 27, 33, 37, 38, 2, 28, 31, 24, 10, 25, 12, 7, 15, 26, 18, 5, 9, 21, 11, 35, 8, 32, 19};
                        break;
                }
                break;
            case 5:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {12, 7, 4, 15, 0, 19, 17, 2, 14, 6, 13, 8, 3, 10, 5, 11, 9, 1, 18, 16};
                        break;
                    case 5:
                        clusterPermutation = new int[] {12, 4, 20, 0, 24, 19, 18, 10, 23, 15, 2, 3, 5, 6, 8, 7, 14, 17, 21, 9, 11, 22, 1, 13, 16};
                        break;
                    case 6:
                        clusterPermutation = new int[] {17, 12, 4, 25, 0, 29, 9, 26, 27, 7, 16, 13, 1, 21, 10, 19, 20, 22, 15, 8, 23, 14, 5, 3, 11, 18, 6, 28, 2, 24};
                        break;
                    case 7:
                        clusterPermutation = new int[] {17, 4, 30, 0, 34, 12, 33, 32, 26, 10, 11, 5, 6, 23, 27, 8, 18, 15, 16, 21, 7, 2, 3, 20, 19, 28, 29, 25, 24, 31, 22, 1, 9, 14, 13};
                        break;
                    case 8:
                        clusterPermutation = new int[] {22, 17, 4, 35, 0, 39, 27, 11, 21, 10, 30, 7, 26, 23, 32, 38, 25, 3, 12, 8, 1, 34, 6, 31, 9, 14, 16, 5, 19, 13, 36, 24, 18, 20, 33, 37, 2, 29, 28, 15};
                        break;
                    case 9:
                        clusterPermutation = new int[] {22, 4, 40, 0, 44, 11, 20, 16, 41, 6, 27, 3, 1, 17, 37, 39, 9, 8, 15, 2, 21, 24, 35, 13, 5, 18, 38, 43, 14, 7, 19, 28, 30, 25, 26, 10, 31, 36, 12, 33, 29, 23, 34, 32, 42};
                        break;
                    case 10:
                        clusterPermutation = new int[] {27, 22, 4, 45, 0, 49, 5, 35, 14, 37, 15, 40, 13, 38, 24, 17, 26, 47, 39, 2, 3, 31, 6, 20, 11, 41, 30, 12, 29, 28, 23, 19, 7, 32, 8, 21, 44, 10, 33, 18, 34, 36, 1, 9, 42, 46, 25, 43, 16, 48};
                        break;
                }
                break;
            case 6:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {15, 8, 5, 18, 0, 23, 17, 10, 14, 9, 13, 11, 16, 21, 4, 22, 3, 12, 2, 6, 19, 7, 20, 1};
                        break;
                    case 5:
                        clusterPermutation = new int[] {15, 14, 5, 24, 0, 29, 22, 18, 3, 16, 13, 17, 20, 4, 2, 27, 28, 1, 12, 11, 21, 9, 19, 6, 23, 26, 8, 10, 25, 7};
                        break;
                    case 6:
                        clusterPermutation = new int[] {21, 14, 5, 30, 0, 35, 22, 26, 9, 16, 32, 17, 31, 2, 12, 19, 34, 13, 20, 1, 4, 15, 10, 24, 28, 18, 7, 23, 6, 11, 27, 25, 33, 3, 8, 29};
                        break;
                    case 7:
                        clusterPermutation = new int[] {21, 20, 5, 36, 0, 41, 7, 40, 29, 38, 30, 24, 33, 13, 22, 8, 10, 4, 3, 37, 26, 39, 25, 32, 17, 31, 11, 2, 18, 12, 9, 34, 6, 19, 27, 35, 23, 15, 28, 14, 1, 16};
                        break;
                    case 8:
                        clusterPermutation = new int[] {27, 20, 5, 42, 0, 47, 10, 3, 6, 18, 12, 23, 34, 4, 41, 24, 8, 45, 2, 14, 38, 31, 46, 44, 16, 39, 13, 32, 21, 40, 17, 43, 9, 36, 22, 15, 26, 30, 35, 25, 1, 19, 29, 7, 37, 28, 33, 11};
                        break;
                    case 9:
                        clusterPermutation = new int[] {27, 26, 5, 48, 0, 53, 49, 52, 20, 37, 1, 9, 31, 50, 36, 23, 10, 41, 38, 45, 46, 11, 16, 2, 12, 40, 8, 25, 3, 33, 44, 35, 19, 34, 29, 43, 14, 18, 4, 24, 32, 47, 6, 42, 13, 22, 15, 30, 28, 39, 17, 21, 51, 7};
                        break;
                    case 10:
                        clusterPermutation = new int[] {33, 26, 5, 54, 0, 59, 36, 40, 25, 28, 31, 12, 56, 38, 44, 57, 41, 52, 3, 27, 11, 35, 21, 16, 6, 46, 23, 45, 19, 8, 30, 24, 20, 50, 17, 9, 1, 4, 7, 49, 32, 48, 18, 47, 10, 42, 37, 51, 22, 43, 29, 55, 15, 53, 14, 39, 2, 34, 13, 58};
                        break;
                }
                break;
            case 7:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {17, 10, 6, 21, 0, 27, 19, 16, 14, 26, 8, 2, 22, 9, 25, 5, 18, 4, 11, 12, 3, 24, 20, 15, 7, 23, 13, 1};
                        break;
                    case 5:
                        clusterPermutation = new int[] {17, 6, 28, 0, 34, 11, 23, 31, 16, 30, 27, 32, 18, 9, 21, 4, 25, 29, 8, 10, 2, 7, 26, 3, 14, 20, 22, 33, 13, 12, 24, 15, 19, 5, 1};
                        break;
                    case 6:
                        clusterPermutation = new int[] {24, 17, 6, 35, 0, 41, 40, 21, 20, 39, 29, 10, 15, 13, 9, 31, 12, 16, 18, 34, 19, 32, 5, 36, 4, 22, 27, 37, 14, 33, 3, 8, 2, 28, 1, 26, 30, 7, 23, 11, 25, 38};
                        break;
                    case 7:
                        clusterPermutation = new int[] {24, 6, 42, 0, 48, 9, 21, 15, 33, 25, 43, 46, 10, 36, 28, 41, 45, 31, 20, 18, 14, 1, 16, 2, 37, 22, 12, 30, 32, 13, 35, 39, 44, 29, 27, 40, 5, 11, 17, 7, 3, 47, 8, 38, 34, 26, 4, 19, 23};
                        break;
                    case 8:
                        clusterPermutation = new int[] {31, 24, 6, 49, 0, 55, 32, 48, 36, 20, 11, 35, 18, 8, 45, 33, 28, 15, 10, 50, 22, 21, 39, 7, 4, 30, 26, 44, 16, 51, 12, 13, 19, 27, 17, 47, 41, 23, 40, 53, 52, 42, 34, 2, 9, 46, 43, 54, 37, 38, 5, 25, 3, 14, 1, 29};
                        break;
                    case 9:
                        clusterPermutation = new int[] {31, 6, 56, 0, 62, 43, 58, 10, 37, 32, 28, 11, 22, 57, 40, 49, 27, 14, 50, 4, 24, 53, 17, 41, 20, 3, 18, 39, 16, 30, 13, 45, 5, 2, 38, 42, 55, 60, 7, 46, 19, 51, 44, 1, 33, 21, 61, 34, 36, 48, 12, 9, 29, 52, 15, 47, 26, 35, 23, 54, 59, 8, 25};
                        break;
                    case 10:
                        clusterPermutation = new int[] {38, 31, 6, 63, 0, 69, 55, 62, 17, 49, 56, 21, 44, 3, 22, 19, 28, 39, 47, 46, 30, 12, 50, 40, 58, 53, 23, 57, 8, 65, 13, 52, 10, 24, 37, 32, 14, 54, 33, 51, 2, 67, 68, 5, 64, 11, 45, 48, 59, 9, 29, 34, 15, 60, 35, 43, 26, 27, 20, 25, 18, 36, 41, 42, 4, 66, 1, 16, 61, 7};
                        break;
                }
                break;
            case 8:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {20, 11, 7, 24, 0, 31, 5, 29, 4, 30, 25, 21, 8, 14, 19, 1, 27, 23, 12, 28, 16, 17, 6, 26, 2, 9, 22, 18, 15, 13, 3, 10};
                        break;
                    case 5:
                        clusterPermutation = new int[] {20, 19, 7, 32, 0, 39, 2, 37, 22, 26, 14, 11, 23, 35, 10, 8, 1, 24, 16, 30, 4, 3, 29, 13, 34, 5, 38, 25, 27, 33, 12, 17, 36, 15, 6, 18, 21, 31, 9, 28};
                        break;
                    case 6:
                        clusterPermutation = new int[] {28, 19, 7, 40, 0, 47, 2, 24, 38, 10, 46, 4, 5, 9, 20, 43, 34, 32, 42, 8, 36, 44, 13, 27, 22, 11, 17, 29, 37, 31, 25, 39, 18, 26, 15, 33, 3, 35, 6, 23, 14, 45, 16, 41, 12, 30, 21, 1};
                        break;
                    case 7:
                        clusterPermutation = new int[] {28, 27, 7, 48, 0, 55, 35, 5, 44, 4, 11, 47, 30, 38, 43, 24, 42, 37, 13, 52, 15, 41, 26, 32, 10, 12, 36, 40, 19, 49, 51, 17, 33, 53, 20, 3, 29, 31, 6, 8, 1, 34, 9, 14, 18, 21, 54, 25, 39, 23, 2, 50, 45, 16, 22, 46};
                        break;
                    case 8:
                        clusterPermutation = new int[] {36, 27, 7, 56, 0, 63, 15, 51, 1, 50, 54, 18, 22, 48, 9, 47, 4, 35, 2, 43, 34, 11, 38, 20, 55, 29, 62, 26, 32, 52, 28, 57, 49, 41, 6, 25, 21, 60, 61, 13, 8, 10, 23, 24, 12, 16, 42, 45, 53, 17, 33, 5, 3, 14, 46, 31, 39, 58, 37, 40, 59, 19, 30, 44};
                        break;
                    case 9:
                        clusterPermutation = new int[] {36, 35, 7, 64, 0, 71, 3, 67, 6, 45, 17, 19, 1, 52, 44, 5, 41, 10, 4, 47, 46, 40, 12, 42, 32, 50, 31, 63, 48, 43, 62, 54, 58, 16, 9, 66, 60, 26, 30, 37, 65, 23, 15, 59, 51, 28, 25, 29, 68, 18, 34, 57, 14, 24, 11, 27, 33, 38, 55, 22, 70, 56, 39, 61, 20, 8, 21, 2, 53, 69, 13, 49};
                        break;
                    case 10:
                        clusterPermutation = new int[] {44, 35, 7, 72, 0, 79, 67, 45, 31, 65, 38, 21, 51, 27, 61, 13, 50, 59, 9, 68, 40, 56, 12, 48, 3, 63, 36, 24, 77, 60, 17, 66, 41, 1, 71, 18, 55, 64, 78, 6, 30, 2, 39, 5, 28, 57, 76, 42, 62, 73, 4, 14, 22, 53, 11, 70, 16, 52, 58, 10, 33, 46, 25, 49, 15, 8, 32, 74, 23, 26, 19, 29, 75, 34, 37, 43, 69, 54, 47, 20};
                        break;
                }
                break;
            case 9:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {22, 13, 8, 27, 0, 35, 7, 4, 19, 25, 16, 9, 17, 31, 34, 30, 32, 20, 3, 1, 14, 12, 6, 26, 23, 2, 28, 18, 33, 5, 21, 10, 24, 11, 29, 15};
                        break;
                    case 5:
                        clusterPermutation = new int[] {22, 8, 36, 0, 44, 3, 30, 13, 5, 7, 32, 25, 38, 39, 41, 18, 20, 21, 4, 19, 24, 14, 40, 37, 29, 9, 31, 6, 12, 10, 15, 11, 23, 33, 27, 16, 28, 34, 43, 1, 26, 42, 35, 2, 17};
                        break;
                    case 6:
                        clusterPermutation = new int[] {31, 22, 8, 45, 0, 53, 36, 12, 47, 25, 39, 30, 7, 16, 28, 5, 6, 41, 18, 11, 51, 37, 34, 49, 2, 40, 23, 20, 38, 17, 13, 35, 24, 52, 42, 4, 26, 46, 3, 14, 10, 33, 15, 50, 27, 43, 9, 44, 1, 29, 19, 21, 48, 32};
                        break;
                    case 7:
                        clusterPermutation = new int[] {31, 8, 54, 0, 62, 25, 49, 22, 32, 5, 12, 55, 46, 34, 28, 33, 14, 4, 39, 6, 40, 10, 60, 42, 13, 26, 1, 7, 17, 47, 2, 20, 15, 58, 56, 3, 61, 30, 41, 59, 57, 38, 37, 48, 50, 45, 44, 43, 52, 18, 23, 11, 29, 51, 9, 24, 36, 21, 53, 27, 35, 16, 19};
                        break;
                    case 8:
                        clusterPermutation = new int[] {40, 31, 8, 63, 0, 71, 10, 65, 50, 4, 2, 23, 6, 35, 1, 66, 70, 15, 59, 62, 29, 13, 20, 57, 38, 43, 45, 51, 5, 21, 36, 32, 12, 60, 41, 28, 52, 19, 9, 68, 49, 61, 69, 42, 30, 18, 3, 54, 47, 34, 24, 17, 25, 58, 33, 27, 64, 37, 55, 7, 11, 39, 67, 44, 22, 16, 26, 53, 46, 48, 14, 56};
                        break;
                    case 9:
                        clusterPermutation = new int[] {40, 8, 72, 0, 80, 41, 33, 28, 43, 24, 50, 56, 26, 37, 48, 71, 20, 53, 3, 12, 70, 61, 75, 74, 23, 1, 68, 13, 78, 76, 79, 67, 51, 17, 55, 32, 14, 18, 29, 69, 42, 49, 25, 77, 52, 57, 46, 5, 35, 4, 6, 60, 47, 63, 7, 27, 65, 11, 39, 58, 31, 36, 9, 22, 54, 45, 19, 34, 2, 73, 59, 62, 16, 10, 66, 64, 44, 21, 38, 15, 30};
                        break;
                    case 10:
                        clusterPermutation = new int[] {49, 40, 8, 81, 0, 89, 10, 72, 76, 69, 75, 34, 32, 47, 82, 38, 22, 86, 25, 31, 48, 56, 13, 59, 19, 51, 87, 44, 79, 23, 77, 9, 60, 26, 57, 55, 88, 65, 80, 4, 35, 14, 39, 3, 53, 27, 70, 66, 52, 12, 83, 28, 2, 36, 50, 43, 42, 30, 6, 61, 41, 85, 21, 63, 5, 15, 11, 58, 16, 62, 73, 29, 74, 7, 45, 54, 20, 64, 68, 46, 1, 78, 67, 71, 33, 17, 24, 18, 84, 37};
                        break;
                }
                break;
            case 10:
                switch (clusterHeights.length) {
                    case 4:
                        clusterPermutation = new int[] {25, 14, 9, 30, 0, 39, 26, 1, 35, 16, 17, 21, 32, 12, 7, 34, 38, 31, 8, 28, 11, 18, 22, 13, 6, 24, 20, 23, 10, 27, 36, 5, 15, 3, 33, 2, 29, 37, 19, 4};
                        break;
                    case 5:
                        clusterPermutation = new int[] {25, 24, 9, 40, 0, 49, 35, 15, 14, 39, 20, 27, 43, 18, 48, 30, 2, 22, 3, 11, 46, 10, 28, 4, 41, 26, 12, 5, 19, 16, 8, 29, 13, 7, 23, 33, 32, 42, 17, 37, 6, 21, 38, 1, 45, 31, 44, 34, 36, 47};
                        break;
                    case 6:
                        clusterPermutation = new int[] {35, 24, 9, 50, 0, 59, 39, 12, 1, 41, 29, 37, 52, 38, 46, 42, 58, 13, 25, 48, 57, 10, 34, 8, 30, 21, 23, 3, 18, 31, 26, 44, 54, 49, 27, 5, 17, 55, 45, 15, 20, 51, 6, 47, 36, 19, 4, 28, 56, 32, 53, 43, 33, 11, 14, 7, 2, 40, 16, 22};
                        break;
                    case 7:
                        clusterPermutation = new int[] {35, 34, 9, 60, 0, 69, 44, 2, 61, 37, 25, 5, 31, 41, 12, 40, 22, 28, 36, 3, 49, 42, 27, 62, 53, 21, 14, 32, 46, 20, 1, 18, 16, 24, 45, 59, 39, 38, 50, 33, 4, 55, 63, 23, 30, 67, 54, 19, 17, 13, 15, 68, 65, 10, 8, 26, 57, 51, 52, 47, 43, 56, 11, 29, 6, 48, 66, 64, 7, 58};
                        break;
                    case 8:
                        clusterPermutation = new int[] {45, 34, 9, 70, 0, 79, 58, 2, 59, 33, 5, 77, 3, 12, 23, 56, 63, 35, 46, 60, 11, 66, 32, 6, 1, 16, 21, 18, 65, 64, 31, 43, 27, 52, 73, 26, 54, 39, 67, 15, 53, 61, 29, 75, 71, 24, 13, 47, 22, 76, 69, 4, 17, 36, 20, 38, 7, 74, 48, 28, 49, 30, 19, 44, 78, 37, 57, 68, 8, 10, 72, 50, 51, 62, 42, 55, 40, 25, 41, 14};
                        break;
                    case 9:
                        clusterPermutation = new int[] {45, 44, 9, 80, 0, 89, 39, 56, 88, 20, 7, 71, 33, 22, 14, 34, 41, 51, 1, 38, 67, 30, 68, 28, 21, 72, 18, 52, 69, 32, 57, 77, 86, 53, 60, 11, 17, 31, 46, 43, 74, 35, 48, 3, 87, 8, 42, 82, 36, 25, 10, 19, 84, 85, 29, 61, 23, 78, 81, 63, 75, 40, 4, 58, 26, 66, 2, 13, 65, 49, 12, 37, 76, 54, 73, 70, 79, 27, 15, 24, 62, 16, 50, 5, 55, 83, 47, 6, 59, 64};
                        break;
                    case 10:
                        clusterPermutation = new int[] {55, 44, 9, 90, 0, 99, 15, 29, 58, 19, 34, 24, 7, 13, 8, 97, 45, 78, 46, 40, 36, 67, 64, 50, 81, 95, 56, 3, 11, 37, 25, 80, 33, 59, 91, 42, 61, 4, 35, 88, 70, 47, 86, 49, 87, 94, 82, 6, 32, 53, 26, 51, 65, 5, 77, 89, 71, 92, 21, 18, 31, 1, 54, 22, 75, 85, 16, 63, 2, 20, 74, 17, 14, 48, 72, 93, 69, 68, 84, 76, 66, 52, 27, 10, 96, 60, 30, 79, 38, 57, 73, 39, 62, 83, 28, 43, 23, 98, 41, 12};
                        break;
                }
                break;
        }
    }

    public int[] computeClusterSizes(int dim) {
        switch (dim) {
            case 20:
                return new int[] {5, 5, 5, 5};
            case 21:
                return new int[] {5, 5, 5, 6};
            case 22:
                return new int[] {5, 6, 5, 6};
            case 23:
                return new int[] {5, 6, 6, 6};
            case 24:
                return new int[] {6, 6, 6, 6};
            case 25:
                return new int[] {5, 5, 5, 5, 5};
            case 26:
                return new int[] {5, 5, 5, 5, 6};
            case 27:
                return new int[] {5, 5, 6, 5, 6};
            case 28:
                return new int[] {5, 6, 5, 6, 6};
            case 29:
                return new int[] {5, 6, 6, 6, 6};
            case 30:
                return new int[] {6, 6, 6, 6, 6};
            case 31:
                return new int[] {5, 5, 5, 5, 5, 6};
            case 32:
                return new int[] {5, 5, 6, 5, 5, 5};
            case 33:
                return new int[] {5, 6, 5, 6, 5, 6};
            case 34:
                return new int[] {5, 6, 6, 5, 6, 6};
            case 35:
                return new int[] {5, 6, 6, 6, 6, 6};
            case 36:
                return new int[] {6, 6, 6, 6, 6, 6};
            case 37:
                return new int[] {5, 5, 5, 6, 5, 5, 6};
            case 38:
                return new int[] {5, 5, 6, 5, 6, 5, 6};
            case 39:
                return new int[] {5, 6, 5, 6, 5, 6, 5};
            case 40:
                return new int[] {5, 6, 6, 5, 6, 6, 6};
            case 41:
                return new int[] {5, 6, 6, 6, 6, 6, 5};
            case 42:
                return new int[] {6, 6, 6, 6, 6, 6, 6};
            case 43:
                return new int[] {5, 5, 6, 5, 5, 6, 5, 6};
            case 44:
                return new int[] {5, 6, 5, 6, 5, 6, 5, 6};
            case 45:
                return new int[] {5, 6, 5, 6, 6, 5, 6, 6};
            case 46:
                return new int[] {5, 6, 6, 6, 5, 6, 6, 6};
            case 47:
                return new int[] {5, 6, 6, 6, 6, 6, 6, 6};
            case 48:
                return new int[] {6, 6, 6, 6, 6, 6, 6, 6};
            case 49:
                return new int[] {5, 5, 6, 5, 6, 5, 6, 5, 5};
            case 50:
                return new int[] {5, 6, 5, 6, 5, 6, 5, 6, 6};
            case 51:
                return new int[] {5, 6, 6, 5, 6, 6, 5, 6, 5};
            case 52:
                return new int[] {5, 6, 6, 6, 5, 6, 6, 6, 6};
            case 53:
                return new int[] {5, 6, 6, 6, 6, 6, 6, 6, 6};
            case 54:
                return new int[] {6, 6, 6, 6, 6, 6, 6, 6, 6};
            case 55:
                return new int[] {5, 6, 5, 6, 5, 6, 5, 6, 5, 6};
            case 56:
                return new int[] {5, 6, 5, 6, 6, 5, 6, 5, 6, 6};
            case 57:
                return new int[] {5, 6, 6, 5, 6, 6, 5, 6, 6, 6};
            case 58:
                return new int[] {5, 6, 6, 6, 6, 5, 6, 6, 6, 5};
            case 59:
                return new int[] {5, 6, 6, 6, 6, 6, 6, 6, 6, 5};
            case 60:
                return new int[] {6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
            default:
                return new int[] {};
        }
    }
}
