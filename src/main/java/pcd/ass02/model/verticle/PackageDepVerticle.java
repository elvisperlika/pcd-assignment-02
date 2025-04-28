package pcd.ass02.model.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.ClassReportImpl;
import pcd.ass02.model.report.PackageReport;

import java.io.File;
import java.util.*;

public class PackageDepVerticle extends AbstractVerticle {
    private final String packageTargetPath;
    private final PackageReport packageReport;

    public PackageDepVerticle(String packageTargetPath, PackageReport packageReport) {
        this.packageTargetPath = packageTargetPath;
        this.packageReport = packageReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File packageFile = new File(packageTargetPath);
        List<Future<String>> futures = new ArrayList<>();
        List<File> files = Arrays.stream(packageFile.listFiles()).toList();
        if (files.isEmpty()) {
            System.out.println("Empty Package");
            promise.complete();
        } else {
            files.stream()
                    .filter(file -> MyJavaUtil.isJavaFile(file.getPath()))
                    .forEach(file -> {
                        ClassReport classReport = new ClassReportImpl(file.getName());
                        futures.add(getVertx().deployVerticle(new ClassDepVerticle(file.getPath(), classReport)));
                        packageReport.addInReportList(classReport);
                    });

            Future.all(futures)
                    .onSuccess(compositeFuture -> promise.complete())
                    .onFailure(throwable -> promise.fail("Composite future failed."));
        }
    }
}
