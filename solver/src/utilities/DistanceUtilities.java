package utilities;

import common.Location;

public class DistanceUtilities {

    private static final double EARTH_RADIUS_IN_KM = 6378.137;

    public static double distanceInKm(Location a, Location b){
        double aLatitudeInRadians = Math.toRadians(a.getLatitude());
        double aLongitudeInRadians = Math.toRadians(a.getLongitude());
        double bLatitudeInRadians = Math.toRadians(b.getLatitude());
        double bLongitudeInRadians = Math.toRadians(b.getLongitude());
        double deltaX = Math.cos(bLatitudeInRadians) * Math.cos(bLongitudeInRadians) -
                Math.cos(aLatitudeInRadians) * Math.cos(aLongitudeInRadians);
        double deltaY = Math.cos(bLatitudeInRadians) * Math.sin(bLongitudeInRadians) -
                Math.cos(aLatitudeInRadians) * Math.sin(aLongitudeInRadians);
        double deltaZ = Math.sin(bLatitudeInRadians) - Math.sin(aLatitudeInRadians);
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) * EARTH_RADIUS_IN_KM;
    }
}
