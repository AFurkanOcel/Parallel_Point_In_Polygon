import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Polygon {
    private final List<Point> vertices;

    public Polygon(List<Point> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Polygon vertices cannot be null.");
        }
        if (vertices.size() < 3) {
            throw new IllegalArgumentException("A polygon must have at least three vertices.");
        }
        if (vertices.contains(null)) {
            throw new IllegalArgumentException("Polygon vertices cannot contain null points.");
        }

        this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public int size() {
        return vertices.size();
    }
}
