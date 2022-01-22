package hephaestus;

import battlecode.common.*;

public class Laboratory extends Robot {

    public Laboratory(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        announceAlive();

        if (rc.canTransmute()) {
            rc.transmute();
        }
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currLaboratories = commsHandler.readBuildingCountLaboratories();
        if (currLaboratories < 254) {
            commsHandler.writeBuildingCountLaboratories(currLaboratories + 1);
        }
    }
}
