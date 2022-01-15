package smite;

import battlecode.common.*;

public class Archon extends Robot {

    int myArchonNum = -1;

    // my build counts (not all archons, just me)
    int numMinersBuilt = 0;
    int numSoldiersBuilt = 0;
    int numSagesBuilt = 0;
    int numBuildersBuilt = 0;

    // current total counts of our units on the map
    int minerCount = 0;
    int soldierCount = 0;

    boolean archonZeroAlive = true;
    boolean archonOneAlive = true;
    boolean archonTwoAlive = true;
    boolean archonThreeAlive = true;
    int numOurArchonsAlive = 1;

    boolean lastArchon = false;

    MapLocation optimalResourceBuildLocation;

    int resourcesOnMap;

    int reservedLead = 0;
    int reservedGold = 0;

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

        // Get best build direction closest to resources
        Direction toResources = Direction.CENTER;
        int nearestDistance = Integer.MAX_VALUE;
        MapLocation[] resourceLocations = rc.senseNearbyLocationsWithLead(RobotType.ARCHON.visionRadiusSquared);
        int length = Math.min(resourceLocations.length, 10);
        for (int i = 0; i < length; i++) {
            MapLocation tile = resourceLocations[i];
            int distance = myLocation.distanceSquaredTo(tile);
            if (distance <= nearestDistance) {
                nearestDistance = distance;
                toResources = myLocation.directionTo(tile);
            }
        }
        optimalResourceBuildLocation = myLocation.add(toResources);
    }

    @Override
    public void runUnit() throws GameActionException {
        // if (currentRound > 45) {
        //     //rc.resign\();
        // }

        archonStatusCheck();
        updateUnitCounts();

        if (currentRound == 2) {
            setInitialExploreClusters();
        }

        setPriorityClusters();

        build();
        repair();
    }

    public void updateUnitCounts() throws GameActionException {
        minerCount = commsHandler.readMinerCount();
        soldierCount = commsHandler.readSoldierCount();

        // //System.out.println\("We currently have " + minerCount + " miners and " + soldierCount + " soldiers.");

        if (lastArchon) {
            commsHandler.writeMinerCount(0);
            commsHandler.writeSoldierCount(0);
        }
    }

    /**
     * Sets initial explore clusters
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

        int centerCluster = whichXLoc[mapWidth/2] + whichYLoc[mapHeight/2];
        int xyCluster = whichXLoc[mapWidth-myLocation.x] + whichYLoc[mapHeight-myLocation.y];
        int xCluster = whichXLoc[mapWidth-myLocation.x] + whichYLoc[myLocation.y];
        int yCluster = whichXLoc[myLocation.x] + whichYLoc[mapHeight-myLocation.y];
        int[] clusters = {centerCluster, xyCluster, xCluster, yCluster};

        for (int i = 0; i < 4; i++) {
            int cluster = clusters[i];
            int controlStatus = commsHandler.readClusterControlStatus(cluster);
            if (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS
                && controlStatus == CommsHandler.ControlStatus.UNKNOWN) {
                commsHandler.writeExploreClusterAll(exploreClusterIndex, cluster + (CommsHandler.ClaimStatus.UNCLAIMED << 7));
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
     * @throws GameActionException
     */
    public void setPriorityClusters() throws GameActionException {
        // Do not set for first turn as archons haven't all filled in data
        if (turnCount <= 1) {
            return;
        }

        resourcesOnMap = 0;
        int combatClusterIndex = 0;
        int mineClusterIndex = 0;
        int exploreClusterIndex = 0;

        // String status = "";
        // for (int i = 0; i < commsHandler.COMBAT_CLUSTER_SLOTS; i++) {
        //     int cluster = commsHandler.readCombatClusterIndex(i);
        //     status += " " + cluster + "(" + commsHandler.readClusterControlStatus(cluster) + ")";
        // }
        // //System.out.println\("Combat"+status);
        // String status = "";
        // for (int i = 0; i < commsHandler.EXPLORE_CLUSTER_SLOTS; i++) {
        //     int cluster = commsHandler.readExploreClusterIndex(i);
        //     status += " " + cluster + "(" + commsHandler.readClusterControlStatus(cluster) + "," + commsHandler.readExploreClusterClaimStatus(i) + ")";
        // }
        // //System.out.println\("Explore"+status);
        // String status = "";
        // for (int i = 0; i < commsHandler.MINE_CLUSTER_SLOTS; i++) {
        //     int cluster = commsHandler.readMineClusterIndex(i);
        //     status += " " + cluster + "(" + commsHandler.readMineClusterClaimStatus(i) + "," + commsHandler.readClusterResourceCount(cluster) + ")";
        // }
        // //System.out.println\("Mine"+status);

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
            if (resourceCount == 0) {
                commsHandler.writeMineClusterIndex(i, commsHandler.UNDEFINED_CLUSTER_INDEX);
            }
            else {
                commsHandler.writeMineClusterClaimStatus(i, resourceCount/4);
            }
        }
        // Clear explore slots
        for (int i = 0; i < commsHandler.EXPLORE_CLUSTER_SLOTS; i++) {
            int nearestClusterAll = commsHandler.readExploreClusterAll(i);
            int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
            int nearestClusterStatus = (nearestClusterAll & 128) >> 7; // 2^7
            int controlStatus = commsHandler.readClusterControlStatus(nearestCluster);
            if (nearestClusterStatus == CommsHandler.ClaimStatus.CLAIMED
                || controlStatus == CommsHandler.ControlStatus.OURS
                || controlStatus == CommsHandler.ControlStatus.THEIRS) {
                // Also resets claimed/unclaimed bit to unclaimed
                commsHandler.writeExploreClusterAll(i, commsHandler.UNDEFINED_CLUSTER_INDEX);
            }
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

        // Set priority clusters
        int startIdx = 0;
        int endIdx = numClusters;

        for (int prePermuteIdx = startIdx; prePermuteIdx < endIdx; prePermuteIdx++) {
            int i = clusterPermutation[prePermuteIdx];
            int controlStatus = commsHandler.readClusterControlStatus(i);
            int resourceCount = commsHandler.readClusterResourceCount(i);
            resourcesOnMap += resourceCount * LEAD_RESOLUTION;
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
            if (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS && resourceCount > 0) {
                // Verify cluster is not already in comms list
                boolean isValid = true;
                for (int j = 0; j < commsHandler.MINE_CLUSTER_SLOTS; j++) {
                    if (commsHandler.readMineClusterIndex(j) == i) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    commsHandler.writeMineClusterAll(mineClusterIndex, i + ((resourceCount/4) << 7));
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

        // //System.out.println\("Estimated resources on the map: " + resourcesOnMap);

        // //rc.setIndicatorString(combatClusterIndex + " " + mineClusterIndex + " " + exploreClusterIndex);
    }

    /**
     * Builds a unit
     * @throws GameActionException
     */
    public void build() throws GameActionException {
        // temporary solution to starvation
        double passThreshold = (1 / (double) (numOurArchons - myArchonNum)) + (rc.getTeamLeadAmount(allyTeam) / 1000.0);
        boolean pass = rng.nextDouble() > passThreshold;
        if (pass && reservedLead == 0 && reservedGold == 0) { // don't pass if we have already reserved some resources
            return;
        }

        // un-reserve whatever we previously reserved; we'll decide what to build independently from the past
        // //System.out.println\("Start of build, currently have reserved " + reservedLead + " lead and " + reservedGold + " gold");
        // //System.out.println\("Global reserves are " + commsHandler.readReservedResourcesLead() + " lead and " + commsHandler.readReservedResourcesGold() + " gold");
        if (reservedLead > 0) {
            commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() - reservedLead);
            reservedLead = 0;
        }
        if (reservedGold > 0) {
            commsHandler.writeReservedResourcesGold(commsHandler.readReservedResourcesGold() - reservedGold);
            reservedGold = 0;
        }
        
        RobotType toBuild = RobotType.SOLDIER;
        int initialMiners = (mapHeight * mapWidth / 200) + 2; // 20x20: 4 total; 60x60: 20 total

        if (minerCount < initialMiners) {
            toBuild = RobotType.MINER;
        } else if (minerCount < rc.getRobotCount() / (Math.max(2.5, (4.5 - resourcesOnMap/600)))) {
            toBuild = RobotType.MINER;
        }

        // TODO: actually make builders sometime?
        // // Build builders if lots of lead for watchtowers
        // if (rc.getTeamLeadAmount(allyTeam) > 500 && rng.nextDouble() < 0.3) {
        //     toBuild = RobotType.BUILDER;
        // }

        // Override: if I haven't built a miner yet, priority build one
        if (numMinersBuilt == 0) {
            toBuild = RobotType.MINER;
            reservedLead = RobotType.MINER.buildCostLead / LEAD_RESERVE_SCALE;
        }

        // Override: if there is a visible enemy archon or soldier, priority build a soldier
        if (nearbyEnemies.length > 0) {
            for (RobotInfo enemy : nearbyEnemies) {
                if (enemy.type == RobotType.SOLDIER || enemy.type == RobotType.ARCHON) {
                    toBuild = RobotType.SOLDIER;
                    reservedLead = RobotType.SOLDIER.buildCostLead / LEAD_RESERVE_SCALE; // priority build
                    // //System.out.println\("Would like to priority build a soldier");
                }
            }
        }

        // Prioritize building towards resources on low rubble
        Direction optimalDir = null;
        int optimalScore = Integer.MAX_VALUE;
        for (Direction dir : directionsWithoutCenter) {
            if (rc.canBuildRobot(toBuild, dir)) {
                MapLocation buildLocation = myLocation.add(dir);
                int score = rc.senseRubble(buildLocation) + buildLocation.distanceSquaredTo(optimalResourceBuildLocation);
                if (score < optimalScore) {
                    optimalDir = dir;
                    optimalScore = score;
                }
            }
        }

        // Either build or reserve
        if (optimalDir != null
            && rc.getTeamLeadAmount(allyTeam) >= toBuild.buildCostLead + commsHandler.readReservedResourcesLead() * LEAD_RESERVE_SCALE
            && rc.getTeamGoldAmount(allyTeam) >= toBuild.buildCostGold + commsHandler.readReservedResourcesGold() * GOLD_RESERVE_SCALE
            && rc.canBuildRobot(toBuild, optimalDir)) {
            buildRobot(toBuild, optimalDir);
            reservedLead = 0;
            reservedGold = 0;
        } else { // Can't build now, reserve resources for it
            if (reservedLead > 0) {
                // //System.out.println\("Updating global lead reserve to " + commsHandler.readReservedResourcesLead() + reservedLead);
                commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() + reservedLead);
            }
            if (reservedGold > 0) {
                // //System.out.println\("Updating global gold reserve to " + commsHandler.readReservedResourcesGold() + reservedGold);
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
     * If we didn't build a unit, repair a damaged nearby one. If there's enemies, repair the weakest
     * to keep them alive. If there aren't enemies, repair the strongest so we can send it back out 
     * @throws GameActionException
     */
    public void repair() throws GameActionException {
        if (rc.isActionReady()) {
            boolean existEnemies = nearbyEnemies.length > 0;
            RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.ARCHON.actionRadiusSquared, allyTeam);
            MapLocation optimalRepair = null;
            int remainingHealth = existEnemies ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            for (RobotInfo ally : nearbyAllies) {
                if (rc.canRepair(ally.location) 
                    && (existEnemies && ally.health < remainingHealth)
                    || !existEnemies && ally.health > remainingHealth && ally.health < ally.type.getMaxHealth(ally.level)) {
                    optimalRepair = ally.location;
                    remainingHealth = ally.health;
                }
            }
            if (optimalRepair != null && rc.canRepair(optimalRepair)) {
                rc.repair(optimalRepair);
            }
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
        } if (commsHandler.readOurArchonStatus(1) == CommsHandler.ArchonStatus.STANDBY_ODD) {
            myArchonNum = 2;
        } if (commsHandler.readOurArchonStatus(2) == CommsHandler.ArchonStatus.STANDBY_ODD) {
            myArchonNum = 3;
        }
        //System.out.println\("I am archon number " + myArchonNum);
    }

    public void archonStatusCheck() throws GameActionException {
        boolean odd = currentRound % 2 == 1;
        if (currentRound > 1) {
            switch(myArchonNum) {
                case 0:
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler.readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler.readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler.readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 1:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler.readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler.readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler.readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 2:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler.readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler.readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonThreeAlive) {
                        archonThreeAlive = commsHandler.readOurArchonStatus(3) == (odd ? CommsHandler.ArchonStatus.STANDBY_EVEN : CommsHandler.ArchonStatus.STANDBY_ODD);
                    }
                    break;
                case 3:
                    if (archonZeroAlive) {
                        archonZeroAlive = commsHandler.readOurArchonStatus(0) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonOneAlive) {
                        archonOneAlive = commsHandler.readOurArchonStatus(1) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    if (archonTwoAlive) {
                        archonTwoAlive = commsHandler.readOurArchonStatus(2) == (odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN);
                    }
                    break;
                default:
                    break;
            }
            lastArchon = false;
            if (myArchonNum == 3) lastArchon = true;
            else if (myArchonNum == 2 && !archonThreeAlive) lastArchon = true;
            else if (myArchonNum == 1 && !archonTwoAlive && !archonThreeAlive) lastArchon = true;
            else if (myArchonNum == 0 && !archonOneAlive && !archonTwoAlive && !archonThreeAlive) lastArchon = true;
        }

        numOurArchonsAlive = 0;
        if (archonZeroAlive) numOurArchonsAlive++;
        if (archonOneAlive) numOurArchonsAlive++;
        if (archonTwoAlive) numOurArchonsAlive++;
        if (archonThreeAlive) numOurArchonsAlive++;

        // //System.out.println\("Archon survival: " + archonZeroAlive + " " + archonOneAlive + " " + archonTwoAlive + " " + archonThreeAlive);

        // if (lastArchon) {
        //     //System.out.println\("I am archon " + myArchonNum + " and I am the last archon!");
        // }

        int newStatus = odd ? CommsHandler.ArchonStatus.STANDBY_ODD : CommsHandler.ArchonStatus.STANDBY_EVEN;
        // //System.out.println\("Writing status " + newStatus + " on archon " + myArchonNum);
        commsHandler.writeOurArchonStatus(myArchonNum, newStatus);
        commsHandler.writeOurArchonLocation(myArchonNum, myLocation);
    }

}
