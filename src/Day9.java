import java.util.*;

public class Day9 extends Day {
    private int maxRow = 0;
    private int maxCol = 0;
    private final List<Point> list = new ArrayList<>();
    private final Queue<Square> squares = new PriorityQueue<>(Comparator.reverseOrder());

    public Day9(String filePath) {
        super("Day 9: Movie Theater", filePath);
    }

    @Override
    void processLine(String line) {
        int[] point = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
        maxRow = Math.max(maxRow, point[1]);
        maxCol = Math.max(maxCol, point[0]);
        list.add(new Point(point[0], point[1]));
    }

    @Override
    Long part1() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                squares.add(new Square(list.get(i), list.get(j)));
            }
        }
        return squares.peek() != null ? squares.peek().area : 0L;
    }

    @Override
    Long part2() {
        return 0L;
    }

    private static class Point {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Square implements Comparable<Square> {
        private final int fromX;
        private final int toX;
        private final int fromY;
        private final int toY;
        private final long area;

        private Square(Point p1, Point p2) {
            this.fromX = Math.min(p1.x, p2.x);
            this.toX = Math.max(p1.x, p2.x);
            this.fromY = Math.min(p1.y, p2.y);
            this.toY = Math.max(p1.y, p2.y);
            this.area = (long) (toX - fromX + 1) * (toY - fromY + 1);
        }

        @Override
        public int compareTo(Square other) {
            return Long.compare(area, other.area);
        }
    }
}
