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
            // Строка не делится пополам - ID валидный
            if (idStr.length() % 2 != 0) {
                return true;
            }

            // Две части одинаковые - ID невалидный
            int halfPos = idStr.length() / 2;
            return !idStr.substring(0, halfPos).equals(idStr.substring(halfPos));
        });
    }

    @Override
    Object part2() {
        return getSumOfInvalidIds((idStr) -> {
            // Проверяем все возможные размеры повторяющихся подстрок
            for (int partSize = idStr.length() / 2; partSize >= 1; partSize--) {
                // Пропускаем размеры, на которые длина строки не делится
                if (idStr.length() % partSize != 0) {
                    continue;
                }

                // Проверяем, что все части одинаковые
                int partsCount = idStr.length() / partSize;
                boolean isAllPartsEqual = true;
                String firstPart = idStr.substring(0, partSize);
                for (int partIndex = 1; partIndex < partsCount; partIndex++) {
                    if (!firstPart.equals(idStr.substring(partSize * partIndex, partSize * (partIndex + 1)))) {
                        isAllPartsEqual = false;
                        break;
                    }
                }

                // Все части одинаковые - ID невалидный
                if (isAllPartsEqual) return false;
            }

            // Не нашли повторяющихся подстрок - ID валидный
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
