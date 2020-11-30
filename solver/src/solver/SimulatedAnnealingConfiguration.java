package solver;

import algorithms.InsertionHeuristicType;

public class SimulatedAnnealingConfiguration {

    private double coolingRate;
    private InsertionHeuristicType constructionHeuristicType;
    private int numIterations;
    private int numOrdersToRemove;
    private double randomizationCoefficient;
    private double reactionFactor;
    private int regretHorizon;
    private int segmentSize;

    //order similarity related parameters
    private double orderSimilarityTaskCompletionTimeCoefficient;
    private double orderSimilarityTaskDistanceCoefficient;
    private double orderSimilarityTaskLoadCoefficient;

    // heuristic scoring function coefficients
    private double rewardForNewGlobalBestSolution;
    private double rewardForLocallyImprovedSolution;
    private double rewardForAcceptedSolution;

    // objective function weights
    private double distanceTravelledCostWeight;
    private double numLateDeliveriesCostWeight;
    private double totalDeliveryDelayCostWeight;
    private double travelTimeCostWeight;

    public SimulatedAnnealingConfiguration(
            double coolingRate, InsertionHeuristicType constructionHeuristicType, int numIterations,
            int numOrdersToRemove, double randomizationCoefficient, double reactionFactor, int regretHorizon,
            int segmentSize, double orderSimilarityTaskCompletionTimeCoefficient,
            double orderSimilarityTaskDistanceCoefficient, double orderSimilarityTaskLoadCoefficient,
            double rewardForNewGlobalBestSolution, double rewardForLocallyImprovedSolution,
            double rewardForAcceptedSolution, double distanceTravelledCostWeight, double numLateDeliveriesCostWeight,
            double totalDeliveryDelayCostWeight, double travelTimeCostWeight) {
        this.coolingRate = coolingRate;
        this.constructionHeuristicType = constructionHeuristicType;
        this.numIterations = numIterations;
        this.numOrdersToRemove = numOrdersToRemove;
        this.randomizationCoefficient = randomizationCoefficient;
        this.reactionFactor = reactionFactor;
        this.regretHorizon = regretHorizon;
        this.segmentSize = segmentSize;
        this.orderSimilarityTaskCompletionTimeCoefficient = orderSimilarityTaskCompletionTimeCoefficient;
        this.orderSimilarityTaskDistanceCoefficient = orderSimilarityTaskDistanceCoefficient;
        this.orderSimilarityTaskLoadCoefficient = orderSimilarityTaskLoadCoefficient;
        this.rewardForNewGlobalBestSolution = rewardForNewGlobalBestSolution;
        this.rewardForLocallyImprovedSolution = rewardForLocallyImprovedSolution;
        this.rewardForAcceptedSolution = rewardForAcceptedSolution;
        this.distanceTravelledCostWeight = distanceTravelledCostWeight;
        this.numLateDeliveriesCostWeight = numLateDeliveriesCostWeight;
        this.totalDeliveryDelayCostWeight = totalDeliveryDelayCostWeight;
        this.travelTimeCostWeight = travelTimeCostWeight;
    }

    public double getCoolingRate() {
        return coolingRate;
    }

    public void setCoolingRate(double coolingRate) {
        this.coolingRate = coolingRate;
    }

    public InsertionHeuristicType getConstructionHeuristicType() {
        return constructionHeuristicType;
    }

    public void setConstructionHeuristicType(InsertionHeuristicType constructionHeuristicType) {
        this.constructionHeuristicType = constructionHeuristicType;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public int getNumOrdersToRemove() {
        return numOrdersToRemove;
    }

    public void setNumOrdersToRemove(int numOrdersToRemove) {
        this.numOrdersToRemove = numOrdersToRemove;
    }

    public double getRandomizationCoefficient() {
        return randomizationCoefficient;
    }

    public void setRandomizationCoefficient(double randomizationCoefficient) {
        this.randomizationCoefficient = randomizationCoefficient;
    }

    public double getReactionFactor() {
        return reactionFactor;
    }

    public void setReactionFactor(double reactionFactor) {
        this.reactionFactor = reactionFactor;
    }

    public int getRegretHorizon() {
        return regretHorizon;
    }

    public void setRegretHorizon(int regretHorizon) {
        this.regretHorizon = regretHorizon;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public double getOrderSimilarityTaskCompletionTimeCoefficient() {
        return orderSimilarityTaskCompletionTimeCoefficient;
    }

    public void setOrderSimilarityTaskCompletionTimeCoefficient(double orderSimilarityTaskCompletionTimeCoefficient) {
        this.orderSimilarityTaskCompletionTimeCoefficient = orderSimilarityTaskCompletionTimeCoefficient;
    }

    public double getOrderSimilarityTaskDistanceCoefficient() {
        return orderSimilarityTaskDistanceCoefficient;
    }

    public void setOrderSimilarityTaskDistanceCoefficient(double orderSimilarityTaskDistanceCoefficient) {
        this.orderSimilarityTaskDistanceCoefficient = orderSimilarityTaskDistanceCoefficient;
    }

    public double getOrderSimilarityTaskLoadCoefficient() {
        return orderSimilarityTaskLoadCoefficient;
    }

    public void setOrderSimilarityTaskLoadCoefficient(double orderSimilarityTaskLoadCoefficient) {
        this.orderSimilarityTaskLoadCoefficient = orderSimilarityTaskLoadCoefficient;
    }

    public double getRewardForNewGlobalBestSolution() {
        return rewardForNewGlobalBestSolution;
    }

    public void setRewardForNewGlobalBestSolution(double rewardForNewGlobalBestSolution) {
        this.rewardForNewGlobalBestSolution = rewardForNewGlobalBestSolution;
    }

    public double getRewardForLocallyImprovedSolution() {
        return rewardForLocallyImprovedSolution;
    }

    public void setRewardForLocallyImprovedSolution(double rewardForLocallyImprovedSolution) {
        this.rewardForLocallyImprovedSolution = rewardForLocallyImprovedSolution;
    }

    public double getRewardForAcceptedSolution() {
        return rewardForAcceptedSolution;
    }

    public void setRewardForAcceptedSolution(double rewardForAcceptedSolution) {
        this.rewardForAcceptedSolution = rewardForAcceptedSolution;
    }

    public double getDistanceTravelledCostWeight() {
        return distanceTravelledCostWeight;
    }

    public void setDistanceTravelledCostWeight(double distanceTravelledCostWeight) {
        this.distanceTravelledCostWeight = distanceTravelledCostWeight;
    }

    public double getNumLateDeliveriesCostWeight() {
        return numLateDeliveriesCostWeight;
    }

    public void setNumLateDeliveriesCostWeight(double numLateDeliveriesCostWeight) {
        this.numLateDeliveriesCostWeight = numLateDeliveriesCostWeight;
    }

    public double getTotalDeliveryDelayCostWeight() {
        return totalDeliveryDelayCostWeight;
    }

    public void setTotalDeliveryDelayCostWeight(double totalDeliveryDelayCostWeight) {
        this.totalDeliveryDelayCostWeight = totalDeliveryDelayCostWeight;
    }

    public double getTravelTimeCostWeight() {
        return travelTimeCostWeight;
    }

    public void setTravelTimeCostWeight(double travelTimeCostWeight) {
        this.travelTimeCostWeight = travelTimeCostWeight;
    }
}
