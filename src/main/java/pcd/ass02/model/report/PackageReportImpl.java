package pcd.ass02.model.report;

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
        StringBuilder myString = new StringBuilder();
        for (ClassReport classReport : classReportList) {
            myString.append(classReport.toString());
        }
        return myString.toString();
    }

}
