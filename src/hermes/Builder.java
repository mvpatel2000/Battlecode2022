package hermes;

import battlecode.common.*;

public class Builder extends Robot {

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        buildOrHeal();

        move();
        
        // Try to act again if we didn't before moving
        buildOrHeal();
    }

    /**
     * Repair existing units or build a new one if threatened and nothing to repair. Prioritize
     * completing prototype units over repairing existing ones.
     * @throws GameActionException
     */
    public void buildOrHeal() throws GameActionException {
        if (!rc.isActionReady()) {
            return;
        }
        // Heal nearby buildings
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        MapLocation repairLocation = null;
        int remainingHealth = Integer.MAX_VALUE;
        for (RobotInfo ally : allies) {
            // Prioritize finishing prototypes
            int allyHealth = ally.getMode() == RobotMode.PROTOTYPE ? ally.getType().getMaxHealth(1) - ally.health - 1000 : ally.health;
            if (rc.canRepair(ally.location) && allyHealth < remainingHealth) {
                repairLocation = ally.location;
                remainingHealth = allyHealth;
            }
        }
        if (repairLocation != null && rc.canRepair(repairLocation)) {
            rc.repair(repairLocation);
        }
        // Build watchtower if in danger and didn't heal
        if (rc.isActionReady()) {
            RobotInfo[] enemies = rc.senseNearbyRobots(RobotType.BUILDER.visionRadiusSquared, enemyTeam);
            if (enemies.length > 0 && rc.getTeamLeadAmount(allyTeam) >= RobotType.WATCHTOWER.buildCostLead) {
                Direction optimalDir = null;
                int optimalRubble = Integer.MAX_VALUE;
                for (Direction dir : directionsWithoutCenter) {
                    if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                        int rubble = rc.senseRubble(myLocation.add(dir));
                        if (rubble < optimalRubble) {
                            optimalDir = dir;
                            optimalRubble = rubble;
                        }
                    }
                }
                if (optimalDir != null && rc.canBuildRobot(RobotType.WATCHTOWER, optimalDir)) {
                    rc.buildRobot(RobotType.WATCHTOWER, optimalDir);
                }
            }
        }
    }

    public void move() throws GameActionException {
        // Don't move if you're finishing building a watchtower
        RobotInfo[] allies = rc.senseNearbyRobots(RobotType.BUILDER.actionRadiusSquared, allyTeam);
        for (RobotInfo ally : allies) {
            if (ally.getMode() == RobotMode.PROTOTYPE) {
                return;
            }
        }

        updateDestinationForExploration();
        rc.setIndicatorLine(myLocation, destination, 0, 0, 255);
        fuzzyMove(destination);
    }
}