package pcd.ass02.model.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.report.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectDepVerticle extends AbstractVerticle {
    private final String projectTargetPath;
    private final ProjectReport projectReport;
    private final List<ClassReport> classReportList = new ArrayList<>();
    private final List<PackageReport> packageReportList = new ArrayList<>();
    private final List<ProjectReport> projectReportList = new ArrayList<>();

    public ProjectDepVerticle(String projectTargetPath, ProjectReport projectReport) {
        this.projectTargetPath = projectTargetPath;
        this.projectReport = projectReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File projectFile = new File(projectTargetPath);
        List<File> files = Arrays.stream(projectFile.listFiles()).toList();
        List<Future<String>> futures = new ArrayList<>();
        files.forEach(file -> {
            if (MyJavaUtil.isJavaFile(file.getPath())) {
                ClassReport classReport = new ClassReportImpl(file.getPath());
                futures.add(getVertx().deployVerticle(new ClassDepVerticle(file.getPath(), classReport)));
                classReportList.add(classReport);
            } else if (MyJavaUtil.isPackage(file.getPath())) {
                PackageReport packageReport = new PackageReportImpl(file.getPath());
                futures.add(getVertx().deployVerticle(new PackageDepVerticle(file.getPath(), packageReport)));
                packageReportList.add(packageReport);
            } else if (MyJavaUtil.isProject(file.getPath())) {
                ProjectReport projectReport = new ProjectReportImpl(file.getPath());
                futures.add(getVertx().deployVerticle(new ProjectDepVerticle(file.getPath(), projectReport)));
                projectReportList.add(projectReport);
            }
        });

        Future.all(futures)
                .onSuccess(compositeFuture -> {
                    projectReport.setClassReportList(classReportList);
                    projectReport.setPackageReportList(packageReportList);
                    projectReport.setSubProjectReportList(projectReportList);
                    promise.complete();
                })
                .onFailure(throwable -> {
                    promise.fail("Project Verticle Fail: " + throwable.getCause());
                    throwable.printStackTrace();
                });
    }

}
