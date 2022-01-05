package hermes;

import battlecode.common.*;

public class Archon extends Robot {

    boolean builtUnit = false; // TODO: Delete

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        if (currentRound > 200) {
            rc.disintegrate();
        }
        // Pick a direction to build in.
        Direction dir = directionsWithoutCenter[rng.nextInt(directionsWithoutCenter.length)];
        if (rng.nextBoolean()) {
            // Let's try to build a miner.
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
            }
        } else {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
            }
        }
        // if (!builtUnit && rc.canBuildRobot(RobotType.MINER, dir)) {
        //     builtUnit = true;
        //     rc.buildRobot(RobotType.MINER, dir);
        // }
    }
}
