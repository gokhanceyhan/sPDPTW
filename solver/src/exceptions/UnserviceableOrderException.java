package exceptions;

/**
 * This exception is thrown when an order cannot be assigned to any driver.
 *
 * @author gokhanceyhan
 *
 */
public class UnserviceableOrderException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public UnserviceableOrderException(String s) {
        super(s);
    }
}
