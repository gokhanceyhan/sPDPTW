package common;

import java.util.Objects;

public class Task {

    public static final int SERVICE_TIME_IN_SECONDS = 0;

    private Location location;
    private int numItems;
    private int orderId;
    private TimeWindow timeWindow;
    private TaskType type;

    public Task(TaskType type) {
        this.location = new Location();
        this.type = type;
        this.timeWindow = new TimeWindow();
    }

    public Task(Location location, int numItems, int orderId, TaskType type) {
        this.location = location;
        this.numItems = numItems;
        this.orderId = orderId;
        this.type = type;
    }

    public Task(Location location, int numItems, int orderId, TimeWindow timeWindow, TaskType type) {
        this.location = location;
        this.numItems = numItems;
        this.orderId = orderId;
        this.timeWindow = timeWindow;
        this.type = type;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getOrderId() == task.getOrderId() && getType() == task.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getType());
    }
}
