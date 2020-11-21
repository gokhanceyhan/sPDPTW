package common;

public class Driver {

    private int capacity;
    private Location endLocation;
    private int id;
    private Location startLocation;
    private int timeWindowEnd;
    private int timeWindowStart;

    public Driver() {
        this.endLocation = new Location();
        this.startLocation = new Location();
    }

    public Driver(int capacity, int id, Location startLocation, int timeWindowEnd, int timeWindowStart) {
        this.capacity = capacity;
        this.id = id;
        this.startLocation = startLocation;
        this.timeWindowEnd = timeWindowEnd;
        this.timeWindowStart = timeWindowStart;
    }

    public Driver(
            int capacity, Location endLocation, int id, Location startLocation, int timeWindowEnd,
            int timeWindowStart) {
        this.capacity = capacity;
        this.endLocation = endLocation;
        this.id = id;
        this.startLocation = startLocation;
        this.timeWindowEnd = timeWindowEnd;
        this.timeWindowStart = timeWindowStart;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
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
