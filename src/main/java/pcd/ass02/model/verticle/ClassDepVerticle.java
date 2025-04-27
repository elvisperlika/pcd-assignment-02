package pcd.ass02.model.verticle;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.sun.source.tree.Tree;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.report.ClassReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ClassDepVerticle extends AbstractVerticle {

    private final String classTargetPath;
    private final ClassReport classReport;

public ClassDepVerticle(String classTargetPath, ClassReport classReport) {
        this.classTargetPath = classTargetPath;
        this.classReport = classReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File classFile = new File(classTargetPath);
        CompilationUnit compilationUnit;
        try {
            compilationUnit = StaticJavaParser.parse(classFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<ClassOrInterfaceType> classOrInterfaceTypes = compilationUnit.findAll(ClassOrInterfaceType.class);
        classOrInterfaceTypes.stream().distinct().forEach(node -> {
            classReport.addDependency(node.getNameAsString() + MyJavaUtil.JAVA_EXTENSION);
        });

        promise.complete();
    }
}
