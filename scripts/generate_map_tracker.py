
def gen_add_switch():
    out = f"""
        switch (index) {{"""
    for i in range(3600):
        b = i % 32
        s = i // 32
        x = 2 ** (31 - b)
        if b == 0:
            x = -x
        out += f"""
            case {i}:
                tracker[{s}] = tracker[{s}] | {x};
                break;"""
    out += f"""
        }}"""
    return out

if __name__ == '__main__':
    with open('./scripts/map_tracker_switch.txt', 'w') as f:
        f.write(gen_add_switch())