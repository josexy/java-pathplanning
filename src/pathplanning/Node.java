package pathplanning;

public class Node implements Comparable<Node> {
    public int x, y;
    public double g, h, f;
    public boolean obstacle, closed, mark;
    public Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        reset(true);
    }

    public void reset(boolean all) {
        this.g = this.h = this.f = Double.POSITIVE_INFINITY;
        this.closed = this.mark = false;
        parent = null;
        if (all) this.obstacle = false;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(f, o.f);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return x << 3 + y << 1;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
