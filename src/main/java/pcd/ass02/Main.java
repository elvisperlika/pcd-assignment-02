package pcd.ass02;

import pcd.ass02.controller.Controller;
import pcd.ass02.controller.ControllerImpl;
import pcd.ass02.model.Model;
import pcd.ass02.view.View;
import pcd.ass02.view.ViewImpl;
import pcd.ass02.model.TargetType;

public class Main {
    private static final String TARGET_PATH = "src/main";

    public static void main(String[] args) {
        Model model = new Model(TARGET_PATH);
        View view = new ViewImpl();
        Controller controller = new ControllerImpl(model);
        controller.attachView(view);
        controller.start();
    }
}