public class PointInPolygon {

    public static boolean isInside(Point p, Polygon poly){
        int intersections = 0;
        int n = poly.vertices.size();

        for(int i=0;i<n;i++){
            Point a = poly.vertices.get(i);
            Point b = poly.vertices.get((i+1)%n);

            if(intersects(p,a,b))
                intersections++;
        }

        return intersections % 2 == 1;
    }

    private static boolean intersects(Point p, Point a, Point b){
        if((a.y > p.y) != (b.y > p.y)){
            double x =
                a.x + (p.y-a.y)*(b.x-a.x)/(b.y-a.y);

            return x > p.x;
        }
        return false;
    }
}