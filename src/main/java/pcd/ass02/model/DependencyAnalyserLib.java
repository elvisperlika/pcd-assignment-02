package pcd.ass02.model;

import io.vertx.core.*;
import pcd.ass02.model.report.*;
import pcd.ass02.model.verticle.ClassDepVerticle;
import pcd.ass02.model.verticle.PackageDepVerticle;
import pcd.ass02.model.verticle.ProjectDepVerticle;

public class DependencyAnalyserLib {

    public static Future<ClassReport> getClassDependency(String classTargetPath) {
        Vertx vertx = Vertx.vertx();
        ClassReport classReport = new ClassReportImpl(classTargetPath);
        Promise<ClassReport> promise = Promise.promise();
        vertx.deployVerticle(new ClassDepVerticle(classTargetPath, classReport)).onSuccess(s -> {
            promise.complete(classReport);
        }).onFailure(throwable -> {
            promise.fail("Class Fail:");
            throwable.printStackTrace();
        });
        return promise.future();
    }

    public static Future<PackageReport> getPackageDependency(String packageTargetPath) {
        Vertx vertx = Vertx.vertx();
        PackageReport packageReport = new PackageReportImpl(packageTargetPath);
        Promise<PackageReport> promise = Promise.promise();
        vertx.deployVerticle(new PackageDepVerticle(packageTargetPath, packageReport)).onSuccess(s -> {
            promise.complete(packageReport);
        }).onFailure(throwable -> {
            promise.fail("Package Fail:");
            throwable.printStackTrace();
        });
        return promise.future();
    }

    public static Future<ProjectReport> getProjectDependency(String projectTargetPath) {
        Vertx vertx = Vertx.vertx();
        ProjectReport projectReport = new ProjectReportImpl(projectTargetPath);
        Promise<ProjectReport> promise = Promise.promise();
        vertx.deployVerticle(new ProjectDepVerticle(projectTargetPath, projectReport)).onSuccess(s -> {
            promise.complete(projectReport);
        }).onFailure(throwable -> {
            promise.fail("Project Fail: ");
            throwable.printStackTrace();
        });
        return promise.future();
    }
}
