package pcd.ass02.model.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.Future;
import pcd.ass02.model.MyJavaUtil;
import pcd.ass02.model.rx.report.ReactClassReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependencyAnalyserReactiveLib {

    public static Observable<ReactClassReport> getClassDependency(String classPath) {
        return Observable.create(emitter -> {
            File classFile = new File(classPath);
            CompilationUnit compilationUnit;
            try {
                compilationUnit = StaticJavaParser.parse(classFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            List<ClassOrInterfaceType> classOrInterfaceTypes = compilationUnit.findAll(ClassOrInterfaceType.class);
            classOrInterfaceTypes.stream().distinct().forEach(node -> {
                emitter.onNext(new ReactClassReport(classFile.getName(),
                        node.getNameAsString() + MyJavaUtil.JAVA_EXTENSION));
            });

            List<ImportDeclaration> importDeclarations = compilationUnit.getImports();
            importDeclarations.forEach(importDeclaration -> {
                String type;
                if (importDeclaration.isAsterisk()) {
                    type = " (all package) ";
                } else {
                    type = " (import) ";
                }
                emitter.onNext(new ReactClassReport(classFile.getName(),
                        importDeclaration.getNameAsString() + type));
            });
            emitter.onComplete();
        });
    }

    public static Observable<ReactClassReport> getPackageDependency(String packagePath) {
        File packageFile = new File(packagePath);
        List<File> files = Arrays.stream(packageFile.listFiles()).toList();
        Observable<ReactClassReport>[] arrayList = new Observable[files.size()];

        if (files.isEmpty()) {
            return Observable.create(Emitter::onComplete);
        } else {
            int i = 0;
            for (File file : files) {
                Observable<ReactClassReport> source = DependencyAnalyserReactiveLib.getClassDependency(file.getPath());
                arrayList[i] = source;
                i++;
            }
        }
        return Observable.mergeArray(arrayList);
    }
}
