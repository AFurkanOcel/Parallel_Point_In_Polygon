import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public final class CsvResultWriter {
    private CsvResultWriter() {
    }

    public static void write(String path, List<BenchmarkResult> results) throws FileNotFoundException {
        File outputFile = new File(path);
        File parentDirectory = outputFile.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("PointCount,ThreadCount,SequentialTimeMs,ParallelTimeMs,InsideCount,Speedup,MeasurementRuns");

            for (BenchmarkResult result : results) {
                writer.println(result.toCsvRow());
            }
        }
    }
}
