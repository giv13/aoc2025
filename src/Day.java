import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Day {
    private final String name;
    private final String filePath;
    private boolean isFailRead = false;

    public Day(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    final void readFile() {
        if (isFailRead) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
            isFailRead = true;
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        }
    }

    abstract void processLine(String line);

    public final void solve() {
        readFile();
        long startTime = System.nanoTime();
        System.out.println("🎯 " + name + "\n" +
                "   ⭐ Part 1: " + part1() + "\n" +
                "   ⭐ Part 2: " + part2() + "\n" +
                "   📊 Benchmark: " + (double) ((System.nanoTime() - startTime) / 1000000) / 1000 + " мс");
    }

    abstract Object part1();

    abstract Object part2();
}
