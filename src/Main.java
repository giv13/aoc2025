import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
    }

    private static void print(String day, Supplier<?> supplier) {
        long startTime = System.nanoTime();
        String result = String.valueOf(supplier.get());
        long endTime = System.nanoTime();
        System.out.println("Day " + day + ": " + result + " (" + (double) ((endTime - startTime) / 1000000) / 1000 + " мс)");
    }
}
