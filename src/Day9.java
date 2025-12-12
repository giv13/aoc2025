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
        SquareChecker squareChecker = new SquareChecker();
        while (!squares.isEmpty()) {
            Square square = squares.poll();
            if (squareChecker.check(square)) {
                return square.area;
            }
        }
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

    private class SquareChecker {
        private final Edge[] edges;

        private SquareChecker() {
            int n = list.size();
            edges = new Edge[n];
            for (int i = 0; i < n; i++) {
                Point prev = list.get((i - 1 + n) % n);
                Point current = list.get(i);
                Point next = list.get((i + 1) % n);
                Point nextNext = list.get((i + 2) % n);
                edges[i] = new Edge(prev, current, next, nextNext);
            }
        }

        private boolean check(Square square) {
            BitSet mask = new BitSet();
            mask.set(square.fromX, square.toX + 1);
            mask.and(buildLine(square.fromY, true));
            mask.and(buildLine(square.toY, true));
            if (mask.cardinality() != square.toX - square.fromX + 1) {
                return false;
            }

            mask = new BitSet();
            mask.set(square.fromY, square.toY + 1);
            mask.and(buildLine(square.fromX, false));
            mask.and(buildLine(square.toX, false));
            return mask.cardinality() == square.toY - square.fromY + 1;
        }

        private BitSet buildLine(int row, boolean isHorizontal) {
            BitSet line = new BitSet(isHorizontal ? maxRow : maxCol);
            BitSet zShapes = new BitSet(isHorizontal ? maxRow : maxCol);

            for (Edge edge : edges) {
                if (isHorizontal == edge.isHorizontal && edge.position == row) {
                    line.set(edge.from, edge.to + 1);
                    if (edge.isZShaped) {
                        zShapes.set(edge.from);
                    }
                } else if (isHorizontal != edge.isHorizontal && edge.from < row && edge.to > row) {
                    line.set(edge.position);
                }
            }

            int count = 0;
            int pos = 0;
            while (true) {
                int from = line.nextSetBit(pos);
                int to = line.nextClearBit(from);
                if (from == -1 || to == line.length()) break;

                if (to - from == 1 || zShapes.get(from)) {
                    count++;
                }
                if (count % 2 == 1) {
                    int setTo = line.nextSetBit(to);
                    line.set(to, setTo);
                    pos = setTo;
                    continue;
                }

                pos = to + 1;
            }

            return line;
        }

        private static class Edge {
            private final int position;
            private final int from;
            private final int to;
            private final boolean isHorizontal;
            private final boolean isZShaped;

            private Edge(Point prev, Point current, Point next, Point nextNext) {
                if (current.x == next.x && current.y != next.y) {
                    this.position = current.x;
                    this.from = Math.min(current.y, next.y);
                    this.to = Math.max(current.y, next.y);
                    this.isHorizontal = false;
                    this.isZShaped = prev.x > current.x != nextNext.x > current.x;
                } else if (current.y == next.y && current.x != next.x) {
                    this.position = current.y;
                    this.from = Math.min(current.x, next.x);
                    this.to = Math.max(current.x, next.x);
                    this.isHorizontal = true;
                    this.isZShaped = prev.y > current.y != nextNext.y > current.y;
                } else {
                    throw new IllegalArgumentException("Edge must be strictly vertical or horizontal.");
                }
            }
        }
    }
}
