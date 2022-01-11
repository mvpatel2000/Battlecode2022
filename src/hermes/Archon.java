package hermes;

import battlecode.common.*;

public class Archon extends Robot {

    int myArchonNum = -1;

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        // if (currentRound > 250) {
        //     rc.resign();
        // }
        if (currentRound <= 3) { // temporary fix to round 1 TLE
            computeArchonNum();
        }
        mainLoop();
    }

    public void mainLoop() throws GameActionException {
        setCompressedClusters();

        build();
        repair();
    }

    /**
     * Sets the compressed clusters list
     * @throws GameActionException
     */
    public void setCompressedClusters() throws GameActionException {
        int combatClusterIndex = 0;
        int miningClusterIndex = 0;

        // Preserve mining clusters which still have resources
        while (miningClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
            int cluster = commsHandler.readMineClusterIndex(miningClusterIndex);
            if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                break;
            }
            int oldResourceCount = commsHandler.readClusterResourceCount(cluster);
            if (oldResourceCount == 0) {
                break;
            }
            miningClusterIndex++;
        }

        for (int i = 0; i < numClusters; i++) {
            if (combatClusterIndex < commsHandler.COMBAT_CLUSTER_SLOTS && commsHandler.readClusterControlStatus(i) == 2) {
                commsHandler.writeCombatClusterIndex(combatClusterIndex, i);
                combatClusterIndex++;
            }
            if (miningClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                int resourceCount = commsHandler.readClusterResourceCount(i);
                if (resourceCount > 0) {
                    commsHandler.writeMineClusterIndex(miningClusterIndex, i);
                    commsHandler.writeMineClusterClaimStatus(miningClusterIndex, resourceCount);
                    miningClusterIndex++;

                    // Preserve mining clusters which still have resources
                    while (miningClusterIndex < commsHandler.MINE_CLUSTER_SLOTS) {
                        int cluster = commsHandler.readMineClusterIndex(miningClusterIndex);
                        if (cluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                            break;
                        }
                        int oldResourceCount = commsHandler.readClusterResourceCount(cluster);
                        if (oldResourceCount == 0) {
                            break;
                        }
                        miningClusterIndex++;
                    }
                }
            }
        }
    }

    /**
     * Builds a unit
     * @throws GameActionException
     */
    public void build() throws GameActionException {
        // temporary solution to starvation
        double passThreshold = (1 / (double) (numOurArchons - myArchonNum)) + (rc.getTeamLeadAmount(allyTeam) / 1000.0);
        System.out.println("passThreshold: " + passThreshold);
        boolean pass = rng.nextDouble() > passThreshold;
        if (pass) {
            return;
        }

        boolean shouldBuildMiner = turnCount < 20 ? true : (turnCount < 100 ? rng.nextBoolean() : rng.nextDouble() < 0.3);
        RobotType toBuild = shouldBuildMiner ? RobotType.MINER : RobotType.SOLDIER;
        // Build builders if lots of lead for watchtowers
        if (rc.getTeamLeadAmount(allyTeam) > 500 && rng.nextDouble() < 0.3) {
            toBuild = RobotType.BUILDER;
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
