import java.util.*;
import java.util.concurrent.Callable;

public class WorkerTask implements Callable<Integer>{

    private List<Point> points;
    private Polygon polygon;

    public WorkerTask(List<Point> points, Polygon polygon){
        this.points = points;
        this.polygon = polygon;
    }

    @Override
    public Integer call(){
        int count = 0;

        for(Point p: points){
            if(PointInPolygon.isInside(p, polygon))
                count++;
        }

        return count;
    }
}