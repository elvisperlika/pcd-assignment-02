package pcd.ass02.view;

import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;
import pcd.ass02.model.rx.report.ReactClassReport;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class App {

    private final JFrame frame;
    private final MyPanel mainPanel;
    private File selectedFile = new File(".");

    public App(int width, int height) {
        frame = new JFrame("Reactive Dependency Analyzer");

        mainPanel = new MyPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton openButton = new JButton("Scegli tra: Java File, Package e Progetto");
        openButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            }
        });

        JButton startAnalyserButton = new JButton("Analyse");
        startAnalyserButton.addActionListener(e -> {
            this.mainPanel.clearAll();
            activeExploration(selectedFile);
        });

        bottomPanel.add(openButton);
        bottomPanel.add(startAnalyserButton);

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void activeExploration(File selectedFile) {
        if (MyJavaUtil.isJavaFile(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getClassDependency(selectedFile.getPath()).subscribe(this::update);
        } else if (MyJavaUtil.isPackage(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getPackageDependency(selectedFile.getPath()).subscribe(this::update);
        } else if (MyJavaUtil.isProject(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getProjectDependency(selectedFile.getPath()).subscribe(this::update);
        } else {
            System.out.println("Path non valida");
        }
    }

    public void update(ReactClassReport dependencyReport) {
        mainPanel.addToGraph(dependencyReport.getSource(), dependencyReport.getDestination());
        mainPanel.repaint();
    }

    public void setFullScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setSize(dimension);
    }

}
