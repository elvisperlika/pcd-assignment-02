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

    public ProjectDepVerticle(String projectTargetPath, ProjectReport projectReport) {
        this.projectTargetPath = projectTargetPath;
        this.projectReport = projectReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File projectFile = new File(projectTargetPath);
        List<File> javaFiles = new ArrayList<>();
        findAllJavaFiles(javaFiles, projectFile);
        List<Future<String>> futures = new ArrayList<>();
        for (File file : javaFiles) {
            String filePath = file.getPath();
            ClassReport classReport = new ClassReportImpl(filePath);
            futures.add(getVertx().deployVerticle(new ClassDepVerticle(filePath, classReport)));
            classReportList.add(classReport);
        }

        Future.all(futures).onSuccess(_ -> {
            classReportList.forEach(projectReport::addToClassReportList);
            System.out.println("ALL FEATURES OK");
            promise.complete();
        }).onFailure(handler -> {
            System.out.println("ALL FEATURES BAD");
            promise.fail("Fail B: " + handler.getCause());
        });
    }

    private void findAllJavaFiles(List<File> javaFiles, File projectFile) {
        for (File file : Arrays.stream(projectFile.listFiles()).toList()) {
            if (MyJavaUtil.isJavaFile(file.getPath())) {
                javaFiles.add(file);
            } else if (file.isDirectory()) {
                findAllJavaFiles(javaFiles, file);
            }
        }
    }

}
