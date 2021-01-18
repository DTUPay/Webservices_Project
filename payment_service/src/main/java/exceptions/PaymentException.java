package exceptions;

/**
 * Exceptions made from payment service
 * @author Mikkel Rosenfeldt Anderson
 */
public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
