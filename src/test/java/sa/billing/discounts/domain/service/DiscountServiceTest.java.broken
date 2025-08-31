package sa.billing.discounts.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import sa.billing.discounts.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Discount Service Tests")
class DiscountServiceTest {

    private DiscountService discountService;
    private Customer employeeCustomer;
    private Customer affiliateCustomer;
    private Customer loyalCustomer;
    private Customer regularCustomer;
    private Product electronicProduct;
    private Product groceryProduct;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
        
        employeeCustomer = Customer.createEmployee("John Employee", "john@company.com", LocalDateTime.now().minusYears(1));
        affiliateCustomer = Customer.createAffiliate("Sarah Affiliate", "sarah@partner.com", LocalDateTime.now().minusYears(1));
        loyalCustomer = Customer.createRegular("Mike Loyal", "mike@email.com", LocalDateTime.now().minusYears(3));
        regularCustomer = Customer.createRegular("Jane Regular", "jane@email.com", LocalDateTime.now().minusMonths(6));
        
        electronicProduct = Product.create("iPhone", "Latest iPhone", Money.of("999.00"), ProductCategory.ELECTRONICS);
        groceryProduct = Product.create("Apples", "Fresh apples", Money.of("5.99"), ProductCategory.GROCERY);
    }

    @Test
    @DisplayName("Should apply 30% employee discount on eligible items")
    void shouldApply30PercentEmployeeDiscountOnEligibleItems() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1),
            BillItem.create(groceryProduct, 1)
        );
        Bill bill = Bill.create(employeeCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, employeeCustomer);
        
        assertEquals(new BigDecimal("299.70"), result.getPercentageBasedDiscount().getAmount());
        assertEquals("EMPLOYEE", result.getPercentageDiscountType());
        assertEquals(new BigDecimal("50.00"), result.getBillBasedDiscount().getAmount());
        assertEquals(new BigDecimal("349.70"), result.getTotalDiscount().getAmount());
    }

    @Test
    @DisplayName("Should apply 10% affiliate discount on eligible items")
    void shouldApply10PercentAffiliateDiscountOnEligibleItems() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1),
            BillItem.create(groceryProduct, 1)
        );
        Bill bill = Bill.create(affiliateCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, affiliateCustomer);
        
        assertEquals(new BigDecimal("99.90"), result.getPercentageBasedDiscount().getAmount());
        assertEquals("AFFILIATE", result.getPercentageDiscountType());
        assertEquals(new BigDecimal("50.00"), result.getBillBasedDiscount().getAmount());
        assertEquals(new BigDecimal("149.90"), result.getTotalDiscount().getAmount());
    }

    @Test
    @DisplayName("Should apply 5% loyalty discount for customers over 2 years")
    void shouldApply5PercentLoyaltyDiscountForCustomersOver2Years() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1),
            BillItem.create(groceryProduct, 1)
        );
        Bill bill = Bill.create(loyalCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, loyalCustomer);
        
        assertEquals(new BigDecimal("49.95"), result.getPercentageBasedDiscount().getAmount());
        assertEquals("LOYALTY", result.getPercentageDiscountType());
        assertEquals(new BigDecimal("50.00"), result.getBillBasedDiscount().getAmount());
        assertEquals(new BigDecimal("99.95"), result.getTotalDiscount().getAmount());
    }

    @Test
    @DisplayName("Should apply only bill-based discount for regular customer")
    void shouldApplyOnlyBillBasedDiscountForRegularCustomer() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1),
            BillItem.create(groceryProduct, 1)
        );
        Bill bill = Bill.create(regularCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, regularCustomer);
        
        assertEquals(BigDecimal.ZERO.setScale(2), result.getPercentageBasedDiscount().getAmount());
        assertNull(result.getPercentageDiscountType());
        assertEquals(new BigDecimal("50.00"), result.getBillBasedDiscount().getAmount());
        assertEquals(new BigDecimal("50.00"), result.getTotalDiscount().getAmount());
    }

    @Test
    @DisplayName("Should not apply percentage discount on groceries")
    void shouldNotApplyPercentageDiscountOnGroceries() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(groceryProduct, 20) // $119.80 total
        );
        Bill bill = Bill.create(employeeCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, employeeCustomer);
        
        assertEquals(BigDecimal.ZERO.setScale(2), result.getPercentageBasedDiscount().getAmount());
        assertNull(result.getPercentageDiscountType());
        assertEquals(new BigDecimal("5.00"), result.getBillBasedDiscount().getAmount());
    }

    @Test
    @DisplayName("Should apply $5 for every $100 bill-based discount")
    void shouldApply5DollarsForEvery100DollarsBillBasedDiscount() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 2), // $1998.00
            BillItem.create(groceryProduct, 1)     // $5.99
        );
        Bill bill = Bill.create(regularCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, regularCustomer);
        
        assertEquals(new BigDecimal("100.00"), result.getBillBasedDiscount().getAmount());
        assertEquals(new BigDecimal("100.00"), result.getTotalDiscount().getAmount());
    }

    @Test
    @DisplayName("Should prioritize employee discount over affiliate and loyalty")
    void shouldPrioritizeEmployeeDiscountOverAffiliateAndLoyalty() {
        Customer customer = Customer.createEmployee("Multi-role", "multi@email.com", LocalDateTime.now().minusYears(3));
        customer.updateType(CustomerType.EMPLOYEE);
        
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1)
        );
        Bill bill = Bill.create(customer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, customer);
        
        assertEquals("EMPLOYEE", result.getPercentageDiscountType());
        assertEquals(new BigDecimal("299.70"), result.getPercentageBasedDiscount().getAmount());
    }

    @Test
    @DisplayName("Should calculate correct net amount")
    void shouldCalculateCorrectNetAmount() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1), // $999.00
            BillItem.create(groceryProduct, 10)    // $59.90
        );
        Bill bill = Bill.create(employeeCustomer.getId(), items);
        
        DiscountCalculationResult result = discountService.calculateDiscountBreakdown(bill, employeeCustomer);
        
        BigDecimal expectedSubtotal = new BigDecimal("1058.90");
        BigDecimal expectedPercentageDiscount = new BigDecimal("299.70");
        BigDecimal expectedBillDiscount = new BigDecimal("50.00");
        BigDecimal expectedTotal = new BigDecimal("349.70");
        BigDecimal expectedNet = new BigDecimal("709.20");
        
        assertEquals(expectedSubtotal, result.getSubtotal().getAmount());
        assertEquals(expectedPercentageDiscount, result.getPercentageBasedDiscount().getAmount());
        assertEquals(expectedBillDiscount, result.getBillBasedDiscount().getAmount());
        assertEquals(expectedTotal, result.getTotalDiscount().getAmount());
        assertEquals(expectedNet, result.getNetAmount().getAmount());
    }

    @Test
    @DisplayName("Should return zero discount for empty bill")
    void shouldReturnZeroDiscountForEmptyBill() {
        List<BillItem> items = Arrays.asList();
        
        assertThrows(IllegalArgumentException.class, () -> {
            Bill.create(employeeCustomer.getId(), items);
        });
    }

    @Test
    @DisplayName("Should check if discount is applicable")
    void shouldCheckIfDiscountIsApplicable() {
        List<BillItem> items = Arrays.asList(
            BillItem.create(electronicProduct, 1)
        );
        Bill bill = Bill.create(employeeCustomer.getId(), items);
        
        assertTrue(discountService.isDiscountApplicable(DiscountType.EMPLOYEE, bill, employeeCustomer));
        assertFalse(discountService.isDiscountApplicable(DiscountType.AFFILIATE, bill, employeeCustomer));
        assertTrue(discountService.isDiscountApplicable(DiscountType.BILL_BASED, bill, employeeCustomer));
    }
}