package algorithms;

import common.RouteCostFunction;
import output.Solution;

public interface InsertionHeuristic {

    Solution run (PartialSolution partialSolution, RouteCostFunction routeCostFunction);

}
