package algorithms;

import common.Driver;
import common.Order;
import common.OrderIdAndDriverId;
import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import input.Instance;
import output.Route;
import output.Solution;
import utilities.SearchUtilities;

import java.util.*;
import java.util.stream.Collectors;

public class GreedyRemovalHeuristic implements RemovalHeuristic {

    private final static Random random = new Random(0);

    private Map<Integer, OrderRemovalImpact> orderId2orderRemovalImpact;
    private Instance instance;
    private int numOrdersToRemove;
    private PartialSolution partialSolution;
    private double randomizationCoefficient;
    private RouteCostFunction routeCostFunction;

    public GreedyRemovalHeuristic(
            Instance instance, int numOrdersToRemove, double randomizationCoefficient,
            RouteCostFunction routeCostFunction) {
        this.instance = instance;
        this.numOrdersToRemove = numOrdersToRemove;
        this.orderId2orderRemovalImpact = new HashMap<>();
        this.randomizationCoefficient = randomizationCoefficient;
        this.routeCostFunction = routeCostFunction;
    }

    @Override
    public PartialSolution run(Solution solution) throws InfeasibleRouteException {
        Map<Integer, Order> orderId2order = this.getInstance().getOrders().stream().collect(
                Collectors.toMap(Order::getId, order -> order));
        List<Order> pendingOrders = new ArrayList<>();
        this.setPartialSolution(new PartialSolution(solution.getDriverId2route(), pendingOrders));

        initializeOrderRemovalImpacts();
        int numOrdersToRemove = this.getNumOrdersToRemove();
        while (numOrdersToRemove > 0){
            int selectedOrderId = selectOrderIdToRemove();
            Order selectedOrder = orderId2order.get(selectedOrderId);
            OrderRemovalImpact orderRemovalImpact = this.getOrderId2orderRemovalImpact().get(selectedOrderId);
            int driverIdToUpdate = orderRemovalImpact.getDriverId();
            Route routeToUpdate = this.getPartialSolution().getDriverId2route().get(driverIdToUpdate);
            routeToUpdate.remove(selectedOrderId);
            routeToUpdate.evaluate(this.getRouteCostFunction());
            this.getPartialSolution().getPendingOrders().add(selectedOrder);
            numOrdersToRemove--;
            updateOrderRemovalImpacts(selectedOrderId, driverIdToUpdate);
        }

        return this.getPartialSolution();
    }

    private void updateOrderRemovalImpacts(int lastRemovedOrderId, int lastUpdatedDriverId) throws
            InfeasibleRouteException {

        Map<Integer, OrderRemovalImpact> updatedOrderId2orderRemovalImpact = new HashMap<>();
        for (Map.Entry<Integer, OrderRemovalImpact> entry : this.getOrderId2orderRemovalImpact().entrySet()){
            int orderId = entry.getKey();
            if (orderId == lastRemovedOrderId)
                continue;
            OrderRemovalImpact orderRemovalImpact = entry.getValue();
            int driverId = orderRemovalImpact.getDriverId();
            if (driverId != lastUpdatedDriverId) {
                updatedOrderId2orderRemovalImpact.put(orderId, orderRemovalImpact);
                continue;
            }
            Route route = this.getPartialSolution().getDriverId2route().get(driverId);
            double currentCost = route.getCost();
            Route updatedRoute = SearchUtilities.removeOrder(route, orderId, this.getRouteCostFunction());
            double costDelta = updatedRoute.getCost() - currentCost;
            OrderRemovalImpact updatedOrderRemovalImpact = new OrderRemovalImpact(driverId, orderId, costDelta);
            updatedOrderId2orderRemovalImpact.put(orderId, updatedOrderRemovalImpact);
        }
        this.setOrderId2orderRemovalImpact(updatedOrderId2orderRemovalImpact);
    }

    private int selectOrderIdToRemove(){
        List<OrderRemovalImpact> orderRemovalImpacts = new ArrayList<>(this.getOrderId2orderRemovalImpact().values());
        Collections.sort(orderRemovalImpacts);
        int selectedIndex = (int) (Math.pow(random.nextDouble(), this.getRandomizationCoefficient()) *
                orderRemovalImpacts.size());
        return orderRemovalImpacts.get(0).getOrderId();
    }

    private void initializeOrderRemovalImpacts() throws InfeasibleRouteException {

        Map<Integer, OrderRemovalImpact> orderId2orderRemovalImpact = new HashMap<>();
        for (Map.Entry<Integer, Route> entry : this.getPartialSolution().getDriverId2route().entrySet()){
            int driverId = entry.getKey();
            Route route = entry.getValue();
            double currentCost = route.getCost();
            for (Integer orderId : route.getOrderIds()){
                Route updatedRoute = SearchUtilities.removeOrder(route, orderId, this.getRouteCostFunction());
                double costDelta = updatedRoute.getCost() - currentCost;
                OrderRemovalImpact orderRemovalImpact = new OrderRemovalImpact(driverId, orderId, costDelta);
                orderId2orderRemovalImpact.put(orderId, orderRemovalImpact);
            }
        }
        this.setOrderId2orderRemovalImpact(orderId2orderRemovalImpact);
    }

    public Map<Integer, OrderRemovalImpact> getOrderId2orderRemovalImpact() {
        return orderId2orderRemovalImpact;
    }

    public void setOrderId2orderRemovalImpact(Map<Integer, OrderRemovalImpact> orderId2orderRemovalImpact) {
        this.orderId2orderRemovalImpact = orderId2orderRemovalImpact;
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

    public PartialSolution getPartialSolution() {
        return partialSolution;
    }

    public void setPartialSolution(PartialSolution partialSolution) {
        this.partialSolution = partialSolution;
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
