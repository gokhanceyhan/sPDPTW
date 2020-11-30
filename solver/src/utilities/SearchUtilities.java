package utilities;

import algorithms.OrderInsertion;
import algorithms.OrderInsertionImpact;
import common.*;
import exceptions.InfeasibleRouteException;
import exceptions.UnserviceableOrderException;
import output.Route;

public class SearchUtilities {

    public static Route insertOrder(
            Route initialRoute, Order order, OrderInsertion orderInsertion, RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        Route route = new Route(initialRoute);
        route.insert(order, orderInsertion);
        route.evaluate(costFunction);
        return route;
    }

    public static Route removeOrder(Route initialRoute, Integer orderId, RouteCostFunction costFunction) throws
            InfeasibleRouteException {
        Route route = new Route(initialRoute);
        route.remove(orderId);
        route.evaluate(costFunction);
        return route;
    }

    public static OrderInsertionImpact findBestOrderInsertion(
            Route route, Order order, RouteCostFunction costFunction) throws InfeasibleRouteException {
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
                    newRoute = insertOrder(route, order, orderInsertion, costFunction);
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
        if (bestRoute == null)
            throw new InfeasibleRouteException(
                    String.format(
                            "Unable to find an insertion point for the order %d in the route of driver %d",
                            order.getId(), route.getDriver().getId()));
        return new OrderInsertionImpact(minCostDelta, bestOrderInsertion, bestRoute);
    }

}
