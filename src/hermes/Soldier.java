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

        // Try to act again if we didn't before moving
        attack();
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
            // TODO: Create better heuristics!
            Direction optimalDirection = null;
            int optimalScore = Integer.MAX_VALUE;
            for (Direction dir : directionsWithCenter) {
                if (rc.canMove(dir)) {
                    MapLocation moveLocation = myLocation.add(dir);
                    if (!rc.onTheMap(moveLocation)) {
                        continue;
                    }
                    // Move towards nearest enemy
                    int score = Integer.MAX_VALUE;
                    for (RobotInfo enemy : enemies) {
                        score = Math.min(score, moveLocation.distanceSquaredTo(enemy.location));
                    }
                    // Move to low rubble tile in combat to be able to fight faster
                    score += rc.senseRubble(moveLocation);
                    if (score < optimalScore) {
                        optimalDirection = dir;
                        optimalScore = score;
                    }
                }
            }
            if (optimalDirection != null && optimalDirection != Direction.CENTER) {
                rc.setIndicatorLine(myLocation, myLocation.add(optimalDirection), 0, 255, 0);
                fuzzyMove(myLocation.add(optimalDirection));
            }
        }
        else {
            updateDestinationForExploration();
            rc.setIndicatorLine(myLocation, destination, 0, 255, 0);
            fuzzyMove(destination);
        }
    }
}