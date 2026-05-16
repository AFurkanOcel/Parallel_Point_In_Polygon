import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class BenchmarkRunner {
    private BenchmarkRunner() {
    }

    public static List<BenchmarkResult> run(BenchmarkConfig config, Polygon polygon)
            throws InterruptedException, ExecutionException {

        List<BenchmarkResult> results = new ArrayList<>();

        for (int pointCount : config.getPointSizes()) {
            List<Point> points = DataGenerator.generateRandomPoints(
                    pointCount,
                    config.getMinCoordinate(),
                    config.getMaxCoordinate(),
                    config.getRandomSeed() + pointCount
            );

            System.out.println("\nTesting with " + pointCount + " points");
            runWarmup(points, polygon, config);

            MeasuredCount sequentialMeasurement = measureSequential(points, polygon, config.getMeasurementRuns());
            System.out.printf(
                    "Sequential -> Inside: %d | Average Time: %.3f ms%n",
                    sequentialMeasurement.count,
                    sequentialMeasurement.averageTimeMs
            );

            for (int threadCount : config.getThreadCounts()) {
                MeasuredCount parallelMeasurement =
                        measureParallel(points, polygon, threadCount, config.getMeasurementRuns());

                if (parallelMeasurement.count != sequentialMeasurement.count) {
                    throw new IllegalStateException(
                            "Parallel result does not match sequential result for "
                                    + pointCount + " points and " + threadCount + " threads."
                    );
                }

                double speedup = sequentialMeasurement.averageTimeMs / parallelMeasurement.averageTimeMs;

                results.add(new BenchmarkResult(
                        pointCount,
                        threadCount,
                        sequentialMeasurement.averageTimeMs,
                        parallelMeasurement.averageTimeMs,
                        sequentialMeasurement.count,
                        speedup,
                        config.getMeasurementRuns()
                ));

                System.out.printf(
                        "%d Threads -> Inside: %d | Average Time: %.3f ms | Speedup: %.2f%n",
                        threadCount,
                        parallelMeasurement.count,
                        parallelMeasurement.averageTimeMs,
                        speedup
                );
            }
        }

        return results;
    }

    public static int runSequential(List<Point> points, Polygon polygon) {
        int insideCount = 0;

        for (Point point : points) {
            if (PointInPolygon.isInside(point, polygon)) {
                insideCount++;
            }
        }

        return insideCount;
    }

    public static int runParallel(List<Point> points, Polygon polygon, int threadCount)
            throws InterruptedException, ExecutionException {

        if (threadCount < 1) {
            throw new IllegalArgumentException("Thread count must be at least one.");
        }
        if (points.isEmpty()) {
            return 0;
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        try {
            int chunkSize = (int) Math.ceil(points.size() / (double) threadCount);

            for (int start = 0; start < points.size(); start += chunkSize) {
                int end = Math.min(start + chunkSize, points.size());
                futures.add(executor.submit(new WorkerTask(points.subList(start, end), polygon)));
            }

            int totalInside = 0;

            for (Future<Integer> future : futures) {
                totalInside += future.get();
            }

            return totalInside;
        } finally {
            executor.shutdown();
        }
    }

    private static void runWarmup(List<Point> points, Polygon polygon, BenchmarkConfig config)
            throws InterruptedException, ExecutionException {

        int warmupSize = Math.min(points.size(), 10000);
        List<Point> warmupPoints = points.subList(0, warmupSize);

        for (int i = 0; i < config.getWarmupIterations(); i++) {
            runSequential(warmupPoints, polygon);

            for (int threadCount : config.getThreadCounts()) {
                runParallel(warmupPoints, polygon, threadCount);
            }
        }
    }

    private static MeasuredCount measureSequential(List<Point> points, Polygon polygon, int runs) {
        double totalTimeMs = 0.0;
        int expectedCount = -1;

        for (int i = 0; i < runs; i++) {
            long start = Benchmark.start();
            int count = runSequential(points, polygon);
            totalTimeMs += Benchmark.stop(start);
            expectedCount = validateStableCount(expectedCount, count, "Sequential");
        }

        return new MeasuredCount(expectedCount, totalTimeMs / runs);
    }

    private static MeasuredCount measureParallel(List<Point> points, Polygon polygon, int threadCount, int runs)
            throws InterruptedException, ExecutionException {

        double totalTimeMs = 0.0;
        int expectedCount = -1;

        for (int i = 0; i < runs; i++) {
            long start = Benchmark.start();
            int count = runParallel(points, polygon, threadCount);
            totalTimeMs += Benchmark.stop(start);
            expectedCount = validateStableCount(expectedCount, count, "Parallel");
        }

        return new MeasuredCount(expectedCount, totalTimeMs / runs);
    }

    private static int validateStableCount(int expectedCount, int currentCount, String label) {
        if (expectedCount == -1) {
            return currentCount;
        }
        if (expectedCount != currentCount) {
            throw new IllegalStateException(label + " benchmark result changed between runs.");
        }

        return expectedCount;
    }

    private static final class MeasuredCount {
        private final int count;
        private final double averageTimeMs;

        private MeasuredCount(int count, double averageTimeMs) {
            this.count = count;
            this.averageTimeMs = averageTimeMs;
        }
    }
}
