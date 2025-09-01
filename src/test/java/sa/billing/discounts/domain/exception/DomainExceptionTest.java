package sa.billing.discounts.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void shouldCreateCustomerNotFoundException() {
        String customerId = "customer123";
        CustomerNotFoundException exception = new CustomerNotFoundException(customerId);
        
        assertNotNull(exception);
        assertEquals("customer123", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldCreateCustomerNotFoundExceptionWithNullId() {
        CustomerNotFoundException exception = new CustomerNotFoundException(null);
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateCustomerNotFoundExceptionWithEmptyId() {
        CustomerNotFoundException exception = new CustomerNotFoundException("");
        
        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void shouldCreateProductNotFoundException() {
        String productId = "product456";
        ProductNotFoundException exception = new ProductNotFoundException(productId);
        
        assertNotNull(exception);
        assertEquals("product456", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldCreateProductNotFoundExceptionWithNullId() {
        ProductNotFoundException exception = new ProductNotFoundException(null);
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateProductNotFoundExceptionWithEmptyId() {
        ProductNotFoundException exception = new ProductNotFoundException("");
        
        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void shouldThrowAndCatchCustomerNotFoundException() {
        String customerId = "nonexistent123";
        
        assertThrows(CustomerNotFoundException.class, () -> {
            throw new CustomerNotFoundException(customerId);
        });
    }

    @Test
    void shouldThrowAndCatchProductNotFoundException() {
        String productId = "nonexistent456";
        
        assertThrows(ProductNotFoundException.class, () -> {
            throw new ProductNotFoundException(productId);
        });
    }

    @Test
    void shouldGetMessageFromCustomerNotFoundException() {
        CustomerNotFoundException exception = new CustomerNotFoundException("test-id");
        String message = exception.getMessage();
        
        assertNotNull(message);
        assertTrue(message.contains("test-id"));
        assertTrue(message.contains("test-id"));
    }

    @Test
    void shouldGetMessageFromProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException("test-product");
        String message = exception.getMessage();
        
        assertNotNull(message);
        assertTrue(message.contains("test-product"));
        assertTrue(message.contains("test-product"));
    }

    @Test
    void shouldCreateCustomerNotFoundExceptionWithLongId() {
        String longId = "very-long-customer-id-with-many-characters-1234567890";
        CustomerNotFoundException exception = new CustomerNotFoundException(longId);
        
        assertTrue(exception.getMessage().contains(longId));
    }

    @Test
    void shouldCreateProductNotFoundExceptionWithLongId() {
        String longId = "very-long-product-id-with-many-characters-abcdefghijklmnop";
        ProductNotFoundException exception = new ProductNotFoundException(longId);
        
        assertTrue(exception.getMessage().contains(longId));
    }

    @Test
    void shouldCreateExceptionsWithSpecialCharacters() {
        String specialId = "id@#$%^&*()";
        
        CustomerNotFoundException customerException = new CustomerNotFoundException(specialId);
        ProductNotFoundException productException = new ProductNotFoundException(specialId);
        
        assertTrue(customerException.getMessage().contains(specialId));
        assertTrue(productException.getMessage().contains(specialId));
    }

    @Test
    void shouldHaveProperInheritanceHierarchy() {
        CustomerNotFoundException customerException = new CustomerNotFoundException("test");
        ProductNotFoundException productException = new ProductNotFoundException("test");
        
        assertTrue(customerException instanceof RuntimeException);
        assertTrue(productException instanceof RuntimeException);
        assertTrue(customerException instanceof Exception);
        assertTrue(productException instanceof Exception);
    }
}