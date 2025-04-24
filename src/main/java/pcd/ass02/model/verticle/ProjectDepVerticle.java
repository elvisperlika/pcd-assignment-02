package pcd.ass02.model.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import pcd.ass02.model.report.ProjectReport;

import java.io.File;
import java.util.function.Supplier;

public class ProjectDepVerticle extends AbstractVerticle {
    private final String projectTargetPath;
    private final ProjectReport projectReport;

    public ProjectDepVerticle(String projectTargetPath, ProjectReport projectReport) {
        this.projectTargetPath = projectTargetPath;
        this.projectReport = projectReport;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        File projectFile = new File(projectTargetPath);

    }
}
