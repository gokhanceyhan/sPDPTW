package algorithms;

import common.Order;
import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import input.Instance;
import output.Route;
import output.Solution;
import utilities.DistanceUtilities;
import utilities.SearchUtilities;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class ShawRemovalHeuristic implements RemovalHeuristic {

    private final static Random random = new Random(0);

    private Instance instance;
    private int numOrdersToRemove;
    private OrderSimilarityFunction orderSimilarityFunction;
    private double randomizationCoefficient;
    private RouteCostFunction routeCostFunction;

    public ShawRemovalHeuristic(
            Instance instance, int numOrdersToRemove, OrderSimilarityFunction orderSimilarityFunction,
            double randomizationCoefficient, RouteCostFunction routeCostFunction) {
        this.instance = instance;
        this.numOrdersToRemove = numOrdersToRemove;
        this.orderSimilarityFunction = orderSimilarityFunction;
        this.randomizationCoefficient = randomizationCoefficient;
        this.routeCostFunction = routeCostFunction;
    }

    @Override
    public PartialSolution run(Solution solution) throws InfeasibleRouteException {

        Map<Integer, Integer> orderId2assignedDriverId = calculateOrderId2assignedDriverId(solution);
        List<Order> selectedOrders = new ArrayList<>();
        int numOrdersToRemove = this.getNumOrdersToRemove();

        Order firstOrderSelected = selectFirstOrderToRemove();
        selectedOrders.add(firstOrderSelected);
        numOrdersToRemove--;

        while (numOrdersToRemove > 0){
            Order nextOrderSelected = nextOrderToRemove(
                    selectedOrders, solution.getDriverId2route(), orderId2assignedDriverId);
            selectedOrders.add(nextOrderSelected);
            numOrdersToRemove--;
        }

        Map<Integer, Route> driverId2updatedRoute = new HashMap<>();
        for (Map.Entry<Integer, Route> entry : solution.getDriverId2route().entrySet())
            driverId2updatedRoute.put(entry.getKey(), new Route(entry.getValue()));

        for (Order order : selectedOrders){
            int assignedDriverId = orderId2assignedDriverId.get(order.getId());
            Route route = driverId2updatedRoute.get(assignedDriverId);
            route.remove(order.getId());
        }

        for (Map.Entry<Integer, Route> entry : driverId2updatedRoute.entrySet())
            entry.getValue().evaluate(this.getRouteCostFunction());

        return new PartialSolution(driverId2updatedRoute, selectedOrders);
    }

    @Override
    public void clear(){

    }

    @Override
    public RemovalHeuristicType getType() {
        return RemovalHeuristicType.SHAW_REMOVAL;
    }

    private Order selectFirstOrderToRemove(){
        List<Order> orders = this.getInstance().getOrders();
        int selectedIndex = random.nextInt(orders.size());
        return orders.get(selectedIndex);
    }

    private Order nextOrderToRemove(
            List<Order> selectedOrders, Map<Integer, Route> driverId2route,
            Map<Integer, Integer> orderId2assignedDriverId){

        List<Order> orders = this.getInstance().getOrders();
        int baseOrderIndex = random.nextInt(selectedOrders.size());
        Order baseOrder = selectedOrders.get(baseOrderIndex);
        Route routeOfBaseOrder = driverId2route.get(orderId2assignedDriverId.get(baseOrder.getId()));

        List<OrderSimilarity> orderSimilarities = new ArrayList<>();
        Map<Integer, Order> comparedOrderId2order = new HashMap<>();
        for (Order order : orders){
            if (selectedOrders.contains(order))
                continue;
            int orderId = order.getId();
            comparedOrderId2order.put(orderId, order);
            int driverId = orderId2assignedDriverId.get(orderId);
            Route routeOfOrder = driverId2route.get(driverId);
            OrderSimilarity orderSimilarity = calculateOrderSimilarityValue(
                    order, baseOrder, routeOfOrder, routeOfBaseOrder);
            orderSimilarities.add(orderSimilarity);
        }
        Collections.sort(orderSimilarities);
        int selectedIndex = (int) (Math.pow(random.nextDouble(), this.getRandomizationCoefficient()) *
                orderSimilarities.size());
        OrderSimilarity bestOrderSimilarity = orderSimilarities.get(selectedIndex);
        int nextOrderId = bestOrderSimilarity.getFirstOrderId() == baseOrder.getId() ?
                bestOrderSimilarity.getSecondOrderId() : bestOrderSimilarity.getFirstOrderId();
        return comparedOrderId2order.get(nextOrderId);
    }

    private OrderSimilarity calculateOrderSimilarityValue(
            Order firstOrder, Order secondOrder, Route routeOfFirstOrder, Route routeOfSecondOrder){

        double differenceBetweenPickUpTimes = Math.abs(routeOfFirstOrder.getTaskCompletionTimes().get(
                routeOfFirstOrder.getOrderId2pickupTaskIndex().get(firstOrder.getId())) -
                routeOfSecondOrder.getTaskCompletionTimes().get(
                        routeOfSecondOrder.getOrderId2pickupTaskIndex().get(secondOrder.getId())));
        double differenceBetweenDeliveryTimes = Math.abs(routeOfFirstOrder.getTaskCompletionTimes().get(
                routeOfFirstOrder.getOrderId2deliveryTaskIndex().get(firstOrder.getId())) -
                routeOfSecondOrder.getTaskCompletionTimes().get(
                        routeOfSecondOrder.getOrderId2deliveryTaskIndex().get(secondOrder.getId())));
        double distanceBetweenPickUpTasks = DistanceUtilities.distanceInKm(
                firstOrder.getPickup().getLocation(), secondOrder.getPickup().getLocation());
        double distanceBetweenDeliveryTasks = DistanceUtilities.distanceInKm(
                firstOrder.getDelivery().getLocation(), secondOrder.getDelivery().getLocation());
        double differenceBetweenLoads = Math.abs(
                firstOrder.getPickup().getNumItems() - secondOrder.getPickup().getNumItems());

        OrderSimilarity orderSimilarity = new OrderSimilarity(
                differenceBetweenDeliveryTimes, differenceBetweenLoads, differenceBetweenPickUpTimes,
                distanceBetweenDeliveryTasks, distanceBetweenPickUpTasks, firstOrder.getId(), secondOrder.getId());
        orderSimilarity.setSimilarityValue(this.getOrderSimilarityFunction());
        return orderSimilarity;
    }

    private Map<Integer, Integer> calculateOrderId2assignedDriverId(Solution solution){
        Map<Integer, Integer> orderId2assignedDriverId = new HashMap<>();
        for (Map.Entry<Integer, Route> entry: solution.getDriverId2route().entrySet()){
            int driverId = entry.getKey();
            List<Integer> orderIds = entry.getValue().getOrderIds();
            for (int orderId : orderIds)
                orderId2assignedDriverId.put(orderId, driverId);
        }
        return orderId2assignedDriverId;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public int getNumOrdersToRemove() {
        return numOrdersToRemove;
    }

    public void setNumOrdersToRemove(int numOrdersToRemove) {
        this.numOrdersToRemove = numOrdersToRemove;
    }

    public OrderSimilarityFunction getOrderSimilarityFunction() {
        return orderSimilarityFunction;
    }

    public void setOrderSimilarityFunction(OrderSimilarityFunction orderSimilarityFunction) {
        this.orderSimilarityFunction = orderSimilarityFunction;
    }

    public double getRandomizationCoefficient() {
        return randomizationCoefficient;
    }

    public void setRandomizationCoefficient(double randomizationCoefficient) {
        this.randomizationCoefficient = randomizationCoefficient;
    }

    public RouteCostFunction getRouteCostFunction() {
        return routeCostFunction;
    }

    public void setRouteCostFunction(RouteCostFunction routeCostFunction) {
        this.routeCostFunction = routeCostFunction;
    }
}
