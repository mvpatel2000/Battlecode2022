
for dim in range(20, 61):
    num = -(-dim // 6)
    step = dim / float(num)
    sizes = []
    t = 0
    
    for i in range(num):
        p = int(t)
        t += step
        sizes.append(str(int(t) - p))
    print(f'case {dim}:')
    print('    return {' + ', '.join(sizes) + '};')