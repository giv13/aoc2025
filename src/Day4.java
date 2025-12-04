import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day4 extends Day {
    public final List<int[]> grid = new ArrayList<>();

    public Day4(String filePath) {
        super("Day 4: Printing Department", filePath);
    }

    @Override
    void processLine(String line) {
        grid.add(Arrays.stream(line.split("")).mapToInt(l -> l.equals("@") ? 1 : 0).toArray());
    }

    @Override
    Integer part1() {
        return getAccessedRollsCount(false);
    }

    @Override
    Integer part2() {
        return IntStream.generate(() -> getAccessedRollsCount(true))
                .takeWhile(count -> count > 0)
                .sum();
    }

    private int getAccessedRollsCount(boolean withRemoval) {
        int count = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).length; j++) {
                if (grid.get(i)[j] == 1 && getAdjacentRollsCount(i, j) < 4) {
                    count++;
                    if (withRemoval) {
                        grid.get(i)[j] = 0;
                    }
                }
            }
        }
        return count;
    }

    private int getAdjacentRollsCount(int row, int col) {
        int count = 0;
        for (int j = col - 1; j <= col + 1; j++) {
            if (j >= 0 && j <= grid.get(row).length - 1) {
                if (row > 0 && grid.get(row - 1)[j] == 1) {
                    count++;
                }
                if (row < grid.size() - 1 && grid.get(row + 1)[j] == 1) {
                    count++;
                }
            }
        }
        if (col > 0 && grid.get(row)[col - 1] == 1) {
            count++;
        }
        if (col < grid.get(row).length - 1 && grid.get(row)[col + 1] == 1) {
            count++;
        }
        return count;
    }
}
