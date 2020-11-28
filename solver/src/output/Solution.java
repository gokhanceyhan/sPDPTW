package output;

import common.Order;
import exceptions.InfeasibleSolutionException;
import input.Instance;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    private double cost;
    private Map<Integer, Route> driverId2route;

    public Solution() {
        this.driverId2route = new HashMap<>();
        this.evaluate();
    }

    public Solution(Map<Integer, Route> driverId2route) {
        this.driverId2route = driverId2route;
    }

    public Solution (Solution solution){
        this.cost = solution.getCost();
        Map<Integer, Route> driverId2route = new HashMap<>();
        for (Map.Entry<Integer, Route> entry : solution.getDriverId2route().entrySet())
            driverId2route.put(entry.getKey(), new Route(entry.getValue()));
        this.driverId2route = driverId2route;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Map<Integer, Route> getDriverId2route() {
        return driverId2route;
    }

    public void setDriverId2route(Map<Integer, Route> driverId2route) {
        this.driverId2route = driverId2route;
    }

    public void updateRoute(int driverId, Route route){
        this.getDriverId2route().put(driverId, route);
    }

    public void evaluate(){
        double initialCost = 0.0;
        double cost = this.getDriverId2route().values().stream().map(Route::getCost).reduce(initialCost, Double:: sum);
        this.setCost(cost);
    }

    public void validate(Instance instance) throws InfeasibleSolutionException {
        List<Integer> assignedOrderIds = new ArrayList<>();
        for (Route route: this.getDriverId2route().values()) {
            for (Integer orderId: route.getOrderIds()) {
                if (assignedOrderIds.contains(orderId))
                    throw new InfeasibleSolutionException(
                            String.format("Order %d is served by multiple routes", orderId));
                assignedOrderIds.add(orderId);
            }
        }
        if (instance.getOrders().size() > assignedOrderIds.size()){
            List<Integer> unAssignedOrderIds = new ArrayList<>();
            for (Order order : instance.getOrders()){
                if (!assignedOrderIds.contains(order.getId()))
                    unAssignedOrderIds.add(order.getId());
            }
            String message = String.format(
                    "The solution contains %d unassigned orders: ", unAssignedOrderIds.size());
            for (Integer orderId: unAssignedOrderIds)
                message = message.concat(String.format("%d ", orderId));
            throw new InfeasibleSolutionException(message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Objects.equals(getDriverId2route(), solution.getDriverId2route());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDriverId2route());
    }
}
