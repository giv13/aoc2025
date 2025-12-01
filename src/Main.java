import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        Day1 day1 = new Day1("files/day1.txt");
        print("1/1", day1::getActualPasswordWithOldMethod);
        print("1/2", day1::getActualPasswordWithNewMethod);
    }

    private static void print(String day, Supplier<?> supplier) {
        long startTime = System.nanoTime();
        String result = String.valueOf(supplier.get());
        long endTime = System.nanoTime();
        System.out.println("Day " + day + ": " + result + " (" + (double) ((endTime - startTime) / 1000000) / 1000 + " мс)");
    }
}
