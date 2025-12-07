import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day3 extends Day {
    private final List<int[]> banks = new ArrayList<>();

    public Day3(String filePath) {
        super("Day 3: Lobby", filePath);
    }

    @Override
    void processLine(String line) {
        banks.add(Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray());
    }

    @Override
    Long part1() {
        return getTotalOutputJoltage(2);
    }

    @Override
    Long part2() {
        return getTotalOutputJoltage(12);
    }

    private long getTotalOutputJoltage(int digitsNumber) {
        return banks.stream().mapToLong(bank -> getMaxJoltage(bank, digitsNumber)).sum();
    }

    private long getMaxJoltage(int[] bank, int digitsNumber) {
        int maxPos = -1;
        long maxJoltage = 0L;

        while (digitsNumber >= 1) {
            int max = 0;
            for (int pos = ++maxPos; pos <= bank.length - digitsNumber; pos++) {
                if (bank[pos] > max) {
                    max = bank[pos];
                    maxPos = pos;
                }
                // There can't be a digit greater than 9, so we can stop here
                if (max == 9) {
                    break;
                }
            }
            maxJoltage += max * (long) Math.pow(10, --digitsNumber);
        }

        return maxJoltage;
    }
}
