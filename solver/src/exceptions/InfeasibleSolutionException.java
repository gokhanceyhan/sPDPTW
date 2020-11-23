package exceptions;

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
