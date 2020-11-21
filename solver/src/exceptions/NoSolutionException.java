/**
 *
 */
package exceptions;

/**
 * This exception is thrown when no solution can be found for the given input
 * data.
 *
 * @author gokhanceyhan
 *
 */
public class NoSolutionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public NoSolutionException(String s) {
        super(s);
    }

}
