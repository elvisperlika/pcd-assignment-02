package pcd.ass02.model.report;

import java.util.List;

public interface ClassReport extends Report {

    void addClassOrInterfaceDependency(String nameAsString);

    List<String> getClassOrInterfaceDependencyList();

    String getClassName();

    @Override
    String toString();

    void show();

    void addImportDependency(String importDep);
}
