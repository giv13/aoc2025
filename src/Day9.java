import java.util.*;

public class Day9 extends Day {
    private int maxRow = 0;
    private int maxCol = 0;
    private final List<Point> points = new ArrayList<>();
    private final Queue<Rect> rects = new PriorityQueue<>(Comparator.reverseOrder());

    public Day9(String filePath) {
        super("Day 9: Movie Theater", filePath);
    }

    @Override
    void processLine(String line) {
        int[] point = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
        maxRow = Math.max(maxRow, point[1]);
        maxCol = Math.max(maxCol, point[0]);
        points.add(new Point(point[0], point[1]));
    }

    @Override
    Long part1() {
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                rects.add(new Rect(points.get(i), points.get(j)));
            }
        }
        return rects.peek() != null ? rects.peek().area : 0L;
    }

    @Override
    Long part2() {
        RectChecker rectChecker = new RectChecker();
        while (!rects.isEmpty()) {
            Rect rect = rects.poll();
            if (rectChecker.check(rect)) {
                return rect.area;
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

    private static class Rect implements Comparable<Rect> {
        private final int fromX;
        private final int toX;
        private final int fromY;
        private final int toY;
        private final long area;

        private Rect(Point p1, Point p2) {
            this.fromX = Math.min(p1.x, p2.x);
            this.toX = Math.max(p1.x, p2.x);
            this.fromY = Math.min(p1.y, p2.y);
            this.toY = Math.max(p1.y, p2.y);
            this.area = (long) (toX - fromX + 1) * (toY - fromY + 1);
        }

        @Override
        public int compareTo(Rect other) {
            return Long.compare(area, other.area);
        }
    }

    private class RectChecker {
        private final Edge[] edges;
        private final Map<String, BitSet> cache = new HashMap<>();

        private RectChecker() {
            // For checks we need edges, not points
            int n = points.size();
            edges = new Edge[n];
            for (int i = 0; i < n; i++) {
                Point prev = points.get((i - 1 + n) % n);
                Point current = points.get(i);
                Point next = points.get((i + 1) % n);
                Point nextNext = points.get((i + 2) % n);
                edges[i] = new Edge(prev, current, next, nextNext);
            }
        }

        private boolean check(Rect rect) {
            // Use BitSet so we can check the entire side of the rectangle at once without loops - THIS IS MUCH FASTER!
            BitSet mask = new BitSet();
            mask.set(rect.fromX, rect.toX + 1);
            mask.and(buildLine(rect.fromY, true));
            mask.and(buildLine(rect.toY, true));
            if (mask.cardinality() != rect.toX - rect.fromX + 1) {
                return false;
            }

            mask = new BitSet();
            mask.set(rect.fromY, rect.toY + 1);
            mask.and(buildLine(rect.fromX, false));
            mask.and(buildLine(rect.toX, false));
            return mask.cardinality() == rect.toY - rect.fromY + 1;
        }

        // Build a line with which we need to compare the side of the rectangle
        private BitSet buildLine(int row, boolean isHorizontal) {
            // If the line is in the cache, return it immediately
            String key = (isHorizontal ? "H" : "V") + row;
            BitSet line = cache.get(key);
            if (line != null) {
                return line;
            }

            // First, we mark the edges of the original polygon on the line
            line = new BitSet();
            Set<Integer> zShapedEdges = new HashSet<>(); // Save the starting points of the longitudinal Z-edges for quick checking
            for (Edge edge : edges) {
                if (isHorizontal == edge.isHorizontal && edge.position == row) {
                    line.set(edge.from, edge.to + 1);
                    if (edge.isZShaped) {
                        zShapedEdges.add(edge.from);
                    }
                } else if (isHorizontal != edge.isHorizontal && edge.from < row && edge.to > row) {
                    line.set(edge.position);
                }
            }

            // Then we "paint" the tiles inside the polygon
            // The main idea: A tile is inside a polygon if there is an odd number of transverse edges + longitudinal Z-edges to the left of it
            int count = 0;
            int pos = 0;
            while (true) {
                int from = line.nextSetBit(pos);
                int to = line.nextClearBit(from);
                if (from == -1 || to == line.length()) break;

                if (to - from == 1 || zShapedEdges.contains(from)) {
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

            cache.put(key, line);
            return line;
        }

        private static class Edge {
            private final int position;
            private final int from;
            private final int to;
            private final boolean isHorizontal;
            private final boolean isZShaped; // Are adjacent edges pointing in different directions? If so, it's a Z-edge

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
