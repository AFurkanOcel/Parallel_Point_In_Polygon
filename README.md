<h1 align="center">Parallel Point In Polygon Detection</h1>

<p align="center">
Parallel point-in-polygon detection using Java multithreading, performance benchmarking, and automatic speedup visualization.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Language-Java-orange"/>
  <img src="https://img.shields.io/badge/Parallelism-ExecutorService-blue"/>
  <img src="https://img.shields.io/badge/Algorithm-Ray%20Casting-green"/>
  <img src="https://img.shields.io/badge/Visualization-Python%20%2B%20Matplotlib-purple"/>
  <img src="https://img.shields.io/badge/Architecture-Object%20Oriented-red"/>
  <img src="https://img.shields.io/badge/Status-Completed-brightgreen"/>
</p>

---

## Project Overview

This project solves the **Point In Polygon (PIP)** problem with a Java-based
parallel programming approach.

Given:

- a polygon represented by ordered `(x, y)` vertices
- a large list of test points

the program determines how many points are inside the polygon. It compares a
single-threaded sequential solution with a parallel solution implemented with
`ExecutorService`, `Callable`, and `Future`.

The implementation supports both convex and concave polygons. Points located on
a polygon edge or vertex are treated as inside.

---

## Problem Definition

The Point In Polygon problem determines whether a point lies inside, outside, or
on the boundary of a polygon.

This project uses the **Ray Casting Algorithm**. A horizontal ray is extended
from the test point to the right side of the plane, and polygon edge
intersections are counted.

### Rule

- Odd number of intersections: inside
- Even number of intersections: outside
- Point on an edge or vertex: inside

<img width="701" height="298" alt="ray casting" src="assets/screenshots/ray-casting.png" />

---

## Technologies Used

| Technology | Purpose |
|-----------|---------|
| Java | Main implementation |
| ExecutorService | Parallel task execution |
| Callable and Future | Worker result collection |
| Python | Graph generation |
| Pandas | CSV processing |
| Matplotlib | Speedup visualization |

---

## Project Report

The final Turkish project report is available here:

[report/project_report_Turkish.pdf](report/project_report_Turkish.pdf)

The report explains the point-in-polygon problem, the Ray Casting algorithm,
the Java parallelization strategy, benchmark methodology, speedup results,
validation cases, and final conclusions in Turkish.

---

## Project Structure

```text
ParallelPointInPolygon/
|-- README.md
|-- LICENSE
|-- assets/
|   `-- screenshots/
|       |-- algorithm-workflow.png
|       |-- performance-graph.png
|       `-- ray-casting.png
|-- report/
|   `-- project_report_Turkish.pdf
|-- scripts/
|   `-- PlotResults.py
|-- src/
|   |-- Main.java
|   |-- Point.java
|   |-- Polygon.java
|   |-- PointInPolygon.java
|   |-- WorkerTask.java
|   |-- Benchmark.java
|   |-- BenchmarkConfig.java
|   |-- BenchmarkResult.java
|   |-- BenchmarkRunner.java
|   |-- CsvResultWriter.java
|   |-- DataGenerator.java
|   `-- ValidationTest.java
`-- results/
    |-- .gitkeep
    |-- Results.csv
    `-- SpeedupGraph.png
```

---

## Main Components

| File | Responsibility |
|------|----------------|
| `src/Main.java` | Starts the benchmark workflow and graph generation |
| `src/Point.java` | Immutable point model |
| `src/Polygon.java` | Immutable polygon vertex container |
| `src/PointInPolygon.java` | Ray Casting implementation with boundary handling |
| `src/WorkerTask.java` | Parallel worker task for a point chunk |
| `src/BenchmarkRunner.java` | Sequential and parallel execution logic |
| `src/BenchmarkConfig.java` | Benchmark parameters |
| `src/BenchmarkResult.java` | Benchmark result model |
| `src/CsvResultWriter.java` | CSV output writer |
| `src/DataGenerator.java` | Random point and polygon generation |
| `src/ValidationTest.java` | Framework-free correctness checks |
| `scripts/PlotResults.py` | Speedup graph generator |
| `report/project_report_Turkish.pdf` | Turkish project report |

---

## Algorithm Workflow

<img width="1024" height="1466" alt="workflow" src="assets/screenshots/algorithm-workflow.png" />

---

## Polygon Generation

The benchmark uses a circular convex polygon with 200 vertices to create a
heavier computational workload.

```text
x = r * cos(theta) + r
y = r * sin(theta) + r
```

where:

- `r = 5`
- `vertex count = 200`

Concave polygon behavior is verified separately in `src/ValidationTest.java`.

---

## Parallelization Strategy

The project uses data parallelism. The point list is divided into chunks, and
each worker checks one chunk independently.

```text
All points
   |
   v
Split into chunks by thread count
   |
   v
Process each chunk in a worker task
   |
   v
Merge partial inside counts
```

The worker tasks do not share mutable state. Each task returns only its local
inside count, and the main benchmark runner sums the partial results.

```java
ExecutorService executor = Executors.newFixedThreadPool(threadCount);
```

The executor is shut down after each parallel run.

---

## Benchmark Methodology

Benchmark configuration:

- Point sizes: `500000`, `1000000`, `2000000`
- Thread counts: `1`, `2`, `4`, `8`
- Polygon vertices: `200`
- Warm-up iterations: `1`
- Measurement runs per configuration: `3`
- Random seed: fixed for reproducible input data

The benchmark measures:

- average sequential execution time
- average parallel execution time
- inside point count
- speedup

Speedup is calculated as:

```text
Speedup = Average Sequential Time / Average Parallel Time
```

The sequential and parallel inside counts are checked for equality. If a
parallel result does not match the sequential result, the benchmark fails.

---

## Benchmark Results

The following values were generated from `results/Results.csv`.

| Points | Threads | Sequential Time (ms) | Parallel Time (ms) | Inside Count | Speedup |
|--------|---------|----------------------|--------------------|--------------|---------|
| 500000 | 1 | 417.349 | 429.766 | 392433 | 0.97 |
| 500000 | 2 | 417.349 | 211.950 | 392433 | 1.97 |
| 500000 | 4 | 417.349 | 154.925 | 392433 | 2.69 |
| 500000 | 8 | 417.349 | 102.811 | 392433 | 4.06 |
| 1000000 | 1 | 956.429 | 895.037 | 784968 | 1.07 |
| 1000000 | 2 | 956.429 | 413.202 | 784968 | 2.31 |
| 1000000 | 4 | 956.429 | 292.380 | 784968 | 3.27 |
| 1000000 | 8 | 956.429 | 190.277 | 784968 | 5.03 |
| 2000000 | 1 | 1741.211 | 1718.575 | 1570954 | 1.01 |
| 2000000 | 2 | 1741.211 | 824.813 | 1570954 | 2.11 |
| 2000000 | 4 | 1741.211 | 517.246 | 1570954 | 3.37 |
| 2000000 | 8 | 1741.211 | 410.366 | 1570954 | 4.24 |

---

## Performance Graph

<img width="1050" height="669" alt="graph" src="assets/screenshots/performance-graph.png" />

Expected observations:

- Parallel execution improves performance as thread count increases.
- A one-thread parallel run may be slower than the direct sequential version
  because it includes executor and task management overhead.
- Speedup does not grow perfectly linearly because of scheduling overhead,
  worker creation cost, CPU limits, and memory access effects.

---

## Validation Cases

`src/ValidationTest.java` checks the correctness of the algorithm without requiring
JUnit or any external Java testing framework.

Covered cases:

- Convex polygon inside point
- Convex polygon outside point
- Concave polygon inside point
- Concave polygon outside point
- Point in a concave indentation
- Point on an edge
- Point on a vertex
- Sequential and parallel result equality

---

## How to Run

Compile all Java files:

```bash
javac -d build src/*.java
```

Run validation tests:

```bash
java -cp build ValidationTest
```

Run the benchmark:

```bash
java -cp build Main
```

Generate the graph manually if needed:

```bash
python scripts/PlotResults.py
```

After execution, the generated outputs are:

```text
results/
|-- Results.csv
`-- SpeedupGraph.png
```

---

## Sample Console Output

```text
Testing with 1000000 points
Sequential -> Inside: 784968 | Average Time: 956.429 ms
1 Threads -> Inside: 784968 | Average Time: 895.037 ms | Speedup: 1.07
2 Threads -> Inside: 784968 | Average Time: 413.202 ms | Speedup: 2.31
4 Threads -> Inside: 784968 | Average Time: 292.380 ms | Speedup: 3.27
8 Threads -> Inside: 784968 | Average Time: 190.277 ms | Speedup: 5.03
```

---

## Limitations and Future Work

- The benchmark uses a fixed generated polygon for the main performance test.
- More polygon shapes and vertex counts can be tested for deeper analysis.
- JVM microbenchmarking could be improved further with a dedicated framework
  such as JMH, but that is intentionally avoided to keep the project simple.
- GPU-based or spatial-index-based approaches could be explored as future work.

---

## Author

**A. Furkan ÖCEL**
