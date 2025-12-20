import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day12 extends Day {
    private final List<Boolean> fits = new ArrayList<>();

    public Day12(String filePath) {
        super("Day 12: Christmas Tree Farm", filePath);
    }

    @Override
    void processLine(String line) {
        String[] parts = line.split(":");
        int regionArea = Arrays.stream(parts[0].split("x")).mapToInt(Integer::parseInt).reduce((a, b) -> a * b).orElse(0);
        int presentsTotalArea = Arrays.stream(parts[1].trim().split(" ")).mapToInt(Integer::parseInt).sum() * 9;
        fits.add(regionArea >= presentsTotalArea);
    }

    @Override
    Long part1() {
        return fits.stream().filter(fit -> fit).count();
    }

    @Override
    Object part2() {
        return null;
    }
}
