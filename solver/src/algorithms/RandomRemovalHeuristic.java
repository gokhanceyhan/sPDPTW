package algorithms;

import common.Order;
import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import exceptions.InvalidInputException;
import input.Instance;
import output.Route;
import output.Solution;

import java.util.*;

public class RandomRemovalHeuristic implements RemovalHeuristic {

    private final static Random random = new Random(0);
    private Instance instance;
    private int numOrdersToRemove;

    public RandomRemovalHeuristic(Instance instance) {
        this.instance = instance;
    }

    @Override
    public PartialSolution run(Solution initialSolution, RouteCostFunction routeCostFunction) throws
            InfeasibleRouteException {
        Solution solution = new Solution(initialSolution);
        Map<Integer, Integer> orderId2driverId = new HashMap<>();
        for (Map.Entry<Integer, Route> entry : initialSolution.getDriverId2route().entrySet()){
            for (Integer orderId : entry.getValue().getOrderIds())
                orderId2driverId.put(orderId, entry.getKey());
        }
        List<Order> selectedOrders = selectOrdersToRemove();
        for (Order order : selectedOrders){
            int orderId = order.getId();
            int driverId = orderId2driverId.get(orderId);
            Route route = solution.getDriverId2route().get(driverId);
            route.remove(orderId);
            route.evaluate(routeCostFunction);
        }
        return new PartialSolution(solution.getDriverId2route(), selectedOrders);
    }

    private List<Order> selectOrdersToRemove(){
        Map<Integer, Order> orderId2order = new HashMap<>();
        List<Integer> orderIds = new ArrayList<>();
        for (Order order : this.getInstance().getOrders()){
            orderId2order.put(order.getId(), order);
            orderIds.add(order.getId());
        }
        int numOrdersToRemove = this.getNumOrdersToRemove();
        List<Order> selectedOrders = new ArrayList<>();
        while (numOrdersToRemove > 0){
            int selectedIndex = random.nextInt(orderIds.size());
            Integer selectedOrderId = orderIds.get(selectedIndex);
            selectedOrders.add(orderId2order.get(selectedOrderId));
            numOrdersToRemove--;
            orderIds.remove(selectedIndex);
        }
        return selectedOrders;
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
}
