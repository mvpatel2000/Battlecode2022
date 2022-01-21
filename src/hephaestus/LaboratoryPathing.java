// Inspired by https://github.com/IvanGeffner/battlecode2021/blob/master/thirtyone/BFSMuckraker.java.
package hephaestus;

import battlecode.common.*;

public class LaboratoryPathing implements UnitPathing {
    
    RobotController rc;

    static MapLocation l62; // location representing relative coordinate (-5, -3)
    static int d62; // shortest distance to location from current location
    static Direction dir62; // best direction to take now to optimally get to location

    static MapLocation l77; // location representing relative coordinate (-5, -2)
    static int d77; // shortest distance to location from current location
    static Direction dir77; // best direction to take now to optimally get to location

    static MapLocation l92; // location representing relative coordinate (-5, -1)
    static int d92; // shortest distance to location from current location
    static Direction dir92; // best direction to take now to optimally get to location

    static MapLocation l107; // location representing relative coordinate (-5, 0)
    static int d107; // shortest distance to location from current location
    static Direction dir107; // best direction to take now to optimally get to location

    static MapLocation l122; // location representing relative coordinate (-5, 1)
    static int d122; // shortest distance to location from current location
    static Direction dir122; // best direction to take now to optimally get to location

    static MapLocation l137; // location representing relative coordinate (-5, 2)
    static int d137; // shortest distance to location from current location
    static Direction dir137; // best direction to take now to optimally get to location

    static MapLocation l152; // location representing relative coordinate (-5, 3)
    static int d152; // shortest distance to location from current location
    static Direction dir152; // best direction to take now to optimally get to location

    static MapLocation l48; // location representing relative coordinate (-4, -4)
    static int d48; // shortest distance to location from current location
    static Direction dir48; // best direction to take now to optimally get to location

    static MapLocation l63; // location representing relative coordinate (-4, -3)
    static int d63; // shortest distance to location from current location
    static Direction dir63; // best direction to take now to optimally get to location

    static MapLocation l78; // location representing relative coordinate (-4, -2)
    static int d78; // shortest distance to location from current location
    static Direction dir78; // best direction to take now to optimally get to location

    static MapLocation l93; // location representing relative coordinate (-4, -1)
    static int d93; // shortest distance to location from current location
    static Direction dir93; // best direction to take now to optimally get to location

    static MapLocation l108; // location representing relative coordinate (-4, 0)
    static int d108; // shortest distance to location from current location
    static Direction dir108; // best direction to take now to optimally get to location

    static MapLocation l123; // location representing relative coordinate (-4, 1)
    static int d123; // shortest distance to location from current location
    static Direction dir123; // best direction to take now to optimally get to location

    static MapLocation l138; // location representing relative coordinate (-4, 2)
    static int d138; // shortest distance to location from current location
    static Direction dir138; // best direction to take now to optimally get to location

    static MapLocation l153; // location representing relative coordinate (-4, 3)
    static int d153; // shortest distance to location from current location
    static Direction dir153; // best direction to take now to optimally get to location

    static MapLocation l168; // location representing relative coordinate (-4, 4)
    static int d168; // shortest distance to location from current location
    static Direction dir168; // best direction to take now to optimally get to location

    static MapLocation l34; // location representing relative coordinate (-3, -5)
    static int d34; // shortest distance to location from current location
    static Direction dir34; // best direction to take now to optimally get to location

    static MapLocation l49; // location representing relative coordinate (-3, -4)
    static int d49; // shortest distance to location from current location
    static Direction dir49; // best direction to take now to optimally get to location

    static MapLocation l64; // location representing relative coordinate (-3, -3)
    static int d64; // shortest distance to location from current location
    static Direction dir64; // best direction to take now to optimally get to location

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

    static MapLocation l154; // location representing relative coordinate (-3, 3)
    static int d154; // shortest distance to location from current location
    static Direction dir154; // best direction to take now to optimally get to location

    static MapLocation l169; // location representing relative coordinate (-3, 4)
    static int d169; // shortest distance to location from current location
    static Direction dir169; // best direction to take now to optimally get to location

    static MapLocation l184; // location representing relative coordinate (-3, 5)
    static int d184; // shortest distance to location from current location
    static Direction dir184; // best direction to take now to optimally get to location

    static MapLocation l35; // location representing relative coordinate (-2, -5)
    static int d35; // shortest distance to location from current location
    static Direction dir35; // best direction to take now to optimally get to location

    static MapLocation l50; // location representing relative coordinate (-2, -4)
    static int d50; // shortest distance to location from current location
    static Direction dir50; // best direction to take now to optimally get to location

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

    static MapLocation l170; // location representing relative coordinate (-2, 4)
    static int d170; // shortest distance to location from current location
    static Direction dir170; // best direction to take now to optimally get to location

    static MapLocation l185; // location representing relative coordinate (-2, 5)
    static int d185; // shortest distance to location from current location
    static Direction dir185; // best direction to take now to optimally get to location

    static MapLocation l36; // location representing relative coordinate (-1, -5)
    static int d36; // shortest distance to location from current location
    static Direction dir36; // best direction to take now to optimally get to location

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

    static MapLocation l186; // location representing relative coordinate (-1, 5)
    static int d186; // shortest distance to location from current location
    static Direction dir186; // best direction to take now to optimally get to location

    static MapLocation l37; // location representing relative coordinate (0, -5)
    static int d37; // shortest distance to location from current location
    static Direction dir37; // best direction to take now to optimally get to location

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

    static MapLocation l187; // location representing relative coordinate (0, 5)
    static int d187; // shortest distance to location from current location
    static Direction dir187; // best direction to take now to optimally get to location

    static MapLocation l38; // location representing relative coordinate (1, -5)
    static int d38; // shortest distance to location from current location
    static Direction dir38; // best direction to take now to optimally get to location

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

    static MapLocation l188; // location representing relative coordinate (1, 5)
    static int d188; // shortest distance to location from current location
    static Direction dir188; // best direction to take now to optimally get to location

    static MapLocation l39; // location representing relative coordinate (2, -5)
    static int d39; // shortest distance to location from current location
    static Direction dir39; // best direction to take now to optimally get to location

    static MapLocation l54; // location representing relative coordinate (2, -4)
    static int d54; // shortest distance to location from current location
    static Direction dir54; // best direction to take now to optimally get to location

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

    static MapLocation l174; // location representing relative coordinate (2, 4)
    static int d174; // shortest distance to location from current location
    static Direction dir174; // best direction to take now to optimally get to location

    static MapLocation l189; // location representing relative coordinate (2, 5)
    static int d189; // shortest distance to location from current location
    static Direction dir189; // best direction to take now to optimally get to location

    static MapLocation l40; // location representing relative coordinate (3, -5)
    static int d40; // shortest distance to location from current location
    static Direction dir40; // best direction to take now to optimally get to location

    static MapLocation l55; // location representing relative coordinate (3, -4)
    static int d55; // shortest distance to location from current location
    static Direction dir55; // best direction to take now to optimally get to location

    static MapLocation l70; // location representing relative coordinate (3, -3)
    static int d70; // shortest distance to location from current location
    static Direction dir70; // best direction to take now to optimally get to location

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

    static MapLocation l160; // location representing relative coordinate (3, 3)
    static int d160; // shortest distance to location from current location
    static Direction dir160; // best direction to take now to optimally get to location

    static MapLocation l175; // location representing relative coordinate (3, 4)
    static int d175; // shortest distance to location from current location
    static Direction dir175; // best direction to take now to optimally get to location

    static MapLocation l190; // location representing relative coordinate (3, 5)
    static int d190; // shortest distance to location from current location
    static Direction dir190; // best direction to take now to optimally get to location

    static MapLocation l56; // location representing relative coordinate (4, -4)
    static int d56; // shortest distance to location from current location
    static Direction dir56; // best direction to take now to optimally get to location

    static MapLocation l71; // location representing relative coordinate (4, -3)
    static int d71; // shortest distance to location from current location
    static Direction dir71; // best direction to take now to optimally get to location

    static MapLocation l86; // location representing relative coordinate (4, -2)
    static int d86; // shortest distance to location from current location
    static Direction dir86; // best direction to take now to optimally get to location

    static MapLocation l101; // location representing relative coordinate (4, -1)
    static int d101; // shortest distance to location from current location
    static Direction dir101; // best direction to take now to optimally get to location

    static MapLocation l116; // location representing relative coordinate (4, 0)
    static int d116; // shortest distance to location from current location
    static Direction dir116; // best direction to take now to optimally get to location

    static MapLocation l131; // location representing relative coordinate (4, 1)
    static int d131; // shortest distance to location from current location
    static Direction dir131; // best direction to take now to optimally get to location

    static MapLocation l146; // location representing relative coordinate (4, 2)
    static int d146; // shortest distance to location from current location
    static Direction dir146; // best direction to take now to optimally get to location

    static MapLocation l161; // location representing relative coordinate (4, 3)
    static int d161; // shortest distance to location from current location
    static Direction dir161; // best direction to take now to optimally get to location

    static MapLocation l176; // location representing relative coordinate (4, 4)
    static int d176; // shortest distance to location from current location
    static Direction dir176; // best direction to take now to optimally get to location

    static MapLocation l72; // location representing relative coordinate (5, -3)
    static int d72; // shortest distance to location from current location
    static Direction dir72; // best direction to take now to optimally get to location

    static MapLocation l87; // location representing relative coordinate (5, -2)
    static int d87; // shortest distance to location from current location
    static Direction dir87; // best direction to take now to optimally get to location

    static MapLocation l102; // location representing relative coordinate (5, -1)
    static int d102; // shortest distance to location from current location
    static Direction dir102; // best direction to take now to optimally get to location

    static MapLocation l117; // location representing relative coordinate (5, 0)
    static int d117; // shortest distance to location from current location
    static Direction dir117; // best direction to take now to optimally get to location

    static MapLocation l132; // location representing relative coordinate (5, 1)
    static int d132; // shortest distance to location from current location
    static Direction dir132; // best direction to take now to optimally get to location

    static MapLocation l147; // location representing relative coordinate (5, 2)
    static int d147; // shortest distance to location from current location
    static Direction dir147; // best direction to take now to optimally get to location

    static MapLocation l162; // location representing relative coordinate (5, 3)
    static int d162; // shortest distance to location from current location
    static Direction dir162; // best direction to take now to optimally get to location


    public LaboratoryPathing(RobotController rc) {
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

        l64 = l80.add(Direction.SOUTHWEST); // (-3, -3) from (-2, -2)
        d64 = 99999;
        dir64 = null;

        l154 = l140.add(Direction.NORTHWEST); // (-3, 3) from (-2, 2)
        d154 = 99999;
        dir154 = null;

        l70 = l84.add(Direction.SOUTHEAST); // (3, -3) from (2, -2)
        d70 = 99999;
        dir70 = null;

        l160 = l144.add(Direction.NORTHEAST); // (3, 3) from (2, 2)
        d160 = 99999;
        dir160 = null;

        l78 = l94.add(Direction.SOUTHWEST); // (-4, -2) from (-3, -1)
        d78 = 99999;
        dir78 = null;

        l138 = l124.add(Direction.NORTHWEST); // (-4, 2) from (-3, 1)
        d138 = 99999;
        dir138 = null;

        l50 = l66.add(Direction.SOUTHWEST); // (-2, -4) from (-1, -3)
        d50 = 99999;
        dir50 = null;

        l170 = l156.add(Direction.NORTHWEST); // (-2, 4) from (-1, 3)
        d170 = 99999;
        dir170 = null;

        l54 = l68.add(Direction.SOUTHEAST); // (2, -4) from (1, -3)
        d54 = 99999;
        dir54 = null;

        l174 = l158.add(Direction.NORTHEAST); // (2, 4) from (1, 3)
        d174 = 99999;
        dir174 = null;

        l86 = l100.add(Direction.SOUTHEAST); // (4, -2) from (3, -1)
        d86 = 99999;
        dir86 = null;

        l146 = l130.add(Direction.NORTHEAST); // (4, 2) from (3, 1)
        d146 = 99999;
        dir146 = null;

        l107 = l108.add(Direction.WEST); // (-5, 0) from (-4, 0)
        d107 = 99999;
        dir107 = null;

        l63 = l79.add(Direction.SOUTHWEST); // (-4, -3) from (-3, -2)
        d63 = 99999;
        dir63 = null;

        l153 = l139.add(Direction.NORTHWEST); // (-4, 3) from (-3, 2)
        d153 = 99999;
        dir153 = null;

        l49 = l65.add(Direction.SOUTHWEST); // (-3, -4) from (-2, -3)
        d49 = 99999;
        dir49 = null;

        l169 = l155.add(Direction.NORTHWEST); // (-3, 4) from (-2, 3)
        d169 = 99999;
        dir169 = null;

        l37 = l52.add(Direction.SOUTH); // (0, -5) from (0, -4)
        d37 = 99999;
        dir37 = null;

        l187 = l172.add(Direction.NORTH); // (0, 5) from (0, 4)
        d187 = 99999;
        dir187 = null;

        l55 = l69.add(Direction.SOUTHEAST); // (3, -4) from (2, -3)
        d55 = 99999;
        dir55 = null;

        l175 = l159.add(Direction.NORTHEAST); // (3, 4) from (2, 3)
        d175 = 99999;
        dir175 = null;

        l71 = l85.add(Direction.SOUTHEAST); // (4, -3) from (3, -2)
        d71 = 99999;
        dir71 = null;

        l161 = l145.add(Direction.NORTHEAST); // (4, 3) from (3, 2)
        d161 = 99999;
        dir161 = null;

        l117 = l116.add(Direction.EAST); // (5, 0) from (4, 0)
        d117 = 99999;
        dir117 = null;

        l92 = l108.add(Direction.SOUTHWEST); // (-5, -1) from (-4, 0)
        d92 = 99999;
        dir92 = null;

        l122 = l108.add(Direction.NORTHWEST); // (-5, 1) from (-4, 0)
        d122 = 99999;
        dir122 = null;

        l36 = l52.add(Direction.SOUTHWEST); // (-1, -5) from (0, -4)
        d36 = 99999;
        dir36 = null;

        l186 = l172.add(Direction.NORTHWEST); // (-1, 5) from (0, 4)
        d186 = 99999;
        dir186 = null;

        l38 = l52.add(Direction.SOUTHEAST); // (1, -5) from (0, -4)
        d38 = 99999;
        dir38 = null;

        l188 = l172.add(Direction.NORTHEAST); // (1, 5) from (0, 4)
        d188 = 99999;
        dir188 = null;

        l102 = l116.add(Direction.SOUTHEAST); // (5, -1) from (4, 0)
        d102 = 99999;
        dir102 = null;

        l132 = l116.add(Direction.NORTHEAST); // (5, 1) from (4, 0)
        d132 = 99999;
        dir132 = null;

        l77 = l93.add(Direction.SOUTHWEST); // (-5, -2) from (-4, -1)
        d77 = 99999;
        dir77 = null;

        l137 = l123.add(Direction.NORTHWEST); // (-5, 2) from (-4, 1)
        d137 = 99999;
        dir137 = null;

        l35 = l51.add(Direction.SOUTHWEST); // (-2, -5) from (-1, -4)
        d35 = 99999;
        dir35 = null;

        l185 = l171.add(Direction.NORTHWEST); // (-2, 5) from (-1, 4)
        d185 = 99999;
        dir185 = null;

        l39 = l53.add(Direction.SOUTHEAST); // (2, -5) from (1, -4)
        d39 = 99999;
        dir39 = null;

        l189 = l173.add(Direction.NORTHEAST); // (2, 5) from (1, 4)
        d189 = 99999;
        dir189 = null;

        l87 = l101.add(Direction.SOUTHEAST); // (5, -2) from (4, -1)
        d87 = 99999;
        dir87 = null;

        l147 = l131.add(Direction.NORTHEAST); // (5, 2) from (4, 1)
        d147 = 99999;
        dir147 = null;

        l48 = l64.add(Direction.SOUTHWEST); // (-4, -4) from (-3, -3)
        d48 = 99999;
        dir48 = null;

        l168 = l154.add(Direction.NORTHWEST); // (-4, 4) from (-3, 3)
        d168 = 99999;
        dir168 = null;

        l56 = l70.add(Direction.SOUTHEAST); // (4, -4) from (3, -3)
        d56 = 99999;
        dir56 = null;

        l176 = l160.add(Direction.NORTHEAST); // (4, 4) from (3, 3)
        d176 = 99999;
        dir176 = null;

        l62 = l78.add(Direction.SOUTHWEST); // (-5, -3) from (-4, -2)
        d62 = 99999;
        dir62 = null;

        l152 = l138.add(Direction.NORTHWEST); // (-5, 3) from (-4, 2)
        d152 = 99999;
        dir152 = null;

        l34 = l50.add(Direction.SOUTHWEST); // (-3, -5) from (-2, -4)
        d34 = 99999;
        dir34 = null;

        l184 = l170.add(Direction.NORTHWEST); // (-3, 5) from (-2, 4)
        d184 = 99999;
        dir184 = null;

        l40 = l54.add(Direction.SOUTHEAST); // (3, -5) from (2, -4)
        d40 = 99999;
        dir40 = null;

        l190 = l174.add(Direction.NORTHEAST); // (3, 5) from (2, 4)
        d190 = 99999;
        dir190 = null;

        l72 = l86.add(Direction.SOUTHEAST); // (5, -3) from (4, -2)
        d72 = 99999;
        dir72 = null;

        l162 = l146.add(Direction.NORTHEAST); // (5, 3) from (4, 2)
        d162 = 99999;
        dir162 = null;



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

        if (rc.onTheMap(l64)) { // check (-3, -3)
            if (d64 > d80) { // from (-2, -2)
                d64 = d80;
                dir64 = dir80;
            }
            if (d64 > d79) { // from (-3, -2)
                d64 = d79;
                dir64 = dir79;
            }
            if (d64 > d65) { // from (-2, -3)
                d64 = d65;
                dir64 = dir65;
            }
            d64 += rc.senseRubble(l64) + 10;
        }

        if (rc.onTheMap(l154)) { // check (-3, 3)
            if (d154 > d140) { // from (-2, 2)
                d154 = d140;
                dir154 = dir140;
            }
            if (d154 > d139) { // from (-3, 2)
                d154 = d139;
                dir154 = dir139;
            }
            if (d154 > d155) { // from (-2, 3)
                d154 = d155;
                dir154 = dir155;
            }
            d154 += rc.senseRubble(l154) + 10;
        }

        if (rc.onTheMap(l70)) { // check (3, -3)
            if (d70 > d84) { // from (2, -2)
                d70 = d84;
                dir70 = dir84;
            }
            if (d70 > d69) { // from (2, -3)
                d70 = d69;
                dir70 = dir69;
            }
            if (d70 > d85) { // from (3, -2)
                d70 = d85;
                dir70 = dir85;
            }
            d70 += rc.senseRubble(l70) + 10;
        }

        if (rc.onTheMap(l160)) { // check (3, 3)
            if (d160 > d144) { // from (2, 2)
                d160 = d144;
                dir160 = dir144;
            }
            if (d160 > d159) { // from (2, 3)
                d160 = d159;
                dir160 = dir159;
            }
            if (d160 > d145) { // from (3, 2)
                d160 = d145;
                dir160 = dir145;
            }
            d160 += rc.senseRubble(l160) + 10;
        }

        if (rc.onTheMap(l78)) { // check (-4, -2)
            if (d78 > d94) { // from (-3, -1)
                d78 = d94;
                dir78 = dir94;
            }
            if (d78 > d79) { // from (-3, -2)
                d78 = d79;
                dir78 = dir79;
            }
            if (d78 > d93) { // from (-4, -1)
                d78 = d93;
                dir78 = dir93;
            }
            if (d78 > d64) { // from (-3, -3)
                d78 = d64;
                dir78 = dir64;
            }
            d78 += rc.senseRubble(l78) + 10;
        }

        if (rc.onTheMap(l138)) { // check (-4, 2)
            if (d138 > d124) { // from (-3, 1)
                d138 = d124;
                dir138 = dir124;
            }
            if (d138 > d139) { // from (-3, 2)
                d138 = d139;
                dir138 = dir139;
            }
            if (d138 > d123) { // from (-4, 1)
                d138 = d123;
                dir138 = dir123;
            }
            if (d138 > d154) { // from (-3, 3)
                d138 = d154;
                dir138 = dir154;
            }
            d138 += rc.senseRubble(l138) + 10;
        }

        if (rc.onTheMap(l50)) { // check (-2, -4)
            if (d50 > d66) { // from (-1, -3)
                d50 = d66;
                dir50 = dir66;
            }
            if (d50 > d65) { // from (-2, -3)
                d50 = d65;
                dir50 = dir65;
            }
            if (d50 > d51) { // from (-1, -4)
                d50 = d51;
                dir50 = dir51;
            }
            if (d50 > d64) { // from (-3, -3)
                d50 = d64;
                dir50 = dir64;
            }
            d50 += rc.senseRubble(l50) + 10;
        }

        if (rc.onTheMap(l170)) { // check (-2, 4)
            if (d170 > d156) { // from (-1, 3)
                d170 = d156;
                dir170 = dir156;
            }
            if (d170 > d155) { // from (-2, 3)
                d170 = d155;
                dir170 = dir155;
            }
            if (d170 > d171) { // from (-1, 4)
                d170 = d171;
                dir170 = dir171;
            }
            if (d170 > d154) { // from (-3, 3)
                d170 = d154;
                dir170 = dir154;
            }
            d170 += rc.senseRubble(l170) + 10;
        }

        if (rc.onTheMap(l54)) { // check (2, -4)
            if (d54 > d68) { // from (1, -3)
                d54 = d68;
                dir54 = dir68;
            }
            if (d54 > d69) { // from (2, -3)
                d54 = d69;
                dir54 = dir69;
            }
            if (d54 > d53) { // from (1, -4)
                d54 = d53;
                dir54 = dir53;
            }
            if (d54 > d70) { // from (3, -3)
                d54 = d70;
                dir54 = dir70;
            }
            d54 += rc.senseRubble(l54) + 10;
        }

        if (rc.onTheMap(l174)) { // check (2, 4)
            if (d174 > d158) { // from (1, 3)
                d174 = d158;
                dir174 = dir158;
            }
            if (d174 > d159) { // from (2, 3)
                d174 = d159;
                dir174 = dir159;
            }
            if (d174 > d173) { // from (1, 4)
                d174 = d173;
                dir174 = dir173;
            }
            if (d174 > d160) { // from (3, 3)
                d174 = d160;
                dir174 = dir160;
            }
            d174 += rc.senseRubble(l174) + 10;
        }

        if (rc.onTheMap(l86)) { // check (4, -2)
            if (d86 > d100) { // from (3, -1)
                d86 = d100;
                dir86 = dir100;
            }
            if (d86 > d85) { // from (3, -2)
                d86 = d85;
                dir86 = dir85;
            }
            if (d86 > d101) { // from (4, -1)
                d86 = d101;
                dir86 = dir101;
            }
            if (d86 > d70) { // from (3, -3)
                d86 = d70;
                dir86 = dir70;
            }
            d86 += rc.senseRubble(l86) + 10;
        }

        if (rc.onTheMap(l146)) { // check (4, 2)
            if (d146 > d130) { // from (3, 1)
                d146 = d130;
                dir146 = dir130;
            }
            if (d146 > d145) { // from (3, 2)
                d146 = d145;
                dir146 = dir145;
            }
            if (d146 > d131) { // from (4, 1)
                d146 = d131;
                dir146 = dir131;
            }
            if (d146 > d160) { // from (3, 3)
                d146 = d160;
                dir146 = dir160;
            }
            d146 += rc.senseRubble(l146) + 10;
        }

        if (rc.onTheMap(l107)) { // check (-5, 0)
            if (d107 > d108) { // from (-4, 0)
                d107 = d108;
                dir107 = dir108;
            }
            if (d107 > d93) { // from (-4, -1)
                d107 = d93;
                dir107 = dir93;
            }
            if (d107 > d123) { // from (-4, 1)
                d107 = d123;
                dir107 = dir123;
            }
            d107 += rc.senseRubble(l107) + 10;
        }

        if (rc.onTheMap(l63)) { // check (-4, -3)
            if (d63 > d79) { // from (-3, -2)
                d63 = d79;
                dir63 = dir79;
            }
            if (d63 > d64) { // from (-3, -3)
                d63 = d64;
                dir63 = dir64;
            }
            if (d63 > d78) { // from (-4, -2)
                d63 = d78;
                dir63 = dir78;
            }
            d63 += rc.senseRubble(l63) + 10;
        }

        if (rc.onTheMap(l153)) { // check (-4, 3)
            if (d153 > d139) { // from (-3, 2)
                d153 = d139;
                dir153 = dir139;
            }
            if (d153 > d154) { // from (-3, 3)
                d153 = d154;
                dir153 = dir154;
            }
            if (d153 > d138) { // from (-4, 2)
                d153 = d138;
                dir153 = dir138;
            }
            d153 += rc.senseRubble(l153) + 10;
        }

        if (rc.onTheMap(l49)) { // check (-3, -4)
            if (d49 > d65) { // from (-2, -3)
                d49 = d65;
                dir49 = dir65;
            }
            if (d49 > d64) { // from (-3, -3)
                d49 = d64;
                dir49 = dir64;
            }
            if (d49 > d50) { // from (-2, -4)
                d49 = d50;
                dir49 = dir50;
            }
            if (d49 > d63) { // from (-4, -3)
                d49 = d63;
                dir49 = dir63;
            }
            d49 += rc.senseRubble(l49) + 10;
        }

        if (rc.onTheMap(l169)) { // check (-3, 4)
            if (d169 > d155) { // from (-2, 3)
                d169 = d155;
                dir169 = dir155;
            }
            if (d169 > d154) { // from (-3, 3)
                d169 = d154;
                dir169 = dir154;
            }
            if (d169 > d170) { // from (-2, 4)
                d169 = d170;
                dir169 = dir170;
            }
            if (d169 > d153) { // from (-4, 3)
                d169 = d153;
                dir169 = dir153;
            }
            d169 += rc.senseRubble(l169) + 10;
        }

        if (rc.onTheMap(l37)) { // check (0, -5)
            if (d37 > d52) { // from (0, -4)
                d37 = d52;
                dir37 = dir52;
            }
            if (d37 > d51) { // from (-1, -4)
                d37 = d51;
                dir37 = dir51;
            }
            if (d37 > d53) { // from (1, -4)
                d37 = d53;
                dir37 = dir53;
            }
            d37 += rc.senseRubble(l37) + 10;
        }

        if (rc.onTheMap(l187)) { // check (0, 5)
            if (d187 > d172) { // from (0, 4)
                d187 = d172;
                dir187 = dir172;
            }
            if (d187 > d171) { // from (-1, 4)
                d187 = d171;
                dir187 = dir171;
            }
            if (d187 > d173) { // from (1, 4)
                d187 = d173;
                dir187 = dir173;
            }
            d187 += rc.senseRubble(l187) + 10;
        }

        if (rc.onTheMap(l55)) { // check (3, -4)
            if (d55 > d69) { // from (2, -3)
                d55 = d69;
                dir55 = dir69;
            }
            if (d55 > d70) { // from (3, -3)
                d55 = d70;
                dir55 = dir70;
            }
            if (d55 > d54) { // from (2, -4)
                d55 = d54;
                dir55 = dir54;
            }
            d55 += rc.senseRubble(l55) + 10;
        }

        if (rc.onTheMap(l175)) { // check (3, 4)
            if (d175 > d159) { // from (2, 3)
                d175 = d159;
                dir175 = dir159;
            }
            if (d175 > d160) { // from (3, 3)
                d175 = d160;
                dir175 = dir160;
            }
            if (d175 > d174) { // from (2, 4)
                d175 = d174;
                dir175 = dir174;
            }
            d175 += rc.senseRubble(l175) + 10;
        }

        if (rc.onTheMap(l71)) { // check (4, -3)
            if (d71 > d85) { // from (3, -2)
                d71 = d85;
                dir71 = dir85;
            }
            if (d71 > d70) { // from (3, -3)
                d71 = d70;
                dir71 = dir70;
            }
            if (d71 > d86) { // from (4, -2)
                d71 = d86;
                dir71 = dir86;
            }
            if (d71 > d55) { // from (3, -4)
                d71 = d55;
                dir71 = dir55;
            }
            d71 += rc.senseRubble(l71) + 10;
        }

        if (rc.onTheMap(l161)) { // check (4, 3)
            if (d161 > d145) { // from (3, 2)
                d161 = d145;
                dir161 = dir145;
            }
            if (d161 > d160) { // from (3, 3)
                d161 = d160;
                dir161 = dir160;
            }
            if (d161 > d146) { // from (4, 2)
                d161 = d146;
                dir161 = dir146;
            }
            if (d161 > d175) { // from (3, 4)
                d161 = d175;
                dir161 = dir175;
            }
            d161 += rc.senseRubble(l161) + 10;
        }

        if (rc.onTheMap(l117)) { // check (5, 0)
            if (d117 > d116) { // from (4, 0)
                d117 = d116;
                dir117 = dir116;
            }
            if (d117 > d101) { // from (4, -1)
                d117 = d101;
                dir117 = dir101;
            }
            if (d117 > d131) { // from (4, 1)
                d117 = d131;
                dir117 = dir131;
            }
            d117 += rc.senseRubble(l117) + 10;
        }

        if (rc.onTheMap(l92)) { // check (-5, -1)
            if (d92 > d108) { // from (-4, 0)
                d92 = d108;
                dir92 = dir108;
            }
            if (d92 > d93) { // from (-4, -1)
                d92 = d93;
                dir92 = dir93;
            }
            if (d92 > d78) { // from (-4, -2)
                d92 = d78;
                dir92 = dir78;
            }
            if (d92 > d107) { // from (-5, 0)
                d92 = d107;
                dir92 = dir107;
            }
            d92 += rc.senseRubble(l92) + 10;
        }

        if (rc.onTheMap(l122)) { // check (-5, 1)
            if (d122 > d108) { // from (-4, 0)
                d122 = d108;
                dir122 = dir108;
            }
            if (d122 > d123) { // from (-4, 1)
                d122 = d123;
                dir122 = dir123;
            }
            if (d122 > d138) { // from (-4, 2)
                d122 = d138;
                dir122 = dir138;
            }
            if (d122 > d107) { // from (-5, 0)
                d122 = d107;
                dir122 = dir107;
            }
            d122 += rc.senseRubble(l122) + 10;
        }

        if (rc.onTheMap(l36)) { // check (-1, -5)
            if (d36 > d52) { // from (0, -4)
                d36 = d52;
                dir36 = dir52;
            }
            if (d36 > d51) { // from (-1, -4)
                d36 = d51;
                dir36 = dir51;
            }
            if (d36 > d50) { // from (-2, -4)
                d36 = d50;
                dir36 = dir50;
            }
            if (d36 > d37) { // from (0, -5)
                d36 = d37;
                dir36 = dir37;
            }
            d36 += rc.senseRubble(l36) + 10;
        }

        if (rc.onTheMap(l186)) { // check (-1, 5)
            if (d186 > d172) { // from (0, 4)
                d186 = d172;
                dir186 = dir172;
            }
            if (d186 > d171) { // from (-1, 4)
                d186 = d171;
                dir186 = dir171;
            }
            if (d186 > d170) { // from (-2, 4)
                d186 = d170;
                dir186 = dir170;
            }
            if (d186 > d187) { // from (0, 5)
                d186 = d187;
                dir186 = dir187;
            }
            d186 += rc.senseRubble(l186) + 10;
        }

        if (rc.onTheMap(l38)) { // check (1, -5)
            if (d38 > d52) { // from (0, -4)
                d38 = d52;
                dir38 = dir52;
            }
            if (d38 > d53) { // from (1, -4)
                d38 = d53;
                dir38 = dir53;
            }
            if (d38 > d54) { // from (2, -4)
                d38 = d54;
                dir38 = dir54;
            }
            if (d38 > d37) { // from (0, -5)
                d38 = d37;
                dir38 = dir37;
            }
            d38 += rc.senseRubble(l38) + 10;
        }

        if (rc.onTheMap(l188)) { // check (1, 5)
            if (d188 > d172) { // from (0, 4)
                d188 = d172;
                dir188 = dir172;
            }
            if (d188 > d173) { // from (1, 4)
                d188 = d173;
                dir188 = dir173;
            }
            if (d188 > d174) { // from (2, 4)
                d188 = d174;
                dir188 = dir174;
            }
            if (d188 > d187) { // from (0, 5)
                d188 = d187;
                dir188 = dir187;
            }
            d188 += rc.senseRubble(l188) + 10;
        }

        if (rc.onTheMap(l102)) { // check (5, -1)
            if (d102 > d116) { // from (4, 0)
                d102 = d116;
                dir102 = dir116;
            }
            if (d102 > d101) { // from (4, -1)
                d102 = d101;
                dir102 = dir101;
            }
            if (d102 > d86) { // from (4, -2)
                d102 = d86;
                dir102 = dir86;
            }
            if (d102 > d117) { // from (5, 0)
                d102 = d117;
                dir102 = dir117;
            }
            d102 += rc.senseRubble(l102) + 10;
        }

        if (rc.onTheMap(l132)) { // check (5, 1)
            if (d132 > d116) { // from (4, 0)
                d132 = d116;
                dir132 = dir116;
            }
            if (d132 > d131) { // from (4, 1)
                d132 = d131;
                dir132 = dir131;
            }
            if (d132 > d146) { // from (4, 2)
                d132 = d146;
                dir132 = dir146;
            }
            if (d132 > d117) { // from (5, 0)
                d132 = d117;
                dir132 = dir117;
            }
            d132 += rc.senseRubble(l132) + 10;
        }

        if (rc.onTheMap(l77)) { // check (-5, -2)
            if (d77 > d93) { // from (-4, -1)
                d77 = d93;
                dir77 = dir93;
            }
            if (d77 > d78) { // from (-4, -2)
                d77 = d78;
                dir77 = dir78;
            }
            if (d77 > d63) { // from (-4, -3)
                d77 = d63;
                dir77 = dir63;
            }
            if (d77 > d92) { // from (-5, -1)
                d77 = d92;
                dir77 = dir92;
            }
            d77 += rc.senseRubble(l77) + 10;
        }

        if (rc.onTheMap(l137)) { // check (-5, 2)
            if (d137 > d123) { // from (-4, 1)
                d137 = d123;
                dir137 = dir123;
            }
            if (d137 > d138) { // from (-4, 2)
                d137 = d138;
                dir137 = dir138;
            }
            if (d137 > d153) { // from (-4, 3)
                d137 = d153;
                dir137 = dir153;
            }
            if (d137 > d122) { // from (-5, 1)
                d137 = d122;
                dir137 = dir122;
            }
            d137 += rc.senseRubble(l137) + 10;
        }

        if (rc.onTheMap(l35)) { // check (-2, -5)
            if (d35 > d51) { // from (-1, -4)
                d35 = d51;
                dir35 = dir51;
            }
            if (d35 > d50) { // from (-2, -4)
                d35 = d50;
                dir35 = dir50;
            }
            if (d35 > d49) { // from (-3, -4)
                d35 = d49;
                dir35 = dir49;
            }
            if (d35 > d36) { // from (-1, -5)
                d35 = d36;
                dir35 = dir36;
            }
            d35 += rc.senseRubble(l35) + 10;
        }

        if (rc.onTheMap(l185)) { // check (-2, 5)
            if (d185 > d171) { // from (-1, 4)
                d185 = d171;
                dir185 = dir171;
            }
            if (d185 > d170) { // from (-2, 4)
                d185 = d170;
                dir185 = dir170;
            }
            if (d185 > d169) { // from (-3, 4)
                d185 = d169;
                dir185 = dir169;
            }
            if (d185 > d186) { // from (-1, 5)
                d185 = d186;
                dir185 = dir186;
            }
            d185 += rc.senseRubble(l185) + 10;
        }

        if (rc.onTheMap(l39)) { // check (2, -5)
            if (d39 > d53) { // from (1, -4)
                d39 = d53;
                dir39 = dir53;
            }
            if (d39 > d54) { // from (2, -4)
                d39 = d54;
                dir39 = dir54;
            }
            if (d39 > d55) { // from (3, -4)
                d39 = d55;
                dir39 = dir55;
            }
            if (d39 > d38) { // from (1, -5)
                d39 = d38;
                dir39 = dir38;
            }
            d39 += rc.senseRubble(l39) + 10;
        }

        if (rc.onTheMap(l189)) { // check (2, 5)
            if (d189 > d173) { // from (1, 4)
                d189 = d173;
                dir189 = dir173;
            }
            if (d189 > d174) { // from (2, 4)
                d189 = d174;
                dir189 = dir174;
            }
            if (d189 > d175) { // from (3, 4)
                d189 = d175;
                dir189 = dir175;
            }
            if (d189 > d188) { // from (1, 5)
                d189 = d188;
                dir189 = dir188;
            }
            d189 += rc.senseRubble(l189) + 10;
        }

        if (rc.onTheMap(l87)) { // check (5, -2)
            if (d87 > d101) { // from (4, -1)
                d87 = d101;
                dir87 = dir101;
            }
            if (d87 > d86) { // from (4, -2)
                d87 = d86;
                dir87 = dir86;
            }
            if (d87 > d71) { // from (4, -3)
                d87 = d71;
                dir87 = dir71;
            }
            if (d87 > d102) { // from (5, -1)
                d87 = d102;
                dir87 = dir102;
            }
            d87 += rc.senseRubble(l87) + 10;
        }

        if (rc.onTheMap(l147)) { // check (5, 2)
            if (d147 > d131) { // from (4, 1)
                d147 = d131;
                dir147 = dir131;
            }
            if (d147 > d146) { // from (4, 2)
                d147 = d146;
                dir147 = dir146;
            }
            if (d147 > d161) { // from (4, 3)
                d147 = d161;
                dir147 = dir161;
            }
            if (d147 > d132) { // from (5, 1)
                d147 = d132;
                dir147 = dir132;
            }
            d147 += rc.senseRubble(l147) + 10;
        }

        if (rc.onTheMap(l48)) { // check (-4, -4)
            if (d48 > d64) { // from (-3, -3)
                d48 = d64;
                dir48 = dir64;
            }
            if (d48 > d63) { // from (-4, -3)
                d48 = d63;
                dir48 = dir63;
            }
            if (d48 > d49) { // from (-3, -4)
                d48 = d49;
                dir48 = dir49;
            }
            d48 += rc.senseRubble(l48) + 10;
        }

        if (rc.onTheMap(l168)) { // check (-4, 4)
            if (d168 > d154) { // from (-3, 3)
                d168 = d154;
                dir168 = dir154;
            }
            if (d168 > d153) { // from (-4, 3)
                d168 = d153;
                dir168 = dir153;
            }
            if (d168 > d169) { // from (-3, 4)
                d168 = d169;
                dir168 = dir169;
            }
            d168 += rc.senseRubble(l168) + 10;
        }

        if (rc.onTheMap(l56)) { // check (4, -4)
            if (d56 > d70) { // from (3, -3)
                d56 = d70;
                dir56 = dir70;
            }
            if (d56 > d55) { // from (3, -4)
                d56 = d55;
                dir56 = dir55;
            }
            if (d56 > d71) { // from (4, -3)
                d56 = d71;
                dir56 = dir71;
            }
            d56 += rc.senseRubble(l56) + 10;
        }

        if (rc.onTheMap(l176)) { // check (4, 4)
            if (d176 > d160) { // from (3, 3)
                d176 = d160;
                dir176 = dir160;
            }
            if (d176 > d175) { // from (3, 4)
                d176 = d175;
                dir176 = dir175;
            }
            if (d176 > d161) { // from (4, 3)
                d176 = d161;
                dir176 = dir161;
            }
            d176 += rc.senseRubble(l176) + 10;
        }

        if (rc.onTheMap(l62)) { // check (-5, -3)
            if (d62 > d78) { // from (-4, -2)
                d62 = d78;
                dir62 = dir78;
            }
            if (d62 > d63) { // from (-4, -3)
                d62 = d63;
                dir62 = dir63;
            }
            if (d62 > d77) { // from (-5, -2)
                d62 = d77;
                dir62 = dir77;
            }
            if (d62 > d48) { // from (-4, -4)
                d62 = d48;
                dir62 = dir48;
            }
            d62 += rc.senseRubble(l62) + 10;
        }

        if (rc.onTheMap(l152)) { // check (-5, 3)
            if (d152 > d138) { // from (-4, 2)
                d152 = d138;
                dir152 = dir138;
            }
            if (d152 > d153) { // from (-4, 3)
                d152 = d153;
                dir152 = dir153;
            }
            if (d152 > d137) { // from (-5, 2)
                d152 = d137;
                dir152 = dir137;
            }
            if (d152 > d168) { // from (-4, 4)
                d152 = d168;
                dir152 = dir168;
            }
            d152 += rc.senseRubble(l152) + 10;
        }

        if (rc.onTheMap(l34)) { // check (-3, -5)
            if (d34 > d50) { // from (-2, -4)
                d34 = d50;
                dir34 = dir50;
            }
            if (d34 > d49) { // from (-3, -4)
                d34 = d49;
                dir34 = dir49;
            }
            if (d34 > d35) { // from (-2, -5)
                d34 = d35;
                dir34 = dir35;
            }
            if (d34 > d48) { // from (-4, -4)
                d34 = d48;
                dir34 = dir48;
            }
            d34 += rc.senseRubble(l34) + 10;
        }

        if (rc.onTheMap(l184)) { // check (-3, 5)
            if (d184 > d170) { // from (-2, 4)
                d184 = d170;
                dir184 = dir170;
            }
            if (d184 > d169) { // from (-3, 4)
                d184 = d169;
                dir184 = dir169;
            }
            if (d184 > d185) { // from (-2, 5)
                d184 = d185;
                dir184 = dir185;
            }
            if (d184 > d168) { // from (-4, 4)
                d184 = d168;
                dir184 = dir168;
            }
            d184 += rc.senseRubble(l184) + 10;
        }

        if (rc.onTheMap(l40)) { // check (3, -5)
            if (d40 > d54) { // from (2, -4)
                d40 = d54;
                dir40 = dir54;
            }
            if (d40 > d55) { // from (3, -4)
                d40 = d55;
                dir40 = dir55;
            }
            if (d40 > d39) { // from (2, -5)
                d40 = d39;
                dir40 = dir39;
            }
            if (d40 > d56) { // from (4, -4)
                d40 = d56;
                dir40 = dir56;
            }
            d40 += rc.senseRubble(l40) + 10;
        }

        if (rc.onTheMap(l190)) { // check (3, 5)
            if (d190 > d174) { // from (2, 4)
                d190 = d174;
                dir190 = dir174;
            }
            if (d190 > d175) { // from (3, 4)
                d190 = d175;
                dir190 = dir175;
            }
            if (d190 > d189) { // from (2, 5)
                d190 = d189;
                dir190 = dir189;
            }
            if (d190 > d176) { // from (4, 4)
                d190 = d176;
                dir190 = dir176;
            }
            d190 += rc.senseRubble(l190) + 10;
        }

        if (rc.onTheMap(l72)) { // check (5, -3)
            if (d72 > d86) { // from (4, -2)
                d72 = d86;
                dir72 = dir86;
            }
            if (d72 > d71) { // from (4, -3)
                d72 = d71;
                dir72 = dir71;
            }
            if (d72 > d87) { // from (5, -2)
                d72 = d87;
                dir72 = dir87;
            }
            if (d72 > d56) { // from (4, -4)
                d72 = d56;
                dir72 = dir56;
            }
            d72 += rc.senseRubble(l72) + 10;
        }

        if (rc.onTheMap(l162)) { // check (5, 3)
            if (d162 > d146) { // from (4, 2)
                d162 = d146;
                dir162 = dir146;
            }
            if (d162 > d161) { // from (4, 3)
                d162 = d161;
                dir162 = dir161;
            }
            if (d162 > d147) { // from (5, 2)
                d162 = d147;
                dir162 = dir147;
            }
            if (d162 > d176) { // from (4, 4)
                d162 = d176;
                dir162 = dir176;
            }
            d162 += rc.senseRubble(l162) + 10;
        }


        // System.out.println("LOCAL DISTANCES:");
        // System.out.println("\t" + "\t" + "\t" + "\t" + "\t" + d184 + "\t" + d185 + "\t" + d186 + "\t" + d187 + "\t" + d188 + "\t" + d189 + "\t" + d190 + "\t" + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + d168 + "\t" + d169 + "\t" + d170 + "\t" + d171 + "\t" + d172 + "\t" + d173 + "\t" + d174 + "\t" + d175 + "\t" + d176 + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d152 + "\t" + d153 + "\t" + d154 + "\t" + d155 + "\t" + d156 + "\t" + d157 + "\t" + d158 + "\t" + d159 + "\t" + d160 + "\t" + d161 + "\t" + d162 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d137 + "\t" + d138 + "\t" + d139 + "\t" + d140 + "\t" + d141 + "\t" + d142 + "\t" + d143 + "\t" + d144 + "\t" + d145 + "\t" + d146 + "\t" + d147 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d122 + "\t" + d123 + "\t" + d124 + "\t" + d125 + "\t" + d126 + "\t" + d127 + "\t" + d128 + "\t" + d129 + "\t" + d130 + "\t" + d131 + "\t" + d132 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d107 + "\t" + d108 + "\t" + d109 + "\t" + d110 + "\t" + d111 + "\t" + d112 + "\t" + d113 + "\t" + d114 + "\t" + d115 + "\t" + d116 + "\t" + d117 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d92 + "\t" + d93 + "\t" + d94 + "\t" + d95 + "\t" + d96 + "\t" + d97 + "\t" + d98 + "\t" + d99 + "\t" + d100 + "\t" + d101 + "\t" + d102 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d77 + "\t" + d78 + "\t" + d79 + "\t" + d80 + "\t" + d81 + "\t" + d82 + "\t" + d83 + "\t" + d84 + "\t" + d85 + "\t" + d86 + "\t" + d87 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + d62 + "\t" + d63 + "\t" + d64 + "\t" + d65 + "\t" + d66 + "\t" + d67 + "\t" + d68 + "\t" + d69 + "\t" + d70 + "\t" + d71 + "\t" + d72 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + d48 + "\t" + d49 + "\t" + d50 + "\t" + d51 + "\t" + d52 + "\t" + d53 + "\t" + d54 + "\t" + d55 + "\t" + d56 + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + "\t" + d34 + "\t" + d35 + "\t" + d36 + "\t" + d37 + "\t" + d38 + "\t" + d39 + "\t" + d40 + "\t" + "\t" + "\t" + "\t");
        // System.out.println("DIRECTIONS:");
        // System.out.println("\t" + "\t" + "\t" + "\t" + "\t" + dir184 + "\t" + dir185 + "\t" + dir186 + "\t" + dir187 + "\t" + dir188 + "\t" + dir189 + "\t" + dir190 + "\t" + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + dir168 + "\t" + dir169 + "\t" + dir170 + "\t" + dir171 + "\t" + dir172 + "\t" + dir173 + "\t" + dir174 + "\t" + dir175 + "\t" + dir176 + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir152 + "\t" + dir153 + "\t" + dir154 + "\t" + dir155 + "\t" + dir156 + "\t" + dir157 + "\t" + dir158 + "\t" + dir159 + "\t" + dir160 + "\t" + dir161 + "\t" + dir162 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir137 + "\t" + dir138 + "\t" + dir139 + "\t" + dir140 + "\t" + dir141 + "\t" + dir142 + "\t" + dir143 + "\t" + dir144 + "\t" + dir145 + "\t" + dir146 + "\t" + dir147 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir122 + "\t" + dir123 + "\t" + dir124 + "\t" + dir125 + "\t" + dir126 + "\t" + dir127 + "\t" + dir128 + "\t" + dir129 + "\t" + dir130 + "\t" + dir131 + "\t" + dir132 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir107 + "\t" + dir108 + "\t" + dir109 + "\t" + dir110 + "\t" + dir111 + "\t" + dir112 + "\t" + dir113 + "\t" + dir114 + "\t" + dir115 + "\t" + dir116 + "\t" + dir117 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir92 + "\t" + dir93 + "\t" + dir94 + "\t" + dir95 + "\t" + dir96 + "\t" + dir97 + "\t" + dir98 + "\t" + dir99 + "\t" + dir100 + "\t" + dir101 + "\t" + dir102 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir77 + "\t" + dir78 + "\t" + dir79 + "\t" + dir80 + "\t" + dir81 + "\t" + dir82 + "\t" + dir83 + "\t" + dir84 + "\t" + dir85 + "\t" + dir86 + "\t" + dir87 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + dir62 + "\t" + dir63 + "\t" + dir64 + "\t" + dir65 + "\t" + dir66 + "\t" + dir67 + "\t" + dir68 + "\t" + dir69 + "\t" + dir70 + "\t" + dir71 + "\t" + dir72 + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + dir48 + "\t" + dir49 + "\t" + dir50 + "\t" + dir51 + "\t" + dir52 + "\t" + dir53 + "\t" + dir54 + "\t" + dir55 + "\t" + dir56 + "\t" + "\t" + "\t");
        // System.out.println("\t" + "\t" + "\t" + "\t" + "\t" + dir34 + "\t" + dir35 + "\t" + dir36 + "\t" + dir37 + "\t" + dir38 + "\t" + dir39 + "\t" + dir40 + "\t" + "\t" + "\t" + "\t");

        int target_dx = target.x - l112.x;
        int target_dy = target.y - l112.y;
        switch (target_dx) {
                case -5:
                    switch (target_dy) {
                        case -3:
                            return dir62; // destination is at relative location (-5, -3)
                        case -2:
                            return dir77; // destination is at relative location (-5, -2)
                        case -1:
                            return dir92; // destination is at relative location (-5, -1)
                        case 0:
                            return dir107; // destination is at relative location (-5, 0)
                        case 1:
                            return dir122; // destination is at relative location (-5, 1)
                        case 2:
                            return dir137; // destination is at relative location (-5, 2)
                        case 3:
                            return dir152; // destination is at relative location (-5, 3)
                    }
                    break;
                case -4:
                    switch (target_dy) {
                        case -4:
                            return dir48; // destination is at relative location (-4, -4)
                        case -3:
                            return dir63; // destination is at relative location (-4, -3)
                        case -2:
                            return dir78; // destination is at relative location (-4, -2)
                        case -1:
                            return dir93; // destination is at relative location (-4, -1)
                        case 0:
                            return dir108; // destination is at relative location (-4, 0)
                        case 1:
                            return dir123; // destination is at relative location (-4, 1)
                        case 2:
                            return dir138; // destination is at relative location (-4, 2)
                        case 3:
                            return dir153; // destination is at relative location (-4, 3)
                        case 4:
                            return dir168; // destination is at relative location (-4, 4)
                    }
                    break;
                case -3:
                    switch (target_dy) {
                        case -5:
                            return dir34; // destination is at relative location (-3, -5)
                        case -4:
                            return dir49; // destination is at relative location (-3, -4)
                        case -3:
                            return dir64; // destination is at relative location (-3, -3)
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
                        case 3:
                            return dir154; // destination is at relative location (-3, 3)
                        case 4:
                            return dir169; // destination is at relative location (-3, 4)
                        case 5:
                            return dir184; // destination is at relative location (-3, 5)
                    }
                    break;
                case -2:
                    switch (target_dy) {
                        case -5:
                            return dir35; // destination is at relative location (-2, -5)
                        case -4:
                            return dir50; // destination is at relative location (-2, -4)
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
                        case 4:
                            return dir170; // destination is at relative location (-2, 4)
                        case 5:
                            return dir185; // destination is at relative location (-2, 5)
                    }
                    break;
                case -1:
                    switch (target_dy) {
                        case -5:
                            return dir36; // destination is at relative location (-1, -5)
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
                        case 5:
                            return dir186; // destination is at relative location (-1, 5)
                    }
                    break;
                case 0:
                    switch (target_dy) {
                        case -5:
                            return dir37; // destination is at relative location (0, -5)
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
                        case 5:
                            return dir187; // destination is at relative location (0, 5)
                    }
                    break;
                case 1:
                    switch (target_dy) {
                        case -5:
                            return dir38; // destination is at relative location (1, -5)
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
                        case 5:
                            return dir188; // destination is at relative location (1, 5)
                    }
                    break;
                case 2:
                    switch (target_dy) {
                        case -5:
                            return dir39; // destination is at relative location (2, -5)
                        case -4:
                            return dir54; // destination is at relative location (2, -4)
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
                        case 4:
                            return dir174; // destination is at relative location (2, 4)
                        case 5:
                            return dir189; // destination is at relative location (2, 5)
                    }
                    break;
                case 3:
                    switch (target_dy) {
                        case -5:
                            return dir40; // destination is at relative location (3, -5)
                        case -4:
                            return dir55; // destination is at relative location (3, -4)
                        case -3:
                            return dir70; // destination is at relative location (3, -3)
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
                        case 3:
                            return dir160; // destination is at relative location (3, 3)
                        case 4:
                            return dir175; // destination is at relative location (3, 4)
                        case 5:
                            return dir190; // destination is at relative location (3, 5)
                    }
                    break;
                case 4:
                    switch (target_dy) {
                        case -4:
                            return dir56; // destination is at relative location (4, -4)
                        case -3:
                            return dir71; // destination is at relative location (4, -3)
                        case -2:
                            return dir86; // destination is at relative location (4, -2)
                        case -1:
                            return dir101; // destination is at relative location (4, -1)
                        case 0:
                            return dir116; // destination is at relative location (4, 0)
                        case 1:
                            return dir131; // destination is at relative location (4, 1)
                        case 2:
                            return dir146; // destination is at relative location (4, 2)
                        case 3:
                            return dir161; // destination is at relative location (4, 3)
                        case 4:
                            return dir176; // destination is at relative location (4, 4)
                    }
                    break;
                case 5:
                    switch (target_dy) {
                        case -3:
                            return dir72; // destination is at relative location (5, -3)
                        case -2:
                            return dir87; // destination is at relative location (5, -2)
                        case -1:
                            return dir102; // destination is at relative location (5, -1)
                        case 0:
                            return dir117; // destination is at relative location (5, 0)
                        case 1:
                            return dir132; // destination is at relative location (5, 1)
                        case 2:
                            return dir147; // destination is at relative location (5, 2)
                        case 3:
                            return dir162; // destination is at relative location (5, 3)
                    }
                    break;
        }
        
        Direction ans = null;
        double bestScore = 0;
        double currDist = Math.sqrt(l112.distanceSquaredTo(target));
        
        double score62 = (currDist - Math.sqrt(l62.distanceSquaredTo(target))) / d62;
        if (score62 > bestScore) {
            bestScore = score62;
            ans = dir62;
        }

        double score77 = (currDist - Math.sqrt(l77.distanceSquaredTo(target))) / d77;
        if (score77 > bestScore) {
            bestScore = score77;
            ans = dir77;
        }

        double score92 = (currDist - Math.sqrt(l92.distanceSquaredTo(target))) / d92;
        if (score92 > bestScore) {
            bestScore = score92;
            ans = dir92;
        }

        double score107 = (currDist - Math.sqrt(l107.distanceSquaredTo(target))) / d107;
        if (score107 > bestScore) {
            bestScore = score107;
            ans = dir107;
        }

        double score122 = (currDist - Math.sqrt(l122.distanceSquaredTo(target))) / d122;
        if (score122 > bestScore) {
            bestScore = score122;
            ans = dir122;
        }

        double score137 = (currDist - Math.sqrt(l137.distanceSquaredTo(target))) / d137;
        if (score137 > bestScore) {
            bestScore = score137;
            ans = dir137;
        }

        double score152 = (currDist - Math.sqrt(l152.distanceSquaredTo(target))) / d152;
        if (score152 > bestScore) {
            bestScore = score152;
            ans = dir152;
        }

        double score48 = (currDist - Math.sqrt(l48.distanceSquaredTo(target))) / d48;
        if (score48 > bestScore) {
            bestScore = score48;
            ans = dir48;
        }

        double score63 = (currDist - Math.sqrt(l63.distanceSquaredTo(target))) / d63;
        if (score63 > bestScore) {
            bestScore = score63;
            ans = dir63;
        }

        double score153 = (currDist - Math.sqrt(l153.distanceSquaredTo(target))) / d153;
        if (score153 > bestScore) {
            bestScore = score153;
            ans = dir153;
        }

        double score168 = (currDist - Math.sqrt(l168.distanceSquaredTo(target))) / d168;
        if (score168 > bestScore) {
            bestScore = score168;
            ans = dir168;
        }

        double score34 = (currDist - Math.sqrt(l34.distanceSquaredTo(target))) / d34;
        if (score34 > bestScore) {
            bestScore = score34;
            ans = dir34;
        }

        double score49 = (currDist - Math.sqrt(l49.distanceSquaredTo(target))) / d49;
        if (score49 > bestScore) {
            bestScore = score49;
            ans = dir49;
        }

        double score169 = (currDist - Math.sqrt(l169.distanceSquaredTo(target))) / d169;
        if (score169 > bestScore) {
            bestScore = score169;
            ans = dir169;
        }

        double score184 = (currDist - Math.sqrt(l184.distanceSquaredTo(target))) / d184;
        if (score184 > bestScore) {
            bestScore = score184;
            ans = dir184;
        }

        double score35 = (currDist - Math.sqrt(l35.distanceSquaredTo(target))) / d35;
        if (score35 > bestScore) {
            bestScore = score35;
            ans = dir35;
        }

        double score185 = (currDist - Math.sqrt(l185.distanceSquaredTo(target))) / d185;
        if (score185 > bestScore) {
            bestScore = score185;
            ans = dir185;
        }

        double score36 = (currDist - Math.sqrt(l36.distanceSquaredTo(target))) / d36;
        if (score36 > bestScore) {
            bestScore = score36;
            ans = dir36;
        }

        double score186 = (currDist - Math.sqrt(l186.distanceSquaredTo(target))) / d186;
        if (score186 > bestScore) {
            bestScore = score186;
            ans = dir186;
        }

        double score37 = (currDist - Math.sqrt(l37.distanceSquaredTo(target))) / d37;
        if (score37 > bestScore) {
            bestScore = score37;
            ans = dir37;
        }

        double score187 = (currDist - Math.sqrt(l187.distanceSquaredTo(target))) / d187;
        if (score187 > bestScore) {
            bestScore = score187;
            ans = dir187;
        }

        double score38 = (currDist - Math.sqrt(l38.distanceSquaredTo(target))) / d38;
        if (score38 > bestScore) {
            bestScore = score38;
            ans = dir38;
        }

        double score188 = (currDist - Math.sqrt(l188.distanceSquaredTo(target))) / d188;
        if (score188 > bestScore) {
            bestScore = score188;
            ans = dir188;
        }

        double score39 = (currDist - Math.sqrt(l39.distanceSquaredTo(target))) / d39;
        if (score39 > bestScore) {
            bestScore = score39;
            ans = dir39;
        }

        double score189 = (currDist - Math.sqrt(l189.distanceSquaredTo(target))) / d189;
        if (score189 > bestScore) {
            bestScore = score189;
            ans = dir189;
        }

        double score40 = (currDist - Math.sqrt(l40.distanceSquaredTo(target))) / d40;
        if (score40 > bestScore) {
            bestScore = score40;
            ans = dir40;
        }

        double score55 = (currDist - Math.sqrt(l55.distanceSquaredTo(target))) / d55;
        if (score55 > bestScore) {
            bestScore = score55;
            ans = dir55;
        }

        double score175 = (currDist - Math.sqrt(l175.distanceSquaredTo(target))) / d175;
        if (score175 > bestScore) {
            bestScore = score175;
            ans = dir175;
        }

        double score190 = (currDist - Math.sqrt(l190.distanceSquaredTo(target))) / d190;
        if (score190 > bestScore) {
            bestScore = score190;
            ans = dir190;
        }

        double score56 = (currDist - Math.sqrt(l56.distanceSquaredTo(target))) / d56;
        if (score56 > bestScore) {
            bestScore = score56;
            ans = dir56;
        }

        double score71 = (currDist - Math.sqrt(l71.distanceSquaredTo(target))) / d71;
        if (score71 > bestScore) {
            bestScore = score71;
            ans = dir71;
        }

        double score161 = (currDist - Math.sqrt(l161.distanceSquaredTo(target))) / d161;
        if (score161 > bestScore) {
            bestScore = score161;
            ans = dir161;
        }

        double score176 = (currDist - Math.sqrt(l176.distanceSquaredTo(target))) / d176;
        if (score176 > bestScore) {
            bestScore = score176;
            ans = dir176;
        }

        double score72 = (currDist - Math.sqrt(l72.distanceSquaredTo(target))) / d72;
        if (score72 > bestScore) {
            bestScore = score72;
            ans = dir72;
        }

        double score87 = (currDist - Math.sqrt(l87.distanceSquaredTo(target))) / d87;
        if (score87 > bestScore) {
            bestScore = score87;
            ans = dir87;
        }

        double score102 = (currDist - Math.sqrt(l102.distanceSquaredTo(target))) / d102;
        if (score102 > bestScore) {
            bestScore = score102;
            ans = dir102;
        }

        double score117 = (currDist - Math.sqrt(l117.distanceSquaredTo(target))) / d117;
        if (score117 > bestScore) {
            bestScore = score117;
            ans = dir117;
        }

        double score132 = (currDist - Math.sqrt(l132.distanceSquaredTo(target))) / d132;
        if (score132 > bestScore) {
            bestScore = score132;
            ans = dir132;
        }

        double score147 = (currDist - Math.sqrt(l147.distanceSquaredTo(target))) / d147;
        if (score147 > bestScore) {
            bestScore = score147;
            ans = dir147;
        }

        double score162 = (currDist - Math.sqrt(l162.distanceSquaredTo(target))) / d162;
        if (score162 > bestScore) {
            bestScore = score162;
            ans = dir162;
        }

        
        return ans;
    }
}