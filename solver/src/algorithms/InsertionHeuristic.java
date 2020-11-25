package algorithms;

import common.RouteCostFunction;
import exceptions.UnserviceableOrderException;
import output.Solution;

public interface InsertionHeuristic {

    Solution run (PartialSolution partialSolution) throws UnserviceableOrderException;

}
