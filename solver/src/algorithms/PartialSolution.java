package algorithms;

import common.Order;
import output.Route;

import java.util.List;
import java.util.Map;

public class PartialSolution {

    private Map<Integer, Route> driverId2route;
    private List<Order> pendingOrders;

    public PartialSolution(Map<Integer, Route> driverId2route, List<Order> pendingOrders) {
        this.driverId2route = driverId2route;
        this.pendingOrders = pendingOrders;
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
