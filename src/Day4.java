import java.util.ArrayList;
import java.util.List;

public class Day4 extends Day {
    private final List<char[]> grid = new ArrayList<>();
    private int[][] neighborCountMatrix;

    public Day4(String filePath) {
        super("Day 4: Printing Department", filePath);
    }

    @Override
    void processLine(String line) {
        grid.add(line.toCharArray());
    }

    @Override
    Integer part1() {
        int count = 0;
        neighborCountMatrix = new int[grid.size()][grid.getFirst().length];
        for (int i = 0; i < neighborCountMatrix.length; i++) {
            for (int j = 0; j < neighborCountMatrix[i].length; j++) {
                neighborCountMatrix[i][j] = grid.get(i)[j] == '@' ? countAdjacentRolls(i, j) : -1;
                if (neighborCountMatrix[i][j] >= 0 && neighborCountMatrix[i][j] < 4) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    Integer part2() {
        int count = 0;
        for (int i = 0; i < neighborCountMatrix.length; i++) {
            for (int j = 0; j < neighborCountMatrix[i].length; j++) {
                if (neighborCountMatrix[i][j] >= 0 && neighborCountMatrix[i][j] < 4) {
                    count += removeRoll(i, j);
                }
            }
        }
        return count;
    }

    private int countAdjacentRolls(int row, int col) {
        int count = -1;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < grid.size() && j >= 0 && j < grid.get(i).length && grid.get(i)[j] == '@') {
                    count++;
                }
            }
        }
        return count;
    }

    private int removeRoll(int row, int col) {
        int count = 1;
        neighborCountMatrix[row][col] = -1;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if ((i != 0 || j != 0) && i >= 0 && i < neighborCountMatrix.length && j >= 0 && j < neighborCountMatrix[i].length) {
                    if (neighborCountMatrix[i][j] > 0) neighborCountMatrix[i][j] -= 1;
                    if (neighborCountMatrix[i][j] >= 0 && neighborCountMatrix[i][j] < 4) {
                        count += removeRoll(i, j);
                    }
                }
            }
        }
        return count;
    }
}
