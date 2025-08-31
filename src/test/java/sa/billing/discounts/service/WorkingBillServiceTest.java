package sa.billing.discounts.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Working Bill Service Unit Tests")
class WorkingBillServiceTest {

    @Test
    @DisplayName("Should create Money object correctly")
    void shouldCreateMoneyObjectCorrectly() {
        Money money = Money.of("100.50");
        
        assertNotNull(money);
        assertEquals("100.50", money.getAmount().toString());
    }

    @Test
    @DisplayName("Should create Product object correctly")
    void shouldCreateProductObjectCorrectly() {
        Product product = Product.create("Test Product", "Description", 
            Money.of("99.99"), ProductCategory.ELECTRONICS);
        
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals(ProductCategory.ELECTRONICS, product.getCategory());
        assertTrue(product.isEligibleForPercentageDiscount());
    }

    @Test
    @DisplayName("Should create Employee Customer correctly")
    void shouldCreateEmployeeCustomerCorrectly() {
        Customer customer = Customer.createEmployee("John Doe", "john@company.com", 
            LocalDateTime.now().minusYears(1));
        
        assertNotNull(customer);
        assertEquals("John Doe", customer.getName());
        assertEquals("john@company.com", customer.getEmail());
        assertEquals(CustomerType.EMPLOYEE, customer.getType());
        assertTrue(customer.isEmployee());
        assertFalse(customer.isAffiliate());
    }

    @Test
    @DisplayName("Should create Affiliate Customer correctly")
    void shouldCreateAffiliateCustomerCorrectly() {
        Customer customer = Customer.createAffiliate("Jane Smith", "jane@partner.com", 
            LocalDateTime.now().minusMonths(6));
        
        assertNotNull(customer);
        assertEquals("Jane Smith", customer.getName());
        assertTrue(customer.isAffiliate());
        assertFalse(customer.isEmployee());
    }

    @Test
    @DisplayName("Should identify loyal customer correctly")
    void shouldIdentifyLoyalCustomerCorrectly() {
        Customer oldCustomer = Customer.createRegular("Old Customer", "old@customer.com", 
            LocalDateTime.now().minusYears(3));
        Customer newCustomer = Customer.createRegular("New Customer", "new@customer.com", 
            LocalDateTime.now().minusMonths(6));
        
        assertTrue(oldCustomer.isLoyalCustomer(LocalDateTime.now()));
        assertFalse(newCustomer.isLoyalCustomer(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should handle Money arithmetic correctly")
    void shouldHandleMoneyArithmeticCorrectly() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("25.50");
        
        Money sum = money1.add(money2);
        Money difference = money1.subtract(money2);
        Money product = money1.multiply(2);
        
        assertEquals("125.50", sum.getAmount().toString());
        assertEquals("74.50", difference.getAmount().toString());
        assertEquals("200.00", product.getAmount().toString());
    }

    @Test
    @DisplayName("Should handle grocery vs non-grocery products")
    void shouldHandleGroceryVsNonGroceryProducts() {
        Product electronics = Product.create("Laptop", "Gaming laptop", 
            Money.of("1000.00"), ProductCategory.ELECTRONICS);
        Product grocery = Product.create("Apples", "Fresh apples", 
            Money.of("5.00"), ProductCategory.GROCERY);
        
        assertTrue(electronics.isEligibleForPercentageDiscount());
        assertFalse(grocery.isEligibleForPercentageDiscount());
    }
}