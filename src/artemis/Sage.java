package artemis;

import battlecode.common.*;

public class Sage extends Robot {

    public Sage(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        announceAlive();
    }

    public void announceAlive() throws GameActionException {
        commsHandler.writeFighterCountSages(commsHandler.readFighterCountSages() + 1);
    }
}
