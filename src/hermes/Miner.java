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

        disintegrate();
    }

    /**
     * Disintegrate to put lead on tile if there's many friendly miners, no enemies, and 
     * no lead on the tile currently
     * @throws GameActionException
     */
    public void disintegrate() throws GameActionException {
        if (rc.senseLead(myLocation) == 0) {
            if (rc.senseNearbyRobots(RobotType.MINER.visionRadiusSquared, enemyTeam).length == 0) {
                RobotInfo[] adjacentAllies = rc.senseNearbyRobots(2, allyTeam);
                int adjacentMiners = 0;
                for (RobotInfo ally : adjacentAllies) {
                    if (ally.type == RobotType.MINER) {
                        adjacentMiners++;
                    }
                }
                if (adjacentMiners >= 5) {
                    rc.disintegrate();
                }
            }
        }
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
            // rc.setIndicatorLine(myLocation, fleeDirection, 255, 0, 0);
            fleeingCounter--;
        }
        // Path
        else {
            fuzzyMove(destination);
            // rc.setIndicatorLine(myLocation, destination, 255, 0, 0);
        }
    }

    /**
     * If there are nearby resources, set that to destination. Otherwise, update destination
     * based on exploration pattern
     * @throws GameActionException
     */
    public void updateDestination() throws GameActionException {
        int requiredLead = currentRound > 20 ? 6 : 1;
        // Don't scan if destination still has lead or gold
        if (destination != null && rc.canSenseLocation(destination)
             && (rc.senseLead(destination) > requiredLead || rc.senseGold(destination) > 0)) {
            exploreMode = false;
            return;
        }      
        
        // Find nearest resource tile
        MapLocation nearestResource = null;
        int optimalDistance = Integer.MAX_VALUE;
        for (MapLocation tile : rc.senseNearbyLocationsWithLead(RobotType.MINER.visionRadiusSquared)) {
            int dist = myLocation.distanceSquaredTo(tile);
            if (rc.senseLead(tile) > requiredLead && dist < optimalDistance) {
                nearestResource = tile;
                optimalDistance = dist;
            }
        }
        for (MapLocation tile : rc.senseNearbyLocationsWithGold(RobotType.MINER.visionRadiusSquared)) {
            int dist = myLocation.distanceSquaredTo(tile);
            if (rc.senseGold(tile) > 0 && dist < optimalDistance) {
                nearestResource = tile;
                optimalDistance = dist;
            }
        }
        if (nearestResource != null) {
            destination = nearestResource;
            exploreMode = false;
        }
        // Switch to explore mode if destination no longer has lead
        else {
            exploreMode = true;
            updateDestinationForExploration();
        }
    }
}
