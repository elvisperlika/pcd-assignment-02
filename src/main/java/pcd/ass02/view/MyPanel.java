package pcd.ass02.view;

import com.github.javaparser.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {

    public static final int PADDING_PERCENTAGE = 10;
    private static final int MIN_DIST = 30;
    private final List<MyNode> nodes = new ArrayList<>();
    private final List<Pair<MyNode, MyNode>> arrows = new ArrayList<>();
    private final Random random = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        setBackground(Color.WHITE);
        if (!nodes.isEmpty()) {
            nodes.forEach(node -> {
                g2.drawString(node.className(), node.x(), node.y());
            });
        }
        if (!arrows.isEmpty()) {
            arrows.forEach(pair -> {
                MyNode s = pair.a;
                MyNode d = pair.b;
                drawArrowHead(g2, s.x(), s.y(), d.x(), d.y());
            });
        }
        g.setColor(Color.BLACK);
        g.drawString("n. nodi: " + nodes.size(), 10, 25);
        g.drawString("n. archi: " + arrows.size(), 10, 40);
    }

    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2) {
        int arrowSize = 10;
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int xArrow1 = x2 - (int)(arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = y2 - (int)(arrowSize * Math.sin(angle - Math.PI / 6));
        int xArrow2 = x2 - (int)(arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = y2 - (int)(arrowSize * Math.sin(angle + Math.PI / 6));
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x2, y2, xArrow1, yArrow1);
        g2.drawLine(x2, y2, xArrow2, yArrow2);
    }

    public void addToGraph(String source, String destination) {
        int minHeight = getHeight() / PADDING_PERCENTAGE;
        int maxHeight = this.getHeight() - minHeight;
        int minWidth = getWidth() / PADDING_PERCENTAGE;
        int maxWidth = this.getWidth() - minWidth;

        if (!nodes.stream().anyMatch(n -> n.className().equals(source))) {
            Point point = findFreePoint(nodes, minHeight, maxHeight, minWidth, maxWidth);
            nodes.add(new MyNode(source, point.x, point.y));
        }
        if (!nodes.stream().anyMatch(n -> n.className().equals(destination))) {
            Point point = findFreePoint(nodes, minHeight, maxHeight, minWidth, maxWidth);
            nodes.add(new MyNode(destination, point.x, point.y));
        }
        MyNode sourceNode = nodes.stream().filter(n -> n.className().equals(source)).findAny().get();
        MyNode destinationNode = nodes.stream().filter(n -> n.className().equals(destination)).findAny().get();
        arrows.add(new Pair<>(sourceNode, destinationNode));
    }

    private Point findFreePoint(List<MyNode> nodes, int minHeight, int maxHeight, int minWidth, int maxWidth) {
        Point point = new Point(random.nextInt(minHeight, maxHeight), random.nextInt(minWidth, maxWidth));
        for (MyNode node : nodes) {
            var xAbs = Math.abs(node.x() - point.x);
            var yAbs = Math.abs(node.y() - point.y);
            if (Math.sqrt(xAbs * xAbs + yAbs * yAbs) < MIN_DIST) {
                return findFreePoint(nodes, minHeight, maxHeight, minWidth, maxWidth);
            }
        }
        return point;
    }
}
