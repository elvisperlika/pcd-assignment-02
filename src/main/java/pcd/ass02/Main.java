package pcd.ass02;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import pcd.ass02.model.rx.DependencyAnalyserReactiveLib;
import pcd.ass02.model.rx.report.ReactClassReport;
import pcd.ass02.view.View;
import pcd.ass02.view.ViewImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final Path CLASS_PATH = Paths.get("src/main/java/pcd/ass02/model/MyJavaUtil.java");
    private static final Path PACKAGE_PATH = Paths.get("src/main/java/pcd/ass02/model/vertx/report");
    private static final Path PROJECT_PATH = Paths.get("src");
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public static void main(String[] args) {

        // Class Example
        // Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getClassDependency(CLASS_PATH.toString());

        // Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getPackageDependency(PACKAGE_PATH.toString());

        Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getProjectDependency(PROJECT_PATH.toString());

        View view = new ViewImpl(WIDTH, HEIGHT);
        source
                .subscribe(reactClassReport -> {
                    Thread.sleep(30);
                    //System.out.println(reactClassReport.getSource() + " -> " + reactClassReport.getDestination());
                    view.update(reactClassReport);
                }, throwable -> {
                    // in case of error
                    System.out.println("Error: " +  throwable.getCause());
                }, () -> {
                    // onComplete
                    System.out.println("Completed");
                });
    }
}