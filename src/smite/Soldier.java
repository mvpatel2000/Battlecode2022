package smite;

import battlecode.common.*;

public class Soldier extends Robot {

    public Soldier(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        attack();

        move();

        // Try to act again if we didn't before moving
        attack();

        // disintegrate();
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
        // Flee back to archon to heal
        if (isDying) {
            destination = baseLocation;
            fuzzyMove(destination);
        }
        // Combat move. Kites enemy soldiers if harassing, otherwise pushes
        else if (nearbyEnemies.length > 0) {
            int combatAllies = 0;
            MapLocation archonLocation = null;
            RobotInfo[] allies = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, allyTeam);
            for (RobotInfo ally : allies) {
                if (ally.type == RobotType.WATCHTOWER || ally.type == RobotType.SOLDIER) {
                    combatAllies++;
                }
                else if (ally.type == RobotType.ARCHON) {
                    archonLocation = ally.location;
                }
            }
            boolean holdGround = (archonLocation != null) && (combatAllies - nearbyEnemies.length >= 2);

            Direction optimalDirection = null;
            int optimalScore = Integer.MIN_VALUE;
            for (Direction dir : directionsWithCenter) {
                if (rc.canMove(dir)) {
                    MapLocation moveLocation = myLocation.add(dir);
                    if (!rc.onTheMap(moveLocation)) {
                        continue;
                    }
                    int score = 0;
                    for (RobotInfo enemy : nearbyEnemies) {
                        // TODO: Prioritize locking up archons?
                        // Avoid enemy combat units unless holding ground (1000000, highest priority)
                        if (!holdGround 
                            && (enemy.type == RobotType.WATCHTOWER && enemy.mode == RobotMode.TURRET 
                                || enemy.type == RobotType.SOLDIER
                                || enemy.type == RobotType.SAGE)
                            && moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.actionRadiusSquared) {
                            score -= 1000000;
                        }
                        // Prioritize staying inside archon healing range (equal to 1 combat unit priority)
                        if (archonLocation != null 
                            && myLocation.distanceSquaredTo(archonLocation) <= RobotType.ARCHON.actionRadiusSquared) {
                            score += 1000000;
                        }
                        boolean canKillTarget = false;
                        // Move towards enemy units we want to kill
                        if (enemy.type == RobotType.MINER || enemy.type == RobotType.BUILDER 
                            || enemy.type == RobotType.LABORATORY || enemy.type == RobotType.ARCHON) {
                            // move towards sighted enemy units (fourth highest priority)
                            score -= moveLocation.distanceSquaredTo(enemy.location);
                            if (moveLocation.distanceSquaredTo(enemy.location) <= RobotType.SOLDIER.actionRadiusSquared) {
                                canKillTarget = true;
                            }
                        }
                        // points for being able to kill them (200000, second highest priority)
                        if (canKillTarget) {
                            score += 200000;
                        }
                    }
                    // Move to low rubble tile in combat to be able to fight faster (1000 - 100000, third highest priority)
                    score -= rc.senseRubble(moveLocation) * 1000;
                    // Tiebreak in favor of not moving
                    if (dir == Direction.CENTER) {
                        score += 1;
                    }
                    if (score > optimalScore) {
                        optimalDirection = dir;
                        optimalScore = score;
                    }
                }
            }
            if (optimalDirection != null && optimalDirection != Direction.CENTER) {
                //rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
                fuzzyMove(myLocation.add(optimalDirection));
            }
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
                // //rc.setIndicatorLine(myLocation, destination, 0, 255, 0);
                fuzzyMove(destination);
            }
        }
    }
}