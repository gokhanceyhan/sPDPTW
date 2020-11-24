package algorithms;

import common.Driver;
import common.Order;
import common.OrderIdAndDriverId;
import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import exceptions.UnservicableOrderException;
import input.Instance;
import output.Route;
import output.Solution;
import utilities.SearchUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GreedyInsertionHeuristic implements HeuristicAlgorithm {

    private Map<OrderIdAndDriverId, OrderInsertionImpact> orderIdAndDriverId2orderInsertionImpact;
    private RouteCostFunction routeCostFunction;
    private Solution solution;

    public GreedyInsertionHeuristic(RouteCostFunction routeCostFunction) {
        this.orderIdAndDriverId2orderInsertionImpact = new HashMap<>();
        this.routeCostFunction = routeCostFunction;
        this.solution = new Solution();
    }

    @Override
    public Solution run(Instance instance) throws UnservicableOrderException {

        initializeOrderInsertionImpacts(instance.getOrders(), instance.getDrivers());
        Map<Integer, Order> orderId2order = instance.getOrders().stream().collect(
                Collectors.toMap(Order::getId, order -> order));

        for (Order order : instance.getOrders()){
            int driverId = findBestDriver(order);
            OrderIdAndDriverId orderIdAndDriverId = new OrderIdAndDriverId(driverId, order.getId());
            OrderInsertionImpact orderInsertionImpact = this.getOrderIdAndDriverId2orderInsertionImpact().get(
                    orderIdAndDriverId);
            this.getSolution().updateRoute(driverId, orderInsertionImpact.getRoute());
            updateOrderInsertionImpacts(orderId2order, orderInsertionImpact.getOrderInsertion());
        }
        this.getSolution().evaluate();
        return getSolution();
    }

    private int findBestDriver(Order order) throws UnservicableOrderException {
        int bestDriverId = -1;
        double minCostDelta = Double.POSITIVE_INFINITY;
        for (Map.Entry<OrderIdAndDriverId, OrderInsertionImpact> entry:
                this.orderIdAndDriverId2orderInsertionImpact.entrySet()){
            int orderId = entry.getKey().getOrderId();
            if (orderId != order.getId())
                continue;
            if (entry.getValue().getCostDelta() < minCostDelta) {
                bestDriverId = entry.getKey().getDriverId();
                minCostDelta = entry.getValue().getCostDelta();
            }
        }
        if (bestDriverId == -1)
            throw new UnservicableOrderException(String.format("Cannot assign the order %d", order.getId()));
        return bestDriverId;
    }

    private void initializeOrderInsertionImpacts(List<Order> orders, List<Driver> drivers){
        Map<OrderIdAndDriverId, OrderInsertionImpact> orderIdAndDriverId2orderInsertionImpact = new HashMap<>();
        for (Order order : orders){
            for (Driver driver: drivers){
                OrderIdAndDriverId orderIdAndDriverId = new OrderIdAndDriverId(driver.getId(), order.getId());
                Route route = new Route(driver);
                OrderInsertionImpact orderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                        route, order, this.getRouteCostFunction());
                orderIdAndDriverId2orderInsertionImpact.put(orderIdAndDriverId, orderInsertionImpact);
            }
        }
        this.setOrderIdAndDriverId2orderInsertionImpact(orderIdAndDriverId2orderInsertionImpact);
    }

    private void updateOrderInsertionImpacts(Map<Integer, Order> orderId2order, OrderInsertion lastOrderInsertion){
        Map<OrderIdAndDriverId, OrderInsertionImpact> updatedOrderIdAndDriverId2orderInsertionImpact = new HashMap<>();
        for (Map.Entry<OrderIdAndDriverId, OrderInsertionImpact> entry:
                this.getOrderIdAndDriverId2orderInsertionImpact().entrySet()){
            int orderId = entry.getKey().getOrderId();
            if (orderId == lastOrderInsertion.getOrderId())
                continue;
            int driverId = entry.getKey().getDriverId();
            if (driverId != lastOrderInsertion.getDriverId()) {
                updatedOrderIdAndDriverId2orderInsertionImpact.put(entry.getKey(), entry.getValue());
                continue;
            }
            OrderInsertionImpact updatedOrderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                    this.getSolution().getDriverId2route().get(driverId), orderId2order.get(orderId),
                    this.getRouteCostFunction());
            updatedOrderIdAndDriverId2orderInsertionImpact.put(entry.getKey(), updatedOrderInsertionImpact);
        }
        this.setOrderIdAndDriverId2orderInsertionImpact(updatedOrderIdAndDriverId2orderInsertionImpact);
    }

    public Map<OrderIdAndDriverId, OrderInsertionImpact> getOrderIdAndDriverId2orderInsertionImpact() {
        return orderIdAndDriverId2orderInsertionImpact;
    }

    public void setOrderIdAndDriverId2orderInsertionImpact(Map<OrderIdAndDriverId,
            OrderInsertionImpact> orderIdAndDriverId2orderInsertionImpact) {
        this.orderIdAndDriverId2orderInsertionImpact = orderIdAndDriverId2orderInsertionImpact;
    }

    public RouteCostFunction getRouteCostFunction() {
        return routeCostFunction;
    }

    public void setRouteCostFunction(RouteCostFunction routeCostFunction) {
        this.routeCostFunction = routeCostFunction;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}
