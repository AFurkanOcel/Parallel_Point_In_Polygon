public final class BenchmarkConfig {
    private final int[] pointSizes;
    private final int[] threadCounts;
    private final int polygonVertexCount;
    private final int warmupIterations;
    private final int measurementRuns;
    private final long randomSeed;
    private final double minCoordinate;
    private final double maxCoordinate;

    public BenchmarkConfig(
            int[] pointSizes,
            int[] threadCounts,
            int polygonVertexCount,
            int warmupIterations,
            int measurementRuns,
            long randomSeed,
            double minCoordinate,
            double maxCoordinate) {

        if (pointSizes == null || pointSizes.length == 0) {
            throw new IllegalArgumentException("Point sizes cannot be empty.");
        }
        if (threadCounts == null || threadCounts.length == 0) {
            throw new IllegalArgumentException("Thread counts cannot be empty.");
        }
        if (polygonVertexCount < 3) {
            throw new IllegalArgumentException("Polygon vertex count must be at least three.");
        }
        if (warmupIterations < 0) {
            throw new IllegalArgumentException("Warm-up iterations cannot be negative.");
        }
        if (measurementRuns < 1) {
            throw new IllegalArgumentException("Measurement runs must be at least one.");
        }
        if (minCoordinate > maxCoordinate) {
            throw new IllegalArgumentException("Minimum coordinate cannot be greater than maximum coordinate.");
        }

        this.pointSizes = pointSizes.clone();
        this.threadCounts = threadCounts.clone();
        this.polygonVertexCount = polygonVertexCount;
        this.warmupIterations = warmupIterations;
        this.measurementRuns = measurementRuns;
        this.randomSeed = randomSeed;
        this.minCoordinate = minCoordinate;
        this.maxCoordinate = maxCoordinate;
    }

    public static BenchmarkConfig defaultConfig() {
        return new BenchmarkConfig(
                new int[]{500000, 1000000, 2000000},
                new int[]{1, 2, 4, 8},
                200,
                1,
                3,
                42L,
                0.0,
                10.0
        );
    }

    public int[] getPointSizes() {
        return pointSizes.clone();
    }

    public int[] getThreadCounts() {
        return threadCounts.clone();
    }

    public int getPolygonVertexCount() {
        return polygonVertexCount;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public int getMeasurementRuns() {
        return measurementRuns;
    }

    public long getRandomSeed() {
        return randomSeed;
    }

    public double getMinCoordinate() {
        return minCoordinate;
    }

    public double getMaxCoordinate() {
        return maxCoordinate;
    }
}
