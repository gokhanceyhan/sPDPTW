package algorithms;

import exceptions.NoSolutionException;
import output.Solution;

public interface HeuristicAlgorithm {

    Solution run() throws NoSolutionException;
}
