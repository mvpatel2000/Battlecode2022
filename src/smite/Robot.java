package smite;

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

    // Pathing
    MapLocation baseLocation;
    MapLocation destination;
    boolean exploreMode;
    ArrayList<MapLocation> priorDestinations;

    // Clusters are 6x6, 5x6, 6x5, or 5x5
    int numClusters;
    int[] clusterHeights;
    int[] clusterWidths;
    float xStep;
    float yStep;
    int[] whichXLoc;
    int[] whichYLoc;
    MapLocation[] clusterCenters;
    int[] clusterResources;
    int[] clusterControls;
    int[] markedClustersBuffer;

    CommsHandler commsHandler;

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
        destination = null;
        exploreMode = false;
        priorDestinations = new ArrayList<MapLocation>();
        commsHandler = new CommsHandler(rc);
        numOurArchons = rc.getArchonCount();

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
                }
            }
        }
    }

    public void run() throws GameActionException {
        // Before unit runs
        turnCount++;
        currentRound = rc.getRoundNum();
        setClusterStates();

        // Does turn
        runUnit();

        // After unit runs
    }

    /**
     * Function to be overridden by the unit classes. This is where
     * all unit-specific run stuff happens.
     */
    public void runUnit() throws GameActionException {
    }

    /**
     * Updates cluster information. Scans nearby tiles, enemy locations, and nearby resources
     * and aggregates into clusterControls and clusterResoruces as buffers. Uses 
     * markedClustersBuffer to track which buffers have been modified each turn to reset them.
     * Alternates whether control or resources are scanned each turn to conserve bytecode.
     * @throws GameActionException
     */
    public void setClusterStates() throws GameActionException {
        // int bytecodeUsed = Clock.getBytecodeNum();
        
        if (turnCount % 2 == 0) {
            setClusterControlStates();
        }
        else {
            setClusterResourceStates();
        }

        // int bytecodeUsed2 = Clock.getBytecodeNum();
        // //rc.setIndicatorString("Cluster States: "+(bytecodeUsed2 - bytecodeUsed));
    }

    /**
     * Updates cluster information. Scans nearby tiles and enemy locations and aggregates into 
     * clusterControls as a buffer. Uses markedClustersBuffer to track which buffers have been
     * modified each turn to write them. In clusterControls, we set the tens digit to be the new
     * value and only read/write if it is different from the previous state.
     * 
     * TODO: replace 10 with bitshifting by 2
     * 
     * @throws GameActionException
     */
    public void setClusterControlStates() throws GameActionException {
        int markedClustersCount = 0;

        // Mark nearby clusters as explored
        int[][] shifts = {{0, 3}, {2, 2}, {3, 0}, {2, -2}, {0, -3}, {-2, -2}, {-3, 0}, {-2, 2}};
        for (int[] shift : shifts) {
            MapLocation shiftedLocation = myLocation.translate(shift[0], shift[1]);
            if (rc.canSenseLocation(shiftedLocation)) {
                // int clusterIdx = whichCluster(shiftedLocation); Note: Inlined to save bytecode
                int clusterIdx = whichXLoc[shiftedLocation.x] + whichYLoc[shiftedLocation.y];
                // Write new status to buffer if we haven't yet
                if (clusterControls[clusterIdx] < 10) {
                    clusterControls[clusterIdx] += 10;
                    markedClustersBuffer[markedClustersCount] = clusterIdx;
                    markedClustersCount++;
                }
            }
        }

        // Mark nearby clusters with enemies as hostile
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, enemyTeam);
        // Process at max 10 enemies
        int numEnemies = Math.min(enemies.length, 10);
        for (int i = 0; i < numEnemies; i++) {
            RobotInfo enemy = enemies[i];
            // int clusterIdx = whichCluster(enemy.location); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[enemy.location.x] + whichYLoc[enemy.location.y];
            // Write new status to buffer if we haven't marked as enemy controlled yet
            if (clusterControls[clusterIdx] < 20) {
                // Only add to modified list if we haven't marked this cluster yet
                if (clusterControls[clusterIdx] < 10) {
                    markedClustersBuffer[markedClustersCount] = clusterIdx;
                    markedClustersCount++;
                }
                clusterControls[clusterIdx] = 20 + (clusterControls[clusterIdx] % 10);
            }
        }

        // Flush control buffer and write to comms
        for (int i = 0; i < markedClustersCount; i++) {
            int clusterIdx = markedClustersBuffer[i];
            int oldClusterStatus = clusterControls[clusterIdx] % 10;
            int newClusterStatus = (clusterControls[clusterIdx] - oldClusterStatus)/10;
            if (oldClusterStatus != newClusterStatus 
                    && newClusterStatus != commsHandler.readClusterControlStatus(clusterIdx)) {
                commsHandler.writeClusterControlStatus(clusterIdx, newClusterStatus);
            }
            clusterControls[clusterIdx] = newClusterStatus;
        }
    }

    /**
     * Updates cluster information. Scans nearby resources and aggregates into clusterResoruces as
     * a buffer. Uses markedClustersBuffer to track which buffers have been modified each turn to
     * reset them. In clusterResoruces, we set the ten millions digit to be the old value and only 
     * read/write if it is different from the previous state.
     * 
     * TODO: replace 10000000 with bitshifting by 3
     * 
     * @throws GameActionException
     */
    public void setClusterResourceStates() throws GameActionException {
        // Reset buffer
        int markedClustersCount = 0;

        // Scan nearby resources and aggregate counts. Require at least 2 lead since 1 lead regenerates
        for (MapLocation tile : rc.senseNearbyLocationsWithLead(RobotType.MINER.visionRadiusSquared, 2)) {
            // int clusterIdx = whichCluster(tile); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[tile.x] + whichYLoc[tile.y];
            // Only add to modified list if we haven't marked this cluster yet
            if (clusterResources[clusterIdx] % 10000000 == 0) {
                markedClustersBuffer[markedClustersCount] = clusterIdx;
                markedClustersCount++;
            }
            clusterResources[clusterIdx] += rc.senseLead(tile) - 1;
        }
        for (MapLocation tile : rc.senseNearbyLocationsWithGold(RobotType.MINER.visionRadiusSquared)) {
            // int clusterIdx = whichCluster(tile); Note: Inlined to save bytecode
            int clusterIdx = whichXLoc[tile.x] + whichYLoc[tile.y];
            // Only add to modified list if we haven't marked this cluster yet
            if (clusterResources[clusterIdx] % 10000000 == 0) {
                markedClustersBuffer[markedClustersCount] = clusterIdx;
                markedClustersCount++;
            }
            clusterResources[clusterIdx] += rc.senseGold(tile);
        }

        // Add surrounding clusters to buffer. This ensures we clear out clusters where all
        // resources have been mined
        int[][] shifts = {{0, 3}, {2, 2}, {3, 0}, {2, -2}, {0, -3}, {-2, -2}, {-3, 0}, {-2, 2}};
        for (int[] shift : shifts) {
            MapLocation shiftedLocation = myLocation.translate(shift[0], shift[1]);
            if (rc.canSenseLocation(shiftedLocation)) {
                // int clusterIdx = whichCluster(shiftedLocation); Note: Inlined to save bytecode
                int clusterIdx = whichXLoc[shiftedLocation.x] + whichYLoc[shiftedLocation.y];
                if (clusterResources[clusterIdx] % 10000000 == 0) {
                    markedClustersBuffer[markedClustersCount] = clusterIdx;
                    markedClustersCount++;
                }
            }
        }
        
        // Flush resource buffer and write to comms
        for (int i = 0; i < markedClustersCount; i++) {
            int clusterIdx = markedClustersBuffer[i];
            int rawResourceCount = clusterResources[clusterIdx] % 10000000;
            int newResourceCount = compressResourceCount(rawResourceCount);
            int oldResourceCount = (clusterResources[clusterIdx] - rawResourceCount)/10000000;
            if (oldResourceCount != newResourceCount 
                    && newResourceCount != commsHandler.readClusterResourceCount(clusterIdx)) {
                commsHandler.writeClusterResourceCount(clusterIdx, newResourceCount);
            }
            clusterResources[clusterIdx] = newResourceCount * 10000000;
        }
    }

    /**
     * Max value of 7 as we only have 3 bits
     * @param resourceCount
     */
    public int compressResourceCount(int resourceCount) {
        // if (resourceCount == 0) {
        //     return 0;
        // }
        // return Math.min((int)Math.log(resourceCount), 7);
        return (resourceCount + 99) / 100;
    }

    /**
     * Use this function instead of rc.move(). Still need
     * to verify canMove before calling this.
     */
    public void move(Direction dir) throws GameActionException {
        rc.move(dir);
        myLocation = myLocation.add(dir);
    }

    /**
     * Moves towards destination, in the optimal direction or diagonal offsets based on which is
     * cheaper to move through. Allows orthogonal moves to unlodge.
     */
    public void fuzzyMove(MapLocation destination) throws GameActionException {
        if (!rc.isMovementReady()) {
            return;
        }
        // Don't move if adjacent to destination and something is blocking it
        if (myLocation.distanceSquaredTo(destination) <= 2 && !rc.canMove(myLocation.directionTo(destination))) {
            return;
        }
        // TODO: This is not optimal! Sometimes taking a slower move is better if its diagonal.
        MapLocation myLocation = rc.getLocation();
        Direction toDest = myLocation.directionTo(destination);
        Direction[] dirs = {toDest, toDest.rotateLeft(), toDest.rotateRight(), toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
        double cost = Integer.MAX_VALUE;
        Direction optimalDir = null;
        for (int i = 0; i < dirs.length; i++) {
            // Prefer forward moving steps over horizontal shifts
            if (i > 2 && cost > 0) {
                break;
            }
            Direction dir = dirs[i];
            if (rc.canMove(dir)) {
                double newCost = rc.senseRubble(myLocation.add(dir));
                // add epsilon boost to forward direction
                if (dir == toDest) {
                    newCost -= 0.001;
                }
                if (newCost < cost) {
                    cost = newCost;
                    optimalDir = dir;
                }
            }
        }
        if (optimalDir != null) {
            move(optimalDir);
        }
    }

    /**
     * Update destination to encourage exploration if destination is off map or destination is not
     * an enemy target. Uses rejection sampling to avoid destinations near already explored areas.
     * @throws GameActionException.
     */
    public void updateDestinationForExploration() throws GameActionException {
        MapLocation nearDestination = myLocation;
        if (destination != null) {
            for (int i = 0; i < 3; i++) {
                nearDestination = nearDestination.add(nearDestination.directionTo(destination));
            }
        }
        // Reroute if 1) nearDestination not on map or 2) can sense destination and it's not on the map
        // or it's not occupied (so no Archon)
        if (destination == null || !rc.onTheMap(nearDestination) ||
            (rc.canSenseLocation(destination)
            && (!rc.onTheMap(destination)
                || !rc.isLocationOccupied(destination)
                || rc.senseRobotAtLocation(destination).team == allyTeam))) {
            // Rerouting
            if (destination != null) {
                priorDestinations.add(destination);
            }
            boolean valid = true;
            int dxexplore = (int)(Math.random()*80);
            int dyexplore = 120 - dxexplore;
            dxexplore = Math.random() < .5 ? dxexplore : -dxexplore;
            dyexplore = Math.random() < .5 ? dyexplore : -dyexplore;
            destination = new MapLocation(baseLocation.x + dxexplore, baseLocation.y + dyexplore);
            for (int i = 0; i < priorDestinations.size(); i++) {
                if (destination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                    valid = false;
                    break;
                }
            }
            while (!valid) {
                valid = true;
                destination = new MapLocation(baseLocation.x + (int)(Math.random()*80 - 40),
                                                baseLocation.y + (int)(Math.random()*80 - 40));
                for (int i = 0; i < priorDestinations.size(); i++) {
                    if (destination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                        valid = false;
                        break;
                    }
                }
            }
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
        for (RobotInfo enemy : nearbyEnemies) {
            int score = 0;
            // Always prioritize kills
            if (enemy.health <= damage) {
                score += 1000000;
            }
            // Prioritize archons after opening
            if (currentRound > 500 && enemy.type == RobotType.ARCHON) {
                score += 20000;
            }
            // Prioritize combat units
            if (enemy.type == RobotType.WATCHTOWER || enemy.type == RobotType.SOLDIER || enemy.type == RobotType.SAGE) {
                score += 10000;
            }
            // Target prototype units closest to being finished
            if (enemy.getMode() == RobotMode.PROTOTYPE && enemy.getType().getMaxHealth(1) - enemy.health <= 10) {
                // We want to prioritize hitting buildings closest to being finished so they're not completed
                score += 100000 + enemy.getType().getMaxHealth(1) - enemy.health;
            }
            // Target weakest unit
            else {
                score += 1000 - enemy.health;
            }
            if (score > optimalScore) {
                optimalAttack = enemy.location;
                optimalScore = score;
            }
        }
        if (optimalAttack != null && rc.canAttack(optimalAttack)) {
            rc.attack(optimalAttack);
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
            // Break if no more combat clusters exist
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                break;
            }
            int distance = myLocation.distanceSquaredTo(clusterCenters[nearestCluster]);
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
            int nearestCluster = commsHandler.readExploreClusterIndex(i);
            // Break if no more combat clusters exist
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                break;
            }
            int distance = myLocation.distanceSquaredTo(clusterCenters[nearestCluster]);
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
     * @param destination
     * @throws GameActionException
     */
    public void resetControlStatus(MapLocation destination) throws GameActionException {
        if (exploreMode) {
            // int cluster = whichCluster(destination);
            int cluster = whichXLoc[destination.x] + whichYLoc[destination.y];
            commsHandler.writeClusterControlStatus(cluster, CommsHandler.ControlStatus.UNKNOWN);
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
                int distance = myLocation.distanceSquaredTo(clusterCenters[i]);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCluster = i;
                }
            }
        }
        return closestCluster;
    }

    public void setupClusters() {
        clusterHeights = computeClusterSizes(mapHeight);
        clusterWidths = computeClusterSizes(mapWidth);
        numClusters = clusterHeights.length * clusterWidths.length;
        yStep = mapHeight / ((float) clusterHeights.length);
        xStep = mapWidth / ((float) clusterWidths.length);
    }

    public void precomputeClusterCenters() {
        clusterCenters = new MapLocation[numClusters];
        int yStart = 0;
        for (int j = 0; j < clusterHeights.length; j++) {
            int xStart = 0;
            for (int i = 0; i < clusterWidths.length; i++) {
                int xCenter = xStart + (clusterWidths[i] / 2);
                int yCenter = yStart + (clusterHeights[j] / 2);
                clusterCenters[i + j * clusterWidths.length] = new MapLocation(xCenter, yCenter);
                xStart += clusterWidths[i];
            }
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
