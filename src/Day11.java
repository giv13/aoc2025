import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {
    private final Map<String, String[]> connections = new HashMap<>();

    public Day11(String filePath) {
        super("Day 11: Reactor", filePath);
    }

    @Override
    void processLine(String line) {
        String[] parts = line.split(":");
        connections.put(parts[0], parts[1].trim().split(" "));
    }

    @Override
    Long part1() {
        return countPaths("you", List.of("out"));
    }

    @Override
    Long part2() {
        return countPaths("svr", List.of("fft", "dac", "out")) + countPaths("svr", List.of("dac", "fft", "out"));
    }

    private long countPaths(String from, List<String> points) {
        return countPaths(from, points, 0, new HashMap<>());
    }

    private long countPaths(String from, List<String> points, int cur, Map<String, Long> cache) {
        String key = from + ":" + points.get(cur);
        Long cachedCount = cache.get(key);
        if (cachedCount != null) {
            return cachedCount;
        }

        if (from.equals(points.get(cur))) {
            return ++cur == points.size() ? 1 : countPaths(from, points, cur, cache);
        }

        long count = 0L;
        String[] outputs = connections.get(from);
        if (outputs != null) {
            for (String output : outputs) {
                count += countPaths(output, points, cur, cache);
            }
        }

        cache.put(key, count);
        return count;
    }
}
