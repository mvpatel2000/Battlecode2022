package demeter;

import battlecode.common.*;

public class Laboratory extends Robot {

    boolean requestingUpgrade = false;
    int transmutationRate = 0;

    public Laboratory(RobotController rc) throws GameActionException {
        super(rc);
        transmutationRate = rc.getTransmutationRate();
    }

    @Override
    public void runUnit() throws GameActionException { 
        transmutationRate = rc.getTransmutationRate();

        announceAlive();

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
        rc.setIndicatorString("Transmutation rate: " + transmutationRate);
        if (rc.canTransmute() && rc.getTeamLeadAmount(allyTeam) - transmutationRate >= RobotType.BUILDER.buildCostLead && !requestingUpgrade) {
            rc.transmute();
        }
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
}
