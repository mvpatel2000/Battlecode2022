package hermes;

import battlecode.common.*;

import java.util.Random;

public class Robot {

    RobotController rc;
    int turnCount = 0;
    int currentRound;
    int myID;
    Team allyTeam;
    Team enemyTeam;

    /** Array containing all the possible movement directions. */
    final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    final Random rng = new Random(6147);

    public Robot(RobotController robotController) throws GameActionException {
        rc = robotController;
        currentRound = rc.getRoundNum();
        turnCount = 0;
        allyTeam = rc.getTeam();
        enemyTeam = allyTeam.opponent();
        myID = rc.getID();
    }

    public void run() throws GameActionException {
        // Before unit runs
        turnCount++;
        currentRound = rc.getRoundNum();

        // Does turn
        runUnit(rc);

        // After unit runs
    }

    /**
     * Function to be overridden by the unit classes. This is where
     * all unit-specific run stuff happens.
     */
    public void runUnit(RobotController rc) throws GameActionException {
    }
}
