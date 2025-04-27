package pcd.ass02.model.report;

import java.util.List;

public interface ProjectReport {

    @Override
    String toString();

    void setClassReportList(List<ClassReport> classReportList);

    void setPackageReportList(List<PackageReport> packageReportList);

    void setSubProjectReportList(List<ProjectReport> projectReportList);

    void show();
}
