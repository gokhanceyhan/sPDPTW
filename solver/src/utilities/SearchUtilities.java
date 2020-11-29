package utilities;

import algorithms.OrderInsertion;
import algorithms.OrderInsertionImpact;
import common.*;
import exceptions.InfeasibleRouteException;
import input.Instance;
import output.Route;

import java.util.*;

public class SearchUtilities {

    public static Route insertOrder(
            Instance instance, Route initialRoute, Order order, OrderInsertion orderInsertion,
            RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        Route route = new Route(initialRoute);
        route.insert(instance, order, orderInsertion);
        route.evaluate(costFunction);
        return route;
    }

    public static Route removeOrder(
            Instance instance, Route initialRoute, Integer orderId, RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        Route route = new Route(initialRoute);
        route.remove(instance, orderId);
        route.evaluate(costFunction);
        return route;
    }

    public static OrderInsertionImpact findBestOrderInsertion(
            Instance instance, Route route, Order order, RouteCostFunction costFunction){
        int numTasks = route.getTasks().size();
        double minCostDelta = Double.POSITIVE_INFINITY;
        OrderInsertion bestOrderInsertion = null;
        Route bestRoute = null;
        for (int pickUpIndex = 0; pickUpIndex < numTasks + 1; pickUpIndex++) {
            for (int deliveryIndex = pickUpIndex + 1; deliveryIndex < numTasks + 2; deliveryIndex++) {
                OrderInsertion orderInsertion = new OrderInsertion(
                        deliveryIndex, route.getDriver().getId(), order.getId(), pickUpIndex);
                Route newRoute;
                try {
                    newRoute = insertOrder(instance, route, order, orderInsertion, costFunction);
                } catch (InfeasibleRouteException e) {
                    continue;
                }
                double costDelta = newRoute.getCost() - route.getCost();
                if (costDelta < minCostDelta){
                    minCostDelta = costDelta;
                    bestOrderInsertion = orderInsertion;
                    bestRoute = newRoute;
                }
            }
        }
        return new OrderInsertionImpact(minCostDelta, bestOrderInsertion, bestRoute);
    }

}
