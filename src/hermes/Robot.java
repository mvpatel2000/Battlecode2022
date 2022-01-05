package hermes;

import battlecode.common.*;

import java.util.Random;
import java.util.ArrayList;

public class Robot {

    RobotController rc;
    int turnCount;
    int currentRound;
    int myID;
    Team allyTeam;
    Team enemyTeam;
    MapLocation myLocation;

    // Pathing
    MapLocation baseLocation;
    MapLocation destination;
    boolean exploreMode;
    ArrayList<MapLocation> priorDestinations;

    /** Array containing all the possible movement directions. */
    final Direction[] directionsWithoutCenter = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST
    };

    final Direction[] directionsWithCenter = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
        Direction.CENTER
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
        myLocation = rc.getLocation();
        destination = null;
        exploreMode = true; // TODO: This should be set to false if given instructions
        priorDestinations = new ArrayList<MapLocation>();

        // Buildings are their own base
        if (rc.getType() == RobotType.LABORATORY || rc.getType() == RobotType.WATCHTOWER || rc.getType() == RobotType.ARCHON) {
            baseLocation = myLocation;
        }
        // Units treat archon as base location
        else {
            RobotInfo[] adjacentRobots = rc.senseNearbyRobots(2, allyTeam);
            for (RobotInfo robot : adjacentRobots) {
                if (robot.type == RobotType.ARCHON) {
                    baseLocation = robot.location;
                }
            }
        }
    }

    public void run() throws GameActionException {
        // Before unit runs
        turnCount++;
        currentRound = rc.getRoundNum();

        // Does turn
        runUnit();

        // After unit runs
    }

    /**
     * Function to be overridden by the unit classes. This is where
     * all unit-specific run stuff happens.
     */
    public void runUnit() throws GameActionException {
    }

    /**
     * Use this function instead of rc.move(). Still need
     * to verify canMove before calling this.
     */
    void move(Direction dir) throws GameActionException {
        rc.move(dir);
        myLocation = myLocation.add(dir);
    }

    /**
     * Moves towards destination, in the optimal direction or diagonal offsets based on which is
     * cheaper to move through. Allows orthogonal moves to unlodge.
     */
    void fuzzyMove(MapLocation destination) throws GameActionException {
        if (!rc.isMovementReady()) {
            return;
        }
        // TODO: This is not optimal! Sometimes taking a slower move is better if its diagonal.
        MapLocation myLocation = rc.getLocation();
        Direction toDest = myLocation.directionTo(destination);
        Direction[] dirs = {toDest, toDest.rotateLeft(), toDest.rotateRight(), toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
        double cost = Integer.MAX_VALUE;
        Direction optimalDir = null;
        for (int i = 0; i < dirs.length; i++) {
            // Prefer forward moving steps over horizontal shifts
            if (i > 2 && cost > 0) {
                break;
            }
            Direction dir = dirs[i];
            if (rc.canMove(dir)) {
                double newCost = rc.senseRubble(myLocation.add(dir));
                // add epsilon boost to forward direction
                if (dir == toDest) {
                    newCost -= 0.001;
                }
                if (newCost < cost) {
                    cost = newCost;
                    optimalDir = dir;
                }
            }
        }
        if (optimalDir != null) {
            move(optimalDir);
        }
    }

    /**
     * Update destination to encourage exploration if destination is off map or destination is not
     * an enemy target. Uses rejection sampling to avoid destinations near already explored areas.
     * @throws GameActionException.
     */
    void updateDestinationForExploration() throws GameActionException {
        MapLocation nearDestination = myLocation;
        if (destination != null) {
            for (int i = 0; i < 3; i++) {
                nearDestination = nearDestination.add(nearDestination.directionTo(destination));
            }
        }
        // Reroute if 1) nearDestination not on map or 2) can sense destination and it's not on the map
        // or it's not occupied (so no Archon)
        if (destination == null || !rc.onTheMap(nearDestination) ||
            (rc.canSenseLocation(destination)
            && (!rc.onTheMap(destination)
                || !rc.isLocationOccupied(destination)
                || rc.senseRobotAtLocation(destination).team == allyTeam))) {
            // Rerouting
            if (destination != null) {
                priorDestinations.add(destination);
            }
            boolean valid = true;
            int dxexplore = (int)(Math.random()*80);
            int dyexplore = 120 - dxexplore;
            dxexplore = Math.random() < .5 ? dxexplore : -dxexplore;
            dyexplore = Math.random() < .5 ? dyexplore : -dyexplore;
            destination = new MapLocation(baseLocation.x + dxexplore, baseLocation.y + dyexplore);
            exploreMode = true;
            for (int i = 0; i < priorDestinations.size(); i++) {
                if (destination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                    valid = false;
                    break;
                }
            }
            while (!valid) {
                valid = true;
                destination = new MapLocation(baseLocation.x + (int)(Math.random()*80 - 40),
                                                baseLocation.y + (int)(Math.random()*80 - 40));
                for (int i = 0; i < priorDestinations.size(); i++) {
                    if (destination.distanceSquaredTo(priorDestinations.get(i)) < 40) {
                        valid = false;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Sorts through nearby units to attack. Prioritize killing >> prototypes close to finishing
     * >> combat units >> weakest unit
     * @throws GameActionException
     */
    public void attack() throws GameActionException {
        if (!rc.isActionReady()) {
            return;
        }
        int actionRadius = rc.getType().actionRadiusSquared;
        int damage = rc.getType().getDamage(rc.getLevel());
        rc.setIndicatorString(actionRadius + "" + damage);
        // Find attack maximizing score
        MapLocation optimalAttack = null;
        int optimalScore = -1;
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(actionRadius, enemyTeam);
        for (RobotInfo enemy : nearbyEnemies) {
            int score = 0;
            // Always prioritize kills
            if (enemy.health <= damage) {
                score += 1000000;
            }
            // Prioritize combat units
            if (enemy.type == RobotType.WATCHTOWER || enemy.type == RobotType.SOLDIER || enemy.type == RobotType.SAGE) {
                score += 10000;
            }
            // Target prototype units closest to being finished
            if (enemy.getMode() == RobotMode.PROTOTYPE && enemy.getType().getMaxHealth(1) - enemy.health <= 10) {
                // We want to prioritize hitting buildings closest to being finished so they're not completed
                score += 100000 + enemy.getType().getMaxHealth(1) - enemy.health;
            }
            // Target weakest unit
            else {
                score += 1000 - enemy.health;
            }
            if (score > optimalScore) {
                optimalAttack = enemy.location;
                optimalScore = score;
            }
        }
        if (optimalAttack != null && rc.canAttack(optimalAttack)) {
            rc.attack(optimalAttack);
        }
    }
}
