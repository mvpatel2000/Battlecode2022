package smite;

import java.util.Map;

import javax.print.attribute.standard.Destination;

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
        announceAlive();
        
        mineNearbySquares();

        move();

        // Try to act again if we didn't before moving
        mineNearbySquares();

        // disintegrate();
    }

    public void announceAlive() throws GameActionException {
        commsHandler.writeMinerCount(commsHandler.readMinerCount() + 1);
    }

    /**
     * Disintegrate to put lead on tile if there's many friendly miners, no enemies, and 
     * no lead on the tile currently
     * @throws GameActionException
     */
    public void disintegrate() throws GameActionException {
        if (rc.senseLead(myLocation) == 0) {
            if (nearbyEnemies.length == 0) {
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
        MapLocation[] mineLocations = rc.senseNearbyLocationsWithGold(RobotType.MINER.actionRadiusSquared);
        for (MapLocation mineLocation : mineLocations) {
            while (rc.canMineGold(mineLocation)) {
                rc.mineGold(mineLocation);
            }
            // No longer able to mine
            if (!rc.isActionReady()) {
                return;
            }
        }
        mineLocations = rc.senseNearbyLocationsWithLead(RobotType.MINER.actionRadiusSquared, 2);
        for (MapLocation mineLocation : mineLocations) {
            int leadCount = rc.senseLead(mineLocation);
            while (rc.canMineLead(mineLocation) && leadCount > 1) {
                rc.mineLead(mineLocation);
                leadCount--;
            }
            // No longer able to mine
            if (!rc.isActionReady()) {
                return;
            }
        }
    }

    public void move() throws GameActionException {
        // if (!rc.isMovementReady()) {
        //     return;
        // }
        updateDestination();
        
        // Find nearest combat enemy to kite
        MapLocation nearestCombatEnemy = null;
        int distanceToEnemy = Integer.MAX_VALUE;
        for (RobotInfo enemy : nearbyEnemies) {
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
            // //rc.setIndicatorLine(myLocation, fleeDirection, 255, 0, 0);
            fleeingCounter--;
        }
        // Path
        pathing.pathToDestination();
    }

    /**
     * If there are nearby resources, set that to destination. Otherwise, update destination
     * based on exploration pattern
     * @throws GameActionException
     */
    public void updateDestination() throws GameActionException {
        // Flee back to archon to heal
        if (baseRetreat()) {
            return;
        }

        // Always claim cluster each turn
        int nearestCluster = getNearestMineCluster();

        // Don't scan if destination still has lead or gold
        if (pathing.destination != null && rc.canSenseLocation(pathing.destination)
             && (rc.senseLead(pathing.destination) > 1 || rc.senseGold(pathing.destination) > 0)) {
            // //rc.setIndicatorString("Destination still has lead or gold: " + destination);
            return;
        }
        
        // Set nearby resource tiles as a destination
        MapLocation nearestResource = null;
        int optimalDistance = Integer.MAX_VALUE;
        for (MapLocation tile : rc.senseNearbyLocationsWithLead(RobotType.MINER.visionRadiusSquared, 2)) {
            int dist = myLocation.distanceSquaredTo(tile);
            if (dist < optimalDistance) {
                nearestResource = tile;
                optimalDistance = dist;
            }
        }
        for (MapLocation tile : rc.senseNearbyLocationsWithGold(RobotType.MINER.visionRadiusSquared)) {
            int dist = myLocation.distanceSquaredTo(tile);
            if (dist < optimalDistance) {
                nearestResource = tile;
                optimalDistance = dist;
            }
        }
        if (nearestResource != null) {
            resetControlStatus(pathing.destination);
            pathing.updateDestination(nearestResource);
            return;
        }

        // Navigate to nearest resources found
        if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
            resetControlStatus(pathing.destination);
            MapLocation newDestination = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                            clusterCentersY[nearestCluster / clusterWidthsLength]);
            pathing.updateDestination(newDestination);
            return;
        }

        // Explore map. Get new cluster if not in explore mode or close to destination
        if (!exploreMode || myLocation.distanceSquaredTo(pathing.destination) <= 8) {
            nearestCluster = getNearestExploreCluster();
            if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                pathing.updateDestination(new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                                clusterCentersY[nearestCluster / clusterWidthsLength]));
                return;
            }
        }
    }

    /**
     * Returns nearest mine cluster or UNDEFINED_CLUSTER_INDEX otherwise
     * @return
     * @throws GameActionException
     */
    public int getNearestMineCluster() throws GameActionException {
        int closestCluster = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestClusterIndex = commsHandler.UNDEFINED_CLUSTER_INDEX;
        int closestClusterStatus = 0;
        int closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < commsHandler.MINE_CLUSTER_SLOTS; i++) {
            int nearestClusterAll = commsHandler.readMineClusterAll(i);
            int nearestCluster = nearestClusterAll & 127; // 7 lowest order bits
            int nearestClusterStatus = (nearestClusterAll & 896) >> 7; // 2^7 + 2^8 + 2^9
            // Continue if no mine written
            if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                continue;
            }
            // Skip clusters which are fully claimed
            if (nearestClusterStatus == 0) {
                continue;
            }
            int distance = myLocation.distanceSquaredTo(
                new MapLocation(
                    clusterCentersX[nearestCluster % clusterWidthsLength], 
                    clusterCentersY[nearestCluster / clusterWidthsLength]
                )
            );
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCluster = nearestCluster;
                closestClusterStatus = nearestClusterStatus;
                closestClusterIndex = i;
            }
        }
        // Claim cluster
        if (closestClusterIndex != commsHandler.UNDEFINED_CLUSTER_INDEX) {
            commsHandler.writeMineClusterClaimStatus(closestClusterIndex, closestClusterStatus-1);
        }
        return closestCluster;
    }
}
