package sa.billing.discounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import sa.billing.discounts.presentation.controller.v1.BillController;
import sa.billing.discounts.presentation.controller.v1.RootController;
import sa.billing.discounts.application.service.BillCalculationService;
import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.application.dto.BillItemResponse;
import sa.billing.discounts.domain.exception.CustomerNotFoundException;
import sa.billing.discounts.domain.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Comprehensive Controller Tests")
class ComprehensiveControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BillCalculationService billCalculationService;
    private BillController billController;
    private RootController rootController;

    @BeforeEach
    void setUp() {
        billCalculationService = mock(BillCalculationService.class);
        billController = new BillController(billCalculationService);
        rootController = new RootController();
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
            .standaloneSetup(billController, rootController)
            .build();
    }

    @Test
    @DisplayName("Should handle GET root endpoint")
    void shouldHandleGetRootEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Welcome to Billing Discounts API"))
                .andExpect(jsonPath("$.data.name").value("Billing Discounts API"))
                .andExpect(jsonPath("$.data.status").value("running"));
    }

    @Test
    @DisplayName("Should handle health check endpoint")
    void shouldHandleHealthCheckEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/bills/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Bill service is running"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @Test
    @DisplayName("Should calculate bill for employee customer")
    void shouldCalculateBillForEmployeeCustomer() throws Exception {
        // Given - Based on api-requests.http
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 1),
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f2", 2)
            )
        );

        BillCalculationResponse mockResponse = new BillCalculationResponse(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f1", "Product 1", "ELECTRONICS", 1, 
                    new BigDecimal("100.00"), new BigDecimal("100.00"), true),
                new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f2", "Product 2", "ELECTRONICS", 2, 
                    new BigDecimal("50.00"), new BigDecimal("100.00"), true)
            ),
            new BigDecimal("200.00"),
            new BigDecimal("60.00"),
            "EMPLOYEE",
            new BigDecimal("5.00"),
            new BigDecimal("65.00"),
            new BigDecimal("135.00")
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.customerId").value("65a1b2c3d4e5f6a7b8c9d0e1"))
                .andExpect(jsonPath("$.data.subtotal").value(200.00))
                .andExpect(jsonPath("$.data.totalDiscount").value(65.00))
                .andExpect(jsonPath("$.data.netAmount").value(135.00));

        verify(billCalculationService).calculateBillDiscount(any(BillCalculationRequest.class));
    }

    @Test
    @DisplayName("Should handle custom unit price in request")
    void shouldHandleCustomUnitPriceInRequest() throws Exception {
        // Given - Based on api-requests.http with custom unitPrice
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 1, new BigDecimal("899.99"))
            )
        );

        BillCalculationResponse mockResponse = new BillCalculationResponse(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f1", "Custom Product", "ELECTRONICS", 1, 
                    new BigDecimal("899.99"), new BigDecimal("899.99"), true)
            ),
            new BigDecimal("899.99"),
            new BigDecimal("269.997"),
            "EMPLOYEE",
            new BigDecimal("40.00"),
            new BigDecimal("309.997"),
            new BigDecimal("589.993")
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].unitPrice").value(899.99))
                .andExpect(jsonPath("$.data.subtotal").value(899.99));
    }

    @Test
    @DisplayName("Should handle large quantity orders")
    void shouldHandleLargeQuantityOrders() throws Exception {
        // Given - Based on api-requests.http with quantity 20
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f2", 20)
            )
        );

        BillCalculationResponse mockResponse = new BillCalculationResponse(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f2", "Bulk Product", "GROCERY", 20, 
                    new BigDecimal("10.00"), new BigDecimal("200.00"), false)
            ),
            new BigDecimal("200.00"),
            new BigDecimal("0.00"),
            null,
            new BigDecimal("50.00"),
            new BigDecimal("50.00"),
            new BigDecimal("150.00")
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].quantity").value(20));
    }

    @Test
    @DisplayName("Should handle complex multi-item order")
    void shouldHandleComplexMultiItemOrder() throws Exception {
        // Given - Based on api-requests.http complex order
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e2",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 1),
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f2", 5),
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f3", 1),
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f4", 3)
            )
        );

        List<BillItemResponse> items = Arrays.asList(
            new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f1", "Electronics 1", "ELECTRONICS", 1, 
                new BigDecimal("200.00"), new BigDecimal("200.00"), true),
            new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f2", "Grocery Item", "GROCERY", 5, 
                new BigDecimal("15.00"), new BigDecimal("75.00"), false),
            new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f3", "Electronics 2", "ELECTRONICS", 1, 
                new BigDecimal("150.00"), new BigDecimal("150.00"), true),
            new BillItemResponse("65a1b2c3d4e5f6a7b8c9d0f4", "Clothing", "CLOTHING", 3, 
                new BigDecimal("50.00"), new BigDecimal("150.00"), true)
        );

        BillCalculationResponse mockResponse = new BillCalculationResponse(
            "65a1b2c3d4e5f6a7b8c9d0e2",
            items,
            new BigDecimal("575.00"),
            new BigDecimal("50.00"),
            "AFFILIATE",
            new BigDecimal("25.00"),
            new BigDecimal("75.00"),
            new BigDecimal("500.00")
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.length()").value(4))
                .andExpect(jsonPath("$.data.percentageDiscountType").value("AFFILIATE"));
    }

    @Test
    @DisplayName("Should handle invalid customer ID")
    void shouldHandleInvalidCustomerId() throws Exception {
        // Given - Based on api-requests.http error case
        BillCalculationRequest request = new BillCalculationRequest(
            "invalid-customer-id",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 1)
            )
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenThrow(new CustomerNotFoundException("Customer not found with ID: invalid-customer-id"));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to calculate bill: Customer not found with ID: invalid-customer-id"));
    }

    @Test
    @DisplayName("Should handle invalid product ID")
    void shouldHandleInvalidProductId() throws Exception {
        // Given - Based on api-requests.http error case
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("invalid-product-id", 1)
            )
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenThrow(new ProductNotFoundException("Product not found with ID: invalid-product-id"));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to calculate bill: Product not found with ID: invalid-product-id"));
    }

    @Test
    @DisplayName("Should handle missing customer ID")
    void shouldHandleMissingCustomerId() throws Exception {
        // Given - Based on api-requests.http missing customerId
        String requestBody = "{"
            + "\"items\": ["
            + "  {"
            + "    \"productId\": \"65a1b2c3d4e5f6a7b8c9d0f1\","
            + "    \"quantity\": 1"
            + "  }"
            + "]"
            + "}";

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle empty items list")
    void shouldHandleEmptyItemsList() throws Exception {
        // Given - Based on api-requests.http empty items
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList()
        );

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle negative quantity")
    void shouldHandleNegativeQuantity() throws Exception {
        // Given - Based on api-requests.http negative quantity
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", -1)
            )
        );

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle zero quantity")
    void shouldHandleZeroQuantity() throws Exception {
        // Given - Based on api-requests.http zero quantity
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(
                new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 0)
            )
        );

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle generic runtime exception")
    void shouldHandleGenericRuntimeException() throws Exception {
        BillCalculationRequest request = new BillCalculationRequest(
            "65a1b2c3d4e5f6a7b8c9d0e1",
            Arrays.asList(new BillItemRequest("65a1b2c3d4e5f6a7b8c9d0f1", 1))
        );

        when(billCalculationService.calculateBillDiscount(any(BillCalculationRequest.class)))
            .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to calculate bill: Database connection failed"));
    }
}