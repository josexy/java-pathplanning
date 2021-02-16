import pathplanning.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[][] map = {
                {0, 0, 0, 0},
                {1, 0, 0, 1},
                {1, 0, 1, 1},
                {1, 0, 0, 0}
        };
        Grid grid = Util.loadByMapFile("res/map2.txt");
        // Grid grid = Util.loadByArray(map);

        AStar aStar = new AStar(14, 1, 14, 26, grid);
        List<Node> path = aStar.run();
        System.out.println(path);

        Util.printGird(grid, path);
    }
}
