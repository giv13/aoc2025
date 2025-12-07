import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        long[] beams = new long[diagram.getFirst().length];
        beams[start[1]] = 1L;

        for (int i = start[0] + 1; i < diagram.size(); i++) {
            for (int j = 1; j < beams.length - 1; j++) {
                if (beams[j] > 0 && diagram.get(i)[j] == '^') {
                    beams[j - 1] += beams[j];
                    beams[j + 1] += beams[j];
                    beams[j] = 0L;
                    splits++;
                }
            }
        }

        result = new Result(splits, Arrays.stream(beams).sum());
        return result;
    }
}
