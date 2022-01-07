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

        disintegrate();
    }

    /**
     * Disintegrate to put lead on tile if there's many friendly miners and soldiers, no enemies, and 
     * no lead on the tile currently
     * @throws GameActionException
     */
    public void disintegrate() throws GameActionException {
        if (rc.senseLead(myLocation) == 0) {
            if (rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, enemyTeam).length == 0) {
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
        RobotInfo[] enemies = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, enemyTeam);
        // Chase and attack nearest enemy
        if (enemies.length > 0) {
            boolean holdGround = false;
            int combatAllies = 0;
            RobotInfo[] allies = rc.senseNearbyRobots(RobotType.SOLDIER.visionRadiusSquared, allyTeam);
            for (RobotInfo ally : allies) {
                if (ally.type == RobotType.WATCHTOWER || ally.type == RobotType.SOLDIER) {
                    combatAllies++;
                }
                else if (ally.type == RobotType.ARCHON) {
                    holdGround = true;
                }
            }
            holdGround |= combatAllies >= 3;

            Direction optimalDirection = null;
            int optimalScore = 0;
            for (Direction dir : directionsWithCenter) {
                if (rc.canMove(dir)) {
                    MapLocation moveLocation = myLocation.add(dir);
                    if (!rc.onTheMap(moveLocation)) {
                        continue;
                    }
                    int score = 0;
                    for (RobotInfo enemy : enemies) {
                        // TODO: Prioritize locking up archons?
                        // Avoid enemy combat units unless holding ground
                        if (!holdGround 
                            && (enemy.type == RobotType.WATCHTOWER && enemy.mode == RobotMode.TURRET 
                                || enemy.type == RobotType.SOLDIER
                                || enemy.type == RobotType.SAGE)
                            && moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.actionRadiusSquared) {
                            score -= 100000;
                        }
                        // Move towards enemy units we want to kill
                        if ((enemy.type == RobotType.MINER || enemy.type == RobotType.BUILDER 
                            || enemy.type == RobotType.LABORATORY || enemy.type == RobotType.ARCHON)
                            && moveLocation.distanceSquaredTo(enemy.location) <= RobotType.SOLDIER.actionRadiusSquared) {
                            score += 10000 - moveLocation.distanceSquaredTo(enemy.location);
                        }
                    }
                    // Move to low rubble tile in combat to be able to fight faster
                    score += rc.senseRubble(moveLocation) * 1000; // TODO: What is the right scaling here?
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
            updateDestinationForExploration();
            //rc.setIndicatorLine(myLocation, destination, 0, 255, 0);
            fuzzyMove(destination);
        }
    }
}