package sa.billing.discounts.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.application.dto.BillItemResponse;
import sa.billing.discounts.presentation.dto.ApiResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@DisplayName("DTO Tests")
class DtoTest {

    @Test
    @DisplayName("Should create BillItemRequest with all fields")
    void shouldCreateBillItemRequestWithAllFields() {
        // When
        BillItemRequest request = new BillItemRequest("product1", 5, new BigDecimal("99.99"));
        
        // Then
        assertEquals("product1", request.getProductId());
        assertEquals(5, request.getQuantity());
        assertEquals(new BigDecimal("99.99"), request.getUnitPrice());
    }

    @Test
    @DisplayName("Should create BillItemRequest without unit price")
    void shouldCreateBillItemRequestWithoutUnitPrice() {
        // When
        BillItemRequest request = new BillItemRequest("product1", 3);
        
        // Then
        assertEquals("product1", request.getProductId());
        assertEquals(3, request.getQuantity());
        assertNull(request.getUnitPrice());
    }

    @Test
    @DisplayName("Should create BillCalculationRequest")
    void shouldCreateBillCalculationRequest() {
        // Given
        List<BillItemRequest> items = Arrays.asList(
            new BillItemRequest("prod1", 2),
            new BillItemRequest("prod2", 1, new BigDecimal("50.00"))
        );
        
        // When
        BillCalculationRequest request = new BillCalculationRequest("customer1", items);
        
        // Then
        assertEquals("customer1", request.getCustomerId());
        assertEquals(2, request.getItems().size());
        assertEquals("prod1", request.getItems().get(0).getProductId());
        assertEquals(2, request.getItems().get(0).getQuantity());
    }

    @Test
    @DisplayName("Should create BillItemResponse")
    void shouldCreateBillItemResponse() {
        // When
        BillItemResponse response = new BillItemResponse(
            "prod1", "Product Name", "ELECTRONICS", 2, 
            new BigDecimal("100.00"), new BigDecimal("200.00"), true
        );
        
        // Then
        assertEquals("prod1", response.getProductId());
        assertEquals("Product Name", response.getProductName());
        assertEquals("ELECTRONICS", response.getCategory());
        assertEquals(2, response.getQuantity());
        assertEquals(new BigDecimal("100.00"), response.getUnitPrice());
        assertEquals(new BigDecimal("200.00"), response.getTotalPrice());
        assertTrue(response.isEligibleForPercentageDiscount());
    }

    @Test
    @DisplayName("Should create BillCalculationResponse")
    void shouldCreateBillCalculationResponse() {
        // Given
        List<BillItemResponse> items = Arrays.asList(
            new BillItemResponse("prod1", "Product 1", "ELECTRONICS", 1, 
                new BigDecimal("100.00"), new BigDecimal("100.00"), true),
            new BillItemResponse("prod2", "Product 2", "GROCERY", 2, 
                new BigDecimal("25.00"), new BigDecimal("50.00"), false)
        );
        
        // When
        BillCalculationResponse response = new BillCalculationResponse(
            "customer1", items, new BigDecimal("150.00"), new BigDecimal("30.00"),
            "EMPLOYEE", new BigDecimal("5.00"), new BigDecimal("35.00"), new BigDecimal("115.00")
        );
        
        // Then
        assertEquals("customer1", response.getCustomerId());
        assertEquals(2, response.getItems().size());
        assertEquals(new BigDecimal("150.00"), response.getSubtotal());
        assertEquals(new BigDecimal("30.00"), response.getPercentageBasedDiscount());
        assertEquals("EMPLOYEE", response.getPercentageDiscountType());
        assertEquals(new BigDecimal("5.00"), response.getBillBasedDiscount());
        assertEquals(new BigDecimal("35.00"), response.getTotalDiscount());
        assertEquals(new BigDecimal("115.00"), response.getNetAmount());
    }

    @Test
    @DisplayName("Should create successful ApiResponse")
    void shouldCreateSuccessfulApiResponse() {
        // Given
        String data = "Test Data";
        
        // When
        ApiResponse<String> response = ApiResponse.success("Operation successful", data);
        
        // Then
        assertEquals("success", response.getStatus());
        assertEquals("Operation successful", response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getMeta());
    }

    @Test
    @DisplayName("Should create error ApiResponse")
    void shouldCreateErrorApiResponse() {
        // When
        ApiResponse<String> response = ApiResponse.error("Something went wrong");
        
        // Then
        assertEquals("fail", response.getStatus());
        assertEquals("Something went wrong", response.getMessage());
        assertNull(response.getData());
        assertNull(response.getMeta());
    }

    @Test
    @DisplayName("Should handle ApiResponse with null data")
    void shouldHandleApiResponseWithNullData() {
        // When
        ApiResponse<String> response = ApiResponse.success("Success with no data", null);
        
        // Then
        assertEquals("success", response.getStatus());
        assertEquals("Success with no data", response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getMeta());
    }

    @Test
    @DisplayName("Should test BillItemRequest equality")
    void shouldTestBillItemRequestEquality() {
        // Given
        BillItemRequest request1 = new BillItemRequest("prod1", 2, new BigDecimal("50.00"));
        BillItemRequest request2 = new BillItemRequest("prod1", 2, new BigDecimal("50.00"));
        BillItemRequest request3 = new BillItemRequest("prod2", 2, new BigDecimal("50.00"));
        
        // Then
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should test BillItemResponse toString")
    void shouldTestBillItemResponseToString() {
        // Given
        BillItemResponse response = new BillItemResponse(
            "prod1", "Test Product", "ELECTRONICS", 1,
            new BigDecimal("100.00"), new BigDecimal("100.00"), true
        );
        
        // When
        String toString = response.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("ELECTRONICS"));
    }

    @Test
    @DisplayName("Should handle BillCalculationRequest with empty items")
    void shouldHandleBillCalculationRequestWithEmptyItems() {
        // When
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList());
        
        // Then
        assertEquals("customer1", request.getCustomerId());
        assertTrue(request.getItems().isEmpty());
    }

    @Test
    @DisplayName("Should test BillItemRequest with zero quantity")
    void shouldTestBillItemRequestWithZeroQuantity() {
        // When
        BillItemRequest request = new BillItemRequest("prod1", 0);
        
        // Then
        assertEquals("prod1", request.getProductId());
        assertEquals(0, request.getQuantity());
    }

    @Test
    @DisplayName("Should test BillCalculationResponse with null discount type")
    void shouldTestBillCalculationResponseWithNullDiscountType() {
        // Given
        List<BillItemResponse> items = Arrays.asList(
            new BillItemResponse("prod1", "Product 1", "GROCERY", 1, 
                new BigDecimal("50.00"), new BigDecimal("50.00"), false)
        );
        
        // When
        BillCalculationResponse response = new BillCalculationResponse(
            "customer1", items, new BigDecimal("50.00"), new BigDecimal("0.00"),
            null, new BigDecimal("5.00"), new BigDecimal("5.00"), new BigDecimal("45.00")
        );
        
        // Then
        assertEquals("customer1", response.getCustomerId());
        assertNull(response.getPercentageDiscountType());
        assertEquals(new BigDecimal("0.00"), response.getPercentageBasedDiscount());
    }

    @Test
    @DisplayName("Should test BillItemResponse with grocery category")
    void shouldTestBillItemResponseWithGroceryCategory() {
        // When
        BillItemResponse response = new BillItemResponse(
            "food1", "Groceries", "GROCERY", 3,
            new BigDecimal("20.00"), new BigDecimal("60.00"), false
        );
        
        // Then
        assertEquals("GROCERY", response.getCategory());
        assertFalse(response.isEligibleForPercentageDiscount());
    }

    @Test
    @DisplayName("Should handle large numbers in calculations")
    void shouldHandleLargeNumbersInCalculations() {
        // Given
        BillItemResponse response = new BillItemResponse(
            "expensive", "Expensive Item", "ELECTRONICS", 100,
            new BigDecimal("999.99"), new BigDecimal("99999.00"), true
        );
        
        // Then
        assertEquals(new BigDecimal("999.99"), response.getUnitPrice());
        assertEquals(new BigDecimal("99999.00"), response.getTotalPrice());
        assertEquals(100, response.getQuantity());
    }
}