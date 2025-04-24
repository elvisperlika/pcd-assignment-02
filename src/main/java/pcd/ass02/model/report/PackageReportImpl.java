package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class PackageReportImpl implements PackageReport {
    private final String packageName;
    private final List<ClassReport> classReportList;

    public PackageReportImpl(String packageName) {
        this.packageName = packageName;
        classReportList = new ArrayList<>();
    }

    @Override
    public void addOnReportList(ClassReport result) {
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
        String myString = "* " + packageName;
        for (ClassReport classReport : classReportList) {
            myString = myString.concat(classReport.toString());
        }
        return myString;
    }

}
