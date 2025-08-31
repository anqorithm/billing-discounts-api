package sa.billing.discounts.domain.model;

import org.junit.jupiter.api.Test;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.valueobject.Percentage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComprehensiveDomainModelTest {

    @Test
    void shouldCreateMoneyWithBigDecimal() {
        BigDecimal amount = new BigDecimal("100.50");
        Money money = Money.of(amount);
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
        assertEquals("$100.50", money.toString());
    }

    @Test
    void shouldCreateMoneyWithDouble() {
        Money money = Money.of(100.5);
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    void shouldCreateMoneyWithString() {
        Money money = Money.of("100.50");
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    void shouldCreateZeroMoney() {
        Money money = Money.zero();
        
        assertEquals(BigDecimal.ZERO.setScale(2), money.getAmount());
        assertTrue(money.isZero());
    }

    @Test
    void shouldAddMoney() {
        Money money1 = Money.of("100.50");
        Money money2 = Money.of("50.25");
        
        Money result = money1.add(money2);
        
        assertEquals(Money.of("150.75"), result);
    }

    @Test
    void shouldSubtractMoney() {
        Money money1 = Money.of("100.50");
        Money money2 = Money.of("30.25");
        
        Money result = money1.subtract(money2);
        
        assertEquals(Money.of("70.25"), result);
    }

    @Test
    void shouldSubtractMoneyAndReturnZeroWhenResultNegative() {
        Money money1 = Money.of("50.00");
        Money money2 = Money.of("100.00");
        
        Money result = money1.subtract(money2);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldMultiplyMoneyWithBigDecimal() {
        Money money = Money.of("100.00");
        BigDecimal multiplier = new BigDecimal("1.5");
        
        Money result = money.multiply(multiplier);
        
        assertEquals(Money.of("150.00"), result);
    }

    @Test
    void shouldMultiplyMoneyWithDouble() {
        Money money = Money.of("100.00");
        
        Money result = money.multiply(1.5);
        
        assertEquals(Money.of("150.00"), result);
    }

    @Test
    void shouldCheckIfMoneyIsGreaterThan() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("50.00");
        
        assertTrue(money1.isGreaterThan(money2));
        assertFalse(money2.isGreaterThan(money1));
    }

    @Test
    void shouldCheckIfMoneyIsGreaterThanOrEqual() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("100.00");
        Money money3 = Money.of("50.00");
        
        assertTrue(money1.isGreaterThanOrEqual(money2));
        assertTrue(money1.isGreaterThanOrEqual(money3));
        assertFalse(money3.isGreaterThanOrEqual(money1));
    }

    @Test
    void shouldTestMoneyEquality() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("100.00");
        Money money3 = Money.of("50.00");
        
        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
        assertNotEquals(money1, null);
        assertNotEquals(money1, "string");
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void shouldThrowExceptionForNullMoneyAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            Money.of((BigDecimal) null);
        });
    }

    @Test
    void shouldThrowExceptionForNegativeMoneyAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            Money.of(new BigDecimal("-10.00"));
        });
    }

    @Test
    void shouldCreatePercentageWithBigDecimal() {
        Percentage percentage = Percentage.of(new BigDecimal("25.5"));
        
        assertEquals(new BigDecimal("25.5000"), percentage.getValue());
        assertEquals("25.5000%", percentage.toString());
    }

    @Test
    void shouldCreatePercentageWithDouble() {
        Percentage percentage = Percentage.of(25.5);
        
        assertEquals(new BigDecimal("25.5000"), percentage.getValue());
    }

    @Test
    void shouldCreatePercentageWithInt() {
        Percentage percentage = Percentage.of(25);
        
        assertEquals(new BigDecimal("25.0000"), percentage.getValue());
    }

    @Test
    void shouldCreateZeroPercentage() {
        Percentage percentage = Percentage.zero();
        
        assertTrue(percentage.isZero());
        assertEquals(BigDecimal.ZERO.setScale(4), percentage.getValue());
    }

    @Test
    void shouldApplyPercentageToMoney() {
        Percentage percentage = Percentage.of(10);
        Money money = Money.of("100.00");
        
        Money result = percentage.applyTo(money);
        
        assertEquals(Money.of("10.00"), result);
    }

    @Test
    void shouldTestPercentageEquality() {
        Percentage percentage1 = Percentage.of(25);
        Percentage percentage2 = Percentage.of(25);
        Percentage percentage3 = Percentage.of(30);
        
        assertEquals(percentage1, percentage2);
        assertNotEquals(percentage1, percentage3);
        assertNotEquals(percentage1, null);
        assertNotEquals(percentage1, "string");
        assertEquals(percentage1.hashCode(), percentage2.hashCode());
    }

    @Test
    void shouldThrowExceptionForNullPercentageValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Percentage.of((BigDecimal) null);
        });
    }

    @Test
    void shouldThrowExceptionForNegativePercentageValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Percentage.of(-10);
        });
    }

    @Test
    void shouldThrowExceptionForPercentageValueOver100() {
        assertThrows(IllegalArgumentException.class, () -> {
            Percentage.of(101);
        });
    }

    @Test
    void shouldCreateEmployeeCustomer() {
        LocalDateTime registrationDate = LocalDateTime.now().minusMonths(6);
        Customer customer = Customer.createEmployee("John Doe", "john@example.com", registrationDate);
        
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals(CustomerType.EMPLOYEE, customer.getType());
        assertEquals(registrationDate, customer.getRegistrationDate());
        assertTrue(customer.isEmployee());
        assertFalse(customer.isAffiliate());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    void shouldCreateAffiliateCustomer() {
        LocalDateTime registrationDate = LocalDateTime.now().minusMonths(6);
        Customer customer = Customer.createAffiliate("Jane Doe", "jane@example.com", registrationDate);
        
        assertEquals("Jane Doe", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals(CustomerType.AFFILIATE, customer.getType());
        assertTrue(customer.isAffiliate());
        assertFalse(customer.isEmployee());
    }

    @Test
    void shouldCreateRegularCustomer() {
        LocalDateTime registrationDate = LocalDateTime.now().minusYears(3);
        Customer customer = Customer.createRegular("Bob Smith", "bob@example.com", registrationDate);
        
        assertEquals("Bob Smith", customer.getName());
        assertEquals("bob@example.com", customer.getEmail());
        assertEquals(CustomerType.REGULAR, customer.getType());
        assertFalse(customer.isEmployee());
        assertFalse(customer.isAffiliate());
    }

    @Test
    void shouldCheckIfCustomerIsLoyal() {
        LocalDateTime registrationDate = LocalDateTime.now().minusYears(3);
        Customer customer = Customer.createRegular("Bob Smith", "bob@example.com", registrationDate);
        
        assertTrue(customer.isLoyalCustomer(LocalDateTime.now()));
    }

    @Test
    void shouldCheckIfCustomerIsNotLoyal() {
        LocalDateTime registrationDate = LocalDateTime.now().minusMonths(6);
        Customer customer = Customer.createRegular("Bob Smith", "bob@example.com", registrationDate);
        
        assertFalse(customer.isLoyalCustomer(LocalDateTime.now()));
    }

    @Test
    void shouldCheckIfEmployeeIsNotLoyalEvenIfOldEnough() {
        LocalDateTime registrationDate = LocalDateTime.now().minusYears(5);
        Customer customer = Customer.createEmployee("John Doe", "john@example.com", registrationDate);
        
        assertFalse(customer.isLoyalCustomer(LocalDateTime.now()));
    }

    @Test
    void shouldUpdateCustomerType() {
        LocalDateTime registrationDate = LocalDateTime.now().minusMonths(6);
        Customer customer = Customer.createRegular("Bob Smith", "bob@example.com", registrationDate);
        LocalDateTime originalUpdatedAt = customer.getUpdatedAt();
        
        customer.updateType(CustomerType.AFFILIATE);
        
        assertEquals(CustomerType.AFFILIATE, customer.getType());
        assertTrue(customer.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void shouldTestCustomerEquality() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Customer customer1 = Customer.createEmployee("John Doe", "john@example.com", registrationDate);
        Customer customer2 = Customer.createEmployee("Jane Doe", "jane@example.com", registrationDate);
        
        assertNotEquals(customer1, customer2);
        assertNotEquals(customer1, null);
        assertNotEquals(customer1, "string");
    }

    @Test
    void shouldTestCustomerToString() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Customer customer = Customer.createEmployee("John Doe", "john@example.com", registrationDate);
        String result = customer.toString();
        
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@example.com"));
        assertTrue(result.contains("EMPLOYEE"));
        assertTrue(result.contains("Customer{"));
    }

    @Test
    void shouldThrowExceptionForNullCustomerName() {
        assertThrows(NullPointerException.class, () -> {
            Customer.createEmployee(null, "john@example.com", LocalDateTime.now());
        });
    }

    @Test
    void shouldThrowExceptionForNullCustomerEmail() {
        assertThrows(NullPointerException.class, () -> {
            Customer.createEmployee("John Doe", null, LocalDateTime.now());
        });
    }

    @Test
    void shouldThrowExceptionForNullRegistrationDate() {
        assertThrows(NullPointerException.class, () -> {
            Customer.createEmployee("John Doe", "john@example.com", null);
        });
    }

    @Test
    void shouldThrowExceptionForNullCustomerTypeUpdate() {
        Customer customer = Customer.createRegular("Bob Smith", "bob@example.com", LocalDateTime.now());
        
        assertThrows(NullPointerException.class, () -> {
            customer.updateType(null);
        });
    }

    @Test
    void shouldCreateProduct() {
        Money price = Money.of("99.99");
        Product product = Product.create("Laptop", "Gaming laptop", price, ProductCategory.ELECTRONICS);
        
        assertEquals("Laptop", product.getName());
        assertEquals("Gaming laptop", product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(ProductCategory.ELECTRONICS, product.getCategory());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldCheckIfProductIsGrocery() {
        Money price = Money.of("5.99");
        Product groceryProduct = Product.create("Apple", "Fresh apple", price, ProductCategory.GROCERY);
        Product electronicsProduct = Product.create("Phone", "Smartphone", price, ProductCategory.ELECTRONICS);
        
        assertTrue(groceryProduct.isGrocery());
        assertFalse(electronicsProduct.isGrocery());
    }

    @Test
    void shouldCheckIfProductIsEligibleForPercentageDiscount() {
        Money price = Money.of("99.99");
        Product groceryProduct = Product.create("Apple", "Fresh apple", price, ProductCategory.GROCERY);
        Product electronicsProduct = Product.create("Phone", "Smartphone", price, ProductCategory.ELECTRONICS);
        
        assertFalse(groceryProduct.isEligibleForPercentageDiscount());
        assertTrue(electronicsProduct.isEligibleForPercentageDiscount());
    }

    @Test
    void shouldUpdateProductPrice() {
        Money originalPrice = Money.of("99.99");
        Money newPrice = Money.of("89.99");
        Product product = Product.create("Laptop", "Gaming laptop", originalPrice, ProductCategory.ELECTRONICS);
        LocalDateTime originalUpdatedAt = product.getUpdatedAt();
        
        product.updatePrice(newPrice);
        
        assertEquals(newPrice, product.getPrice());
        assertTrue(product.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void shouldUpdateProductCategory() {
        Money price = Money.of("99.99");
        Product product = Product.create("Item", "Some item", price, ProductCategory.ELECTRONICS);
        LocalDateTime originalUpdatedAt = product.getUpdatedAt();
        
        product.updateCategory(ProductCategory.BOOKS);
        
        assertEquals(ProductCategory.BOOKS, product.getCategory());
        assertTrue(product.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void shouldTestProductEquality() {
        Money price = Money.of("99.99");
        Product product1 = Product.create("Laptop", "Gaming laptop", price, ProductCategory.ELECTRONICS);
        Product product2 = Product.create("Phone", "Smartphone", price, ProductCategory.ELECTRONICS);
        
        assertNotEquals(product1, product2);
        assertNotEquals(product1, null);
        assertNotEquals(product1, "string");
    }

    @Test
    void shouldTestProductToString() {
        Money price = Money.of("99.99");
        Product product = Product.create("Laptop", "Gaming laptop", price, ProductCategory.ELECTRONICS);
        String result = product.toString();
        
        assertTrue(result.contains("Laptop"));
        assertTrue(result.contains("$99.99"));
        assertTrue(result.contains("ELECTRONICS"));
        assertTrue(result.contains("Product{"));
    }

    @Test
    void shouldThrowExceptionForNullProductName() {
        Money price = Money.of("99.99");
        
        assertThrows(NullPointerException.class, () -> {
            Product.create(null, "Description", price, ProductCategory.ELECTRONICS);
        });
    }

    @Test
    void shouldThrowExceptionForNullProductPrice() {
        assertThrows(NullPointerException.class, () -> {
            Product.create("Laptop", "Gaming laptop", null, ProductCategory.ELECTRONICS);
        });
    }

    @Test
    void shouldThrowExceptionForNullProductCategory() {
        Money price = Money.of("99.99");
        
        assertThrows(NullPointerException.class, () -> {
            Product.create("Laptop", "Gaming laptop", price, null);
        });
    }

    @Test
    void shouldThrowExceptionForNullPriceUpdate() {
        Money price = Money.of("99.99");
        Product product = Product.create("Laptop", "Gaming laptop", price, ProductCategory.ELECTRONICS);
        
        assertThrows(NullPointerException.class, () -> {
            product.updatePrice(null);
        });
    }

    @Test
    void shouldThrowExceptionForNullCategoryUpdate() {
        Money price = Money.of("99.99");
        Product product = Product.create("Laptop", "Gaming laptop", price, ProductCategory.ELECTRONICS);
        
        assertThrows(NullPointerException.class, () -> {
            product.updateCategory(null);
        });
    }

    @Test
    void shouldAllowNullProductDescription() {
        Money price = Money.of("99.99");
        Product product = Product.create("Laptop", null, price, ProductCategory.ELECTRONICS);
        
        assertEquals("Laptop", product.getName());
        assertNull(product.getDescription());
    }

    @Test
    void shouldTestCustomerTypeValues() {
        assertEquals(3, CustomerType.values().length);
        assertTrue(CustomerType.valueOf("EMPLOYEE") == CustomerType.EMPLOYEE);
        assertTrue(CustomerType.valueOf("AFFILIATE") == CustomerType.AFFILIATE);
        assertTrue(CustomerType.valueOf("REGULAR") == CustomerType.REGULAR);
    }

    @Test
    void shouldTestProductCategoryValues() {
        assertTrue(ProductCategory.values().length >= 4);
        assertTrue(ProductCategory.valueOf("ELECTRONICS") == ProductCategory.ELECTRONICS);
        assertTrue(ProductCategory.valueOf("BOOKS") == ProductCategory.BOOKS);
        assertTrue(ProductCategory.valueOf("GROCERY") == ProductCategory.GROCERY);
    }

    @Test
    void shouldHandleMoneyRounding() {
        Money money = Money.of("100.999");
        
        assertEquals(new BigDecimal("101.00"), money.getAmount());
    }

    @Test
    void shouldHandlePercentageRounding() {
        Percentage percentage = Percentage.of(new BigDecimal("10.99999"));
        
        assertEquals(new BigDecimal("11.0000"), percentage.getValue());
    }

    @Test
    void shouldHandlePercentageEdgeCases() {
        Percentage zero = Percentage.of(0);
        Percentage hundred = Percentage.of(100);
        
        assertTrue(zero.isZero());
        assertEquals(new BigDecimal("100.0000"), hundred.getValue());
    }
}