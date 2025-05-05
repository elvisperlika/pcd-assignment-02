package pcd.ass02.view;

import pcd.ass02.model.rx.report.ReactClassReport;

public interface View {

    void update(ReactClassReport dependencyName);

    void setFullScreen();
}
