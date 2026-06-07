public final class Benchmark {
    private Benchmark() {
    }

    public static long start() {
        return System.nanoTime();
    }

    public static double stop(long start) {
        return (System.nanoTime() - start) / 1e6;
    }
}
