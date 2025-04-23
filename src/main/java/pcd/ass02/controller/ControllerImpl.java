package pcd.ass02.controller;

import pcd.ass02.model.Model;
import pcd.ass02.view.View;

public class ControllerImpl implements Controller {
    private final Model model;
    private View view;

    public ControllerImpl(Model model) {
        this.model = model;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void start() {
        model.exploreDependencies();
        view.showDependencies(model.getReport());
    }
}
