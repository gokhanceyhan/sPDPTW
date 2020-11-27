package algorithms;

import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import output.Solution;

public interface RemovalHeuristic {

    PartialSolution run (Solution solution) throws InfeasibleRouteException;

    void clear();

    RemovalHeuristicType getType();
}
