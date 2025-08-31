package sa.billing.discounts.infrastructure.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import sa.billing.discounts.domain.exception.CustomerNotFoundException;
import sa.billing.discounts.domain.exception.ProductNotFoundException;
import sa.billing.discounts.presentation.dto.ApiResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private BindingResult bindingResult;

    @Test
    void shouldHandleCustomerNotFoundException() {
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer not found with ID: 123");
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleCustomerNotFoundException(exception, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Customer not found with ID: 123", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("CUSTOMER_NOT_FOUND", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found with ID: 456");
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleProductNotFoundException(exception, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Product not found with ID: 456", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("PRODUCT_NOT_FOUND", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError1 = new FieldError("billRequest", "customerId", "Customer ID is required");
        FieldError fieldError2 = new FieldError("billRequest", "items", "Items cannot be empty");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(exception.getMessage()).thenReturn("Validation failed");
        
        ResponseEntity<ApiResponse<Map<String, String>>> response = globalExceptionHandler.handleValidationExceptions(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        
        Map<String, String> errors = response.getBody().getData();
        assertEquals("Customer ID is required", errors.get("customerId"));
        assertEquals("Items cannot be empty", errors.get("items"));
        assertEquals("VALIDATION_ERROR", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionWithSingleError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("billRequest", "customerId", "Customer ID is required");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(exception.getMessage()).thenReturn("Validation failed");
        
        ResponseEntity<ApiResponse<Map<String, String>>> response = globalExceptionHandler.handleValidationExceptions(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        
        Map<String, String> errors = response.getBody().getData();
        assertEquals("Customer ID is required", errors.get("customerId"));
        assertEquals("VALIDATION_ERROR", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionWithNoErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());
        when(exception.getMessage()).thenReturn("Validation failed");
        
        ResponseEntity<ApiResponse<Map<String, String>>> response = globalExceptionHandler.handleValidationExceptions(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input parameter");
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Invalid input parameter", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("INVALID_ARGUMENT", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleNoResourceFoundException() {
        NoResourceFoundException exception = new NoResourceFoundException(null, "Resource not found");
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleNoResourceFoundException(exception, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("NOT_FOUND", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException exception = new RuntimeException("Unexpected error occurred");
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleGenericException(exception, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleGenericExceptionWithNullMessage() {
        RuntimeException exception = new RuntimeException();
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleGenericException(exception, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleCustomerNotFoundExceptionWithNullMessage() {
        CustomerNotFoundException exception = new CustomerNotFoundException(null);
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleCustomerNotFoundException(exception, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        assertEquals("CUSTOMER_NOT_FOUND", response.getBody().getMeta().get("errorCode"));
    }

    @Test
    void shouldHandleProductNotFoundExceptionWithNullMessage() {
        ProductNotFoundException exception = new ProductNotFoundException(null);
        
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleProductNotFoundException(exception, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        assertEquals("PRODUCT_NOT_FOUND", response.getBody().getMeta().get("errorCode"));
    }
}