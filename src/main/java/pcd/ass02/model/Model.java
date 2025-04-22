package pcd.ass02.model;

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
        System.out.println("target: " + target);
        switch (targetType) {
            case CLASS -> DependencyAnalyserLib.getClassDependencies(target);
            case PACKAGE -> DependencyAnalyserLib.getPackageDependencies(target);
        };
    }
}
