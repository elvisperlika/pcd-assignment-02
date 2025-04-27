package pcd.ass02.model.report;

import java.util.List;

public interface ProjectReport {

    String getProjectName();

    void addToClassReportList(ClassReport classReport);

    List<ClassReport> getClassReportList();

    @Override
    String toString();

}
