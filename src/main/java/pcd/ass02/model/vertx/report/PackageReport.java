package pcd.ass02.model.vertx.report;

import java.util.List;

public interface PackageReport extends Report {

    void addInReportList(ClassReport result);

    List<ClassReport> getClassReportList();

    String getPackageName();

    @Override
    String toString();

    void show();
}
