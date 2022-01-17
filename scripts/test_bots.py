import subprocess
import concurrent.futures
import argparse
import logging

def thread_worker(p1, p2, results, game_info):
    result = play_game(p1, p2, game_info[1])
    results[2*game_info[0]] = result
    print(result)

    result = play_game(p2, p1, game_info[1])
    results[2*game_info[0] + 1] = result
    print(result)

def play_game(p1, p2, map):
    args = [f"./gradlew", "run", f"-PteamA={p1}", f"-PteamB={p2}", f"-Pmaps={map}"]
    proc = subprocess.run(args, capture_output=True, text=True)

    if (') wins (round ' in proc.stdout):
        winner = proc.stdout.split(') wins (round ')[0].split(' (')[-1]
        num_rounds = proc.stdout.split(') wins (round ')[-1].split(')')[0]
        winning_team = p1 if winner == 'A' else p2
        return f'{winning_team} ({winner}) won in {num_rounds} rounds on {map}'
    return 'No result'

if __name__ == "__main__":
    all_maps = ['colosseum', 'eckleburg', 'fortress', 'intersection', 'jellyfish', 'maptestsmall', 'nottestsmall',
                'progress', 'rivers', 'sandwich', 'squer', 'uncomfortable', 'underground', 'valley']
    parser = argparse.ArgumentParser()
    parser.add_argument("--n", type=int, help="number of threads to spawn", default=8)
    parser.add_argument("--p1", type=str, help="player 1")
    parser.add_argument("--p2", type=str, help="player 2")
    parser.add_argument("--maps", type=str, help="maps to play on", nargs='+', default=all_maps)

    args = parser.parse_args()

    results = [None] * len(args.maps) * 2

    with concurrent.futures.ThreadPoolExecutor(max_workers=6) as executor:
        executor.map(lambda m: thread_worker(args.p1, args.p2, results, m), list(enumerate(args.maps)))
    
    print(results)
    p1_total = sum(1 for r in results if r.split(' ')[0] == args.p1)
    p2_total = sum(1 for r in results if r.split(' ')[0] == args.p2)
    print(f"Overall: {args.p1} won {p1_total} times, {args.p2} won {p2_total} times")