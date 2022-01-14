package ares;

import battlecode.common.*;

public class Watchtower extends Robot {

    public Watchtower(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {  
        attack();
    }
}
