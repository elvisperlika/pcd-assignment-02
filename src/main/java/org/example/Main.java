package org.example;

import org.example.model.Model;
import org.example.view.View;
import org.example.view.ViewImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String TARGET_PATH = "src/main/java/org/example/target/";

    public static void main(String[] args) {
        Model model = new Model(TARGET_PATH);
        View view = new ViewImpl();
        Controller controller = new ControllerImpl(model);
        controller.attachView(view);
        controller.start();
    }
}