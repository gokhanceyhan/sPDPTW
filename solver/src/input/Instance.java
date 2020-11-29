package input;

import common.*;
import utilities.DistanceUtilities;
import utilities.TimeUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instance {

    private List<Driver> drivers;
    private List<Order> orders;

    private Map<Integer, Integer> driverId2startLocationId;
    private Map<Task, Integer> task2locationId;

    private Map<ArcId, Arc> arcId2arc;

    public Instance(List<Driver> drivers, List<Order> orders) {
        this.drivers = drivers;
        this.orders = orders;
        initialize();
    }

    public Arc getArc(Task fromTask, Task toTask){
        ArcId arcId = new ArcId(this.getTask2locationId().get(fromTask), this.getTask2locationId().get(toTask));
        return this.getArcId2arc().get(arcId);
    }

    public Arc getArc(int driverId, Task toTask){
        ArcId arcId = new ArcId(
                this.getDriverId2startLocationId().get(driverId), this.getTask2locationId().get(toTask));
        return this.getArcId2arc().get(arcId);
    }

    private void initialize(){
        Map<Integer, Location> locationId2location = new HashMap<>();
        int locationId = 0;

        Map<Integer, Integer> driverId2startLocationId = new HashMap<>();
        for (Driver driver : this.getDrivers()) {
            driverId2startLocationId.put(driver.getId(), locationId);
            locationId2location.put(locationId, driver.getStartLocation());
            locationId ++;
        }

        Map<Task, Integer> task2locationId = new HashMap<>();
        for (Order order : this.getOrders()){
            Task pickup = order.getPickup();
            task2locationId.put(pickup, locationId);
            locationId2location.put(locationId, pickup.getLocation());
            locationId ++;

            Task delivery = order.getDelivery();
            task2locationId.put(delivery, locationId);
            locationId2location.put(locationId, delivery.getLocation());
            locationId ++;
        }

        Map<ArcId, Arc> arcId2arc = new HashMap<>();
        for (Map.Entry<Integer, Location> fromLocationEntry : locationId2location.entrySet()){
            int fromLocationId = fromLocationEntry.getKey();
            Location fromLocation = fromLocationEntry.getValue();
            for (Map.Entry<Integer, Location> toLocationEntry : locationId2location.entrySet()){
                int toLocationId = toLocationEntry.getKey();
                if (fromLocationId == toLocationId)
                    continue;
                Location toLocation = toLocationEntry.getValue();
                double distance = DistanceUtilities.distanceInKm(fromLocation, toLocation);
                double travelTime = TimeUtilities.travelTimeInSeconds(distance, Driver.AVERAGE_SPEED_IN_KM_PER_HOUR);
                ArcId arcId = new ArcId(fromLocationId, toLocationId);
                Arc arc = new Arc(arcId, distance, travelTime);
                arcId2arc.put(arcId, arc);
            }
        }
        this.setDriverId2startLocationId(driverId2startLocationId);
        this.setTask2locationId(task2locationId);
        this.setArcId2arc(arcId2arc);
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Map<Integer, Integer> getDriverId2startLocationId() {
        return driverId2startLocationId;
    }

    public void setDriverId2startLocationId(Map<Integer, Integer> driverId2startLocationId) {
        this.driverId2startLocationId = driverId2startLocationId;
    }

    public Map<Task, Integer> getTask2locationId() {
        return task2locationId;
    }

    public void setTask2locationId(Map<Task, Integer> task2locationId) {
        this.task2locationId = task2locationId;
    }

    public Map<ArcId, Arc> getArcId2arc() {
        return arcId2arc;
    }

    public void setArcId2arc(Map<ArcId, Arc> arcId2arc) {
        this.arcId2arc = arcId2arc;
    }
}
