package sa.billing.discounts.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.domain.model.*;
import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;
import sa.billing.discounts.domain.service.DiscountCalculationResult;
import sa.billing.discounts.domain.service.DiscountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bill Calculation Service Tests")
class BillCalculationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private BillCalculationService billCalculationService;

    private Customer testCustomer;
    private Product testProduct;
    private BillCalculationRequest testRequest;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.createEmployee("Test Employee", "test@company.com", LocalDateTime.now().minusYears(1));
        testProduct = Product.create("Test Product", "Test Description", Money.of("100.00"), ProductCategory.ELECTRONICS);
        
        BillItemRequest itemRequest = new BillItemRequest("product1", 2);
        testRequest = new BillCalculationRequest("customer1", Arrays.asList(itemRequest));
    }

    @Test
    @DisplayName("Should calculate bill discount successfully")
    void shouldCalculateBillDiscountSuccessfully() {
        DiscountCalculationResult mockResult = DiscountCalculationResult.builder()
                .subtotal(Money.of("200.00"))
                .percentageBasedDiscount(Money.of("60.00"))
                .percentageDiscountType("EMPLOYEE")
                .billBasedDiscount(Money.of("10.00"))
                .totalDiscount(Money.of("70.00"))
                .netAmount(Money.of("130.00"))
                .build();

        when(customerRepository.findById("customer1")).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById("product1")).thenReturn(Optional.of(testProduct));
        when(discountService.calculateDiscountBreakdown(any(Bill.class), any(Customer.class)))
                .thenReturn(mockResult);

        BillCalculationResponse response = billCalculationService.calculateBillDiscount(testRequest);

        assertNotNull(response);
        assertEquals("customer1", response.getCustomerId());
        assertEquals(new BigDecimal("200.00"), response.getSubtotal());
        assertEquals(new BigDecimal("60.00"), response.getPercentageBasedDiscount());
        assertEquals("EMPLOYEE", response.getPercentageDiscountType());
        assertEquals(new BigDecimal("10.00"), response.getBillBasedDiscount());
        assertEquals(new BigDecimal("70.00"), response.getTotalDiscount());
        assertEquals(new BigDecimal("130.00"), response.getNetAmount());
        assertEquals(1, response.getItems().size());

        verify(customerRepository).findById("customer1");
        verify(productRepository).findById("product1");
        verify(discountService).calculateDiscountBreakdown(any(Bill.class), eq(testCustomer));
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found")
    void shouldThrowCustomerNotFoundExceptionWhenCustomerNotFound() {
        when(customerRepository.findById("customer1")).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
            CustomerNotFoundException.class,
            () -> billCalculationService.calculateBillDiscount(testRequest)
        );

        assertEquals("Customer not found with ID: customer1", exception.getMessage());
        verify(customerRepository).findById("customer1");
        verifyNoInteractions(productRepository, discountService);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found")
    void shouldThrowProductNotFoundExceptionWhenProductNotFound() {
        when(customerRepository.findById("customer1")).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById("product1")).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class,
            () -> billCalculationService.calculateBillDiscount(testRequest)
        );

        assertEquals("Product not found with ID: product1", exception.getMessage());
        verify(customerRepository).findById("customer1");
        verify(productRepository).findById("product1");
        verifyNoInteractions(discountService);
    }

    @Test
    @DisplayName("Should handle custom unit price in bill item")
    void shouldHandleCustomUnitPriceInBillItem() {
        BillItemRequest itemRequest = new BillItemRequest("product1", 2, new BigDecimal("150.00"));
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList(itemRequest));

        DiscountCalculationResult mockResult = DiscountCalculationResult.builder()
                .subtotal(Money.of("300.00"))
                .percentageBasedDiscount(Money.of("90.00"))
                .percentageDiscountType("EMPLOYEE")
                .billBasedDiscount(Money.of("15.00"))
                .totalDiscount(Money.of("105.00"))
                .netAmount(Money.of("195.00"))
                .build();

        when(customerRepository.findById("customer1")).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById("product1")).thenReturn(Optional.of(testProduct));
        when(discountService.calculateDiscountBreakdown(any(Bill.class), any(Customer.class)))
                .thenReturn(mockResult);

        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("300.00"), response.getSubtotal());
        assertEquals(new BigDecimal("150.00"), response.getItems().get(0).getUnitPrice());
        assertEquals(new BigDecimal("300.00"), response.getItems().get(0).getTotalPrice());
    }

    @Test
    @DisplayName("Should map bill item response correctly")
    void shouldMapBillItemResponseCorrectly() {
        DiscountCalculationResult mockResult = DiscountCalculationResult.builder()
                .subtotal(Money.of("200.00"))
                .percentageBasedDiscount(Money.zero())
                .percentageDiscountType(null)
                .billBasedDiscount(Money.of("10.00"))
                .totalDiscount(Money.of("10.00"))
                .netAmount(Money.of("190.00"))
                .build();

        when(customerRepository.findById("customer1")).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById("product1")).thenReturn(Optional.of(testProduct));
        when(discountService.calculateDiscountBreakdown(any(Bill.class), any(Customer.class)))
                .thenReturn(mockResult);

        BillCalculationResponse response = billCalculationService.calculateBillDiscount(testRequest);

        assertEquals(1, response.getItems().size());
        var item = response.getItems().get(0);
        assertEquals(testProduct.getId(), item.getProductId());
        assertEquals(testProduct.getName(), item.getProductName());
        assertEquals(testProduct.getCategory().name(), item.getCategory());
        assertEquals(2, item.getQuantity());
        assertEquals(testProduct.getPrice().getAmount(), item.getUnitPrice());
        assertEquals(new BigDecimal("200.00"), item.getTotalPrice());
        assertTrue(item.isEligibleForPercentageDiscount());
    }

    @Test
    @DisplayName("Should handle multiple items in request")
    void shouldHandleMultipleItemsInRequest() {
        Product product2 = Product.create("Product 2", "Description 2", Money.of("50.00"), ProductCategory.GROCERY);
        
        BillItemRequest item1 = new BillItemRequest("product1", 1);
        BillItemRequest item2 = new BillItemRequest("product2", 3);
        BillCalculationRequest request = new BillCalculationRequest("customer1", Arrays.asList(item1, item2));

        DiscountCalculationResult mockResult = DiscountCalculationResult.builder()
                .subtotal(Money.of("250.00"))
                .percentageBasedDiscount(Money.of("30.00"))
                .percentageDiscountType("EMPLOYEE")
                .billBasedDiscount(Money.of("10.00"))
                .totalDiscount(Money.of("40.00"))
                .netAmount(Money.of("210.00"))
                .build();

        when(customerRepository.findById("customer1")).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById("product1")).thenReturn(Optional.of(testProduct));
        when(productRepository.findById("product2")).thenReturn(Optional.of(product2));
        when(discountService.calculateDiscountBreakdown(any(Bill.class), any(Customer.class)))
                .thenReturn(mockResult);

        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);

        assertEquals(2, response.getItems().size());
        assertEquals(new BigDecimal("250.00"), response.getSubtotal());

        verify(productRepository).findById("product1");
        verify(productRepository).findById("product2");
    }
}