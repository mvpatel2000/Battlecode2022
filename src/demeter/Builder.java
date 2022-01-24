package demeter;

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
            rc.setIndicatorString("Main builder");
            builderRequest = commsHandler.readBuilderRequestType();
            if (builderRequest != CommsHandler.BuilderRequest.NONE) {
                MapLocation builderRequestLocation = new MapLocation(commsHandler.readBuilderRequestXCoord(), commsHandler.readBuilderRequestYCoord());
                pathing.updateDestination(builderRequestLocation);
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

        // Look for nearby buildings to heal
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        MapLocation repairLocation = null;
        int remainingHealth = Integer.MAX_VALUE;
        boolean foundLabPrototype = false;
        for (RobotInfo ally : allies) {
            // Prioritize finishing prototypes
            int allyHealth = ally.getMode() == RobotMode.PROTOTYPE ? ally.getType().getMaxHealth(1) - ally.health - 1000 : ally.health;
            if (rc.canRepair(ally.location) && allyHealth < remainingHealth && allyHealth != ally.getType().getMaxHealth(ally.level)) {
                repairLocation = ally.location;
                remainingHealth = allyHealth;
                foundLabPrototype = foundLabPrototype | (ally.getMode() == RobotMode.PROTOTYPE && ally.getType() == RobotType.LABORATORY);
            }
        }

        // main builder stays away from the action
        if (mainBuilder) {
            // repair lab prototype if possible
            if (foundLabPrototype && repairLocation != null && rc.canRepair(repairLocation)) {
                rc.repair(repairLocation);
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
                if (rc.canSenseLocation(myLocation.add(d))) {
                    int rubble = rc.senseRubble(myLocation.add(d));
                    if (rc.canMove(d) && rc.senseRubble(myLocation.add(d)) < bestRubble && rc.senseLead(myLocation.add(d)) == 0) {
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
                pathing.updateDestination(new MapLocation(20 * myLocation.x - 19 * clusterCentersX[nearestCluster % clusterWidthsLength],
                                                20 * myLocation.y - 19 * clusterCentersY[nearestCluster / clusterWidthsLength]));
            } else {
                pathing.updateDestination(new MapLocation(20 * myLocation.x - 19 * ((mapWidth - 1)/2),
                                                20 * myLocation.y - 19 * ((mapHeight - 1)/2)));
            }
        }
        // Farmer moves towards combat or towards center
        else if (leadFarmSacrifice) {
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                resetControlStatus(pathing.destination);
                pathing.updateDestination(new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]));
            } else {
                pathing.updateDestination(new MapLocation((mapWidth - 1)/2, (mapHeight - 1)/2));
            }
        }

        pathing.pathToDestination();
    }
}