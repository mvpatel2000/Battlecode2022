# Generate better ordering
import numpy as np

max_val = 0

def printgrid(grid):
    print("----------")
    for i in range(len(grid[0])):
        for j in range(len(grid)):
            print("{:02d}".format(grid[j][i]), end = " ")
        print()
    print("----------")

def offset_val(val, offset):
    global max_val
    if val*4 + offset > max_val:
        max_val = val*4 + offset
    return val*4 + offset

def flip_horizontal(x, y, end_x, end_y, grid):
    for i in range(x, (x+end_x)//2):
        for j in range(y, end_y):
            residual = i - x
            temp = grid[i][j]
            grid[i][j] = grid[end_x-1-residual][j]
            grid[end_x-1-residual][j] = temp
    return grid

def flip_vertical(x, y, end_x, end_y, grid):
    for i in range(x, end_y):
        for j in range(y, (y+end_y)//2):
            residual = j - y
            temp = grid[i][j]
            grid[i][j] = grid[i][end_y-1-residual]
            grid[i][end_y-1-residual] = temp
    return grid

def clamp(grid, highest_val):
    dct = {}
    for i in range(len(grid[0])):
        for j in range(len(grid)):
            dct[grid[i][j]] = [i, j]
    
    missing_vals = []
    for num in range(highest_val):
        if num not in dct:
            missing_vals.append(num)
    print(missing_vals)

    if len(missing_vals) == 0:
        printgrid(grid)
        return grid
    keys = sorted(dct.keys())
    for key in keys:
        if key > missing_vals[0]:
            print("greater", key, missing_vals[0])
            arr = dct[key]
            grid[arr[0]][arr[1]] = missing_vals[0]
            missing_vals[0] = key
            missing_vals = sorted(missing_vals)
            print(missing_vals)
    printgrid(grid)
    return grid

def generate_corner(start_x, start_y, end_x, end_y, offset, grid, count = 0):
    start_elem = count
    num_elem = count
    printgrid(grid)
    #grid[start_x][start_y] = offset_val(count, offset)
    x_range = [i for i in range(start_x, end_x)]
    y_range = [i for i in range(start_y+1, end_y)]
    x_idx = 0
    y_idx = 0
    while(1):
        if x_idx < len(x_range):
            grid[x_range[x_idx]][start_y] = offset_val(count, offset)
            count += 1
            num_elem += 1
            x_idx += 1
        if y_idx < len(y_range):
            grid[start_x][y_range[y_idx]] = offset_val(count, offset)
            count += 1
            num_elem += 1
            y_idx += 1
        if x_idx == len(x_range) and y_idx == len(y_range):
            break

    if start_y+1 == end_y or start_x+1 == end_x:
        print("Done with corner: ", offset)
        return grid
    return generate_corner(start_x+1, start_y+1, end_x, end_y, offset, grid, num_elem)
            

def generate_order(i, j, grid):
    global max_val
    half_i = i//2
    half_j = j//2
    grid = generate_corner(0, 0, half_i, half_j, 0, grid)

    grid = generate_corner(0, half_j, half_i, j, 2, grid)
    grid = flip_vertical(0, half_j, half_i, j, grid)

    grid = generate_corner(half_i, 0, i, half_j, 3, grid)
    grid = flip_horizontal(half_i, 0, i, half_j, grid)

    grid = generate_corner(half_i, half_j, i, j, 1, grid)
    grid = flip_horizontal(half_i, half_j, i, j, grid)
    grid = flip_vertical(half_i, half_j, i, j, grid)

    printgrid(grid)

    grid = clamp(grid, max_val)
    return grid
"""
for i in range(7, 11):
    for j in range(9, 11):
        grid = [[0 for k in range(i)] for m in range(j)]
        grid = generate_order(i, j, grid)
        quit()
"""
def invert_grid(i, j, grid):
    arr = []
    inverted_arr = [0]*(i*j)
    for x in range(i):
        for y in range(j):
            arr.append(grid[y][x])

    for k in range(len(arr)):
        inverted_arr[arr[k]] = k

    return inverted_arr

def solvegrid(i, j, grid):
    # Hard-code centers and corners
    center_x = i//2
    center_y = j//2
    grid[center_y][center_x] = 0
    grid[0][0] = 1
    grid[j-1][0] = 2
    grid[0][i-1] = 3
    grid[j-1][i-1] = 4
    # Randomly place remaining values.
    remaining_vals = np.arange(5, i*j)
    np.random.shuffle(remaining_vals)
    idx = 0
    for x in range(i):
        for y in range(j):
            if grid[y][x] == -1:
                grid[y][x] = remaining_vals[idx]
                idx += 1
    #printgrid(grid)
    return grid

def compute_permutation(width, height):
    j = width
    i = height
    grid = [[-1 for k in range(i)] for m in range(j)]
    grid = solvegrid(i, j, grid)
    return invert_grid(i, j, grid)