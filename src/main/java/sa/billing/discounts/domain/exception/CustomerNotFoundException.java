package sa.billing.discounts.domain.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String message) {
        super(message);
    }
    
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}