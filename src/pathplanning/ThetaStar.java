package pathplanning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThetaStar extends AStar {
    public ThetaStar() {
    }

    public ThetaStar(int sx, int sy, int gx, int gy, Grid grid) {
        super(sx, sy, gx, gy, grid);
    }

    public boolean lineOfSight(Node s, Node ss) {
        int x0 = s.x, y0 = s.y;
        int x1 = ss.x, y1 = ss.y;
        int dx = x1 - x0, dy = y1 - y0;
        int f = 0;
        int sx = 1, sy = 1;
        if (dx < 0) {
            dx = -dx;
            sx = -1;
        }

        if (dy < 0) {
            dy = -dy;
            sy = -1;
        }
        if (dx >= dy) {
            while (x0 != x1) {
                f += dy;
                if (f >= dx) {
                    if (grid.obstacle(x0 + (sx - 1) / 2, y0 + (sy - 1) / 2))
                        return false;
                    y0 += sy;
                    f -= dx;
                }
                if (f != 0 && grid.obstacle(x0 + (sx - 1) / 2, y0 + (sy - 1) / 2))
                    return false;
                if (dy == 0 && grid.obstacle(x0 + (sx - 1) / 2, y0) && grid.obstacle(x0 + (sx - 1) / 2, y0 - 1))
                    return false;
                x0 += sx;
            }
        } else {
            while (y0 != y1) {
                f += dx;
                if (f >= dy) {
                    if (grid.obstacle(x0 + (sx - 1) / 2, y0 + (sy - 1) / 2))
                        return false;
                    x0 += sx;
                    f -= dy;
                }
                if (f != 0 && grid.obstacle(x0 + (sx - 1) / 2, y0 + (sy - 1) / 2))
                    return false;
                if (dx == 0 && grid.obstacle(x0, y0 + (sy - 1) / 2) && grid.obstacle(x0 - 1, y0 + (sy - 1) / 2))
                    return false;
                y0 += sy;
            }
        }
        return true;
    }

    @Override
    public void init() {
        super.init();
        start.parent = start;
        start.f = start.h = heuristic(start, goal);
    }

    @Override
    public void updateVertex(Node s, Node ss) {
        boolean exist = true;
        if (!U.contains(ss)) {
            exist = false;
            ss.g = Double.POSITIVE_INFINITY;
            ss.parent = null;
        }
        if (lineOfSight(s.parent, ss)) {
            // PATH2
            if (s.parent.g + cost(s.parent, ss) < ss.g) {
                ss.parent = s.parent;
                ss.g = s.parent.g + cost(s.parent, ss);
                ss.h = heuristic(ss, goal);
                ss.f = ss.h + ss.g;
                if (!exist) U.add(ss);
            }

        } else {
            // PATH1
            if (s.g + cost(s, ss) < ss.g) {
                ss.parent = s;
                ss.g = s.g + cost(s, ss);
                ss.h = heuristic(ss, goal);
                ss.f = ss.g + ss.h;
                if (!exist) U.add(ss);
            }
        }
    }

    @Override
    public double cost(Node x, Node y) {
        return Util.euclidean(x, y);
    }

    @Override
    public double heuristic(Node x, Node y) {
        return Util.euclidean(x, y);
    }

    @Override
    public List<Node> backtrack() {
        Node cur = goal;
        List<Node> path = new ArrayList<>();
        while (cur != start) {
            path.add(cur);
            cur = cur.parent;
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
