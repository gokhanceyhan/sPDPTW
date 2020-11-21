package input;

import common.Driver;
import common.Order;
import exceptions.InvalidInputException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface InputDataConsumer {

    List<Driver> fetchDrivers(String dataPath) throws IOException, InvalidInputException;

    List<Order> fetchOrders(String dataPath) throws IOException, InvalidInputException;
}
