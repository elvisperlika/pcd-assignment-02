package pcd.ass02.view;

import pcd.ass02.model.rx.report.ReactClassReport;

import javax.swing.*;
import java.awt.*;

public class ViewImpl implements View {

    private final JFrame frame;
    private final MyPanel panel;

    public ViewImpl(int width, int height) {
        this.frame = new JFrame("Reactive Dependency Analyzer");
        this.panel = new MyPanel();


        frame.setSize(width, height);
        panel.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void update(ReactClassReport dependencyReport) {
        // System.out.println("Found: " + dependencyReport.getSource() + " -> " + dependencyReport.getDestination());
        panel.addToGraph(dependencyReport.getSource(), dependencyReport.getDestination());
        panel.repaint();
    }

    @Override
    public void setFullScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setSize(dimension);
    }
}
