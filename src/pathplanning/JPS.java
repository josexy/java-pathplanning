package pathplanning;

import java.util.ArrayList;
import java.util.List;

public class JPS extends AStar {

    public JPS() {
    }

    public JPS(int sx, int sy, int gx, int gy, Grid grid) {
        super(sx, sy, gx, gy, grid);
    }

    public void identifySuccessors(Node cur) {
        var succ = findNeighbor(cur);
        for (Node s : succ) {
            Node jumpNode = jump(s, cur);
            if (jumpNode == null || jumpNode.closed) continue;

            updateVertex(cur, jumpNode);
        }
    }

    public Node jump(Node succ, Node cur) {
        if (succ == null) return null;
        int x = succ.x, y = succ.y;
        int px = cur.x, py = cur.y;

        int dx = x - px;
        int dy = y - py;

        // do not walk, reach the edge or encounter obstacles
        if (!grid.canPass(x, y)) return null;
        if (succ == goal) return succ;
        // Oblique direction
        if (dx != 0 && dy != 0) {
            // There is a forced node near the current point, then the jump point is the current successor node
            if ((grid.canPass(x - dx, y + dy) && grid.obstacle(x - dx, y)) ||
                    (grid.canPass(x + dx, y - dy) && grid.obstacle(x, y - dy))) {
                return succ;
            }
            // Otherwise, there is no forced neighbor near the current position,
            // then recursively find a jump point in the vertical direction, and this jump point has a forced node
            if (jump(grid.cell(x + dx, y), succ) != null) return succ;
            // Recursively find a jump point in the horizontal direction, and this jump point has a forced node
            if (jump(grid.cell(x, y + dy), succ) != null) return succ;
        } else {
            // If the current successor node in the vertical direction has a jump point,
            // the jump point is the current successor node
            if (dx != 0) {
                if ((grid.canPass(x + dx, y + 1) && grid.obstacle(x, y + 1)) ||
                        (grid.canPass(x + dx, y - 1) && grid.obstacle(x, y - 1))) {
                    return succ;
                }
            } else {
                // If the current successor node in the horizontal direction has a jump point,
                // the jump point is the current successor node
                if ((grid.canPass(x + 1, y + dy) && grid.obstacle(x + 1, y)) ||
                        (grid.canPass(x - 1, y + dy) && grid.obstacle(x - 1, y))) {
                    return succ;
                }
            }
        }
        return jump(grid.cell(x + dx, y + dy), succ);
    }

    public List<? extends Node> findNeighbor(Node cur) {
        if (cur.parent == null) return grid.getSucc(cur);

        List<Node> neighbor = new ArrayList<>();
        int x = cur.x, y = cur.y;
        int px = cur.parent.x, py = cur.parent.y;
        int dx = 0, dy = 0;

        if (x > px) dx = 1;
        else if (x < px) dx = -1;

        if (y > py) dy = 1;
        else if (y < py) dy = -1;

        if (dx != 0 && dy != 0) {
            if (grid.canPass(x, y + dy))
                neighbor.add(grid.cell(x, y + dy));
            if (grid.canPass(x + dx, y))
                neighbor.add(grid.cell(x + dx, y));
            if (grid.canPass(x + dx, y + dy))
                neighbor.add(grid.cell(x + dx, y + dy));

            if (grid.obstacle(x - dx, y))
                neighbor.add(grid.cell(x - dx, y + dy));
            if (grid.obstacle(x, y - dy))
                neighbor.add(grid.cell(x + dx, y - dy));
        } else {
            // horizontal
            if (dx == 0) {
                // go straight
                if (grid.canPass(x, y + dy)) {
                    neighbor.add(grid.cell(x, y + dy));
                }
                // there are obstacles below
                if (grid.obstacle(x + 1, y)) {
                    neighbor.add(grid.cell(x + 1, y + dy));
                }
                // there are obstacles on it
                if (grid.obstacle(x - 1, y)) {
                    neighbor.add(grid.cell(x - 1, y + dy));
                }
            } else {
                // vertical
                if (grid.canPass(x + dx, y)) {
                    neighbor.add(grid.cell(x + dx, y));
                }
                // there is an obstacle on the left
                if (grid.obstacle(x, y - 1)) {
                    neighbor.add(grid.cell(x + dx, y - 1));
                }
                // there is an obstacle on the right
                if (grid.obstacle(x, y + 1)) {
                    neighbor.add(grid.cell(x + dx, y + 1));
                }
            }
        }
        return neighbor;
    }

    public List<Node> expandPath() {
        List<Node> path = backtrack();
        List<Node> realPath = new ArrayList<>();
        for (int i = 0; i + 1 < path.size(); i++) {
            int x = path.get(i).x, y = path.get(i).y;
            int x2 = path.get(i + 1).x, y2 = path.get(i + 1).y;
            int dx = 0, dy = 0;

            if (x2 > x)  // down
                dx = 1;
            else if (x2 < x)
                dx = -1;  // up
            if (y2 > y)   // right
                dy = 1;
            else if (y2 < y)  // left
                dy = -1;

            if (dx != 0 && dy != 0) {
                while (x != x2 && y != y2) {
                    if (!grid.canPass(x, y)) break;
                    realPath.add(grid.cell(x, y));
                    x += dx;
                    y += dy;
                }
            } else if (dx == 0) {
                while (y != y2) {
                    if (!grid.canPass(x, y)) break;
                    realPath.add(grid.cell(x, y));
                    y += dy;
                }
            } else {
                while (x != x2) {
                    if (!grid.canPass(x, y)) break;
                    realPath.add(grid.cell(x, y));
                    x += dx;
                }
            }
        }
        realPath.add(goal);
        return realPath;
    }

    @Override
    public void updateSuccessors(Node cur) {
        identifySuccessors(cur);
    }

    @Override
    public List<Node> getPath() {
        return expandPath();
    }

    @Override
    public double cost(Node x, Node y) {
        return 10 * Util.octile(x, y);
    }
}
