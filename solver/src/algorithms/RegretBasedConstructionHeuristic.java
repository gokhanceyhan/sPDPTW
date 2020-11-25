package algorithms;

import common.Driver;
import common.Order;
import common.RouteCostFunction;
import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Route;
import output.Solution;
import utilities.SearchUtilities;

import java.util.*;
import java.util.stream.Collectors;

public class RegretBasedConstructionHeuristic implements HeuristicAlgorithm {

    // Each order insertion impact represents the best insertion to the route of a different driver
    private Map<Integer, List<OrderInsertionImpact>> orderId2orderInsertionImpacts;
    private int regretHorizon;
    private RouteCostFunction routeCostFunction;
    private Solution solution;

    public RegretBasedConstructionHeuristic(RouteCostFunction routeCostFunction, int regretHorizon) {
        this.orderId2orderInsertionImpacts = new HashMap<>();
        assert regretHorizon > 0;
        this.regretHorizon = regretHorizon;
        this.routeCostFunction = routeCostFunction;
        this.solution = new Solution();
    }

    @Override
    public Solution run(Instance instance) throws UnserviceableOrderException {
        Map<Integer, Order> orderId2order = instance.getOrders().stream().collect(
                Collectors.toMap(Order::getId, order -> order));
        initializeOrderInsertionImpacts(instance.getOrders(), instance.getDrivers());

        List<Order> pendingOrders = new ArrayList<>(instance.getOrders());
        while (pendingOrders.size() > 0){
            Map<Integer, Double> orderId2regret = new HashMap<>();
            for (Order order : pendingOrders){
                double regret = calculateRegret(this.getOrderId2orderInsertionImpacts().get(order.getId()));
                orderId2regret.put(order.getId(), regret);
            }
            // WARNING: returns a random order when regret horizon is 1.
            int nextOrderId = findBestOrder(orderId2regret);
            OrderInsertionImpact bestOrderInsertionImpact = findBestOrderInsertionImpact(
                    nextOrderId, this.getOrderId2orderInsertionImpacts().get(nextOrderId));
            int assignedDriverId = bestOrderInsertionImpact.getOrderInsertion().getDriverId();
            this.getSolution().updateRoute(assignedDriverId, bestOrderInsertionImpact.getRoute());
            updateOrderInsertionImpacts(
                    orderId2order, instance.getDrivers(), bestOrderInsertionImpact.getOrderInsertion());
            pendingOrders.remove(orderId2order.get(nextOrderId));
        }

        this.getSolution().evaluate();
        return getSolution();
    }

    private int findBestOrder(Map<Integer, Double> orderId2regret){
        assert orderId2regret.size() > 1;
        int bestOrderId = -1;
        double maxRegret = Double.NEGATIVE_INFINITY;
        for (Map.Entry<Integer, Double> entry: orderId2regret.entrySet()){
            if (entry.getValue() > maxRegret){
                bestOrderId = entry.getKey();
                maxRegret = entry.getValue();
            }
        }
        assert bestOrderId != 1;
        return bestOrderId;
    }

    private double calculateRegret(List<OrderInsertionImpact> orderInsertionImpacts){
        double minCostDelta = Double.POSITIVE_INFINITY;
        for (OrderInsertionImpact orderInsertionImpact : orderInsertionImpacts){
            if (orderInsertionImpact.getCostDelta() < minCostDelta)
                minCostDelta = orderInsertionImpact.getCostDelta();
        }
        double regret = 0.0;
        for (OrderInsertionImpact orderInsertionImpact : orderInsertionImpacts){
            regret += orderInsertionImpact.getCostDelta() - minCostDelta;
        }
        return regret;
    }

    private OrderInsertionImpact findBestOrderInsertionImpact(
            int orderId, List<OrderInsertionImpact> orderInsertionImpacts) throws UnserviceableOrderException {
        OrderInsertionImpact bestOrderInsertionImpact = null;
        double minCostDelta = Double.POSITIVE_INFINITY;
        for (OrderInsertionImpact orderInsertionImpact : orderInsertionImpacts){
            if (orderInsertionImpact.getCostDelta() < minCostDelta) {
                bestOrderInsertionImpact = orderInsertionImpact;
                minCostDelta = orderInsertionImpact.getCostDelta();
            }
        }
        if (bestOrderInsertionImpact == null)
            throw new UnserviceableOrderException(String.format("Cannot assign the order %d", orderId));
        return bestOrderInsertionImpact;
    }

    private void initializeOrderInsertionImpacts(List<Order> orders, List<Driver> drivers){
        Map<Integer, List<OrderInsertionImpact>> orderId2orderInsertionImpacts = new HashMap<>();
        for (Order order : orders){
            List<OrderInsertionImpact> orderInsertionImpacts = new ArrayList<>();
            Stack<Double> maxCostDeltas = new Stack<>();
            maxCostDeltas.push(Double.NEGATIVE_INFINITY);
            Stack<Integer> indicesOfOrderInsertionImpactsWithMaxCostDelta = new Stack<>();
            indicesOfOrderInsertionImpactsWithMaxCostDelta.push(-1);
            for (Driver driver: drivers){
                Route route = new Route(driver);
                OrderInsertionImpact orderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                        route, order, this.getRouteCostFunction());
                if (orderInsertionImpacts.size() < this.getRegretHorizon()) {
                    orderInsertionImpacts.add(orderInsertionImpact);
                    if (orderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(orderInsertionImpact.getCostDelta());
                        int indexOfInsertion = orderInsertionImpacts.size() - 1;
                        indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                    }
                }
                else if (orderInsertionImpact.getCostDelta() < maxCostDeltas.peek()){
                    maxCostDeltas.pop();
                    int indexToRemove = indicesOfOrderInsertionImpactsWithMaxCostDelta.pop();
                    orderInsertionImpacts.remove(indexToRemove);
                    orderInsertionImpacts.add(orderInsertionImpact);
                    if (orderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(orderInsertionImpact.getCostDelta());
                        int indexOfInsertion = orderInsertionImpacts.size() - 1;
                        indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                    }
                }
            }
            orderId2orderInsertionImpacts.put(order.getId(), orderInsertionImpacts);
        }
        this.setOrderId2orderInsertionImpacts(orderId2orderInsertionImpacts);
    }

    private void updateOrderInsertionImpacts(
            Map<Integer, Order> orderId2order, List<Driver> drivers, OrderInsertion lastOrderInsertion){
        Map<Integer, List<OrderInsertionImpact>> updatedOrderId2orderInsertionImpacts = new HashMap<>();
        for (Map.Entry<Integer, List<OrderInsertionImpact>> entry: this.getOrderId2orderInsertionImpacts().entrySet()){
            int orderId = entry.getKey();
            if (orderId == lastOrderInsertion.getOrderId())
                continue;
            // first update for the current best routes
            Map<Integer, OrderInsertionImpact> driverId2updatedOrderInsertionImpact = new HashMap<>();
            Stack<Double> maxCostDeltas = new Stack<>();
            maxCostDeltas.push(Double.NEGATIVE_INFINITY);
            Stack<Integer> driverIdsWithMaxCostDelta = new Stack<>();
            int dummyDriverId = -1;
            driverIdsWithMaxCostDelta.push(dummyDriverId);
            for (OrderInsertionImpact orderInsertionImpact : entry.getValue()){
                int driverId = orderInsertionImpact.getOrderInsertion().getDriverId();
                OrderInsertionImpact updatedOrderInsertionImpact;
                if (driverId != lastOrderInsertion.getDriverId())
                    updatedOrderInsertionImpact = orderInsertionImpact;
                else
                    updatedOrderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                            this.getSolution().getDriverId2route().get(driverId), orderId2order.get(orderId),
                            this.getRouteCostFunction());
                driverId2updatedOrderInsertionImpact.put(driverId, updatedOrderInsertionImpact);
                if (updatedOrderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                    maxCostDeltas.push(updatedOrderInsertionImpact.getCostDelta());
                    driverIdsWithMaxCostDelta.push(driverId);
                }
            }
            // check if another route has become one the current best routes
            for (Driver driver : drivers){
                int driverId = driver.getId();
                if (driverId2updatedOrderInsertionImpact.containsKey(driverId))
                    continue;
                Route routeOfDriver = this.getSolution().getDriverId2route().get(driverId);
                if (routeOfDriver == null)
                    routeOfDriver = new Route(driver);
                OrderInsertionImpact orderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                        routeOfDriver, orderId2order.get(orderId), this.getRouteCostFunction());
                if (orderInsertionImpact.getCostDelta() < maxCostDeltas.peek()){
                    maxCostDeltas.pop();
                    int driverIdToRemove = driverIdsWithMaxCostDelta.pop();
                    driverId2updatedOrderInsertionImpact.remove(driverIdToRemove);
                    driverId2updatedOrderInsertionImpact.put(driverId, orderInsertionImpact);
                    if (orderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(orderInsertionImpact.getCostDelta());
                        driverIdsWithMaxCostDelta.push(driverId);
                    }
                }
            }
            List<OrderInsertionImpact> updatedOrderInsertionImpacts = new ArrayList<>(
                    driverId2updatedOrderInsertionImpact.values());
            updatedOrderId2orderInsertionImpacts.put(orderId, updatedOrderInsertionImpacts);

        }
        this.setOrderId2orderInsertionImpacts(updatedOrderId2orderInsertionImpacts);
    }

    public Map<Integer, List<OrderInsertionImpact>> getOrderId2orderInsertionImpacts() {
        return orderId2orderInsertionImpacts;
    }

    public void setOrderId2orderInsertionImpacts(Map<Integer, List<OrderInsertionImpact>> orderId2orderInsertionImpacts) {
        this.orderId2orderInsertionImpacts = orderId2orderInsertionImpacts;
    }

    public int getRegretHorizon() {
        return regretHorizon;
    }

    public void setRegretHorizon(int regretHorizon) {
        this.regretHorizon = regretHorizon;
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
