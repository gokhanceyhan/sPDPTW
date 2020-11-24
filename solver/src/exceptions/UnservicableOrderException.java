package exceptions;

public class UnservicableOrderException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param s
     *            Exception message.
     */
    public UnservicableOrderException(String s) {
        super(s);
    }
}
