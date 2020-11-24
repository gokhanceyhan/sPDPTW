package algorithms;

import input.Instance;
import output.Solution;

public interface LocalSearch {

    Solution run (Instance instance, Solution initialSolution);

}
