package output;

import algorithms.OrderInsertion;
import common.*;
import exceptions.InfeasibleRouteException;
import utilities.DistanceUtilities;
import utilities.TimeUtilities;

import java.util.*;

public class Route {

    private double cost;
    private List<Double> cumulativeDistances;
    private List<Double> cumulativeTravelTimes;
    private double distanceTravelled;
    private Driver driver;
    private Map<Integer, Double> lateDeliveredOrderId2delay;
    private List<Integer> driverLoads;
    private Map<Integer, Integer> orderId2deliveryTaskIndex;
    private Map<Integer, Integer> orderId2pickupTaskIndex;
    private List<Integer> orderIds;
    private List<Double> taskCompletionTimes;
    private List<Task> tasks;
    private double travelTime;

    public Route(Driver driver) {
        this.cumulativeDistances = new ArrayList<>();
        this.cumulativeTravelTimes = new ArrayList<>();
        this.driver = driver;
        this.lateDeliveredOrderId2delay = new HashMap<>();
        this.driverLoads = new ArrayList<>();
        this.orderId2deliveryTaskIndex = new HashMap<>();
        this.orderId2pickupTaskIndex = new HashMap<>();
        this.orderIds = new ArrayList<>();
        this.taskCompletionTimes = new ArrayList<>();
        this.tasks = new LinkedList<>();
    }

    public Route(Driver driver, List<Task> tasks) throws InfeasibleRouteException {
        this.driver = driver;
        this.tasks = tasks;
        registerTasks();
        schedule();
    }

    public Route(Route route){
        this.cost = route.getCost();
        this.cumulativeDistances = new ArrayList<>(route.cumulativeDistances);
        this.cumulativeTravelTimes = new ArrayList<>(route.getCumulativeTravelTimes());
        this.distanceTravelled = route.getDistanceTravelled();
        this.driver = route.getDriver();
        this.lateDeliveredOrderId2delay = new HashMap<>(route.getLateDeliveredOrderId2delay());
        this.driverLoads = new ArrayList<>(route.getDriverLoads());
        this.orderId2deliveryTaskIndex = new HashMap<>(route.getOrderId2deliveryTaskIndex());
        this.orderId2pickupTaskIndex = new HashMap<>(route.getOrderId2pickupTaskIndex());
        this.orderIds = new ArrayList<>(route.getOrderIds());
        this.taskCompletionTimes = new ArrayList<>(route.getTaskCompletionTimes());
        this.tasks = new LinkedList<>(route.getTasks());
        this.travelTime = route.getTravelTime();
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Double> getCumulativeDistances() {
        return cumulativeDistances;
    }

    public void setCumulativeDistances(List<Double> cumulativeDistances) {
        this.cumulativeDistances = cumulativeDistances;
    }

    public List<Double> getCumulativeTravelTimes() {
        return cumulativeTravelTimes;
    }

    public void setCumulativeTravelTimes(List<Double> cumulativeTravelTimes) {
        this.cumulativeTravelTimes = cumulativeTravelTimes;
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

    public List<Integer> getDriverLoads() {
        return driverLoads;
    }

    public void setDriverLoads(List<Integer> driverLoads) {
        this.driverLoads = driverLoads;
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

    private void evaluate(RouteCostFunction costFunction){
        double cost = costFunction.calculateCost(this);
        this.setCost(cost);
    }

    private void registerTasks() throws InfeasibleRouteException {
        Map<Integer, Integer> orderId2deliveryTaskIndex = new HashMap<>();
        Map<Integer, Integer> orderId2pickupTaskIndex = new HashMap<>();
        List<Integer> orderIds = new ArrayList<>();
        List<Integer> driverLoads = new ArrayList<>();
        int driverLoad = 0;
        for (Task task : this.getTasks()) {
            int orderId = task.getOrderId();
            int taskIndex = tasks.indexOf(task);
            driverLoad += task.getNumItems();
            if (task.getType().equals(TaskType.PICKUP)) {
                if (orderIds.contains(orderId))
                    throw new InfeasibleRouteException(
                            String.format("Order %s is served multiple times", orderId));
                orderIds.add(orderId);
                orderId2pickupTaskIndex.put(orderId, taskIndex);
                if (driverLoad > this.getDriver().getCapacity())
                    throw new InfeasibleRouteException(
                            String.format("The driver capacity exceeded: Capacity: %d, " +
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
            driverLoads.add(driverLoad);
        }
        this.setDriverLoads(driverLoads);
        this.setOrderIds(orderIds);
        this.setOrderId2pickupTaskIndex(orderId2pickupTaskIndex);
        this.setOrderId2deliveryTaskIndex(orderId2deliveryTaskIndex);
    }

    private void schedule(){
        Location startLocation = this.getDriver().getStartLocation();
        int earliestStartTime = this.getDriver().getTimeWindow().getStart();
        double totalDistanceTravelled = 0;
        double totalTravelTime = 0;
        List<Double> cumulativeDistances = new ArrayList<>();
        List<Double> cumulativeTravelTimes = new ArrayList<>();
        List<Double> taskCompletionTimes = new ArrayList<>();
        Map<Integer, Double> lateDeliveredOrderId2delay = new HashMap<>();
        double previousTaskCompletionTime = earliestStartTime;
        Location previousLocation = new Location(startLocation);
        for (Task task: this.getTasks()) {
            int timeWindowStart = task.getTimeWindow().getStart();
            int timeWindowEnd = task.getTimeWindow().getEnd();
            double distance = DistanceUtilities.distanceInKm(previousLocation, task.getLocation());
            totalDistanceTravelled += distance;
            cumulativeDistances.add(totalDistanceTravelled);
            double travelTime = TimeUtilities.travelTimeInSeconds(distance, Driver.AVERAGE_SPEED_IN_KM_PER_HOUR);
            totalTravelTime += travelTime;
            cumulativeTravelTimes.add(totalTravelTime);
            double taskStartTime = previousTaskCompletionTime + travelTime < timeWindowStart ? timeWindowStart :
                    previousTaskCompletionTime + travelTime;
            double taskCompletionTime = taskStartTime + Task.SERVICE_TIME_IN_SECONDS;
            taskCompletionTimes.add(taskCompletionTime);
            if (task.getType().equals(TaskType.DELIVERY) & taskCompletionTime > timeWindowEnd)
                lateDeliveredOrderId2delay.put(task.getOrderId(), taskCompletionTime - timeWindowEnd);
            previousLocation = task.getLocation();
            previousTaskCompletionTime = taskCompletionTime;
        }
        this.setCumulativeDistances(cumulativeDistances);
        this.setCumulativeTravelTimes(cumulativeTravelTimes);
        this.setDistanceTravelled(totalDistanceTravelled);
        this.setTravelTime(totalTravelTime);
        this.setTaskCompletionTimes(taskCompletionTimes);
        this.setLateDeliveredOrderId2delay(lateDeliveredOrderId2delay);
    }

    public void insert(Order order, OrderInsertion orderInsertion, RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        this.getTasks().add(orderInsertion.getPickUpTaskIndex(), order.getPickup());
        this.getTasks().add(orderInsertion.getDeliveryTaskIndex(), order.getDelivery());
        this.registerTasks();
        this.schedule();
        this.evaluate(costFunction);
    }
}
