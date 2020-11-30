package algorithms;

import common.ScalingFunction;

public class OrderSimilarityFunction {

    private double taskCompletionTimeCoefficient;
    private ScalingFunction taskCompletionTimeDifferenceScalingFunction;
    private double taskDistanceCoefficient;
    private ScalingFunction taskDistanceScalingFunction;
    private double taskLoadCoefficient;
    private ScalingFunction taskLoadDifferenceScalingFunction;

    public OrderSimilarityFunction(
            ScalingFunction taskCompletionTimeDifferenceScalingFunction, ScalingFunction taskDistanceScalingFunction,
            ScalingFunction taskLoadDifferenceScalingFunction, double taskCompletionTimeCoefficient,
            double taskDistanceCoefficient, double taskLoadCoefficient) {
        this.taskCompletionTimeCoefficient = taskCompletionTimeCoefficient;
        this.taskCompletionTimeDifferenceScalingFunction = taskCompletionTimeDifferenceScalingFunction;
        this.taskDistanceCoefficient = taskDistanceCoefficient;
        this.taskDistanceScalingFunction = taskDistanceScalingFunction;
        this.taskLoadCoefficient = taskLoadCoefficient;
        this.taskLoadDifferenceScalingFunction = taskLoadDifferenceScalingFunction;
    }

    public double calculateSimilarityValue(OrderSimilarity orderSimilarity){
        double taskCompletionTimeTerm = this.getTaskCompletionTimeDifferenceScalingFunction().scale(
                orderSimilarity.getDifferenceBetweenPickUpTimes()) +
                this.getTaskCompletionTimeDifferenceScalingFunction().scale(
                        orderSimilarity.getDifferenceBetweenDeliveryTimes());
        double taskDistanceTerm = this.getTaskDistanceScalingFunction().scale(
                orderSimilarity.getDistanceBetweenPickUpTasks()) +
                this.getTaskDistanceScalingFunction().scale(orderSimilarity.getDistanceBetweenDeliveryTasks());
        double taskLoadTerm = this.getTaskLoadDifferenceScalingFunction().scale(
                orderSimilarity.getDifferentBetweenLoads());
        return this.getTaskCompletionTimeCoefficient() * taskCompletionTimeTerm +
                this.getTaskDistanceCoefficient() * taskDistanceTerm +
                this.getTaskLoadCoefficient() * taskLoadTerm;
    }

    public double getTaskCompletionTimeCoefficient() {
        return taskCompletionTimeCoefficient;
    }

    public void setTaskCompletionTimeCoefficient(double taskCompletionTimeCoefficient) {
        this.taskCompletionTimeCoefficient = taskCompletionTimeCoefficient;
    }

    public ScalingFunction getTaskCompletionTimeDifferenceScalingFunction() {
        return taskCompletionTimeDifferenceScalingFunction;
    }

    public void setTaskCompletionTimeDifferenceScalingFunction(
            ScalingFunction taskCompletionTimeDifferenceScalingFunction) {
        this.taskCompletionTimeDifferenceScalingFunction = taskCompletionTimeDifferenceScalingFunction;
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

    public ScalingFunction getTaskLoadDifferenceScalingFunction() {
        return taskLoadDifferenceScalingFunction;
    }

    public void setTaskLoadDifferenceScalingFunction(ScalingFunction taskLoadDifferenceScalingFunction) {
        this.taskLoadDifferenceScalingFunction = taskLoadDifferenceScalingFunction;
    }
}
