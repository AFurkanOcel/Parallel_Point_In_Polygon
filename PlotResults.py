import pandas as pd
import matplotlib.pyplot as plt


RESULTS_CSV = "Results/Results.csv"
OUTPUT_IMAGE = "Results/SpeedupGraph.png"


def main():
    df = pd.read_csv(RESULTS_CSV)

    fig = plt.figure(figsize=(10, 6), facecolor="black")
    ax = fig.add_subplot(111)
    ax.set_facecolor("black")

    for point_count, group in df.groupby("PointCount"):
        group = group.sort_values("ThreadCount")
        ax.plot(
            group["ThreadCount"],
            group["Speedup"],
            marker="o",
            label=f"{point_count} points"
        )

    ax.set_xlabel("Number of Threads", color="white")
    ax.set_ylabel("Speedup", color="white")
    ax.set_title("Parallel Speedup Analysis", color="white")

    ax.set_xticks(sorted(df["ThreadCount"].unique()))
    ax.tick_params(axis="x", colors="white")
    ax.tick_params(axis="y", colors="white")
    ax.grid(True, color="gray", alpha=0.3)

    for spine in ax.spines.values():
        spine.set_color("white")

    legend = ax.legend()
    legend.get_frame().set_facecolor("black")
    legend.get_frame().set_edgecolor("white")

    for text in legend.get_texts():
        text.set_color("white")

    plt.savefig(OUTPUT_IMAGE, facecolor="black", bbox_inches="tight")
    plt.close(fig)


if __name__ == "__main__":
    main()
