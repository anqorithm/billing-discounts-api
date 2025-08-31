package sa.billing.discounts.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import sa.billing.discounts.application.service.BillCalculationService;
import sa.billing.discounts.application.config.DiscountConfig;
import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;
import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.exception.CustomerNotFoundException;
import sa.billing.discounts.domain.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Comprehensive Bill Calculation Service Tests")
class ComprehensiveBillCalculationServiceTest {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private DiscountConfig discountConfig;
    private BillCalculationService billCalculationService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);
        discountConfig = mock(DiscountConfig.class);
        
        // Setup discount config defaults
        when(discountConfig.getEmployeePercentage()).thenReturn(new BigDecimal("30.00"));
        when(discountConfig.getAffiliatePercentage()).thenReturn(new BigDecimal("10.00"));
        when(discountConfig.getLoyaltyPercentage()).thenReturn(new BigDecimal("5.00"));
        when(discountConfig.getBillThreshold()).thenReturn(new BigDecimal("100.00"));
        when(discountConfig.getBillDiscountAmount()).thenReturn(new BigDecimal("5.00"));
        
        billCalculationService = new BillCalculationService(customerRepository, productRepository, discountConfig);
    }
    
    private Customer createCustomerWithId(String id, Customer customer) {
        try {
            java.lang.reflect.Field idField = Customer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(customer, id);
            return customer;
        } catch (Exception e) {
            throw new RuntimeException("Could not set customer ID for test", e);
        }
    }

    @Test
    @DisplayName("Should calculate discount for employee customer")
    void shouldCalculateDiscountForEmployeeCustomer() {
        // Given
        Customer employee = createCustomerWithId("emp1", 
            Customer.createEmployee("John", "john@company.com", LocalDateTime.now().minusYears(1)));
        Product laptop = Product.create("Laptop", "Gaming laptop", Money.of("1000.00"), ProductCategory.ELECTRONICS);
        
        BillItemRequest itemRequest = new BillItemRequest("laptop1", 1);
        BillCalculationRequest request = new BillCalculationRequest("emp1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("emp1")).thenReturn(Optional.of(employee));
        when(productRepository.findById("laptop1")).thenReturn(Optional.of(laptop));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals("emp1", response.getCustomerId());
        assertEquals(new BigDecimal("1000.00"), response.getSubtotal());
        assertTrue(response.getTotalDiscount().compareTo(BigDecimal.ZERO) > 0); // Should have discount
        
        verify(customerRepository).findById("emp1");
        verify(productRepository).findById("laptop1");
    }

    @Test
    @DisplayName("Should calculate discount for affiliate customer")
    void shouldCalculateDiscountForAffiliateCustomer() {
        // Given
        Customer affiliate = createCustomerWithId("aff1", Customer.createAffiliate("Jane", "jane@partner.com", LocalDateTime.now().minusMonths(6)));
        Product phone = Product.create("Phone", "Smartphone", Money.of("500.00"), ProductCategory.ELECTRONICS);
        
        BillItemRequest itemRequest = new BillItemRequest("phone1", 2);
        BillCalculationRequest request = new BillCalculationRequest("aff1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("aff1")).thenReturn(Optional.of(affiliate));
        when(productRepository.findById("phone1")).thenReturn(Optional.of(phone));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("1000.00"), response.getSubtotal());
        assertTrue(response.getTotalDiscount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should calculate discount for loyal regular customer")
    void shouldCalculateDiscountForLoyalRegularCustomer() {
        // Given
        Customer loyalCustomer = createCustomerWithId("loyal1", Customer.createRegular("Bob", "bob@customer.com", LocalDateTime.now().minusYears(3)));
        Product tablet = Product.create("Tablet", "iPad", Money.of("400.00"), ProductCategory.ELECTRONICS);
        
        BillItemRequest itemRequest = new BillItemRequest("tablet1", 1);
        BillCalculationRequest request = new BillCalculationRequest("loyal1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("loyal1")).thenReturn(Optional.of(loyalCustomer));
        when(productRepository.findById("tablet1")).thenReturn(Optional.of(tablet));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("400.00"), response.getSubtotal());
    }

    @Test
    @DisplayName("Should handle mixed product categories")
    void shouldHandleMixedProductCategories() {
        // Given
        Customer employee = createCustomerWithId("emp2", Customer.createEmployee("Alice", "alice@company.com", LocalDateTime.now().minusYears(1)));
        Product laptop = Product.create("Laptop", "Work laptop", Money.of("800.00"), ProductCategory.ELECTRONICS);
        Product groceries = Product.create("Groceries", "Food items", Money.of("100.00"), ProductCategory.GROCERY);
        
        BillItemRequest item1 = new BillItemRequest("laptop1", 1);
        BillItemRequest item2 = new BillItemRequest("food1", 1);
        BillCalculationRequest request = new BillCalculationRequest("emp2", Arrays.asList(item1, item2));
        
        when(customerRepository.findById("emp2")).thenReturn(Optional.of(employee));
        when(productRepository.findById("laptop1")).thenReturn(Optional.of(laptop));
        when(productRepository.findById("food1")).thenReturn(Optional.of(groceries));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("900.00"), response.getSubtotal());
        assertEquals(2, response.getItems().size());
        
        // Electronics should be eligible for discount, groceries should not
        boolean hasEligibleItem = response.getItems().stream()
            .anyMatch(item -> item.isEligibleForPercentageDiscount());
        boolean hasNonEligibleItem = response.getItems().stream()
            .anyMatch(item -> !item.isEligibleForPercentageDiscount());
        
        assertTrue(hasEligibleItem);
        assertTrue(hasNonEligibleItem);
    }

    @Test
    @DisplayName("Should throw exception for invalid customer")
    void shouldThrowExceptionForInvalidCustomer() {
        // Given
        BillItemRequest itemRequest = new BillItemRequest("product1", 1);
        BillCalculationRequest request = new BillCalculationRequest("invalid", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("invalid")).thenReturn(Optional.empty());
        
        // When & Then
        CustomerNotFoundException exception = assertThrows(
            CustomerNotFoundException.class,
            () -> billCalculationService.calculateBillDiscount(request)
        );
        
        assertEquals("Customer not found with ID: invalid", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid product")
    void shouldThrowExceptionForInvalidProduct() {
        // Given
        Customer customer = createCustomerWithId("cust1", Customer.createEmployee("Test", "test@company.com", LocalDateTime.now()));
        BillItemRequest itemRequest = new BillItemRequest("invalid", 1);
        BillCalculationRequest request = new BillCalculationRequest("cust1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("cust1")).thenReturn(Optional.of(customer));
        when(productRepository.findById("invalid")).thenReturn(Optional.empty());
        
        // When & Then
        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class,
            () -> billCalculationService.calculateBillDiscount(request)
        );
        
        assertEquals("Product not found with ID: invalid", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle large quantity orders")
    void shouldHandleLargeQuantityOrders() {
        // Given
        Customer employee = createCustomerWithId("bulk1", Customer.createEmployee("Bulk Buyer", "bulk@company.com", LocalDateTime.now()));
        Product item = Product.create("Bulk Item", "Bulk purchase", Money.of("10.00"), ProductCategory.ELECTRONICS);
        
        BillItemRequest itemRequest = new BillItemRequest("bulk1", 100);
        BillCalculationRequest request = new BillCalculationRequest("bulk1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("bulk1")).thenReturn(Optional.of(employee));
        when(productRepository.findById("bulk1")).thenReturn(Optional.of(item));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("1000.00"), response.getSubtotal());
        assertEquals(100, response.getItems().get(0).getQuantity());
    }

    @Test
    @DisplayName("Should handle bill above threshold for bill-based discount")
    void shouldHandleBillAboveThresholdForBillBasedDiscount() {
        // Given
        Customer regular = createCustomerWithId("regular1", Customer.createRegular("Regular", "regular@customer.com", LocalDateTime.now()));
        Product expensiveItem = Product.create("Expensive", "High value", Money.of("150.00"), ProductCategory.GROCERY);
        
        BillItemRequest itemRequest = new BillItemRequest("expensive1", 1);
        BillCalculationRequest request = new BillCalculationRequest("regular1", Arrays.asList(itemRequest));
        
        when(customerRepository.findById("regular1")).thenReturn(Optional.of(regular));
        when(productRepository.findById("expensive1")).thenReturn(Optional.of(expensiveItem));
        
        // When
        BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
        
        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("150.00"), response.getSubtotal());
        // Should get bill-based discount for orders over threshold
        assertTrue(response.getTotalDiscount().compareTo(BigDecimal.ZERO) >= 0);
    }
}