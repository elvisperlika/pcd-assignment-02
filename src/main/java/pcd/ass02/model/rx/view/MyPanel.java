package pcd.ass02.model.rx.view;

import com.github.javaparser.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

record Node(String className, int x, int y){}

public class MyPanel extends JPanel {

    public static final int PADDING_PERCENTAGE = 10;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Pair<Node, Node>> arrows = new ArrayList<>();
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
                Node s = pair.a;
                Node d = pair.b;
                drawArrowHead(g2, s.x(), s.y(), d.x(), d.y());
            });
        }
        g.setColor(Color.BLACK);
        g.drawString("n nodes: " + nodes.size(), 10, 25);
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
            nodes.add(new Node(source, random.nextInt(minHeight, maxHeight), random.nextInt(minWidth, maxWidth)));
        }
        if (!nodes.stream().anyMatch(n -> n.className().equals(destination))) {
            nodes.add(new Node(destination, random.nextInt(minHeight, maxHeight), random.nextInt(minWidth, maxWidth)));
        }
        Node sourceNode = nodes.stream().filter(n -> n.className().equals(source)).findAny().get();
        Node destinationNode = nodes.stream().filter(n -> n.className().equals(destination)).findAny().get();
        arrows.add(new Pair<>(sourceNode, destinationNode));
    }
}
