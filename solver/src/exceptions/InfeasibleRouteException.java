package exceptions;

/**
 * This exception is thrown when a route is infeasible.
 *
 * @author gokhanceyhan
 *
 */
public class InfeasibleRouteException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public InfeasibleRouteException(String s) {
        super(s);
    }
}
