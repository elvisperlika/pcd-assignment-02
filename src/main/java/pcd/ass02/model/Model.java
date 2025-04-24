package pcd.ass02.model;

import pcd.ass02.model.report.Report;

import java.io.File;
import java.util.Arrays;

public class Model {

    private final String targetPath;

    public Model(String targetPath) {
        this.targetPath = targetPath;
    }

    public void exploreDependencies() {
        if (MyJavaUtil.isJavaFile(targetPath)) {
            DependencyAnalyser.getClassDependency(targetPath, classReportAsyncResult -> {
                if (classReportAsyncResult.succeeded()) {
                    System.out.println(classReportAsyncResult.result().toString());
                } else {
                    System.out.println("Fail: " + classReportAsyncResult.cause());
                }
            });
        } else if (MyJavaUtil.isPackage(targetPath)) {
            DependencyAnalyser.getPackageDependency(targetPath, packageReportAsyncResult -> {
                if (packageReportAsyncResult.succeeded()) {
                    System.out.println(packageReportAsyncResult.result().toString());
                } else {
                    System.out.println("Fail: " + packageReportAsyncResult.cause());
                }
            });
        } else if (MyJavaUtil.isProject(targetPath)) {
            DependencyAnalyser.getProjectDependency(targetPath, projetReportAsyncResult -> {
                if (projetReportAsyncResult.succeeded()) {
                    projetReportAsyncResult.result().getClassReportList().forEach(classReport -> {
                        System.out.println(classReport.toString());
                    });
                } else {
                    System.out.println("Fail: " + projetReportAsyncResult.cause());
                }
            });
        } else {
            System.out.println("This path is not allowed.");
        }
    }

}
