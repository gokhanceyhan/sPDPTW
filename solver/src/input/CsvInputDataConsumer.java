package input;

import common.Driver;
import common.Order;
import exceptions.InvalidInputException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvInputDataConsumer implements InputDataConsumer {

    private static final String DELIMITER = ",";

    private static final String DRIVER_CAPACITY_HEADER = "capacity";
    private static final String DRIVER_ID_HEADER = "driver_id";
    private static final String DRIVER_START_LOCATION_LAT_HEADER = "start_location_lat";
    private static final String DRIVER_START_LOCATION_LONG_HEADER = "start_location_long";
    private static final String DRIVER_TIME_WINDOW_END_HEADER = "shift_end_sec";
    private static final String DRIVER_TIME_WINDOW_START_HEADER = "shift_start_sec";

    private static final String ORDER_ID_HEADER = "order_id";
    private static final String ORDER_DELIVERY_LOCATION_LAT_HEADER = "customer_lat";
    private static final String ORDER_DELIVERY_LOCATION_LONG_HEADER = "customer_long";
    private static final String ORDER_DELIVERY_TIME_WINDOW_END_HEADER = "preferred_otd_sec";
    private static final String ORDER_NUM_ITEMS_HEADER = "no_of_items";
    private static final String ORDER_PICKUP_LOCATION_LAT_HEADER = "restaurant_lat";
    private static final String ORDER_PICKUP_LOCATION_LONG_HEADER = "restaurant_long";
    private static final String ORDER_PICKUP_TIME_WINDOW_START_HEADER = "prep_duration_sec";

    @Override
    public List<Driver> fetchDrivers(String dataPath) throws IOException, InvalidInputException {

        List<Driver> drivers = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(dataPath));
        String[] headers = br.readLine().split(DELIMITER);

        String line = "";
        while ((line = br.readLine()) != null) {
            String[] driverData = line.split(DELIMITER);
            Driver driver = createDriver(headers, driverData);
            drivers.add(driver);
        }
        return drivers;
    }

    @Override
    public List<Order> fetchOrders(String dataPath) throws IOException, InvalidInputException {
        List<Order> orders = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(dataPath));
        String[] headers = br.readLine().split(DELIMITER);

        String line = "";
        while ((line = br.readLine()) != null) {
            String[] orderData = line.split(DELIMITER);
            Order order = createOrder(headers, orderData);
            orders.add(order);
        }
        return orders;
    }

    private Driver createDriver(String[] driverFileHeaders, String[] driverData) throws InvalidInputException{

        Driver driver = new Driver();
        for (int i = 0; i < driverData.length ; i++) {
            String header = driverFileHeaders[i];
            if (header.startsWith("\""))
                header = header.subSequence(1, header.length() - 1).toString();

            switch (header){
                case DRIVER_ID_HEADER:
                    int id = Integer.parseInt(driverData[i]);
                    driver.setId(id);
                    break;
                case DRIVER_TIME_WINDOW_START_HEADER:
                    int timeWindowStart = Integer.parseInt(driverData[i]);
                    driver.getTimeWindow().setStart(timeWindowStart);
                    break;
                case DRIVER_TIME_WINDOW_END_HEADER:
                    int timeWindowEnd = Integer.parseInt(driverData[i]);
                    driver.getTimeWindow().setEnd(timeWindowEnd);
                    break;
                case DRIVER_START_LOCATION_LAT_HEADER:
                    double startLocationLatitude = Double.parseDouble(driverData[i]);
                    driver.getStartLocation().setLatitude(startLocationLatitude);
                    break;
                case DRIVER_START_LOCATION_LONG_HEADER:
                    double startLocationLongitude = Double.parseDouble(driverData[i]);
                    driver.getStartLocation().setLongitude(startLocationLongitude);
                    break;
                case DRIVER_CAPACITY_HEADER:
                    int capacity = Integer.parseInt(driverData[i]);
                    driver.setCapacity(capacity);
                    break;
                default:
                    throw new InvalidInputException(String.format("Unknown driver data file header %s", header));
            }
        }
        return driver;
    }

    private Order createOrder(String[] orderFileHeaders, String[] orderData) throws InvalidInputException{

        Order order = new Order();
        for (int i = 0; i < orderData.length ; i++) {
            String header = orderFileHeaders[i];
            if (header.startsWith("\""))
                header = header.subSequence(1, header.length() - 1).toString();

            switch (header){
                case ORDER_ID_HEADER:
                    int id = Integer.parseInt(orderData[i]);
                    order.setId(id);
                    break;
                case ORDER_PICKUP_LOCATION_LAT_HEADER:
                    double pickupLocationLatitude = Double.parseDouble(orderData[i]);
                    order.getPickup().getLocation().setLatitude(pickupLocationLatitude);
                    break;
                case ORDER_PICKUP_LOCATION_LONG_HEADER:
                    double pickupLocationLongitude = Double.parseDouble(orderData[i]);
                    order.getPickup().getLocation().setLongitude(pickupLocationLongitude);
                    break;
                case ORDER_DELIVERY_LOCATION_LAT_HEADER:
                    double deliveryLocationLatitude = Double.parseDouble(orderData[i]);
                    order.getDelivery().getLocation().setLatitude(deliveryLocationLatitude);
                    break;
                case ORDER_DELIVERY_LOCATION_LONG_HEADER:
                    double deliveryLocationLongitude = Double.parseDouble(orderData[i]);
                    order.getDelivery().getLocation().setLongitude(deliveryLocationLongitude);
                    break;
                case ORDER_NUM_ITEMS_HEADER:
                    int numItems = Integer.parseInt(orderData[i]);
                    order.getPickup().setNumItems(numItems);
                    order.getDelivery().setNumItems(-numItems);
                    break;
                case ORDER_PICKUP_TIME_WINDOW_START_HEADER:
                    int timeWindowStart = Integer.parseInt(orderData[i]);
                    order.getPickup().getTimeWindow().setStart(timeWindowStart);
                    break;
                case ORDER_DELIVERY_TIME_WINDOW_END_HEADER:
                    int timeWindowEnd = Integer.parseInt(orderData[i]);
                    order.getDelivery().getTimeWindow().setEnd(timeWindowEnd);
                    break;
                default:
                    throw new InvalidInputException(String.format("Unknown order data file header %s", header));
            }
        }
        return order;
    }
}
