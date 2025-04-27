package pcd.ass02.model;

import java.io.File;
import java.util.Arrays;

public class MyJavaUtil {

    public static final String JAVA_EXTENSION = ".java";

    public static boolean isProject(String targetPath) {
        File file = new File(targetPath);
        return file.isDirectory()
                && Arrays.stream(file.listFiles()).anyMatch(File::isDirectory);
    }

    public static boolean isPackage(String targetPath) {
        File file = new File(targetPath);
        return file.isDirectory()
                && Arrays.stream(file.listFiles()).allMatch(f -> isJavaFile(f.getPath()));
    }

    public static boolean isJavaFile(String targetPath) {
        File file = new File(targetPath);
        return file.isFile() && targetPath.contains(JAVA_EXTENSION);
    }
}
