package exceptions;

/**
 * This exception is thrown when a solution is infeasible.
 *
 * @author gokhanceyhan
 *
 */
public class InfeasibleSolutionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public InfeasibleSolutionException(String s) {
        super(s);
    }
}
