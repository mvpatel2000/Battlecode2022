package smite;

import battlecode.common.*;

public class Soldier extends Robot {

    final double GAMMA = 0.9;

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
                repairPerTurn = (2*ally.level) * 10 / (10.0 + rc.senseRubble(archonLocation));
            }
        }
        // boolean isPositionReinforced = rc.getHealth() > 30 && ((archonLocation != null) || (combatAllies - nearbyEnemies.length >= 2));
        boolean isPositionReinforced = archonLocation != null && rc.getHealth() > 10;

        Direction optimalDirection = null;
        double optimalScore = -999.0;
        for (Direction dir : directionsWithCenter) {
            if (rc.canMove(dir) || dir == Direction.CENTER) {
                MapLocation moveLocation = myLocation.add(dir);
                if (!rc.onTheMap(moveLocation)) {
                    continue;
                }
                double myRubbleFactor = 10 / (10.0 + rc.senseRubble(moveLocation));
                // Include archon repair benefit
                double score = 0;
                if (archonLocation != null 
                    && myLocation.distanceSquaredTo(archonLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                    score += repairPerTurn;
                }
                double enemyCombatHealth = 0.0;
                double distToNearestEnemy = 1000000.1;
                boolean canAttack = false;
                boolean canView = false;
                for (RobotInfo enemy : nearbyEnemies) {
                    // Penalize by their damage per turn times how long I will be there
                    if (!isPositionReinforced &&
                            (enemy.type == RobotType.WATCHTOWER && enemy.mode == RobotMode.TURRET 
                            || enemy.type == RobotType.SOLDIER
                            || enemy.type == RobotType.SAGE)) {
                        double enemyRubbleFactor = 10 / (10.0 + rc.senseRubble(enemy.location));
                        enemyCombatHealth += enemy.getHealth() * enemyRubbleFactor;
                        double enemyDist = myLocation.distanceSquaredTo(enemy.location);
                        if (enemyDist < distToNearestEnemy) {
                            distToNearestEnemy = enemyDist;
                        }
                        // They can hit me, full points off
                        if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.actionRadiusSquared) {
                            score -= enemy.type.getDamage(enemy.level) * enemyRubbleFactor;
                            // //System.out.println\("  hit: " + (-enemy.type.getDamage(enemy.level) * enemyRubbleFactor));
                        }
                        // They can see me. If they step in, I can start shooting but they can too, so normalize by rubble
                        else if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.visionRadiusSquared) {
                            score -= GAMMA * enemy.type.getDamage(enemy.level) * enemyRubbleFactor;
                            // //System.out.println\("  view: " + (-enemy.type.getDamage(enemy.level) * enemyRubbleFactor) + " " + enemy.location);
                            canView = true;
                        }
                    }
                    // Factor in enemy archon repair
                    if (!isPositionReinforced && enemy.type == RobotType.ARCHON && nearbyEnemies.length > 1) {
                        double enemyRubbleFactor = 10 / (10.0 + rc.senseRubble(enemy.location));
                        score -= enemy.level * 2 * enemyRubbleFactor;
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
                if (canAttack || canView) {
                    // //System.out.println\("  Shoot: " + (RobotType.SOLDIER.damage * myRubbleFactor));
                    double viewOnlyMultiplier = canAttack ? 1.0 : GAMMA;
                    score += RobotType.SOLDIER.damage * myRubbleFactor  * viewOnlyMultiplier;
                    // Pursue if higher health, otherwise flee
                    // if (distToNearestEnemy < 1000000.0) {
                    //     // //System.out.println\("Chase: " + ((rc.getHealth() * myRubbleFactor - enemyCombatHealth) * distToNearestEnemy /10000.0));
                    //     score += (rc.getHealth() * myRubbleFactor - enemyCombatHealth) * distToNearestEnemy /10000.0;
                    // }
                }
                // Tiebreaker
                if (dir == Direction.CENTER) {
                    score += 0.00001;
                }
                // //System.out.println\(myLocation + " " + dir + " " + score);
                // Add rubble movement factor, often serves as a tiebreak for flee
                score += myRubbleFactor;
                if (score > optimalScore) {
                    optimalDirection = dir;
                    optimalScore = score;
                }
            }
        }
        if (optimalDirection != null && optimalDirection != Direction.CENTER) {
            //rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
            // //System.out.println\("Moving!: " + myLocation + " -> " + myLocation.add(optimalDirection));
            pathing.move(optimalDirection);
        }
    }
}