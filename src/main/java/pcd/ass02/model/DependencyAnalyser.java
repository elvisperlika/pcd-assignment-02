package pcd.ass02.model;

import io.vertx.core.*;
import pcd.ass02.model.report.*;
import pcd.ass02.model.verticle.ClassDepVerticle;
import pcd.ass02.model.verticle.PackageDepVerticle;
import pcd.ass02.model.verticle.ProjectDepVerticle;

import java.io.File;

public class DependencyAnalyser {

    /**
     * Produce asynchronously a ClassReport with the target class's dependencies.
     * @param classTargetPath is the target class's path.
     * @param handler
     */
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

    /**
     * Produce asynchronously a PackageReport with class's ClassReport
     * @param packageTargetPath is the target package's path.
     * @param handler
     */
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

    public static void getProjectDependency(String projectTargetPath, Handler<AsyncResult<ProjectReport>> handler) {
        Vertx vertx = Vertx.vertx();
        ProjectReport projectReport = new ProjectReportImpl(projectTargetPath);
        vertx.deployVerticle(new ProjectDepVerticle(projectTargetPath, projectReport), asyncRes -> {
            if (asyncRes.succeeded()) {
                handler.handle(Future.succeededFuture(projectReport));
            } else {
                handler.handle(Future.failedFuture(asyncRes.cause()));
            }
        });
    }
}
