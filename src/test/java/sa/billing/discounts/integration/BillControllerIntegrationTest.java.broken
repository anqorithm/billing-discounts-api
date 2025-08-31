package sa.billing.discounts.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.application.service.BillCalculationService;
import sa.billing.discounts.application.service.CustomerNotFoundException;
import sa.billing.discounts.application.service.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("Bill Controller Integration Tests")
class BillControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BillCalculationService billCalculationService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should return health check successfully")
    void shouldReturnHealthCheckSuccessfully() throws Exception {
        mockMvc.perform(get("/api/v1/bills/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bill service is running"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @Test
    @DisplayName("Should calculate bill successfully")
    void shouldCalculateBillSuccessfully() throws Exception {
        BillItemRequest itemRequest = new BillItemRequest("product1", 2);
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList(itemRequest));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Bill calculated successfully"));
    }

    @Test
    @DisplayName("Should return 404 when customer not found")
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        BillItemRequest itemRequest = new BillItemRequest("product1", 2);
        BillCalculationRequest request = new BillCalculationRequest("nonexistent", Arrays.asList(itemRequest));

        when(billCalculationService.calculateBillDiscount(any()))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: nonexistent"));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("CUSTOMER_NOT_FOUND"));
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        BillItemRequest itemRequest = new BillItemRequest("nonexistent", 2);
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList(itemRequest));

        when(billCalculationService.calculateBillDiscount(any()))
                .thenThrow(new ProductNotFoundException("Product not found with ID: nonexistent"));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("Should return 400 for invalid request data")
    void shouldReturn400ForInvalidRequestData() throws Exception {
        BillCalculationRequest request = new BillCalculationRequest(null, Arrays.asList());

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should return 400 for negative quantity")
    void shouldReturn400ForNegativeQuantity() throws Exception {
        BillItemRequest itemRequest = new BillItemRequest("product1", -1);
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList(itemRequest));

        mockMvc.perform(post("/api/v1/bills/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false));
    }
}