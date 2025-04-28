package pcd.ass02;

import io.vertx.core.Future;
import pcd.ass02.model.DependencyAnalyserLib;
import pcd.ass02.model.report.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path TARGET_PATH = Paths.get("src/main/java/pcd/ass02/model/MyJavaUtil.java");

    public static void main(String[] args) {
        Future<ClassReport> future = DependencyAnalyserLib.getClassDependency(TARGET_PATH.toString());
        while (!future.isComplete()) {
            System.out.println("Future is not complete...");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (future.succeeded()) {
            future.result().show();
        } else {
            future.cause().printStackTrace();
        }
    }
}