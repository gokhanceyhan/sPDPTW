package algorithms;

import exceptions.UnserviceableOrderException;
import input.Instance;
import output.Solution;

public interface HeuristicAlgorithm {

    Solution run(Instance instance) throws UnserviceableOrderException;
}
