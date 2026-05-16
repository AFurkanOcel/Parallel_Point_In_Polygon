import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ValidationTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        validateConvexPolygon();
        validateConcavePolygon();
        validateBoundaryPolicy();
        validateParallelMatchesSequential();

        System.out.println("All validation tests passed.");
    }

    private static void validateConvexPolygon() {
        Polygon square = new Polygon(Arrays.asList(
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
        ));

        assertInside(new Point(2, 2), square, "Convex inside point should be inside.");
        assertOutside(new Point(5, 5), square, "Convex outside point should be outside.");
    }

    private static void validateConcavePolygon() {
        Polygon concave = new Polygon(Arrays.asList(
                new Point(0, 0),
                new Point(6, 0),
                new Point(6, 6),
                new Point(3, 3),
                new Point(0, 6)
        ));

        assertInside(new Point(1, 1), concave, "Concave inside point should be inside.");
        assertOutside(new Point(7, 3), concave, "Concave outside point should be outside.");
        assertOutside(new Point(3, 5), concave, "Point in the concave indentation should be outside.");
    }

    private static void validateBoundaryPolicy() {
        Polygon square = new Polygon(Arrays.asList(
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
        ));

        assertInside(new Point(2, 0), square, "Point on an edge should be treated as inside.");
        assertInside(new Point(0, 0), square, "Point on a vertex should be treated as inside.");
    }

    private static void validateParallelMatchesSequential() throws InterruptedException, ExecutionException {
        Polygon polygon = new Polygon(DataGenerator.generateCircularPolygon(50, 5.0));
        List<Point> points = DataGenerator.generateRandomPoints(10000, 0.0, 10.0, 2026L);

        int sequentialCount = BenchmarkRunner.runSequential(points, polygon);

        for (int threadCount : new int[]{1, 2, 4, 8}) {
            int parallelCount = BenchmarkRunner.runParallel(points, polygon, threadCount);

            if (parallelCount != sequentialCount) {
                throw new AssertionError(
                        "Parallel count does not match sequential count for "
                                + threadCount + " threads."
                );
            }
        }
    }

    private static void assertInside(Point point, Polygon polygon, String message) {
        if (!PointInPolygon.isInside(point, polygon)) {
            throw new AssertionError(message);
        }
    }

    private static void assertOutside(Point point, Polygon polygon, String message) {
        if (PointInPolygon.isInside(point, polygon)) {
            throw new AssertionError(message);
        }
    }
}
