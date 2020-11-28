package algorithms;

import exceptions.InfeasibleRouteException;
import exceptions.InfeasibleSolutionException;
import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Solution;

public class LocalSearch {

    private Instance instance;
    private InsertionHeuristic insertionHeuristic;
    private RemovalHeuristic removalHeuristic;

    public LocalSearch(Instance instance) {
        this.instance = instance;
    }

    public Solution run (Solution solution) throws
            UnserviceableOrderException, InfeasibleRouteException, InfeasibleSolutionException {
        Solution initialSolution = new Solution(solution);
        RemovalHeuristic removalHeuristic = this.getRemovalHeuristic();
        InsertionHeuristic insertionHeuristic = this.getInsertionHeuristic();
        removalHeuristic.clear();
        insertionHeuristic.clear();
        PartialSolution partialSolution = removalHeuristic.run(initialSolution);
        partialSolution.validate(this.getInstance());
        Solution candidateSolution = insertionHeuristic.run(partialSolution);
        candidateSolution.validate(this.getInstance());
        return candidateSolution;
    }

    public InsertionHeuristic getInsertionHeuristic() {
        return insertionHeuristic;
    }

    public void setInsertionHeuristic(InsertionHeuristic insertionHeuristic) {
        this.insertionHeuristic = insertionHeuristic;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public RemovalHeuristic getRemovalHeuristic() {
        return removalHeuristic;
    }

    public void setRemovalHeuristic(RemovalHeuristic removalHeuristic) {
        this.removalHeuristic = removalHeuristic;
    }
}
