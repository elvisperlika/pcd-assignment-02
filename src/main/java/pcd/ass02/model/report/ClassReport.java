package pcd.ass02.model.report;

import java.util.List;

public interface ClassReport extends Report {

    void addDependency(String nameAsString);

    List<String> getDependencyList();

    String getClassName();
}
