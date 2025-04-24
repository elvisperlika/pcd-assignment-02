package pcd.ass02.model.report;

public class ProjectReportImpl implements ProjectReport {

    private final String projectName;

    public ProjectReportImpl(String projectPath) {
        this.projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);
    }

    @Override
    public String getProjectName() {
        return projectName;
    }
}
