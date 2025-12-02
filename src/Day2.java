import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.LongStream;

public class Day2 extends Day {
    private final List<long[]> ranges = new ArrayList<>();

    public Day2(String filePath) {
        super(filePath);
    }

    @Override
    void processLine(String line) {
        for (String range : line.split(",")) {
            ranges.add(Arrays.stream(range.split("-")).mapToLong(Long::parseLong).toArray());
        }
    }

    @Override
    Long part1() {
        return getSumOfInvalidIds((idStr) -> {
            // Skip odd-length strings that can't have equal halves - ID is valid
            if (idStr.length() % 2 != 0) {
                return true;
            }

            // Two halves are equal - ID is invalid
            int halfPos = idStr.length() / 2;
            return !idStr.substring(0, halfPos).equals(idStr.substring(halfPos));
        });
    }

    @Override
    Object part2() {
        return getSumOfInvalidIds((idStr) -> {
            // Iterate through all possible sequence lengths
            for (int seqLength = idStr.length() / 2; seqLength >= 1; seqLength--) {
                // Skip lengths that don't evenly divide the string
                if (idStr.length() % seqLength != 0) {
                    continue;
                }

                // Check if all sequences are equal
                int seqCount = idStr.length() / seqLength;
                boolean isAllSeqEqual = true;
                String firstPart = idStr.substring(0, seqLength);
                for (int seqIndex = 1; seqIndex < seqCount; seqIndex++) {
                    if (!firstPart.equals(idStr.substring(seqLength * seqIndex, seqLength * (seqIndex + 1)))) {
                        isAllSeqEqual = false;
                        break;
                    }
                }

                // All sequences are equal - ID is invalid
                if (isAllSeqEqual) return false;
            }

            // No repeating sequences were found - ID is valid
            return true;
        });
    }

    private long getSumOfInvalidIds(Predicate<String> isValid) {
        return ranges.stream()
                .flatMapToLong(range -> LongStream.rangeClosed(range[0], range[1]))
                .filter(id -> !isValid.test(String.valueOf(id)))
                .sum();
    }
}
