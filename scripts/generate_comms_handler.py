from pathlib import Path
import sys

SCHEMA = {
    'our_archon': {
        'slots': 4,
        'bits': {
            'status': 2,
            'is_moving': 1,
            'accepting_patients': 1,
            'x_coord': 6,
            'y_coord': 6,
        }
    },
    'worker_count': { # make sure this is in a single int
        'slots': 1,
        'bits': {
            'miners': 8,
            'builders': 8,
        }
    },
    'fighter_count': { # make sure this is in a single int
        'slots': 1,
        'bits': {
            'soldiers': 8,
            'sages': 8,
        }
    },
    'cluster': {
        'slots': 100,
        'bits': {
            'control_status': 3,
            'resource_count': 3,
        }
    },
    'combat_cluster': {
        'slots': 10,
        'bits': {
            'index': 7,
        }
    },
    'explore_cluster': {
        'slots': 10,
        'bits': {
            'claim_status': 1,
            'index': 7,
        }
    },
    'mine_cluster': {
        'slots': 10,
        'bits': {
            'claim_status': 3,
            'index': 7,
        }
    },
    'last_archon': {
        'slots': 1,
        'bits': {
            'num': 2,
        }
    },
    'reserved_resources': {
        'slots': 1,
        'bits': {
            'lead': 10,
            'gold': 6,
        }
    },
    'map': {
        'slots': 1,
        'bits': {
            'symmetry': 2
        }
    }
}

def gen_constants():
    out = """"""
    for datatype in SCHEMA:
        out += f"""
    final int {datatype.upper()}_SLOTS = {SCHEMA[datatype]['slots']};"""
    return out+"\n"

def gen():
    out = """"""""
    bits_so_far = 0
    for datatype in SCHEMA:
        datatype_bits = sum(SCHEMA[datatype]['bits'].values())
        prefix_bits = 0

        for attribute in [*SCHEMA[datatype]['bits'], 'all']:
            if attribute == 'all':
                attribute_bits = datatype_bits
                prefix_bits = 0
            else:
                attribute_bits = SCHEMA[datatype]['bits'][attribute]

            # read function
            rets = []
            for idx in range(SCHEMA[datatype]['slots']):
                start_bit = bits_so_far + datatype_bits*idx + prefix_bits
                # we want to read attribute_bits starting from start_bit
                start_int = start_bit // 16
                rem = start_bit % 16
                end_int = (start_bit + attribute_bits - 1) // 16
                ret = ""
                if start_int == end_int:
                    bitstring = '1' * attribute_bits + '0' * (16 - attribute_bits - rem)
                    ret = f"(rc.readSharedArray({start_int}) & {int(bitstring, 2)}) >>> {(16 - attribute_bits - rem)}"
                else:
                    part1_bitstring = '1' * (16 - rem)
                    part2_bitstring = '1' * (attribute_bits + rem - 16) + '0' * (32 - attribute_bits - rem)
                    ret = f"((rc.readSharedArray({start_int}) & {int(part1_bitstring, 2)}) << {(attribute_bits + rem - 16)}) + ((rc.readSharedArray({end_int}) & {int(part2_bitstring, 2)}) >>> {(32 - attribute_bits - rem)})"
                rets.append(ret)
            
            if SCHEMA[datatype]['slots'] == 1:
                out += f"""
    public int read{capitalize(datatype)}{capitalize(attribute)}() throws GameActionException {{
        return {rets[0]};
    }}
"""
            else:
                out += f"""
    public int read{capitalize(datatype)}{capitalize(attribute)}(int idx) throws GameActionException {{
        switch (idx) {{"""
                for idx, ret in enumerate(rets):
                    out += f"""
            case {idx}:
                return {ret};"""
                out += f"""
            default:
                return -1;
        }}
    }}
"""

            # write function
            writes = []
            for idx in range(SCHEMA[datatype]['slots']):
                start_bit = bits_so_far + datatype_bits*idx + prefix_bits
                # we want to write attribute_bits starting from start_bit
                start_int = start_bit // 16
                rem = start_bit % 16
                end_int = (start_bit + attribute_bits - 1) // 16
                write = []
                if start_int == end_int:
                    bitstring = '1' * rem + '0' * attribute_bits + '1' * (16 - attribute_bits - rem)
                    write.append(f"rc.writeSharedArray({start_int}, (rc.readSharedArray({start_int}) & {int(bitstring, 2)}) | (value << {(16 - attribute_bits - rem)}))")
                else:
                    part1_bitstring = '1' * rem + '0' * (16 - rem)
                    part2_bitstring = '0' * (attribute_bits + rem - 16) + '1' * (32 - attribute_bits - rem)
                    value1_bitstring = '1' * (16 - rem) + '0' * (attribute_bits + rem - 16)
                    value2_bitstring = '1' * (attribute_bits + rem - 16)
                    write.append(f"rc.writeSharedArray({start_int}, (rc.readSharedArray({start_int}) & {int(part1_bitstring, 2)}) | ((value & {int(value1_bitstring, 2)}) >>> {(attribute_bits + rem - 16)}))")
                    write.append(f"rc.writeSharedArray({end_int}, (rc.readSharedArray({end_int}) & {int(part2_bitstring, 2)}) | ((value & {int(value2_bitstring, 2)}) << {(32 - attribute_bits - rem)}))")
                writes.append(write)
            
            if SCHEMA[datatype]['slots'] == 1:
                out += f"""
    public void write{capitalize(datatype)}{capitalize(attribute)}(int value) throws GameActionException {{"""
                for w in writes[0]:
                    out += f"""
        {w};"""
                out += f"""
    }}
"""
            else:
                out += f"""
    public void write{capitalize(datatype)}{capitalize(attribute)}(int idx, int value) throws GameActionException {{
        switch (idx) {{"""
                for idx, write in enumerate(writes):
                    out += f"""
            case {idx}:"""
                    for w in write:
                        out += f"""
                {w};"""
                    out += f"""
                break;"""
                out += f"""
        }}
    }}
"""

            prefix_bits += attribute_bits

        bits_so_far += datatype_bits * SCHEMA[datatype]['slots']
    # remove redundant shifts
    out = out.replace(" >>> 0", "")
    out = out.replace(" << 0", "")
    return out

def capitalize(s):
    return ''.join(x.capitalize() for x in s.split('_'))

def gen_init_clusters():
    shmem = '0'*1024
    out = """    public void initPriorityClusters() throws GameActionException {"""
    bits_so_far = 0
    for datatype in SCHEMA:
        datatype_bits = sum(SCHEMA[datatype]['bits'].values())
        prefix_bits = 0

        if '_cluster' in datatype:
            for attribute in SCHEMA[datatype]['bits']:
                attribute_bits = SCHEMA[datatype]['bits'][attribute]
                if attribute == 'index':
                    for idx in range(SCHEMA[datatype]['slots']):
                        start_bit = bits_so_far + datatype_bits * idx + prefix_bits
                        shmem = shmem[:start_bit] + '1' * attribute_bits + shmem[start_bit+attribute_bits:]
                prefix_bits += attribute_bits
        bits_so_far += datatype_bits * SCHEMA[datatype]['slots']
    
    shmem = [shmem[16*i:16*i+16] for i in range(64)]
    for idx, word in enumerate(shmem):
        if word != '0'*16:
            out += f"""
        rc.writeSharedArray({idx}, {int(word, 2)});"""
    out += f"""
    }}
"""
    return out

if __name__ == '__main__':
    template_file = Path('./scripts/CommsHandlerTemplate.java')
    out_file = Path('./src/') / sys.argv[1] / 'CommsHandler.java'
    with open(template_file, 'r') as t:
        with open(out_file, 'w') as f:
            for line in t:
                if 'package examplefuncsplayer;' in line:
                    f.write(f"package {sys.argv[1]};\n")
                elif '// TO BE GENERATED' in line:
                    f.write(gen())
                elif '// CONSTS' in line:
                    f.write(gen_constants())
                elif '// PRIORITY CLUSTER INIT' in line:
                    f.write(gen_init_clusters())
                else:
                    f.write(line)