package pcd.ass02.model;

import pcd.ass02.model.report.ClassReport;
import pcd.ass02.model.report.PackageReport;
import pcd.ass02.model.report.Report;

public class Model {

    private final TargetType targetType;
    private final String target;
    private Report report;

    public Model(TargetType targetType, String targetPath) {
        this.targetType = targetType;
        this.target = targetPath;
    }

    public void exploreDependencies() {
        switch (targetType) {
            case CLASS -> DependencyAnalyserLib.getClassDependencies(target, reportPromise -> {
                if (reportPromise.future().succeeded()) {
                    ClassReport report = reportPromise.future().result();
                    System.out.println(" - - - " + report.getClassName() + ": ");
                    printClassDependencies(report);
                } else {
                    System.out.println(reportPromise.future().cause().toString());
                }
            });
            case PACKAGE -> DependencyAnalyserLib.getPackageDependencies(target, reportPromise -> {
                if (reportPromise.future().succeeded()) {
                    PackageReport report = reportPromise.future().result();
                    System.out.println(" - " + report.getPackageName() + ": ");
                    report.getClassReportList().forEach(this::printClassDependencies);
                }
            });
        };
    }

    private void printClassDependencies(ClassReport report) {
        report.getDependencyList().forEach(dep -> {
            System.out.println(" - - - - - " + dep);
        });
    }

    public Report getReport() {
        return report;
    }
}
