package zeus;

import battlecode.common.*;

public class Soldier extends Robot {

    SoldierPathing pather;

    public Soldier(RobotController rc) throws GameActionException {
        super(rc);
        pather = new SoldierPathing(rc);
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

    @Override
    public void pathTo(MapLocation target) throws GameActionException {
        if (myLocation.distanceSquaredTo(target) <= 2) {
            if (!rc.isLocationOccupied(target) && rc.senseRubble(target) < 30) {
                if (rc.canMove(myLocation.directionTo(target))) {
                    move(myLocation.directionTo(target));
                }
            }
            return;
        }
        Direction dir = pather.bestDir(target);
        if (dir == null) { // should only happen if the area is majorly congested or something
            fuzzyMove(target);
        } else {
            if (rc.canMove(dir)) move(dir);
        }
    }

    /**
     * Chases nearest enemy or moves on exploration path
     * @throws GameActionException
     */
    public void move() throws GameActionException {
        // Flee back to archon to heal
        if (baseRetreat()) {
            return;
        }
        // Combat move. Kites enemy soldiers if harassing, otherwise pushes
        else if (nearbyEnemies.length > 0) {
            combatMove();
        }
        else {
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
            
            if (destination != null) {
                rc.setIndicatorLine(myLocation, destination, 100 - rc.getTeam().ordinal() * 100, 50, rc.getTeam().ordinal() * 100);
                pathTo(destination);
            }
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
                    if ((enemy.type == RobotType.WATCHTOWER && enemy.mode == RobotMode.TURRET 
                            || enemy.type == RobotType.SOLDIER
                            || enemy.type == RobotType.SAGE)
                        && moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.actionRadiusSquared) {
                        double enemyRubbleFactor = 10 / (10.0 + rc.senseRubble(enemy.location));
                        score -= enemy.type.getDamage(enemy.level) * enemyRubbleFactor / myRubbleFactor;
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
                // System.out.println(myLocation + " " + dir + " " + score);
                if (score > optimalScore) {
                    optimalDirection = dir;
                    optimalScore = score;
                }
            }
        }
        if (optimalDirection != null && optimalDirection != Direction.CENTER) {
            // System.out.println(myLocation + " Move: " + optimalDirection + " " + optimalScore);
            rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
            fuzzyMove(myLocation.add(optimalDirection));
        }
    }
}