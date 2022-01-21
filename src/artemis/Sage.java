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
        int currSages = commsHandler.readFighterCountSages();
        if (currSages < 254) {
            commsHandler.writeFighterCountSages(currSages + 1);
        }
    }
}
