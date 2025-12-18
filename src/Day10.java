import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    Object part1() {
        return machines.stream().mapToInt(Machine::getMinPressesForLightDiagram).sum();
    }

    @Override
    Object part2() {
        return machines.stream().mapToInt(Machine::getMinPressesForJoltageRequirements).sum();
    }

    private static class Machine {
        private final BitSet lightDiagram;
        private final BitSet[] buttonWiringSchematics;
        private final int[] joltageRequirements;

        private Machine(BitSet lightDiagram, BitSet[] buttonWiringSchematics, int[] joltageRequirements) {
            this.lightDiagram = lightDiagram;
            this.buttonWiringSchematics = buttonWiringSchematics;
            this.joltageRequirements = joltageRequirements;
        }

        private int getMinPressesForLightDiagram() {
            return getPossiblePressesStream(lightDiagram).mapToInt(presses -> presses.length).min().orElse(0);
        }

        private int getMinPressesForJoltageRequirements() {
            return getMinPressesForJoltageRequirements(joltageRequirements);
        }

        private int getMinPressesForJoltageRequirements(int[] joltageRequirements) {
            BitSet diagram = new BitSet();
            for (int i = 0; i < joltageRequirements.length; i++) {
                if (joltageRequirements[i] % 2 == 1) {
                    diagram.set(i);
                }
            }
            return getPossiblePressesStream(diagram).mapToInt(presses -> {
                int[] newJoltageRequirements = Arrays.copyOf(joltageRequirements, joltageRequirements.length);
                for (int i = 0; i < presses.length; i++) {
                    for (int j = 0; j < presses[i].length(); j++) {
                        if (presses[i].get(j)) {
                            newJoltageRequirements[j]--;
                        }
                    }
                }
                if (Arrays.stream(newJoltageRequirements).anyMatch(element -> element < 0)) {
                    return 10000;
                }
                if (Arrays.stream(newJoltageRequirements).allMatch(element -> element == 0)) {
                    return presses.length;
                }
                for (int i = 0; i < newJoltageRequirements.length; i++) {
                    newJoltageRequirements[i] /= 2;
                }
                return presses.length + 2 * getMinPressesForJoltageRequirements(newJoltageRequirements);
            }).min().orElse(10000);
        }

        private Stream<BitSet[]> getPossiblePressesStream(BitSet diagram) {
            return IntStream.range(0, 1 << buttonWiringSchematics.length)
                    .mapToObj(mask -> IntStream.range(0, buttonWiringSchematics.length)
                            .filter(i -> (mask & (1 << i)) != 0)
                            .mapToObj(i -> buttonWiringSchematics[i])
                            .toArray(BitSet[]::new)
                    )
                    .filter(presses -> Arrays.stream(presses)
                            .reduce((BitSet) diagram.clone(), (a, b) -> {
                                a.xor(b);
                                return a;
                            })
                            .isEmpty()
                    );
        }
    }
}
