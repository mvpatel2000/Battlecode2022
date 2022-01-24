package athena;

import battlecode.common.*;

public class Watchtower extends Robot {

    public Watchtower(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {  
        announceAlive();
        attack();
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currWatchtowers = commsHandler.readBuildingCountWatchtowers();
        if (currWatchtowers < 254) {
            commsHandler.writeBuildingCountWatchtowers(currWatchtowers + 1);
        }
    }
}
