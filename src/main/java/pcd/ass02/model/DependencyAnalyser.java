package pcd.ass02.model;

import io.vertx.core.*;
import pcd.ass02.model.report.*;
import pcd.ass02.model.verticle.ClassDepVerticle;
import pcd.ass02.model.verticle.PackageDepVerticle;
import pcd.ass02.model.verticle.ProjectDepVerticle;

public class DependencyAnalyser {

    public static void getClassDependency(String classTargetPath, ClassReport classReport, Handler<AsyncResult<ClassReport>> handler) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ClassDepVerticle(classTargetPath, classReport)).onSuccess(_ -> {
            handler.handle(Future.succeededFuture(classReport));
        }).onFailure(throwable -> {
            handler.handle(Future.failedFuture(throwable));
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

    /**
     *
     * @param projectTargetPath
     * @param handler
     */
    public static void getProjectDependency(String projectTargetPath, Handler<AsyncResult<ProjectReport>> handler) {
        Vertx vertx = Vertx.vertx();
        ProjectReport projectReport = new ProjectReportImpl(projectTargetPath);
        vertx.deployVerticle(new ProjectDepVerticle(projectTargetPath, projectReport), asyncRes -> {
            if (asyncRes.succeeded()) {
                System.out.println("OK");
                handler.handle(Future.succeededFuture(projectReport));
            } else {
                System.out.println("BAD");
                handler.handle(Future.failedFuture(asyncRes.cause()));
            }
        });
    }
}
