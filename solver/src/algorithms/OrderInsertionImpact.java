package algorithms;

import output.Route;

public class OrderInsertionImpact {

    private double costDelta;
    private OrderInsertion orderInsertion;
    private Route route;

    public OrderInsertionImpact(double costDelta, OrderInsertion orderInsertion, Route route) {
        this.costDelta = costDelta;
        this.orderInsertion = orderInsertion;
        this.route = route;
    }

    public double getCostDelta() {
        return costDelta;
    }

    public void setCostDelta(double costDelta) {
        this.costDelta = costDelta;
    }

    public OrderInsertion getOrderInsertion() {
        return orderInsertion;
    }

    public void setOrderInsertion(OrderInsertion orderInsertion) {
        this.orderInsertion = orderInsertion;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
