package pcd.ass02.model.report;

import java.util.List;

public interface PackageReport extends Report {

    void addOnReportList(ClassReport result);

    List<ClassReport> getClassReportList();

    String getPackageName();
}
