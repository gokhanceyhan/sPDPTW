package common;

public class TimeWindow {

    private int end;
    private int start;

    public TimeWindow() {
        this.end = (int) Double.POSITIVE_INFINITY;
    }

    public TimeWindow(int end, int start) {
        this.end = end;
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
