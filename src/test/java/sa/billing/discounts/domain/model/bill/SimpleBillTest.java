package sa.billing.discounts.domain.model.bill;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBillTest {

    private List<BillItem> items;
    private Product groceryProduct;
    private Product electronicsProduct;

    @BeforeEach
    void setUp() {
        groceryProduct = Product.create("Bread", "", Money.of(new BigDecimal("2.50")), ProductCategory.GROCERY);
        electronicsProduct = Product.create("Phone", "", Money.of(new BigDecimal("500.00")), ProductCategory.ELECTRONICS);
        
        items = new ArrayList<>();
        items.add(BillItem.create(groceryProduct, 2));
        items.add(BillItem.create(electronicsProduct, 1));
    }

    @Test
    void shouldCreateBillWithFactoryMethod() {
        Bill bill = Bill.create("customer123", items);
        
        assertNotNull(bill);
        assertEquals("customer123", bill.getCustomerId());
        assertEquals(2, bill.getItems().size());
        // ID generation handled internally
        assertNotNull(bill.getCreatedAt());
        assertNotNull(bill.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionForEmptyBill() {
        assertThrows(IllegalArgumentException.class, () -> {
            Bill.create("customer456", new ArrayList<>());
        });
    }

    @Test
    void shouldGetBasicProperties() {
        Bill bill = Bill.create("customer789", items);
        
        // ID generation handled internally
        assertEquals("customer789", bill.getCustomerId());
        assertFalse(bill.isEmpty());
        assertNotNull(bill.getSubtotal());
        assertNotNull(bill.getNetAmount());
        assertNotNull(bill.getStatus());
        assertNotNull(bill.getCreatedAt());
        assertNotNull(bill.getUpdatedAt());
    }

    @Test
    void shouldAddItemToBill() {
        Bill bill = Bill.create("customer999", items);
        Product newProduct = Product.create("Book", "", Money.of(new BigDecimal("15.99")), ProductCategory.OTHER);
        BillItem newItem = BillItem.create(newProduct, 1);
        
        bill.addItem(newItem);
        
        assertEquals(3, bill.getItems().size());
    }

    @Test
    void shouldRemoveItemFromBill() {
        Bill bill = Bill.create("customer111", items);
        BillItem firstItem = bill.getItems().get(0);
        
        bill.removeItem(firstItem);
        
        assertEquals(1, bill.getItems().size());
    }

    @Test
    void shouldApplyDiscount() {
        Bill bill = Bill.create("customer222", items);
        Money discount = Money.of(new BigDecimal("50.00"));
        
        bill.applyDiscount(discount);
        
        assertEquals(discount, bill.getTotalDiscount());
    }

    @Test
    void shouldFinalizeBill() {
        Bill bill = Bill.create("customer333", items);
        
        assertFalse(bill.isFinalized());
        
        bill.finalize();
        
        assertTrue(bill.isFinalized());
        assertEquals(BillStatus.FINALIZED, bill.getStatus());
    }

    @Test
    void shouldCalculateNonGroceryAmount() {
        Bill bill = Bill.create("customer444", items);
        Money nonGroceryAmount = bill.getNonGroceryAmount();
        
        assertNotNull(nonGroceryAmount);
        assertTrue(nonGroceryAmount.getAmount().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void shouldCheckHasNonGroceryItems() {
        Bill bill = Bill.create("customer555", items);
        assertTrue(bill.hasNonGroceryItems());
        
        List<BillItem> groceryOnlyItems = new ArrayList<>();
        groceryOnlyItems.add(BillItem.create(groceryProduct, 1));
        Bill groceryOnlyBill = Bill.create("customer666", groceryOnlyItems);
        
        assertFalse(groceryOnlyBill.hasNonGroceryItems());
    }

    @Test
    void shouldTestEquality() {
        Bill bill1 = Bill.create("customer777", items);
        Bill bill2 = Bill.create("customer888", items);
        
        // Different bills may be equal if same content
        assertNotNull(bill1);
        assertNotNull(bill2);
        
        // Bill should equal itself
        assertEquals(bill1, bill1);
        
        // Null comparison
        assertNotEquals(bill1, null);
        
        // Different type comparison
        assertNotEquals(bill1, "not a bill");
    }

    @Test
    void shouldTestHashCode() {
        Bill bill = Bill.create("customer999", items);
        
        // Hash code should be consistent
        assertEquals(bill.hashCode(), bill.hashCode());
        
        // Hash code should not be zero (most likely)
        assertNotEquals(0, bill.hashCode());
    }

    @Test
    void shouldTestToString() {
        Bill bill = Bill.create("customerABC", items);
        String billString = bill.toString();
        
        assertNotNull(billString);
        assertTrue(billString.length() > 0);
        assertTrue(billString.contains("Bill"));
    }

    @Test
    void shouldGetTotalDiscountZeroInitially() {
        Bill bill = Bill.create("customerDEF", items);
        
        assertNotNull(bill.getTotalDiscount());
        assertEquals(Money.zero(), bill.getTotalDiscount());
    }

    @Test
    void shouldCalculateEligibleAmountForPercentageDiscount() {
        Bill bill = Bill.create("customerGHI", items);
        Money eligibleAmount = bill.calculateEligibleAmountForPercentageDiscount();
        
        assertNotNull(eligibleAmount);
    }
}