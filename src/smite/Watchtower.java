package smite;

import battlecode.common.*;

public class Watchtower extends Robot {

    // used to move around
    int turnsUntilLand = -1;

    public Watchtower(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {  
        announceAlive();
        attack();
        
        int nearestCluster = considerTransform();

        boolean isPortable = rc.getMode() == RobotMode.PORTABLE;

        if (isPortable) {
            portableMove(nearestCluster);
        }

        attack();
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currWatchtowers = commsHandler.readBuildingCountWatchtowers();
        if (currWatchtowers < 254) {
            commsHandler.writeBuildingCountWatchtowers(currWatchtowers + 1);
        }
    }

        /**
     * Consider transforming to mobile mode or vice-versa. Returns target cluster at each step or UNDEFINED_CLUSTER_INDEX
     * @throws GameActionException
     */
    public int considerTransform() throws GameActionException {
        // Must be in turret mode
        if (rc.getMode() == RobotMode.TURRET) {
            // No enemies nearby and miners exist in beginning
            if (nearbyEnemies.length == 0) {
                    int nearestCluster = getNearestCombatCluster();
                    // Transform and return target cluster
                    if (nearestCluster != commsHandler.UNDEFINED_CLUSTER_INDEX) {
                        MapLocation newDest = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                            clusterCentersY[nearestCluster / clusterWidthsLength]);
                        if (myLocation.distanceSquaredTo(newDest) > 64 && rc.canTransform()) {
                            rc.transform();
                            return nearestCluster;
                        }
                    }
            }
            return commsHandler.UNDEFINED_CLUSTER_INDEX;
        }
        // indicate whether we should land or otherwise return target cluster
        else if (rc.getMode() == RobotMode.PORTABLE) {
            int nearestCluster = getNearestCombatCluster();
            // Try to land if we're not already doing so
            if (turnsUntilLand < 0) {
                boolean nonMinerUnit = false;
                for (RobotInfo enemy : nearbyEnemies) {
                    if (enemy.type != RobotType.MINER) {
                        nonMinerUnit = true;
                        break;
                    }
                }
                // Transform faster
                if (nonMinerUnit || (pathing.destination != null && myLocation.distanceSquaredTo(pathing.destination) <= 16)) {
                    turnsUntilLand = 3;
                }
                // Transform but allow moves to better location
                if (nearestCluster == commsHandler.UNDEFINED_CLUSTER_INDEX) {
                    turnsUntilLand = 5;
                }
            }
            return nearestCluster;
        }
        // Prototype mode
        else {
            return commsHandler.UNDEFINED_CLUSTER_INDEX;
        }
    }


    /**
     * Paths to nearest combat cluster. Prepares to land if appropriate
     * @param nearestCluster
     * @throws GameActionException
     */
    public void portableMove(int nearestCluster) throws GameActionException {
        // Transform back to turret
        if (turnsUntilLand >= 0) {
            // Move if we can find a lower terrain tile near us
            if (rc.isMovementReady()) {
                Direction optimalDirection = Direction.CENTER;
                int optimalRubble = rc.senseRubble(myLocation);
                for (Direction dir : directionsWithoutCenter) {
                    if (rc.canMove(dir)) {
                        MapLocation moveLocation = myLocation.add(dir);
                        int rubble = rc.senseRubble(moveLocation);
                        if (rubble < optimalRubble) {
                            optimalDirection = dir;
                            optimalRubble = rubble;
                        }
                    }
                }
                // Move to lower rubble area
                if (optimalDirection != Direction.CENTER) {
                    pathing.move(optimalDirection);
                }
            }
            // Decrease counter for finding a spot to land
            if (turnsUntilLand > 0) {
                turnsUntilLand--;
            }
            // Transform if we chose not to move or we're out of turns to find better land position
            if (rc.canTransform() && (rc.isMovementReady() || turnsUntilLand == 0)) {
                rc.transform();
                // setBestBuildLocations();
                turnsUntilLand = -1;
            }
        }
        // Keep going
        else {
            MapLocation newDest = new MapLocation(clusterCentersX[nearestCluster % clusterWidthsLength], 
                                            clusterCentersY[nearestCluster / clusterWidthsLength]);
            pathing.updateDestination(newDest);
            pathing.pathToDestination();
        }
    }
}
