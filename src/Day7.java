import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 extends Day {
    private final List<char[]> diagram = new ArrayList<>();
    private final int[] start = {0, -1};
    private Result result;

    public Day7(String filePath) {
        super("Day 7: Laboratories", filePath);
    }

    @Override
    void processLine(String line) {
        char[] l = line.toCharArray();
        if (start[1] == -1) {
            for (int i = 0; i < l.length; i++) {
                if (l[i] == 'S') {
                    start[1] = i;
                    break;
                }
            }
            if (start[1] == -1) {
                start[0]++;
            }
        }
        diagram.add(l);
    }

    @Override
    Integer part1() {
        return countSplitsAndTimelines().splits();
    }

    @Override
    Long part2() {
        return countSplitsAndTimelines().timelines();
    }

    private record Result(int splits, long timelines) {
    }

    private Result countSplitsAndTimelines() {
        if (result != null) {
            return result;
        }

        int splits = 0;
        Map<Integer, Long> beams = new HashMap<>();
        beams.put(start[1], 1L);

        for (int i = start[0] + 1; i < diagram.size(); i++) {
            for (Map.Entry<Integer, Long> beam : new HashMap<>(beams).entrySet()) {
                if (diagram.get(i)[beam.getKey()] == '^') {
                    for (Integer key : new Integer[]{beam.getKey() - 1, beam.getKey() + 1}) {
                        beams.compute(key, (k, v) -> (v == null ? 0L : v) + beam.getValue());
                    }
                    beams.remove(beam.getKey());
                    splits++;
                }
            }
        }

        result = new Result(splits, beams.values().stream().mapToLong(Long::longValue).sum());
        return result;
    }
}
