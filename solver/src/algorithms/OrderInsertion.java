package algorithms;

public class OrderInsertion {

    /**
     * Task index denotes the index of an arc of the route under consideration, where initial arc from the start
     * location of the driver to the first task and the final arc from the last task to the end location of the driver
     */
    private int deliveryTaskIndex;
    private int driverId;
    private int orderId;
    private int pickUpTaskIndex;

    public OrderInsertion(int deliveryTaskIndex, int driverId, int orderId, int pickUpTaskIndex) {
        this.deliveryTaskIndex = deliveryTaskIndex;
        this.driverId = driverId;
        this.orderId = orderId;
        this.pickUpTaskIndex = pickUpTaskIndex;
    }

    public int getDeliveryTaskIndex() {
        return deliveryTaskIndex;
    }

    public void setDeliveryTaskIndex(int deliveryTaskIndex) {
        this.deliveryTaskIndex = deliveryTaskIndex;
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

    public int getPickUpTaskIndex() {
        return pickUpTaskIndex;
    }

    public void setPickUpTaskIndex(int pickUpTaskIndex) {
        this.pickUpTaskIndex = pickUpTaskIndex;
    }
}
