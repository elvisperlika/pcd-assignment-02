package pcd.ass02;

import pcd.ass02.view.App;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path CLASS_PATH = Paths.get("src/main/java/pcd/ass02/model/MyJavaUtil.java");
    private static final Path PACKAGE_PATH = Paths.get("src/main/java/pcd/ass02/model/vertx/report");
    private static final Path PROJECT_PATH = Paths.get("/Users/elvisperlika/Universita/Magistrale/java-design-patterns");
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public static void main(String[] args) {
        new App(WIDTH, HEIGHT);
    }
}