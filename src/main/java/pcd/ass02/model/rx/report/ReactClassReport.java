package pcd.ass02.model.rx.report;

public class ReactClassReport {
    private final String source;
    private final String destination;

    public ReactClassReport(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }
}
