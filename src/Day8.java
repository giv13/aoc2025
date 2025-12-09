import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day8 extends Day {
    private final List<junctionBox> junctionBoxes = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private Result result;

    public Day8(String filePath) {
        super("Day 8: Playground", filePath);
    }

    @Override
    void processLine(String line) {
        int[] axes = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
        junctionBoxes.add(new junctionBox(axes[0], axes[1], axes[2]));
    }

    @Override
    Integer part1() {
        return kruskal().product1();
    }

    @Override
    Integer part2() {
        return kruskal().product2();
    }

    private record junctionBox(int x, int y, int z) {
    }

    private record Connection(junctionBox box1, junctionBox box2, double distance) implements Comparable<Connection> {
        @Override
        public int compareTo(Connection other) {
            return Double.compare(distance, other.distance);
        }
    }

    private record Result(int product1, int product2) {
    }

    private Result kruskal() {
        if (result != null) {
            return result;
        }

        calculateDistances();

        int product1 = 0;
        int product2 = 0;
        int k = 1000;
        Map<junctionBox, Integer> circuits = new HashMap<>();
        for (int i = 0; i < junctionBoxes.size(); i++) {
            circuits.put(junctionBoxes.get(i), i);
        }

        for (Connection connection : connections) {
            Integer cid1 = circuits.get(connection.box1());
            Integer cid2 = circuits.get(connection.box2());

            if (!cid1.equals(cid2)) {
                for (Map.Entry<junctionBox, Integer> circuit : circuits.entrySet()) {
                    if (circuit.getValue().equals(cid2)) {
                        circuit.setValue(cid1);
                    }
                }
            }

            if (--k == 1) {
                product1 = circuits
                        .values()
                        .stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .values()
                        .stream()
                        .sorted(Comparator.reverseOrder())
                        .limit(3)
                        .mapToInt(i -> (int) i.longValue())
                        .reduce((a, b) -> a * b)
                        .orElse(0);
            }

            if (circuits.values().stream().distinct().count() == 1) {
                product2 = connection.box1().x() * connection.box2().x();
                break;
            }
        }

        result = new Result(product1, product2);
        return result;
    }

    private void calculateDistances() {
        for (int i = 0; i < junctionBoxes.size(); i++) {
            for (int j = i + 1; j < junctionBoxes.size(); j++) {
                long x = Math.abs(junctionBoxes.get(i).x() - junctionBoxes.get(j).x());
                long y = Math.abs(junctionBoxes.get(i).y() - junctionBoxes.get(j).y());
                long z = Math.abs(junctionBoxes.get(i).z() - junctionBoxes.get(j).z());
                double distance = Math.sqrt(x * x + y * y + z * z);
                connections.add(new Connection(junctionBoxes.get(i), junctionBoxes.get(j), distance));
            }
        }
        Collections.sort(connections);
    }
}
