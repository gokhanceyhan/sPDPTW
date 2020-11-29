package common;

public class Arc {

    private ArcId id;
    private double distance;
    private double travelTime;

    public Arc(ArcId id, double distance, double travelTime) {
        this.id = id;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public ArcId getId() {
        return id;
    }

    public void setId(ArcId id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }
}
