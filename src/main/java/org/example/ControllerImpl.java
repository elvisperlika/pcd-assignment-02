package org.example;

import org.example.model.Model;
import org.example.view.View;

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
        model.execute();
    }
}
