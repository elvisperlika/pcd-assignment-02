package pcd.ass02.view;

import com.github.javaparser.utils.Pair;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {

    public static final int PADDING_FACTOR = 20;
    private static final int MIN_DIST = 5;
    private final List<MyNode> nodesToDraw = Collections.synchronizedList(new ArrayList<>());
    private final List<Pair<MyNode, MyNode>> arrows = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        setBackground(Color.WHITE);
        if (!arrows.isEmpty()) {
            arrows.forEach(pair -> {
                MyNode s = pair.a;
                MyNode d = pair.b;
                drawArrowHead(g2, s.x(), s.y(), d.x(), d.y());
            });
        }

        g2.setColor(Color.BLACK);
        if (!nodesToDraw.isEmpty()) {
            nodesToDraw.forEach(node -> g2.drawString(node.className(), node.x(), node.y()));
        }
        g2.drawString("N. Class: " + DependencyAnalyserReactiveLib.getClassCounter(), 5, 15);
        g2.drawString("N. Dependency: " + arrows.size(), 5, 30);
    }

    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2) {
        int arrowSize = 10;
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int xArrow1 = x2 - (int)(arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = y2 - (int)(arrowSize * Math.sin(angle - Math.PI / 6));
        int xArrow2 = x2 - (int)(arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = y2 - (int)(arrowSize * Math.sin(angle + Math.PI / 6));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x2, y2, xArrow1, yArrow1);
        g2.drawLine(x2, y2, xArrow2, yArrow2);
    }

    public void addToGraph(String source, String destination) {
        int minHeight = getHeight() / PADDING_FACTOR;
        int maxHeight = this.getHeight() - minHeight;
        int minWidth = getWidth() / PADDING_FACTOR;
        int maxWidth = this.getWidth() - minWidth;

        if (!nodesToDraw.stream().anyMatch(n -> n.className().equals(source))) {
            Point point = findFreePoint(nodesToDraw, minHeight, maxHeight, minWidth, maxWidth);
            nodesToDraw.add(new MyNode(source, point.x, point.y));
        }
        if (!nodesToDraw.stream().anyMatch(n -> n.className().equals(destination))) {
            Point point = findFreePoint(nodesToDraw, minHeight, maxHeight, minWidth, maxWidth);
            nodesToDraw.add(new MyNode(destination, point.x, point.y));
        }

        MyNode sourceNode = nodesToDraw.stream().filter(n -> n.className().equals(source)).findAny().get();
        MyNode destinationNode = nodesToDraw.stream().filter(n -> n.className().equals(destination)).findAny().get();
        arrows.add(new Pair<>(sourceNode, destinationNode));
    }

    private Point findFreePoint(List<MyNode> nodes, int minHeight, int maxHeight, int minWidth, int maxWidth) {
        Point point = new Point(random.nextInt(minWidth, maxWidth), random.nextInt(minHeight, maxHeight));
        for (MyNode node : nodes) {
            var xAbs = Math.abs(node.x() - point.x);
            var yAbs = Math.abs(node.y() - point.y);
            if (Math.sqrt(xAbs * xAbs + yAbs * yAbs) < MIN_DIST) {
                return findFreePoint(nodes, minHeight, maxHeight, minWidth, maxWidth);
            }
        }
        return point;
    }

    public void clearAll() {
        nodesToDraw.clear();
        arrows.clear();
        this.repaint();
    }
}
