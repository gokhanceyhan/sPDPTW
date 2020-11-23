package algorithms;

import common.Order;
import common.OrderIdAndDriverId;
import common.RouteCostFunction;
import input.Instance;
import output.Solution;

import java.util.HashMap;
import java.util.Map;

public class GreedyInsertionHeuristic implements HeuristicAlgorithm {

    private RouteCostFunction routeCostFunction;
    private Map<OrderIdAndDriverId, OrderInsertion> orderIdAndDriverId2orderInsertion = new HashMap<>();

    public GreedyInsertionHeuristic(RouteCostFunction routeCostFunction) {
        this.routeCostFunction = routeCostFunction;
    }

    @Override
    public Solution run(Instance instance) {

        for (Order order : instance.getOrders()){

        }

        return null;
    }

    public RouteCostFunction getRouteCostFunction() {
        return routeCostFunction;
    }

    public void setRouteCostFunction(RouteCostFunction routeCostFunction) {
        this.routeCostFunction = routeCostFunction;
    }

    public Map<OrderIdAndDriverId, OrderInsertion> getOrderIdAndDriverId2orderInsertion() {
        return orderIdAndDriverId2orderInsertion;
    }

    public void setOrderIdAndDriverId2orderInsertion(Map<OrderIdAndDriverId, OrderInsertion> orderIdAndDriverId2orderInsertion) {
        this.orderIdAndDriverId2orderInsertion = orderIdAndDriverId2orderInsertion;
    }
}
