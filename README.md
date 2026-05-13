# Parallel Point In Polygon Detection using Java

## Project Overview

This project solves the **Point In Polygon (PIP)** problem using **parallel programming** in Java.

Given:

- a **convex or concave polygon** defined by multiple `(x, y)` coordinates
- a large set of random test points

the program determines whether each point lies **inside** or **outside** the polygon.

The project compares:

- **Sequential execution**
- **Parallel execution using multiple threads**

and analyzes the **speedup performance**.

---

## Problem Definition

The Point-In-Polygon problem determines whether a point lies inside a polygon.

This project uses the **Ray Casting Algorithm**, which counts how many times a horizontal ray intersects polygon edges.

### Rule

- odd intersections → inside
- even intersections → outside

---

## Technologies Used

| Technology | Purpose |
|-----------|---------|
| Java | Main project implementation |
| ExecutorService | Parallel processing |
| Visual Studio Code | Development environment |
| Python | Graph generation |
| Matplotlib | Visualization |
| Pandas | CSV processing |
| GitHub | Version control |

---

# Project Structure

```text
ParallelPointInPolygon/
│── Main.java
│── Point.java
│── Polygon.java
│── PointInPolygon.java
│── WorkerTask.java
│── Benchmark.java
│── PlotResults.py
│── .gitignore
│── README.md
│
└── Results/
    │── Results.csv
    └── SpeedupGraph.png
```

---

## File Descriptions

| File | Description |
|------|-------------|
| `Main.java` | Controls program flow and benchmarking |
| `Point.java` | Stores x-y coordinates |
| `Polygon.java` | Polygon vertex container |
| `PointInPolygon.java` | Ray Casting implementation |
| `WorkerTask.java` | Parallel worker thread logic |
| `Benchmark.java` | Execution time measurement |
| `PlotResults.py` | Automatic graph generation |
| `Results.csv` | Benchmark outputs |
| `SpeedupGraph.png` | Generated performance graph |

---

# Algorithm Workflow

<img width="1024" height="1536" alt="workflow" src="https://github.com/user-attachments/assets/068f1b85-e013-492c-a5d1-5f5973db0840" />

---

# Polygon Generation

A circular polygon with **200 vertices** is generated for heavier computation.

### Formula

```text
x = r cos(θ) + r
y = r sin(θ) + r
```

where:

- `r = 5`
- vertex count = `200`

---

# Parallelization Strategy

The point set is divided into chunks:

```text
Total points
   ↓
split into N chunks
   ↓
assign each chunk to one thread
   ↓
merge partial results
```

Implemented using:

```java
ExecutorService executor =
    Executors.newFixedThreadPool(threadCount);
```

---

# Benchmark Configuration

## Test Sizes

- 500,000 points
- 1,000,000 points
- 2,000,000 points

## Thread Counts

- 1
- 2
- 4
- 8

---

# Speedup Formula

```text
Speedup = T_sequential / T_parallel
```

where:

- `T_sequential` = sequential execution time
- `T_parallel` = parallel execution time

---

# Benchmark Results

| Points | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
|--------|----------|-----------|-----------|-----------|
| 500k | 0.87 | 1.79 | 3.35 | 4.37 |
| 1M | 0.95 | 1.98 | 2.65 | 3.96 |
| 2M | 1.00 | 1.99 | 2.88 | 4.18 |

---

# Performance Graph

<img width="1042" height="675" alt="graph" src="https://github.com/user-attachments/assets/c248d064-b893-4494-b64c-0de2078b9c3c" />

Expected observations:

- speedup increases as thread count increases
- diminishing returns may appear after 4–8 threads because of thread overhead

---

# Sample Console Output

```text
(Testing with 1000000 points)
Sequential -> Inside: 785472 | Time: 400,021 ms
1 Thread -> Inside: 785472 | Time: 421,412 ms | Speedup: 0,95
2 Threads -> Inside: 785472 | Time: 202,540 ms | Speedup: 1,98
4 Threads -> Inside: 785472 | Time: 150,752 ms | Speedup: 2,65
8 Threads -> Inside: 785472 | Time: 100,954 ms | Speedup: 3,96
```

---

# How to Run

## Compile Java files

```bash
javac *.java
```

---

## Run the program

```bash
java Main
```

---

## Generated Outputs

After execution:

```text
Results/
 ├── Results.csv
 └── SpeedupGraph.png
```

---

# Key Features

- Supports both **convex** and **concave** polygons
- Uses **parallel programming** with multiple threads
- Automatically calculates **speedup**
- Automatically generates **CSV benchmark output**
- Automatically generates **performance graph**
- Clean and modular object-oriented design

---

# Conclusion

The project successfully demonstrates that:

- parallel programming significantly reduces execution time
- larger datasets benefit more from multithreading
- Java thread pools provide efficient task scheduling

### Best observed speedup

**4.37x**

using:

- 8 threads
- 500,000 points

---

# Future Improvements

Possible future enhancements:

- testing with larger datasets
- experimenting with different polygon shapes
- GPU-based implementation
- dynamic user input support

---

# Author

**A. Furkan ÖCEL**  
