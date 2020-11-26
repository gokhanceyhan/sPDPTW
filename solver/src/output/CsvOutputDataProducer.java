package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class CsvOutputDataProducer implements OutputDataProducer {

    private static final String DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String DRIVER_ID_HEADER = "driver_id";
    private static final String ESTIMATED_DELIVERY_TIME_HEADER = "estimated_delivery_sec";
    private static final String ESTIMATED_PICKUP_TIME_HEADER = "estimated_pickuptime_sec";
    private static final String ORDER_ID_HEADER = "order_id";

    @Override
    public void write(Solution solution, String destination) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File(destination));
        StringBuilder sb = new StringBuilder();
        String headerLine = ORDER_ID_HEADER + DELIMITER + DRIVER_ID_HEADER + DELIMITER + ESTIMATED_PICKUP_TIME_HEADER +
                DELIMITER + ESTIMATED_DELIVERY_TIME_HEADER;
        sb.append(headerLine);
        sb.append(NEW_LINE_SEPARATOR);

        for (Map.Entry<Integer, Route> entry : solution.getDriverId2route().entrySet()) {
            int driverId = entry.getKey();
            Route route = entry.getValue();
            for (int orderId : route.getOrderIds()){
                sb.append(orderId).append(DELIMITER);
                sb.append(driverId).append(DELIMITER);
                int pickUpTaskIndex = route.getOrderId2pickupTaskIndex().get(orderId);
                int deliveryTaskIndex = route.getOrderId2deliveryTaskIndex().get(orderId);
                long estimatedPickUpTime = Math.round(route.getTaskCompletionTimes().get(pickUpTaskIndex));
                long estimatedDeliveryTime = Math.round(route.getTaskCompletionTimes().get(deliveryTaskIndex));
                sb.append(estimatedPickUpTime).append(DELIMITER);
                sb.append(estimatedDeliveryTime);
                sb.append(NEW_LINE_SEPARATOR);
            }
        }
        pw.write(sb.toString());
        pw.close();
    }
}
