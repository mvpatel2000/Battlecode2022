package artemis;

import battlecode.common.*;

public class Archon extends Robot {

    final int HOSPITAL_SIZE = 160;

    int myArchonNum = -1;

    // my build counts (not all archons, just me)
    int numMinersBuilt = 0;
    int numSoldiersBuilt = 0;
    int numSagesBuilt = 0;
    int numBuildersBuilt = 0;

    // current total counts of our units on the map
    int minerCount = 0;
    int soldierCount = 0;
    int builderCount = 0;
    int sageCount = 0;

    boolean lastArchon = false;

    MapLocation optimalResourceBuildLocation;
    MapLocation optimalCombatBuildLocation;
    MapLocation optimalShelteredBuildLocation;

    int resourcesOnMap;

    int reservedLead = 0;
    int reservedGold = 0;

    // used to reset exploration
    int turnsWithNoExploring = 0;

    int oldResourceCount = 0;
    int resourceRate = 0;

    // used to move around
    int turnsUntilLand = -1;

    // regen cache
    int[] timeToRegen;

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
        computeArchonNum();
        initClusterPermutation();
        numOurArchonsAlive = numOurArchons;
        if (myArchonNum == 0) {
            commsHandler.initPriorityClusters();
        }
        if (myArchonNum == numOurArchons - 1) {
            lastArchon = true;
        }

        setBestBuildLocations();
        commsHandler.writeOurArchonAcceptingPatients(myArchonNum, CommsHandler.ArchonStatus.ACCEPTING_PATIENTS);
        timeToRegen = new int[numClusters];
    }

    @Override
    public void runUnit() throws GameActionException {
        // if (currentRound > 450) {
        //     System.out.println("Symmetry: " + commsHandler.readMapSymmetry());
        //     rc.resign();
        // }

        updateUnitCounts();

        if (currentRound == 2) {
            setInitialExploreClusters();
        }

        updateResourceRate();

        int nearestCluster = considerTransform();
        // Finished transforming back to turret from moving
        if (rc.getMode() == RobotMode.TURRET && rc.isActionReady() 
            && commsHandler.readOurArchonIsMoving(myArchonNum) == CommsHandler.ArchonStatus.MOVING) {
            commsHandler.writeOurArchonIsMoving(myArchonNum, CommsHandler.ArchonStatus.STATIONARY);
        }
        // rc.setIndicatorString(turnsUntilLand + " " + nearestCluster);

        boolean isPortable = rc.getMode() == RobotMode.PORTABLE;

        setPriorityClusters(isPortable);
        if (isPortable) {
            portableMove(nearestCluster);
        }

        checkTLE();
        build();
        checkTLE();
        repair();
        checkTLE();
    }

    public void checkTLE() throws GameActionException {
        if (rc.getRoundNum() > currentRound) { // if we TLEd into the next round
            System.out.println("I TLE'd");
            currentRound = rc.getRoundNum();
            updateUnitCounts();
            updateResourceRate();
            archonStatusCheck();
        }
    }

    /**
     * Paths to nearest combat cluster. Prepares to land if appropriate
     * @param nearestCluster
     * @throws GameActionException
     */
    public void portableMove(int nearestCluster) throws GameActionException {
        // Transform back to turret
        if (turnsUntilLand >= 0) {
            // Move if we can find a lower terrain tile near us
            if (rc.isMovementReady()) {
                Direction optimalDirection = Direction.CENTER;
                int optimalRubble = rc.senseRubble(myLocation);
                for (Direction dir : directionsWithoutCenter) {
                    if (rc.canMove(dir)) {
                        MapLocation moveLocation = myLocation.add(dir);
                        int rubble = rc.senseRubble(moveLocation);
                        if (rubble < optimalRubble) {
                            optimalDirection = dir;
                            optimalRubble = rubble;
                        }
                    }
                }
                // Move to lower rubble area
                if (optimalDirection != Direction.CENTER) {
                    pathing.move(optimalDirection);
                }
            }
            // Decrease counter for finding a spot to land
            if (turnsUntilLand > 0) {
                turnsUntilLand--;
            }
            // Transform if we chose not to move or we're out of turns to find better land position
            else if (rc.canTransform() && (rc.isMovementReady() || turnsUntilLand == 0)) {
                rc.transform();
                setBestBuildLocations();
                turnsUntilLand = -1;
            }
        }
        // Keep going
        else {
            MapLocation newDest = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                            clusterCentersY[nearestCluster / clusterWidthsLength]);
            pathing.updateDestination(newDest);
            pathing.pathToDestination();
        }
    }

    /**
     * Consider transforming to mobile mode or vice-versa. Returns target cluster at each step or UNDEFINED_CLUSTER_INDEX
     * @throws GameActionException
     */
    public int considerTransform() throws GameActionException {
        // Must be in turret mode with no allies nearby
        if (rc.getMode() == RobotMode.TURRET) {
            if (nearbyEnemies.length == 0) {
                boolean otherArchonTurretExists = false;
                if (archonZeroAlive && myArchonNum != 0 && commsHandler.readOurArchonIsMoving(0) == CommsHandler.ArchonStatus.STATIONARY) {
                    otherArchonTurretExists = true;
                }
                else if (archonOneAlive && myArchonNum != 1 && commsHandler.readOurArchonIsMoving(1) == CommsHandler.ArchonStatus.STATIONARY) {
                    otherArchonTurretExists = true;
                }
                else if (archonTwoAlive && myArchonNum != 2 && commsHandler.readOurArchonIsMoving(2) == CommsHandler.ArchonStatus.STATIONARY) {
                    otherArchonTurretExists = true;
                }
                else if (archonThreeAlive && myArchonNum != 3 && commsHandler.readOurArchonIsMoving(3) == CommsHandler.ArchonStatus.STATIONARY) {
                    otherArchonTurretExists = true;
                }
                // Must have at least one archon in turret mode
                if (otherArchonTurretExists) {
                    RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.ARCHON.actionRadiusSquared, allyTeam);
                    int length = nearbyAllies.length;
                    boolean canRepair = false;
                    for (int i = 0; i < length; i++) {
                        RobotInfo ally = nearbyAllies[i];
                        if (ally.health < ally.type.getMaxHealth(ally.level)) {
                            canRepair = true;
                            break;
                        }
                    }
                    // Must not be any repair targets
                    if (!canRepair) {
                        int nearestCluster = getNearestCombatCluster();
                        // Transform and return target cluster
                        if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                            MapLocation newDest = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]);
                            if (myLocation.distanceSquaredTo(newDest) > 64 && rc.canTransform()) {
                                rc.transform();
                                commsHandler.writeOurArchonIsMoving(myArchonNum, CommsHandler.ArchonStatus.MOVING);
                                return nearestCluster;
                            }
                        }
                    }
                }
            }
            return commsHandler.UNDEFINED_CLUSTER_INDEX;
        }
        // indicate whether we should land or otherwise return target cluster
        else if (rc.getMode() == RobotMode.PORTABLE) {
            int nearestCluster = getNearestCombatCluster();
            // Try to land if we're not already doing so
            if (turnsUntilLand < 0) {
                // Transform faster
                if (nearbyEnemies.length > 0 || (pathing.destination != null && myLocation.distanceSquaredTo(pathing.destination) <= 64)) {
                    turnsUntilLand = 3;
                }
                // Transform but allow moves to better location
                if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    turnsUntilLand = 5;
                }
            }
            return nearestCluster;
        }
        else {
            System.out.println("UNEXPECTED STATE! ARCHON IS NIETHER TURRET NOR PORTABLE");
            return commsHandler.UNDEFINED_CLUSTER_INDEX;
        }
    }

    /**
     * Sets best build locations
     * @throws GameActionException
     */
    public void setBestBuildLocations() throws GameActionException {
        // Get best build direction closest to resources
        Direction toResources = Direction.CENTER;
        int nearestDistance = Integer.MAX_VALUE;
        MapLocation[] resourceLocations = rc.senseNearbyLocationsWithLead(RobotType.ARCHON.visionRadiusSquared);
        int length = Math.min(resourceLocations.length, 10);
        for (int i = 0; i < length; i++) {
            MapLocation tile = resourceLocations[i];
            int distance = myLocation.distanceSquaredTo(tile);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                toResources = myLocation.directionTo(tile);
            }
        }
        optimalResourceBuildLocation = myLocation.add(toResources);

        // Get best soldier spawn location
        Direction toEnemy = Direction.CENTER;
        nearestDistance = Integer.MAX_VALUE;
        RobotInfo[] nearbyArchons = rc.senseNearbyRobots(RobotType.ARCHON.visionRadiusSquared, enemyTeam);
        // Spawn towards nearby enemy if there exists one
        if (nearbyArchons.length > 0) {
            for (int i = 0; i < nearbyArchons.length; i++) {
                MapLocation nearbyArchon = nearbyArchons[i].location;
                int distance = myLocation.distanceSquaredTo(nearbyArchon);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    toEnemy = myLocation.directionTo(nearbyArchon);
                }
            }
            optimalCombatBuildLocation = myLocation.add(toEnemy);
            optimalShelteredBuildLocation = myLocation.add(toEnemy.opposite());
        }
        // Otherwise spawn towards middle of map
        else {
            MapLocation center = new MapLocation(mapWidth/2, mapHeight/2);
            Direction toCenter = myLocation.directionTo(center);
            optimalCombatBuildLocation = myLocation.add(toCenter);
            optimalShelteredBuildLocation = myLocation.add(toCenter.opposite());
        }
    }

    public void updateResourceRate() throws GameActionException {
        int newResourceCount = rc.getTeamLeadAmount(allyTeam);
        resourceRate = newResourceCount - oldResourceCount;
        // Built a unit, adjust for it
        if (resourceRate < 0) {
            // Built builder
            if (resourceRate > -40) {
                resourceRate += 40;
            }
            // Built miner
            else if (resourceRate > -50) {
                resourceRate += 50;
            }
            // Built soldier
            else {
                resourceRate += 75;
            }
            // Built multiple units, unclear what we built. Set rate to 10
            if (resourceRate < 0) {
                resourceRate = 10;
            }
        }
    }

    public void updateUnitCounts() throws GameActionException {
        minerCount = commsHandler.readWorkerCountMiners();
        soldierCount = commsHandler.readFighterCountSoldiers();
        builderCount = commsHandler.readWorkerCountBuilders();
        sageCount = commsHandler.readFighterCountSages();

        // System.out.println("We currently have " + minerCount + " miners and " +
        // soldierCount + " soldiers.");

        if (lastArchon) {
            commsHandler.writeWorkerCountAll(0);
            commsHandler.writeFighterCountAll(0);
        }
    }

    /**
     * Sets initial explore clusters
     * 
     * @throws GameActionException
     */
    public void setInitialExploreClusters() throws GameActionException {
        int exploreClusterIndex = 0;
        // Preserve explore clusters which still have not been claimed
        while (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS) {
            int nearestClusterAll = commsHandler.readExploreClusterAll(exploreClusterIndex);
            int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
            int nearestClusterStatus = (nearestClusterAll & 128) >> 7; // 2^7
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX
                    || nearestClusterStatus == CommsHandler.ClaimStatus.CLAIMED) {
                break;
            }
            exploreClusterIndex++;
        }

        int centerCluster = whichXLoc[mapWidth / 2] + whichYLoc[mapHeight / 2];
        int xyCluster = whichXLoc[mapWidth - myLocation.x] + whichYLoc[mapHeight - myLocation.y];
        int xCluster = whichXLoc[mapWidth - myLocation.x] + whichYLoc[myLocation.y];
        int yCluster = whichXLoc[myLocation.x] + whichYLoc[mapHeight - myLocation.y];
        int[] clusters = { centerCluster, xyCluster, xCluster, yCluster };

        for (int i = 0; i < 4; i++) {
            int cluster = clusters[i];
            int controlStatus = commsHandler.readClusterControlStatus(cluster);
            if (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS
                    && controlStatus == CommsHandler.ControlStatus.UNKNOWN) {
                commsHandler.writeExploreClusterAll(exploreClusterIndex,
                        cluster + (CommsHandler.ClaimStatus.UNCLAIMED << 7));
                exploreClusterIndex++;

                // Preserve explore clusters which still have not been claimed
                while (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS) {
                    int nearestClusterAll = commsHandler.readExploreClusterAll(exploreClusterIndex);
                    int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
                    int nearestClusterStatus = (nearestClusterAll & 128) >> 7; // 2^7
                    if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX
                            || nearestClusterStatus == CommsHandler.ClaimStatus.CLAIMED) {
                        break;
                    }
                    exploreClusterIndex++;
                }
            }
        }
    }

    /**
     * Sets the priority clusters list
     * 
     * @throws GameActionException
     */
    public void setPriorityClusters(boolean isPortable) throws GameActionException {
        // Do not set for first turn as archons haven't all filled in data
        if (turnCount <= 1) {
            return;
        }

        resourcesOnMap = 0;
        int combatClusterIndex = 0;
        int mineClusterIndex = 0;
        int exploreClusterIndex = 0;
        int emptyExploreClusters = 0;

        // String status = "";
        // for (int i = 0; i < commsHandler.COMBAT_CLUSTER_SLOTS; i++) {
        // int cluster = commsHandler.readCombatClusterIndex(i);
        // status += " " + cluster + "(" +
        // commsHandler.readClusterControlStatus(cluster) + ")";
        // }
        // System.out.println("Combat"+status);
        // String status = "";
        // for (int i = 0; i < commsHandler.EXPLORE_CLUSTER_SLOTS; i++) {
        // int cluster = commsHandler.readExploreClusterIndex(i);
        // status += " " + cluster + "(" +
        // commsHandler.readClusterControlStatus(cluster) + "," +
        // commsHandler.readExploreClusterClaimStatus(i) + ")";
        // }
        // System.out.println("Explore"+status);
        // String status = "";
        // for (int i = 0; i < commsHandler.MINE_CLUSTER_SLOTS; i++) {
        //     int cluster = commsHandler.readMineClusterIndex(i);
        //     if (cluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
        //         rc.setIndicatorDot(clusterToCenter(cluster), 255, 0, 0);
        //     }
        //     MapLocation center = cluster != commsHandler.UNDEFINED_CLUSTER_INDEX ? clusterToCenter(cluster) : new MapLocation(-1, -1);
        //     status += " " + cluster + "|" + center + "(" + commsHandler.readMineClusterClaimStatus(i) +
        //     "," + commsHandler.readClusterResourceCount(cluster) + ")";
        // }
        // System.out.println("Mine"+status);

        // Clear slots and adjust indices if not moving
        if (!isPortable) {
            // Clear combat clusters
            for (int i = 0; i < commsHandler.COMBAT_CLUSTER_SLOTS; i++) {
                int cluster = commsHandler.readCombatClusterIndex(i);
                if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    continue;
                }
                if (commsHandler.readClusterControlStatus(cluster) != CommsHandler.ControlStatus.THEIRS) {
                    commsHandler.writeCombatClusterIndex(i, commsHandler.UNDEFINED_CLUSTER_INDEX);
                }
            }
            // Clear mine slots
            for (int i = 0; i < commsHandler.MINE_CLUSTER_SLOTS; i++) {
                int cluster = commsHandler.readMineClusterIndex(i);
                if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    continue;
                }
                int resourceCount = commsHandler.readClusterResourceCount(cluster);
                int controlStatus = commsHandler.readClusterControlStatus(cluster);
                if (resourceCount == 0 || controlStatus == CommsHandler.ControlStatus.THEIRS) {
                    commsHandler.writeMineClusterIndex(i, commsHandler.UNDEFINED_CLUSTER_INDEX);
                } else {
                    commsHandler.writeMineClusterClaimStatus(i, resourceCount / 4);
                }
            }
            // Clear explore slots
            for (int i = 0; i < commsHandler.EXPLORE_CLUSTER_SLOTS; i++) {
                int nearestClusterAll = commsHandler.readExploreClusterAll(i);
                int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
                if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    emptyExploreClusters++;
                    continue;
                }
                int nearestClusterStatus = (nearestClusterAll & 128) >> 7; // 2^7
                int controlStatus = commsHandler.readClusterControlStatus(nearestCluster);
                if (nearestClusterStatus == CommsHandler.ClaimStatus.CLAIMED
                        || controlStatus == CommsHandler.ControlStatus.OURS
                        || controlStatus == CommsHandler.ControlStatus.THEIRS) {
                    // Also resets claimed/unclaimed bit to unclaimed
                    commsHandler.writeExploreClusterAll(i, commsHandler.UNDEFINED_CLUSTER_INDEX);
                }
            }

            // Track number of turns no explore cluster has been set
            boolean allExploredClustersEmpty = emptyExploreClusters == commsHandler.EXPLORE_CLUSTER_SLOTS;
            if (currentRound > 1000 && allExploredClustersEmpty) {
                turnsWithNoExploring++;
            } else {
                turnsWithNoExploring = 0;
            }
            // Reset explore bits
            if (turnsWithNoExploring >= 2) {
                turnsWithNoExploring = 0;
                // System.out.println("RESETTING EXPLORE");
                // int length = clusterPermutation.length;
                // for (int prePermuteIdx = 0; prePermuteIdx < length; prePermuteIdx++) {
                //     int i = clusterPermutation[prePermuteIdx];
                //     if (commsHandler.readClusterControlStatus(i) == CommsHandler.ControlStatus.OURS) {
                //         commsHandler.writeClusterControlStatus(i, CommsHandler.ControlStatus.UNKNOWN);
                //     }
                // }
                commsHandler.resetAllClusterControlStatus();
            }

            // Preserve combat clusters which still have enemies
            while (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS) {
                int cluster = commsHandler.readCombatClusterIndex(combatClusterIndex);
                if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    break;
                }
                combatClusterIndex++;
            }
            // Preserve mining clusters which still have resources
            while (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                int cluster = commsHandler.readMineClusterIndex(mineClusterIndex);
                if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    break;
                }
                mineClusterIndex++;
            }
            // Preserve explore clusters which still have not been claimed
            while (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS) {
                int nearestCluster = commsHandler.readExploreClusterIndex(exploreClusterIndex);
                if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    break;
                }
                exploreClusterIndex++;
            }
        }

        // Cap number of regen writes
        int regenWrites = 0;

        // Set priority clusters
        int startIdx = 0;
        int endIdx = numClusters;

        for (int prePermuteIdx = startIdx; prePermuteIdx < endIdx; prePermuteIdx++) {
            int i = clusterPermutation[prePermuteIdx];
            int controlStatus = commsHandler.readClusterControlStatus(i);
            int resourceCount = commsHandler.readClusterResourceCount(i);

            // Note: This will cause ghost pings on areas that are cleared out. We need
            // 1 additional bit to indicate empty to avoid ghost pings
            // Advance timer if it's on -- note that 0 means timer is off
            if (timeToRegen[i] > 0) {
                timeToRegen[i]++;
            }
            // Cluster has resoures, reset timer
            if (resourceCount > 0) {
                timeToRegen[i] = 1;
            }
            // Write regen to cluster
            else if (regenWrites < 10 && resourceCount == 0 && timeToRegen[i] >= 30) {
                commsHandler.writeClusterResourceCount(i, 1);
                resourceCount = 1;
                regenWrites++;
            }

            // below for debugging
            // if (!lastArchon) {
            //     MapLocation clusterCenter = new MapLocation(clusterCentersX[i % clusterWidthsLength], 
            //                                         clusterCentersY[i / clusterWidthsLength]);
            //     switch (controlStatus) {
            //         case CommsHandler.ControlStatus.THEIRS:
            //             rc.setIndicatorDot(clusterCenter, rc.getTeam().ordinal() * 255, 0, 255 - 255 * rc.getTeam().ordinal());
            //             break;
            //         case CommsHandler.ControlStatus.OURS:
            //             rc.setIndicatorDot(clusterCenter, 255 - rc.getTeam().ordinal() * 255, 0, 255 * rc.getTeam().ordinal());
            //             break;
            //         case CommsHandler.ControlStatus.EXPLORING:
            //             rc.setIndicatorDot(clusterCenter, 0, 255, 0);
            //             break;
            //         default:
            //             rc.setIndicatorDot(clusterCenter, 255, 255, 0);
            //     }
            // }
            
            // debug mine clusters
            // MapLocation clusterCenter = clusterToCenter(i);
            // if (resourceCount > 0) {
            //     rc.setIndicatorDot(clusterCenter, 255, 0, 0);
            // }

            if (controlStatus != CommsHandler.ControlStatus.THEIRS) {
                resourcesOnMap += resourceCount * LEAD_RESOLUTION;
            }
            // Set short list if not moving
            if (!isPortable) {
                // Combat cluster
                if (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS
                        && controlStatus == CommsHandler.ControlStatus.THEIRS) {
                    // Verify cluster is not already in comms list
                    boolean isValid = true;
                    for (int j = 0; j < commsHandler.COMBAT_CLUSTER_SLOTS; j++) {
                        if (commsHandler.readCombatClusterIndex(j) == i) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) {
                        commsHandler.writeCombatClusterIndex(combatClusterIndex, i);
                        combatClusterIndex++;

                        // Preserve combat clusters which still have enemies
                        while (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS) {
                            int cluster = commsHandler.readCombatClusterIndex(combatClusterIndex);
                            if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                                break;
                            }
                            combatClusterIndex++;
                        }
                    }
                }
                // Mine cluster
                if (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS && resourceCount > 0
                        && controlStatus != CommsHandler.ControlStatus.THEIRS) {
                    // Verify cluster is not already in comms list
                    boolean isValid = true;
                    for (int j = 0; j < commsHandler.MINE_CLUSTER_SLOTS; j++) {
                        if (commsHandler.readMineClusterIndex(j) == i) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) {
                        commsHandler.writeMineClusterAll(mineClusterIndex, i + ((resourceCount / 4) << 7));
                        mineClusterIndex++;

                        // Preserve mining clusters which still have resources
                        while (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                            int cluster = commsHandler.readMineClusterIndex(mineClusterIndex);
                            if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                                break;
                            }
                            mineClusterIndex++;
                        }
                    }
                }
                // Explore cluster
                if (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS
                        && controlStatus == CommsHandler.ControlStatus.UNKNOWN) {
                    // Verify cluster is not already in comms list
                    boolean isValid = true;
                    for (int j = 0; j < commsHandler.EXPLORE_CLUSTER_SLOTS; j++) {
                        if (commsHandler.readExploreClusterIndex(j) == i) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) {
                        // Implicitly sets unclaimed bit to 0 (unclaimed)
                        commsHandler.writeExploreClusterAll(exploreClusterIndex, i);
                        exploreClusterIndex++;

                        // Preserve explore clusters which still have not been claimed
                        while (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS) {
                            int nearestCluster = commsHandler.readExploreClusterIndex(exploreClusterIndex);
                            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                                break;
                            }
                            exploreClusterIndex++;
                        }
                    }
                }
            }
        }

        // System.out.println("Estimated resources on the map: " + resourcesOnMap);

        // rc.setIndicatorString(combatClusterIndex + " " + mineClusterIndex + " " +
        // exploreClusterIndex);
    }

    /**
     * Builds a unit
     * 
     * @throws GameActionException
     */
    public void build() throws GameActionException {
        // temporary solution to starvation
        double passThreshold = (1 / (double) (numOurArchons - myArchonNum)) + (rc.getTeamLeadAmount(allyTeam) / 1000.0);
        boolean pass = rng.nextDouble() > passThreshold;
        if (pass && reservedLead == 0 && reservedGold == 0) { // don't pass if we have already reserved some resources
            return;
        }

        // un-reserve whatever we previously reserved; we'll decide what to build
        // independently from the past
        // System.out.println("Start of build, currently have reserved " + reservedLead
        // + " lead and " + reservedGold + " gold");
        // System.out.println("Global reserves are " +
        // commsHandler.readReservedResourcesLead() + " lead and " +
        // commsHandler.readReservedResourcesGold() + " gold");
        if (reservedLead > 0) {
            commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() - reservedLead);
            reservedLead = 0;
        }
        if (reservedGold > 0) {
            commsHandler.writeReservedResourcesGold(commsHandler.readReservedResourcesGold() - reservedGold);
            reservedGold = 0;
        }

        RobotType toBuild = RobotType.SOLDIER;
        int initialMiners = Math.max(4, (int) ((mapHeight * mapWidth / 240) + 3)); // 4-18
        // int initialMiners = (mapHeight * mapWidth / 200) + 2;

        if (minerCount < initialMiners) {
            toBuild = RobotType.MINER;
        // } else if (numBuildersBuilt == 0 && soldierCount >= (numOurArchons * 2)) {
            // toBuild = RobotType.BUILDER;
        } else if (minerCount < rc.getRobotCount() / (Math.max(2.5, (4.5 - resourcesOnMap / 600)))) {
            toBuild = RobotType.MINER;
        }

        // TODO: actually make builders sometime?
        // // Build builders if lots of lead for watchtowers
        // if (rc.getTeamLeadAmount(allyTeam) > 500 && rng.nextDouble() < 0.3) {
        // toBuild = RobotType.BUILDER;
        // }

        // Override: if I'm dying (and there are no enemy threats visible) and there aren't many builders out on the map, priority build a builder
        if (rc.getHealth() < 0.3 * RobotType.ARCHON.getMaxHealth(rc.getLevel()) && builderCount < 2) {
            toBuild = RobotType.BUILDER;
            reservedLead = RobotType.BUILDER.buildCostLead / LEAD_RESERVE_SCALE;
        }

        // Override: if I haven't built a miner yet, priority build one
        if (numMinersBuilt == 0) {
            toBuild = RobotType.MINER;
            reservedLead = RobotType.MINER.buildCostLead / LEAD_RESERVE_SCALE;
        }

        // Override: if there is a visible enemy archon/soldier/sage/watchtower, priority build a
        // soldier
        if (nearbyEnemies.length > 0) {
            for (RobotInfo enemy : nearbyEnemies) {
                if (enemy.type == RobotType.SOLDIER || enemy.type == RobotType.ARCHON || enemy.type == RobotType.SAGE || enemy.type == RobotType.WATCHTOWER) {
                    toBuild = RobotType.SOLDIER;
                    reservedLead = RobotType.SOLDIER.buildCostLead / LEAD_RESERVE_SCALE; // priority build
                    // System.out.println("Would like to priority build a soldier");
                }
            }
        }

        // Highest priority override: if we have gold, just make a sage
        if (rc.getTeamGoldAmount(allyTeam) >= RobotType.SAGE.buildCostGold) {
            toBuild = RobotType.SAGE;
            reservedLead = 0;
        }

        // Prioritize building towards resources on low rubble
        MapLocation optimalBuildLocation = toBuild == RobotType.MINER ? optimalResourceBuildLocation : toBuild == RobotType.BUILDER ? optimalShelteredBuildLocation : optimalCombatBuildLocation;
        Direction optimalDir = null;
        int optimalScore = Integer.MAX_VALUE;
        for (Direction dir : directionsWithoutCenter) {
            if (rc.canBuildRobot(toBuild, dir)) {
                MapLocation buildLocation = myLocation.add(dir);
                int score = rc.senseRubble(buildLocation)
                        + buildLocation.distanceSquaredTo(optimalBuildLocation);
                if (score < optimalScore) {
                    optimalDir = dir;
                    optimalScore = score;
                }
            }
        }

        // Either build or reserve
        if (optimalDir != null
                && rc.getTeamLeadAmount(allyTeam) >= toBuild.buildCostLead
                        + commsHandler.readReservedResourcesLead() * LEAD_RESERVE_SCALE
                && rc.getTeamGoldAmount(allyTeam) >= toBuild.buildCostGold
                        + commsHandler.readReservedResourcesGold() * GOLD_RESERVE_SCALE
                && rc.canBuildRobot(toBuild, optimalDir)) {
            buildRobot(toBuild, optimalDir);
            reservedLead = 0;
            reservedGold = 0;
        } else { // Can't build now, reserve resources for it
            if (reservedLead > 0) {
                // System.out.println("Updating global lead reserve to " +
                // commsHandler.readReservedResourcesLead() + reservedLead);
                commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() + reservedLead);
            }
            if (reservedGold > 0) {
                // System.out.println("Updating global gold reserve to " +
                // commsHandler.readReservedResourcesGold() + reservedGold);
                commsHandler.writeReservedResourcesGold(commsHandler.readReservedResourcesGold() + reservedGold);
            }
        }
    }

    private void buildRobot(RobotType type, Direction dir) throws GameActionException {
        switch (type) {
            case MINER:
                numMinersBuilt++;
                break;
            case SOLDIER:
                numSoldiersBuilt++;
                break;
            case SAGE:
                numSagesBuilt++;
                break;
            case BUILDER:
                numBuildersBuilt++;
                break;
            default:
                break;
        }
        rc.buildRobot(type, dir);
    }

    /**
     * If we didn't build a unit, repair a damaged nearby one. If there's enemies,
     * repair the weakest
     * to keep them alive. If there aren't enemies, repair the strongest so we can
     * send it back out
     * 
     * @throws GameActionException
     */
    public void repair() throws GameActionException {
        int amountToRepair = 0;
        int numFriendlyArchons = 0;
        boolean existEnemies = nearbyEnemies.length > 0;
        RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.ARCHON.actionRadiusSquared, allyTeam);
        MapLocation optimalRepair = null;
        int remainingHealth = existEnemies ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        boolean optimalPriority = false;
        for (RobotInfo ally : nearbyAllies) {
            if (ally.type == RobotType.ARCHON) {
                numFriendlyArchons++;
            }
            int amountToRepairForAlly = ally.type.getMaxHealth(ally.level) - ally.health;
            if (amountToRepairForAlly > 0) {
                amountToRepair += amountToRepairForAlly;
                // If under duress, prioritize healing soldiers unless a miner is about to die
                boolean allyPriority = existEnemies 
                                && (ally.type == RobotType.SOLDIER || (ally.type == RobotType.MINER && ally.health <= 9));
                // Either ally is priority or both current priority and ally priority are false
                boolean isHigherPriority = allyPriority || optimalPriority == allyPriority;
                if (rc.canRepair(ally.location) && isHigherPriority
                        && ((existEnemies && ally.health < remainingHealth)
                        || (!existEnemies && ally.health > remainingHealth))) {
                    optimalPriority = allyPriority;
                    optimalRepair = ally.location;
                    remainingHealth = ally.health;
                }
            }
        }
        if (optimalRepair != null && rc.canRepair(optimalRepair)) {
            rc.repair(optimalRepair);
            // System.out.println("Repairing " + optimalRepair);
        }
        double myRubbleFactor = 10 / (10.0 + rc.senseRubble(myLocation));
        boolean shouldAcceptPatients = amountToRepair >= HOSPITAL_SIZE * myRubbleFactor * (numFriendlyArchons + 1);
        int isHospitalFull = shouldAcceptPatients ? CommsHandler.ArchonStatus.NOT_ACCEPTING_PATIENTS
                                                    : CommsHandler.ArchonStatus.ACCEPTING_PATIENTS;
        if (commsHandler.readOurArchonAcceptingPatients(myArchonNum) != isHospitalFull) {
            commsHandler.writeOurArchonAcceptingPatients(myArchonNum, isHospitalFull);
        }
    }

    /**
     * First round, find out our archon number and set our status and location
     * 
     * @throws GameActionException
     */
    public void computeArchonNum() throws GameActionException {
        if (myArchonNum >= 0) {
            return;
        }
        myArchonNum = 0;
        if (commsHandler.readOurArchonStatus(0) == CommsHandler.ArchonStatus.STANDBY_ODD) {
            myArchonNum = 1;
        }
        if (commsHandler.readOurArchonStatus(1) == CommsHandler.ArchonStatus.STANDBY_ODD) {
            myArchonNum = 2;
        }
        if (commsHandler.readOurArchonStatus(2) == CommsHandler.ArchonStatus.STANDBY_ODD) {
            myArchonNum = 3;
        }
    }

    @Override
    public void archonStatusCheck() throws GameActionException {
        boolean odd = rc.getRoundNum() % 2 == 1;
        if (currentRound > 1) {
            switch (myArchonNum) {
                case 0:
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler
                                .readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler
                                .readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler
                                .readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 1:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler
                                .readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler
                                .readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler
                                .readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 2:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler
                                .readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler
                                .readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler
                                .readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN
                                        : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 3:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler
                                .readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler
                                .readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler
                                .readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD
                                        : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    break;
                default:
                    break;
            }
            lastArchon = false;
            if (myArchonNum == 3)
                lastArchon = true;
            else if (myArchonNum == 2 && !archonThreeAlive)
                lastArchon = true;
            else if (myArchonNum == 1 && !archonTwoAlive && !archonThreeAlive)
                lastArchon = true;
            else if (myArchonNum == 0 && !archonOneAlive && !archonTwoAlive && !archonThreeAlive)
                lastArchon = true;
        }

        numOurArchonsAlive = rc.getArchonCount();

        // System.out.println("Archon survival: " + archonZeroAlive + " " +
        // archonOneAlive + " " + archonTwoAlive + " " + archonThreeAlive);

        // if (lastArchon) {
        // System.out.println("I am archon " + myArchonNum + " and I am the last
        // archon!");
        // }

        int newStatus = odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN;
        // System.out.println("Writing status " + newStatus + " on archon " +
        // myArchonNum);
        commsHandler.writeOurArchonStatus(myArchonNum, newStatus);
        commsHandler.writeOurArchonLocation(myArchonNum, myLocation);
    }

}
