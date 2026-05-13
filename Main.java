import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Main {

    // generates random test points between min and max values
    public static List<Point> generateRandomPoints(int count, double min, double max) {
        Random random = new Random();
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double x = min + (max - min) * random.nextDouble();
            double y = min + (max - min) * random.nextDouble();
            points.add(new Point(x, y));
        }
        return points;
    }

    // checks all points one by one (single-threaded)
    public static int runSequential(List<Point> points, Polygon polygon) {
        int insideCount = 0;

        for (Point p : points) {
            if (PointInPolygon.isInside(p, polygon)) {
                insideCount++;
            }
        }
        return insideCount;
    }

    // divides points among multiple threads
    public static int runParallel(List<Point> points, Polygon polygon, int threadCount)
            throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        int chunkSize = points.size() / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = (i == threadCount - 1) ? points.size() : start + chunkSize;

            List<Point> subList = points.subList(start, end);
            WorkerTask task = new WorkerTask(subList, polygon);
            futures.add(executor.submit(task));
        }

        int totalInside = 0;

        for (Future<Integer> future : futures) {
            totalInside += future.get();
        }

        executor.shutdown();
        return totalInside;
    }

    // creates a circular polygon with many vertices
    public static List<Point> generateCircularPolygon(int vertexCount, double radius) {
        List<Point> vertices = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            double angle = 2 * Math.PI * i / vertexCount;

            double x = radius * Math.cos(angle) + radius;
            double y = radius * Math.sin(angle) + radius;

            vertices.add(new Point(x, y));
        }

        return vertices;
    }

    public static void main(String[] args) {
        try {
            // create a polygon with 200 vertices for heavier computation
            List<Point> vertices = generateCircularPolygon(200, 5);
            Polygon polygon = new Polygon(vertices);

            int[] testSizes = {500000, 1000000, 2000000};
            int[] threadCounts = {1, 2, 4, 8};

            // store speedup values
            double[][] speedupResults =
                    new double[threadCounts.length][testSizes.length];

            // create csv file
            PrintWriter writer = new PrintWriter("Results/Results.csv");
            writer.println("Threads,500000,1000000,2000000");

            for (int j = 0; j < testSizes.length; j++) {
                int pointCount = testSizes[j];

                System.out.println("\n(Testing with " + pointCount + " points)");

                List<Point> points =
                        generateRandomPoints(pointCount, 0, 10);

                // measure sequential execution time
                long start = Benchmark.start();
                int sequentialInside =
                        runSequential(points, polygon);

                double sequentialTime =
                        Benchmark.stop(start);

                System.out.printf(
                        "Sequential -> Inside: %d | Time: %.3f ms%n",
                        sequentialInside,
                        sequentialTime
                );

                // test performance for different thread counts
                for (int i = 0; i < threadCounts.length; i++) {
                    int threads = threadCounts[i];

                    start = Benchmark.start();

                    int parallelInside =
                            runParallel(points, polygon, threads);

                    double parallelTime =
                            Benchmark.stop(start);

                    double speedup =
                            sequentialTime / parallelTime;

                    speedupResults[i][j] = speedup;

                    System.out.printf(
                            "%d Threads -> Inside: %d | Time: %.3f ms | Speedup: %.2f%n",
                            threads,
                            parallelInside,
                            parallelTime,
                            speedup
                    );
                }
            }

            // write csv data
            for (int i = 0; i < threadCounts.length; i++) {
                writer.print(threadCounts[i]);

                for (int j = 0; j < testSizes.length; j++) {
                    writer.print("," + speedupResults[i][j]);
                }

                writer.println();
            }

            writer.close();

            System.out.println("\nResults.csv created.");

            // automatically run python graph script
            ProcessBuilder pb =
                    new ProcessBuilder("python", "PlotResults.py");

            pb.inheritIO();
            pb.start().waitFor();

            System.out.println("SpeedupGraph.png created.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}