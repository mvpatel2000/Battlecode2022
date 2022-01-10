package hermes;

import battlecode.common.*;

public class Archon extends Robot {

    int myArchonNum;
    int numOurArchons;

    public Archon(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException {
        switch (currentRound) {
            case 1:
                firstRound();
                break;
            case 2:
                secondRound();
                break;
            default:
                // rc.resign();
                mainLoop();
                break;
        }
    }

    public void mainLoop() throws GameActionException {
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

    /**
     * First round, find out our archon number and set our status and location
     * 
     * @throws GameActionException
     */
    public void firstRound() throws GameActionException {
        myArchonNum = 0;
        if (commsHandler.readOurArchonStatus(0) == 1) {
            myArchonNum = 1;
        } if (commsHandler.readOurArchonStatus(1) == 1) {
            myArchonNum = 2;
        } if (commsHandler.readOurArchonStatus(2) == 1) {
            myArchonNum = 3;
        }
        System.out.println("I am archon number " + myArchonNum);
        commsHandler.writeOurArchonStatus(myArchonNum, 1);
        commsHandler.writeOurArchonLocation(myArchonNum, myLocation);
    }

    /**
     * Second round, find out how many archons we have
     * 
     * @throws GameActionException
     */
    public void secondRound() throws GameActionException {
        numOurArchons = 1;
        if (commsHandler.readOurArchonStatus(1) == 1) {
            numOurArchons++;
        } if (commsHandler.readOurArchonStatus(2) == 1) {
            numOurArchons++;
        } if (commsHandler.readOurArchonStatus(3) == 1) {
            numOurArchons++;
        }
        System.out.println("Our team has " + numOurArchons + " archons");
    }
}
