package exceptions;

/**
 * Exceptions are re-thrown from FastPay
 * @author Mikkel
 */
public class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}
