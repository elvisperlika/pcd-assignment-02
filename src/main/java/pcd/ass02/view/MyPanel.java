package pcd.ass02.view;

import com.github.javaparser.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {

    public static final int PADDING_FACTOR = 40;
    public static int INC_FACTOR = 100;
    private final List<MyNode> nodesToDraw = Collections.synchronizedList(new ArrayList<>());
    private final List<Pair<MyNode, MyNode>> arrows = Collections.synchronizedList(new ArrayList<>());
    private int ray;
    private Point centre;
    private double scale = 1;

    public MyPanel(int width, int height) {
        ray = getRayByPanelSize(width, height);
        centre = getCentreByPanelSize(width, height);
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

    private int getRayByPanelSize(int width, int height) {
        return width < height ? (width - PADDING_FACTOR) / 2 : (height - PADDING_FACTOR) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.scale(scale, scale);

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
            findNodePointAndAdd(source);
        }
        if (!nodesToDraw.stream().anyMatch(n -> n.className().equals(destination))) {
            findNodePointAndAdd(destination);
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

    private void findNodePointAndAdd(String source) {
        nodesToDraw.addFirst(new MyNode(source, 0, 0));
        incCircumferenceSize();

        double fracDegree = (double) 360 / nodesToDraw.size();
        if (nodesToDraw.size() % 100 == 0) {
            INC_FACTOR = INC_FACTOR * 2;
        }
        System.out.println("FR: " + fracDegree);
        for (int i = 0; i < nodesToDraw.size(); i++) {
            Point p = getPointByDegree(fracDegree * i);
            nodesToDraw.get(i).setNewPosition(p.x, p.y);
        }

        incPanelSize();
    }

    private void incPanelSize() {
        MyNode maxXNode = null;
        MyNode maxYNode = null;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (MyNode node : nodesToDraw) {
            if (node.x() > maxX) {
                maxX = node.x();
                maxXNode = node;
            }
            if (node.y() > maxY) {
                maxY = node.y();
                maxYNode = node;
            }
        }

        setPreferredSize(new Dimension(maxXNode.x() + (PADDING_FACTOR * 4),
                maxYNode.y() + (PADDING_FACTOR * 2)));
    }

    private void incCircumferenceSize() {
        this.ray = this.ray + (this.ray / INC_FACTOR);
        this.centre = new Point(centre.x + (this.ray / INC_FACTOR), centre.y + (this.ray / INC_FACTOR));
    }

    private Point getPointByDegree(double fracDegree) {
        double angleRad = Math.toRadians(fracDegree);
        double cosU = Math.cos(angleRad);
        double sinU = Math.sin(angleRad);
        return new Point((int) (centre.x + (ray * cosU)), (int) (centre.y + (ray * sinU)));
    }

    public void clearAll() {
        nodesToDraw.clear();
        arrows.clear();
    }

    public void zoom(double factor) {
        scale *= factor;
        revalidate();
        repaint();
    }

    public String getDependenciesFound() {
        return String.valueOf(arrows.size());
    }
}
