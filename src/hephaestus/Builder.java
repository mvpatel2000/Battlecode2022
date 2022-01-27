package hephaestus;

import battlecode.common.*;

public class Builder extends Robot {

    int fleeingCounter;
    MapLocation lastEnemyLocation;
    
    int reservedLead = 0;
    int reservedGold = 0;

    boolean shouldMakeLaboratory = false;
    boolean leadFarmSacrifice = false;

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        announceAlive();

        checkLabRequested();

        shouldDisintegrate();

        buildOrHealOrUpgrade();
        
        move();

        // Try to act again if we didn't before moving
        buildOrHealOrUpgrade();
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currBuilders = commsHandler.readWorkerCountBuilders();
        if (currBuilders < 254) {
            commsHandler.writeWorkerCountBuilders(currBuilders + 1);
        }
    }

    public void checkLabRequested() throws GameActionException {
        shouldMakeLaboratory = commsHandler.readWorkerCountBuilders() == 1; // temporary
        leadFarmSacrifice = !shouldMakeLaboratory;
        rc.setIndicatorString("Should make lab: " + shouldMakeLaboratory);
        // if (commsHandler.readBuilderQueueLaboratory() == CommsHandler.BuilderQueue.REQUESTED) {
        //     // System.out.println("I am going to try to make a lab");
        //     rc.setIndicatorString("Making lab");
        //     shouldMakeLaboratory = true;
        //     commsHandler.writeBuilderQueueLaboratory(CommsHandler.BuilderQueue.NONE);
        // }
        // if (!shouldMakeLaboratory) {
        //     rc.setIndicatorString("Going to battlefront");
        // }
    }

    public void shouldDisintegrate() throws GameActionException {
        // System.out.println("I see " + commsHandler.readWorkerCountBuilders() + " builders before me");
        if (leadFarmSacrifice && rc.senseLead(rc.getLocation()) == 0) {
            commsHandler.writeWorkerCountBuilders(commsHandler.readWorkerCountBuilders() - 1);
            rc.disintegrate();
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
        // if a laboratory has been requested, make one
        if (shouldMakeLaboratory) {
            // TODO: route to edge of map and place laboratory on a 0-rubble tile
            Direction optimalDir = null;
            int optimalRubble = Integer.MAX_VALUE;
            for (Direction dir : directionsWithoutCenter) {
                if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                    int rubble = rc.senseRubble(myLocation.add(dir));
                    if (rubble < optimalRubble) {
                        optimalDir = dir;
                        optimalRubble = rubble;
                    }
                }
            }
            if (optimalDir != null && rc.getTeamLeadAmount(allyTeam) >= RobotType.LABORATORY.buildCostLead) {
                rc.buildRobot(RobotType.LABORATORY, optimalDir);
                commsHandler.writeBuilderQueueLaboratory(CommsHandler.BuilderQueue.NONE);
                shouldMakeLaboratory = false;
            }
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
        // build watchtower if in danger and didn't heal
        if (rc.isActionReady() && !shouldMakeLaboratory && false) { // TODO: remove the false and eventually make watchtowers?
            if (nearbyEnemies.length > 0) {
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
        
        // temporary, for lead farming
        if (leadFarmSacrifice) {
            int bestRubble = 99999;
            Direction bestDir = null;
            // if I'm adjacent to a low rubble tile and I can move there, move there
            for (Direction d : directionsWithoutCenter) {
                if (rc.canSenseLocation(myLocation.add(d))) {
                    int rubble = rc.senseRubble(myLocation.add(d));
                    if (rc.canMove(d) && rc.senseRubble(myLocation.add(d)) < bestRubble) {
                        bestRubble = rubble;
                        bestDir = d;
                    }
                }
            }
            if (bestDir != null) {
                rc.move(bestDir);
                return;
            }
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

        int nearestCluster = getNearestCombatCluster();
        // Run away from nearest enemy if we're tasked to make a lab
        if (shouldMakeLaboratory) {
            resetControlStatus(pathing.destination);
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                pathing.updateDestination(new MapLocation(20 * myLocation.x - 19 * clusterCentersX[nearestCluster % clusterWidthsLength],
                                                20 * myLocation.y - 19 * clusterCentersY[nearestCluster / clusterWidthsLength]));
            } else {
                pathing.updateDestination(new MapLocation(20 * myLocation.x - 19 * ((mapWidth - 1)/2),
                                                20 * myLocation.y - 19 * ((mapHeight - 1)/2)));
            }
        }
        // Navigate to nearest found enemy
        else if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
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