package sa.billing.discounts.domain.model.customer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCustomerTest {

    @Test
    void shouldCreateRegularCustomer() {
        Customer customer = Customer.createRegular("John Doe", "john@example.com", LocalDateTime.now());
        
        assertNotNull(customer);
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals(CustomerType.REGULAR, customer.getType());
        assertNotNull(customer.getRegistrationDate());
    }

    @Test
    void shouldCreateEmployeeCustomer() {
        Customer customer = Customer.createEmployee("Jane Employee", "jane@company.com", LocalDateTime.now());
        
        assertNotNull(customer);
        assertEquals("Jane Employee", customer.getName());
        assertEquals("jane@company.com", customer.getEmail());
        assertEquals(CustomerType.EMPLOYEE, customer.getType());
    }

    @Test
    void shouldCreateAffiliateCustomer() {
        Customer customer = Customer.createAffiliate("Bob Affiliate", "bob@affiliate.com", LocalDateTime.now());
        
        assertNotNull(customer);
        assertEquals("Bob Affiliate", customer.getName());
        assertEquals("bob@affiliate.com", customer.getEmail());
        assertEquals(CustomerType.AFFILIATE, customer.getType());
    }

    @Test
    void shouldGetBasicProperties() {
        LocalDateTime registrationDate = LocalDateTime.of(2023, 1, 15, 10, 0);
        Customer customer = Customer.createRegular("Test User", "test@example.com", registrationDate);
        
        // ID generation handled internally
        assertEquals("Test User", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals(CustomerType.REGULAR, customer.getType());
        assertEquals(registrationDate, customer.getRegistrationDate());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    void shouldTestEquals() {
        LocalDateTime date = LocalDateTime.now();
        Customer customer1 = Customer.createRegular("Same Name", "same@email.com", date);
        Customer customer2 = Customer.createRegular("Same Name", "same@email.com", date);
        
        assertEquals(customer1, customer1);
        assertNotEquals(customer1, null);
        assertNotEquals(customer1, "not a customer");
        
        // Different instances with same data should be equal (business equality)
        assertEquals(customer1, customer2);
    }

    @Test
    void shouldTestHashCode() {
        LocalDateTime date = LocalDateTime.now();
        Customer customer1 = Customer.createRegular("Hash Test", "hash@test.com", date);
        Customer customer2 = Customer.createRegular("Hash Test", "hash@test.com", date);
        
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void shouldTestToString() {
        Customer customer = Customer.createRegular("ToString Test", "tostring@test.com", LocalDateTime.now());
        
        String customerString = customer.toString();
        assertNotNull(customerString);
        assertTrue(customerString.length() > 0);
        assertTrue(customerString.contains("Customer"));
    }

    @Test
    void shouldHandleDifferentCustomerTypes() {
        LocalDateTime date = LocalDateTime.now();
        
        Customer regular = Customer.createRegular("Regular", "regular@test.com", date);
        Customer employee = Customer.createEmployee("Employee", "employee@test.com", date);
        Customer affiliate = Customer.createAffiliate("Affiliate", "affiliate@test.com", date);
        
        assertEquals(CustomerType.REGULAR, regular.getType());
        assertEquals(CustomerType.EMPLOYEE, employee.getType());
        assertEquals(CustomerType.AFFILIATE, affiliate.getType());
        
        // All should be different due to different types
        assertNotEquals(regular, employee);
        assertNotEquals(employee, affiliate);
        assertNotEquals(regular, affiliate);
    }

    @Test
    void shouldHandlePastDates() {
        LocalDateTime pastDate = LocalDateTime.of(2020, 6, 15, 14, 30);
        Customer customer = Customer.createRegular("Past User", "past@test.com", pastDate);
        
        assertEquals(pastDate, customer.getRegistrationDate());
        assertTrue(customer.getCreatedAt().isAfter(pastDate));
    }

    @Test
    void shouldHandleFutureDates() {
        LocalDateTime futureDate = LocalDateTime.of(2025, 12, 31, 23, 59);
        Customer customer = Customer.createRegular("Future User", "future@test.com", futureDate);
        
        assertEquals(futureDate, customer.getRegistrationDate());
    }

    @Test
    void shouldCreateCustomersWithLongNames() {
        String longName = "Very Long Customer Name That Exceeds Normal Length";
        Customer customer = Customer.createRegular(longName, "long@test.com", LocalDateTime.now());
        
        assertEquals(longName, customer.getName());
    }

    @Test
    void shouldCreateCustomersWithLongEmails() {
        String longEmail = "verylongemailaddress@verylongdomainname.com";
        Customer customer = Customer.createRegular("Long Email", longEmail, LocalDateTime.now());
        
        assertEquals(longEmail, customer.getEmail());
    }
}