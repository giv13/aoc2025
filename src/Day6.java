import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

public class Day6 extends Day {
    private final List<String> worksheet = new ArrayList<>();

    public Day6(String filePath) {
        super("Day 6: Trash Compactor", filePath);
    }

    @Override
    void processLine(String line) {
        worksheet.add(line);
    }

    @Override
    Long part1() {
        char[] operations = worksheet.getLast().replace(" ", "").toCharArray();
        long[] solves = new long[operations.length];
        for (int i = 0; i < worksheet.size() - 1; i++) {
            long[] numbers = Arrays.stream(worksheet.get(i).trim().split("\\s+")).mapToLong(Long::parseLong).toArray();
            for (int j = 0; j < numbers.length; j++) {
                solves[j] = i == 0 ? numbers[j] : (operations[j] == '*' ? solves[j] * numbers[j] : solves[j] + numbers[j]);
            }
        }
        return LongStream.of(solves).sum();
    }

    @Override
    Long part2() {
        long sum = 0;
        long solve = 0;
        boolean isMpy = false;
        List<char[]> problems = new ArrayList<>();
        int maxProblemLength = 0;
        StringBuilder sbNumber = new StringBuilder();

        for (String problem : worksheet) {
            problems.add(problem.toCharArray());
            maxProblemLength = Math.max(maxProblemLength, problem.length());
        }

        for (int i = 0; i < maxProblemLength; i++) {
            // If the last line contains an operation symbol, then the previous problem is finished
            if (i < problems.getLast().length && problems.getLast()[i] != ' ') {
                isMpy = problems.getLast()[i] == '*';
                sum += solve;
                solve = isMpy ? 1 : 0;
            }

            // Collecting the number
            sbNumber.setLength(0);
            for (int j = 0; j < problems.size() - 1; j++) {
                if (i < problems.get(j).length && problems.get(j)[i] != ' ') {
                    sbNumber.append(problems.get(j)[i]);
                }
            }
            if (!sbNumber.toString().isBlank()) {
                int num = Integer.parseInt(sbNumber.toString().trim());
                solve = isMpy ? solve * num : solve + num;
            }
        }

        // Adding the final problem's result to the total sum, since the loop missed it
        return sum + solve;
    }
}
