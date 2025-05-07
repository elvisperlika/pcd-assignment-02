package pcd.ass02.view;

public class MyNode {
    private final String className;
    private int x;
    private int y;

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

    public void setNewPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
