import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day3 extends Day {
    private final List<int[]> banks = new ArrayList<>();

    public Day3(String filePath) {
        super(filePath);
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
        return Long.parseLong(getMaxJoltage(bank, 0, digitsNumber));
    }

    private String getMaxJoltage(int[] bank, int pos, int digitsNumber) {
        int max = 0;
        int maxPos = 0;
        for (; pos <= bank.length - digitsNumber; pos++) {
            if (bank[pos] > max) {
                max = bank[pos];
                maxPos = pos;
            }
            // There can't be a digit greater than 9, so we can stop here.
            if (max == 9) {
                break;
            }
        }
        return max + (digitsNumber == 1 ? "" : getMaxJoltage(bank, maxPos + 1, digitsNumber - 1));
    }
}
