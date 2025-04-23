package pcd.ass02.view;

import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.PackageReport;
import pcd.ass02.model.report.ProjectReport;
import pcd.ass02.model.report.Report;

public class ViewImpl implements View {

    @Override
    public void showDependencies(Report report) {
        if (report instanceof ClassReport classReport) {
            printClassReport(classReport);
        } else if (report instanceof PackageReport packageReport) {
            printPackageReport(packageReport);
        } else if (report instanceof ProjectReport projectReport) {
            printProjectReport(projectReport);
        }
    }

    private void printProjectReport(ProjectReport projectReport) {
        System.out.println(" - " + projectReport.getProjectName());
    }

    private void printPackageReport(PackageReport packageReport) {
        System.out.println(" - - " + packageReport.getPackageName());
        packageReport.getClassReportList().forEach(this::printClassReport);
    }

    private void printClassReport(ClassReport classReport) {
        System.out.println(" -> " + classReport + " = ");
        classReport.getDependencyList().forEach(classDependency -> {
            System.out.println(" - - - " + classDependency);
        });
    }
}
