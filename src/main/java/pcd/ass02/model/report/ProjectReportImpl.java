package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class ProjectReportImpl implements ProjectReport {

    private final String projectName;
    private final List<ClassReport> classReportList = new ArrayList<>();
    private final List<PackageReport> packageReportList = new ArrayList<>();

    public ProjectReportImpl(String projectPath) {
        this.projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void addToClassReportList(ClassReport classReport) {
        classReportList.add(classReport);
    }

    @Override
    public void addToPackageReportList(PackageReport packageReport) {
        packageReportList.add(packageReport);
    }

    @Override
    public List<ClassReport> getClassReportList() {
        return new ArrayList<>(classReportList);
    }

    @Override
    public List<PackageReport> getPackageReportList() {
        return new ArrayList<>(packageReportList);
    }
}
