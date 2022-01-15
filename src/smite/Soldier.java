package smite;

import battlecode.common.*;

public class Soldier extends Robot {

    public Soldier(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        announceAlive();

        attack();

        move();

        // Try to act again if we didn't before moving
        attack();

        // disintegrate();
    }

    public void announceAlive() throws GameActionException {
        commsHandler.writeSoldierCount(commsHandler.readSoldierCount() + 1);
    }

    /**
     * Disintegrate to put lead on tile if I'm almost dead, there's many friendly miners and soldiers, no enemies, and 
     * no lead on the tile currently
     * @throws GameActionException
     */
    public void disintegrate() throws GameActionException {
        if (rc.senseLead(myLocation) == 0 && rc.getHealth() <= 10) {
            if (nearbyEnemies.length == 0) {
                RobotInfo[] adjacentAllies = rc.senseNearbyRobots(2, allyTeam);
                int adjacentMiners = 0;
                int adjacentSoldiers = 0;
                for (RobotInfo ally : adjacentAllies) {
                    if (ally.type == RobotType.MINER) {
                        adjacentMiners++;
                    }
                    else if (ally.type == RobotType.SOLDIER) {
                        adjacentSoldiers++;
                    }
                }
                if (adjacentAllies.length >= 6 && adjacentMiners >= 1 && adjacentSoldiers >= 1) {
                    rc.disintegrate();
                }
            }
        }
    }

    /**
     * Chases nearest enemy or moves on exploration path
     * @throws GameActionException
     */
    public void move() throws GameActionException {
        // if (!rc.isMovementReady()) {
        //     return;
        // }
        // Flee back to archon to heal
        if (baseRetreat()) {
            return;
        }
        // Combat move. Kites enemy soldiers if harassing, otherwise pushes
        else if (nearbyEnemies.length > 0) {
            combatMove();
        }
        else {
            MapLocation newDestination = pathing.destination;
            // Navigate to nearest found enemy
            int nearestCluster = getNearestCombatCluster();
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                resetControlStatus(pathing.destination);
                newDestination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]);
            }
            // Explore map. Get new cluster if not in explore mode or close to destination
            else if (!exploreMode || myLocation.distanceSquaredTo(pathing.destination) <= 8) {
                nearestCluster = getNearestExploreCluster();
                if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    newDestination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                    clusterCentersY[nearestCluster / clusterWidthsLength]);
                }
            }
            
            pathing.updateDestination(newDestination);
            pathing.pathToDestination();
        }
    }

    /**
     * Move in combat
     * @throws GameActionException
     */
    public void combatMove() throws GameActionException {
        int combatAllies = 0;
        MapLocation archonLocation = null;
        double repairPerTurn = 0;
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, allyTeam);
        for (RobotInfo ally : allies) {
            if (ally.type == RobotType.WATCHTOWER || ally.type == RobotType.SOLDIER) {
                combatAllies++;
            }
            else if (ally.type == RobotType.ARCHON) {
                archonLocation = ally.location;
                // Repair normalized to per turn by rubble
                repairPerTurn = (ally.level + 1) * 10 / (10.0 + rc.senseRubble(archonLocation));
            }
        }
        boolean holdGround = (archonLocation != null) || (combatAllies - nearbyEnemies.length >= 1);

        Direction optimalDirection = null;
        double optimalScore = Double.MIN_VALUE;
        for (Direction dir : directionsWithCenter) {
            if (rc.canMove(dir) || dir == Direction.CENTER) {
                MapLocation moveLocation = myLocation.add(dir);
                if (!rc.onTheMap(moveLocation)) {
                    continue;
                }
                double myRubbleFactor = 10 / (10.0 + rc.senseRubble(moveLocation));
                // Include archon repair benefit
                int score = 0;
                if (archonLocation != null 
                    && myLocation.distanceSquaredTo(archonLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                    score += repairPerTurn;
                }
                boolean canAttack = false;
                for (RobotInfo enemy : nearbyEnemies) {
                    // Penalize by their damage per turn times how long I will be there
                    if (enemy.type == RobotType.WATCHTOWER && enemy.mode == RobotMode.TURRET 
                            || enemy.type == RobotType.SOLDIER
                            || enemy.type == RobotType.SAGE) {
                        double enemyRubbleFactor = 10 / (10.0 + rc.senseRubble(enemy.location));
                        // They can hit me, full points off
                        if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.actionRadiusSquared) {
                            score -= enemy.type.getDamage(enemy.level) * enemyRubbleFactor / myRubbleFactor;
                        }
                        // They can see me / step in and attack me, half points off
                        else if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.visionRadiusSquared) {
                            score -= 0.5 * enemy.type.getDamage(enemy.level) * enemyRubbleFactor / myRubbleFactor;
                        }
                    }
                    // See if you can attack anyone
                    if (moveLocation.distanceSquaredTo(enemy.location) <= RobotType.SOLDIER.actionRadiusSquared) {
                        canAttack = true;
                    }
                    // TODO: Stop moving around archon?
                    // TODO: Encourage moving towards targets?
                    // TODO: Points for being able to kill (finish off) someone?
                    // TODO: Don't flee into high rubble terrain?
                    // TODO: encourage aggressiveness if outnumbering?
                    // TODO: pursue enemy if low hp
                }
                // Add damage normalized to per turn by rubble
                if (canAttack) {
                    score += RobotType.SOLDIER.damage * myRubbleFactor;
                }
                // Tiebreak in favor of not moving
                if (dir == Direction.CENTER) {
                    score += 0.01;
                }
                if (score > optimalScore) {
                    optimalDirection = dir;
                    optimalScore = score;
                }
            }
        }
        if (optimalDirection != null && optimalDirection != Direction.CENTER) {
            //rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
            pathing.move(optimalDirection);
        }
    }
}