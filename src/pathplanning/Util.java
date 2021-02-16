package pathplanning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public final class Util {

    public static double manhattan(Node x, Node y) {
        return Math.abs(x.x - y.x) + Math.abs(x.y - y.y);
    }

    public static double euclidean(Node x, Node y) {
        return Math.hypot(x.x - y.x, x.y - y.y);
    }

    public static double octile(Node x, Node y) {
        double dx = Math.abs(x.x - y.x);
        double dy = Math.abs(x.y - y.y);
        if (dx > dy) {
            double t = dx;
            dx = dy;
            dy = t;
        }
        return ((Math.sqrt(2) - 1.0) * dx + dy);
    }

    public static double chebyshev(Node x, Node y) {
        return Math.max(Math.abs(x.x - y.x), Math.abs(x.y - y.y));
    }

    public static void printColor(String str, int color) {
        if (System.getProperties().getProperty("os.name").equals("Linux")) {
            String s = "\033[" + color + ";1m" + str + "\033[0m";
            System.out.print(s);
        } else
            System.out.print(str);
    }

    public static void attach(Grid grid, List<Node> path) {
        if (path != null) {
            for (var node : path) {
                grid.map.get(node.x).get(node.y).mark = true;
            }
        }
    }

    public static void printGird(Grid grid, List<Node> path) {
        attach(grid, path);

        for (var l : grid.map) {
            for (var node : l) {
                if (node.mark) printColor("o", 33);
                else if (node.obstacle) printColor("#", 32);
                else System.out.print(".");
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static Grid loadByArray(int[][] array) {
        if (array == null || array.length == 0) return null;
        Grid grid = new Grid();
        grid.setHeight(array.length);
        grid.setWidth(array[0].length);

        grid.map = new ArrayList<>();
        for (int x = 0; x < array.length; x++) {
            ArrayList<Node> list = new ArrayList<>();
            for (int y = 0; y < array[x].length; y++) {
                Node node = new Node(x, y);
                if (array[x][y] == 1) node.obstacle = true;
                list.add(node);
            }
            grid.map.add(list);
        }
        return grid;
    }

    public static Grid loadByMapFile(String filename) {
        Grid grid = new Grid();
        grid.map = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int x = 0, y = 0;
            while ((line = reader.readLine()) != null) {
                y = 0;
                ArrayList<Node> list = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ' ') continue;
                    Node node = new Node(x, y);
                    if (line.charAt(i) == '#')
                        node.obstacle = true;
                    list.add(node);
                    y++;
                }
                grid.map.add(list);
                x++;
            }
            grid.setWidth(y);
            grid.setHeight(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    public static List<Pair<Node>> buildLines(List<Node> nodeList) {
        List<Pair<Node>> lines = new ArrayList<>();
        for (int i = 0; i + 1 < nodeList.size(); i++) {
            lines.add(new Pair<>(nodeList.get(i), nodeList.get(i + 1)));
        }
        return lines;
    }
}
