package pcd.ass02.controller;

import pcd.ass02.model.report.Report;
import pcd.ass02.view.View;

public interface Controller {

    void attachView(View view);

    void start();

}
