package hephaestus;

import battlecode.common.*;

public class Sage extends Robot {

    public Sage(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void runUnit() throws GameActionException { 
        announceAlive();

        envisionCharge();
        attack();

        combatMove();

        envisionCharge();
        attack();
    }

    public void envisionCharge() throws GameActionException {
        if (rc.canEnvision(AnomalyType.CHARGE)) {
            int damageDealt = 0;
            RobotInfo[] attackEnemies = rc.senseNearbyRobots(RobotType.SAGE.actionRadiusSquared, enemyTeam);
            int nearbyEnemiesLength = Math.min(attackEnemies.length, 15);
            for (int i = 0; i < nearbyEnemiesLength; i++) {
                RobotInfo enemy = attackEnemies[i];
                if (enemy.mode == RobotMode.DROID) {
                    damageDealt += enemy.type.getMaxHealth(enemy.level) * 22 / 100;
                }
            }
            if (damageDealt >= RobotType.SAGE.damage) {
                rc.envision(AnomalyType.CHARGE);
            }
        }
    }

    @Override
    public void announceAlive() throws GameActionException {
        int currSages = commsHandler.readFighterCountSages();
        if (currSages < 254) {
            commsHandler.writeFighterCountSages(currSages + 1);
        }
    }
}
