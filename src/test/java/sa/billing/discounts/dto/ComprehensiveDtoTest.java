package sa.billing.discounts.dto;

import org.junit.jupiter.api.Test;
import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.application.dto.BillItemResponse;
import sa.billing.discounts.presentation.dto.ApiResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComprehensiveDtoTest {

    @Test
    void shouldCreateBillItemRequestWithAllProperties() {
        BillItemRequest request = new BillItemRequest();
        request.setProductId("prod-123");
        request.setQuantity(5);
        
        assertEquals("prod-123", request.getProductId());
        assertEquals(5, request.getQuantity());
    }

    @Test
    void shouldCreateBillItemRequestUsingConstructor() {
        BillItemRequest request = new BillItemRequest("prod-456", 3);
        
        assertEquals("prod-456", request.getProductId());
        assertEquals(3, request.getQuantity());
    }

    @Test
    void shouldTestBillItemRequestEquality() {
        BillItemRequest request1 = new BillItemRequest("prod-123", 5);
        BillItemRequest request2 = new BillItemRequest("prod-123", 5);
        BillItemRequest request3 = new BillItemRequest("prod-456", 3);
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, "string");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldTestBillItemRequestToString() {
        BillItemRequest request = new BillItemRequest("prod-123", 5);
        String result = request.toString();
        
        assertTrue(result.contains("prod-123"));
        assertTrue(result.contains("5"));
        assertTrue(result.contains("BillItemRequest"));
    }

    @Test
    void shouldCreateBillCalculationRequestWithAllProperties() {
        BillItemRequest item1 = new BillItemRequest("prod-1", 2);
        BillItemRequest item2 = new BillItemRequest("prod-2", 1);
        List<BillItemRequest> items = List.of(item1, item2);
        
        BillCalculationRequest request = new BillCalculationRequest();
        request.setCustomerId("cust-123");
        request.setItems(items);
        
        assertEquals("cust-123", request.getCustomerId());
        assertEquals(2, request.getItems().size());
        assertEquals("prod-1", request.getItems().get(0).getProductId());
    }

    @Test
    void shouldCreateBillCalculationRequestUsingConstructor() {
        BillItemRequest item = new BillItemRequest("prod-1", 2);
        BillCalculationRequest request = new BillCalculationRequest("cust-123", List.of(item));
        
        assertEquals("cust-123", request.getCustomerId());
        assertEquals(1, request.getItems().size());
    }

    @Test
    void shouldTestBillCalculationRequestEquality() {
        BillItemRequest item = new BillItemRequest("prod-1", 2);
        BillCalculationRequest request1 = new BillCalculationRequest("cust-123", List.of(item));
        BillCalculationRequest request2 = new BillCalculationRequest("cust-123", List.of(item));
        BillCalculationRequest request3 = new BillCalculationRequest("cust-456", List.of(item));
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, "string");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldTestBillCalculationRequestToString() {
        BillItemRequest item = new BillItemRequest("prod-1", 2);
        BillCalculationRequest request = new BillCalculationRequest("cust-123", List.of(item));
        String result = request.toString();
        
        assertTrue(result.contains("cust-123"));
        assertTrue(result.contains("prod-1"));
        assertTrue(result.contains("BillCalculationRequest"));
    }

    @Test
    void shouldCreateBillItemResponseWithAllProperties() {
        BillItemResponse response = new BillItemResponse();
        response.setProductId("prod-123");
        response.setProductName("Test Product");
        response.setCategory("ELECTRONICS");
        response.setQuantity(3);
        response.setUnitPrice(new BigDecimal("10.99"));
        response.setTotalPrice(new BigDecimal("32.97"));
        response.setEligibleForPercentageDiscount(true);
        
        assertEquals("prod-123", response.getProductId());
        assertEquals("Test Product", response.getProductName());
        assertEquals("ELECTRONICS", response.getCategory());
        assertEquals(3, response.getQuantity());
        assertEquals(new BigDecimal("10.99"), response.getUnitPrice());
        assertEquals(new BigDecimal("32.97"), response.getTotalPrice());
        assertTrue(response.isEligibleForPercentageDiscount());
    }

    @Test
    void shouldCreateBillItemResponseUsingConstructor() {
        BillItemResponse response = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 3, 
                                                        new BigDecimal("10.99"), new BigDecimal("32.97"), true);
        
        assertEquals("prod-123", response.getProductId());
        assertEquals("Test Product", response.getProductName());
        assertEquals("ELECTRONICS", response.getCategory());
        assertEquals(3, response.getQuantity());
        assertEquals(new BigDecimal("10.99"), response.getUnitPrice());
        assertEquals(new BigDecimal("32.97"), response.getTotalPrice());
        assertTrue(response.isEligibleForPercentageDiscount());
    }

    @Test
    void shouldTestBillItemResponseEquality() {
        BillItemResponse response1 = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 3, 
                                                         new BigDecimal("10.99"), new BigDecimal("32.97"), true);
        BillItemResponse response2 = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 3, 
                                                         new BigDecimal("10.99"), new BigDecimal("32.97"), true);
        BillItemResponse response3 = new BillItemResponse("prod-456", "Other Product", "BOOKS", 1, 
                                                         new BigDecimal("5.99"), new BigDecimal("5.99"), false);
        
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);
        assertNotEquals(response1, "string");
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldTestBillItemResponseToString() {
        BillItemResponse response = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 3, 
                                                        new BigDecimal("10.99"), new BigDecimal("32.97"), true);
        String result = response.toString();
        
        assertTrue(result.contains("prod-123"));
        assertTrue(result.contains("Test Product"));
        assertTrue(result.contains("ELECTRONICS"));
        assertTrue(result.contains("10.99"));
        assertTrue(result.contains("BillItemResponse"));
    }

    @Test
    void shouldCreateBillCalculationResponseWithAllProperties() {
        BillItemResponse item = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 1, 
                                                    new BigDecimal("10.99"), new BigDecimal("10.99"), true);
        
        BillCalculationResponse response = new BillCalculationResponse();
        response.setCustomerId("cust-123");
        response.setItems(List.of(item));
        response.setSubtotal(new BigDecimal("10.99"));
        response.setTotalDiscount(new BigDecimal("1.00"));
        response.setNetAmount(new BigDecimal("9.99"));
        
        assertEquals("cust-123", response.getCustomerId());
        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("10.99"), response.getSubtotal());
        assertEquals(new BigDecimal("1.00"), response.getTotalDiscount());
        assertEquals(new BigDecimal("9.99"), response.getNetAmount());
    }

    @Test
    void shouldCreateBillCalculationResponseUsingConstructor() {
        BillItemResponse item = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 1, 
                                                    new BigDecimal("10.99"), new BigDecimal("10.99"), true);
        BillCalculationResponse response = new BillCalculationResponse("cust-123", List.of(item), 
                                                                     new BigDecimal("10.99"), new BigDecimal("0.50"), "EMPLOYEE",
                                                                     new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("9.99"));
        
        assertEquals("cust-123", response.getCustomerId());
        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("10.99"), response.getSubtotal());
        assertEquals(new BigDecimal("1.00"), response.getTotalDiscount());
        assertEquals(new BigDecimal("9.99"), response.getNetAmount());
    }

    @Test
    void shouldTestBillCalculationResponseEquality() {
        BillItemResponse item = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 1, 
                                                    new BigDecimal("10.99"), new BigDecimal("10.99"), true);
        BillCalculationResponse response1 = new BillCalculationResponse("cust-123", List.of(item), 
                                                                       new BigDecimal("10.99"), new BigDecimal("0.50"), "EMPLOYEE",
                                                                       new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("9.99"));
        BillCalculationResponse response2 = new BillCalculationResponse("cust-123", List.of(item), 
                                                                       new BigDecimal("10.99"), new BigDecimal("0.50"), "EMPLOYEE",
                                                                       new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("9.99"));
        BillCalculationResponse response3 = new BillCalculationResponse("cust-456", List.of(item), 
                                                                       new BigDecimal("10.99"), new BigDecimal("0.50"), "EMPLOYEE",
                                                                       new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("9.99"));
        
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);
        assertNotEquals(response1, "string");
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldTestBillCalculationResponseToString() {
        BillItemResponse item = new BillItemResponse("prod-123", "Test Product", "ELECTRONICS", 1, 
                                                    new BigDecimal("10.99"), new BigDecimal("10.99"), true);
        BillCalculationResponse response = new BillCalculationResponse("cust-123", List.of(item), 
                                                                     new BigDecimal("10.99"), new BigDecimal("0.50"), "EMPLOYEE",
                                                                     new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("9.99"));
        String result = response.toString();
        
        assertTrue(result.contains("cust-123"));
        assertTrue(result.contains("10.99"));
        assertTrue(result.contains("BillCalculationResponse"));
    }

    @Test
    void shouldCreateApiResponseWithAllProperties() {
        Map<String, Object> data = Map.of("key", "value");
        Map<String, Object> meta = Map.of("timestamp", "2023-01-01");
        
        ApiResponse<Map<String, Object>> response = new ApiResponse<Map<String, Object>>();
        response.setMessage("Success");
        response.setStatus("success");
        response.setData(data);
        response.setMeta(meta);
        
        assertEquals("Success", response.getMessage());
        assertEquals("success", response.getStatus());
        assertEquals(data, response.getData());
        assertEquals(meta, response.getMeta());
    }

    @Test
    void shouldCreateApiResponseUsingConstructors() {
        Map<String, Object> data = Map.of("key", "value");
        
        ApiResponse<Map<String, Object>> response1 = new ApiResponse<Map<String, Object>>("Success", "success", data, null);
        assertEquals("Success", response1.getMessage());
        assertEquals("success", response1.getStatus());
        assertEquals(data, response1.getData());
        assertNotNull(response1.getMeta());
        
        Map<String, Object> meta = Map.of("timestamp", "2023-01-01");
        ApiResponse<Map<String, Object>> response2 = new ApiResponse<Map<String, Object>>("Success", "success", data, meta);
        assertEquals("Success", response2.getMessage());
        assertEquals("success", response2.getStatus());
        assertEquals(data, response2.getData());
        assertEquals(meta, response2.getMeta());
    }

    @Test
    void shouldCreateSuccessApiResponse() {
        Map<String, Object> data = Map.of("key", "value");
        ApiResponse<Map<String, Object>> response = ApiResponse.success("Operation successful", data);
        
        assertEquals("Operation successful", response.getMessage());
        assertEquals("success", response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getMeta());
    }

    @Test
    void shouldCreateErrorApiResponse() {
        ApiResponse<Object> response = ApiResponse.error("Something went wrong");
        
        assertEquals("Something went wrong", response.getMessage());
        assertEquals("fail", response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMeta());
    }

    @Test
    void shouldCreateErrorApiResponseWithErrorCode() {
        ApiResponse<Object> response = ApiResponse.error("Something went wrong", "ERROR_CODE");
        
        assertEquals("Something went wrong", response.getMessage());
        assertEquals("fail", response.getStatus());
        assertNull(response.getData());
        assertEquals("ERROR_CODE", response.getMeta().get("errorCode"));
    }

    @Test
    void shouldTestApiResponseEquality() {
        Map<String, Object> data = Map.of("key", "value");
        ApiResponse<Map<String, Object>> response1 = new ApiResponse<Map<String, Object>>("Success", "success", data, null);
        ApiResponse<Map<String, Object>> response2 = new ApiResponse<Map<String, Object>>("Success", "success", data, null);
        ApiResponse<Map<String, Object>> response3 = new ApiResponse<Map<String, Object>>("Error", "fail", null, null);
        
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);
        assertNotEquals(response1, "string");
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldTestApiResponseToString() {
        Map<String, Object> data = Map.of("key", "value");
        ApiResponse<Map<String, Object>> response = new ApiResponse<Map<String, Object>>("Success", "success", data, null);
        String result = response.toString();
        
        assertTrue(result.contains("Success"));
        assertTrue(result.contains("success"));
        assertTrue(result.contains("key"));
        assertTrue(result.contains("ApiResponse"));
    }

    @Test
    void shouldTestBillItemResponseWithNullValues() {
        BillItemResponse response = new BillItemResponse();
        
        assertNull(response.getProductId());
        assertNull(response.getProductName());
        assertNull(response.getCategory());
        assertEquals(0, response.getQuantity());
        assertNull(response.getUnitPrice());
        assertNull(response.getTotalPrice());
        assertFalse(response.isEligibleForPercentageDiscount());
    }

    @Test
    void shouldTestBillCalculationResponseWithNullValues() {
        BillCalculationResponse response = new BillCalculationResponse();
        
        assertNull(response.getCustomerId());
        assertNull(response.getItems());
        assertNull(response.getSubtotal());
        assertNull(response.getTotalDiscount());
        assertNull(response.getNetAmount());
    }

    @Test
    void shouldTestApiResponseWithNullValues() {
        ApiResponse<Object> response = new ApiResponse<Object>();
        
        assertNull(response.getMessage());
        assertNull(response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMeta());
    }
}