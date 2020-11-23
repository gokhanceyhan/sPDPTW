package output;

import common.Order;
import exceptions.InfeasibleSolutionException;
import input.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

    private double cost;
    private List<Route> routes;

    public Solution(List<Route> routes) {
        this.routes = routes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    private void evaluate(){
        double cost = this.getRoutes().stream().map(Route::getCost).reduce(0.0, Double:: sum);
        this.setCost(cost);
    }

    private void validate(Instance instance) throws InfeasibleSolutionException {
        List<Integer> assignedOrderIds = new ArrayList<>();
        for (Route route: this.getRoutes()) {
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
}
