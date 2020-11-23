package common;

import java.util.Objects;

public class OrderIdAndDriverId {

    private int driverId;
    private int orderId;

    public OrderIdAndDriverId(int driverId, int orderId) {
        this.driverId = driverId;
        this.orderId = orderId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderIdAndDriverId that = (OrderIdAndDriverId) o;
        return getDriverId() == that.getDriverId() &&
                getOrderId() == that.getOrderId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDriverId(), getOrderId());
    }
}
