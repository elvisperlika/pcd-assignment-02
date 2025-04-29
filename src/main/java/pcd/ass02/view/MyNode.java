package pcd.ass02.view;

public class MyNode {
    private final String className;
    private final int x;
    private final int y;

    public MyNode(String className, int x, int y) {
        this.className = className;
        this.x = x;
        this.y = y;
    }

    public String className() {
        return className;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
