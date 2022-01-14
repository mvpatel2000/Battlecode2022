package ares;

import battlecode.common.*;

public class Builder extends Robot {

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        buildOrHealOrUpgrade();

        move();
        
        // Try to act again if we didn't before moving
        buildOrHealOrUpgrade();
    }

    /**
     * Repair existing units or build a new one if threatened and nothing to repair. Prioritize
     * completing prototype units over repairing existing ones.
     * @throws GameActionException
     */
    public void buildOrHealOrUpgrade() throws GameActionException {
        if (!rc.isActionReady()) {
            return;
        }
        // Heal nearby buildings
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        MapLocation repairLocation = null;
        int remainingHealth = Integer.MAX_VALUE;
        for (RobotInfo ally : allies) {
            // Prioritize finishing prototypes
            int allyHealth = ally.getMode() == RobotMode.PROTOTYPE ? ally.getType().getMaxHealth(1) - ally.health - 1000 : ally.health;
            if (rc.canRepair(ally.location) && allyHealth < remainingHealth && allyHealth != ally.getType().getMaxHealth(ally.level)) {
                repairLocation = ally.location;
                remainingHealth = allyHealth;
            }
        }
        if (repairLocation != null && rc.canRepair(repairLocation)) {
            rc.repair(repairLocation);
        }
        // Build watchtower if in danger and didn't heal
        if (rc.isActionReady()) {
            if (nearbyEnemies.length > 0 && rc.getTeamLeadAmount(allyTeam) >= RobotType.WATCHTOWER.buildCostLead) {
                Direction optimalDir = null;
                int optimalRubble = Integer.MAX_VALUE;
                for (Direction dir : directionsWithoutCenter) {
                    if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                        int rubble = rc.senseRubble(myLocation.add(dir));
                        if (rubble < optimalRubble) {
                            optimalDir = dir;
                            optimalRubble = rubble;
                        }
                    }
                }
                if (optimalDir != null && rc.canBuildRobot(RobotType.WATCHTOWER, optimalDir)) {
                    rc.buildRobot(RobotType.WATCHTOWER, optimalDir);
                }
            }
        }
        // Upgrade watchtower if lots of resources
        if (rc.isActionReady() && rc.getTeamLeadAmount(allyTeam) - rc.getTeamLeadAmount(enemyTeam) > 5000) {
            allies = rc.senseNearbyRobots(2, allyTeam); // Can only mutate adjacent buildings

            int buildingsAroundMe = 0;
            for (RobotInfo ally : allies) {
                if (ally.type == RobotType.WATCHTOWER) {
                    buildingsAroundMe++;
                }
            }
            if (buildingsAroundMe == 0) {
                return;
            }

            MapLocation mutateLocation = null;
            int optimalRubble = Integer.MAX_VALUE;
            for (RobotInfo ally : allies) {
                int rubble = rc.senseRubble(ally.location);
                if (rc.canMutate(ally.location) && rubble < optimalRubble) {
                    mutateLocation = ally.location;
                    optimalRubble = rubble;
                }
            }
            if (mutateLocation != null && rc.canMutate(mutateLocation)) {
                rc.mutate(mutateLocation);
            }
        }
    }

    public void move() throws GameActionException {
        // Flee back to archon to heal
        if (baseRetreat()) {
            return;
        }

        // Don't move if you're finishing building a watchtower
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        for (RobotInfo ally : allies) {
            if (ally.getMode() == RobotMode.PROTOTYPE) {
                return;
            }
        }

        // Navigate to nearest found enemy
        int nearestCluster = getNearestCombatCluster();
        if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
            resetControlStatus(destination);
            destination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                            clusterCentersY[nearestCluster / clusterWidthsLength]);
        }
        // Explore map. Get new cluster if not in explore mode or close to destination
        else if (!exploreMode || myLocation.distanceSquaredTo(destination) <= 8) {
            nearestCluster = getNearestExploreCluster();
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                destination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]);
            }
        }

        // rc.setIndicatorLine(myLocation, destination, 0, 0, 255);
        if (destination != null) {
            fuzzyMove(destination);
        }
    }
}