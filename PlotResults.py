import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results.csv")

threads = df["Threads"]

fig = plt.figure(figsize=(10, 6), facecolor='black')
ax = fig.add_subplot(111)

ax.set_facecolor('black')

for column in df.columns[1:]:
    plt.plot(
        threads,
        df[column],
        marker='o',
        label=f"{column} points"
    )

plt.xlabel("Number of Threads", color="white")
plt.ylabel("Speedup", color="white")
plt.title("Parallel Speedup Analysis", color="white")

plt.xticks(threads, color="white")
plt.yticks(color="white")

plt.grid(True, color="gray", alpha=0.3)

for spine in ax.spines.values():
    spine.set_color("white")

legend = plt.legend()
legend.get_frame().set_facecolor("black")
legend.get_frame().set_edgecolor("white")
for text in legend.get_texts():
    text.set_color("white")

plt.savefig(
    "speedup_graph.png",
    facecolor='black',
    bbox_inches='tight'
)

plt.show()