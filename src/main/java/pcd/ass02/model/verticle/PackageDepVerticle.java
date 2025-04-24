package pcd.ass02.model.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.ClassReportImpl;
import pcd.ass02.model.report.PackageReport;

import java.io.File;
import java.util.Arrays;

public class PackageDepVerticle extends AbstractVerticle {
    private final String packageTargetPath;
    private final PackageReport packageReport;

    public PackageDepVerticle(String packageTargetPath, PackageReport packageReport) {
        this.packageTargetPath = packageTargetPath;
        this.packageReport = packageReport;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        File packageFile = new File(packageTargetPath);
        Arrays.stream(packageFile.listFiles()).toList().forEach(file -> {
            ClassReport classReport = new ClassReportImpl(file.getName());
            getVertx().deployVerticle(new ClassDepVerticle(file.getPath(), classReport));
            packageReport.addOnReportList(classReport);
        });
    }
}
