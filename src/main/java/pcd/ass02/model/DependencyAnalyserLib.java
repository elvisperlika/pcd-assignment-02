package pcd.ass02.model;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;

import java.util.List;

public class DependencyAnalyserLib {

    public static void getClassDependencies(String file) {
        Vertx vertx = Vertx.vertx();
        FileSystem fileSystem = vertx.fileSystem();
        fileSystem.readFile(file, res -> {
            if (res.succeeded()) {
                CompilationUnit compilationUnit = StaticJavaParser.parse(res.result().toString());
                List<ClassOrInterfaceType> all = compilationUnit.findAll(ClassOrInterfaceType.class);
                all.stream().distinct().forEach(node ->
                        System.out.println(node.getNameAsString()));
            } else {
                System.out.println("Fail: " + res.cause());
            }
        });
    }

    public static void getPackageDependencies(String packageFolder) {
        Vertx vertx = Vertx.vertx();
        FileSystem fileSystem = vertx.fileSystem();
        fileSystem.readDir(packageFolder, res -> {
            if (res.succeeded()) {
                res.result().stream().
                        forEach(name -> {
                            if (name.contains(".java")) {
                                getClassDependencies(packageFolder + "/" + name);
                            } else {
                                getPackageDependencies(packageFolder + "/" + name.split(".")[0]);
                            }
                        });
            } else {
                System.out.println("fail: " + res.cause());
            }
        });
    }

    public static void getProjectDependencies(String projectFolder) {
    }
}
