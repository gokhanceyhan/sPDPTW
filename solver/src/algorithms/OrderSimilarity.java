package algorithms;

public class OrderSimilarity implements Comparable<OrderSimilarity> {

    private double differenceBetweenDeliveryTimes;
    private double differentBetweenLoads;
    private double differenceBetweenPickUpTimes;
    private double distanceBetweenDeliveryTasks;
    private double distanceBetweenPickUpTasks;
    private int firstOrderId;
    private int secondOrderId;
    private double similarityValue;

    public OrderSimilarity(
            double differenceBetweenDeliveryTimes, double differentBetweenLoads, double differenceBetweenPickUpTimes,
            double distanceBetweenDeliveryTasks, double distanceBetweenPickUpTasks, int firstOrderId,
            int secondOrderId) {
        this.differenceBetweenDeliveryTimes = differenceBetweenDeliveryTimes;
        this.differentBetweenLoads = differentBetweenLoads;
        this.differenceBetweenPickUpTimes = differenceBetweenPickUpTimes;
        this.distanceBetweenDeliveryTasks = distanceBetweenDeliveryTasks;
        this.distanceBetweenPickUpTasks = distanceBetweenPickUpTasks;
        this.firstOrderId = firstOrderId;
        this.secondOrderId = secondOrderId;
    }

    public double getDifferenceBetweenDeliveryTimes() {
        return differenceBetweenDeliveryTimes;
    }

    public void setDifferenceBetweenDeliveryTimes(double differenceBetweenDeliveryTimes) {
        this.differenceBetweenDeliveryTimes = differenceBetweenDeliveryTimes;
    }

    public double getDifferentBetweenLoads() {
        return differentBetweenLoads;
    }

    public void setDifferentBetweenLoads(double differentBetweenLoads) {
        this.differentBetweenLoads = differentBetweenLoads;
    }

    public double getDifferenceBetweenPickUpTimes() {
        return differenceBetweenPickUpTimes;
    }

    public void setDifferenceBetweenPickUpTimes(double differenceBetweenPickUpTimes) {
        this.differenceBetweenPickUpTimes = differenceBetweenPickUpTimes;
    }

    public double getDistanceBetweenDeliveryTasks() {
        return distanceBetweenDeliveryTasks;
    }

    public void setDistanceBetweenDeliveryTasks(double distanceBetweenDeliveryTasks) {
        this.distanceBetweenDeliveryTasks = distanceBetweenDeliveryTasks;
    }

    public double getDistanceBetweenPickUpTasks() {
        return distanceBetweenPickUpTasks;
    }

    public void setDistanceBetweenPickUpTasks(double distanceBetweenPickUpTasks) {
        this.distanceBetweenPickUpTasks = distanceBetweenPickUpTasks;
    }

    public int getFirstOrderId() {
        return firstOrderId;
    }

    public void setFirstOrderId(int firstOrderId) {
        this.firstOrderId = firstOrderId;
    }

    public int getSecondOrderId() {
        return secondOrderId;
    }

    public void setSecondOrderId(int secondOrderId) {
        this.secondOrderId = secondOrderId;
    }

    public double getSimilarityValue() {
        return similarityValue;
    }

    public void setSimilarityValue(double similarityValue) {
        this.similarityValue = similarityValue;
    }

    public void setSimilarityValue(OrderSimilarityFunction orderSimilarityFunction) {
        this.similarityValue = orderSimilarityFunction.calculateSimilarityValue(this);
    }

    @Override
    public int compareTo(OrderSimilarity o) {
        return Double.compare(this.getSimilarityValue(), o.getSimilarityValue());
    }
}
