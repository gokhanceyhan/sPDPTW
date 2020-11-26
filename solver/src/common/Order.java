package common;

import java.util.Objects;

public class Order {

    private Task delivery;
    private int id;
    private Task pickup;

    public Order() {
        this.delivery = new Task(TaskType.DELIVERY);
        this.pickup = new Task(TaskType.PICKUP);
    }

    public Order(Task delivery, int id, Task pickup) {
        this.delivery = delivery;
        this.id = id;
        this.pickup = pickup;
    }

    public Task getDelivery() {
        return delivery;
    }

    public void setDelivery(Task delivery) {
        this.delivery = delivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.getDelivery().setOrderId(id);
        this.getPickup().setOrderId(id);
    }

    public Task getPickup() {
        return pickup;
    }

    public void setPickup(Task pickup) {
        this.pickup = pickup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
