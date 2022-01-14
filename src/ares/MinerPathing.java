// Inspired by https://github.com/IvanGeffner/battlecode2021/blob/master/thirtyone/BFSMuckraker.java.
package ares;

import battlecode.common.*;

public class MinerPathing {
    
    RobotController rc;

    MapLocation l62; // location representing relative coordinate (-5, -3)
    int r62; // rubble at location
    int d62; // shortest distance to location from current location
    Direction dir62; // best direction to take now to optimally get to location

    MapLocation l77; // location representing relative coordinate (-5, -2)
    int r77; // rubble at location
    int d77; // shortest distance to location from current location
    Direction dir77; // best direction to take now to optimally get to location

    MapLocation l92; // location representing relative coordinate (-5, -1)
    int r92; // rubble at location
    int d92; // shortest distance to location from current location
    Direction dir92; // best direction to take now to optimally get to location

    MapLocation l107; // location representing relative coordinate (-5, 0)
    int r107; // rubble at location
    int d107; // shortest distance to location from current location
    Direction dir107; // best direction to take now to optimally get to location

    MapLocation l122; // location representing relative coordinate (-5, 1)
    int r122; // rubble at location
    int d122; // shortest distance to location from current location
    Direction dir122; // best direction to take now to optimally get to location

    MapLocation l137; // location representing relative coordinate (-5, 2)
    int r137; // rubble at location
    int d137; // shortest distance to location from current location
    Direction dir137; // best direction to take now to optimally get to location

    MapLocation l152; // location representing relative coordinate (-5, 3)
    int r152; // rubble at location
    int d152; // shortest distance to location from current location
    Direction dir152; // best direction to take now to optimally get to location

    MapLocation l48; // location representing relative coordinate (-4, -4)
    int r48; // rubble at location
    int d48; // shortest distance to location from current location
    Direction dir48; // best direction to take now to optimally get to location

    MapLocation l63; // location representing relative coordinate (-4, -3)
    int r63; // rubble at location
    int d63; // shortest distance to location from current location
    Direction dir63; // best direction to take now to optimally get to location

    MapLocation l78; // location representing relative coordinate (-4, -2)
    int r78; // rubble at location
    int d78; // shortest distance to location from current location
    Direction dir78; // best direction to take now to optimally get to location

    MapLocation l93; // location representing relative coordinate (-4, -1)
    int r93; // rubble at location
    int d93; // shortest distance to location from current location
    Direction dir93; // best direction to take now to optimally get to location

    MapLocation l108; // location representing relative coordinate (-4, 0)
    int r108; // rubble at location
    int d108; // shortest distance to location from current location
    Direction dir108; // best direction to take now to optimally get to location

    MapLocation l123; // location representing relative coordinate (-4, 1)
    int r123; // rubble at location
    int d123; // shortest distance to location from current location
    Direction dir123; // best direction to take now to optimally get to location

    MapLocation l138; // location representing relative coordinate (-4, 2)
    int r138; // rubble at location
    int d138; // shortest distance to location from current location
    Direction dir138; // best direction to take now to optimally get to location

    MapLocation l153; // location representing relative coordinate (-4, 3)
    int r153; // rubble at location
    int d153; // shortest distance to location from current location
    Direction dir153; // best direction to take now to optimally get to location

    MapLocation l168; // location representing relative coordinate (-4, 4)
    int r168; // rubble at location
    int d168; // shortest distance to location from current location
    Direction dir168; // best direction to take now to optimally get to location

    MapLocation l34; // location representing relative coordinate (-3, -5)
    int r34; // rubble at location
    int d34; // shortest distance to location from current location
    Direction dir34; // best direction to take now to optimally get to location

    MapLocation l49; // location representing relative coordinate (-3, -4)
    int r49; // rubble at location
    int d49; // shortest distance to location from current location
    Direction dir49; // best direction to take now to optimally get to location

    MapLocation l64; // location representing relative coordinate (-3, -3)
    int r64; // rubble at location
    int d64; // shortest distance to location from current location
    Direction dir64; // best direction to take now to optimally get to location

    MapLocation l79; // location representing relative coordinate (-3, -2)
    int r79; // rubble at location
    int d79; // shortest distance to location from current location
    Direction dir79; // best direction to take now to optimally get to location

    MapLocation l94; // location representing relative coordinate (-3, -1)
    int r94; // rubble at location
    int d94; // shortest distance to location from current location
    Direction dir94; // best direction to take now to optimally get to location

    MapLocation l109; // location representing relative coordinate (-3, 0)
    int r109; // rubble at location
    int d109; // shortest distance to location from current location
    Direction dir109; // best direction to take now to optimally get to location

    MapLocation l124; // location representing relative coordinate (-3, 1)
    int r124; // rubble at location
    int d124; // shortest distance to location from current location
    Direction dir124; // best direction to take now to optimally get to location

    MapLocation l139; // location representing relative coordinate (-3, 2)
    int r139; // rubble at location
    int d139; // shortest distance to location from current location
    Direction dir139; // best direction to take now to optimally get to location

    MapLocation l154; // location representing relative coordinate (-3, 3)
    int r154; // rubble at location
    int d154; // shortest distance to location from current location
    Direction dir154; // best direction to take now to optimally get to location

    MapLocation l169; // location representing relative coordinate (-3, 4)
    int r169; // rubble at location
    int d169; // shortest distance to location from current location
    Direction dir169; // best direction to take now to optimally get to location

    MapLocation l184; // location representing relative coordinate (-3, 5)
    int r184; // rubble at location
    int d184; // shortest distance to location from current location
    Direction dir184; // best direction to take now to optimally get to location

    MapLocation l35; // location representing relative coordinate (-2, -5)
    int r35; // rubble at location
    int d35; // shortest distance to location from current location
    Direction dir35; // best direction to take now to optimally get to location

    MapLocation l50; // location representing relative coordinate (-2, -4)
    int r50; // rubble at location
    int d50; // shortest distance to location from current location
    Direction dir50; // best direction to take now to optimally get to location

    MapLocation l65; // location representing relative coordinate (-2, -3)
    int r65; // rubble at location
    int d65; // shortest distance to location from current location
    Direction dir65; // best direction to take now to optimally get to location

    MapLocation l80; // location representing relative coordinate (-2, -2)
    int r80; // rubble at location
    int d80; // shortest distance to location from current location
    Direction dir80; // best direction to take now to optimally get to location

    MapLocation l95; // location representing relative coordinate (-2, -1)
    int r95; // rubble at location
    int d95; // shortest distance to location from current location
    Direction dir95; // best direction to take now to optimally get to location

    MapLocation l110; // location representing relative coordinate (-2, 0)
    int r110; // rubble at location
    int d110; // shortest distance to location from current location
    Direction dir110; // best direction to take now to optimally get to location

    MapLocation l125; // location representing relative coordinate (-2, 1)
    int r125; // rubble at location
    int d125; // shortest distance to location from current location
    Direction dir125; // best direction to take now to optimally get to location

    MapLocation l140; // location representing relative coordinate (-2, 2)
    int r140; // rubble at location
    int d140; // shortest distance to location from current location
    Direction dir140; // best direction to take now to optimally get to location

    MapLocation l155; // location representing relative coordinate (-2, 3)
    int r155; // rubble at location
    int d155; // shortest distance to location from current location
    Direction dir155; // best direction to take now to optimally get to location

    MapLocation l170; // location representing relative coordinate (-2, 4)
    int r170; // rubble at location
    int d170; // shortest distance to location from current location
    Direction dir170; // best direction to take now to optimally get to location

    MapLocation l185; // location representing relative coordinate (-2, 5)
    int r185; // rubble at location
    int d185; // shortest distance to location from current location
    Direction dir185; // best direction to take now to optimally get to location

    MapLocation l36; // location representing relative coordinate (-1, -5)
    int r36; // rubble at location
    int d36; // shortest distance to location from current location
    Direction dir36; // best direction to take now to optimally get to location

    MapLocation l51; // location representing relative coordinate (-1, -4)
    int r51; // rubble at location
    int d51; // shortest distance to location from current location
    Direction dir51; // best direction to take now to optimally get to location

    MapLocation l66; // location representing relative coordinate (-1, -3)
    int r66; // rubble at location
    int d66; // shortest distance to location from current location
    Direction dir66; // best direction to take now to optimally get to location

    MapLocation l81; // location representing relative coordinate (-1, -2)
    int r81; // rubble at location
    int d81; // shortest distance to location from current location
    Direction dir81; // best direction to take now to optimally get to location

    MapLocation l96; // location representing relative coordinate (-1, -1)
    int r96; // rubble at location
    int d96; // shortest distance to location from current location
    Direction dir96; // best direction to take now to optimally get to location

    MapLocation l111; // location representing relative coordinate (-1, 0)
    int r111; // rubble at location
    int d111; // shortest distance to location from current location
    Direction dir111; // best direction to take now to optimally get to location

    MapLocation l126; // location representing relative coordinate (-1, 1)
    int r126; // rubble at location
    int d126; // shortest distance to location from current location
    Direction dir126; // best direction to take now to optimally get to location

    MapLocation l141; // location representing relative coordinate (-1, 2)
    int r141; // rubble at location
    int d141; // shortest distance to location from current location
    Direction dir141; // best direction to take now to optimally get to location

    MapLocation l156; // location representing relative coordinate (-1, 3)
    int r156; // rubble at location
    int d156; // shortest distance to location from current location
    Direction dir156; // best direction to take now to optimally get to location

    MapLocation l171; // location representing relative coordinate (-1, 4)
    int r171; // rubble at location
    int d171; // shortest distance to location from current location
    Direction dir171; // best direction to take now to optimally get to location

    MapLocation l186; // location representing relative coordinate (-1, 5)
    int r186; // rubble at location
    int d186; // shortest distance to location from current location
    Direction dir186; // best direction to take now to optimally get to location

    MapLocation l37; // location representing relative coordinate (0, -5)
    int r37; // rubble at location
    int d37; // shortest distance to location from current location
    Direction dir37; // best direction to take now to optimally get to location

    MapLocation l52; // location representing relative coordinate (0, -4)
    int r52; // rubble at location
    int d52; // shortest distance to location from current location
    Direction dir52; // best direction to take now to optimally get to location

    MapLocation l67; // location representing relative coordinate (0, -3)
    int r67; // rubble at location
    int d67; // shortest distance to location from current location
    Direction dir67; // best direction to take now to optimally get to location

    MapLocation l82; // location representing relative coordinate (0, -2)
    int r82; // rubble at location
    int d82; // shortest distance to location from current location
    Direction dir82; // best direction to take now to optimally get to location

    MapLocation l97; // location representing relative coordinate (0, -1)
    int r97; // rubble at location
    int d97; // shortest distance to location from current location
    Direction dir97; // best direction to take now to optimally get to location

    MapLocation l112; // location representing relative coordinate (0, 0)
    int r112; // rubble at location
    int d112; // shortest distance to location from current location
    Direction dir112; // best direction to take now to optimally get to location

    MapLocation l127; // location representing relative coordinate (0, 1)
    int r127; // rubble at location
    int d127; // shortest distance to location from current location
    Direction dir127; // best direction to take now to optimally get to location

    MapLocation l142; // location representing relative coordinate (0, 2)
    int r142; // rubble at location
    int d142; // shortest distance to location from current location
    Direction dir142; // best direction to take now to optimally get to location

    MapLocation l157; // location representing relative coordinate (0, 3)
    int r157; // rubble at location
    int d157; // shortest distance to location from current location
    Direction dir157; // best direction to take now to optimally get to location

    MapLocation l172; // location representing relative coordinate (0, 4)
    int r172; // rubble at location
    int d172; // shortest distance to location from current location
    Direction dir172; // best direction to take now to optimally get to location

    MapLocation l187; // location representing relative coordinate (0, 5)
    int r187; // rubble at location
    int d187; // shortest distance to location from current location
    Direction dir187; // best direction to take now to optimally get to location

    MapLocation l38; // location representing relative coordinate (1, -5)
    int r38; // rubble at location
    int d38; // shortest distance to location from current location
    Direction dir38; // best direction to take now to optimally get to location

    MapLocation l53; // location representing relative coordinate (1, -4)
    int r53; // rubble at location
    int d53; // shortest distance to location from current location
    Direction dir53; // best direction to take now to optimally get to location

    MapLocation l68; // location representing relative coordinate (1, -3)
    int r68; // rubble at location
    int d68; // shortest distance to location from current location
    Direction dir68; // best direction to take now to optimally get to location

    MapLocation l83; // location representing relative coordinate (1, -2)
    int r83; // rubble at location
    int d83; // shortest distance to location from current location
    Direction dir83; // best direction to take now to optimally get to location

    MapLocation l98; // location representing relative coordinate (1, -1)
    int r98; // rubble at location
    int d98; // shortest distance to location from current location
    Direction dir98; // best direction to take now to optimally get to location

    MapLocation l113; // location representing relative coordinate (1, 0)
    int r113; // rubble at location
    int d113; // shortest distance to location from current location
    Direction dir113; // best direction to take now to optimally get to location

    MapLocation l128; // location representing relative coordinate (1, 1)
    int r128; // rubble at location
    int d128; // shortest distance to location from current location
    Direction dir128; // best direction to take now to optimally get to location

    MapLocation l143; // location representing relative coordinate (1, 2)
    int r143; // rubble at location
    int d143; // shortest distance to location from current location
    Direction dir143; // best direction to take now to optimally get to location

    MapLocation l158; // location representing relative coordinate (1, 3)
    int r158; // rubble at location
    int d158; // shortest distance to location from current location
    Direction dir158; // best direction to take now to optimally get to location

    MapLocation l173; // location representing relative coordinate (1, 4)
    int r173; // rubble at location
    int d173; // shortest distance to location from current location
    Direction dir173; // best direction to take now to optimally get to location

    MapLocation l188; // location representing relative coordinate (1, 5)
    int r188; // rubble at location
    int d188; // shortest distance to location from current location
    Direction dir188; // best direction to take now to optimally get to location

    MapLocation l39; // location representing relative coordinate (2, -5)
    int r39; // rubble at location
    int d39; // shortest distance to location from current location
    Direction dir39; // best direction to take now to optimally get to location

    MapLocation l54; // location representing relative coordinate (2, -4)
    int r54; // rubble at location
    int d54; // shortest distance to location from current location
    Direction dir54; // best direction to take now to optimally get to location

    MapLocation l69; // location representing relative coordinate (2, -3)
    int r69; // rubble at location
    int d69; // shortest distance to location from current location
    Direction dir69; // best direction to take now to optimally get to location

    MapLocation l84; // location representing relative coordinate (2, -2)
    int r84; // rubble at location
    int d84; // shortest distance to location from current location
    Direction dir84; // best direction to take now to optimally get to location

    MapLocation l99; // location representing relative coordinate (2, -1)
    int r99; // rubble at location
    int d99; // shortest distance to location from current location
    Direction dir99; // best direction to take now to optimally get to location

    MapLocation l114; // location representing relative coordinate (2, 0)
    int r114; // rubble at location
    int d114; // shortest distance to location from current location
    Direction dir114; // best direction to take now to optimally get to location

    MapLocation l129; // location representing relative coordinate (2, 1)
    int r129; // rubble at location
    int d129; // shortest distance to location from current location
    Direction dir129; // best direction to take now to optimally get to location

    MapLocation l144; // location representing relative coordinate (2, 2)
    int r144; // rubble at location
    int d144; // shortest distance to location from current location
    Direction dir144; // best direction to take now to optimally get to location

    MapLocation l159; // location representing relative coordinate (2, 3)
    int r159; // rubble at location
    int d159; // shortest distance to location from current location
    Direction dir159; // best direction to take now to optimally get to location

    MapLocation l174; // location representing relative coordinate (2, 4)
    int r174; // rubble at location
    int d174; // shortest distance to location from current location
    Direction dir174; // best direction to take now to optimally get to location

    MapLocation l189; // location representing relative coordinate (2, 5)
    int r189; // rubble at location
    int d189; // shortest distance to location from current location
    Direction dir189; // best direction to take now to optimally get to location

    MapLocation l40; // location representing relative coordinate (3, -5)
    int r40; // rubble at location
    int d40; // shortest distance to location from current location
    Direction dir40; // best direction to take now to optimally get to location

    MapLocation l55; // location representing relative coordinate (3, -4)
    int r55; // rubble at location
    int d55; // shortest distance to location from current location
    Direction dir55; // best direction to take now to optimally get to location

    MapLocation l70; // location representing relative coordinate (3, -3)
    int r70; // rubble at location
    int d70; // shortest distance to location from current location
    Direction dir70; // best direction to take now to optimally get to location

    MapLocation l85; // location representing relative coordinate (3, -2)
    int r85; // rubble at location
    int d85; // shortest distance to location from current location
    Direction dir85; // best direction to take now to optimally get to location

    MapLocation l100; // location representing relative coordinate (3, -1)
    int r100; // rubble at location
    int d100; // shortest distance to location from current location
    Direction dir100; // best direction to take now to optimally get to location

    MapLocation l115; // location representing relative coordinate (3, 0)
    int r115; // rubble at location
    int d115; // shortest distance to location from current location
    Direction dir115; // best direction to take now to optimally get to location

    MapLocation l130; // location representing relative coordinate (3, 1)
    int r130; // rubble at location
    int d130; // shortest distance to location from current location
    Direction dir130; // best direction to take now to optimally get to location

    MapLocation l145; // location representing relative coordinate (3, 2)
    int r145; // rubble at location
    int d145; // shortest distance to location from current location
    Direction dir145; // best direction to take now to optimally get to location

    MapLocation l160; // location representing relative coordinate (3, 3)
    int r160; // rubble at location
    int d160; // shortest distance to location from current location
    Direction dir160; // best direction to take now to optimally get to location

    MapLocation l175; // location representing relative coordinate (3, 4)
    int r175; // rubble at location
    int d175; // shortest distance to location from current location
    Direction dir175; // best direction to take now to optimally get to location

    MapLocation l190; // location representing relative coordinate (3, 5)
    int r190; // rubble at location
    int d190; // shortest distance to location from current location
    Direction dir190; // best direction to take now to optimally get to location

    MapLocation l56; // location representing relative coordinate (4, -4)
    int r56; // rubble at location
    int d56; // shortest distance to location from current location
    Direction dir56; // best direction to take now to optimally get to location

    MapLocation l71; // location representing relative coordinate (4, -3)
    int r71; // rubble at location
    int d71; // shortest distance to location from current location
    Direction dir71; // best direction to take now to optimally get to location

    MapLocation l86; // location representing relative coordinate (4, -2)
    int r86; // rubble at location
    int d86; // shortest distance to location from current location
    Direction dir86; // best direction to take now to optimally get to location

    MapLocation l101; // location representing relative coordinate (4, -1)
    int r101; // rubble at location
    int d101; // shortest distance to location from current location
    Direction dir101; // best direction to take now to optimally get to location

    MapLocation l116; // location representing relative coordinate (4, 0)
    int r116; // rubble at location
    int d116; // shortest distance to location from current location
    Direction dir116; // best direction to take now to optimally get to location

    MapLocation l131; // location representing relative coordinate (4, 1)
    int r131; // rubble at location
    int d131; // shortest distance to location from current location
    Direction dir131; // best direction to take now to optimally get to location

    MapLocation l146; // location representing relative coordinate (4, 2)
    int r146; // rubble at location
    int d146; // shortest distance to location from current location
    Direction dir146; // best direction to take now to optimally get to location

    MapLocation l161; // location representing relative coordinate (4, 3)
    int r161; // rubble at location
    int d161; // shortest distance to location from current location
    Direction dir161; // best direction to take now to optimally get to location

    MapLocation l176; // location representing relative coordinate (4, 4)
    int r176; // rubble at location
    int d176; // shortest distance to location from current location
    Direction dir176; // best direction to take now to optimally get to location

    MapLocation l72; // location representing relative coordinate (5, -3)
    int r72; // rubble at location
    int d72; // shortest distance to location from current location
    Direction dir72; // best direction to take now to optimally get to location

    MapLocation l87; // location representing relative coordinate (5, -2)
    int r87; // rubble at location
    int d87; // shortest distance to location from current location
    Direction dir87; // best direction to take now to optimally get to location

    MapLocation l102; // location representing relative coordinate (5, -1)
    int r102; // rubble at location
    int d102; // shortest distance to location from current location
    Direction dir102; // best direction to take now to optimally get to location

    MapLocation l117; // location representing relative coordinate (5, 0)
    int r117; // rubble at location
    int d117; // shortest distance to location from current location
    Direction dir117; // best direction to take now to optimally get to location

    MapLocation l132; // location representing relative coordinate (5, 1)
    int r132; // rubble at location
    int d132; // shortest distance to location from current location
    Direction dir132; // best direction to take now to optimally get to location

    MapLocation l147; // location representing relative coordinate (5, 2)
    int r147; // rubble at location
    int d147; // shortest distance to location from current location
    Direction dir147; // best direction to take now to optimally get to location

    MapLocation l162; // location representing relative coordinate (5, 3)
    int r162; // rubble at location
    int d162; // shortest distance to location from current location
    Direction dir162; // best direction to take now to optimally get to location


    public MinerPathing(RobotController rc) {
        this.rc = rc;
    }

    public Direction bestDir(MapLocation target) throws GameActionException {

        l112 = rc.getLocation();
        r112 = rc.senseRubble(l112);
        d112 = 1000000;
        dir112 = Direction.CENTER;

        l111 = l112.add(Direction.WEST); // (-1, 0) from (0, 0)
        r111 = rc.senseRubble(l111);
        d111 = 1000000;
        dir111 = null;

        l97 = l112.add(Direction.SOUTH); // (0, -1) from (0, 0)
        r97 = rc.senseRubble(l97);
        d97 = 1000000;
        dir97 = null;

        l127 = l112.add(Direction.NORTH); // (0, 1) from (0, 0)
        r127 = rc.senseRubble(l127);
        d127 = 1000000;
        dir127 = null;

        l113 = l112.add(Direction.EAST); // (1, 0) from (0, 0)
        r113 = rc.senseRubble(l113);
        d113 = 1000000;
        dir113 = null;

        l96 = l112.add(Direction.SOUTHWEST); // (-1, -1) from (0, 0)
        r96 = rc.senseRubble(l96);
        d96 = 1000000;
        dir96 = null;

        l126 = l112.add(Direction.NORTHWEST); // (-1, 1) from (0, 0)
        r126 = rc.senseRubble(l126);
        d126 = 1000000;
        dir126 = null;

        l98 = l112.add(Direction.SOUTHEAST); // (1, -1) from (0, 0)
        r98 = rc.senseRubble(l98);
        d98 = 1000000;
        dir98 = null;

        l128 = l112.add(Direction.NORTHEAST); // (1, 1) from (0, 0)
        r128 = rc.senseRubble(l128);
        d128 = 1000000;
        dir128 = null;

        l110 = l111.add(Direction.WEST); // (-2, 0) from (-1, 0)
        r110 = rc.senseRubble(l110);
        d110 = 1000000;
        dir110 = null;

        l82 = l97.add(Direction.SOUTH); // (0, -2) from (0, -1)
        r82 = rc.senseRubble(l82);
        d82 = 1000000;
        dir82 = null;

        l142 = l127.add(Direction.NORTH); // (0, 2) from (0, 1)
        r142 = rc.senseRubble(l142);
        d142 = 1000000;
        dir142 = null;

        l114 = l113.add(Direction.EAST); // (2, 0) from (1, 0)
        r114 = rc.senseRubble(l114);
        d114 = 1000000;
        dir114 = null;

        l95 = l111.add(Direction.SOUTHWEST); // (-2, -1) from (-1, 0)
        r95 = rc.senseRubble(l95);
        d95 = 1000000;
        dir95 = null;

        l125 = l111.add(Direction.NORTHWEST); // (-2, 1) from (-1, 0)
        r125 = rc.senseRubble(l125);
        d125 = 1000000;
        dir125 = null;

        l81 = l97.add(Direction.SOUTHWEST); // (-1, -2) from (0, -1)
        r81 = rc.senseRubble(l81);
        d81 = 1000000;
        dir81 = null;

        l141 = l127.add(Direction.NORTHWEST); // (-1, 2) from (0, 1)
        r141 = rc.senseRubble(l141);
        d141 = 1000000;
        dir141 = null;

        l83 = l97.add(Direction.SOUTHEAST); // (1, -2) from (0, -1)
        r83 = rc.senseRubble(l83);
        d83 = 1000000;
        dir83 = null;

        l143 = l127.add(Direction.NORTHEAST); // (1, 2) from (0, 1)
        r143 = rc.senseRubble(l143);
        d143 = 1000000;
        dir143 = null;

        l99 = l113.add(Direction.SOUTHEAST); // (2, -1) from (1, 0)
        r99 = rc.senseRubble(l99);
        d99 = 1000000;
        dir99 = null;

        l129 = l113.add(Direction.NORTHEAST); // (2, 1) from (1, 0)
        r129 = rc.senseRubble(l129);
        d129 = 1000000;
        dir129 = null;

        l80 = l96.add(Direction.SOUTHWEST); // (-2, -2) from (-1, -1)
        r80 = rc.senseRubble(l80);
        d80 = 1000000;
        dir80 = null;

        l140 = l126.add(Direction.NORTHWEST); // (-2, 2) from (-1, 1)
        r140 = rc.senseRubble(l140);
        d140 = 1000000;
        dir140 = null;

        l84 = l98.add(Direction.SOUTHEAST); // (2, -2) from (1, -1)
        r84 = rc.senseRubble(l84);
        d84 = 1000000;
        dir84 = null;

        l144 = l128.add(Direction.NORTHEAST); // (2, 2) from (1, 1)
        r144 = rc.senseRubble(l144);
        d144 = 1000000;
        dir144 = null;

        l109 = l110.add(Direction.WEST); // (-3, 0) from (-2, 0)
        r109 = rc.senseRubble(l109);
        d109 = 1000000;
        dir109 = null;

        l67 = l82.add(Direction.SOUTH); // (0, -3) from (0, -2)
        r67 = rc.senseRubble(l67);
        d67 = 1000000;
        dir67 = null;

        l157 = l142.add(Direction.NORTH); // (0, 3) from (0, 2)
        r157 = rc.senseRubble(l157);
        d157 = 1000000;
        dir157 = null;

        l115 = l114.add(Direction.EAST); // (3, 0) from (2, 0)
        r115 = rc.senseRubble(l115);
        d115 = 1000000;
        dir115 = null;

        l94 = l110.add(Direction.SOUTHWEST); // (-3, -1) from (-2, 0)
        r94 = rc.senseRubble(l94);
        d94 = 1000000;
        dir94 = null;

        l124 = l110.add(Direction.NORTHWEST); // (-3, 1) from (-2, 0)
        r124 = rc.senseRubble(l124);
        d124 = 1000000;
        dir124 = null;

        l66 = l82.add(Direction.SOUTHWEST); // (-1, -3) from (0, -2)
        r66 = rc.senseRubble(l66);
        d66 = 1000000;
        dir66 = null;

        l156 = l142.add(Direction.NORTHWEST); // (-1, 3) from (0, 2)
        r156 = rc.senseRubble(l156);
        d156 = 1000000;
        dir156 = null;

        l68 = l82.add(Direction.SOUTHEAST); // (1, -3) from (0, -2)
        r68 = rc.senseRubble(l68);
        d68 = 1000000;
        dir68 = null;

        l158 = l142.add(Direction.NORTHEAST); // (1, 3) from (0, 2)
        r158 = rc.senseRubble(l158);
        d158 = 1000000;
        dir158 = null;

        l100 = l114.add(Direction.SOUTHEAST); // (3, -1) from (2, 0)
        r100 = rc.senseRubble(l100);
        d100 = 1000000;
        dir100 = null;

        l130 = l114.add(Direction.NORTHEAST); // (3, 1) from (2, 0)
        r130 = rc.senseRubble(l130);
        d130 = 1000000;
        dir130 = null;

        l79 = l95.add(Direction.SOUTHWEST); // (-3, -2) from (-2, -1)
        r79 = rc.senseRubble(l79);
        d79 = 1000000;
        dir79 = null;

        l139 = l125.add(Direction.NORTHWEST); // (-3, 2) from (-2, 1)
        r139 = rc.senseRubble(l139);
        d139 = 1000000;
        dir139 = null;

        l65 = l81.add(Direction.SOUTHWEST); // (-2, -3) from (-1, -2)
        r65 = rc.senseRubble(l65);
        d65 = 1000000;
        dir65 = null;

        l155 = l141.add(Direction.NORTHWEST); // (-2, 3) from (-1, 2)
        r155 = rc.senseRubble(l155);
        d155 = 1000000;
        dir155 = null;

        l69 = l83.add(Direction.SOUTHEAST); // (2, -3) from (1, -2)
        r69 = rc.senseRubble(l69);
        d69 = 1000000;
        dir69 = null;

        l159 = l143.add(Direction.NORTHEAST); // (2, 3) from (1, 2)
        r159 = rc.senseRubble(l159);
        d159 = 1000000;
        dir159 = null;

        l85 = l99.add(Direction.SOUTHEAST); // (3, -2) from (2, -1)
        r85 = rc.senseRubble(l85);
        d85 = 1000000;
        dir85 = null;

        l145 = l129.add(Direction.NORTHEAST); // (3, 2) from (2, 1)
        r145 = rc.senseRubble(l145);
        d145 = 1000000;
        dir145 = null;

        l108 = l109.add(Direction.WEST); // (-4, 0) from (-3, 0)
        r108 = rc.senseRubble(l108);
        d108 = 1000000;
        dir108 = null;

        l52 = l67.add(Direction.SOUTH); // (0, -4) from (0, -3)
        r52 = rc.senseRubble(l52);
        d52 = 1000000;
        dir52 = null;

        l172 = l157.add(Direction.NORTH); // (0, 4) from (0, 3)
        r172 = rc.senseRubble(l172);
        d172 = 1000000;
        dir172 = null;

        l116 = l115.add(Direction.EAST); // (4, 0) from (3, 0)
        r116 = rc.senseRubble(l116);
        d116 = 1000000;
        dir116 = null;

        l93 = l109.add(Direction.SOUTHWEST); // (-4, -1) from (-3, 0)
        r93 = rc.senseRubble(l93);
        d93 = 1000000;
        dir93 = null;

        l123 = l109.add(Direction.NORTHWEST); // (-4, 1) from (-3, 0)
        r123 = rc.senseRubble(l123);
        d123 = 1000000;
        dir123 = null;

        l51 = l67.add(Direction.SOUTHWEST); // (-1, -4) from (0, -3)
        r51 = rc.senseRubble(l51);
        d51 = 1000000;
        dir51 = null;

        l171 = l157.add(Direction.NORTHWEST); // (-1, 4) from (0, 3)
        r171 = rc.senseRubble(l171);
        d171 = 1000000;
        dir171 = null;

        l53 = l67.add(Direction.SOUTHEAST); // (1, -4) from (0, -3)
        r53 = rc.senseRubble(l53);
        d53 = 1000000;
        dir53 = null;

        l173 = l157.add(Direction.NORTHEAST); // (1, 4) from (0, 3)
        r173 = rc.senseRubble(l173);
        d173 = 1000000;
        dir173 = null;

        l101 = l115.add(Direction.SOUTHEAST); // (4, -1) from (3, 0)
        r101 = rc.senseRubble(l101);
        d101 = 1000000;
        dir101 = null;

        l131 = l115.add(Direction.NORTHEAST); // (4, 1) from (3, 0)
        r131 = rc.senseRubble(l131);
        d131 = 1000000;
        dir131 = null;

        l64 = l80.add(Direction.SOUTHWEST); // (-3, -3) from (-2, -2)
        r64 = rc.senseRubble(l64);
        d64 = 1000000;
        dir64 = null;

        l154 = l140.add(Direction.NORTHWEST); // (-3, 3) from (-2, 2)
        r154 = rc.senseRubble(l154);
        d154 = 1000000;
        dir154 = null;

        l70 = l84.add(Direction.SOUTHEAST); // (3, -3) from (2, -2)
        r70 = rc.senseRubble(l70);
        d70 = 1000000;
        dir70 = null;

        l160 = l144.add(Direction.NORTHEAST); // (3, 3) from (2, 2)
        r160 = rc.senseRubble(l160);
        d160 = 1000000;
        dir160 = null;

        l78 = l94.add(Direction.SOUTHWEST); // (-4, -2) from (-3, -1)
        r78 = rc.senseRubble(l78);
        d78 = 1000000;
        dir78 = null;

        l138 = l124.add(Direction.NORTHWEST); // (-4, 2) from (-3, 1)
        r138 = rc.senseRubble(l138);
        d138 = 1000000;
        dir138 = null;

        l50 = l66.add(Direction.SOUTHWEST); // (-2, -4) from (-1, -3)
        r50 = rc.senseRubble(l50);
        d50 = 1000000;
        dir50 = null;

        l170 = l156.add(Direction.NORTHWEST); // (-2, 4) from (-1, 3)
        r170 = rc.senseRubble(l170);
        d170 = 1000000;
        dir170 = null;

        l54 = l68.add(Direction.SOUTHEAST); // (2, -4) from (1, -3)
        r54 = rc.senseRubble(l54);
        d54 = 1000000;
        dir54 = null;

        l174 = l158.add(Direction.NORTHEAST); // (2, 4) from (1, 3)
        r174 = rc.senseRubble(l174);
        d174 = 1000000;
        dir174 = null;

        l86 = l100.add(Direction.SOUTHEAST); // (4, -2) from (3, -1)
        r86 = rc.senseRubble(l86);
        d86 = 1000000;
        dir86 = null;

        l146 = l130.add(Direction.NORTHEAST); // (4, 2) from (3, 1)
        r146 = rc.senseRubble(l146);
        d146 = 1000000;
        dir146 = null;

        l107 = l108.add(Direction.WEST); // (-5, 0) from (-4, 0)
        r107 = rc.senseRubble(l107);
        d107 = 1000000;
        dir107 = null;

        l63 = l79.add(Direction.SOUTHWEST); // (-4, -3) from (-3, -2)
        r63 = rc.senseRubble(l63);
        d63 = 1000000;
        dir63 = null;

        l153 = l139.add(Direction.NORTHWEST); // (-4, 3) from (-3, 2)
        r153 = rc.senseRubble(l153);
        d153 = 1000000;
        dir153 = null;

        l49 = l65.add(Direction.SOUTHWEST); // (-3, -4) from (-2, -3)
        r49 = rc.senseRubble(l49);
        d49 = 1000000;
        dir49 = null;

        l169 = l155.add(Direction.NORTHWEST); // (-3, 4) from (-2, 3)
        r169 = rc.senseRubble(l169);
        d169 = 1000000;
        dir169 = null;

        l37 = l52.add(Direction.SOUTH); // (0, -5) from (0, -4)
        r37 = rc.senseRubble(l37);
        d37 = 1000000;
        dir37 = null;

        l187 = l172.add(Direction.NORTH); // (0, 5) from (0, 4)
        r187 = rc.senseRubble(l187);
        d187 = 1000000;
        dir187 = null;

        l55 = l69.add(Direction.SOUTHEAST); // (3, -4) from (2, -3)
        r55 = rc.senseRubble(l55);
        d55 = 1000000;
        dir55 = null;

        l175 = l159.add(Direction.NORTHEAST); // (3, 4) from (2, 3)
        r175 = rc.senseRubble(l175);
        d175 = 1000000;
        dir175 = null;

        l71 = l85.add(Direction.SOUTHEAST); // (4, -3) from (3, -2)
        r71 = rc.senseRubble(l71);
        d71 = 1000000;
        dir71 = null;

        l161 = l145.add(Direction.NORTHEAST); // (4, 3) from (3, 2)
        r161 = rc.senseRubble(l161);
        d161 = 1000000;
        dir161 = null;

        l117 = l116.add(Direction.EAST); // (5, 0) from (4, 0)
        r117 = rc.senseRubble(l117);
        d117 = 1000000;
        dir117 = null;

        l92 = l108.add(Direction.SOUTHWEST); // (-5, -1) from (-4, 0)
        r92 = rc.senseRubble(l92);
        d92 = 1000000;
        dir92 = null;

        l122 = l108.add(Direction.NORTHWEST); // (-5, 1) from (-4, 0)
        r122 = rc.senseRubble(l122);
        d122 = 1000000;
        dir122 = null;

        l36 = l52.add(Direction.SOUTHWEST); // (-1, -5) from (0, -4)
        r36 = rc.senseRubble(l36);
        d36 = 1000000;
        dir36 = null;

        l186 = l172.add(Direction.NORTHWEST); // (-1, 5) from (0, 4)
        r186 = rc.senseRubble(l186);
        d186 = 1000000;
        dir186 = null;

        l38 = l52.add(Direction.SOUTHEAST); // (1, -5) from (0, -4)
        r38 = rc.senseRubble(l38);
        d38 = 1000000;
        dir38 = null;

        l188 = l172.add(Direction.NORTHEAST); // (1, 5) from (0, 4)
        r188 = rc.senseRubble(l188);
        d188 = 1000000;
        dir188 = null;

        l102 = l116.add(Direction.SOUTHEAST); // (5, -1) from (4, 0)
        r102 = rc.senseRubble(l102);
        d102 = 1000000;
        dir102 = null;

        l132 = l116.add(Direction.NORTHEAST); // (5, 1) from (4, 0)
        r132 = rc.senseRubble(l132);
        d132 = 1000000;
        dir132 = null;

        l77 = l93.add(Direction.SOUTHWEST); // (-5, -2) from (-4, -1)
        r77 = rc.senseRubble(l77);
        d77 = 1000000;
        dir77 = null;

        l137 = l123.add(Direction.NORTHWEST); // (-5, 2) from (-4, 1)
        r137 = rc.senseRubble(l137);
        d137 = 1000000;
        dir137 = null;

        l35 = l51.add(Direction.SOUTHWEST); // (-2, -5) from (-1, -4)
        r35 = rc.senseRubble(l35);
        d35 = 1000000;
        dir35 = null;

        l185 = l171.add(Direction.NORTHWEST); // (-2, 5) from (-1, 4)
        r185 = rc.senseRubble(l185);
        d185 = 1000000;
        dir185 = null;

        l39 = l53.add(Direction.SOUTHEAST); // (2, -5) from (1, -4)
        r39 = rc.senseRubble(l39);
        d39 = 1000000;
        dir39 = null;

        l189 = l173.add(Direction.NORTHEAST); // (2, 5) from (1, 4)
        r189 = rc.senseRubble(l189);
        d189 = 1000000;
        dir189 = null;

        l87 = l101.add(Direction.SOUTHEAST); // (5, -2) from (4, -1)
        r87 = rc.senseRubble(l87);
        d87 = 1000000;
        dir87 = null;

        l147 = l131.add(Direction.NORTHEAST); // (5, 2) from (4, 1)
        r147 = rc.senseRubble(l147);
        d147 = 1000000;
        dir147 = null;

        l48 = l64.add(Direction.SOUTHWEST); // (-4, -4) from (-3, -3)
        r48 = rc.senseRubble(l48);
        d48 = 1000000;
        dir48 = null;

        l168 = l154.add(Direction.NORTHWEST); // (-4, 4) from (-3, 3)
        r168 = rc.senseRubble(l168);
        d168 = 1000000;
        dir168 = null;

        l56 = l70.add(Direction.SOUTHEAST); // (4, -4) from (3, -3)
        r56 = rc.senseRubble(l56);
        d56 = 1000000;
        dir56 = null;

        l176 = l160.add(Direction.NORTHEAST); // (4, 4) from (3, 3)
        r176 = rc.senseRubble(l176);
        d176 = 1000000;
        dir176 = null;

        l62 = l78.add(Direction.SOUTHWEST); // (-5, -3) from (-4, -2)
        r62 = rc.senseRubble(l62);
        d62 = 1000000;
        dir62 = null;

        l152 = l138.add(Direction.NORTHWEST); // (-5, 3) from (-4, 2)
        r152 = rc.senseRubble(l152);
        d152 = 1000000;
        dir152 = null;

        l34 = l50.add(Direction.SOUTHWEST); // (-3, -5) from (-2, -4)
        r34 = rc.senseRubble(l34);
        d34 = 1000000;
        dir34 = null;

        l184 = l170.add(Direction.NORTHWEST); // (-3, 5) from (-2, 4)
        r184 = rc.senseRubble(l184);
        d184 = 1000000;
        dir184 = null;

        l40 = l54.add(Direction.SOUTHEAST); // (3, -5) from (2, -4)
        r40 = rc.senseRubble(l40);
        d40 = 1000000;
        dir40 = null;

        l190 = l174.add(Direction.NORTHEAST); // (3, 5) from (2, 4)
        r190 = rc.senseRubble(l190);
        d190 = 1000000;
        dir190 = null;

        l72 = l86.add(Direction.SOUTHEAST); // (5, -3) from (4, -2)
        r72 = rc.senseRubble(l72);
        d72 = 1000000;
        dir72 = null;

        l162 = l146.add(Direction.NORTHEAST); // (5, 3) from (4, 2)
        r162 = rc.senseRubble(l162);
        d162 = 1000000;
        dir162 = null;



        if (rc.onTheMap(l111)) { // check (-1, 0)
            if (!rc.isLocationOccupied(l111)) { 
                if (d111 > d112 + r111) { // from (0, 0)
                    d111 = d112 + r111;
                    dir111 = Direction.WEST;
                }
            }
        }

        if (rc.onTheMap(l97)) { // check (0, -1)
            if (!rc.isLocationOccupied(l97)) { 
                if (d97 > d111 + r97) { // from (-1, 0)
                    d97 = d111 + r97;
                    dir97 = dir111;
                }
                if (d97 > d112 + r97) { // from (0, 0)
                    d97 = d112 + r97;
                    dir97 = Direction.SOUTH;
                }
            }
        }

        if (rc.onTheMap(l127)) { // check (0, 1)
            if (!rc.isLocationOccupied(l127)) { 
                if (d127 > d111 + r127) { // from (-1, 0)
                    d127 = d111 + r127;
                    dir127 = dir111;
                }
                if (d127 > d112 + r127) { // from (0, 0)
                    d127 = d112 + r127;
                    dir127 = Direction.NORTH;
                }
            }
        }

        if (rc.onTheMap(l113)) { // check (1, 0)
            if (!rc.isLocationOccupied(l113)) { 
                if (d113 > d97 + r113) { // from (0, -1)
                    d113 = d97 + r113;
                    dir113 = dir97;
                }
                if (d113 > d112 + r113) { // from (0, 0)
                    d113 = d112 + r113;
                    dir113 = Direction.EAST;
                }
                if (d113 > d127 + r113) { // from (0, 1)
                    d113 = d127 + r113;
                    dir113 = dir127;
                }
            }
        }

        if (rc.onTheMap(l96)) { // check (-1, -1)
            if (!rc.isLocationOccupied(l96)) { 
                if (d96 > d111 + r96) { // from (-1, 0)
                    d96 = d111 + r96;
                    dir96 = dir111;
                }
                if (d96 > d97 + r96) { // from (0, -1)
                    d96 = d97 + r96;
                    dir96 = dir97;
                }
                if (d96 > d112 + r96) { // from (0, 0)
                    d96 = d112 + r96;
                    dir96 = Direction.SOUTHWEST;
                }
            }
        }

        if (rc.onTheMap(l126)) { // check (-1, 1)
            if (!rc.isLocationOccupied(l126)) { 
                if (d126 > d111 + r126) { // from (-1, 0)
                    d126 = d111 + r126;
                    dir126 = dir111;
                }
                if (d126 > d112 + r126) { // from (0, 0)
                    d126 = d112 + r126;
                    dir126 = Direction.NORTHWEST;
                }
                if (d126 > d127 + r126) { // from (0, 1)
                    d126 = d127 + r126;
                    dir126 = dir127;
                }
            }
        }

        if (rc.onTheMap(l98)) { // check (1, -1)
            if (!rc.isLocationOccupied(l98)) { 
                if (d98 > d97 + r98) { // from (0, -1)
                    d98 = d97 + r98;
                    dir98 = dir97;
                }
                if (d98 > d112 + r98) { // from (0, 0)
                    d98 = d112 + r98;
                    dir98 = Direction.SOUTHEAST;
                }
                if (d98 > d113 + r98) { // from (1, 0)
                    d98 = d113 + r98;
                    dir98 = dir113;
                }
            }
        }

        if (rc.onTheMap(l128)) { // check (1, 1)
            if (!rc.isLocationOccupied(l128)) { 
                if (d128 > d112 + r128) { // from (0, 0)
                    d128 = d112 + r128;
                    dir128 = Direction.NORTHEAST;
                }
                if (d128 > d127 + r128) { // from (0, 1)
                    d128 = d127 + r128;
                    dir128 = dir127;
                }
                if (d128 > d113 + r128) { // from (1, 0)
                    d128 = d113 + r128;
                    dir128 = dir113;
                }
            }
        }

        if (rc.onTheMap(l110)) { // check (-2, 0)
            if (d110 > d96 + r110) { // from (-1, -1)
                d110 = d96 + r110;
                dir110 = dir96;
            }
            if (d110 > d111 + r110) { // from (-1, 0)
                d110 = d111 + r110;
                dir110 = dir111;
            }
            if (d110 > d126 + r110) { // from (-1, 1)
                d110 = d126 + r110;
                dir110 = dir126;
            }
        }

        if (rc.onTheMap(l82)) { // check (0, -2)
            if (d82 > d96 + r82) { // from (-1, -1)
                d82 = d96 + r82;
                dir82 = dir96;
            }
            if (d82 > d97 + r82) { // from (0, -1)
                d82 = d97 + r82;
                dir82 = dir97;
            }
            if (d82 > d98 + r82) { // from (1, -1)
                d82 = d98 + r82;
                dir82 = dir98;
            }
        }

        if (rc.onTheMap(l142)) { // check (0, 2)
            if (d142 > d126 + r142) { // from (-1, 1)
                d142 = d126 + r142;
                dir142 = dir126;
            }
            if (d142 > d127 + r142) { // from (0, 1)
                d142 = d127 + r142;
                dir142 = dir127;
            }
            if (d142 > d128 + r142) { // from (1, 1)
                d142 = d128 + r142;
                dir142 = dir128;
            }
        }

        if (rc.onTheMap(l114)) { // check (2, 0)
            if (d114 > d98 + r114) { // from (1, -1)
                d114 = d98 + r114;
                dir114 = dir98;
            }
            if (d114 > d113 + r114) { // from (1, 0)
                d114 = d113 + r114;
                dir114 = dir113;
            }
            if (d114 > d128 + r114) { // from (1, 1)
                d114 = d128 + r114;
                dir114 = dir128;
            }
        }

        if (rc.onTheMap(l95)) { // check (-2, -1)
            if (d95 > d110 + r95) { // from (-2, 0)
                d95 = d110 + r95;
                dir95 = dir110;
            }
            if (d95 > d96 + r95) { // from (-1, -1)
                d95 = d96 + r95;
                dir95 = dir96;
            }
            if (d95 > d111 + r95) { // from (-1, 0)
                d95 = d111 + r95;
                dir95 = dir111;
            }
        }

        if (rc.onTheMap(l125)) { // check (-2, 1)
            if (d125 > d110 + r125) { // from (-2, 0)
                d125 = d110 + r125;
                dir125 = dir110;
            }
            if (d125 > d111 + r125) { // from (-1, 0)
                d125 = d111 + r125;
                dir125 = dir111;
            }
            if (d125 > d126 + r125) { // from (-1, 1)
                d125 = d126 + r125;
                dir125 = dir126;
            }
        }

        if (rc.onTheMap(l81)) { // check (-1, -2)
            if (d81 > d95 + r81) { // from (-2, -1)
                d81 = d95 + r81;
                dir81 = dir95;
            }
            if (d81 > d96 + r81) { // from (-1, -1)
                d81 = d96 + r81;
                dir81 = dir96;
            }
            if (d81 > d82 + r81) { // from (0, -2)
                d81 = d82 + r81;
                dir81 = dir82;
            }
            if (d81 > d97 + r81) { // from (0, -1)
                d81 = d97 + r81;
                dir81 = dir97;
            }
        }

        if (rc.onTheMap(l141)) { // check (-1, 2)
            if (d141 > d125 + r141) { // from (-2, 1)
                d141 = d125 + r141;
                dir141 = dir125;
            }
            if (d141 > d126 + r141) { // from (-1, 1)
                d141 = d126 + r141;
                dir141 = dir126;
            }
            if (d141 > d127 + r141) { // from (0, 1)
                d141 = d127 + r141;
                dir141 = dir127;
            }
            if (d141 > d142 + r141) { // from (0, 2)
                d141 = d142 + r141;
                dir141 = dir142;
            }
        }

        if (rc.onTheMap(l83)) { // check (1, -2)
            if (d83 > d82 + r83) { // from (0, -2)
                d83 = d82 + r83;
                dir83 = dir82;
            }
            if (d83 > d97 + r83) { // from (0, -1)
                d83 = d97 + r83;
                dir83 = dir97;
            }
            if (d83 > d98 + r83) { // from (1, -1)
                d83 = d98 + r83;
                dir83 = dir98;
            }
        }

        if (rc.onTheMap(l143)) { // check (1, 2)
            if (d143 > d127 + r143) { // from (0, 1)
                d143 = d127 + r143;
                dir143 = dir127;
            }
            if (d143 > d142 + r143) { // from (0, 2)
                d143 = d142 + r143;
                dir143 = dir142;
            }
            if (d143 > d128 + r143) { // from (1, 1)
                d143 = d128 + r143;
                dir143 = dir128;
            }
        }

        if (rc.onTheMap(l99)) { // check (2, -1)
            if (d99 > d83 + r99) { // from (1, -2)
                d99 = d83 + r99;
                dir99 = dir83;
            }
            if (d99 > d98 + r99) { // from (1, -1)
                d99 = d98 + r99;
                dir99 = dir98;
            }
            if (d99 > d113 + r99) { // from (1, 0)
                d99 = d113 + r99;
                dir99 = dir113;
            }
            if (d99 > d114 + r99) { // from (2, 0)
                d99 = d114 + r99;
                dir99 = dir114;
            }
        }

        if (rc.onTheMap(l129)) { // check (2, 1)
            if (d129 > d113 + r129) { // from (1, 0)
                d129 = d113 + r129;
                dir129 = dir113;
            }
            if (d129 > d128 + r129) { // from (1, 1)
                d129 = d128 + r129;
                dir129 = dir128;
            }
            if (d129 > d143 + r129) { // from (1, 2)
                d129 = d143 + r129;
                dir129 = dir143;
            }
            if (d129 > d114 + r129) { // from (2, 0)
                d129 = d114 + r129;
                dir129 = dir114;
            }
        }

        if (rc.onTheMap(l80)) { // check (-2, -2)
            if (d80 > d95 + r80) { // from (-2, -1)
                d80 = d95 + r80;
                dir80 = dir95;
            }
            if (d80 > d81 + r80) { // from (-1, -2)
                d80 = d81 + r80;
                dir80 = dir81;
            }
            if (d80 > d96 + r80) { // from (-1, -1)
                d80 = d96 + r80;
                dir80 = dir96;
            }
        }

        if (rc.onTheMap(l140)) { // check (-2, 2)
            if (d140 > d125 + r140) { // from (-2, 1)
                d140 = d125 + r140;
                dir140 = dir125;
            }
            if (d140 > d126 + r140) { // from (-1, 1)
                d140 = d126 + r140;
                dir140 = dir126;
            }
            if (d140 > d141 + r140) { // from (-1, 2)
                d140 = d141 + r140;
                dir140 = dir141;
            }
        }

        if (rc.onTheMap(l84)) { // check (2, -2)
            if (d84 > d83 + r84) { // from (1, -2)
                d84 = d83 + r84;
                dir84 = dir83;
            }
            if (d84 > d98 + r84) { // from (1, -1)
                d84 = d98 + r84;
                dir84 = dir98;
            }
            if (d84 > d99 + r84) { // from (2, -1)
                d84 = d99 + r84;
                dir84 = dir99;
            }
        }

        if (rc.onTheMap(l144)) { // check (2, 2)
            if (d144 > d128 + r144) { // from (1, 1)
                d144 = d128 + r144;
                dir144 = dir128;
            }
            if (d144 > d143 + r144) { // from (1, 2)
                d144 = d143 + r144;
                dir144 = dir143;
            }
            if (d144 > d129 + r144) { // from (2, 1)
                d144 = d129 + r144;
                dir144 = dir129;
            }
        }

        if (rc.onTheMap(l109)) { // check (-3, 0)
            if (d109 > d95 + r109) { // from (-2, -1)
                d109 = d95 + r109;
                dir109 = dir95;
            }
            if (d109 > d110 + r109) { // from (-2, 0)
                d109 = d110 + r109;
                dir109 = dir110;
            }
            if (d109 > d125 + r109) { // from (-2, 1)
                d109 = d125 + r109;
                dir109 = dir125;
            }
        }

        if (rc.onTheMap(l67)) { // check (0, -3)
            if (d67 > d81 + r67) { // from (-1, -2)
                d67 = d81 + r67;
                dir67 = dir81;
            }
            if (d67 > d82 + r67) { // from (0, -2)
                d67 = d82 + r67;
                dir67 = dir82;
            }
            if (d67 > d83 + r67) { // from (1, -2)
                d67 = d83 + r67;
                dir67 = dir83;
            }
        }

        if (rc.onTheMap(l157)) { // check (0, 3)
            if (d157 > d141 + r157) { // from (-1, 2)
                d157 = d141 + r157;
                dir157 = dir141;
            }
            if (d157 > d142 + r157) { // from (0, 2)
                d157 = d142 + r157;
                dir157 = dir142;
            }
            if (d157 > d143 + r157) { // from (1, 2)
                d157 = d143 + r157;
                dir157 = dir143;
            }
        }

        if (rc.onTheMap(l115)) { // check (3, 0)
            if (d115 > d99 + r115) { // from (2, -1)
                d115 = d99 + r115;
                dir115 = dir99;
            }
            if (d115 > d114 + r115) { // from (2, 0)
                d115 = d114 + r115;
                dir115 = dir114;
            }
            if (d115 > d129 + r115) { // from (2, 1)
                d115 = d129 + r115;
                dir115 = dir129;
            }
        }

        if (rc.onTheMap(l94)) { // check (-3, -1)
            if (d94 > d109 + r94) { // from (-3, 0)
                d94 = d109 + r94;
                dir94 = dir109;
            }
            if (d94 > d80 + r94) { // from (-2, -2)
                d94 = d80 + r94;
                dir94 = dir80;
            }
            if (d94 > d95 + r94) { // from (-2, -1)
                d94 = d95 + r94;
                dir94 = dir95;
            }
            if (d94 > d110 + r94) { // from (-2, 0)
                d94 = d110 + r94;
                dir94 = dir110;
            }
        }

        if (rc.onTheMap(l124)) { // check (-3, 1)
            if (d124 > d109 + r124) { // from (-3, 0)
                d124 = d109 + r124;
                dir124 = dir109;
            }
            if (d124 > d110 + r124) { // from (-2, 0)
                d124 = d110 + r124;
                dir124 = dir110;
            }
            if (d124 > d125 + r124) { // from (-2, 1)
                d124 = d125 + r124;
                dir124 = dir125;
            }
            if (d124 > d140 + r124) { // from (-2, 2)
                d124 = d140 + r124;
                dir124 = dir140;
            }
        }

        if (rc.onTheMap(l66)) { // check (-1, -3)
            if (d66 > d80 + r66) { // from (-2, -2)
                d66 = d80 + r66;
                dir66 = dir80;
            }
            if (d66 > d81 + r66) { // from (-1, -2)
                d66 = d81 + r66;
                dir66 = dir81;
            }
            if (d66 > d67 + r66) { // from (0, -3)
                d66 = d67 + r66;
                dir66 = dir67;
            }
            if (d66 > d82 + r66) { // from (0, -2)
                d66 = d82 + r66;
                dir66 = dir82;
            }
        }

        if (rc.onTheMap(l156)) { // check (-1, 3)
            if (d156 > d140 + r156) { // from (-2, 2)
                d156 = d140 + r156;
                dir156 = dir140;
            }
            if (d156 > d141 + r156) { // from (-1, 2)
                d156 = d141 + r156;
                dir156 = dir141;
            }
            if (d156 > d142 + r156) { // from (0, 2)
                d156 = d142 + r156;
                dir156 = dir142;
            }
            if (d156 > d157 + r156) { // from (0, 3)
                d156 = d157 + r156;
                dir156 = dir157;
            }
        }

        if (rc.onTheMap(l68)) { // check (1, -3)
            if (d68 > d67 + r68) { // from (0, -3)
                d68 = d67 + r68;
                dir68 = dir67;
            }
            if (d68 > d82 + r68) { // from (0, -2)
                d68 = d82 + r68;
                dir68 = dir82;
            }
            if (d68 > d83 + r68) { // from (1, -2)
                d68 = d83 + r68;
                dir68 = dir83;
            }
            if (d68 > d84 + r68) { // from (2, -2)
                d68 = d84 + r68;
                dir68 = dir84;
            }
        }

        if (rc.onTheMap(l158)) { // check (1, 3)
            if (d158 > d142 + r158) { // from (0, 2)
                d158 = d142 + r158;
                dir158 = dir142;
            }
            if (d158 > d157 + r158) { // from (0, 3)
                d158 = d157 + r158;
                dir158 = dir157;
            }
            if (d158 > d143 + r158) { // from (1, 2)
                d158 = d143 + r158;
                dir158 = dir143;
            }
            if (d158 > d144 + r158) { // from (2, 2)
                d158 = d144 + r158;
                dir158 = dir144;
            }
        }

        if (rc.onTheMap(l100)) { // check (3, -1)
            if (d100 > d84 + r100) { // from (2, -2)
                d100 = d84 + r100;
                dir100 = dir84;
            }
            if (d100 > d99 + r100) { // from (2, -1)
                d100 = d99 + r100;
                dir100 = dir99;
            }
            if (d100 > d114 + r100) { // from (2, 0)
                d100 = d114 + r100;
                dir100 = dir114;
            }
            if (d100 > d115 + r100) { // from (3, 0)
                d100 = d115 + r100;
                dir100 = dir115;
            }
        }

        if (rc.onTheMap(l130)) { // check (3, 1)
            if (d130 > d114 + r130) { // from (2, 0)
                d130 = d114 + r130;
                dir130 = dir114;
            }
            if (d130 > d129 + r130) { // from (2, 1)
                d130 = d129 + r130;
                dir130 = dir129;
            }
            if (d130 > d144 + r130) { // from (2, 2)
                d130 = d144 + r130;
                dir130 = dir144;
            }
            if (d130 > d115 + r130) { // from (3, 0)
                d130 = d115 + r130;
                dir130 = dir115;
            }
        }

        if (rc.onTheMap(l79)) { // check (-3, -2)
            if (d79 > d94 + r79) { // from (-3, -1)
                d79 = d94 + r79;
                dir79 = dir94;
            }
            if (d79 > d80 + r79) { // from (-2, -2)
                d79 = d80 + r79;
                dir79 = dir80;
            }
            if (d79 > d95 + r79) { // from (-2, -1)
                d79 = d95 + r79;
                dir79 = dir95;
            }
        }

        if (rc.onTheMap(l139)) { // check (-3, 2)
            if (d139 > d124 + r139) { // from (-3, 1)
                d139 = d124 + r139;
                dir139 = dir124;
            }
            if (d139 > d125 + r139) { // from (-2, 1)
                d139 = d125 + r139;
                dir139 = dir125;
            }
            if (d139 > d140 + r139) { // from (-2, 2)
                d139 = d140 + r139;
                dir139 = dir140;
            }
        }

        if (rc.onTheMap(l65)) { // check (-2, -3)
            if (d65 > d79 + r65) { // from (-3, -2)
                d65 = d79 + r65;
                dir65 = dir79;
            }
            if (d65 > d80 + r65) { // from (-2, -2)
                d65 = d80 + r65;
                dir65 = dir80;
            }
            if (d65 > d66 + r65) { // from (-1, -3)
                d65 = d66 + r65;
                dir65 = dir66;
            }
            if (d65 > d81 + r65) { // from (-1, -2)
                d65 = d81 + r65;
                dir65 = dir81;
            }
        }

        if (rc.onTheMap(l155)) { // check (-2, 3)
            if (d155 > d139 + r155) { // from (-3, 2)
                d155 = d139 + r155;
                dir155 = dir139;
            }
            if (d155 > d140 + r155) { // from (-2, 2)
                d155 = d140 + r155;
                dir155 = dir140;
            }
            if (d155 > d141 + r155) { // from (-1, 2)
                d155 = d141 + r155;
                dir155 = dir141;
            }
            if (d155 > d156 + r155) { // from (-1, 3)
                d155 = d156 + r155;
                dir155 = dir156;
            }
        }

        if (rc.onTheMap(l69)) { // check (2, -3)
            if (d69 > d68 + r69) { // from (1, -3)
                d69 = d68 + r69;
                dir69 = dir68;
            }
            if (d69 > d83 + r69) { // from (1, -2)
                d69 = d83 + r69;
                dir69 = dir83;
            }
            if (d69 > d84 + r69) { // from (2, -2)
                d69 = d84 + r69;
                dir69 = dir84;
            }
        }

        if (rc.onTheMap(l159)) { // check (2, 3)
            if (d159 > d143 + r159) { // from (1, 2)
                d159 = d143 + r159;
                dir159 = dir143;
            }
            if (d159 > d158 + r159) { // from (1, 3)
                d159 = d158 + r159;
                dir159 = dir158;
            }
            if (d159 > d144 + r159) { // from (2, 2)
                d159 = d144 + r159;
                dir159 = dir144;
            }
        }

        if (rc.onTheMap(l85)) { // check (3, -2)
            if (d85 > d69 + r85) { // from (2, -3)
                d85 = d69 + r85;
                dir85 = dir69;
            }
            if (d85 > d84 + r85) { // from (2, -2)
                d85 = d84 + r85;
                dir85 = dir84;
            }
            if (d85 > d99 + r85) { // from (2, -1)
                d85 = d99 + r85;
                dir85 = dir99;
            }
            if (d85 > d100 + r85) { // from (3, -1)
                d85 = d100 + r85;
                dir85 = dir100;
            }
        }

        if (rc.onTheMap(l145)) { // check (3, 2)
            if (d145 > d129 + r145) { // from (2, 1)
                d145 = d129 + r145;
                dir145 = dir129;
            }
            if (d145 > d144 + r145) { // from (2, 2)
                d145 = d144 + r145;
                dir145 = dir144;
            }
            if (d145 > d159 + r145) { // from (2, 3)
                d145 = d159 + r145;
                dir145 = dir159;
            }
            if (d145 > d130 + r145) { // from (3, 1)
                d145 = d130 + r145;
                dir145 = dir130;
            }
        }

        if (rc.onTheMap(l108)) { // check (-4, 0)
            if (d108 > d94 + r108) { // from (-3, -1)
                d108 = d94 + r108;
                dir108 = dir94;
            }
            if (d108 > d109 + r108) { // from (-3, 0)
                d108 = d109 + r108;
                dir108 = dir109;
            }
            if (d108 > d124 + r108) { // from (-3, 1)
                d108 = d124 + r108;
                dir108 = dir124;
            }
        }

        if (rc.onTheMap(l52)) { // check (0, -4)
            if (d52 > d66 + r52) { // from (-1, -3)
                d52 = d66 + r52;
                dir52 = dir66;
            }
            if (d52 > d67 + r52) { // from (0, -3)
                d52 = d67 + r52;
                dir52 = dir67;
            }
            if (d52 > d68 + r52) { // from (1, -3)
                d52 = d68 + r52;
                dir52 = dir68;
            }
        }

        if (rc.onTheMap(l172)) { // check (0, 4)
            if (d172 > d156 + r172) { // from (-1, 3)
                d172 = d156 + r172;
                dir172 = dir156;
            }
            if (d172 > d157 + r172) { // from (0, 3)
                d172 = d157 + r172;
                dir172 = dir157;
            }
            if (d172 > d158 + r172) { // from (1, 3)
                d172 = d158 + r172;
                dir172 = dir158;
            }
        }

        if (rc.onTheMap(l116)) { // check (4, 0)
            if (d116 > d100 + r116) { // from (3, -1)
                d116 = d100 + r116;
                dir116 = dir100;
            }
            if (d116 > d115 + r116) { // from (3, 0)
                d116 = d115 + r116;
                dir116 = dir115;
            }
            if (d116 > d130 + r116) { // from (3, 1)
                d116 = d130 + r116;
                dir116 = dir130;
            }
        }

        if (rc.onTheMap(l93)) { // check (-4, -1)
            if (d93 > d108 + r93) { // from (-4, 0)
                d93 = d108 + r93;
                dir93 = dir108;
            }
            if (d93 > d79 + r93) { // from (-3, -2)
                d93 = d79 + r93;
                dir93 = dir79;
            }
            if (d93 > d94 + r93) { // from (-3, -1)
                d93 = d94 + r93;
                dir93 = dir94;
            }
            if (d93 > d109 + r93) { // from (-3, 0)
                d93 = d109 + r93;
                dir93 = dir109;
            }
        }

        if (rc.onTheMap(l123)) { // check (-4, 1)
            if (d123 > d108 + r123) { // from (-4, 0)
                d123 = d108 + r123;
                dir123 = dir108;
            }
            if (d123 > d109 + r123) { // from (-3, 0)
                d123 = d109 + r123;
                dir123 = dir109;
            }
            if (d123 > d124 + r123) { // from (-3, 1)
                d123 = d124 + r123;
                dir123 = dir124;
            }
            if (d123 > d139 + r123) { // from (-3, 2)
                d123 = d139 + r123;
                dir123 = dir139;
            }
        }

        if (rc.onTheMap(l51)) { // check (-1, -4)
            if (d51 > d65 + r51) { // from (-2, -3)
                d51 = d65 + r51;
                dir51 = dir65;
            }
            if (d51 > d66 + r51) { // from (-1, -3)
                d51 = d66 + r51;
                dir51 = dir66;
            }
            if (d51 > d52 + r51) { // from (0, -4)
                d51 = d52 + r51;
                dir51 = dir52;
            }
            if (d51 > d67 + r51) { // from (0, -3)
                d51 = d67 + r51;
                dir51 = dir67;
            }
        }

        if (rc.onTheMap(l171)) { // check (-1, 4)
            if (d171 > d155 + r171) { // from (-2, 3)
                d171 = d155 + r171;
                dir171 = dir155;
            }
            if (d171 > d156 + r171) { // from (-1, 3)
                d171 = d156 + r171;
                dir171 = dir156;
            }
            if (d171 > d157 + r171) { // from (0, 3)
                d171 = d157 + r171;
                dir171 = dir157;
            }
            if (d171 > d172 + r171) { // from (0, 4)
                d171 = d172 + r171;
                dir171 = dir172;
            }
        }

        if (rc.onTheMap(l53)) { // check (1, -4)
            if (d53 > d52 + r53) { // from (0, -4)
                d53 = d52 + r53;
                dir53 = dir52;
            }
            if (d53 > d67 + r53) { // from (0, -3)
                d53 = d67 + r53;
                dir53 = dir67;
            }
            if (d53 > d68 + r53) { // from (1, -3)
                d53 = d68 + r53;
                dir53 = dir68;
            }
            if (d53 > d69 + r53) { // from (2, -3)
                d53 = d69 + r53;
                dir53 = dir69;
            }
        }

        if (rc.onTheMap(l173)) { // check (1, 4)
            if (d173 > d157 + r173) { // from (0, 3)
                d173 = d157 + r173;
                dir173 = dir157;
            }
            if (d173 > d172 + r173) { // from (0, 4)
                d173 = d172 + r173;
                dir173 = dir172;
            }
            if (d173 > d158 + r173) { // from (1, 3)
                d173 = d158 + r173;
                dir173 = dir158;
            }
            if (d173 > d159 + r173) { // from (2, 3)
                d173 = d159 + r173;
                dir173 = dir159;
            }
        }

        if (rc.onTheMap(l101)) { // check (4, -1)
            if (d101 > d85 + r101) { // from (3, -2)
                d101 = d85 + r101;
                dir101 = dir85;
            }
            if (d101 > d100 + r101) { // from (3, -1)
                d101 = d100 + r101;
                dir101 = dir100;
            }
            if (d101 > d115 + r101) { // from (3, 0)
                d101 = d115 + r101;
                dir101 = dir115;
            }
            if (d101 > d116 + r101) { // from (4, 0)
                d101 = d116 + r101;
                dir101 = dir116;
            }
        }

        if (rc.onTheMap(l131)) { // check (4, 1)
            if (d131 > d115 + r131) { // from (3, 0)
                d131 = d115 + r131;
                dir131 = dir115;
            }
            if (d131 > d130 + r131) { // from (3, 1)
                d131 = d130 + r131;
                dir131 = dir130;
            }
            if (d131 > d145 + r131) { // from (3, 2)
                d131 = d145 + r131;
                dir131 = dir145;
            }
            if (d131 > d116 + r131) { // from (4, 0)
                d131 = d116 + r131;
                dir131 = dir116;
            }
        }

        if (rc.onTheMap(l64)) { // check (-3, -3)
            if (d64 > d79 + r64) { // from (-3, -2)
                d64 = d79 + r64;
                dir64 = dir79;
            }
            if (d64 > d65 + r64) { // from (-2, -3)
                d64 = d65 + r64;
                dir64 = dir65;
            }
            if (d64 > d80 + r64) { // from (-2, -2)
                d64 = d80 + r64;
                dir64 = dir80;
            }
        }

        if (rc.onTheMap(l154)) { // check (-3, 3)
            if (d154 > d139 + r154) { // from (-3, 2)
                d154 = d139 + r154;
                dir154 = dir139;
            }
            if (d154 > d140 + r154) { // from (-2, 2)
                d154 = d140 + r154;
                dir154 = dir140;
            }
            if (d154 > d155 + r154) { // from (-2, 3)
                d154 = d155 + r154;
                dir154 = dir155;
            }
        }

        if (rc.onTheMap(l70)) { // check (3, -3)
            if (d70 > d69 + r70) { // from (2, -3)
                d70 = d69 + r70;
                dir70 = dir69;
            }
            if (d70 > d84 + r70) { // from (2, -2)
                d70 = d84 + r70;
                dir70 = dir84;
            }
            if (d70 > d85 + r70) { // from (3, -2)
                d70 = d85 + r70;
                dir70 = dir85;
            }
        }

        if (rc.onTheMap(l160)) { // check (3, 3)
            if (d160 > d144 + r160) { // from (2, 2)
                d160 = d144 + r160;
                dir160 = dir144;
            }
            if (d160 > d159 + r160) { // from (2, 3)
                d160 = d159 + r160;
                dir160 = dir159;
            }
            if (d160 > d145 + r160) { // from (3, 2)
                d160 = d145 + r160;
                dir160 = dir145;
            }
        }

        if (rc.onTheMap(l78)) { // check (-4, -2)
            if (d78 > d93 + r78) { // from (-4, -1)
                d78 = d93 + r78;
                dir78 = dir93;
            }
            if (d78 > d64 + r78) { // from (-3, -3)
                d78 = d64 + r78;
                dir78 = dir64;
            }
            if (d78 > d79 + r78) { // from (-3, -2)
                d78 = d79 + r78;
                dir78 = dir79;
            }
            if (d78 > d94 + r78) { // from (-3, -1)
                d78 = d94 + r78;
                dir78 = dir94;
            }
        }

        if (rc.onTheMap(l138)) { // check (-4, 2)
            if (d138 > d123 + r138) { // from (-4, 1)
                d138 = d123 + r138;
                dir138 = dir123;
            }
            if (d138 > d124 + r138) { // from (-3, 1)
                d138 = d124 + r138;
                dir138 = dir124;
            }
            if (d138 > d139 + r138) { // from (-3, 2)
                d138 = d139 + r138;
                dir138 = dir139;
            }
            if (d138 > d154 + r138) { // from (-3, 3)
                d138 = d154 + r138;
                dir138 = dir154;
            }
        }

        if (rc.onTheMap(l50)) { // check (-2, -4)
            if (d50 > d64 + r50) { // from (-3, -3)
                d50 = d64 + r50;
                dir50 = dir64;
            }
            if (d50 > d65 + r50) { // from (-2, -3)
                d50 = d65 + r50;
                dir50 = dir65;
            }
            if (d50 > d51 + r50) { // from (-1, -4)
                d50 = d51 + r50;
                dir50 = dir51;
            }
            if (d50 > d66 + r50) { // from (-1, -3)
                d50 = d66 + r50;
                dir50 = dir66;
            }
        }

        if (rc.onTheMap(l170)) { // check (-2, 4)
            if (d170 > d154 + r170) { // from (-3, 3)
                d170 = d154 + r170;
                dir170 = dir154;
            }
            if (d170 > d155 + r170) { // from (-2, 3)
                d170 = d155 + r170;
                dir170 = dir155;
            }
            if (d170 > d156 + r170) { // from (-1, 3)
                d170 = d156 + r170;
                dir170 = dir156;
            }
            if (d170 > d171 + r170) { // from (-1, 4)
                d170 = d171 + r170;
                dir170 = dir171;
            }
        }

        if (rc.onTheMap(l54)) { // check (2, -4)
            if (d54 > d53 + r54) { // from (1, -4)
                d54 = d53 + r54;
                dir54 = dir53;
            }
            if (d54 > d68 + r54) { // from (1, -3)
                d54 = d68 + r54;
                dir54 = dir68;
            }
            if (d54 > d69 + r54) { // from (2, -3)
                d54 = d69 + r54;
                dir54 = dir69;
            }
            if (d54 > d70 + r54) { // from (3, -3)
                d54 = d70 + r54;
                dir54 = dir70;
            }
        }

        if (rc.onTheMap(l174)) { // check (2, 4)
            if (d174 > d158 + r174) { // from (1, 3)
                d174 = d158 + r174;
                dir174 = dir158;
            }
            if (d174 > d173 + r174) { // from (1, 4)
                d174 = d173 + r174;
                dir174 = dir173;
            }
            if (d174 > d159 + r174) { // from (2, 3)
                d174 = d159 + r174;
                dir174 = dir159;
            }
            if (d174 > d160 + r174) { // from (3, 3)
                d174 = d160 + r174;
                dir174 = dir160;
            }
        }

        if (rc.onTheMap(l86)) { // check (4, -2)
            if (d86 > d70 + r86) { // from (3, -3)
                d86 = d70 + r86;
                dir86 = dir70;
            }
            if (d86 > d85 + r86) { // from (3, -2)
                d86 = d85 + r86;
                dir86 = dir85;
            }
            if (d86 > d100 + r86) { // from (3, -1)
                d86 = d100 + r86;
                dir86 = dir100;
            }
            if (d86 > d101 + r86) { // from (4, -1)
                d86 = d101 + r86;
                dir86 = dir101;
            }
        }

        if (rc.onTheMap(l146)) { // check (4, 2)
            if (d146 > d130 + r146) { // from (3, 1)
                d146 = d130 + r146;
                dir146 = dir130;
            }
            if (d146 > d145 + r146) { // from (3, 2)
                d146 = d145 + r146;
                dir146 = dir145;
            }
            if (d146 > d160 + r146) { // from (3, 3)
                d146 = d160 + r146;
                dir146 = dir160;
            }
            if (d146 > d131 + r146) { // from (4, 1)
                d146 = d131 + r146;
                dir146 = dir131;
            }
        }


        int target_dx = target.x - l112.x;
        int target_dy = target.y - l112.y;
        switch (target_dx) {
                case -4:
                    switch (target_dy) {
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
                    }
                    break;
                case -3:
                    switch (target_dy) {
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
                    }
                    break;
                case -2:
                    switch (target_dy) {
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
                    }
                    break;
                case 3:
                    switch (target_dy) {
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
                    }
                    break;
                case 4:
                    switch (target_dy) {
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
                    }
                    break;
        }
        
        Direction ans = null;
        double bestScore = 0;
        double currDist = Math.sqrt(l112.distanceSquaredTo(target));
        
        double score78 = (currDist - Math.sqrt(l78.distanceSquaredTo(target))) / d78;
        if (score78 > bestScore) {
            bestScore = score78;
            ans = dir78;
        }

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

        double score138 = (currDist - Math.sqrt(l138.distanceSquaredTo(target))) / d138;
        if (score138 > bestScore) {
            bestScore = score138;
            ans = dir138;
        }

        double score64 = (currDist - Math.sqrt(l64.distanceSquaredTo(target))) / d64;
        if (score64 > bestScore) {
            bestScore = score64;
            ans = dir64;
        }

        double score79 = (currDist - Math.sqrt(l79.distanceSquaredTo(target))) / d79;
        if (score79 > bestScore) {
            bestScore = score79;
            ans = dir79;
        }

        double score139 = (currDist - Math.sqrt(l139.distanceSquaredTo(target))) / d139;
        if (score139 > bestScore) {
            bestScore = score139;
            ans = dir139;
        }

        double score154 = (currDist - Math.sqrt(l154.distanceSquaredTo(target))) / d154;
        if (score154 > bestScore) {
            bestScore = score154;
            ans = dir154;
        }

        double score50 = (currDist - Math.sqrt(l50.distanceSquaredTo(target))) / d50;
        if (score50 > bestScore) {
            bestScore = score50;
            ans = dir50;
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

        double score170 = (currDist - Math.sqrt(l170.distanceSquaredTo(target))) / d170;
        if (score170 > bestScore) {
            bestScore = score170;
            ans = dir170;
        }

        double score51 = (currDist - Math.sqrt(l51.distanceSquaredTo(target))) / d51;
        if (score51 > bestScore) {
            bestScore = score51;
            ans = dir51;
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

        double score173 = (currDist - Math.sqrt(l173.distanceSquaredTo(target))) / d173;
        if (score173 > bestScore) {
            bestScore = score173;
            ans = dir173;
        }

        double score54 = (currDist - Math.sqrt(l54.distanceSquaredTo(target))) / d54;
        if (score54 > bestScore) {
            bestScore = score54;
            ans = dir54;
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

        double score174 = (currDist - Math.sqrt(l174.distanceSquaredTo(target))) / d174;
        if (score174 > bestScore) {
            bestScore = score174;
            ans = dir174;
        }

        double score70 = (currDist - Math.sqrt(l70.distanceSquaredTo(target))) / d70;
        if (score70 > bestScore) {
            bestScore = score70;
            ans = dir70;
        }

        double score85 = (currDist - Math.sqrt(l85.distanceSquaredTo(target))) / d85;
        if (score85 > bestScore) {
            bestScore = score85;
            ans = dir85;
        }

        double score145 = (currDist - Math.sqrt(l145.distanceSquaredTo(target))) / d145;
        if (score145 > bestScore) {
            bestScore = score145;
            ans = dir145;
        }

        double score160 = (currDist - Math.sqrt(l160.distanceSquaredTo(target))) / d160;
        if (score160 > bestScore) {
            bestScore = score160;
            ans = dir160;
        }

        double score86 = (currDist - Math.sqrt(l86.distanceSquaredTo(target))) / d86;
        if (score86 > bestScore) {
            bestScore = score86;
            ans = dir86;
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

        double score146 = (currDist - Math.sqrt(l146.distanceSquaredTo(target))) / d146;
        if (score146 > bestScore) {
            bestScore = score146;
            ans = dir146;
        }

        return ans;
    }
}
