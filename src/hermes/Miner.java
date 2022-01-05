package hermes;

import battlecode.common.*;

public class Miner extends Robot {

    int fleeingCounter;
    MapLocation lastEnemyLocation;

    final static int[][] INNER_SPIRAL_ORDER = {{0,0},{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,-1},{-1,1},{0,2},{2,0},{0,-2},{-2,0},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{-1,2},{2,2},{2,-2},{-2,-2},{-2,2},{0,3},{3,0},{0,-3},{-3,0},{1,3},{3,1},{3,-1},{1,-3},{-1,-3},{-3,-1},{-3,1},{-1,3},{2,3},{3,2},{3,-2},{2,-3},{-2,-3},{-3,-2},{-3,2},{-2,3}};
    final static int[][] OUTER_SPIRAL_ORDER = {{0,4},{4,0},{0,-4},{-4,0},{1,4},{4,1},{4,-1},{1,-4},{-1,-4},{-4,-1},{-4,1},{-1,4},{3,3},{3,-3},{-3,-3},{-3,3},{2,4},{4,2},{4,-2},{2,-4},{-2,-4},{-4,-2},{-4,2},{-2,4}};
    
    public Miner(RobotController rc) throws GameActionException {
        super(rc);
        fleeingCounter = 0;
        lastEnemyLocation = null;
    }

    @Override
    public void runUnit() throws GameActionException { 
        mineNearbySquares();

        move();

        // Try to act again if we didn't before moving
        mineNearbySquares();
    }

    /**
     * Mines all nearby squares, leaving one lead since it regenerates. Note that you can mine
     * multiple times in a single turn
     * @throws GameActionException
     */
    public void mineNearbySquares() throws GameActionException {
        if (!rc.isActionReady()) {
            return;
        }
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                if (!rc.onTheMap(mineLocation)) {
                    continue;
                }
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                int leadCount = rc.senseLead(mineLocation);
                while (rc.canMineLead(mineLocation) && leadCount > 1) {
                    rc.mineLead(mineLocation);
                    leadCount--;
                }
            }
        }
    }

    public void move() throws GameActionException {
        updateDestination();
        
        // Find nearest combat enemy to kite
        RobotInfo[] enemies = rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, enemyTeam);
        MapLocation nearestCombatEnemy = null;
        int distanceToEnemy = Integer.MAX_VALUE;
        for (RobotInfo enemy : enemies) {
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
            fleeingCounter = 5;
        }

        // Kite enemy unit
        if (fleeingCounter > 0) {
            MapLocation fleeDirection = myLocation.add(myLocation.directionTo(lastEnemyLocation).opposite());
            fuzzyMove(fleeDirection);
            rc.setIndicatorLine(myLocation, fleeDirection, 255, 0, 0);
            fleeingCounter--;
        }
        // Path
        else {
            fuzzyMove(destination);
            rc.setIndicatorLine(myLocation, destination, 255, 0, 0);
        }
    }

    /**
     * If there are nearby resources, set that to destination. Otherwise, update destination
     * based on exploration pattern
     * @throws GameActionException
     */
    public void updateDestination() throws GameActionException {
        int requiredLead = currentRound > 20 ? 6 : 1;
        // Rescan all tiles in vision radius if we're not moving and have extra compute
        if (!rc.isMovementReady()) {
            for (int[] shift : INNER_SPIRAL_ORDER) {
                MapLocation spiralPlace = myLocation.translate(shift[0], shift[1]);
                if (!rc.onTheMap(spiralPlace)) {
                    continue;
                }
                // Require more lead after first 20 rounds so miners don't go back to refreshed lead deposits
                if (rc.senseLead(spiralPlace) > requiredLead || rc.senseGold(spiralPlace) > 0) {
                    destination = spiralPlace;
                    exploreMode = false;
                    return;
                }
            }
        }
        // Only scan newly visible tiles if we're ready to move
        for (int[] shift : OUTER_SPIRAL_ORDER) {
            MapLocation spiralPlace = myLocation.translate(shift[0], shift[1]);
            if (!rc.onTheMap(spiralPlace)) {
                continue;
            }
            // Require more lead after first 20 rounds so miners don't go back to refreshed lead deposits
            if (rc.senseLead(spiralPlace) > requiredLead || rc.senseGold(spiralPlace) > 0) {
                destination = spiralPlace;
                exploreMode = false;
                return;
            }
        }
        // Switch to explore mode if destination no longer has lead
        if (exploreMode || !rc.onTheMap(destination) || !rc.canSenseLocation(destination) 
            || rc.senseLead(destination) <= requiredLead) {
            exploreMode = true;
            updateDestinationForExploration();
        }
    }
}
