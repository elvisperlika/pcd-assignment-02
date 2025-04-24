package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class ClassReportImpl implements ClassReport {
    private final String className;
    private final List<String> dependencyList;

    public ClassReportImpl(String className) {
        this.className = className.substring(className.lastIndexOf("/") + 1);
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

    @Override
    public String toString() {
        String myString = className + ": \n";
        for(String str : dependencyList) {
            myString = myString + " - " + str + "\n";
        }
        return myString;
    }
}
