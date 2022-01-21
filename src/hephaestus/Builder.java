package hephaestus;

import battlecode.common.*;

public class Builder extends Robot {

    int fleeingCounter;
    MapLocation lastEnemyLocation;
    
    int reservedLead = 0;
    int reservedGold = 0;

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        announceAlive();

        buildOrHealOrUpgrade();

        move();
        
        // Try to act again if we didn't before moving
        buildOrHealOrUpgrade();
    }

    public void announceAlive() throws GameActionException {
        int currBuilders = commsHandler.readWorkerCountBuilders();
        if (currBuilders < 254) {
            commsHandler.writeWorkerCountBuilders(currBuilders + 1);
        }
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
        if (reservedLead > 0) {
            commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() - reservedLead);
            reservedLead = 0;
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
        // priority build watchtower if in danger and didn't heal
        if (rc.isActionReady()) {
            if (nearbyEnemies.length > 0) {
                reservedLead = RobotType.WATCHTOWER.buildCostLead / LEAD_RESERVE_SCALE;
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
                if (optimalDir != null
                    && rc.getTeamLeadAmount(allyTeam) >= RobotType.WATCHTOWER.buildCostLead + commsHandler.readReservedResourcesLead() * LEAD_RESERVE_SCALE
                    && rc.canBuildRobot(RobotType.WATCHTOWER, optimalDir)) {
                    rc.buildRobot(RobotType.WATCHTOWER, optimalDir);
                    reservedLead = 0;
                } else {
                    if (reservedLead > 0) { // can't build now, reserve resources for it
                        if (commsHandler.readReservedResourcesLead() + reservedLead < 1024) {
                            commsHandler.writeReservedResourcesLead(commsHandler.readReservedResourcesLead() + reservedLead);
                        } else {
                            reservedLead = 0;
                        }
                    }
                }
            }
        }
        // Upgrade watchtower or archon if lots of resources
        if (rc.isActionReady() && rc.getTeamLeadAmount(allyTeam) - rc.getTeamLeadAmount(enemyTeam) > 1000) {
            allies = rc.senseNearbyRobots(2, allyTeam); // Can only mutate adjacent buildings

            int buildingsAroundMe = 0;
            for (RobotInfo ally : allies) {
                if (ally.type == RobotType.WATCHTOWER || ally.type == RobotType.ARCHON) {
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
        // if (!rc.isMovementReady()) {
        //     return;
        // }
        // Flee back to archon to heal
        if (baseRetreat()) {
            return;
        }

        MapLocation nearestCombatEnemy = null;
        int distanceToEnemy = Integer.MAX_VALUE;
        int maxScan = Math.min(nearbyEnemies.length, 10);
        for (int i = 0; i < maxScan; i++) {
            RobotInfo enemy = nearbyEnemies[i];
            if (enemy.getType() == RobotType.SOLDIER || enemy.getType() == RobotType.SAGE 
                    || enemy.getType() == RobotType.WATCHTOWER) {
                int dist = myLocation.distanceSquaredTo(enemy.location);
                if (dist < distanceToEnemy) {
                    nearestCombatEnemy = enemy.location;
                    distanceToEnemy = dist;
                }
            }
        }
        if (nearestCombatEnemy != null) { 
            lastEnemyLocation = nearestCombatEnemy;
            fleeingCounter = 2;
        }

        // Kite enemy unit
        if (fleeingCounter > 0) {
            Direction away = myLocation.directionTo(lastEnemyLocation).opposite();
            MapLocation fleeDirection = myLocation.add(away).add(away).add(away).add(away).add(away);
            pathing.fuzzyMove(fleeDirection);
            // rc.setIndicatorLine(myLocation, fleeDirection, 255, 0, 0);
            fleeingCounter--;
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
            resetControlStatus(pathing.destination);
            pathing.updateDestination(new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                            clusterCentersY[nearestCluster / clusterWidthsLength]));
        }
        // Explore map. Get new cluster if not in explore mode or close to destination
        else if (!exploreMode || myLocation.distanceSquaredTo(pathing.destination) <= 8) {
            nearestCluster = getNearestExploreCluster();
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                pathing.updateDestination(new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]));
            }
        }

        pathing.pathToDestination();
    }
}