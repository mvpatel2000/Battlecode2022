package hermes;

import battlecode.common.*;

public class Archon extends Robot {

    boolean builtUnit = false; // TODO: Delete

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        if (currentRound > 1000) {
            rc.disintegrate();
        }
        RobotType toBuild = rng.nextBoolean() ? RobotType.MINER : RobotType.SOLDIER;
        // Build builders if lots of lead for watchtowers
        if (rc.getTeamLeadAmount(allyTeam) > 1000) {
            toBuild = RobotType.BUILDER;
        }

        Direction optimalDir = null;
        int optimalRubble = Integer.MAX_VALUE;
        for (Direction dir : directionsWithoutCenter) {
            if (rc.canBuildRobot(toBuild, dir)) {
                int rubble = rc.senseRubble(myLocation.add(dir));
                if (rubble < optimalRubble) {
                    optimalDir = dir;
                    optimalRubble = rubble;
                }
            }
        }
        if (optimalDir != null && rc.canBuildRobot(toBuild, optimalDir)) {
            rc.buildRobot(toBuild, optimalDir);
        }
    }
}
