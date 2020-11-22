package exceptions;

/**
 * This exception is thrown when the data files include invalid entries like
 * null values, values from wrong data types or illogical values.
 *
 * @author gokhanceyhan
 *
 */
public class InvalidInputException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public InvalidInputException(String s) {
        super(s);
    }
}
