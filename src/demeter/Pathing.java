package demeter;

import battlecode.common.*;

public class Pathing {

    Robot r;
    RobotController rc;
    UnitPathing up;
    RobotType myType;
    MapLocation destination;

    int[] tracker = new int[113];

    int fuzzyMovesLeft = 0;
    int MAX_FUZZY_MOVES = 3;

    public Pathing(Robot r) {
        this.r = r;
        this.rc = r.rc;
        myType = rc.getType();
        switch (myType) { // cases for soldier, miner, builder, sage, archon
            case SOLDIER:
                up = new SoldierPathing(rc);
                break;
            case MINER:
                up = new MinerPathing(rc);
                break;
            case BUILDER:
                up = new BuilderPathing(rc);
                break;
            case SAGE:
                up = new SagePathing(rc);
                break;
            case ARCHON:
                up = new ArchonPathing(rc);
                break;
            case WATCHTOWER:
                up = new WatchtowerPathing(rc);
                break;
            case LABORATORY:
                up = new LaboratoryPathing(rc);
                break;
        }
    }

    public void updateDestination(MapLocation newDest) {
        if (destination == null || destination.distanceSquaredTo(newDest) != 0) {
            destination = newDest;
            resetTracker();
            addVisited(r.myLocation);
        }
    }

    public void pathToDestination() throws GameActionException {
        if (destination != null) {
            pathTo(destination);
            if ((myType == RobotType.SOLDIER || myType == RobotType.SAGE) && destination.distanceSquaredTo(r.baseLocation) > 0) {
                rc.setIndicatorLine(r.myLocation, destination, 100 - rc.getTeam().ordinal() * 100, 50, rc.getTeam().ordinal() * 100);
            } else if ((myType == RobotType.MINER || myType == RobotType.BUILDER) && destination.distanceSquaredTo(r.baseLocation) > 0) {
                rc.setIndicatorLine(r.myLocation, destination, 150 + 100 - rc.getTeam().ordinal() * 100, 150, 150 + rc.getTeam().ordinal() * 100);
            } else if (myType == RobotType.ARCHON || myType == RobotType.WATCHTOWER || myType == RobotType.LABORATORY) {
                rc.setIndicatorLine(r.myLocation, destination, 250 - 250 * rc.getTeam().ordinal(), 0, 250 * rc.getTeam().ordinal());
            } else if (destination.distanceSquaredTo(r.baseLocation) == 0) {
                rc.setIndicatorLine(r.myLocation, destination, 200, 200, 200);
            }
        }
    }

    public void pathTo(MapLocation target) throws GameActionException {
        if (!rc.isMovementReady()) return;

        // if i'm not a special pather or if i still have fuzzy moves left, fuzzy move
        if (fuzzyMovesLeft > 0) {
            fuzzyMove(target);
            rc.setIndicatorDot(rc.getLocation(), 0, 0, 0);
            return;
        }

        // if i'm adjacent to my destination and it is unoccupied / not high rubble, move there
        if (r.myLocation.distanceSquaredTo(target) <= 2) {
            if (!rc.isLocationOccupied(target) && rc.senseRubble(target) < 30) {
                if (rc.canMove(r.myLocation.directionTo(target))) {
                    move(r.myLocation.directionTo(target));
                }
            }
            return;
        }

        // get bfs best direction
        Direction dir = up.bestDir(target);

        if (dir == null || !rc.canMove(dir)) return;
        
        if (isVisited(r.myLocation.add(dir))) {
            // System.out.println("Switching to fuzzy move for " + MAX_FUZZY_MOVES + " moves");
            fuzzyMovesLeft = MAX_FUZZY_MOVES;
            pathTo(target);
        } else {
            move(dir);
            rc.setIndicatorDot(rc.getLocation(), 255, 255, 255);
        }
    }

    /**
     * Use this function instead of rc.move(). Still need to verify canMove before calling this.
     */
    public void move(Direction dir) throws GameActionException {
        rc.move(dir);
        if (fuzzyMovesLeft > 0) {
            fuzzyMovesLeft--;
        }
        r.myLocation = r.myLocation.add(dir);
        addVisited(r.myLocation);
        r.nearbyEnemies = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, r.enemyTeam);
    }

    public void fuzzyMove(MapLocation target) throws GameActionException {
        if (!rc.isMovementReady()) return;

        // Don't move if adjacent to destination and something is blocking it
        if (r.myLocation.distanceSquaredTo(target) <= 2 && !rc.canMove(r.myLocation.directionTo(target))) {
            return;
        }
        // TODO: This is not optimal! Sometimes taking a slower move is better if its diagonal.
        MapLocation myLocation = rc.getLocation();
        Direction toDest = myLocation.directionTo(target);
        Direction[] dirs = {toDest, toDest.rotateLeft(), toDest.rotateRight(), toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
        int cost = 99999;
        Direction optimalDir = null;
        for (int i = 0; i < dirs.length; i++) {
            // Prefer forward moving steps over horizontal shifts
            if (i > 2 && cost > 0) {
                break;
            }
            Direction dir = dirs[i];
            if (rc.canMove(dir)) {
                int newCost = rc.senseRubble(myLocation.add(dir));
                // add epsilon boost to forward direction
                if (dir == toDest) {
                    newCost -= 1;
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
     * Move in the directon to the target, either directly or 45 degrees left or right, if there
     * is an open move with less than 20 rubble.
     * 
     * @param target
     * @throws GameActionException
     */
    public void cautiousGreedyMove(MapLocation target) throws GameActionException {
        if (!rc.isMovementReady()) return;

        // Get direction to target; check rubble in that direction, to the left, and to the right,
        // and move to the direction with the least rubble, as long as that rubble is at most 20.
        Direction dir = r.myLocation.directionTo(target);
        int bestRubble = 20;
        Direction bestDir = null;
        MapLocation loc = r.myLocation.add(dir);
        if (rc.onTheMap(loc) && rc.canMove(dir)) {
            int rubble = rc.senseRubble(loc);
            if (rubble < bestRubble) {
                bestRubble = rubble;
                bestDir = dir;
            }
        }
        loc = r.myLocation.add(dir.rotateLeft());
        if (rc.onTheMap(loc) && rc.canMove(dir.rotateLeft())) {
            int rubble = rc.senseRubble(loc);
            if (rubble < bestRubble) {
                bestRubble = rubble;
                bestDir = dir.rotateLeft();
            }
        }
        loc = r.myLocation.add(dir.rotateRight());
        if (rc.onTheMap(loc) && rc.canMove(dir.rotateRight())) {
            int rubble = rc.senseRubble(loc);
            if (rubble < bestRubble) {
                bestRubble = rubble;
                bestDir = dir.rotateRight();
            }
        }
        if (bestDir != null) {
            move(bestDir);
        }
    }

    private void addVisited(MapLocation loc) {
        int bit = loc.x + 60*loc.y;
        tracker[bit >>> 5] |= 1 << (31 - bit & 31);
    }

    private boolean isVisited(MapLocation loc) {
        int bit = loc.x + 60*loc.y;
        return (tracker[bit >>> 5] & (1 << (31 - bit & 31))) != 0;
    }

    private void resetTracker() {
        tracker = new int[113];
    }

}
