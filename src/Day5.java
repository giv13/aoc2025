import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 extends Day {
    private boolean isId = false;
    private final List<long[]> ranges = new ArrayList<>();
    private final List<Long> ids = new ArrayList<>();

    public Day5(String filePath) {
        super("Day 5: Cafeteria", filePath);
    }

    @Override
    void processLine(String line) {
        if (line.isBlank()) {
            isId = true;
            return;
        }
        if (isId) {
            ids.add(Long.valueOf(line));
        } else {
            ranges.add(Arrays.stream(line.split("-")).mapToLong(Long::parseLong).toArray());
        }
    }

    @Override
    Integer part1() {
        int count = 0;
        for (Long id : ids) {
            for (long[] range : ranges) {
                if (id >= range[0] && id <= range[1]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    @Override
    Long part2() {
        collapseRanges(ranges);
        return ranges.stream().mapToLong(range -> range[1] - range[0] + 1).sum();
    }

    private void collapseRanges(List<long[]> ranges) {
        for (int i = 0; i < ranges.size(); ) {
            long[] range1 = ranges.get(i);
            boolean noCollapses = true;
            for (int j = ranges.size() - 1; j > i; j--) {
                long[] range2 = ranges.get(j);
                if (range1[0] <= range2[1] && range1[1] >= range2[0]) {
                    range1[0] = Math.min(range1[0], range2[0]);
                    range1[1] = Math.max(range1[1], range2[1]);
                    ranges.remove(j);
                    noCollapses = false;
                }
            }
            if (noCollapses) {
                i++;
            }
        }
    }
}
