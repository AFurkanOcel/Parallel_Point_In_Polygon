public final class PointInPolygon {
    private static final double EPSILON = 1e-10;

    private PointInPolygon() {
    }

    public static boolean isInside(Point point, Polygon polygon) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }
        if (polygon == null) {
            throw new IllegalArgumentException("Polygon cannot be null.");
        }

        boolean inside = false;
        int vertexCount = polygon.size();

        for (int i = 0; i < vertexCount; i++) {
            Point a = polygon.getVertices().get(i);
            Point b = polygon.getVertices().get((i + 1) % vertexCount);

            if (isPointOnSegment(point, a, b)) {
                return true;
            }

            if (rayIntersectsSegment(point, a, b)) {
                inside = !inside;
            }
        }

        return inside;
    }

    private static boolean rayIntersectsSegment(Point point, Point a, Point b) {
        double py = point.getY();
        double ay = a.getY();
        double by = b.getY();

        if ((ay > py) != (by > py)) {
            double intersectionX =
                    a.getX() + (py - ay) * (b.getX() - a.getX()) / (by - ay);
            return intersectionX > point.getX() + EPSILON;
        }

        return false;
    }

    private static boolean isPointOnSegment(Point point, Point a, Point b) {
        double cross =
                (point.getY() - a.getY()) * (b.getX() - a.getX())
                        - (point.getX() - a.getX()) * (b.getY() - a.getY());

        if (Math.abs(cross) > EPSILON) {
            return false;
        }

        double minX = Math.min(a.getX(), b.getX()) - EPSILON;
        double maxX = Math.max(a.getX(), b.getX()) + EPSILON;
        double minY = Math.min(a.getY(), b.getY()) - EPSILON;
        double maxY = Math.max(a.getY(), b.getY()) + EPSILON;

        return point.getX() >= minX
                && point.getX() <= maxX
                && point.getY() >= minY
                && point.getY() <= maxY;
    }
}
