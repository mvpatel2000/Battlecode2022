package ares;

import battlecode.common.*;

public class Pathing {

    Robot r;
    RobotController rc;
    SoldierPathing sp;
    MinerPathing mp;
    boolean isSoldier = false;
    boolean isMiner = false;
    MapLocation destination;

    int[] tracker = new int[113];

    int fuzzyMovesLeft = 0;
    int MAX_FUZZY_MOVES = 3;

    public Pathing(Robot r) {
        this.r = r;
        this.rc = r.rc;
        if (rc.getType() == RobotType.SOLDIER) {
            isSoldier = true;
            sp = new SoldierPathing(rc);
        }
        else if (rc.getType() == RobotType.MINER) {
            isMiner = true;
            mp = new MinerPathing(rc);
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
            if (isSoldier && destination.distanceSquaredTo(r.baseLocation) > 0) {
                rc.setIndicatorLine(r.myLocation, destination, 100 - rc.getTeam().ordinal() * 100, 50, rc.getTeam().ordinal() * 100);
            }
            else if (isMiner && destination.distanceSquaredTo(r.baseLocation) > 0) {
                rc.setIndicatorLine(r.myLocation, destination, 150 + 100 - rc.getTeam().ordinal() * 100, 150, 150 + rc.getTeam().ordinal() * 100);
            } else if (destination.distanceSquaredTo(r.baseLocation) == 0) {
                rc.setIndicatorLine(r.myLocation, destination, 200, 200, 200);
            }
        }
    }

    public void pathTo(MapLocation target) throws GameActionException {
        if (!rc.isMovementReady()) return;

        // if i'm not a soldier/miner or if i still have fuzzy moves left, fuzzy move
        if ((!isSoldier && !isMiner) || fuzzyMovesLeft > 0) {
            fuzzyMove(target);
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
        Direction dir = null;
        if (isSoldier) {
            dir = sp.bestDir(target);
        } else if (isMiner) {
            dir = mp.bestDir(target);
        }

        if (dir == null || !rc.canMove(dir)) return;
        
        if (isVisited(r.myLocation.add(dir))) {
            // System.out.println("Switching to fuzzy move for " + MAX_FUZZY_MOVES + " moves");
            fuzzyMovesLeft = MAX_FUZZY_MOVES;
            pathTo(target);
        } else {
            move(dir);
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
