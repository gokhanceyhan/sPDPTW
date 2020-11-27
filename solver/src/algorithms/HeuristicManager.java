package algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HeuristicManager {

    private final static Random random = new Random(0);

    private double reactionFactor;
    private HeuristicScoringFunction heuristicScoringFunction;
    private Map<InsertionHeuristicType, InsertionHeuristic> insertionHeuristicType2heuristic;
    private Map<InsertionHeuristicType, HeuristicStatistics> insertionHeuristicType2statistics;
    private Map<InsertionHeuristicType, Double> insertionHeuristicType2weight;
    private Map<RemovalHeuristicType, RemovalHeuristic> removalHeuristicType2heuristic;
    private Map<RemovalHeuristicType, HeuristicStatistics> removalHeuristicType2statistics;
    private Map<RemovalHeuristicType, Double> removalHeuristicType2weight;

    public HeuristicManager(
            Map<InsertionHeuristicType, InsertionHeuristic> insertionHeuristicType2heuristic,
            Map<RemovalHeuristicType, RemovalHeuristic> removalHeuristicType2heuristic, double reactionFactor,
            HeuristicScoringFunction heuristicScoringFunction) {
        this.insertionHeuristicType2heuristic = insertionHeuristicType2heuristic;
        this.removalHeuristicType2heuristic = removalHeuristicType2heuristic;
        assert (reactionFactor >= 0 & reactionFactor <= 1);
        this.reactionFactor = reactionFactor;
        this.heuristicScoringFunction = heuristicScoringFunction;
        initialize();
    }

    public InsertionHeuristic selectInsertionHeuristic(){
        int numHeuristics = this.getInsertionHeuristicType2heuristic().size();
        InsertionHeuristicType[] types = new InsertionHeuristicType[numHeuristics];
        double[] weights = new double[numHeuristics];
        double[] cumulativeProbabilities = new double[numHeuristics];

        double totalWeight = 0.0;
        for (double weight : weights) {
            totalWeight += weight;
        }
        int index = 0;
        double cumulativeProbability = 0.0;
        for (Map.Entry<InsertionHeuristicType, Double> entry: this.getInsertionHeuristicType2weight().entrySet()){
            types[index] = entry.getKey();
            weights[index] = entry.getValue();
            double relativeWeight = entry.getValue() / totalWeight;
            cumulativeProbability += relativeWeight;
            cumulativeProbabilities[index] = cumulativeProbability;
            index++;
        }
        double randomVariate = random.nextDouble();
        InsertionHeuristicType selectedHeuristicType = null;
        for (int i = 0; i < cumulativeProbabilities.length ; i++) {
            if (randomVariate <= cumulativeProbabilities[i]){
                selectedHeuristicType = types[i];
                break;
            }
        }
        return this.getInsertionHeuristicType2heuristic().get(selectedHeuristicType);
    }

    public RemovalHeuristic selectRemovalHeuristic(){
        int numHeuristics = this.getRemovalHeuristicType2heuristic().size();
        RemovalHeuristicType[] types = new RemovalHeuristicType[numHeuristics];
        double[] weights = new double[numHeuristics];
        double[] cumulativeProbabilities = new double[numHeuristics];

        double totalWeight = 0.0;
        for (double weight : weights) {
            totalWeight += weight;
        }
        int index = 0;
        double cumulativeProbability = 0.0;
        for (Map.Entry<RemovalHeuristicType, Double> entry: this.getRemovalHeuristicType2weight().entrySet()){
            types[index] = entry.getKey();
            weights[index] = entry.getValue();
            double relativeWeight = entry.getValue() / totalWeight;
            cumulativeProbability += relativeWeight;
            cumulativeProbabilities[index] = cumulativeProbability;
            index++;
        }
        double randomVariate = random.nextDouble();
        RemovalHeuristicType selectedHeuristicType = null;
        for (int i = 0; i < cumulativeProbabilities.length ; i++) {
            if (randomVariate <= cumulativeProbabilities[i]){
                selectedHeuristicType = types[i];
                break;
            }
        }
        return this.getRemovalHeuristicType2heuristic().get(selectedHeuristicType);
    }

    public void clearHeuristicStatistics(){
        for (Map.Entry<InsertionHeuristicType, HeuristicStatistics> entry:
                this.getInsertionHeuristicType2statistics().entrySet()){
            HeuristicStatistics statistics = entry.getValue();
            statistics.clear();
        }
        for (Map.Entry<RemovalHeuristicType, HeuristicStatistics> entry:
                this.getRemovalHeuristicType2statistics().entrySet()){
            HeuristicStatistics statistics = entry.getValue();
            statistics.clear();
        }
    }

    public void updateHeuristicStatistics(LocalSearchResult localSearchResult){
        InsertionHeuristicType insertionHeuristicType = localSearchResult.getInsertionHeuristicType();
        RemovalHeuristicType removalHeuristicType = localSearchResult.getRemovalHeuristicType();
        HeuristicStatistics insertionHeuristicStatistics = this.getInsertionHeuristicType2statistics().get(
                insertionHeuristicType);
        HeuristicStatistics removalHeuristicStatistics = this.getRemovalHeuristicType2statistics().get(
                removalHeuristicType);
        insertionHeuristicStatistics.incrementNumTimeUsed();
        removalHeuristicStatistics.incrementNumTimeUsed();
        if (localSearchResult.isNewGlobalBestSolution()){
            insertionHeuristicStatistics.incrementNumTimesWithNewGlobalBestSolution();
            removalHeuristicStatistics.incrementNumTimesWithNewGlobalBestSolution();
        }
        else if (localSearchResult.isLocallyImprovedSolution()){
            insertionHeuristicStatistics.incrementNumTimesWithLocallyImprovedSolution();
            removalHeuristicStatistics.incrementNumTimesWithLocallyImprovedSolution();
        }
        else if (localSearchResult.isNewSolution()){
            insertionHeuristicStatistics.incrementNumTimesWithAcceptedSolution();
            removalHeuristicStatistics.incrementNumTimesWithAcceptedSolution();
        }
        insertionHeuristicStatistics.setScore(this.getHeuristicScoringFunction());
        removalHeuristicStatistics.setScore(this.getHeuristicScoringFunction());
    }

    public void updateHeuristicWeights(){

        Map<InsertionHeuristicType, Double> updatedInsertionHeuristicType2weight = new HashMap<>();
        for (Map.Entry<InsertionHeuristicType, Double> entry: this.getInsertionHeuristicType2weight().entrySet()){
            InsertionHeuristicType heuristicType = entry.getKey();
            double currentWeight = entry.getValue();
            HeuristicStatistics statistics = this.getInsertionHeuristicType2statistics().get(heuristicType);
            double updatedWeight = currentWeight * (1 - this.getReactionFactor()) + this.getReactionFactor() * (
                    statistics.getScore() / statistics.getNumTimesUsed());
            updatedInsertionHeuristicType2weight.put(heuristicType, updatedWeight);
        }
        this.setInsertionHeuristicType2weight(updatedInsertionHeuristicType2weight);

        Map<RemovalHeuristicType, Double> updatedRemovalHeuristicType2weight = new HashMap<>();
        for (Map.Entry<RemovalHeuristicType, Double> entry: this.getRemovalHeuristicType2weight().entrySet()){
            RemovalHeuristicType heuristicType = entry.getKey();
            double currentWeight = entry.getValue();
            HeuristicStatistics statistics = this.getRemovalHeuristicType2statistics().get(heuristicType);
            double updatedWeight = currentWeight * (1 - this.getReactionFactor()) + this.getReactionFactor() * (
                    statistics.getScore() / statistics.getNumTimesUsed());
            updatedRemovalHeuristicType2weight.put(heuristicType, updatedWeight);
        }
        this.setRemovalHeuristicType2weight(updatedRemovalHeuristicType2weight);
    }

    private void initialize(){
        int numInsertionHeuristics = this.getInsertionHeuristicType2heuristic().size();
        int numRemovalHeuristics = this.getRemovalHeuristicType2heuristic().size();
        assert numInsertionHeuristics > 0;
        assert numRemovalHeuristics > 0;

        double insertionHeuristicWeight = (double) 1 / numInsertionHeuristics;
        double removalHeuristicWeight = (double) 1 / numRemovalHeuristics;

        Map<InsertionHeuristicType, HeuristicStatistics> insertionHeuristicType2statistics = new HashMap<>();
        Map<InsertionHeuristicType, Double> insertionHeuristicType2weight = new HashMap<>();
        Map<RemovalHeuristicType, HeuristicStatistics> removalHeuristicType2statistics = new HashMap<>();
        Map<RemovalHeuristicType, Double> removalHeuristicType2weight = new HashMap<>();

        for (InsertionHeuristicType insertionHeuristicType : this.getInsertionHeuristicType2heuristic().keySet()){
            insertionHeuristicType2statistics.put(insertionHeuristicType, new HeuristicStatistics());
            insertionHeuristicType2weight.put(insertionHeuristicType, insertionHeuristicWeight);
        }
        for (RemovalHeuristicType removalHeuristicType : this.getRemovalHeuristicType2heuristic().keySet()){
            removalHeuristicType2statistics.put(removalHeuristicType, new HeuristicStatistics());
            removalHeuristicType2weight.put(removalHeuristicType, removalHeuristicWeight);
        }

        this.setInsertionHeuristicType2statistics(insertionHeuristicType2statistics);
        this.setInsertionHeuristicType2weight(insertionHeuristicType2weight);
        this.setRemovalHeuristicType2statistics(removalHeuristicType2statistics);
        this.setRemovalHeuristicType2weight(removalHeuristicType2weight);
    }

    public HeuristicScoringFunction getHeuristicScoringFunction() {
        return heuristicScoringFunction;
    }

    public void setHeuristicScoringFunction(HeuristicScoringFunction heuristicScoringFunction) {
        this.heuristicScoringFunction = heuristicScoringFunction;
    }

    public Map<InsertionHeuristicType, InsertionHeuristic> getInsertionHeuristicType2heuristic() {
        return insertionHeuristicType2heuristic;
    }

    public void setInsertionHeuristicType2heuristic(
            Map<InsertionHeuristicType, InsertionHeuristic> insertionHeuristicType2heuristic) {
        this.insertionHeuristicType2heuristic = insertionHeuristicType2heuristic;
    }

    public Map<InsertionHeuristicType, HeuristicStatistics> getInsertionHeuristicType2statistics() {
        return insertionHeuristicType2statistics;
    }

    public void setInsertionHeuristicType2statistics(
            Map<InsertionHeuristicType, HeuristicStatistics> insertionHeuristicType2statistics) {
        this.insertionHeuristicType2statistics = insertionHeuristicType2statistics;
    }

    public Map<InsertionHeuristicType, Double> getInsertionHeuristicType2weight() {
        return insertionHeuristicType2weight;
    }

    public void setInsertionHeuristicType2weight(Map<InsertionHeuristicType, Double> insertionHeuristicType2weight) {
        this.insertionHeuristicType2weight = insertionHeuristicType2weight;
    }

    public double getReactionFactor() {
        return reactionFactor;
    }

    public void setReactionFactor(double reactionFactor) {
        this.reactionFactor = reactionFactor;
    }

    public Map<RemovalHeuristicType, RemovalHeuristic> getRemovalHeuristicType2heuristic() {
        return removalHeuristicType2heuristic;
    }

    public void setRemovalHeuristicType2heuristic(
            Map<RemovalHeuristicType, RemovalHeuristic> removalHeuristicType2heuristic) {
        this.removalHeuristicType2heuristic = removalHeuristicType2heuristic;
    }

    public Map<RemovalHeuristicType, HeuristicStatistics> getRemovalHeuristicType2statistics() {
        return removalHeuristicType2statistics;
    }

    public void setRemovalHeuristicType2statistics(
            Map<RemovalHeuristicType, HeuristicStatistics> removalHeuristicType2statistics) {
        this.removalHeuristicType2statistics = removalHeuristicType2statistics;
    }

    public Map<RemovalHeuristicType, Double> getRemovalHeuristicType2weight() {
        return removalHeuristicType2weight;
    }

    public void setRemovalHeuristicType2weight(Map<RemovalHeuristicType, Double> removalHeuristicType2weight) {
        this.removalHeuristicType2weight = removalHeuristicType2weight;
    }
}
