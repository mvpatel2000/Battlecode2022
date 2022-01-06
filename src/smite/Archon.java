package smite;

import battlecode.common.*;

public class Archon extends Robot {

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        // if (currentRound > 500) {
        //     rc.disintegrate();
        // }
        boolean shouldBuildMiner = turnCount < 100 ? rng.nextBoolean() : rng.nextDouble() < 0.3;
        RobotType toBuild = shouldBuildMiner ? RobotType.MINER : RobotType.SOLDIER;
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

        repair();
    }

    /**
     * If we didn't build a unit, repair a damaged nearby one
     * @throws GameActionException
     */
    public void repair() throws GameActionException {
        if (rc.isActionReady()) {
            RobotInfo[] nearbyAllies = rc.senseNearbyRobots(RobotType.ARCHON.actionRadiusSquared, allyTeam);
            MapLocation optimalRepair = null;
            int remainingHealth = Integer.MAX_VALUE;
            for (RobotInfo ally : nearbyAllies) {
                if (rc.canRepair(ally.location) && ally.health < remainingHealth) {
                    optimalRepair = ally.location;
                    remainingHealth = ally.health;
                }
            }
            if (optimalRepair != null && rc.canRepair(optimalRepair)) {
                rc.repair(optimalRepair);
            }
        }
    }
}
