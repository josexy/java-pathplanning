import pathplanning.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Panel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    final static int CELL_SIZE = 10;
    final static int BLOCK_SIZE = 2;
    int width;
    int height;

    java.util.List<Node> path;
    AStar astar;
    Node start;
    Node goal;

    public Panel(int width, int height) {
        super();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.width = width / CELL_SIZE;
        this.height = height / CELL_SIZE;

        Grid grid = new Grid(this.width, this.height);
        // grid.setDiagonal(false);
        // Change path planning algorithm
        astar = new JPS(0, 0, grid.getHeight() - 1, grid.getWidth() - 1, grid);

        start = astar.getStart();
        goal = astar.getGoal();
    }

    public Point mapToGridPos(int x, int y) {
        return new Point(y / CELL_SIZE, (x / CELL_SIZE));
    }

    public Point mapToScreenPos(int x, int y) {
        return new Point(y * CELL_SIZE, (x * CELL_SIZE));
    }

    public Point mapToGridPos(Point pos) {
        return new Point(pos.y / CELL_SIZE, (pos.x / CELL_SIZE));
    }

    public Point mapToScreenPos(Point pos) {
        return new Point(pos.y * CELL_SIZE, (pos.x * CELL_SIZE));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1));

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Point mp = mapToScreenPos(x, y);
                Node node = astar.getGrid().cell(x, y);

                if (node == start) {
                    g2d.setColor(Color.red);
                    g2d.fillRect(mp.x, mp.y, CELL_SIZE, CELL_SIZE);
                } else if (node == goal) {
                    g2d.setColor(Color.blue);
                    g2d.fillRect(mp.x, mp.y, CELL_SIZE, CELL_SIZE);
                } else {
                    if (node.obstacle) {
                        g2d.setColor(Color.gray);
                        g2d.fillRect(mp.x, mp.y, CELL_SIZE, CELL_SIZE);
                    } else if (node.mark) {
                        g2d.setColor(Color.green);
                        g2d.fillRect(mp.x, mp.y, CELL_SIZE, CELL_SIZE);
                    } else {
                        g2d.setColor(Color.white);
                        g2d.fillRect(mp.x, mp.y, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
        if (path == null) return;

        if (astar instanceof ThetaStar) {
            g.setColor(Color.darkGray);
            Stroke old = g2d.getStroke();
            g2d.setStroke(new BasicStroke(5));
            var lines = Util.buildLines(path);
            for (var line : lines) {
                Point p1 = mapToScreenPos(line.getFirst().x, line.getFirst().y);
                Point p2 = mapToScreenPos(line.getSecond().x, line.getSecond().y);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            g2d.setStroke(old);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Exit
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_Q) System.exit(0);
            // Run
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            // clear marked path
            astar.reset(false);
            path = astar.run();
            if (path == null) {
                System.out.println("Not found path!");
                return;
            }
            Util.attach(astar.getGrid(), path);
            // Util.printGird(astar.getGrid(), path);
        }
        // Clear obstacles and path
        else if (e.getKeyCode() == KeyEvent.VK_C) {
            System.out.println("Clear!");
            astar.reset(true);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point pos = mapToGridPos(e.getPoint());
        Node node = astar.getGrid().cell(pos.x, pos.y);
        if (node == null) return;

        if (e.getClickCount() == 2) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                start = node;
                astar.setStart(start);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                goal = node;
                astar.setGoal(goal);
            }
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point pos = mapToGridPos(e.getPoint());
        Node node = astar.getGrid().cell(pos.x, pos.y);
        if (node == null) return;

        for (int x = -BLOCK_SIZE; x < BLOCK_SIZE; x++) {
            for (int y = -BLOCK_SIZE; y < BLOCK_SIZE; y++) {
                Node n = astar.getGrid().cell(pos.x + x, pos.y + y);
                if (n == null) continue;
                n.obstacle = !e.isShiftDown();
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

class Frame extends JFrame {
    public Frame(int width, int height) {
        super();
        initUI(width, height);
    }

    public void initUI(int width, int height) {
        setTitle("Java-PathPlanning");
        setSize(width, height + 50);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Panel panel = new Panel(width, height);
        panel.setFocusable(true);
        setContentPane(panel);
        setVisible(true);
    }
}

public class GUIMain {
    public static void main(String[] args) {
        new Frame(800, 800);
    }
}
