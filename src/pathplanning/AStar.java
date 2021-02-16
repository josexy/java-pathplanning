package pathplanning;

import java.util.*;

public class AStar {

    protected PriorityQueue<Node> U;

    protected Grid grid;
    protected Node start, goal;

    public AStar() {
        U = new PriorityQueue<>();
    }

    public AStar(int sx, int sy, int gx, int gy, Grid grid) {
        U = new PriorityQueue<>();
        this.grid = grid;
        start = this.grid.cell(sx, sy);
        goal = this.grid.cell(gx, gy);
    }

    public Node getStart() {
        return start;
    }

    public Node getGoal() {
        return goal;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    public double heuristic(Node x, Node y) {
        return 10 * Util.manhattan(x, y);
    }

    public double cost(Node x, Node y) {
        return (x.x - y.x == 0 || x.y - y.y == 0) ? 10 : 14;
    }

    public List<Node> getPath() {
        return backtrack();
    }

    public void reset(boolean all) {
        if (grid.map == null) return;
        for (var list : grid.map) {
            for (var node : list) {
                node.reset(all);
            }
        }
    }

    public void init() {
        U.clear();
        start.g = 0;
        goal.h = 0;
    }

    public List<Node> run() {
        init();

        U.add(start);
        while (!U.isEmpty()) {
            Node cur = U.poll();
            if (cur.closed) continue;
            cur.closed = true;

            if (cur.x == goal.x && cur.y == goal.y) {
                return getPath();
            }

            updateSuccessors(cur);
        }
        return null;
    }

    public void updateSuccessors(Node cur) {
        var succ = grid.getSucc(cur);
        for (Node s : succ) {
            if (s.closed) continue;
            updateVertex(cur, s);
        }
    }

    public void updateVertex(Node cur, Node s) {
        double c = cost(cur, s);

        if (!U.contains(s)) {
            s.g = cur.g + c;
            s.h = heuristic(s, goal);
            s.f = s.g + s.h;
            s.parent = cur;
            U.add(s);
        } else if (cur.g + c < s.g) {
            s.g = cur.g + c;
            s.f = s.g + s.h;
            s.parent = cur;
        }
    }

    public List<Node> backtrack() {
        Node cur = goal;
        List<Node> path = new ArrayList<>();
        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
