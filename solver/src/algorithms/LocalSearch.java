package algorithms;

import common.RouteCostFunction;
import exceptions.InfeasibleRouteException;
import input.Instance;
import output.Solution;

import java.util.Map;

public interface LocalSearch {

    Solution run (Solution initialSolution, RouteCostFunction routeCostFunction) throws InfeasibleRouteException;

}
