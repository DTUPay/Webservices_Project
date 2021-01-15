package exceptions;

/**
 * Exceptions made from payment service
 * @author Mikkel
 */
public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
