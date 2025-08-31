package sa.billing.discounts.domain.model.discount;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComprehensiveDiscountTest {

    @Mock
    private Bill bill;

    @Mock
    private Customer customer;

    @Test
    void shouldCreateEmployeeDiscountWithValidPercentage() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        assertEquals(DiscountType.EMPLOYEE, discount.getType());
        assertTrue(discount.getDescription().contains("30% discount for employees"));
    }

    @Test
    void shouldCalculateEmployeeDiscountForEligibleCustomer() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        Money eligibleAmount = Money.of(new BigDecimal("100"));
        Money expectedDiscount = Money.of(new BigDecimal("30.00"));
        
        when(customer.isEmployee()).thenReturn(true);
        when(bill.isEmpty()).thenReturn(false);
        when(bill.calculateEligibleAmountForPercentageDiscount()).thenReturn(eligibleAmount);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(expectedDiscount, result);
    }

    @Test
    void shouldReturnZeroDiscountForNonEmployeeCustomer() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        when(customer.isEmployee()).thenReturn(false);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldReturnZeroDiscountForEmployeeWithEmptyBill() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        when(customer.isEmployee()).thenReturn(true);
        when(bill.isEmpty()).thenReturn(true);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldCheckEmployeeDiscountApplicability() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        when(customer.isEmployee()).thenReturn(true);
        when(bill.isEmpty()).thenReturn(false);
        
        assertTrue(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckEmployeeDiscountNotApplicableForNonEmployee() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        when(customer.isEmployee()).thenReturn(false);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckEmployeeDiscountNotApplicableForEmptyBill() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        when(customer.isEmployee()).thenReturn(true);
        when(bill.isEmpty()).thenReturn(true);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCreateAffiliateDiscountWithValidPercentage() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        
        assertEquals(DiscountType.AFFILIATE, discount.getType());
        assertTrue(discount.getDescription().contains("10% discount for affiliates"));
    }

    @Test
    void shouldCalculateAffiliateDiscountForEligibleCustomer() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        Money nonGroceryAmount = Money.of(new BigDecimal("100"));
        Money expectedDiscount = Money.of(new BigDecimal("10.00"));
        
        when(customer.getType()).thenReturn(CustomerType.AFFILIATE);
        when(bill.getNonGroceryAmount()).thenReturn(nonGroceryAmount);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(expectedDiscount, result);
    }

    @Test
    void shouldReturnZeroDiscountForNonAffiliateCustomer() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        
        when(customer.getType()).thenReturn(CustomerType.REGULAR);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldCheckAffiliateDiscountApplicability() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        
        when(customer.getType()).thenReturn(CustomerType.AFFILIATE);
        when(bill.hasNonGroceryItems()).thenReturn(true);
        
        assertTrue(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckAffiliateDiscountNotApplicableForRegularCustomer() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        
        when(customer.getType()).thenReturn(CustomerType.REGULAR);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckAffiliateDiscountNotApplicableWithoutNonGroceryItems() {
        AffiliateDiscount discount = new AffiliateDiscount(new BigDecimal("10"));
        
        when(customer.getType()).thenReturn(CustomerType.AFFILIATE);
        when(bill.hasNonGroceryItems()).thenReturn(false);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCreateLoyaltyDiscountWithValidPercentage() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        assertEquals(DiscountType.LOYALTY, discount.getType());
        assertTrue(discount.getDescription().contains("5% discount for loyal customers"));
    }

    @Test
    void shouldCalculateLoyaltyDiscountForEligibleCustomer() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        Money eligibleAmount = Money.of(new BigDecimal("100"));
        Money expectedDiscount = Money.of(new BigDecimal("5.00"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(true);
        when(bill.isEmpty()).thenReturn(false);
        when(bill.calculateEligibleAmountForPercentageDiscount()).thenReturn(eligibleAmount);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(expectedDiscount, result);
    }

    @Test
    void shouldReturnZeroDiscountForNonLoyalCustomer() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(false);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldReturnZeroDiscountForLoyalCustomerWithEmptyBill() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(true);
        when(bill.isEmpty()).thenReturn(true);
        
        Money result = discount.calculateDiscount(bill, customer);
        
        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldCheckLoyaltyDiscountApplicability() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(true);
        when(bill.isEmpty()).thenReturn(false);
        
        assertTrue(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckLoyaltyDiscountNotApplicableForNonLoyalCustomer() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(false);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldCheckLoyaltyDiscountNotApplicableForEmptyBill() {
        LoyaltyDiscount discount = new LoyaltyDiscount(new BigDecimal("5"));
        
        when(customer.isLoyalCustomer(any(LocalDateTime.class))).thenReturn(true);
        when(bill.isEmpty()).thenReturn(true);
        
        assertFalse(discount.isApplicable(bill, customer));
    }

    @Test
    void shouldTestDiscountTypeEnumValues() {
        assertEquals(1, DiscountType.EMPLOYEE.getPriority());
        assertEquals(2, DiscountType.AFFILIATE.getPriority());
        assertEquals(3, DiscountType.LOYALTY.getPriority());
        assertEquals(4, DiscountType.BILL_BASED.getPriority());
    }

    @Test
    void shouldTestDiscountTypePercentageBasedCheck() {
        assertTrue(DiscountType.EMPLOYEE.isPercentageBased());
        assertTrue(DiscountType.AFFILIATE.isPercentageBased());
        assertTrue(DiscountType.LOYALTY.isPercentageBased());
        assertFalse(DiscountType.BILL_BASED.isPercentageBased());
    }

    @Test
    void shouldTestDiscountEquality() {
        EmployeeDiscount discount1 = new EmployeeDiscount(new BigDecimal("30"));
        EmployeeDiscount discount2 = new EmployeeDiscount(new BigDecimal("30"));
        AffiliateDiscount discount3 = new AffiliateDiscount(new BigDecimal("10"));
        
        assertEquals(discount1, discount2);
        assertNotEquals(discount1, discount3);
        assertNotEquals(discount1, null);
        assertNotEquals(discount1, "string");
        assertEquals(discount1.hashCode(), discount2.hashCode());
    }

    @Test
    void shouldTestDiscountToString() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        String result = discount.toString();
        
        assertTrue(result.contains("EMPLOYEE"));
        assertTrue(result.contains("30% discount"));
        assertTrue(result.contains("Discount{"));
    }

    @Test
    void shouldTestDiscountConstructorWithNullType() {
        assertThrows(NullPointerException.class, () -> {
            new TestDiscount(null, "test");
        });
    }

    @Test
    void shouldTestDiscountConstructorWithNullDescription() {
        assertThrows(NullPointerException.class, () -> {
            new TestDiscount(DiscountType.EMPLOYEE, null);
        });
    }

    @Test
    void shouldTestDiscountGetters() {
        EmployeeDiscount discount = new EmployeeDiscount(new BigDecimal("30"));
        
        assertEquals(DiscountType.EMPLOYEE, discount.getType());
        assertNotNull(discount.getDescription());
        assertTrue(discount.getDescription().contains("30% discount for employees"));
    }

    private static class TestDiscount extends Discount {
        public TestDiscount(DiscountType type, String description) {
            super(type, description);
        }

        @Override
        public Money calculateDiscount(Bill bill, Customer customer) {
            return Money.zero();
        }

        @Override
        public boolean isApplicable(Bill bill, Customer customer) {
            return false;
        }
    }
}