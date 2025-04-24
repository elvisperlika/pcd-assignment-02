package pcd.ass02.model.report;

import java.util.List;

public interface ProjectReport {

    String getProjectName();

    @Override
    String toString();

    void addToClassReportList(ClassReport classReport);

    void addToPackageReportList(PackageReport packageReport);

    List<ClassReport> getClassReportList();

    List<PackageReport> getPackageReportList();
}
