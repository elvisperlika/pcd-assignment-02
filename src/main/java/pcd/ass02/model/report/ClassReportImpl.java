package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class ClassReportImpl implements ClassReport {
    private final String className;
    private final List<String> classOrInterfaceDependencyList;
    private final List<String> importDependencies;

    public ClassReportImpl(String classPath) {
        this.className = classPath.substring(classPath.lastIndexOf("/") + 1);
        this.classOrInterfaceDependencyList = new ArrayList<>();
        this.importDependencies = new ArrayList<>();
    }

    @Override
    public void addClassOrInterfaceDependency(String nameAsString) {
        this.classOrInterfaceDependencyList.add(nameAsString);
    }

    @Override
    public List<String> getClassOrInterfaceDependencyList() {
        return new ArrayList<>(classOrInterfaceDependencyList);
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CLASS: ").append(className).append("\n");
        for(String str : classOrInterfaceDependencyList) {
            stringBuilder.append(" -> ").append(str).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void addImportDependency(String importDep) {
        importDependencies.add(importDep);
    }

    @Override
    public void show() {
        System.out.println("      CLASS: " + className);
        for (String s : classOrInterfaceDependencyList) {
            System.out.println("         - "  + s);
        }
        for (String s : importDependencies) {
            System.out.println("         ~ " + s);
        }
    }
}
