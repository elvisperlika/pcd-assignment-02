package pcd.ass02.model.rx.view;

import pcd.ass02.model.rx.report.ReactClassReport;

import javax.swing.*;
import java.awt.*;

public class ViewImpl implements View {

    private final JFrame frame;
    private final MyPanel panel;

    public ViewImpl(Integer width, Integer height) {
        this.frame = new JFrame("Reactive Dependency Analyzer");
        this.panel = new MyPanel();


        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        panel.repaint();
    }

    @Override
    public void update(ReactClassReport dependencyReport) {
        System.out.println("Found: " + dependencyReport);
        panel.addToGraph(dependencyReport.getSource(), dependencyReport.getDestination());
        panel.repaint();
    }
}
