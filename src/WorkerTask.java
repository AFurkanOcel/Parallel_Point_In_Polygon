import java.util.*;
import java.util.concurrent.Callable;

public class WorkerTask implements Callable<Integer> {

    private final List<Point> points;
    private final Polygon polygon;

    public WorkerTask(List<Point> points, Polygon polygon) {
        this.points = points;
        this.polygon = polygon;
    }

    @Override
    public Integer call() {
        int count = 0;

        for (Point point : points) {
            if (PointInPolygon.isInside(point, polygon)) {
                count++;
            }
        }

        return count;
    }
}
