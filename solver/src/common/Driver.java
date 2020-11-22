package common;

public class Driver {

    public static final double AVERAGE_SPEED_IN_KM_PER_HOUR = 15;

    private int capacity;
    private Location endLocation;
    private int id;
    private Location startLocation;
    private TimeWindow timeWindow;

    public Driver() {
        this.endLocation = new Location();
        this.startLocation = new Location();
        this.timeWindow = new TimeWindow();
    }

    public Driver(int capacity, int id, Location startLocation, TimeWindow timeWindow) {
        this.capacity = capacity;
        this.id = id;
        this.startLocation = startLocation;
        this.timeWindow = timeWindow;
    }

    public Driver(
            int capacity, Location endLocation, int id, Location startLocation, TimeWindow timeWindow) {
        this.capacity = capacity;
        this.endLocation = endLocation;
        this.id = id;
        this.startLocation = startLocation;
        this.timeWindow = timeWindow;
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

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }
}
