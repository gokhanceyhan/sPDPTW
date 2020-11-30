package common;

import output.Route;

public class RouteCostFunction {

    private double distanceTravelledCostWeight;
    private double numLateDeliveriesCostWeight;
    private double totalDeliveryDelayCostWeight;
    private double travelTimeCostWeight;

    public RouteCostFunction(
            double distanceTravelledCostWeight, double numLateDeliveriesCostWeight,
            double totalDeliveryDelayCostWeight, double travelTimeCostWeight) {
        this.distanceTravelledCostWeight = distanceTravelledCostWeight;
        this.numLateDeliveriesCostWeight = numLateDeliveriesCostWeight;
        this.totalDeliveryDelayCostWeight = totalDeliveryDelayCostWeight;
        this.travelTimeCostWeight = travelTimeCostWeight;
    }

    public double calculateCost(Route route){
        int numLateDeliveries = route.getLateDeliveredOrderId2delay().size();
        double totalDeliveryDelay = 0.0;
        for (double delay : route.getLateDeliveredOrderId2delay().values())
            totalDeliveryDelay += delay;
        double travelTime = route.getTravelTime();
        double distanceTravelled = route.getDistanceTravelled();
        return numLateDeliveries * this.getNumLateDeliveriesCostWeight() +
                totalDeliveryDelay * this.getTotalDeliveryDelayCostWeight() +
                travelTime * this.getTravelTimeCostWeight() +
                distanceTravelled * this.getDistanceTravelledCostWeight();
    }

    public double getDistanceTravelledCostWeight() {
        return distanceTravelledCostWeight;
    }

    public void setDistanceTravelledCostWeight(double distanceTravelledCostWeight) {
        this.distanceTravelledCostWeight = distanceTravelledCostWeight;
    }

    public double getNumLateDeliveriesCostWeight() {
        return numLateDeliveriesCostWeight;
    }

    public void setNumLateDeliveriesCostWeight(double numLateDeliveriesCostWeight) {
        this.numLateDeliveriesCostWeight = numLateDeliveriesCostWeight;
    }

    public double getTotalDeliveryDelayCostWeight() {
        return totalDeliveryDelayCostWeight;
    }

    public void setTotalDeliveryDelayCostWeight(double totalDeliveryDelayCostWeight) {
        this.totalDeliveryDelayCostWeight = totalDeliveryDelayCostWeight;
    }

    public double getTravelTimeCostWeight() {
        return travelTimeCostWeight;
    }

    public void setTravelTimeCostWeight(double travelTimeCostWeight) {
        this.travelTimeCostWeight = travelTimeCostWeight;
    }
}
