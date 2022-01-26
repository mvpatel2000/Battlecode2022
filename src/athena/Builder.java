package athena;

import battlecode.common.*;

public class Builder extends Robot {

    int fleeingCounter;
    MapLocation lastEnemyLocation;
    
    int reservedLead = 0;
    int reservedGold = 0;

    boolean mainBuilder = false;
    int builderRequest = -1;
    boolean leadFarmSacrifice = false;

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        announceAlive();

        determinePurpose();

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

    public void determinePurpose() throws GameActionException {
        mainBuilder = commsHandler.readWorkerCountBuilders() == 1; // first builder is the main one
        leadFarmSacrifice = !mainBuilder; // everyone else is a sacrifice
        
        if (mainBuilder) {
            builderRequest = commsHandler.readBuilderRequestType();
            rc.setIndicatorString("Main builder; no requests");
            if (builderRequest != CommsHandler.BuilderRequest.NONE) {
                MapLocation builderRequestLocation = new MapLocation(commsHandler.readBuilderRequestXCoord(), commsHandler.readBuilderRequestYCoord());
                pathing.updateDestination(builderRequestLocation);
                rc.setIndicatorString("Main builder; request " + builderRequest + " at " + builderRequestLocation);
            }
        }
        else {
            rc.setIndicatorString("Lead farm sacrifice");
        }
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

        boolean shouldMutate = rc.getTeamLeadAmount(allyTeam) >= 500;

        // Look for nearby buildings to heal
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        MapLocation repairLocation = null;
        int remainingHealth = Integer.MAX_VALUE;
        boolean foundLabPrototype = false;
        MapLocation mutateLocation = null;
        for (RobotInfo ally : allies) {
            // Prioritize finishing prototypes
            int allyHealth = ally.getMode() == RobotMode.PROTOTYPE ? ally.getType().getMaxHealth(1) - ally.health - 1000 : ally.health;
            if (rc.canRepair(ally.location) && allyHealth < remainingHealth && allyHealth != ally.getType().getMaxHealth(ally.level)) {
                repairLocation = ally.location;
                remainingHealth = allyHealth;
                foundLabPrototype = foundLabPrototype | (ally.getMode() == RobotMode.PROTOTYPE && ally.getType() == RobotType.LABORATORY);
            }
            if (shouldMutate && rc.canMutate(ally.location)) {
                mutateLocation = ally.location;
            }
        }

        // main builder stays away from the action
        if (mainBuilder) {
            // repair lab prototype if possible
            if (foundLabPrototype && repairLocation != null && rc.canRepair(repairLocation)) {
                rc.repair(repairLocation);
                return;
            }
            // mutate if we have lots of resources and we can mutate level 1 to level 2
            else if (shouldMutate && mutateLocation != null && rc.canMutate(mutateLocation)) {
                rc.mutate(mutateLocation);
                return;
            }

            if (builderRequest == CommsHandler.BuilderRequest.NONE) {
                // make a lab if possible
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
                    return;
                }
            } else { // we've been requested to upgrade something
                if (myLocation.distanceSquaredTo(pathing.destination) <= 2) { // if we are adjacent to the lab to upgrade
                    if (rc.canMutate(pathing.destination)) {
                        rc.mutate(pathing.destination);
                        return;
                    }
                }
            }
        }

        // heal a nearby building if we can
        if (repairLocation != null && rc.canRepair(repairLocation)) {
            rc.repair(repairLocation);
        }
        // mutate if we have lots of resources and we can mutate level 1 to level 2
        else if (shouldMutate && mutateLocation != null && rc.canMutate(mutateLocation)) {
            rc.mutate(mutateLocation);
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
        
        // Lead farmers move to adjacent tile without lead and disintegrate
        if (leadFarmSacrifice) {
            int bestRubble = 99999;
            Direction bestDir = null;
            // if I'm adjacent to a low rubble tile with no lead and I can move there, move there and disintegrate
            for (Direction d : directionsWithoutCenter) {
                MapLocation moveLocation = myLocation.add(d);
                if (rc.canSenseLocation(moveLocation)) {
                    int rubble = rc.senseRubble(moveLocation);
                    if (rc.canMove(d) && rubble < bestRubble && rc.senseLead(moveLocation) == 0) {
                        bestRubble = rubble;
                        bestDir = d;
                    }
                }
            }
            if (bestDir != null) {
                rc.move(bestDir);
                rc.disintegrate();
                return;
            }

        }

        // Calculations for kiting
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

        // Hold ground if you're finishing building a prototype
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        for (RobotInfo ally : allies) {
            if (ally.getMode() == RobotMode.PROTOTYPE) {
                return;
            }
        }

        int nearestCluster = getNearestCombatCluster();
        // Main builder moves away from combat if it has no upgrades pending
        if (mainBuilder && builderRequest == CommsHandler.BuilderRequest.NONE) {
            resetControlStatus(pathing.destination);
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                pathing.updateDestination(new MapLocation(20 * ourArchonCentroid.x - 19 * clusterCentersX[nearestCluster % clusterWidthsLength],
                                                20 * ourArchonCentroid.y - 19 * clusterCentersY[nearestCluster / clusterWidthsLength]));
            } else {
                pathing.updateDestination(new MapLocation(20 * myLocation.x - 19 * ((mapWidth - 1)/2),
                                                20 * myLocation.y - 19 * ((mapHeight - 1)/2)));
            }
        }
        
        else if (leadFarmSacrifice) {
            // Farmer moves towards combat or towards center
            // if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
            //     resetControlStatus(pathing.destination);
            //     pathing.updateDestination(new MapLocation((clusterCentersX[nearestCluster % clusterWidthsLength] + ourArchonCentroid.x)/2, 
            //                                     (clusterCentersY[nearestCluster / clusterWidthsLength] + ourArchonCentroid.y)/2));
            // } else {
            //     pathing.updateDestination(new MapLocation((mapWidth - 1 + ourArchonCentroid.x)/2, (mapHeight - 1 + ourArchonCentroid.y)/2));
            // }

            // Farmer moves away from friendly units
            double xVec = 0;
            double yVec = 0;
            RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.BUILDER.visionRadiusSquared, allyTeam);
            for (RobotInfo ally : nearbyAllies) {
                xVec += -5.0 / ((myLocation.x - ally.location.x) * (myLocation.x - ally.location.x));
                yVec += -5.0 / ((myLocation.y - ally.location.y) * (myLocation.y - ally.location.y));
            }
            int xDest = Math.max(Math.min((int) (myLocation.x + xVec), mapWidth - 1), 0);
            int yDest = Math.max(Math.min((int) (myLocation.y + yVec), mapHeight - 1), 0);
            pathing.updateDestination(new MapLocation(xDest, yDest));
        }

        if (turnCount == 1) {
            pathing.cautiousGreedyMove(pathing.destination);
        } else {
            pathing.pathToDestination();
        }
    }
}