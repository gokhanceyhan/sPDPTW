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

    public Solution run (Solution initialSolution) throws
            UnserviceableOrderException, InfeasibleRouteException, InfeasibleSolutionException {
        PartialSolution partialSolution = this.getRemovalHeuristic().run(initialSolution);
        Solution solution = this.getInsertionHeuristic().run(partialSolution);
        solution.validate(this.getInstance());
        return solution;
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
