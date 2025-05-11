package pcd.ass02.view;

import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;
import pcd.ass02.model.rx.report.ReactClassReport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class App {

    public static final int SCROLL_SPEED = 10;
    private final JFrame frame;
    private final MyPanel mainPanel;
    private File selectedFile = new File(".");
    private final Label classNumberLabel = new Label("n Class Analysed: " + 0);
    private final Label depNumberLabel = new Label("n Dependencies found: " + 0);
    private final Label messageLabel = new Label("Message: none");
    JScrollPane scrollPane;

    public App(int width, int height) {
        frame = new JFrame("Reactive Dependency Analyzer");

        mainPanel = new MyPanel(width, height);
        scrollPane = new JScrollPane(mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);

        setupZoomKeyBindings();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton openButton = new JButton("Choose source");
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
        bottomPanel.add(new Label("   |   "));
        classNumberLabel.setSize(100, classNumberLabel.getHeight());
        bottomPanel.add(classNumberLabel);
        bottomPanel.add(new Label("   |   "));
        depNumberLabel.setSize(100, depNumberLabel.getHeight());
        bottomPanel.add(depNumberLabel);
        bottomPanel.add(new Label("   |   "));
        messageLabel.setSize(100, messageLabel.getHeight());
        bottomPanel.add(messageLabel);

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void setupZoomKeyBindings() {
        InputMap im = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = mainPanel.getActionMap();

        // Cmd (Mac) o Ctrl (Win) + +
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "zoomIn");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "zoomOut");

        am.put("zoomIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.zoom(1.1); // Zoom in
                scrollPane.getHorizontalScrollBar().setValue(MouseInfo.getPointerInfo().getLocation().x);
                scrollPane.getVerticalScrollBar().setValue(MouseInfo.getPointerInfo().getLocation().y);
            }
        });

        am.put("zoomOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.zoom(0.9); // Zoom out
            }
        });
    }

    private void activeExploration(File selectedFile) {
        DependencyAnalyserReactiveLib.resetCounter();
        if (MyJavaUtil.isJavaFile(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getClassDependency(selectedFile.getPath()).subscribe(this::update);
        } else if (MyJavaUtil.isPackage(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getPackageDependency(selectedFile.getPath()).subscribe(this::update);
        } else if (MyJavaUtil.isProject(selectedFile.getPath())) {
            DependencyAnalyserReactiveLib.getProjectDependency(selectedFile.getPath())
                    .subscribe(c -> {
                        this.updateLabels();
                        this.update(c);
                    }, er -> {
                        this.show("ERROR: " + er.getMessage());
                    }, () -> {
                        this.show("COMPLETED!");
                    });
        } else {
            System.out.println("Invalid Path");
        }
    }

    private void updateLabels() {
        SwingUtilities.invokeLater(() -> {
            classNumberLabel.setText("n Class Analysed: " + DependencyAnalyserReactiveLib.getClassCounter());
            depNumberLabel.setText("n Dependencies found: " + mainPanel.getDependenciesFound());
        });
    }

    private void show(String s) {
        messageLabel.setText("Message: " + s);
        updateView();
    }

    public void update(ReactClassReport dependencyReport) {
        mainPanel.addToGraph(dependencyReport.getSource(), dependencyReport.getDestination());
        updateView();
    }

    private void updateView() {
        mainPanel.revalidate();
        mainPanel.paintImmediately(mainPanel.getBounds()); // sync
        // mainPanel.repaint(); // async
    }

    public void setFullScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setSize(dimension);
    }

}
