package hermes;

import battlecode.common.*;

public class Archon extends Robot {

    int myArchonNum = -1;

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        // if (currentRound > 25) {
        //     rc.resign();
        // }
        
        if (currentRound <= 3) { // temporary fix to round 1 TLE
            computeArchonNum();
        }

        // Prepare comms by wiping shortlist
        if (currentRound == 2) {
            commsHandler.clearShortlist();
        }
        setPriorityClusters();

        build();
        repair();
    }

    /**
     * Sets the priority clusters list
     * @throws GameActionException
     */
    public void setPriorityClusters() throws GameActionException {
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
        int mode = (myArchonNum + currentRound) % 3; 
        int startIdx = 0;
        int endIdx = 0;
        switch (mode) {
            case 0:
                startIdx = 0;
                endIdx = numClusters / 3;
                break;
            case 1:
                startIdx = numClusters / 3;
                endIdx = numClusters * 2 / 3;
                break;
            case 2:
                startIdx = numClusters * 2 / 3;
                endIdx = numClusters;
                break;
            default:
                System.out.println("[Error] Unexpected case in setPriorityQueue!");
        }

        for (int i = startIdx; i < endIdx; i++) {
            int controlStatus = commsHandler.readClusterControlStatus(i);
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
            if (mineClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                int resourceCount = commsHandler.readClusterResourceCount(i);
                if (resourceCount > 0) {
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

        boolean shouldBuildMiner = turnCount < 20 ? true : (turnCount < 100 ? rng.nextDouble() < (0.2 + 0.0001 * mapHeight * mapWidth) : rng.nextDouble() < (0.05 + 0.00005 * mapHeight * mapWidth));
        RobotType toBuild = shouldBuildMiner ? RobotType.MINER : RobotType.SOLDIER;
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

        Direction optimalDir = null;
        int optimalRubble = Integer.MAX_VALUE;
        for (Direction dir : directionsWithoutCenter) {
            if (rc.canBuildRobot(toBuild, dir)) {
                int rubble = rc.senseRubble(myLocation.add(dir));
                if (rubble < optimalRubble) {
                    optimalDir = dir;
                    optimalRubble = rubble;
                }
            }
        }
        if (optimalDir != null && rc.canBuildRobot(toBuild, optimalDir)) {
            rc.buildRobot(toBuild, optimalDir);
        }
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
