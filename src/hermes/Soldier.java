package hermes;

import battlecode.common.*;

public class Soldier extends Robot {

    public Soldier(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        attack();

        move();

        // Try to attack again if we didn't attack before move
        if (rc.isActionReady()) {
            attack();
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
            // Find direction minimizing score (closest to enemy)
            // TODO: Create better heuristics! Should not rush into enemy
            Direction optimalDirection = null;
            int optimalScore = Integer.MAX_VALUE;
            for (Direction dir : directionsWithCenter) {
                int score = Integer.MAX_VALUE;
                for (RobotInfo enemy : enemies) {
                    score = Math.min(score, myLocation.distanceSquaredTo(enemy.location));
                }
                if (score < optimalScore) {
                    optimalDirection = dir;
                    optimalScore = score;
                }
            }
            if (optimalDirection != null && optimalDirection != Direction.CENTER) {
                fuzzyMove(myLocation.add(optimalDirection));
            }
        }
        else {
            updateDestinationForExploration();
            rc.setIndicatorLine(myLocation, destination, 0, 255, 0);
            fuzzyMove(destination);
        }
    }

    /**
     * Sorts through nearby units to attack. Prioritize killing >> combat units >> weakest unit
     * @throws GameActionException
     */
    public void attack() throws GameActionException {
        // Find attack maximizing score
        MapLocation optimalAttack = null;
        int optimalScore = 0;
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(RobotType.SOLDIER.actionRadiusSquared, enemyTeam);
        for (RobotInfo enemy : nearbyEnemies) {
            int score = 0;
            // Always prioritize kills
            if (enemy.health <= RobotType.SOLDIER.damage) {
                score += 100000;
            }
            // Prioritize combat units
            if (enemy.type == RobotType.WATCHTOWER || enemy.type == RobotType.SOLDIER || enemy.type == RobotType.SAGE) {
                score += 10000;
            }
            // Target units closest to dying
            score += 1000 - enemy.health;
            if (score > optimalScore) {
                optimalAttack = enemy.location;
                optimalScore = score;
            }
        }
        if (optimalAttack != null && rc.canAttack(optimalAttack)) {
            rc.attack(optimalAttack);
        }
    }
}