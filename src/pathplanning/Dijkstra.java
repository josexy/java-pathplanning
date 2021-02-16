package pathplanning;

public class Dijkstra extends AStar {

    private Dijkstra() {
    }

    public Dijkstra(int sx, int sy, int gx, int gy, Grid grid) {
        super(sx, sy, gx, gy, grid);
    }

    @Override
    public double heuristic(Node x, Node y) {
        return 0;
    }
}
