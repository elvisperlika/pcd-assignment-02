package pcd.ass02;

import pcd.ass02.controller.Controller;
import pcd.ass02.controller.ControllerImpl;
import pcd.ass02.model.DependencyAnalyser;
import pcd.ass02.model.Model;
import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.ClassReportImpl;
import pcd.ass02.view.View;
import pcd.ass02.view.ViewImpl;

public class Main {
    private static final String TARGET_PATH = "src/main/java/pcd/ass02/target/deep/moreDeep/TargetMain.java";

    public static void main(String[] args) {
        ClassReport classReport = new ClassReportImpl(TARGET_PATH);
        DependencyAnalyser.getClassDependency(TARGET_PATH, classReport, handler -> {
            if (handler.succeeded()) {
                System.out.println(classReport);
            } else {
                System.out.println("Fail: " + handler.cause());
            }
        });
        while (true) {
            System.out.println("loop");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        Model model = new Model(TARGET_PATH);
//        View view = new ViewImpl();
//        Controller controller = new ControllerImpl(model);
//        controller.attachView(view);
//        controller.start();
    }
}