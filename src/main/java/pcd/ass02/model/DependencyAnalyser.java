package pcd.ass02.model;

import io.vertx.core.*;
import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.ClassReportImpl;
import pcd.ass02.model.report.PackageReport;
import pcd.ass02.model.report.PackageReportImpl;
import pcd.ass02.model.verticle.ClassDepVerticle;
import pcd.ass02.model.verticle.PackageDepVerticle;

import java.io.File;

public class DependencyAnalyser {

    public static void getClassDependency(String classTargetPath, Handler<AsyncResult<ClassReport>> handler) {
        Vertx vertx = Vertx.vertx();
        ClassReport classReport = new ClassReportImpl(classTargetPath);
        vertx.deployVerticle(new ClassDepVerticle(classTargetPath, classReport), asyncRes -> {
            if (asyncRes.succeeded()) {
                handler.handle(Future.succeededFuture(classReport));
            } else {
                handler.handle(Future.failedFuture(asyncRes.cause()));
            }
        });
    }

    public static void getPackageDependency(String packageTargetPath, Handler<AsyncResult<PackageReport>> handler) {
        Vertx vertx = Vertx.vertx();
        PackageReport packageReport = new PackageReportImpl(packageTargetPath);
        vertx.deployVerticle(new PackageDepVerticle(packageTargetPath, packageReport), asyncRes -> {
            if (asyncRes.succeeded()) {
                handler.handle(Future.succeededFuture(packageReport));
            } else {
                handler.handle(Future.failedFuture(asyncRes.cause()));
            }
        });
    }
}
