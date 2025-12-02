public class Main {
    private static final int DAYS_COUNT = 2;

    public static void main(String[] args) throws Exception {
        Day day;
        for (int i = 1; i <= DAYS_COUNT; i++) {
            Class<?> clazz = Class.forName("Day" + i);
            day = (Day) clazz.getDeclaredConstructor(new Class[]{String.class}).newInstance("files/day" + i + ".txt");
            day.solve();
        }
    }
}
