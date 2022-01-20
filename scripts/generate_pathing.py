import sys
from pathlib import Path

def encode(x, y):
    return (x+7) + 15*(y+7)

VISION_RADIUS = {'Miner': 20, 'Builder': 20, 'Soldier': 20, 'Sage': 34, 'Archon': 34}[sys.argv[2]]
SMALLER_RADIUS = {'Miner': 10, 'Builder': 10, 'Soldier': 10, 'Sage': 20, 'Archon': 20}[sys.argv[2]]

DIRECTIONS = {
    (1, 0): 'Direction.EAST',
    (-1, 0): 'Direction.WEST',
    (0, 1): 'Direction.NORTH',
    (0, -1): 'Direction.SOUTH',
    (1, 1): 'Direction.NORTHEAST',
    (-1, 1): 'Direction.NORTHWEST',
    (1, -1): 'Direction.SOUTHEAST',
    (-1, -1): 'Direction.SOUTHWEST',
}

def dist(x, y):
    return x*x + y*y

def gen_constants():
    out = f""""""
    for x in range(-7, 8):
        for y in range(-7, 8):
            if dist(x, y) <= VISION_RADIUS:
                out += f"""
    static MapLocation l{encode(x,y)}; // location representing relative coordinate ({x}, {y})
    static int d{encode(x,y)}; // shortest distance to location from current location
    static Direction dir{encode(x,y)}; // best direction to take now to optimally get to location
"""
    return out

def sign(x):
    if x > 0:
        return 1
    if x < 0:
        return -1
    return 0

def gen_init():
    out = f"""
        l{encode(0,0)} = rc.getLocation();
        d{encode(0,0)} = 0;
        dir{encode(0,0)} = Direction.CENTER;
"""
    for r2 in range(1, VISION_RADIUS+1):
        for x in range(-7, 8):
            for y in range(-7, 8):
                if dist(x, y) == r2:
                    out += f"""
        l{encode(x,y)} = l{encode(x - sign(x), y - sign(y))}.add({DIRECTIONS[(sign(x), sign(y))]}); // ({x}, {y}) from ({x - sign(x)}, {y - sign(y)})
        d{encode(x,y)} = 99999;
        dir{encode(x,y)} = null;
"""
    return out

def gen_bfs():
    visited = set([encode(0,0)])
    out = f"""
"""
    for r2 in range(1, VISION_RADIUS+1):
        for x in range(-7, 8):
            for y in range(-7, 8):
                if dist(x, y) == r2:
                    out += f"""
        if (rc.onTheMap(l{encode(x,y)})) {{ // check ({x}, {y})"""
                    indent = ""
                    if r2 <= 2:
                        out += f"""
            if (!rc.isLocationOccupied(l{encode(x,y)})) {{ """
                        indent = "    "
                    dxdy = [(dx, dy) for dx in range(-1, 2) for dy in range(-1, 2) if (dx, dy) != (0, 0) and dist(x+dx,y+dy) <= VISION_RADIUS]
                    dxdy = sorted(dxdy, key=lambda dd: dist(x+dd[0], y+dd[1]))
                    for dx, dy in dxdy:
                        if encode(x+dx, y+dy) in visited:
                            out += f"""
            {indent}if (d{encode(x,y)} > d{encode(x+dx,y+dy)}) {{ // from ({x+dx}, {y+dy})
                {indent}d{encode(x,y)} = d{encode(x+dx,y+dy)};
                {indent}dir{encode(x,y)} = {DIRECTIONS[(-dx, -dy)] if (x+dx,y+dy) == (0, 0) else f'dir{encode(x+dx,y+dy)}'};
            {indent}}}"""
                    out += f"""
            {indent}d{encode(x,y)} += rc.senseRubble(l{encode(x,y)}) + 10;"""
                    if r2 <= 2:
                        out += f"""
            }}"""
                    visited.add(encode(x,y))
                    out += f"""
        }}
"""
    return out

def gen_selection():
    out = f"""
        int target_dx = target.x - l{encode(0,0)}.x;
        int target_dy = target.y - l{encode(0,0)}.y;
        switch (target_dx) {{"""
    for tdx in range(-7, 8):
        if tdx**2 <= VISION_RADIUS:
            out += f"""
                case {tdx}:
                    switch (target_dy) {{"""
            for tdy in range(-7, 8):
                if dist(tdx, tdy) <= VISION_RADIUS:
                    out += f"""
                        case {tdy}:
                            return dir{encode(tdx, tdy)}; // destination is at relative location ({tdx}, {tdy})"""
            out += f"""
                    }}
                    break;"""
    out += f"""
        }}
        
        Direction ans = null;
        double bestScore = 0;
        double currDist = Math.sqrt(l{encode(0,0)}.distanceSquaredTo(target));
        """
    for x in range(-7, 8):
        for y in range(-7, 8):
            if SMALLER_RADIUS < dist(x, y) <= VISION_RADIUS: # on the edge of the vision radius
                out += f"""
        double score{encode(x,y)} = (currDist - Math.sqrt(l{encode(x,y)}.distanceSquaredTo(target))) / d{encode(x,y)};
        if (score{encode(x,y)} > bestScore) {{
            bestScore = score{encode(x,y)};
            ans = dir{encode(x,y)};
        }}
"""
    return out

def gen_print():
    out = f"""
        // System.out.println("LOCAL DISTANCES:");"""
    for y in range(7, -8, -1):
        if y**2 <= VISION_RADIUS:
            out += f"""
        // System.out.println("""
            for x in range(-7, 8):
                if dist(x, y) <= VISION_RADIUS:
                    out += f""""\\t" + d{encode(x,y)} + """
                else:
                    out += f""""\\t" + """
            out = out[:-3] + """);"""
    out += f"""
        // System.out.println("DIRECTIONS:");"""
    for y in range(7, -8, -1):
        if y**2 <= VISION_RADIUS:
            out += f"""
        // System.out.println("""
            for x in range(-7, 8):
                if dist(x, y) <= VISION_RADIUS:
                    out += f""""\\t" + dir{encode(x,y)} + """
                else:
                    out += f""""\\t" + """
            out = out[:-3] + """);"""
    return out

if __name__ == '__main__':
    out_file = Path('./src/') / sys.argv[1] / f'{sys.argv[2]}Pathing.java'
    with open(out_file, 'w') as f:
        f.write(f"""// Inspired by https://github.com/IvanGeffner/battlecode2021/blob/master/thirtyone/BFSMuckraker.java.
package {sys.argv[1]};

import battlecode.common.*;

public class {sys.argv[2]}Pathing {{
    
    RobotController rc;
{gen_constants()}

    public {sys.argv[2]}Pathing(RobotController rc) {{
        this.rc = rc;
    }}

    public Direction bestDir(MapLocation target) throws GameActionException {{
{gen_init()}
{gen_bfs()}
{gen_print()}
{gen_selection()}
        
        return ans;
    }}
}}
""")