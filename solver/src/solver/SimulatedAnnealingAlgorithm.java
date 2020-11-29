package solver;

import algorithms.*;
import common.RouteCostFunction;
import common.ScalingFunction;
import exceptions.InfeasibleRouteException;
import exceptions.InfeasibleSolutionException;
import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Route;
import output.Solution;

import java.util.*;

public class SimulatedAnnealingAlgorithm implements HeuristicAlgorithm {

    private final static Random random = new Random(0);

    private AnnealingScheme annealingScheme;
    private Solution bestSolution;
    private SimulatedAnnealingConfiguration configuration;
    private List<Solution> generatedSolutions;
    private HeuristicManager heuristicManager;
    private HeuristicScoringFunction heuristicScoringFunction;
    private Instance instance;
    private LocalSearch localSearch;
    private OrderSimilarityFunction orderSimilarityFunction;
    private RouteCostFunction routeCostFunction;


    public SimulatedAnnealingAlgorithm(Instance instance, SimulatedAnnealingConfiguration configuration) {
        this.configuration = configuration;
        this.instance = instance;
        this.generatedSolutions = new ArrayList<>();
        initialize();
    }

    @Override
    public Solution run() throws UnserviceableOrderException, InfeasibleRouteException,
            InfeasibleSolutionException {
        Solution solution = findInitialSolution();
        System.out.println(String.format("Initial solution with cost %.2f: ", solution.getCost()));
        addSolution(solution);
        initializeAnnealingScheme(solution);
        this.setBestSolution(new Solution(solution));
        int iterationIndex = 1;
        while (iterationIndex < this.getConfiguration().getNumIterations()){
            if (iterationIndex % this.getConfiguration().getSegmentSize() == 0){
                System.out.println(String.format("Running iteration %d...", iterationIndex));
                this.getHeuristicManager().updateHeuristicWeights();
                this.getHeuristicManager().clearHeuristicStatistics();
            }
            InsertionHeuristic insertionHeuristic = this.getHeuristicManager().selectInsertionHeuristic();
            RemovalHeuristic removalHeuristic = this.getHeuristicManager().selectRemovalHeuristic();
            LocalSearch localSearch = this.getLocalSearch();
            localSearch.setInsertionHeuristic(insertionHeuristic);
            localSearch.setRemovalHeuristic(removalHeuristic);
            Solution candidateSolution = localSearch.run(solution);
            LocalSearchResult localSearchResult = createLocalSearchResult(solution, candidateSolution);
            this.getHeuristicManager().updateHeuristicStatistics(localSearchResult);
            if (localSearchResult.isNewGlobalBestSolution()) {
                this.setBestSolution(candidateSolution);
                System.out.println(String.format("New global best solution: %.2f", candidateSolution.getCost()));
            }
            if (localSearchResult.isNewGlobalBestSolution() || localSearchResult.isLocallyImprovedSolution() ||
                    localSearchResult.isNewSolution())
                addSolution(candidateSolution);
                solution = candidateSolution;
            this.getAnnealingScheme().updateTemperature();
            iterationIndex ++;
        }
        /*
        System.out.println(String.format("Ending temperature %.2f", this.getAnnealingScheme().getCurrentTemperature()));
        */
        return this.getBestSolution();
    }

    private void addSolution(Solution solution){
        this.generatedSolutions.add(solution);
    }

    private LocalSearchResult createLocalSearchResult(Solution currentSolution, Solution candidateSolution){
        if (this.generatedSolutions.contains(candidateSolution))
            return new LocalSearchResult(this.getLocalSearch().getInsertionHeuristic().getType(),
                    this.getLocalSearch().getRemovalHeuristic().getType(), false,
                    false, false);

        boolean newGlobalBestSolution = false;
        boolean locallyImprovedSolution = false;
        boolean newSolution = false;

        if (candidateSolution.getCost() < this.getBestSolution().getCost())
            newGlobalBestSolution = true;
        else if (candidateSolution.getCost() < currentSolution.getCost())
            locallyImprovedSolution = true;
        else {
            double acceptProbability = this.getAnnealingScheme().calculateAcceptProbability(
                    currentSolution.getCost(), candidateSolution.getCost());
            double randomVariate = random.nextDouble();
            if (randomVariate <= acceptProbability)
                newSolution = true;
        }
        return new LocalSearchResult(
                this.getLocalSearch().getInsertionHeuristic().getType(),
                this.getLocalSearch().getRemovalHeuristic().getType(), newGlobalBestSolution, locallyImprovedSolution,
                newSolution);
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
        initializeLocalSearch();
    }

    private void initializeAnnealingScheme(Solution initialSolution){
        double w = 0.05;
        double p = 0.5;
        double startTemperature = w * initialSolution.getCost() / Math.log(1.0 / p);
        AnnealingScheme annealingScheme = new AnnealingScheme(
                this.getConfiguration().getCoolingRate(), startTemperature);
        this.setAnnealingScheme(annealingScheme);
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

    private void initializeLocalSearch(){
        LocalSearch localSearch = new LocalSearch(this.getInstance());
        this.setLocalSearch(localSearch);
    }

    private void initializeOrderSimilarityFunction(){
        ScalingFunction taskCompletionTimeDifferenceScalingFunction = new ScalingFunction(2000, 0);
        ScalingFunction taskDistanceScalingFunction = new ScalingFunction(10, 0);
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
