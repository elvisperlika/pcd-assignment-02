package pcd.ass02.view;

import com.github.javaparser.utils.Pair;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {

    public static final int PADDING_FACTOR = 30;
    private static final int MIN_DIST = 10;
    private final List<MyNode> nodesToDraw = Collections.synchronizedList(new ArrayList<>());
    private final List<Pair<MyNode, MyNode>> arrows = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();
    private final double ray;
    private final Point centre;

    public MyPanel(int width, int height) {
        ray = getRayByPanelSize(width, height);
        centre = getCentreByPanelSize(width, height);
        System.out.println(ray);
        System.out.println(centre);
    }

    private Point getCentreByPanelSize(int width, int height) {
        var w = width - PADDING_FACTOR;
        var h = height - PADDING_FACTOR;
        if (w < h) {
            return new Point(w / 2 + PADDING_FACTOR, w / 2 + PADDING_FACTOR);
        } else {
            return new Point(h / 2 + PADDING_FACTOR, h / 2 + PADDING_FACTOR);
        }
    }

    private double getRayByPanelSize(int width, int height) {
        return width < height ? (double) (width - PADDING_FACTOR) / 2 : (double) (height - PADDING_FACTOR) / 2;
    }

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

        MyNode sourceNode = nodesToDraw.stream()
                .filter(n -> n.className().equals(source))
                .findAny()
                .get();
        MyNode destinationNode = nodesToDraw.stream()
                .filter(n -> n.className().equals(destination))
                .findAny()
                .get();
        arrows.add(new Pair<>(sourceNode, destinationNode));
    }

    private Point findFreePoint(List<MyNode> nodes, int minHeight, int maxHeight, int minWidth, int maxWidth) {
        double degree = random.nextDouble(0, 360);
        double cosU = Math.cos(degree);
        double sinU = Math.sin(degree);
        Point point = new Point(centre.x + (int) (ray * cosU), centre.y + (int) (ray * sinU));
        for (MyNode node : nodes) {
            var xAbs = Math.abs(node.x() - point.x);
            var yAbs = Math.abs(node.y() - point.y);
            if (Math.sqrt(xAbs * xAbs + yAbs * yAbs) < MIN_DIST) {
                return findFreePoint(nodes, minHeight, maxHeight, minWidth, maxWidth);
            }
        }
        return point;

//        Point point = new Point(random.nextInt(minWidth, maxWidth), random.nextInt(minHeight, maxHeight));
    }

    public void clearAll() {
        nodesToDraw.clear();
        arrows.clear();
    }
}
