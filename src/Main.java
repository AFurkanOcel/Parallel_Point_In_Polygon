import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    private static final String RESULTS_CSV_PATH = "results/Results.csv";

    public static void main(String[] args) {
        BenchmarkConfig config = BenchmarkConfig.defaultConfig();
        Polygon polygon = new Polygon(
                DataGenerator.generateCircularPolygon(config.getPolygonVertexCount(), 5.0)
        );

        try {
            List<BenchmarkResult> results = BenchmarkRunner.run(config, polygon);
            CsvResultWriter.write(RESULTS_CSV_PATH, results);

            System.out.println("\nresults/Results.csv created.");
            runPlotScript();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Benchmark execution was interrupted.");
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void runPlotScript() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "scripts/PlotResults.py");
        processBuilder.inheritIO();

        int exitCode = processBuilder.start().waitFor();

        if (exitCode == 0) {
            System.out.println("results/SpeedupGraph.png created.");
        } else {
            System.err.println("PlotResults.py finished with exit code " + exitCode + ".");
        }
    }
}
