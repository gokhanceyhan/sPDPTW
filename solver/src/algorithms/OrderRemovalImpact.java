package algorithms;

public class OrderRemovalImpact implements Comparable<OrderRemovalImpact> {

    private int driverId;
    private int orderId;
    private double costDelta;

    public OrderRemovalImpact(int driverId, int orderId, double costDelta) {
        this.driverId = driverId;
        this.orderId = orderId;
        this.costDelta = costDelta;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getCostDelta() {
        return costDelta;
    }

    public void setCostDelta(double costDelta) {
        this.costDelta = costDelta;
    }

    @Override
    public int compareTo(OrderRemovalImpact o) {
        return Double.compare(this.getCostDelta(), o.getCostDelta());
    }
}
