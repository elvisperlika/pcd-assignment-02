package pcd.ass02;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;
import pcd.ass02.model.rx.report.ReactClassReport;
import pcd.ass02.model.rx.view.View;
import pcd.ass02.model.rx.view.ViewImpl;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path CLASS_PATH = Paths.get("src/main/java/pcd/ass02/model/MyJavaUtil.java");
    private static final Path PACKAGE_PATH = Paths.get("src/main/java/pcd/ass02/model/vertx/report");
    private static final Path PROJECT_PATH = Paths.get("src");
    private static final Integer WIDTH = 800;
    private static final Integer HEIGHT = 800;

    public static void main(String[] args) {

        // Class Example
        // Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getClassDependency(CLASS_PATH.toString());

        Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getPackageDependency(PACKAGE_PATH.toString());

        View view = new ViewImpl(WIDTH, HEIGHT);
        source.subscribe(reactClassReport -> {
            Thread.sleep(1000);
            view.update(reactClassReport);
        });
    }
}