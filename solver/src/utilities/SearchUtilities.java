package utilities;

import algorithms.OrderInsertion;
import common.*;
import exceptions.InfeasibleRouteException;
import output.Route;

import java.util.*;

public class SearchUtilities {

    public static double costOfOrderInsertion(
            Route initialRoute, Order order, OrderInsertion orderInsertion, RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        Route route = new Route(initialRoute);
        route.insert(order, orderInsertion, costFunction);
        return route.getCost();
    }

    public static OrderInsertion findBestOrderInsertion(
            Route route, Order order, RouteCostFunction costFunction){
        int numTasks = route.getTasks().size();
        double currentCost = route.getCost();
        double minCostDelta = Double.POSITIVE_INFINITY;
        OrderInsertion bestOrderInsertion = new OrderInsertion(
                numTasks + 1, route.getDriver().getId(), order.getId(), numTasks);
        for (int pickUpIndex = 0; pickUpIndex < numTasks + 1; pickUpIndex++) {
            for (int deliveryIndex = pickUpIndex + 1; deliveryIndex < numTasks + 2; deliveryIndex++) {
                OrderInsertion orderInsertion = new OrderInsertion(
                        deliveryIndex, route.getDriver().getId(), order.getId(), pickUpIndex);
                double cost;
                try {
                    cost = costOfOrderInsertion(route, order, orderInsertion, costFunction);
                } catch (InfeasibleRouteException e) {
                    continue;
                }
                double costDelta = cost - currentCost;
                if (costDelta < minCostDelta){
                    minCostDelta = costDelta;
                    bestOrderInsertion = orderInsertion;
                }
            }
        }
        return bestOrderInsertion;
    }

    public static boolean isCapacitySufficient(List<Integer> driverLoads, int numItems, int capacity){
        int maxLoad = Collections.max(driverLoads);
        if (maxLoad + numItems > capacity)
            return false;
        return true;
    }

}
