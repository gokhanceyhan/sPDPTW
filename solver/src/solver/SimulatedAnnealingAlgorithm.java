package solver;

import algorithms.*;
import common.RouteCostFunction;
import common.ScalingFunction;
import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Route;
import output.Solution;

import java.util.HashMap;
import java.util.Map;

public class SimulatedAnnealingAlgorithm implements HeuristicAlgorithm {

    private AnnealingScheme annealingScheme;
    private Solution bestSolution;
    private SimulatedAnnealingConfiguration configuration;
    private HeuristicManager heuristicManager;
    private HeuristicScoringFunction heuristicScoringFunction;
    private Instance instance;
    private LocalSearch localSearch;
    private OrderSimilarityFunction orderSimilarityFunction;
    private RouteCostFunction routeCostFunction;


    public SimulatedAnnealingAlgorithm(Instance instance, SimulatedAnnealingConfiguration configuration) {
        this.configuration = configuration;
        this.instance = instance;
        initialize();
    }

    @Override
    public Solution run(Instance instance) throws UnserviceableOrderException {
        Solution bestSolution = findInitialSolution();

        int iterationIndex = 1;
        while (iterationIndex < this.getConfiguration().getNumIterations()){

            iterationIndex ++;
        }

        return null;
    }

    private Solution findInitialSolution() throws UnserviceableOrderException {
        InsertionHeuristicType heuristicType = this.getConfiguration().getConstructionHeuristicType();
        InsertionHeuristic heuristic = null;
        if (heuristicType.equals(InsertionHeuristicType.GREEDY_INSERTION))
            heuristic = new GreedyInsertionHeuristic(this.getInstance(), this.getRouteCostFunction());
        else if (heuristicType.equals(InsertionHeuristicType.REGRET_BASED_INSERTION))
            heuristic = new RegretBasedInsertionHeuristic(
                    this.getInstance(), this.getRouteCostFunction(), this.getConfiguration().getRegretHorizon());
        else
            assert false;
        PartialSolution partialSolution = new PartialSolution(this.getInstance().getOrders());
        return heuristic.run(partialSolution);
    }

    private void initialize(){
        initializeHeuristicScoringFunction();
        initializeOrderSimilarityFunction();
        initializeRouteCostFunction();
        initializeHeuristicManager();
    }

    private void initializeHeuristicManager(){
        // register the insertion heuristics
        Map<InsertionHeuristicType, InsertionHeuristic> insertionHeuristicType2heuristic = new HashMap<>();
        insertionHeuristicType2heuristic.put(
                InsertionHeuristicType.GREEDY_INSERTION,
                new GreedyInsertionHeuristic(this.getInstance(), this.getRouteCostFunction()));
        insertionHeuristicType2heuristic.put(
                InsertionHeuristicType.REGRET_BASED_INSERTION,
                new RegretBasedInsertionHeuristic(
                        this.getInstance(), this.getRouteCostFunction(), this.getConfiguration().getRegretHorizon()));
        // register the removal heuristics
        Map<RemovalHeuristicType, RemovalHeuristic> removalHeuristicType2heuristic = new HashMap<>();
        removalHeuristicType2heuristic.put(
                RemovalHeuristicType.RANDOM_REMOVAL,
                new RandomRemovalHeuristic(
                        this.getInstance(), this.getConfiguration().getNumOrdersToRemove(),
                        this.getRouteCostFunction()));
        removalHeuristicType2heuristic.put(
                RemovalHeuristicType.GREEDY_REMOVAL,
                new GreedyRemovalHeuristic(
                        this.getInstance(), this.getConfiguration().getNumOrdersToRemove(),
                        this.getConfiguration().getRandomizationCoefficient(), this.getRouteCostFunction()));
        removalHeuristicType2heuristic.put(
                RemovalHeuristicType.SHAW_REMOVAL,
                new ShawRemovalHeuristic(
                        this.getInstance(), this.getConfiguration().getNumOrdersToRemove(),
                        this.getOrderSimilarityFunction(), this.getConfiguration().getRandomizationCoefficient(),
                        this.getRouteCostFunction()));

        HeuristicManager heuristicManager = new HeuristicManager(
                insertionHeuristicType2heuristic, removalHeuristicType2heuristic,
                this.getConfiguration().getReactionFactor(), this.getHeuristicScoringFunction());
        this.setHeuristicManager(heuristicManager);
    }

    private void initializeHeuristicScoringFunction(){
        HeuristicScoringFunction heuristicScoringFunction = new HeuristicScoringFunction(
                this.getConfiguration().getRewardForAcceptedSolution(),
                this.getConfiguration().getRewardForLocallyImprovedSolution(),
                this.getConfiguration().getRewardForNewGlobalBestSolution());
        this.setHeuristicScoringFunction(heuristicScoringFunction);
    }

    private void initializeOrderSimilarityFunction(){
        ScalingFunction taskCompletionTimeDifferenceScalingFunction = new ScalingFunction(1e-4, 0);
        ScalingFunction taskDistanceScalingFunction = new ScalingFunction(100, 0);
        ScalingFunction taskLoadDifferenceScalingFunction = new ScalingFunction(20, 0);
        OrderSimilarityFunction orderSimilarityFunction = new OrderSimilarityFunction(
                taskCompletionTimeDifferenceScalingFunction, taskDistanceScalingFunction,
                taskLoadDifferenceScalingFunction,
                this.getConfiguration().getOrderSimilarityTaskCompletionTimeCoefficient(),
                this.getConfiguration().getOrderSimilarityTaskDistanceCoefficient(),
                this.getConfiguration().getOrderSimilarityTaskLoadCoefficient());
        this.setOrderSimilarityFunction(orderSimilarityFunction);
    }

    private void initializeRouteCostFunction(){
        RouteCostFunction routeCostFunction = new RouteCostFunction(
                this.getConfiguration().getDistanceTravelledCostWeight(),
                this.getConfiguration().getNumLateDeliveriesCostWeight(),
                this.getConfiguration().getTotalDeliveryDelayCostWeight(),
                this.getConfiguration().getTravelTimeCostWeight());
        this.setRouteCostFunction(routeCostFunction);
    }

    public AnnealingScheme getAnnealingScheme() {
        return annealingScheme;
    }

    public void setAnnealingScheme(AnnealingScheme annealingScheme) {
        this.annealingScheme = annealingScheme;
    }

    public Solution getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Solution bestSolution) {
        this.bestSolution = bestSolution;
    }

    public HeuristicScoringFunction getHeuristicScoringFunction() {
        return heuristicScoringFunction;
    }

    public void setHeuristicScoringFunction(HeuristicScoringFunction heuristicScoringFunction) {
        this.heuristicScoringFunction = heuristicScoringFunction;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public LocalSearch getLocalSearch() {
        return localSearch;
    }

    public void setLocalSearch(LocalSearch localSearch) {
        this.localSearch = localSearch;
    }

    public HeuristicManager getHeuristicManager() {
        return heuristicManager;
    }

    public void setHeuristicManager(HeuristicManager heuristicManager) {
        this.heuristicManager = heuristicManager;
    }

    public SimulatedAnnealingConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SimulatedAnnealingConfiguration configuration) {
        this.configuration = configuration;
    }

    public OrderSimilarityFunction getOrderSimilarityFunction() {
        return orderSimilarityFunction;
    }

    public void setOrderSimilarityFunction(OrderSimilarityFunction orderSimilarityFunction) {
        this.orderSimilarityFunction = orderSimilarityFunction;
    }

    public RouteCostFunction getRouteCostFunction() {
        return routeCostFunction;
    }

    public void setRouteCostFunction(RouteCostFunction routeCostFunction) {
        this.routeCostFunction = routeCostFunction;
    }
}
