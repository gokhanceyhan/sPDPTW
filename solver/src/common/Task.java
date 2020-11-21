package common;

public class Task {

    private Location location;
    private int numItems;
    private int timeWindowEnd;
    private int timeWindowStart;

    public Task() {
        this.location = new Location();
    }

    public Task(Location location, int numItems) {
        this.location = location;
        this.numItems = numItems;
    }

    public Task(Location location, int numItems, int timeWindowEnd, int timeWindowStart) {
        this.location = location;
        this.numItems = numItems;
        this.timeWindowEnd = timeWindowEnd;
        this.timeWindowStart = timeWindowStart;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public int getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public void setTimeWindowEnd(int timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public int getTimeWindowStart() {
        return timeWindowStart;
    }

    public void setTimeWindowStart(int timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }
}
