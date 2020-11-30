package utilities;

public class TimeUtilities {

    private static final int NUM_SECONDS_PER_HOUR = 3600;

    public static double travelTimeInSeconds(double distanceInKm, double averageSpeedInKmPerHour){
        if (averageSpeedInKmPerHour > 0)
            return distanceInKm / averageSpeedInKmPerHour * NUM_SECONDS_PER_HOUR;
        return Double.POSITIVE_INFINITY;
    }
}
