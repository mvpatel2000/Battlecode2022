package smite;

import battlecode.common.*;

public class Laboratory extends Robot {

    final static int[][] SENSE_SPIRAL_ORDER = {{0,0},{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,-1},{-1,1},{0,2},{2,0},{0,-2},{-2,0},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{-1,2},{2,2},{2,-2},{-2,-2},{-2,2},{0,3},{3,0},{0,-3},{-3,0},{1,3},{3,1},{3,-1},{1,-3},{-1,-3},{-3,-1},{-3,1},{-1,3},{2,3},{3,2},{3,-2},{2,-3},{-2,-3},{-3,-2},{-3,2},{-2,3},{0,4},{4,0},{0,-4},{-4,0},{1,4},{4,1},{4,-1},{1,-4},{-1,-4},{-4,-1},{-4,1},{-1,4},{3,3},{3,-3},{-3,-3},{-3,3},{2,4},{4,2},{4,-2},{2,-4},{-2,-4},{-4,-2},{-4,2},{-2,4},{0,5},{3,4},{4,3},{5,0},{4,-3},{3,-4},{0,-5},{-3,-4},{-4,-3},{-5,0},{-4,3},{-3,4}};

    boolean requestingUpgrade = false;
    int transmutationRate = 0;
    int lastTeamLead = 0;

    public Laboratory(RobotController rc) throws GameActionException {
        super(rc);
        transmutationRate = rc.getTransmutationRate();
    }

    @Override
    public void runUnit() throws GameActionException { 
        transmutationRate = rc.getTransmutationRate();

        announceAlive();

        considerMoving();

        shouldRequestUpgrade();

        transmute();
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currLaboratories = commsHandler.readBuildingCountLaboratories();
        if (currLaboratories < 254) {
            commsHandler.writeBuildingCountLaboratories(currLaboratories + 1);
        }
    }

    public void transmute() throws GameActionException {
        // //rc.setIndicatorString("Transmutation rate: " + transmutationRate);
        int currentTeamLead = rc.getTeamLeadAmount(allyTeam);
        if (rc.canTransmute() && !requestingUpgrade && commsHandler.readProductionControlGold() == CommsHandler.ProductionControl.CONTINUE) {
            rc.transmute();
        }
        lastTeamLead = currentTeamLead;
    }

    /**
     * Runs a heuristic to determine whether or not to request an upgrade, and then
     * requests it if appropriate and possible.
     * 
     * @throws GameActionException
     */
    public void shouldRequestUpgrade() throws GameActionException {
        if (transmutationRate >= 6 && rc.getLevel() == 1) {
            requestingUpgrade = true;
        } else if (transmutationRate <= 4 && rc.getLevel() == 1) {
            requestingUpgrade = false;
        } else if (transmutationRate >= 4 && rc.getLevel() == 2) {
            requestingUpgrade = true;
        } else if (transmutationRate == 3 && rc.getLevel() == 2) {
            requestingUpgrade = false;
        } else if (rc.getLevel() == 3) {
            requestingUpgrade = false;
        }

        if (requestingUpgrade && rc.getLevel() == 1 && commsHandler.readBuilderRequestType() == CommsHandler.BuilderRequest.NONE) {
            commsHandler.writeBuilderRequestAll((CommsHandler.BuilderRequest.LABORATORY_LEVEL_2 << 12) | (myLocation.x << 6) | rc.getLocation().y);
        } else if (requestingUpgrade && rc.getLevel() == 2 && commsHandler.readBuilderRequestType() == CommsHandler.BuilderRequest.NONE) {
            commsHandler.writeBuilderRequestAll((CommsHandler.BuilderRequest.LABORATORY_LEVEL_3 << 12) | (myLocation.x << 6) | rc.getLocation().y);
        }
    }

    public void considerMoving() throws GameActionException {
        int bestRubble = rc.senseRubble(myLocation);
        if (bestRubble == 0) {
            //rc.setIndicatorString("I'm on zero rubble! yay :D");
            if (rc.getMode() == RobotMode.PORTABLE && rc.canTransform()) {
                rc.transform();
            }
        } else {
            for (int[] dxdy : SENSE_SPIRAL_ORDER) {
                MapLocation newLocation = new MapLocation(myLocation.x + dxdy[0], myLocation.y + dxdy[1]);
                if (rc.canSenseLocation(newLocation) && !rc.isLocationOccupied(newLocation)) {
                    if (rc.senseRubble(newLocation) < bestRubble) {
                        bestRubble = rc.senseRubble(newLocation);
                        pathing.updateDestination(newLocation);
                    }
                }
            }
            //System.out.println\("Best destination: " + pathing.destination);
            if (pathing.destination.distanceSquaredTo(myLocation) == 0) {
                if (rc.getMode() == RobotMode.PORTABLE && rc.canTransform()) {
                    rc.transform();
                }
            } else {
                if (rc.getMode() != RobotMode.PORTABLE && rc.canTransform()) {
                    rc.transform();
                }
                pathing.fuzzyMove(pathing.destination);
            }
        }
    }
}
