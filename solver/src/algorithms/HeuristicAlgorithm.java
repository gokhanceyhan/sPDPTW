package algorithms;

import exceptions.UnservicableOrderException;
import input.Instance;
import output.Solution;

public interface HeuristicAlgorithm {

    Solution run(Instance instance) throws UnservicableOrderException;
}
