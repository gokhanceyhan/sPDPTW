package algorithms;

import common.ScalingFunction;

public class OrderSimilarityFunction {

    private double taskCompletionTimeCoefficient;
    private ScalingFunction taskCompletionTimeScalingFunction;
    private double taskDistanceCoefficient;
    private ScalingFunction taskDistanceScalingFunction;
    private double taskLoadCoefficient;
    private ScalingFunction taskLoadScalingFunction;

    public OrderSimilarityFunction(
            ScalingFunction taskCompletionTimeScalingFunction, ScalingFunction taskDistanceScalingFunction,
            ScalingFunction taskLoadScalingFunction, double taskCompletionTimeCoefficient,
            double taskDistanceCoefficient, double taskLoadCoefficient) {
        this.taskCompletionTimeCoefficient = taskCompletionTimeCoefficient;
        this.taskCompletionTimeScalingFunction = taskCompletionTimeScalingFunction;
        this.taskDistanceCoefficient = taskDistanceCoefficient;
        this.taskDistanceScalingFunction = taskDistanceScalingFunction;
        this.taskLoadCoefficient = taskLoadCoefficient;
        this.taskLoadScalingFunction = taskLoadScalingFunction;
    }

    public double calculateSimilarityValue(OrderSimilarity orderSimilarity){
        double taskCompletionTimeTerm = this.getTaskCompletionTimeCoefficient() * (
                this.getTaskCompletionTimeScalingFunction().scale(orderSimilarity.getDifferenceBetweenPickUpTimes()) +
                        this.getTaskCompletionTimeScalingFunction().scale(
                                orderSimilarity.getDifferenceBetweenDeliveryTimes()));
        double taskDistanceTerm = this.getTaskDistanceCoefficient() * (
                this.getTaskDistanceScalingFunction().scale(orderSimilarity.getDistanceBetweenPickUpTasks()) +
                        this.getTaskDistanceScalingFunction().scale(orderSimilarity.getDistanceBetweenDeliveryTasks()));
        double taskLoadTerm = this.getTaskLoadCoefficient() *
                this.getTaskLoadScalingFunction().scale(orderSimilarity.getDifferentBetweenLoads());
        return taskCompletionTimeTerm + taskDistanceTerm + taskLoadTerm;
    }

    public double getTaskCompletionTimeCoefficient() {
        return taskCompletionTimeCoefficient;
    }

    public void setTaskCompletionTimeCoefficient(double taskCompletionTimeCoefficient) {
        this.taskCompletionTimeCoefficient = taskCompletionTimeCoefficient;
    }

    public ScalingFunction getTaskCompletionTimeScalingFunction() {
        return taskCompletionTimeScalingFunction;
    }

    public void setTaskCompletionTimeScalingFunction(ScalingFunction taskCompletionTimeScalingFunction) {
        this.taskCompletionTimeScalingFunction = taskCompletionTimeScalingFunction;
    }

    public double getTaskDistanceCoefficient() {
        return taskDistanceCoefficient;
    }

    public void setTaskDistanceCoefficient(double taskDistanceCoefficient) {
        this.taskDistanceCoefficient = taskDistanceCoefficient;
    }

    public ScalingFunction getTaskDistanceScalingFunction() {
        return taskDistanceScalingFunction;
    }

    public void setTaskDistanceScalingFunction(ScalingFunction taskDistanceScalingFunction) {
        this.taskDistanceScalingFunction = taskDistanceScalingFunction;
    }

    public double getTaskLoadCoefficient() {
        return taskLoadCoefficient;
    }

    public void setTaskLoadCoefficient(double taskLoadCoefficient) {
        this.taskLoadCoefficient = taskLoadCoefficient;
    }

    public ScalingFunction getTaskLoadScalingFunction() {
        return taskLoadScalingFunction;
    }

    public void setTaskLoadScalingFunction(ScalingFunction taskLoadScalingFunction) {
        this.taskLoadScalingFunction = taskLoadScalingFunction;
    }
}
