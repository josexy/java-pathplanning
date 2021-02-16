package pathplanning;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final int[][] offset_8 = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
    private final int[][] offset_4 = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int width;
    private int height;
    private boolean diagonal;

    ArrayList<ArrayList<Node>> map = null;

    public Grid() {
        diagonal = true;
    }

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.diagonal = true;
        map = new ArrayList<>();
        for (int x = 0; x < height; x++) {
            ArrayList<Node> list = new ArrayList<>();
            for (int y = 0; y < width; y++) {
                list.add(new Node(x, y));
            }
            map.add(list);
        }
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public List<Node> getSucc(Node cur) {
        List<Node> list = new ArrayList<>();
        int[][] offset = diagonal ? offset_8 : offset_4;

        for (int[] ofs : offset) {
            int nx = cur.x + ofs[0];
            int ny = cur.y + ofs[1];
            if (canPass(nx, ny)) {
                list.add(map.get(nx).get(ny));
            }
        }
        return list;
    }

    public List<Node> getPred(Node cur) {
        return getSucc(cur);
    }

    public boolean in(int x, int y) {
        return x >= 0 && y >= 0 && x < height && y < width;
    }

    public boolean canPass(int x, int y) {
        return in(x, y) && !cell(x, y).obstacle;
    }

    public boolean obstacle(int x, int y) {
        return !canPass(x, y);
    }

    public Node cell(int x, int y) {
        return in(x, y) ? map.get(x).get(y) : null;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
