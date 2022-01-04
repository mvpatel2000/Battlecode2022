import math

# spiral order starts north and goes clockwise

R2 = 20
D = []

for x in range(-10, 10):
    for y in range(-10, 10):
        if x**2 + y**2 <= R2:
            th = math.atan2(x,y) % (2*math.pi)
            D.append([x**2 + y**2, th, [x, y]])
reverse = [[-d[0], d[1], d[2]] for d in D]
reverse = sorted(reverse)
reverse = [d[2] for d in reverse]
D = sorted(D)
D = [d[2] for d in D]
print('Count: '+str(len(D)))
print('final static int[][] SENSE_SPIRAL_ORDER = {'+(',').join(['{'+str(x)+','+str(y)+'}' for x,y in D])+'};')
# print('Decreasing radius: ')
# print('{'+(',').join(['{'+str(x)+','+str(y)+'}' for x,y in reverse])+'}')

directions = {
    'NORTH': [0,1],
    'NORTHEAST': [1,1],
    'EAST': [1,0],
    'SOUTHEAST': [1,-1],
    'SOUTH': [0,-1],
    'SOUTHWEST': [-1,-1],
    'WEST': [-1,0],
    'NORTHWEST': [-1,1]
}

for di in directions.keys():
    de = directions[di]
    old_vision = [[x-de[0],y-de[1]] for x,y in D]
    new_spots = [d for d in D if d not in old_vision]
    print('final static int[][] NEW_SENSED_LOCS_'+di+' = {'+(',').join(['{'+str(x)+','+str(y)+'}' for x,y in new_spots])+'};')