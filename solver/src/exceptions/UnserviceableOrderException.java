package exceptions;

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
