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

public class RegretBasedInsertionHeuristic implements InsertionHeuristic {

    // Each order insertion impact represents the best insertion to the route of a different driver
    private Instance instance;
    private Map<Integer, List<OrderInsertionImpact>> orderId2bestOrderInsertionImpacts;
    private Map<Integer, List<OrderInsertionImpact>> orderId2candidateOrderInsertionImpacts;
    private int regretHorizon;
    private RouteCostFunction routeCostFunction;
    private PartialSolution partialSolution;

    public RegretBasedInsertionHeuristic(Instance instance, RouteCostFunction routeCostFunction, int regretHorizon) {
        this.instance = instance;
        this.orderId2bestOrderInsertionImpacts = new HashMap<>();
        this.orderId2candidateOrderInsertionImpacts = new HashMap<>();
        assert regretHorizon > 1;
        this.regretHorizon = regretHorizon;
        this.routeCostFunction = routeCostFunction;
    }

    @Override
    public Solution run(PartialSolution partialSolution) throws UnserviceableOrderException {

        this.setPartialSolution(new PartialSolution(partialSolution));
        List<Order> pendingOrders = this.getPartialSolution().getPendingOrders();
        Map<Integer, Order> orderId2order = pendingOrders.stream().collect(
                Collectors.toMap(Order::getId, order -> order));
        initializeOrderInsertionImpacts(pendingOrders, instance.getDrivers());

        while (pendingOrders.size() > 0){
            Map<Integer, Double> orderId2regret = new HashMap<>();
            for (Order order : pendingOrders){
                double regret = calculateRegret(this.getOrderId2bestOrderInsertionImpacts().get(order.getId()));
                orderId2regret.put(order.getId(), regret);
            }
            int nextOrderId = findBestOrder(orderId2regret);
            OrderInsertionImpact bestOrderInsertionImpact = findBestOrderInsertionImpact(
                    nextOrderId, this.getOrderId2bestOrderInsertionImpacts().get(nextOrderId));
            int assignedDriverId = bestOrderInsertionImpact.getOrderInsertion().getDriverId();
            this.getPartialSolution().updateRoute(assignedDriverId, bestOrderInsertionImpact.getRoute());
            updateOrderInsertionImpacts(
                    orderId2order, instance.getDrivers(), bestOrderInsertionImpact.getOrderInsertion());
            pendingOrders.remove(orderId2order.get(nextOrderId));
        }
        Solution solution = new Solution(this.getPartialSolution().getDriverId2route());
        solution.evaluate();
        return solution;
    }

    public void clear(){
        this.setPartialSolution(null);
        this.getOrderId2bestOrderInsertionImpacts().clear();
    }

    @Override
    public InsertionHeuristicType getType() {
        return InsertionHeuristicType.REGRET_BASED_INSERTION;
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
        Map<Integer, List<OrderInsertionImpact>> orderId2bestOrderInsertionImpacts = new HashMap<>();
        Map<Integer, List<OrderInsertionImpact>> orderId2candidateOrderInsertionImpacts = new HashMap<>();
        for (Order order : orders){
            List<OrderInsertionImpact> bestOrderInsertionImpacts = new ArrayList<>();
            List<OrderInsertionImpact> candidateOrderInsertionImpacts = new ArrayList<>();
            Stack<Double> maxCostDeltas = new Stack<>();
            maxCostDeltas.push(Double.NEGATIVE_INFINITY);
            Stack<Integer> indicesOfOrderInsertionImpactsWithMaxCostDelta = new Stack<>();
            indicesOfOrderInsertionImpactsWithMaxCostDelta.push(-1);
            for (Driver driver: drivers){
                Route route = this.getPartialSolution().getDriverId2route().getOrDefault(
                        driver.getId(), new Route(driver));
                OrderInsertionImpact orderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                        route, order, this.getRouteCostFunction());
                if (bestOrderInsertionImpacts.size() < this.getRegretHorizon()) {
                    bestOrderInsertionImpacts.add(orderInsertionImpact);
                    if (orderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(orderInsertionImpact.getCostDelta());
                        int indexOfInsertion = bestOrderInsertionImpacts.size() - 1;
                        indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                    }
                }
                else if (orderInsertionImpact.getCostDelta() < maxCostDeltas.peek()){
                    maxCostDeltas.pop();
                    int indexToRemove = indicesOfOrderInsertionImpactsWithMaxCostDelta.pop();
                    OrderInsertionImpact removedOrderInsertionImpact = bestOrderInsertionImpacts.remove(indexToRemove);
                    candidateOrderInsertionImpacts.add(removedOrderInsertionImpact);
                    bestOrderInsertionImpacts.add(orderInsertionImpact);
                    if (orderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(orderInsertionImpact.getCostDelta());
                        int indexOfInsertion = bestOrderInsertionImpacts.size() - 1;
                        indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                    }
                }
                else
                    candidateOrderInsertionImpacts.add(orderInsertionImpact);
            }
            orderId2bestOrderInsertionImpacts.put(order.getId(), bestOrderInsertionImpacts);
            orderId2candidateOrderInsertionImpacts.put(order.getId(), candidateOrderInsertionImpacts);
        }
        this.setOrderId2bestOrderInsertionImpacts(orderId2bestOrderInsertionImpacts);
        this.setOrderId2candidateOrderInsertionImpacts(orderId2candidateOrderInsertionImpacts);
    }

    private void updateOrderInsertionImpacts(
            Map<Integer, Order> orderId2order, List<Driver> drivers, OrderInsertion lastOrderInsertion){
        Map<Integer, Driver> driverId2driver = drivers.stream().collect(
                Collectors.toMap(Driver::getId, driver -> driver));
        Map<Integer, List<OrderInsertionImpact>> updatedOrderId2bestOrderInsertionImpacts = new HashMap<>();
        Map<Integer, List<OrderInsertionImpact>> updatedOrderId2candidateOrderInsertionImpacts = new HashMap<>();
        for (Map.Entry<Integer, List<OrderInsertionImpact>> entry:
                this.getOrderId2bestOrderInsertionImpacts().entrySet()){
            int orderId = entry.getKey();
            if (orderId == lastOrderInsertion.getOrderId())
                continue;
            // first update for the current best routes
            List<OrderInsertionImpact> updatedBestOrderInsertionImpacts = new ArrayList<>();
            Stack<Double> maxCostDeltas = new Stack<>();
            maxCostDeltas.push(Double.NEGATIVE_INFINITY);
            Stack<Integer> indicesOfOrderInsertionImpactsWithMaxCostDelta = new Stack<>();
            indicesOfOrderInsertionImpactsWithMaxCostDelta.push(-1);
            for (OrderInsertionImpact orderInsertionImpact : entry.getValue()){
                int driverId = orderInsertionImpact.getOrderInsertion().getDriverId();
                OrderInsertionImpact updatedOrderInsertionImpact;
                if (driverId != lastOrderInsertion.getDriverId())
                    updatedOrderInsertionImpact = orderInsertionImpact;
                else {
                    Route route = this.getPartialSolution().getDriverId2route().get(driverId);
                    updatedOrderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                            route, orderId2order.get(orderId), this.getRouteCostFunction());
                }
                updatedBestOrderInsertionImpacts.add(updatedOrderInsertionImpact);
                if (updatedOrderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                    maxCostDeltas.push(updatedOrderInsertionImpact.getCostDelta());
                    int indexOfInsertion = updatedBestOrderInsertionImpacts.size() - 1;
                    indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                }
            }
            // check if a candidate route has become one of the current best routes
            List<OrderInsertionImpact> updatedCandidateOrderInsertionImpacts = new ArrayList<>();
            for (OrderInsertionImpact orderInsertionImpact :
                    this.getOrderId2candidateOrderInsertionImpacts().get(orderId)){
                int driverId = orderInsertionImpact.getOrderInsertion().getDriverId();
                OrderInsertionImpact updatedOrderInsertionImpact;
                if (driverId != lastOrderInsertion.getDriverId())
                    updatedOrderInsertionImpact = orderInsertionImpact;
                else{
                    Route route = this.getPartialSolution().getDriverId2route().get(driverId);
                    updatedOrderInsertionImpact = SearchUtilities.findBestOrderInsertion(
                            route, orderId2order.get(orderId), this.getRouteCostFunction());
                }
                updatedCandidateOrderInsertionImpacts.add(updatedOrderInsertionImpact);
                if (updatedOrderInsertionImpact.getCostDelta() < maxCostDeltas.peek()){
                    maxCostDeltas.pop();
                    int indexToRemove = indicesOfOrderInsertionImpactsWithMaxCostDelta.pop();
                    OrderInsertionImpact removedOrderInsertionImpact = updatedBestOrderInsertionImpacts.remove(
                            indexToRemove);
                    updatedCandidateOrderInsertionImpacts.add(removedOrderInsertionImpact);
                    updatedBestOrderInsertionImpacts.add(updatedOrderInsertionImpact);
                    if (updatedOrderInsertionImpact.getCostDelta() > maxCostDeltas.peek()) {
                        maxCostDeltas.push(updatedOrderInsertionImpact.getCostDelta());
                        int indexOfInsertion = updatedBestOrderInsertionImpacts.size() - 1;
                        indicesOfOrderInsertionImpactsWithMaxCostDelta.push(indexOfInsertion);
                    }
                }
            }
            updatedOrderId2bestOrderInsertionImpacts.put(orderId, updatedBestOrderInsertionImpacts);
            updatedOrderId2candidateOrderInsertionImpacts.put(orderId, updatedCandidateOrderInsertionImpacts);
        }
        this.setOrderId2bestOrderInsertionImpacts(updatedOrderId2bestOrderInsertionImpacts);
        this.setOrderId2candidateOrderInsertionImpacts(updatedOrderId2candidateOrderInsertionImpacts);
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Map<Integer, List<OrderInsertionImpact>> getOrderId2bestOrderInsertionImpacts() {
        return orderId2bestOrderInsertionImpacts;
    }

    public void setOrderId2bestOrderInsertionImpacts(
            Map<Integer, List<OrderInsertionImpact>> orderId2bestOrderInsertionImpacts) {
        this.orderId2bestOrderInsertionImpacts = orderId2bestOrderInsertionImpacts;
    }

    public Map<Integer, List<OrderInsertionImpact>> getOrderId2candidateOrderInsertionImpacts() {
        return orderId2candidateOrderInsertionImpacts;
    }

    public void setOrderId2candidateOrderInsertionImpacts(
            Map<Integer, List<OrderInsertionImpact>> orderId2candidateOrderInsertionImpacts) {
        this.orderId2candidateOrderInsertionImpacts = orderId2candidateOrderInsertionImpacts;
    }

    public PartialSolution getPartialSolution() {
        return partialSolution;
    }

    public void setPartialSolution(PartialSolution partialSolution) {
        this.partialSolution = partialSolution;
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

}
