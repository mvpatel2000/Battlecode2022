
def computePermutation(width, height):
    # TODO (Nikhil)
    return []

if __name__ == '__main__':
    out = f"""
    switch (clusterWidths.length) {{"""
    for i in range(4, 11):
        out += f"""
        case {i}:
            switch (clusterHeights.length) {{"""
        for j in range(4, 11):
            out += f"""
            case {j}:
                clusterPermutation = {{{computePermutation(i, j)}}};"""
        out += f"""
            }}
            break;"""
    out += f"""
    }}"""


# 2   7 3
# 6     
#       5
# 0 4   1

# 12131415
# 8 9 1011
# 4 5 6 7
# 0 1 2 3 

# 0 2 1 0
# 1 3 3 2
# 2 3 3 1
# 0 1 2 0



# 4 5 6 7
# 0 1 2 3

# 6
# 4 9
# 2 7 8
# 0 1 3 5