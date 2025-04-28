package pcd.ass02.model.report;

import java.util.ArrayList;
import java.util.List;

public class ProjectReportImpl implements ProjectReport {

    private final String projectName;
    private List<ClassReport> classReportList = new ArrayList<>();
    private List<PackageReport> packageReportList = new ArrayList<>();
    private List<ProjectReport> subProjectReportList = new ArrayList<>();

    public ProjectReportImpl(String projectPath) {
        this.projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void setClassReportList(List<ClassReport> classReportList) {
        this.classReportList = new ArrayList<>(classReportList);
    }

    @Override
    public void setPackageReportList(List<PackageReport> packageReportList) {
        this.packageReportList = new ArrayList<>(packageReportList);
    }

    @Override
    public void setSubProjectReportList(List<ProjectReport> projectReportList) {
        this.subProjectReportList = new ArrayList<>(projectReportList);
    }

    @Override
    public void show() {
        System.out.println("PROJECT: " + projectName);
        showClasses(classReportList);
        packageReportList.forEach(PackageReport::show);
        subProjectReportList.forEach(ProjectReport::show);
    }

    private void showClasses(List<ClassReport> classReportList) {
        classReportList.forEach(classReport -> System.out.println(classReport.toString()));
    }

}
