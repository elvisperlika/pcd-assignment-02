package pcd.ass02.model.verticle;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.types.ResolvedType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.report.ClassReport;

import javax.naming.spi.ResolveResult;
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
            classReport.addClassOrInterfaceDependency(
                    node.getNameAsString() + MyJavaUtil.JAVA_EXTENSION + " (class or interface) ");
        });

        List<ImportDeclaration> importDeclarations = compilationUnit.getImports();
        importDeclarations.forEach(importDeclaration -> {
            String type;
            if (importDeclaration.isAsterisk()) {
                type = " (all package) ";
            } else {
                type = " (import) ";
            }
            classReport.addImportDependency(importDeclaration.getNameAsString() + type);
        });

        promise.complete();
    }
}
