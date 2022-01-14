package zeus;

import javax.management.loading.MLet;

import battlecode.common.*;

public class Archon extends Robot {

    int myArchonNum = -1;

    // my build counts (not all archons, just me)
    int numMinersBuilt = 0;
    int numSoldiersBuilt = 0;
    int numSagesBuilt = 0;
    int numBuildersBuilt = 0;

    MapLocation optimalResourceBuildLocation;

    int resourcesOnMap;

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
        computeArchonNum();
        if (myArchonNum == 0) {
            commsHandler.initPriorityClusters();
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
        // if (currentRound > 25) {
        //     rc.resign();
        // }
        if (currentRound == 2) {
            setInitialExploreClusters();
        }

        setPriorityClusters();

        build();
        repair();
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

        // Preserve combat clusters which still have enemies
        while (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS) {
            int cluster = commsHandler.readCombatClusterIndex(combatClusterIndex);
            if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                break;
            }
            if (commsHandler.readClusterControlStatus(cluster) != CommsHandler.ControlStatus.THEIRS) {
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
            if (commsHandler.readClusterResourceCount(cluster) == 0) {
                break;
            }
            mineClusterIndex++;
        }
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

        // Alternate sweeping each half of the clusters every turn
        int startIdx = 0;
        int endIdx = numClusters;

        for (int i = startIdx; i < endIdx; i++) {
            int controlStatus = commsHandler.readClusterControlStatus(i);
            int resourceCount = commsHandler.readClusterResourceCount(i);
            resourcesOnMap += resourceCount * 100;
            // Combat cluster
            if (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS 
                && controlStatus == CommsHandler.ControlStatus.THEIRS) {
                commsHandler.writeCombatClusterIndex(combatClusterIndex, i);
                combatClusterIndex++;

                // Preserve combat clusters which still have enemies
                while (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS) {
                    int cluster = commsHandler.readCombatClusterIndex(combatClusterIndex);
                    if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                        break;
                    }
                    if (commsHandler.readClusterControlStatus(cluster) != CommsHandler.ControlStatus.THEIRS) {
                        break;
                    }
                    combatClusterIndex++;
                }
            }
            // Mine cluster
            if (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS && resourceCount > 0) {
                commsHandler.writeMineClusterAll(mineClusterIndex, i + (resourceCount << 7));
                mineClusterIndex++;

                // Preserve mining clusters which still have resources
                while (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                    int cluster = commsHandler.readMineClusterIndex(mineClusterIndex);
                    if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                        break;
                    }
                    if (commsHandler.readClusterResourceCount(cluster) == 0) {
                        break;
                    }
                    mineClusterIndex++;
                }
            }
            // Explore cluster
            if (exploreClusterIndex < commsHandler.EXPLORE_CLUSTER_SLOTS
                    && controlStatus == CommsHandler.ControlStatus.UNKNOWN) {
                commsHandler.writeExploreClusterAll(exploreClusterIndex, i + (CommsHandler.ClaimStatus.UNCLAIMED << 7));
                exploreClusterIndex++;

                /// Preserve explore clusters which still have not been claimed
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

        // rc.setIndicatorString(combatClusterIndex + " " + mineClusterIndex + " " + exploreClusterIndex);
    }

    /**
     * Builds a unit
     * @throws GameActionException
     */
    public void build() throws GameActionException {
        // temporary solution to starvation
        double passThreshold = (1 / (double) (numOurArchons - myArchonNum)) + (rc.getTeamLeadAmount(allyTeam) / 1000.0);
        boolean pass = rng.nextDouble() > passThreshold;
        if (pass) {
            return;
        }
        
        RobotType toBuild = RobotType.SOLDIER;
        int initialMiners = ((mapHeight * mapWidth / 200) + 2) / numOurArchons; // 20x20: 4 total; 60x60: 20 total
        if (numMinersBuilt < initialMiners) {
            toBuild = RobotType.MINER;
        } else if (numMinersBuilt < rc.getRobotCount() / (5 * numOurArchons)) { // account for lost archons?
            toBuild = RobotType.MINER;
        }

        // Build builders if lots of lead for watchtowers
        if (rc.getTeamLeadAmount(allyTeam) > 500 && rng.nextDouble() < 0.3) {
            toBuild = RobotType.BUILDER;
        }

        // override: if there is a visible enemy archon or soldier, build a soldier
        if (nearbyEnemies.length > 0) {
            for (RobotInfo enemy : nearbyEnemies) {
                if (enemy.type == RobotType.SOLDIER || enemy.type == RobotType.ARCHON) {
                    toBuild = RobotType.SOLDIER;
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
        if (optimalDir != null && rc.canBuildRobot(toBuild, optimalDir)) {
            buildRobot(toBuild, optimalDir);
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
     * If we didn't build a unit, repair a damaged nearby one
     * @throws GameActionException
     */
    public void repair() throws GameActionException {
        if (rc.isActionReady()) {
            RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.ARCHON.actionRadiusSquared, allyTeam);
            MapLocation optimalRepair = null;
            int remainingHealth = Integer.MAX_VALUE;
            for (RobotInfo ally : nearbyAllies) {
                if (rc.canRepair(ally.location) && ally.health < remainingHealth) {
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
        if (commsHandler.readOurArchonStatus(0) == 1) {
            myArchonNum = 1;
        } if (commsHandler.readOurArchonStatus(1) == 1) {
            myArchonNum = 2;
        } if (commsHandler.readOurArchonStatus(2) == 1) {
            myArchonNum = 3;
        }
        System.out.println("I am archon number " + myArchonNum);
        commsHandler.writeOurArchonStatus(myArchonNum, 1);
        commsHandler.writeOurArchonLocation(myArchonNum, myLocation);
    }
}
