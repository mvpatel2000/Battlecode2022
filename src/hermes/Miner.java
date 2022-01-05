package hermes;

import battlecode.common.*;

public class Miner extends Robot {

    final static int[][] SENSE_SPIRAL_ORDER = {{0,0},{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,-1},{-1,1},{0,2},{2,0},{0,-2},{-2,0},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{-1,2},{2,2},{2,-2},{-2,-2},{-2,2},{0,3},{3,0},{0,-3},{-3,0},{1,3},{3,1},{3,-1},{1,-3},{-1,-3},{-3,-1},{-3,1},{-1,3},{2,3},{3,2},{3,-2},{2,-3},{-2,-3},{-3,-2},{-3,2},{-2,3},{0,4},{4,0},{0,-4},{-4,0},{1,4},{4,1},{4,-1},{1,-4},{-1,-4},{-4,-1},{-4,1},{-1,4},{3,3},{3,-3},{-3,-3},{-3,3},{2,4},{4,2},{4,-2},{2,-4},{-2,-4},{-4,-2},{-4,2},{-2,4}};
    
    public Miner(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        updateDestination();
        rc.setIndicatorLine(myLocation, destination, 255, 0, 0);
        mineNearbySquares();

        fuzzyMove(destination);
    }

    /**
     * Mines all nearby squares, leaving one lead since it regenerates. Note that you can mine
     * multiple times in a single turn
     * @throws GameActionException
     */
    public void mineNearbySquares() throws GameActionException {
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

    /**
     * If there are nearby resources, set that to destination. Otherwise, update destination
     * based on exploration pattern
     * @throws GameActionException
     */
    public void updateDestination() throws GameActionException {
        for (int[] shift : SENSE_SPIRAL_ORDER) {
            MapLocation spiralPlace = myLocation.translate(shift[0], shift[1]);
            if (!rc.onTheMap(spiralPlace)) {
                continue;
            }
            // Require more lead after first 20 rounds so miners don't go back to refreshed lead deposits
            int requiredLead = currentRound > 20 ? 6 : 1;
            if (rc.senseLead(spiralPlace) > requiredLead || rc.senseGold(spiralPlace) > 0) {
                destination = spiralPlace;
                exploreMode = false;
                return;
            }
        }
        exploreMode = true;
        updateDestinationForExploration();
    }
}
