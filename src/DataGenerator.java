import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class DataGenerator {
    private DataGenerator() {
    }

    public static List<Point> generateRandomPoints(int count, double min, double max, long seed) {
        if (count < 0) {
            throw new IllegalArgumentException("Point count cannot be negative.");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
        }

        Random random = new Random(seed);
        List<Point> points = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            double x = min + (max - min) * random.nextDouble();
            double y = min + (max - min) * random.nextDouble();
            points.add(new Point(x, y));
        }

        return points;
    }

    public static List<Point> generateCircularPolygon(int vertexCount, double radius) {
        if (vertexCount < 3) {
            throw new IllegalArgumentException("A polygon must have at least three vertices.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive.");
        }

        List<Point> vertices = new ArrayList<>(vertexCount);

        for (int i = 0; i < vertexCount; i++) {
            double angle = 2 * Math.PI * i / vertexCount;
            double x = radius * Math.cos(angle) + radius;
            double y = radius * Math.sin(angle) + radius;
            vertices.add(new Point(x, y));
        }

        return vertices;
    }
}
