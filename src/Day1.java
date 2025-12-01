import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;
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
        // Прибавляем 1, если диск после поворота будет в положении 0
        return getActualPassword((dial, rotation) -> (dial + rotation) % 100 == 0 ? 1 : 0);
    }

    public int getActualPasswordWithNewMethod() {
        // Вычисляем, сколько полных оборотов пройдет диск после поворота
        // К результату прибавляем 1, если диск после поворота будет в положении 0 или пройдет через 0
        return getActualPassword((dial, rotation) -> Math.abs((dial + rotation) / 100) + (dial == -rotation || dial * (dial + rotation) < 0 ? 1 : 0));
    }

    public int getActualPassword(IntBinaryOperator method) {
        int dial = 50;
        int actualPassword = 0;
        for (int rotation : rotations) {
            actualPassword += method.applyAsInt(dial, rotation);
            dial = (dial + rotation) % 100;
        }
        return actualPassword;
    }
}
