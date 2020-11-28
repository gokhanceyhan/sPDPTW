package algorithms;

import common.Order;
import exceptions.InfeasibleSolutionException;
import input.Instance;
import output.Route;
import output.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartialSolution {

    private Map<Integer, Route> driverId2route;
    private List<Order> pendingOrders;

    public PartialSolution(List<Order> pendingOrders) {
        this.driverId2route = new HashMap<>();
        this.pendingOrders = pendingOrders;
    }

    public PartialSolution(Map<Integer, Route> driverId2route, List<Order> pendingOrders) {
        this.driverId2route = driverId2route;
        this.pendingOrders = pendingOrders;
    }

    public PartialSolution (PartialSolution solution){
        Map<Integer, Route> driverId2route = new HashMap<>();
        for (Map.Entry<Integer, Route> entry : solution.getDriverId2route().entrySet())
            driverId2route.put(entry.getKey(), new Route(entry.getValue()));
        this.driverId2route = driverId2route;
        this.pendingOrders = new ArrayList<>(solution.getPendingOrders());
    }

    public void updateRoute(int driverId, Route route){
        this.getDriverId2route().put(driverId, route);
    }

    public Map<Integer, Route> getDriverId2route() {
        return driverId2route;
    }

    public void setDriverId2route(Map<Integer, Route> driverId2route) {
        this.driverId2route = driverId2route;
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(List<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
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
        List<Integer> unAssignedOrderIds = new ArrayList<>();
        for (Order order : instance.getOrders()){
            if (!assignedOrderIds.contains(order.getId()))
                unAssignedOrderIds.add(order.getId());
        }
        List<Integer> pendingOrderIds = new ArrayList<>();
        for (Order order : pendingOrders){
            pendingOrderIds.add(order.getId());
        }
        for (int orderId : unAssignedOrderIds){
            if (pendingOrderIds.contains(orderId))
                continue;
            throw new InfeasibleSolutionException(
                    String.format("Unassigned order %d is not among the pending orders", orderId));
        }
        for (int orderId : assignedOrderIds){
            if (pendingOrderIds.contains(orderId))
                throw new InfeasibleSolutionException(
                        String.format("Assigned order %d is among the pending orders", orderId));
        }
    }

}
