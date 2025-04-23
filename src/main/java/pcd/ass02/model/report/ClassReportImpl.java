package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class ClassReportImpl implements ClassReport {
    private final String className;
    private final List<String> dependencyList;

    public ClassReportImpl(String className) {
        this.className = className;
        dependencyList = new ArrayList<>();
    }

    @Override
    public void addDependency(String nameAsString) {
        this.dependencyList.add(nameAsString);
    }

    @Override
    public List<String> getDependencyList() {
        return new ArrayList<>(dependencyList);
    }

    @Override
    public String getClassName() {
        return className;
    }
}
