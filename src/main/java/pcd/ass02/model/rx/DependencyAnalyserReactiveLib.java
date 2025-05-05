package pcd.ass02.model.rx;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.rx.report.ReactClassReport;
import pcd.ass02.view.MyPanel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DependencyAnalyserReactiveLib {

    private static AtomicInteger n = new AtomicInteger(-1);

    public static Observable<ReactClassReport> getClassDependency(String classPath) {
        System.out.println(n.incrementAndGet());
        return Observable.create(emitter -> {
            File classFile = new File(classPath);

            // System.out.println("[" + Thread.currentThread().getName() + "] " + classFile.getName());

            ParserConfiguration config = new ParserConfiguration()
                    .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
            StaticJavaParser.setConfiguration(config);
            CompilationUnit compilationUnit = StaticJavaParser.parse(classFile);

            List<ClassOrInterfaceType> classOrInterfaceTypes = compilationUnit.findAll(ClassOrInterfaceType.class);
            classOrInterfaceTypes.stream().distinct().forEach(node -> {
                emitter.onNext(new ReactClassReport(classFile.getName(),
                        node.getNameAsString() + MyJavaUtil.JAVA_EXTENSION));
            });

            List<ImportDeclaration> importDeclarations = compilationUnit.getImports();
            importDeclarations.forEach(importDeclaration -> {
                String type;
                if (importDeclaration.isAsterisk()) {
                    type = " (all package)";
                } else {
                    type = " (import)";
                }
                emitter.onNext(new ReactClassReport(classFile.getName(),
                        importDeclaration.getNameAsString()));
            });
            emitter.onComplete();
        });
    }

    public static Observable<ReactClassReport> getPackageDependency(String packagePath) {
        File packageFile = new File(packagePath);
        List<File> javaFiles = Arrays.stream(packageFile.listFiles())
                .filter(file -> MyJavaUtil.isJavaFile(file.getPath()))
                .toList();

        if (javaFiles.isEmpty()) {
            return Observable.create(Emitter::onComplete);
        } else {
            return Observable
                    .fromStream(javaFiles.stream())
                    .flatMap(file -> {
                        // System.out.println("file -> " + file.getName());
                        return DependencyAnalyserReactiveLib.getClassDependency(file.getPath())
                                .subscribeOn(Schedulers.io());
                    });
        }
    }

    public static Observable<ReactClassReport> getProjectDependency(String projectPath) {
        File projectFile = new File(projectPath);
        List<File> files = Arrays.stream(projectFile.listFiles()).toList();
        if (files.isEmpty()) {
            return Observable.create(Emitter::onComplete);
        } else {
            return Observable
                    .fromStream(files.stream())
                    .flatMap(file -> {
                        if (MyJavaUtil.isJavaFile(file.getPath())) {
                            return DependencyAnalyserReactiveLib.getClassDependency(file.getPath());
                        } else if (MyJavaUtil.isPackage(file.getPath())) {
                            return DependencyAnalyserReactiveLib.getPackageDependency(file.getPath());
                        } else if (MyJavaUtil.isProject(file.getPath())) {
                            return DependencyAnalyserReactiveLib.getProjectDependency(file.getPath());
                        } else {
                            return Observable.create(Emitter::onComplete);
                        }
                    });
        }
    }

    public static int getNumberOfClassAnalysed() {
        return n.get();
    }
}
