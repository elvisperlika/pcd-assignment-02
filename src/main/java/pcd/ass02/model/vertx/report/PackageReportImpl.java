package pcd.ass02.model.vertx.report;

import java.util.ArrayList;
import java.util.List;

public class PackageReportImpl implements PackageReport {
    private final String packageName;
    private final List<ClassReport> classReportList;

    public PackageReportImpl(String packagePath) {
        this.packageName = packagePath.substring(packagePath.lastIndexOf("/") + 1);
        classReportList = new ArrayList<>();
    }

    @Override
    public void addInReportList(ClassReport result) {
        this.classReportList.add(result);
    }

    @Override
    public List<ClassReport> getClassReportList() {
        return new ArrayList<>(classReportList);
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" - PACKAGE: ").append(packageName).append("\n");
        for (ClassReport classReport : classReportList) {
            stringBuilder.append(classReport.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public void show() {
        System.out.println("   PACKAGE: " + packageName);
        classReportList.forEach(ClassReport::show);
    }

}
