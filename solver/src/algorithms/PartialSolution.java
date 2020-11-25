package algorithms;

import common.Order;
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
        this.driverId2route = new HashMap<>(solution.getDriverId2route());
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
}
