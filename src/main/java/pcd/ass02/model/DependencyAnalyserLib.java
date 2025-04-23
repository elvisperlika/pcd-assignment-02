package pcd.ass02.model;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.impl.future.PromiseImpl;
import pcd.ass02.model.report.*;

import java.util.List;

public class DependencyAnalyserLib {

    public static void getClassDependencies(String classPath, Handler<Promise<ClassReport>> promiseHandler) {
        Promise<ClassReport> reportPromise = new PromiseImpl<>();
        ClassReport classReportImpl = new ClassReportImpl(getLastPathElement(classPath));
        Vertx vertx = Vertx.vertx();
        FileSystem fileSystem = vertx.fileSystem();
        fileSystem.readFile(classPath, res -> {
            if (res.succeeded()) {
                CompilationUnit compilationUnit = StaticJavaParser.parse(res.result().toString());
                List<ClassOrInterfaceType> classOrInterfaceTypes = compilationUnit.findAll(ClassOrInterfaceType.class);
                classOrInterfaceTypes.stream().distinct().forEach(node -> {
                            classReportImpl.addDependency(node.getNameAsString());
                        }
                );
                reportPromise.complete(classReportImpl);
            } else {
                reportPromise.fail(res.cause());
            }
            promiseHandler.handle(reportPromise);
        });
    }

    private static String getLastPathElement(String classPath) {
        return classPath.substring(classPath.lastIndexOf("/") + 1);
    }

    public static void getPackageDependencies(String packagePath, Handler<Promise<PackageReport>> promiseHandler) {
        Promise<PackageReport> reportPromise = new PromiseImpl<>();
        PackageReport packageReportImpl = new PackageReportImpl(getLastPathElement(packagePath));
        Vertx vertx = Vertx.vertx();
        FileSystem fileSystem = vertx.fileSystem();
        fileSystem.readDir(packagePath, res -> {
            if (res.succeeded()) {
                res.result().forEach(file -> {
                    getClassDependencies(file, promise -> {
                        if (promise.future().succeeded()) {
                            packageReportImpl.addOnReportList(promise.future().result());
                        } else {
                            reportPromise.fail("A class analyse fail: " + promise.future().cause());
                        }
                    });
                });
                reportPromise.complete(packageReportImpl);
            } else {
                reportPromise.fail("Fail: " + res.cause());
            }
            promiseHandler.handle(reportPromise);
        });
    }

    public static void getProjectDependencies(String projectFolder) {
    }
}
