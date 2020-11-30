package solver;

import algorithms.InsertionHeuristicType;

public class SimulatedAnnealingConfigurationBuilder {

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

    public SimulatedAnnealingConfigurationBuilder() {
        this.coolingRate = 0.99;
        this.constructionHeuristicType = InsertionHeuristicType.REGRET_BASED_INSERTION;
        this.numIterations = 1000;
        this.numOrdersToRemove = 10;
        this.randomizationCoefficient = 3;
        this.reactionFactor = 0.1;
        this.regretHorizon = 4;
        this.segmentSize = 100;
        this.orderSimilarityTaskCompletionTimeCoefficient = 3;
        this.orderSimilarityTaskDistanceCoefficient = 9;
        this.orderSimilarityTaskLoadCoefficient = 2;
        this.rewardForNewGlobalBestSolution = 33;
        this.rewardForLocallyImprovedSolution = 13;
        this.rewardForAcceptedSolution = 9;
        this.distanceTravelledCostWeight = 1;
        this.numLateDeliveriesCostWeight = 1e6;
        this.totalDeliveryDelayCostWeight = 1e3;
        this.travelTimeCostWeight = 0;
    }

    public SimulatedAnnealingConfigurationBuilder setCoolingRate(double coolingRate) {
        this.coolingRate = coolingRate;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setConstructionHeuristicType(
            InsertionHeuristicType constructionHeuristicType) {
        this.constructionHeuristicType = constructionHeuristicType;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setNumIterations(int numIterations) {
        this.numIterations = numIterations;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setNumOrdersToRemove(int numOrdersToRemove) {
        this.numOrdersToRemove = numOrdersToRemove;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setRandomizationCoefficient(double randomizationCoefficient) {
        this.randomizationCoefficient = randomizationCoefficient;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setReactionFactor(double reactionFactor) {
        this.reactionFactor = reactionFactor;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setRegretHorizon(int regretHorizon) {
        this.regretHorizon = regretHorizon;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setOrderSimilarityTaskCompletionTimeCoefficient(
            double orderSimilarityTaskCompletionTimeCoefficient) {
        this.orderSimilarityTaskCompletionTimeCoefficient = orderSimilarityTaskCompletionTimeCoefficient;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setOrderSimilarityTaskDistanceCoefficient(
            double orderSimilarityTaskDistanceCoefficient) {
        this.orderSimilarityTaskDistanceCoefficient = orderSimilarityTaskDistanceCoefficient;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setOrderSimilarityTaskLoadCoefficient(
            double orderSimilarityTaskLoadCoefficient) {
        this.orderSimilarityTaskLoadCoefficient = orderSimilarityTaskLoadCoefficient;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setRewardForNewGlobalBestSolution(
            double rewardForNewGlobalBestSolution) {
        this.rewardForNewGlobalBestSolution = rewardForNewGlobalBestSolution;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setRewardForLocallyImprovedSolution(
            double rewardForLocallyImprovedSolution) {
        this.rewardForLocallyImprovedSolution = rewardForLocallyImprovedSolution;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setRewardForAcceptedSolution(double rewardForAcceptedSolution) {
        this.rewardForAcceptedSolution = rewardForAcceptedSolution;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setDistanceTravelledCostWeight(double distanceTravelledCostWeight) {
        this.distanceTravelledCostWeight = distanceTravelledCostWeight;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setNumLateDeliveriesCostWeight(double numLateDeliveriesCostWeight) {
        this.numLateDeliveriesCostWeight = numLateDeliveriesCostWeight;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setTotalDeliveryDelayCostWeight(double totalDeliveryDelayCostWeight) {
        this.totalDeliveryDelayCostWeight = totalDeliveryDelayCostWeight;
        return this;
    }

    public SimulatedAnnealingConfigurationBuilder setTravelTimeCostWeight(double travelTimeCostWeight) {
        this.travelTimeCostWeight = travelTimeCostWeight;
        return this;
    }

    public SimulatedAnnealingConfiguration build(){
        return new SimulatedAnnealingConfiguration(
                coolingRate, constructionHeuristicType, numIterations, numOrdersToRemove, randomizationCoefficient,
                reactionFactor, regretHorizon, segmentSize, orderSimilarityTaskCompletionTimeCoefficient,
                orderSimilarityTaskDistanceCoefficient, orderSimilarityTaskLoadCoefficient,
                rewardForNewGlobalBestSolution, rewardForLocallyImprovedSolution, rewardForAcceptedSolution,
                distanceTravelledCostWeight, numLateDeliveriesCostWeight, totalDeliveryDelayCostWeight,
                travelTimeCostWeight);
    }
}
