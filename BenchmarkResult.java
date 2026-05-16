public final class BenchmarkResult {
    private final int pointCount;
    private final int threadCount;
    private final double averageSequentialTimeMs;
    private final double averageParallelTimeMs;
    private final int insideCount;
    private final double speedup;
    private final int measurementRuns;

    public BenchmarkResult(
            int pointCount,
            int threadCount,
            double averageSequentialTimeMs,
            double averageParallelTimeMs,
            int insideCount,
            double speedup,
            int measurementRuns) {

        this.pointCount = pointCount;
        this.threadCount = threadCount;
        this.averageSequentialTimeMs = averageSequentialTimeMs;
        this.averageParallelTimeMs = averageParallelTimeMs;
        this.insideCount = insideCount;
        this.speedup = speedup;
        this.measurementRuns = measurementRuns;
    }

    public int getPointCount() {
        return pointCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public double getAverageSequentialTimeMs() {
        return averageSequentialTimeMs;
    }

    public double getAverageParallelTimeMs() {
        return averageParallelTimeMs;
    }

    public int getInsideCount() {
        return insideCount;
    }

    public double getSpeedup() {
        return speedup;
    }

    public int getMeasurementRuns() {
        return measurementRuns;
    }

    public String toCsvRow() {
        return pointCount
                + "," + threadCount
                + "," + averageSequentialTimeMs
                + "," + averageParallelTimeMs
                + "," + insideCount
                + "," + speedup
                + "," + measurementRuns;
    }
}
