package algorithms;

import exceptions.InfeasibleRouteException;
import exceptions.InfeasibleSolutionException;
import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Solution;

public interface HeuristicAlgorithm {

    Solution run() throws UnserviceableOrderException, InfeasibleRouteException, InfeasibleSolutionException;
}
