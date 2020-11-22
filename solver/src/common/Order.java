package common;

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
}
