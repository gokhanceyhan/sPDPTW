package output;

import common.*;
import exceptions.InfeasibleRouteException;
import utilities.DistanceUtilities;
import utilities.TimeUtilities;

import java.util.*;

public class Route {

    private double distanceTravelled;
    private Driver driver;
    private Map<Integer, Double> lateDeliveredOrderId2delay;
    private Map<Integer, Integer> orderId2deliveryTaskIndex;
    private Map<Integer, Integer> orderId2pickupTaskIndex;
    private List<Integer> orderIds;
    private List<Double> taskCompletionTimes;
    private List<Task> tasks;
    private double travelTime;

    public Route(Driver driver, List<Task> tasks) throws InfeasibleRouteException {
        this.driver = driver;
        this.tasks = tasks;
        registerTasks();
        schedule();
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Map<Integer, Double> getLateDeliveredOrderId2delay() {
        return lateDeliveredOrderId2delay;
    }

    public void setLateDeliveredOrderId2delay(Map<Integer, Double> lateDeliveredOrderId2delay) {
        this.lateDeliveredOrderId2delay = lateDeliveredOrderId2delay;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public Map<Integer, Integer> getOrderId2deliveryTaskIndex() {
        return orderId2deliveryTaskIndex;
    }

    public void setOrderId2deliveryTaskIndex(Map<Integer, Integer> orderId2deliveryTaskIndex) {
        this.orderId2deliveryTaskIndex = orderId2deliveryTaskIndex;
    }

    public Map<Integer, Integer> getOrderId2pickupTaskIndex() {
        return orderId2pickupTaskIndex;
    }

    public void setOrderId2pickupTaskIndex(Map<Integer, Integer> orderId2pickupTaskIndex) {
        this.orderId2pickupTaskIndex = orderId2pickupTaskIndex;
    }

    public List<Double> getTaskCompletionTimes() {
        return taskCompletionTimes;
    }

    public void setTaskCompletionTimes(List<Double> taskCompletionTimes) {
        this.taskCompletionTimes = taskCompletionTimes;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    private void registerTasks() throws InfeasibleRouteException {
        Map<Integer, Integer> orderId2deliveryTaskIndex = new HashMap<>();
        Map<Integer, Integer> orderId2pickupTaskIndex = new HashMap<>();
        List<Integer> orderIds = new ArrayList<>();

        int driverLoad = 0;
        for (Task task : this.getTasks()) {
            int orderId = task.getOrderId();
            int taskIndex = tasks.indexOf(task);

            if (task.getType().equals(TaskType.PICKUP)) {
                if (orderIds.contains(orderId))
                    throw new InfeasibleRouteException(String.format("Order %s is served multiple times", orderId));
                orderIds.add(orderId);
                orderId2pickupTaskIndex.put(orderId, taskIndex);
                if (driverLoad > this.getDriver().getCapacity())
                    throw new InfeasibleRouteException(String.format("The driver capacity exceeded: Capacity: %d, " +
                            "Load after picking up order %d is: %d", this.getDriver().getCapacity(), orderId,
                            driverLoad));
            }
            else {
                if (orderId2pickupTaskIndex.containsKey(orderId))
                    orderId2deliveryTaskIndex.put(orderId, taskIndex);
                else
                    throw new InfeasibleRouteException(
                            String.format("Order %d must be picked up before its delivery", orderId));
            }
            driverLoad += task.getNumItems();
        }
        this.setOrderIds(orderIds);
        this.setOrderId2pickupTaskIndex(orderId2pickupTaskIndex);
        this.setOrderId2deliveryTaskIndex(orderId2deliveryTaskIndex);
    }

    private void schedule(){
        Location startLocation = this.getDriver().getStartLocation();
        int earliestStartTime = this.getDriver().getTimeWindow().getStart();

        double totalDistanceTravelled = 0;
        double totalTravelTime = 0;
        List<Double> taskCompletionTimes = new ArrayList<>();
        Map<Integer, Double> lateDeliveredOrderId2delay = new HashMap<>();

        int previousTaskCompletionTime = earliestStartTime;
        Location previousLocation = new Location(startLocation);
        for (Task task: this.getTasks()) {
            int timeWindowStart = task.getTimeWindow().getStart();
            int timeWindowEnd = task.getTimeWindow().getEnd();
            double distance = DistanceUtilities.distanceInKm(previousLocation, task.getLocation());
            totalDistanceTravelled += distance;
            double travelTime = TimeUtilities.travelTimeInSeconds(distance, Driver.AVERAGE_SPEED_IN_KM_PER_HOUR);
            totalTravelTime += travelTime;
            double taskStartTime = previousTaskCompletionTime + travelTime < timeWindowStart ? timeWindowStart :
                    previousTaskCompletionTime + travelTime;
            double taskCompletionTime = taskStartTime + Task.SERVICE_TIME_IN_SECONDS;
            taskCompletionTimes.add(taskCompletionTime);
            if (task.getType().equals(TaskType.DELIVERY) & taskCompletionTime > timeWindowEnd)
                lateDeliveredOrderId2delay.put(task.getOrderId(), taskCompletionTime - timeWindowEnd);
        }

        this.setDistanceTravelled(totalDistanceTravelled);
        this.setTravelTime(totalTravelTime);
        this.setTaskCompletionTimes(taskCompletionTimes);
        this.setLateDeliveredOrderId2delay(lateDeliveredOrderId2delay);
    }
}
