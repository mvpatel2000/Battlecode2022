// Inspired by https://github.com/IvanGeffner/battlecode2021/blob/master/thirtyone/BFSMuckraker.java.
package smite;

import battlecode.common.*;

public class MinerPathing {
    
    RobotController rc;

    static MapLocation l93; // location representing relative coordinate (-4, -1)
    static int d93; // shortest distance to location from current location
    static Direction dir93; // best direction to take now to optimally get to location

    static MapLocation l108; // location representing relative coordinate (-4, 0)
    static int d108; // shortest distance to location from current location
    static Direction dir108; // best direction to take now to optimally get to location

    static MapLocation l123; // location representing relative coordinate (-4, 1)
    static int d123; // shortest distance to location from current location
    static Direction dir123; // best direction to take now to optimally get to location

    static MapLocation l79; // location representing relative coordinate (-3, -2)
    static int d79; // shortest distance to location from current location
    static Direction dir79; // best direction to take now to optimally get to location

    static MapLocation l94; // location representing relative coordinate (-3, -1)
    static int d94; // shortest distance to location from current location
    static Direction dir94; // best direction to take now to optimally get to location

    static MapLocation l109; // location representing relative coordinate (-3, 0)
    static int d109; // shortest distance to location from current location
    static Direction dir109; // best direction to take now to optimally get to location

    static MapLocation l124; // location representing relative coordinate (-3, 1)
    static int d124; // shortest distance to location from current location
    static Direction dir124; // best direction to take now to optimally get to location

    static MapLocation l139; // location representing relative coordinate (-3, 2)
    static int d139; // shortest distance to location from current location
    static Direction dir139; // best direction to take now to optimally get to location

    static MapLocation l65; // location representing relative coordinate (-2, -3)
    static int d65; // shortest distance to location from current location
    static Direction dir65; // best direction to take now to optimally get to location

    static MapLocation l80; // location representing relative coordinate (-2, -2)
    static int d80; // shortest distance to location from current location
    static Direction dir80; // best direction to take now to optimally get to location

    static MapLocation l95; // location representing relative coordinate (-2, -1)
    static int d95; // shortest distance to location from current location
    static Direction dir95; // best direction to take now to optimally get to location

    static MapLocation l110; // location representing relative coordinate (-2, 0)
    static int d110; // shortest distance to location from current location
    static Direction dir110; // best direction to take now to optimally get to location

    static MapLocation l125; // location representing relative coordinate (-2, 1)
    static int d125; // shortest distance to location from current location
    static Direction dir125; // best direction to take now to optimally get to location

    static MapLocation l140; // location representing relative coordinate (-2, 2)
    static int d140; // shortest distance to location from current location
    static Direction dir140; // best direction to take now to optimally get to location

    static MapLocation l155; // location representing relative coordinate (-2, 3)
    static int d155; // shortest distance to location from current location
    static Direction dir155; // best direction to take now to optimally get to location

    static MapLocation l51; // location representing relative coordinate (-1, -4)
    static int d51; // shortest distance to location from current location
    static Direction dir51; // best direction to take now to optimally get to location

    static MapLocation l66; // location representing relative coordinate (-1, -3)
    static int d66; // shortest distance to location from current location
    static Direction dir66; // best direction to take now to optimally get to location

    static MapLocation l81; // location representing relative coordinate (-1, -2)
    static int d81; // shortest distance to location from current location
    static Direction dir81; // best direction to take now to optimally get to location

    static MapLocation l96; // location representing relative coordinate (-1, -1)
    static int d96; // shortest distance to location from current location
    static Direction dir96; // best direction to take now to optimally get to location

    static MapLocation l111; // location representing relative coordinate (-1, 0)
    static int d111; // shortest distance to location from current location
    static Direction dir111; // best direction to take now to optimally get to location

    static MapLocation l126; // location representing relative coordinate (-1, 1)
    static int d126; // shortest distance to location from current location
    static Direction dir126; // best direction to take now to optimally get to location

    static MapLocation l141; // location representing relative coordinate (-1, 2)
    static int d141; // shortest distance to location from current location
    static Direction dir141; // best direction to take now to optimally get to location

    static MapLocation l156; // location representing relative coordinate (-1, 3)
    static int d156; // shortest distance to location from current location
    static Direction dir156; // best direction to take now to optimally get to location

    static MapLocation l171; // location representing relative coordinate (-1, 4)
    static int d171; // shortest distance to location from current location
    static Direction dir171; // best direction to take now to optimally get to location

    static MapLocation l52; // location representing relative coordinate (0, -4)
    static int d52; // shortest distance to location from current location
    static Direction dir52; // best direction to take now to optimally get to location

    static MapLocation l67; // location representing relative coordinate (0, -3)
    static int d67; // shortest distance to location from current location
    static Direction dir67; // best direction to take now to optimally get to location

    static MapLocation l82; // location representing relative coordinate (0, -2)
    static int d82; // shortest distance to location from current location
    static Direction dir82; // best direction to take now to optimally get to location

    static MapLocation l97; // location representing relative coordinate (0, -1)
    static int d97; // shortest distance to location from current location
    static Direction dir97; // best direction to take now to optimally get to location

    static MapLocation l112; // location representing relative coordinate (0, 0)
    static int d112; // shortest distance to location from current location
    static Direction dir112; // best direction to take now to optimally get to location

    static MapLocation l127; // location representing relative coordinate (0, 1)
    static int d127; // shortest distance to location from current location
    static Direction dir127; // best direction to take now to optimally get to location

    static MapLocation l142; // location representing relative coordinate (0, 2)
    static int d142; // shortest distance to location from current location
    static Direction dir142; // best direction to take now to optimally get to location

    static MapLocation l157; // location representing relative coordinate (0, 3)
    static int d157; // shortest distance to location from current location
    static Direction dir157; // best direction to take now to optimally get to location

    static MapLocation l172; // location representing relative coordinate (0, 4)
    static int d172; // shortest distance to location from current location
    static Direction dir172; // best direction to take now to optimally get to location

    static MapLocation l53; // location representing relative coordinate (1, -4)
    static int d53; // shortest distance to location from current location
    static Direction dir53; // best direction to take now to optimally get to location

    static MapLocation l68; // location representing relative coordinate (1, -3)
    static int d68; // shortest distance to location from current location
    static Direction dir68; // best direction to take now to optimally get to location

    static MapLocation l83; // location representing relative coordinate (1, -2)
    static int d83; // shortest distance to location from current location
    static Direction dir83; // best direction to take now to optimally get to location

    static MapLocation l98; // location representing relative coordinate (1, -1)
    static int d98; // shortest distance to location from current location
    static Direction dir98; // best direction to take now to optimally get to location

    static MapLocation l113; // location representing relative coordinate (1, 0)
    static int d113; // shortest distance to location from current location
    static Direction dir113; // best direction to take now to optimally get to location

    static MapLocation l128; // location representing relative coordinate (1, 1)
    static int d128; // shortest distance to location from current location
    static Direction dir128; // best direction to take now to optimally get to location

    static MapLocation l143; // location representing relative coordinate (1, 2)
    static int d143; // shortest distance to location from current location
    static Direction dir143; // best direction to take now to optimally get to location

    static MapLocation l158; // location representing relative coordinate (1, 3)
    static int d158; // shortest distance to location from current location
    static Direction dir158; // best direction to take now to optimally get to location

    static MapLocation l173; // location representing relative coordinate (1, 4)
    static int d173; // shortest distance to location from current location
    static Direction dir173; // best direction to take now to optimally get to location

    static MapLocation l69; // location representing relative coordinate (2, -3)
    static int d69; // shortest distance to location from current location
    static Direction dir69; // best direction to take now to optimally get to location

    static MapLocation l84; // location representing relative coordinate (2, -2)
    static int d84; // shortest distance to location from current location
    static Direction dir84; // best direction to take now to optimally get to location

    static MapLocation l99; // location representing relative coordinate (2, -1)
    static int d99; // shortest distance to location from current location
    static Direction dir99; // best direction to take now to optimally get to location

    static MapLocation l114; // location representing relative coordinate (2, 0)
    static int d114; // shortest distance to location from current location
    static Direction dir114; // best direction to take now to optimally get to location

    static MapLocation l129; // location representing relative coordinate (2, 1)
    static int d129; // shortest distance to location from current location
    static Direction dir129; // best direction to take now to optimally get to location

    static MapLocation l144; // location representing relative coordinate (2, 2)
    static int d144; // shortest distance to location from current location
    static Direction dir144; // best direction to take now to optimally get to location

    static MapLocation l159; // location representing relative coordinate (2, 3)
    static int d159; // shortest distance to location from current location
    static Direction dir159; // best direction to take now to optimally get to location

    static MapLocation l85; // location representing relative coordinate (3, -2)
    static int d85; // shortest distance to location from current location
    static Direction dir85; // best direction to take now to optimally get to location

    static MapLocation l100; // location representing relative coordinate (3, -1)
    static int d100; // shortest distance to location from current location
    static Direction dir100; // best direction to take now to optimally get to location

    static MapLocation l115; // location representing relative coordinate (3, 0)
    static int d115; // shortest distance to location from current location
    static Direction dir115; // best direction to take now to optimally get to location

    static MapLocation l130; // location representing relative coordinate (3, 1)
    static int d130; // shortest distance to location from current location
    static Direction dir130; // best direction to take now to optimally get to location

    static MapLocation l145; // location representing relative coordinate (3, 2)
    static int d145; // shortest distance to location from current location
    static Direction dir145; // best direction to take now to optimally get to location

    static MapLocation l101; // location representing relative coordinate (4, -1)
    static int d101; // shortest distance to location from current location
    static Direction dir101; // best direction to take now to optimally get to location

    static MapLocation l116; // location representing relative coordinate (4, 0)
    static int d116; // shortest distance to location from current location
    static Direction dir116; // best direction to take now to optimally get to location

    static MapLocation l131; // location representing relative coordinate (4, 1)
    static int d131; // shortest distance to location from current location
    static Direction dir131; // best direction to take now to optimally get to location


    public MinerPathing(RobotController rc) {
        this.rc = rc;
    }

    public Direction bestDir(MapLocation target) throws GameActionException {

        l112 = rc.getLocation();
        d112 = 0;
        dir112 = Direction.CENTER;

        l111 = l112.add(Direction.WEST); // (-1, 0) from (0, 0)
        d111 = 99999;
        dir111 = null;

        l97 = l112.add(Direction.SOUTH); // (0, -1) from (0, 0)
        d97 = 99999;
        dir97 = null;

        l127 = l112.add(Direction.NORTH); // (0, 1) from (0, 0)
        d127 = 99999;
        dir127 = null;

        l113 = l112.add(Direction.EAST); // (1, 0) from (0, 0)
        d113 = 99999;
        dir113 = null;

        l96 = l112.add(Direction.SOUTHWEST); // (-1, -1) from (0, 0)
        d96 = 99999;
        dir96 = null;

        l126 = l112.add(Direction.NORTHWEST); // (-1, 1) from (0, 0)
        d126 = 99999;
        dir126 = null;

        l98 = l112.add(Direction.SOUTHEAST); // (1, -1) from (0, 0)
        d98 = 99999;
        dir98 = null;

        l128 = l112.add(Direction.NORTHEAST); // (1, 1) from (0, 0)
        d128 = 99999;
        dir128 = null;

        l110 = l111.add(Direction.WEST); // (-2, 0) from (-1, 0)
        d110 = 99999;
        dir110 = null;

        l82 = l97.add(Direction.SOUTH); // (0, -2) from (0, -1)
        d82 = 99999;
        dir82 = null;

        l142 = l127.add(Direction.NORTH); // (0, 2) from (0, 1)
        d142 = 99999;
        dir142 = null;

        l114 = l113.add(Direction.EAST); // (2, 0) from (1, 0)
        d114 = 99999;
        dir114 = null;

        l95 = l111.add(Direction.SOUTHWEST); // (-2, -1) from (-1, 0)
        d95 = 99999;
        dir95 = null;

        l125 = l111.add(Direction.NORTHWEST); // (-2, 1) from (-1, 0)
        d125 = 99999;
        dir125 = null;

        l81 = l97.add(Direction.SOUTHWEST); // (-1, -2) from (0, -1)
        d81 = 99999;
        dir81 = null;

        l141 = l127.add(Direction.NORTHWEST); // (-1, 2) from (0, 1)
        d141 = 99999;
        dir141 = null;

        l83 = l97.add(Direction.SOUTHEAST); // (1, -2) from (0, -1)
        d83 = 99999;
        dir83 = null;

        l143 = l127.add(Direction.NORTHEAST); // (1, 2) from (0, 1)
        d143 = 99999;
        dir143 = null;

        l99 = l113.add(Direction.SOUTHEAST); // (2, -1) from (1, 0)
        d99 = 99999;
        dir99 = null;

        l129 = l113.add(Direction.NORTHEAST); // (2, 1) from (1, 0)
        d129 = 99999;
        dir129 = null;

        l80 = l96.add(Direction.SOUTHWEST); // (-2, -2) from (-1, -1)
        d80 = 99999;
        dir80 = null;

        l140 = l126.add(Direction.NORTHWEST); // (-2, 2) from (-1, 1)
        d140 = 99999;
        dir140 = null;

        l84 = l98.add(Direction.SOUTHEAST); // (2, -2) from (1, -1)
        d84 = 99999;
        dir84 = null;

        l144 = l128.add(Direction.NORTHEAST); // (2, 2) from (1, 1)
        d144 = 99999;
        dir144 = null;

        l109 = l110.add(Direction.WEST); // (-3, 0) from (-2, 0)
        d109 = 99999;
        dir109 = null;

        l67 = l82.add(Direction.SOUTH); // (0, -3) from (0, -2)
        d67 = 99999;
        dir67 = null;

        l157 = l142.add(Direction.NORTH); // (0, 3) from (0, 2)
        d157 = 99999;
        dir157 = null;

        l115 = l114.add(Direction.EAST); // (3, 0) from (2, 0)
        d115 = 99999;
        dir115 = null;

        l94 = l110.add(Direction.SOUTHWEST); // (-3, -1) from (-2, 0)
        d94 = 99999;
        dir94 = null;

        l124 = l110.add(Direction.NORTHWEST); // (-3, 1) from (-2, 0)
        d124 = 99999;
        dir124 = null;

        l66 = l82.add(Direction.SOUTHWEST); // (-1, -3) from (0, -2)
        d66 = 99999;
        dir66 = null;

        l156 = l142.add(Direction.NORTHWEST); // (-1, 3) from (0, 2)
        d156 = 99999;
        dir156 = null;

        l68 = l82.add(Direction.SOUTHEAST); // (1, -3) from (0, -2)
        d68 = 99999;
        dir68 = null;

        l158 = l142.add(Direction.NORTHEAST); // (1, 3) from (0, 2)
        d158 = 99999;
        dir158 = null;

        l100 = l114.add(Direction.SOUTHEAST); // (3, -1) from (2, 0)
        d100 = 99999;
        dir100 = null;

        l130 = l114.add(Direction.NORTHEAST); // (3, 1) from (2, 0)
        d130 = 99999;
        dir130 = null;

        l79 = l95.add(Direction.SOUTHWEST); // (-3, -2) from (-2, -1)
        d79 = 99999;
        dir79 = null;

        l139 = l125.add(Direction.NORTHWEST); // (-3, 2) from (-2, 1)
        d139 = 99999;
        dir139 = null;

        l65 = l81.add(Direction.SOUTHWEST); // (-2, -3) from (-1, -2)
        d65 = 99999;
        dir65 = null;

        l155 = l141.add(Direction.NORTHWEST); // (-2, 3) from (-1, 2)
        d155 = 99999;
        dir155 = null;

        l69 = l83.add(Direction.SOUTHEAST); // (2, -3) from (1, -2)
        d69 = 99999;
        dir69 = null;

        l159 = l143.add(Direction.NORTHEAST); // (2, 3) from (1, 2)
        d159 = 99999;
        dir159 = null;

        l85 = l99.add(Direction.SOUTHEAST); // (3, -2) from (2, -1)
        d85 = 99999;
        dir85 = null;

        l145 = l129.add(Direction.NORTHEAST); // (3, 2) from (2, 1)
        d145 = 99999;
        dir145 = null;

        l108 = l109.add(Direction.WEST); // (-4, 0) from (-3, 0)
        d108 = 99999;
        dir108 = null;

        l52 = l67.add(Direction.SOUTH); // (0, -4) from (0, -3)
        d52 = 99999;
        dir52 = null;

        l172 = l157.add(Direction.NORTH); // (0, 4) from (0, 3)
        d172 = 99999;
        dir172 = null;

        l116 = l115.add(Direction.EAST); // (4, 0) from (3, 0)
        d116 = 99999;
        dir116 = null;

        l93 = l109.add(Direction.SOUTHWEST); // (-4, -1) from (-3, 0)
        d93 = 99999;
        dir93 = null;

        l123 = l109.add(Direction.NORTHWEST); // (-4, 1) from (-3, 0)
        d123 = 99999;
        dir123 = null;

        l51 = l67.add(Direction.SOUTHWEST); // (-1, -4) from (0, -3)
        d51 = 99999;
        dir51 = null;

        l171 = l157.add(Direction.NORTHWEST); // (-1, 4) from (0, 3)
        d171 = 99999;
        dir171 = null;

        l53 = l67.add(Direction.SOUTHEAST); // (1, -4) from (0, -3)
        d53 = 99999;
        dir53 = null;

        l173 = l157.add(Direction.NORTHEAST); // (1, 4) from (0, 3)
        d173 = 99999;
        dir173 = null;

        l101 = l115.add(Direction.SOUTHEAST); // (4, -1) from (3, 0)
        d101 = 99999;
        dir101 = null;

        l131 = l115.add(Direction.NORTHEAST); // (4, 1) from (3, 0)
        d131 = 99999;
        dir131 = null;



        if (rc.onTheMap(l111)) { // check (-1, 0)
            if (!rc.isLocationOccupied(l111)) { 
                if (d111 > d112) { // from (0, 0)
                    d111 = d112;
                    dir111 = Direction.WEST;
                }
                d111 += rc.senseRubble(l111) + 10;
            }
        }

        if (rc.onTheMap(l97)) { // check (0, -1)
            if (!rc.isLocationOccupied(l97)) { 
                if (d97 > d112) { // from (0, 0)
                    d97 = d112;
                    dir97 = Direction.SOUTH;
                }
                if (d97 > d111) { // from (-1, 0)
                    d97 = d111;
                    dir97 = dir111;
                }
                d97 += rc.senseRubble(l97) + 10;
            }
        }

        if (rc.onTheMap(l127)) { // check (0, 1)
            if (!rc.isLocationOccupied(l127)) { 
                if (d127 > d112) { // from (0, 0)
                    d127 = d112;
                    dir127 = Direction.NORTH;
                }
                if (d127 > d111) { // from (-1, 0)
                    d127 = d111;
                    dir127 = dir111;
                }
                d127 += rc.senseRubble(l127) + 10;
            }
        }

        if (rc.onTheMap(l113)) { // check (1, 0)
            if (!rc.isLocationOccupied(l113)) { 
                if (d113 > d112) { // from (0, 0)
                    d113 = d112;
                    dir113 = Direction.EAST;
                }
                if (d113 > d97) { // from (0, -1)
                    d113 = d97;
                    dir113 = dir97;
                }
                if (d113 > d127) { // from (0, 1)
                    d113 = d127;
                    dir113 = dir127;
                }
                d113 += rc.senseRubble(l113) + 10;
            }
        }

        if (rc.onTheMap(l96)) { // check (-1, -1)
            if (!rc.isLocationOccupied(l96)) { 
                if (d96 > d112) { // from (0, 0)
                    d96 = d112;
                    dir96 = Direction.SOUTHWEST;
                }
                if (d96 > d111) { // from (-1, 0)
                    d96 = d111;
                    dir96 = dir111;
                }
                if (d96 > d97) { // from (0, -1)
                    d96 = d97;
                    dir96 = dir97;
                }
                d96 += rc.senseRubble(l96) + 10;
            }
        }

        if (rc.onTheMap(l126)) { // check (-1, 1)
            if (!rc.isLocationOccupied(l126)) { 
                if (d126 > d112) { // from (0, 0)
                    d126 = d112;
                    dir126 = Direction.NORTHWEST;
                }
                if (d126 > d111) { // from (-1, 0)
                    d126 = d111;
                    dir126 = dir111;
                }
                if (d126 > d127) { // from (0, 1)
                    d126 = d127;
                    dir126 = dir127;
                }
                d126 += rc.senseRubble(l126) + 10;
            }
        }

        if (rc.onTheMap(l98)) { // check (1, -1)
            if (!rc.isLocationOccupied(l98)) { 
                if (d98 > d112) { // from (0, 0)
                    d98 = d112;
                    dir98 = Direction.SOUTHEAST;
                }
                if (d98 > d97) { // from (0, -1)
                    d98 = d97;
                    dir98 = dir97;
                }
                if (d98 > d113) { // from (1, 0)
                    d98 = d113;
                    dir98 = dir113;
                }
                d98 += rc.senseRubble(l98) + 10;
            }
        }

        if (rc.onTheMap(l128)) { // check (1, 1)
            if (!rc.isLocationOccupied(l128)) { 
                if (d128 > d112) { // from (0, 0)
                    d128 = d112;
                    dir128 = Direction.NORTHEAST;
                }
                if (d128 > d127) { // from (0, 1)
                    d128 = d127;
                    dir128 = dir127;
                }
                if (d128 > d113) { // from (1, 0)
                    d128 = d113;
                    dir128 = dir113;
                }
                d128 += rc.senseRubble(l128) + 10;
            }
        }

        if (rc.onTheMap(l110)) { // check (-2, 0)
            if (d110 > d111) { // from (-1, 0)
                d110 = d111;
                dir110 = dir111;
            }
            if (d110 > d96) { // from (-1, -1)
                d110 = d96;
                dir110 = dir96;
            }
            if (d110 > d126) { // from (-1, 1)
                d110 = d126;
                dir110 = dir126;
            }
            d110 += rc.senseRubble(l110) + 10;
        }

        if (rc.onTheMap(l82)) { // check (0, -2)
            if (d82 > d97) { // from (0, -1)
                d82 = d97;
                dir82 = dir97;
            }
            if (d82 > d96) { // from (-1, -1)
                d82 = d96;
                dir82 = dir96;
            }
            if (d82 > d98) { // from (1, -1)
                d82 = d98;
                dir82 = dir98;
            }
            d82 += rc.senseRubble(l82) + 10;
        }

        if (rc.onTheMap(l142)) { // check (0, 2)
            if (d142 > d127) { // from (0, 1)
                d142 = d127;
                dir142 = dir127;
            }
            if (d142 > d126) { // from (-1, 1)
                d142 = d126;
                dir142 = dir126;
            }
            if (d142 > d128) { // from (1, 1)
                d142 = d128;
                dir142 = dir128;
            }
            d142 += rc.senseRubble(l142) + 10;
        }

        if (rc.onTheMap(l114)) { // check (2, 0)
            if (d114 > d113) { // from (1, 0)
                d114 = d113;
                dir114 = dir113;
            }
            if (d114 > d98) { // from (1, -1)
                d114 = d98;
                dir114 = dir98;
            }
            if (d114 > d128) { // from (1, 1)
                d114 = d128;
                dir114 = dir128;
            }
            d114 += rc.senseRubble(l114) + 10;
        }

        if (rc.onTheMap(l95)) { // check (-2, -1)
            if (d95 > d111) { // from (-1, 0)
                d95 = d111;
                dir95 = dir111;
            }
            if (d95 > d96) { // from (-1, -1)
                d95 = d96;
                dir95 = dir96;
            }
            if (d95 > d110) { // from (-2, 0)
                d95 = d110;
                dir95 = dir110;
            }
            d95 += rc.senseRubble(l95) + 10;
        }

        if (rc.onTheMap(l125)) { // check (-2, 1)
            if (d125 > d111) { // from (-1, 0)
                d125 = d111;
                dir125 = dir111;
            }
            if (d125 > d126) { // from (-1, 1)
                d125 = d126;
                dir125 = dir126;
            }
            if (d125 > d110) { // from (-2, 0)
                d125 = d110;
                dir125 = dir110;
            }
            d125 += rc.senseRubble(l125) + 10;
        }

        if (rc.onTheMap(l81)) { // check (-1, -2)
            if (d81 > d97) { // from (0, -1)
                d81 = d97;
                dir81 = dir97;
            }
            if (d81 > d96) { // from (-1, -1)
                d81 = d96;
                dir81 = dir96;
            }
            if (d81 > d82) { // from (0, -2)
                d81 = d82;
                dir81 = dir82;
            }
            if (d81 > d95) { // from (-2, -1)
                d81 = d95;
                dir81 = dir95;
            }
            d81 += rc.senseRubble(l81) + 10;
        }

        if (rc.onTheMap(l141)) { // check (-1, 2)
            if (d141 > d127) { // from (0, 1)
                d141 = d127;
                dir141 = dir127;
            }
            if (d141 > d126) { // from (-1, 1)
                d141 = d126;
                dir141 = dir126;
            }
            if (d141 > d142) { // from (0, 2)
                d141 = d142;
                dir141 = dir142;
            }
            if (d141 > d125) { // from (-2, 1)
                d141 = d125;
                dir141 = dir125;
            }
            d141 += rc.senseRubble(l141) + 10;
        }

        if (rc.onTheMap(l83)) { // check (1, -2)
            if (d83 > d97) { // from (0, -1)
                d83 = d97;
                dir83 = dir97;
            }
            if (d83 > d98) { // from (1, -1)
                d83 = d98;
                dir83 = dir98;
            }
            if (d83 > d82) { // from (0, -2)
                d83 = d82;
                dir83 = dir82;
            }
            d83 += rc.senseRubble(l83) + 10;
        }

        if (rc.onTheMap(l143)) { // check (1, 2)
            if (d143 > d127) { // from (0, 1)
                d143 = d127;
                dir143 = dir127;
            }
            if (d143 > d128) { // from (1, 1)
                d143 = d128;
                dir143 = dir128;
            }
            if (d143 > d142) { // from (0, 2)
                d143 = d142;
                dir143 = dir142;
            }
            d143 += rc.senseRubble(l143) + 10;
        }

        if (rc.onTheMap(l99)) { // check (2, -1)
            if (d99 > d113) { // from (1, 0)
                d99 = d113;
                dir99 = dir113;
            }
            if (d99 > d98) { // from (1, -1)
                d99 = d98;
                dir99 = dir98;
            }
            if (d99 > d114) { // from (2, 0)
                d99 = d114;
                dir99 = dir114;
            }
            if (d99 > d83) { // from (1, -2)
                d99 = d83;
                dir99 = dir83;
            }
            d99 += rc.senseRubble(l99) + 10;
        }

        if (rc.onTheMap(l129)) { // check (2, 1)
            if (d129 > d113) { // from (1, 0)
                d129 = d113;
                dir129 = dir113;
            }
            if (d129 > d128) { // from (1, 1)
                d129 = d128;
                dir129 = dir128;
            }
            if (d129 > d114) { // from (2, 0)
                d129 = d114;
                dir129 = dir114;
            }
            if (d129 > d143) { // from (1, 2)
                d129 = d143;
                dir129 = dir143;
            }
            d129 += rc.senseRubble(l129) + 10;
        }

        if (rc.onTheMap(l80)) { // check (-2, -2)
            if (d80 > d96) { // from (-1, -1)
                d80 = d96;
                dir80 = dir96;
            }
            if (d80 > d95) { // from (-2, -1)
                d80 = d95;
                dir80 = dir95;
            }
            if (d80 > d81) { // from (-1, -2)
                d80 = d81;
                dir80 = dir81;
            }
            d80 += rc.senseRubble(l80) + 10;
        }

        if (rc.onTheMap(l140)) { // check (-2, 2)
            if (d140 > d126) { // from (-1, 1)
                d140 = d126;
                dir140 = dir126;
            }
            if (d140 > d125) { // from (-2, 1)
                d140 = d125;
                dir140 = dir125;
            }
            if (d140 > d141) { // from (-1, 2)
                d140 = d141;
                dir140 = dir141;
            }
            d140 += rc.senseRubble(l140) + 10;
        }

        if (rc.onTheMap(l84)) { // check (2, -2)
            if (d84 > d98) { // from (1, -1)
                d84 = d98;
                dir84 = dir98;
            }
            if (d84 > d83) { // from (1, -2)
                d84 = d83;
                dir84 = dir83;
            }
            if (d84 > d99) { // from (2, -1)
                d84 = d99;
                dir84 = dir99;
            }
            d84 += rc.senseRubble(l84) + 10;
        }

        if (rc.onTheMap(l144)) { // check (2, 2)
            if (d144 > d128) { // from (1, 1)
                d144 = d128;
                dir144 = dir128;
            }
            if (d144 > d143) { // from (1, 2)
                d144 = d143;
                dir144 = dir143;
            }
            if (d144 > d129) { // from (2, 1)
                d144 = d129;
                dir144 = dir129;
            }
            d144 += rc.senseRubble(l144) + 10;
        }

        if (rc.onTheMap(l109)) { // check (-3, 0)
            if (d109 > d110) { // from (-2, 0)
                d109 = d110;
                dir109 = dir110;
            }
            if (d109 > d95) { // from (-2, -1)
                d109 = d95;
                dir109 = dir95;
            }
            if (d109 > d125) { // from (-2, 1)
                d109 = d125;
                dir109 = dir125;
            }
            d109 += rc.senseRubble(l109) + 10;
        }

        if (rc.onTheMap(l67)) { // check (0, -3)
            if (d67 > d82) { // from (0, -2)
                d67 = d82;
                dir67 = dir82;
            }
            if (d67 > d81) { // from (-1, -2)
                d67 = d81;
                dir67 = dir81;
            }
            if (d67 > d83) { // from (1, -2)
                d67 = d83;
                dir67 = dir83;
            }
            d67 += rc.senseRubble(l67) + 10;
        }

        if (rc.onTheMap(l157)) { // check (0, 3)
            if (d157 > d142) { // from (0, 2)
                d157 = d142;
                dir157 = dir142;
            }
            if (d157 > d141) { // from (-1, 2)
                d157 = d141;
                dir157 = dir141;
            }
            if (d157 > d143) { // from (1, 2)
                d157 = d143;
                dir157 = dir143;
            }
            d157 += rc.senseRubble(l157) + 10;
        }

        if (rc.onTheMap(l115)) { // check (3, 0)
            if (d115 > d114) { // from (2, 0)
                d115 = d114;
                dir115 = dir114;
            }
            if (d115 > d99) { // from (2, -1)
                d115 = d99;
                dir115 = dir99;
            }
            if (d115 > d129) { // from (2, 1)
                d115 = d129;
                dir115 = dir129;
            }
            d115 += rc.senseRubble(l115) + 10;
        }

        if (rc.onTheMap(l94)) { // check (-3, -1)
            if (d94 > d110) { // from (-2, 0)
                d94 = d110;
                dir94 = dir110;
            }
            if (d94 > d95) { // from (-2, -1)
                d94 = d95;
                dir94 = dir95;
            }
            if (d94 > d80) { // from (-2, -2)
                d94 = d80;
                dir94 = dir80;
            }
            if (d94 > d109) { // from (-3, 0)
                d94 = d109;
                dir94 = dir109;
            }
            d94 += rc.senseRubble(l94) + 10;
        }

        if (rc.onTheMap(l124)) { // check (-3, 1)
            if (d124 > d110) { // from (-2, 0)
                d124 = d110;
                dir124 = dir110;
            }
            if (d124 > d125) { // from (-2, 1)
                d124 = d125;
                dir124 = dir125;
            }
            if (d124 > d140) { // from (-2, 2)
                d124 = d140;
                dir124 = dir140;
            }
            if (d124 > d109) { // from (-3, 0)
                d124 = d109;
                dir124 = dir109;
            }
            d124 += rc.senseRubble(l124) + 10;
        }

        if (rc.onTheMap(l66)) { // check (-1, -3)
            if (d66 > d82) { // from (0, -2)
                d66 = d82;
                dir66 = dir82;
            }
            if (d66 > d81) { // from (-1, -2)
                d66 = d81;
                dir66 = dir81;
            }
            if (d66 > d80) { // from (-2, -2)
                d66 = d80;
                dir66 = dir80;
            }
            if (d66 > d67) { // from (0, -3)
                d66 = d67;
                dir66 = dir67;
            }
            d66 += rc.senseRubble(l66) + 10;
        }

        if (rc.onTheMap(l156)) { // check (-1, 3)
            if (d156 > d142) { // from (0, 2)
                d156 = d142;
                dir156 = dir142;
            }
            if (d156 > d141) { // from (-1, 2)
                d156 = d141;
                dir156 = dir141;
            }
            if (d156 > d140) { // from (-2, 2)
                d156 = d140;
                dir156 = dir140;
            }
            if (d156 > d157) { // from (0, 3)
                d156 = d157;
                dir156 = dir157;
            }
            d156 += rc.senseRubble(l156) + 10;
        }

        if (rc.onTheMap(l68)) { // check (1, -3)
            if (d68 > d82) { // from (0, -2)
                d68 = d82;
                dir68 = dir82;
            }
            if (d68 > d83) { // from (1, -2)
                d68 = d83;
                dir68 = dir83;
            }
            if (d68 > d84) { // from (2, -2)
                d68 = d84;
                dir68 = dir84;
            }
            if (d68 > d67) { // from (0, -3)
                d68 = d67;
                dir68 = dir67;
            }
            d68 += rc.senseRubble(l68) + 10;
        }

        if (rc.onTheMap(l158)) { // check (1, 3)
            if (d158 > d142) { // from (0, 2)
                d158 = d142;
                dir158 = dir142;
            }
            if (d158 > d143) { // from (1, 2)
                d158 = d143;
                dir158 = dir143;
            }
            if (d158 > d144) { // from (2, 2)
                d158 = d144;
                dir158 = dir144;
            }
            if (d158 > d157) { // from (0, 3)
                d158 = d157;
                dir158 = dir157;
            }
            d158 += rc.senseRubble(l158) + 10;
        }

        if (rc.onTheMap(l100)) { // check (3, -1)
            if (d100 > d114) { // from (2, 0)
                d100 = d114;
                dir100 = dir114;
            }
            if (d100 > d99) { // from (2, -1)
                d100 = d99;
                dir100 = dir99;
            }
            if (d100 > d84) { // from (2, -2)
                d100 = d84;
                dir100 = dir84;
            }
            if (d100 > d115) { // from (3, 0)
                d100 = d115;
                dir100 = dir115;
            }
            d100 += rc.senseRubble(l100) + 10;
        }

        if (rc.onTheMap(l130)) { // check (3, 1)
            if (d130 > d114) { // from (2, 0)
                d130 = d114;
                dir130 = dir114;
            }
            if (d130 > d129) { // from (2, 1)
                d130 = d129;
                dir130 = dir129;
            }
            if (d130 > d144) { // from (2, 2)
                d130 = d144;
                dir130 = dir144;
            }
            if (d130 > d115) { // from (3, 0)
                d130 = d115;
                dir130 = dir115;
            }
            d130 += rc.senseRubble(l130) + 10;
        }

        if (rc.onTheMap(l79)) { // check (-3, -2)
            if (d79 > d95) { // from (-2, -1)
                d79 = d95;
                dir79 = dir95;
            }
            if (d79 > d80) { // from (-2, -2)
                d79 = d80;
                dir79 = dir80;
            }
            if (d79 > d94) { // from (-3, -1)
                d79 = d94;
                dir79 = dir94;
            }
            d79 += rc.senseRubble(l79) + 10;
        }

        if (rc.onTheMap(l139)) { // check (-3, 2)
            if (d139 > d125) { // from (-2, 1)
                d139 = d125;
                dir139 = dir125;
            }
            if (d139 > d140) { // from (-2, 2)
                d139 = d140;
                dir139 = dir140;
            }
            if (d139 > d124) { // from (-3, 1)
                d139 = d124;
                dir139 = dir124;
            }
            d139 += rc.senseRubble(l139) + 10;
        }

        if (rc.onTheMap(l65)) { // check (-2, -3)
            if (d65 > d81) { // from (-1, -2)
                d65 = d81;
                dir65 = dir81;
            }
            if (d65 > d80) { // from (-2, -2)
                d65 = d80;
                dir65 = dir80;
            }
            if (d65 > d66) { // from (-1, -3)
                d65 = d66;
                dir65 = dir66;
            }
            if (d65 > d79) { // from (-3, -2)
                d65 = d79;
                dir65 = dir79;
            }
            d65 += rc.senseRubble(l65) + 10;
        }

        if (rc.onTheMap(l155)) { // check (-2, 3)
            if (d155 > d141) { // from (-1, 2)
                d155 = d141;
                dir155 = dir141;
            }
            if (d155 > d140) { // from (-2, 2)
                d155 = d140;
                dir155 = dir140;
            }
            if (d155 > d156) { // from (-1, 3)
                d155 = d156;
                dir155 = dir156;
            }
            if (d155 > d139) { // from (-3, 2)
                d155 = d139;
                dir155 = dir139;
            }
            d155 += rc.senseRubble(l155) + 10;
        }

        if (rc.onTheMap(l69)) { // check (2, -3)
            if (d69 > d83) { // from (1, -2)
                d69 = d83;
                dir69 = dir83;
            }
            if (d69 > d84) { // from (2, -2)
                d69 = d84;
                dir69 = dir84;
            }
            if (d69 > d68) { // from (1, -3)
                d69 = d68;
                dir69 = dir68;
            }
            d69 += rc.senseRubble(l69) + 10;
        }

        if (rc.onTheMap(l159)) { // check (2, 3)
            if (d159 > d143) { // from (1, 2)
                d159 = d143;
                dir159 = dir143;
            }
            if (d159 > d144) { // from (2, 2)
                d159 = d144;
                dir159 = dir144;
            }
            if (d159 > d158) { // from (1, 3)
                d159 = d158;
                dir159 = dir158;
            }
            d159 += rc.senseRubble(l159) + 10;
        }

        if (rc.onTheMap(l85)) { // check (3, -2)
            if (d85 > d99) { // from (2, -1)
                d85 = d99;
                dir85 = dir99;
            }
            if (d85 > d84) { // from (2, -2)
                d85 = d84;
                dir85 = dir84;
            }
            if (d85 > d100) { // from (3, -1)
                d85 = d100;
                dir85 = dir100;
            }
            if (d85 > d69) { // from (2, -3)
                d85 = d69;
                dir85 = dir69;
            }
            d85 += rc.senseRubble(l85) + 10;
        }

        if (rc.onTheMap(l145)) { // check (3, 2)
            if (d145 > d129) { // from (2, 1)
                d145 = d129;
                dir145 = dir129;
            }
            if (d145 > d144) { // from (2, 2)
                d145 = d144;
                dir145 = dir144;
            }
            if (d145 > d130) { // from (3, 1)
                d145 = d130;
                dir145 = dir130;
            }
            if (d145 > d159) { // from (2, 3)
                d145 = d159;
                dir145 = dir159;
            }
            d145 += rc.senseRubble(l145) + 10;
        }

        if (rc.onTheMap(l108)) { // check (-4, 0)
            if (d108 > d109) { // from (-3, 0)
                d108 = d109;
                dir108 = dir109;
            }
            if (d108 > d94) { // from (-3, -1)
                d108 = d94;
                dir108 = dir94;
            }
            if (d108 > d124) { // from (-3, 1)
                d108 = d124;
                dir108 = dir124;
            }
            d108 += rc.senseRubble(l108) + 10;
        }

        if (rc.onTheMap(l52)) { // check (0, -4)
            if (d52 > d67) { // from (0, -3)
                d52 = d67;
                dir52 = dir67;
            }
            if (d52 > d66) { // from (-1, -3)
                d52 = d66;
                dir52 = dir66;
            }
            if (d52 > d68) { // from (1, -3)
                d52 = d68;
                dir52 = dir68;
            }
            d52 += rc.senseRubble(l52) + 10;
        }

        if (rc.onTheMap(l172)) { // check (0, 4)
            if (d172 > d157) { // from (0, 3)
                d172 = d157;
                dir172 = dir157;
            }
            if (d172 > d156) { // from (-1, 3)
                d172 = d156;
                dir172 = dir156;
            }
            if (d172 > d158) { // from (1, 3)
                d172 = d158;
                dir172 = dir158;
            }
            d172 += rc.senseRubble(l172) + 10;
        }

        if (rc.onTheMap(l116)) { // check (4, 0)
            if (d116 > d115) { // from (3, 0)
                d116 = d115;
                dir116 = dir115;
            }
            if (d116 > d100) { // from (3, -1)
                d116 = d100;
                dir116 = dir100;
            }
            if (d116 > d130) { // from (3, 1)
                d116 = d130;
                dir116 = dir130;
            }
            d116 += rc.senseRubble(l116) + 10;
        }

        if (rc.onTheMap(l93)) { // check (-4, -1)
            if (d93 > d109) { // from (-3, 0)
                d93 = d109;
                dir93 = dir109;
            }
            if (d93 > d94) { // from (-3, -1)
                d93 = d94;
                dir93 = dir94;
            }
            if (d93 > d79) { // from (-3, -2)
                d93 = d79;
                dir93 = dir79;
            }
            if (d93 > d108) { // from (-4, 0)
                d93 = d108;
                dir93 = dir108;
            }
            d93 += rc.senseRubble(l93) + 10;
        }

        if (rc.onTheMap(l123)) { // check (-4, 1)
            if (d123 > d109) { // from (-3, 0)
                d123 = d109;
                dir123 = dir109;
            }
            if (d123 > d124) { // from (-3, 1)
                d123 = d124;
                dir123 = dir124;
            }
            if (d123 > d139) { // from (-3, 2)
                d123 = d139;
                dir123 = dir139;
            }
            if (d123 > d108) { // from (-4, 0)
                d123 = d108;
                dir123 = dir108;
            }
            d123 += rc.senseRubble(l123) + 10;
        }

        if (rc.onTheMap(l51)) { // check (-1, -4)
            if (d51 > d67) { // from (0, -3)
                d51 = d67;
                dir51 = dir67;
            }
            if (d51 > d66) { // from (-1, -3)
                d51 = d66;
                dir51 = dir66;
            }
            if (d51 > d65) { // from (-2, -3)
                d51 = d65;
                dir51 = dir65;
            }
            if (d51 > d52) { // from (0, -4)
                d51 = d52;
                dir51 = dir52;
            }
            d51 += rc.senseRubble(l51) + 10;
        }

        if (rc.onTheMap(l171)) { // check (-1, 4)
            if (d171 > d157) { // from (0, 3)
                d171 = d157;
                dir171 = dir157;
            }
            if (d171 > d156) { // from (-1, 3)
                d171 = d156;
                dir171 = dir156;
            }
            if (d171 > d155) { // from (-2, 3)
                d171 = d155;
                dir171 = dir155;
            }
            if (d171 > d172) { // from (0, 4)
                d171 = d172;
                dir171 = dir172;
            }
            d171 += rc.senseRubble(l171) + 10;
        }

        if (rc.onTheMap(l53)) { // check (1, -4)
            if (d53 > d67) { // from (0, -3)
                d53 = d67;
                dir53 = dir67;
            }
            if (d53 > d68) { // from (1, -3)
                d53 = d68;
                dir53 = dir68;
            }
            if (d53 > d69) { // from (2, -3)
                d53 = d69;
                dir53 = dir69;
            }
            if (d53 > d52) { // from (0, -4)
                d53 = d52;
                dir53 = dir52;
            }
            d53 += rc.senseRubble(l53) + 10;
        }

        if (rc.onTheMap(l173)) { // check (1, 4)
            if (d173 > d157) { // from (0, 3)
                d173 = d157;
                dir173 = dir157;
            }
            if (d173 > d158) { // from (1, 3)
                d173 = d158;
                dir173 = dir158;
            }
            if (d173 > d159) { // from (2, 3)
                d173 = d159;
                dir173 = dir159;
            }
            if (d173 > d172) { // from (0, 4)
                d173 = d172;
                dir173 = dir172;
            }
            d173 += rc.senseRubble(l173) + 10;
        }

        if (rc.onTheMap(l101)) { // check (4, -1)
            if (d101 > d115) { // from (3, 0)
                d101 = d115;
                dir101 = dir115;
            }
            if (d101 > d100) { // from (3, -1)
                d101 = d100;
                dir101 = dir100;
            }
            if (d101 > d85) { // from (3, -2)
                d101 = d85;
                dir101 = dir85;
            }
            if (d101 > d116) { // from (4, 0)
                d101 = d116;
                dir101 = dir116;
            }
            d101 += rc.senseRubble(l101) + 10;
        }

        if (rc.onTheMap(l131)) { // check (4, 1)
            if (d131 > d115) { // from (3, 0)
                d131 = d115;
                dir131 = dir115;
            }
            if (d131 > d130) { // from (3, 1)
                d131 = d130;
                dir131 = dir130;
            }
            if (d131 > d145) { // from (3, 2)
                d131 = d145;
                dir131 = dir145;
            }
            if (d131 > d116) { // from (4, 0)
                d131 = d116;
                dir131 = dir116;
            }
            d131 += rc.senseRubble(l131) + 10;
        }


        // //System.out.println\("LOCAL DISTANCES:");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + d171 + "\t" + d172 + "\t" + d173 + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + d155 + "\t" + d156 + "\t" + d157 + "\t" + d158 + "\t" + d159 + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + d139 + "\t" + d140 + "\t" + d141 + "\t" + d142 + "\t" + d143 + "\t" + d144 + "\t" + d145 + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + d123 + "\t" + d124 + "\t" + d125 + "\t" + d126 + "\t" + d127 + "\t" + d128 + "\t" + d129 + "\t" + d130 + "\t" + d131 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + d108 + "\t" + d109 + "\t" + d110 + "\t" + d111 + "\t" + d112 + "\t" + d113 + "\t" + d114 + "\t" + d115 + "\t" + d116 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + d93 + "\t" + d94 + "\t" + d95 + "\t" + d96 + "\t" + d97 + "\t" + d98 + "\t" + d99 + "\t" + d100 + "\t" + d101 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + d79 + "\t" + d80 + "\t" + d81 + "\t" + d82 + "\t" + d83 + "\t" + d84 + "\t" + d85 + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + d65 + "\t" + d66 + "\t" + d67 + "\t" + d68 + "\t" + d69 + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + d51 + "\t" + d52 + "\t" + d53 + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("DIRECTIONS:");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + dir171 + "\t" + dir172 + "\t" + dir173 + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + dir155 + "\t" + dir156 + "\t" + dir157 + "\t" + dir158 + "\t" + dir159 + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + dir139 + "\t" + dir140 + "\t" + dir141 + "\t" + dir142 + "\t" + dir143 + "\t" + dir144 + "\t" + dir145 + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + dir123 + "\t" + dir124 + "\t" + dir125 + "\t" + dir126 + "\t" + dir127 + "\t" + dir128 + "\t" + dir129 + "\t" + dir130 + "\t" + dir131 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + dir108 + "\t" + dir109 + "\t" + dir110 + "\t" + dir111 + "\t" + dir112 + "\t" + dir113 + "\t" + dir114 + "\t" + dir115 + "\t" + dir116 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + dir93 + "\t" + dir94 + "\t" + dir95 + "\t" + dir96 + "\t" + dir97 + "\t" + dir98 + "\t" + dir99 + "\t" + dir100 + "\t" + dir101 + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + dir79 + "\t" + dir80 + "\t" + dir81 + "\t" + dir82 + "\t" + dir83 + "\t" + dir84 + "\t" + dir85 + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + dir65 + "\t" + dir66 + "\t" + dir67 + "\t" + dir68 + "\t" + dir69 + "\t" + "\t" + "\t" + "\t" + "\t");
        // //System.out.println\("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + dir51 + "\t" + dir52 + "\t" + dir53 + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");

        int target_dx = target.x - l112.x;
        int target_dy = target.y - l112.y;
        switch (target_dx) {
                case -4:
                    switch (target_dy) {
                        case -1:
                            return dir93; // destination is at relative location (-4, -1)
                        case 0:
                            return dir108; // destination is at relative location (-4, 0)
                        case 1:
                            return dir123; // destination is at relative location (-4, 1)
                    }
                    break;
                case -3:
                    switch (target_dy) {
                        case -2:
                            return dir79; // destination is at relative location (-3, -2)
                        case -1:
                            return dir94; // destination is at relative location (-3, -1)
                        case 0:
                            return dir109; // destination is at relative location (-3, 0)
                        case 1:
                            return dir124; // destination is at relative location (-3, 1)
                        case 2:
                            return dir139; // destination is at relative location (-3, 2)
                    }
                    break;
                case -2:
                    switch (target_dy) {
                        case -3:
                            return dir65; // destination is at relative location (-2, -3)
                        case -2:
                            return dir80; // destination is at relative location (-2, -2)
                        case -1:
                            return dir95; // destination is at relative location (-2, -1)
                        case 0:
                            return dir110; // destination is at relative location (-2, 0)
                        case 1:
                            return dir125; // destination is at relative location (-2, 1)
                        case 2:
                            return dir140; // destination is at relative location (-2, 2)
                        case 3:
                            return dir155; // destination is at relative location (-2, 3)
                    }
                    break;
                case -1:
                    switch (target_dy) {
                        case -4:
                            return dir51; // destination is at relative location (-1, -4)
                        case -3:
                            return dir66; // destination is at relative location (-1, -3)
                        case -2:
                            return dir81; // destination is at relative location (-1, -2)
                        case -1:
                            return dir96; // destination is at relative location (-1, -1)
                        case 0:
                            return dir111; // destination is at relative location (-1, 0)
                        case 1:
                            return dir126; // destination is at relative location (-1, 1)
                        case 2:
                            return dir141; // destination is at relative location (-1, 2)
                        case 3:
                            return dir156; // destination is at relative location (-1, 3)
                        case 4:
                            return dir171; // destination is at relative location (-1, 4)
                    }
                    break;
                case 0:
                    switch (target_dy) {
                        case -4:
                            return dir52; // destination is at relative location (0, -4)
                        case -3:
                            return dir67; // destination is at relative location (0, -3)
                        case -2:
                            return dir82; // destination is at relative location (0, -2)
                        case -1:
                            return dir97; // destination is at relative location (0, -1)
                        case 0:
                            return dir112; // destination is at relative location (0, 0)
                        case 1:
                            return dir127; // destination is at relative location (0, 1)
                        case 2:
                            return dir142; // destination is at relative location (0, 2)
                        case 3:
                            return dir157; // destination is at relative location (0, 3)
                        case 4:
                            return dir172; // destination is at relative location (0, 4)
                    }
                    break;
                case 1:
                    switch (target_dy) {
                        case -4:
                            return dir53; // destination is at relative location (1, -4)
                        case -3:
                            return dir68; // destination is at relative location (1, -3)
                        case -2:
                            return dir83; // destination is at relative location (1, -2)
                        case -1:
                            return dir98; // destination is at relative location (1, -1)
                        case 0:
                            return dir113; // destination is at relative location (1, 0)
                        case 1:
                            return dir128; // destination is at relative location (1, 1)
                        case 2:
                            return dir143; // destination is at relative location (1, 2)
                        case 3:
                            return dir158; // destination is at relative location (1, 3)
                        case 4:
                            return dir173; // destination is at relative location (1, 4)
                    }
                    break;
                case 2:
                    switch (target_dy) {
                        case -3:
                            return dir69; // destination is at relative location (2, -3)
                        case -2:
                            return dir84; // destination is at relative location (2, -2)
                        case -1:
                            return dir99; // destination is at relative location (2, -1)
                        case 0:
                            return dir114; // destination is at relative location (2, 0)
                        case 1:
                            return dir129; // destination is at relative location (2, 1)
                        case 2:
                            return dir144; // destination is at relative location (2, 2)
                        case 3:
                            return dir159; // destination is at relative location (2, 3)
                    }
                    break;
                case 3:
                    switch (target_dy) {
                        case -2:
                            return dir85; // destination is at relative location (3, -2)
                        case -1:
                            return dir100; // destination is at relative location (3, -1)
                        case 0:
                            return dir115; // destination is at relative location (3, 0)
                        case 1:
                            return dir130; // destination is at relative location (3, 1)
                        case 2:
                            return dir145; // destination is at relative location (3, 2)
                    }
                    break;
                case 4:
                    switch (target_dy) {
                        case -1:
                            return dir101; // destination is at relative location (4, -1)
                        case 0:
                            return dir116; // destination is at relative location (4, 0)
                        case 1:
                            return dir131; // destination is at relative location (4, 1)
                    }
                    break;
        }
        
        Direction ans = null;
        double bestScore = 0;
        double currDist = Math.sqrt(l112.distanceSquaredTo(target));
        
        double score93 = (currDist - Math.sqrt(l93.distanceSquaredTo(target))) / d93;
        if (score93 > bestScore) {
            bestScore = score93;
            ans = dir93;
        }

        double score108 = (currDist - Math.sqrt(l108.distanceSquaredTo(target))) / d108;
        if (score108 > bestScore) {
            bestScore = score108;
            ans = dir108;
        }

        double score123 = (currDist - Math.sqrt(l123.distanceSquaredTo(target))) / d123;
        if (score123 > bestScore) {
            bestScore = score123;
            ans = dir123;
        }

        double score79 = (currDist - Math.sqrt(l79.distanceSquaredTo(target))) / d79;
        if (score79 > bestScore) {
            bestScore = score79;
            ans = dir79;
        }

        double score94 = (currDist - Math.sqrt(l94.distanceSquaredTo(target))) / d94;
        if (score94 > bestScore) {
            bestScore = score94;
            ans = dir94;
        }

        double score124 = (currDist - Math.sqrt(l124.distanceSquaredTo(target))) / d124;
        if (score124 > bestScore) {
            bestScore = score124;
            ans = dir124;
        }

        double score139 = (currDist - Math.sqrt(l139.distanceSquaredTo(target))) / d139;
        if (score139 > bestScore) {
            bestScore = score139;
            ans = dir139;
        }

        double score65 = (currDist - Math.sqrt(l65.distanceSquaredTo(target))) / d65;
        if (score65 > bestScore) {
            bestScore = score65;
            ans = dir65;
        }

        double score155 = (currDist - Math.sqrt(l155.distanceSquaredTo(target))) / d155;
        if (score155 > bestScore) {
            bestScore = score155;
            ans = dir155;
        }

        double score51 = (currDist - Math.sqrt(l51.distanceSquaredTo(target))) / d51;
        if (score51 > bestScore) {
            bestScore = score51;
            ans = dir51;
        }

        double score66 = (currDist - Math.sqrt(l66.distanceSquaredTo(target))) / d66;
        if (score66 > bestScore) {
            bestScore = score66;
            ans = dir66;
        }

        double score156 = (currDist - Math.sqrt(l156.distanceSquaredTo(target))) / d156;
        if (score156 > bestScore) {
            bestScore = score156;
            ans = dir156;
        }

        double score171 = (currDist - Math.sqrt(l171.distanceSquaredTo(target))) / d171;
        if (score171 > bestScore) {
            bestScore = score171;
            ans = dir171;
        }

        double score52 = (currDist - Math.sqrt(l52.distanceSquaredTo(target))) / d52;
        if (score52 > bestScore) {
            bestScore = score52;
            ans = dir52;
        }

        double score172 = (currDist - Math.sqrt(l172.distanceSquaredTo(target))) / d172;
        if (score172 > bestScore) {
            bestScore = score172;
            ans = dir172;
        }

        double score53 = (currDist - Math.sqrt(l53.distanceSquaredTo(target))) / d53;
        if (score53 > bestScore) {
            bestScore = score53;
            ans = dir53;
        }

        double score68 = (currDist - Math.sqrt(l68.distanceSquaredTo(target))) / d68;
        if (score68 > bestScore) {
            bestScore = score68;
            ans = dir68;
        }

        double score158 = (currDist - Math.sqrt(l158.distanceSquaredTo(target))) / d158;
        if (score158 > bestScore) {
            bestScore = score158;
            ans = dir158;
        }

        double score173 = (currDist - Math.sqrt(l173.distanceSquaredTo(target))) / d173;
        if (score173 > bestScore) {
            bestScore = score173;
            ans = dir173;
        }

        double score69 = (currDist - Math.sqrt(l69.distanceSquaredTo(target))) / d69;
        if (score69 > bestScore) {
            bestScore = score69;
            ans = dir69;
        }

        double score159 = (currDist - Math.sqrt(l159.distanceSquaredTo(target))) / d159;
        if (score159 > bestScore) {
            bestScore = score159;
            ans = dir159;
        }

        double score85 = (currDist - Math.sqrt(l85.distanceSquaredTo(target))) / d85;
        if (score85 > bestScore) {
            bestScore = score85;
            ans = dir85;
        }

        double score100 = (currDist - Math.sqrt(l100.distanceSquaredTo(target))) / d100;
        if (score100 > bestScore) {
            bestScore = score100;
            ans = dir100;
        }

        double score130 = (currDist - Math.sqrt(l130.distanceSquaredTo(target))) / d130;
        if (score130 > bestScore) {
            bestScore = score130;
            ans = dir130;
        }

        double score145 = (currDist - Math.sqrt(l145.distanceSquaredTo(target))) / d145;
        if (score145 > bestScore) {
            bestScore = score145;
            ans = dir145;
        }

        double score101 = (currDist - Math.sqrt(l101.distanceSquaredTo(target))) / d101;
        if (score101 > bestScore) {
            bestScore = score101;
            ans = dir101;
        }

        double score116 = (currDist - Math.sqrt(l116.distanceSquaredTo(target))) / d116;
        if (score116 > bestScore) {
            bestScore = score116;
            ans = dir116;
        }

        double score131 = (currDist - Math.sqrt(l131.distanceSquaredTo(target))) / d131;
        if (score131 > bestScore) {
            bestScore = score131;
            ans = dir131;
        }

        
        return ans;
    }
}
