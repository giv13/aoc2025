import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Day10 extends Day {
    List<Machine> machines = new ArrayList<>();

    public Day10(String filePath) {
        super("Day 10: Factory", filePath);
    }

    @Override
    void processLine(String line) {
        String[] parts = Arrays.stream(line.split(" ")).map(part -> part.substring(1, part.length() - 1)).toArray(String[]::new);
        BitSet lightDiagram = IntStream.range(0, parts[0].length()).filter(i -> parts[0].charAt(i) == '#').collect(BitSet::new, BitSet::set, BitSet::or);
        BitSet[] buttonWiringSchematics = new BitSet[parts.length - 2];
        for (int i = 1; i < parts.length - 1; i++) {
            buttonWiringSchematics[i - 1] = (Arrays.stream(parts[i].split(",")).mapToInt(Integer::parseInt).collect(BitSet::new, BitSet::set, BitSet::or));
        }
        int[] joltageRequirements = Arrays.stream(parts[parts.length - 1].split(",")).mapToInt(Integer::parseInt).toArray();
        machines.add(new Machine(lightDiagram, buttonWiringSchematics, joltageRequirements));
    }

    @Override
    Integer part1() {
        return machines.stream().mapToInt(Machine::getMinPressesForLightDiagram).sum();
    }

    @Override
    Integer part2() {
        return machines.stream().mapToInt(Machine::getMinPressesForJoltageRequirements).sum();
    }

    private static class Machine {
        private final BitSet lightDiagram;
        private final BitSet[] buttonWiringSchematics;
        private final int[] joltageRequirements;
        private final Map<BitSet, List<List<BitSet>>> cache = new ConcurrentHashMap<>();

        private Machine(BitSet lightDiagram, BitSet[] buttonWiringSchematics, int[] joltageRequirements) {
            this.lightDiagram = lightDiagram;
            this.buttonWiringSchematics = buttonWiringSchematics;
            this.joltageRequirements = joltageRequirements;
        }

        private int getMinPressesForLightDiagram() {
            return getPossiblePresses(lightDiagram).stream().mapToInt(List::size).min().orElse(0);
        }

        private int getMinPressesForJoltageRequirements() {
            return Optional.ofNullable(getMinPressesForJoltageRequirements(joltageRequirements)).orElse(0);
        }

        private Integer getMinPressesForJoltageRequirements(int[] joltageRequirements) {
            BitSet diagram = new BitSet();
            boolean allZero = true;
            for (int i = 0; i < joltageRequirements.length; i++) {
                if (joltageRequirements[i] < 0) {
                    return null;
                }
                if (joltageRequirements[i] != 0) {
                    allZero = false;
                }
                if (joltageRequirements[i] % 2 == 1) {
                    diagram.set(i);
                }
            }
            if (allZero) {
                return 0;
            }

            return getPossiblePresses(diagram).parallelStream().map(presses -> {
                int[] newJoltageRequirements = Arrays.copyOf(joltageRequirements, joltageRequirements.length);
                for (BitSet press : presses) {
                    for (int i = press.nextSetBit(0); i >= 0; i = press.nextSetBit(i + 1)) {
                        newJoltageRequirements[i]--;
                    }
                }
                for (int i = 0; i < newJoltageRequirements.length; i++) {
                    newJoltageRequirements[i] /= 2;
                }
                Integer subResult = getMinPressesForJoltageRequirements(newJoltageRequirements);
                if (subResult == null) {
                    return null;
                }
                return presses.size() + 2 * subResult;
            }).filter(Objects::nonNull).min(Integer::compare).orElse(null);
        }

        private List<List<BitSet>> getPossiblePresses(BitSet diagram) {
            return cache.computeIfAbsent(diagram, d -> {
                List<List<BitSet>> possiblePresses = new ArrayList<>();
                int n = buttonWiringSchematics.length;

                for (int mask = 0; mask < (1 << n); mask++) {
                    List<BitSet> presses = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        if ((mask & (1 << i)) != 0) {
                            presses.add(buttonWiringSchematics[i]);
                        }
                    }

                    BitSet reduced = (BitSet) d.clone();
                    for (BitSet press : presses) {
                        reduced.xor(press);
                    }
                    if (reduced.isEmpty()) {
                        possiblePresses.add(presses);
                    }
                }

                return possiblePresses;
            });
        }
    }
}
