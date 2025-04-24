package pcd.ass02.model.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import pcd.ass02.model.DependencyAnalyser;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.report.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ProjectDepVerticle extends AbstractVerticle {
    private final String projectTargetPath;
    private final ProjectReport projectReport;

    public ProjectDepVerticle(String projectTargetPath, ProjectReport projectReport) {
        this.projectTargetPath = projectTargetPath;
        this.projectReport = projectReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File projectFile = new File(projectTargetPath);
        List<Future<String>> futures = new ArrayList<>();
        List<PackageReport> packageReportList = new ArrayList<>();
        for (File file : Arrays.stream(projectFile.listFiles()).toList()) {
            String path = file.getPath();
            if (MyJavaUtil.isJavaFile(path)) {
                ClassReport classReport = new ClassReportImpl(path);
                futures.add(vertx.deployVerticle(new ClassDepVerticle(path, classReport)));
                projectReport.addToClassReportList(classReport);
            } else if (MyJavaUtil.isPackage(path)) {
                PackageReport packageReport = new PackageReportImpl(path);
                futures.add(vertx.deployVerticle(new PackageDepVerticle(path, packageReport)));
                packageReportList.add(packageReport);
            } else if (MyJavaUtil.isProject(path)) {
                futures.add(vertx.deployVerticle(new ProjectDepVerticle(path, projectReport)));
            } else {
                promise.fail("Path not allowed: " + file.getPath());
            }
        }

        Future.all(futures).onSuccess(compositeFuture -> {
            if (compositeFuture.succeeded()) {
                packageReportList.forEach(packageReport -> {
                    packageReport.getClassReportList().forEach(projectReport::addToClassReportList);
                });
                promise.complete();
            } else {
                promise.fail("Composite future failed.");
            }
        });
    }
}
