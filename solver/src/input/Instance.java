package input;

import common.Driver;
import common.Order;

import java.util.List;

public class Instance {

    private List<Driver> drivers;
    private List<Order> orders;

    public Instance(List<Driver> drivers, List<Order> orders) {
        this.drivers = drivers;
        this.orders = orders;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
