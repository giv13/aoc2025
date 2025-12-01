import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 extends Day {
    private final List<Integer> rotations = new ArrayList<>();

    public Day1(String filePath) {
        super(filePath);
        readFile();
    }

    @Override
    protected void processLine(String line) {
        String regex = "^([LR])(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int rotation = Integer.parseInt(matcher.group(2));
            if ("L".equals(matcher.group(1))) {
                rotation = -rotation;
            }
            rotations.add(rotation);
        }
    }

    public int getActualPasswordWithOldMethod() {
        int dial = 50;
        int actualPassword = 0;
        for (Integer rotation : rotations) {
            if ((dial + rotation) % 100 == 0) {
                actualPassword++;
            }
            dial = (dial + rotation) % 100;
        }
        return actualPassword;
    }

    public int getActualPasswordWithNewMethod() {
        int dial = 50;
        int actualPassword = 0;
        for (Integer rotation : rotations) {
            actualPassword += Math.abs((dial + rotation) / 100);
            if (dial == -rotation || dial * (dial + rotation) < 0) {
                actualPassword++;
            }
            dial = (dial + rotation) % 100;
        }
        return actualPassword;
    }
}
