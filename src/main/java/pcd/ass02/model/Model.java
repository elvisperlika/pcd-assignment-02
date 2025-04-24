package pcd.ass02.model;

import pcd.ass02.model.report.Report;

import java.io.File;
import java.util.Arrays;

public class Model {

    private static final String JAVA_EXTENSION = ".java";
    private final String targetPath;
    private Report report;
    private boolean areDependenciesFind = false;

    public Model(String targetPath) {
        this.targetPath = targetPath;
    }

    public void exploreDependencies() {
        if (isJavaFile(targetPath)) {
            DependencyAnalyser.getClassDependency(targetPath, classReportAsyncResult -> {
                if (classReportAsyncResult.succeeded()) {
                    System.out.println(classReportAsyncResult.result().toString());
                } else {
                    System.out.println("Fail: " + classReportAsyncResult.cause());
                }
            });
        } else if (isPackage(targetPath)) {
            DependencyAnalyser.getPackageDependency(targetPath, packageReportAsyncResult -> {
                if (packageReportAsyncResult.succeeded()) {
                    System.out.println(packageReportAsyncResult.result().toString());
                } else {
                    System.out.println("Fail: " + packageReportAsyncResult.cause());
                }
            });
        } else if (isProject(targetPath)) {

            System.out.println("è un progetto");
        } else {
            System.out.println("This path is not allowed.");
        }
    }

    private boolean isProject(String targetPath) {
        File file = new File(targetPath);
        return file.isDirectory()
                && Arrays.stream(file.listFiles()).anyMatch(f -> f.isDirectory());
    }

    private boolean isPackage(String targetPath) {
        File file = new File(targetPath);
        return file.isDirectory()
                && Arrays.stream(file.listFiles()).allMatch(f -> isJavaFile(f.getPath()));
    }

    private boolean isJavaFile(String targetPath) {
        File file = new File(targetPath);
        return file.isFile() && targetPath.contains(JAVA_EXTENSION);
    }

    public boolean areDependenciesFind() {
        return areDependenciesFind;
    }
}
