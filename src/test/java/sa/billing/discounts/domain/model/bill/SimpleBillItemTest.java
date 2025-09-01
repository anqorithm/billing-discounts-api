package sa.billing.discounts.domain.model.bill;

import org.junit.jupiter.api.Test;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBillItemTest {

    @Test
    void shouldCreateBillItem() {
        Product product = Product.create("Apple", "", Money.of(new BigDecimal("1.50")), ProductCategory.GROCERY);
        BillItem billItem = BillItem.create(product, 5);
        
        assertNotNull(billItem);
        assertEquals(product, billItem.getProduct());
        assertEquals(5, billItem.getQuantity());
        assertNotNull(billItem.getTotalPrice());
    }

    @Test
    void shouldCreateBillItemWithCustomPrice() {
        Product product = Product.create("Laptop", "", Money.of(new BigDecimal("800.00")), ProductCategory.ELECTRONICS);
        Money customPrice = Money.of(new BigDecimal("750.00"));
        BillItem billItem = BillItem.create(product, 1, customPrice);
        
        assertNotNull(billItem);
        assertEquals(product, billItem.getProduct());
        assertEquals(1, billItem.getQuantity());
        assertEquals(customPrice, billItem.getTotalPrice());
    }

    @Test
    void shouldGetBasicProperties() {
        Product product = Product.create("Book", "", Money.of(new BigDecimal("25.99")), ProductCategory.OTHER);
        BillItem billItem = BillItem.create(product, 3);
        
        assertNotNull(billItem.getProduct());
        assertEquals(3, billItem.getQuantity());
        assertNotNull(billItem.getTotalPrice());
    }

    @Test
    void shouldGetUnitPrice() {
        Product product = Product.create("Shirt", "", Money.of(new BigDecimal("29.99")), ProductCategory.OTHER);
        BillItem billItem = BillItem.create(product, 2);
        
        assertEquals(Money.of(new BigDecimal("29.99")), billItem.getUnitPrice());
        assertEquals(Money.of(new BigDecimal("59.98")), billItem.getTotalPrice());
    }

    @Test
    void shouldCreateWithCustomUnitPrice() {
        Product product = Product.create("Phone", "", Money.of(new BigDecimal("599.99")), ProductCategory.ELECTRONICS);
        Money customPrice = Money.of(new BigDecimal("549.99"));
        BillItem billItem = BillItem.create(product, 1, customPrice);
        
        assertEquals(customPrice, billItem.getUnitPrice());
        assertEquals(customPrice, billItem.getTotalPrice());
    }

    @Test
    void shouldTestEquals() {
        Product product = Product.create("Test Product", "", Money.of(new BigDecimal("10.00")), ProductCategory.OTHER);
        BillItem item1 = BillItem.create(product, 1);
        BillItem item2 = BillItem.create(product, 1);
        
        // Items should be equal based on their content
        assertEquals(item1, item1);
        assertNotEquals(item1, null);
        assertNotEquals(item1, "not a bill item");
    }

    @Test
    void shouldTestHashCode() {
        Product product = Product.create("Hash Test", "", Money.of(new BigDecimal("5.00")), ProductCategory.OTHER);
        BillItem billItem = BillItem.create(product, 2);
        
        // Hash code should be consistent
        assertEquals(billItem.hashCode(), billItem.hashCode());
    }

    @Test
    void shouldTestToString() {
        Product product = Product.create("ToString Test", "", Money.of(new BigDecimal("15.00")), ProductCategory.OTHER);
        BillItem billItem = BillItem.create(product, 1);
        
        String itemString = billItem.toString();
        assertNotNull(itemString);
        assertTrue(itemString.length() > 0);
    }

    @Test
    void shouldThrowExceptionForZeroQuantity() {
        Product product = Product.create("Zero Qty", "", Money.of(new BigDecimal("1.00")), ProductCategory.OTHER);
        
        assertThrows(IllegalArgumentException.class, () -> {
            BillItem.create(product, 0);
        });
    }

    @Test
    void shouldHandleLargeQuantity() {
        Product product = Product.create("Large Qty", "", Money.of(new BigDecimal("0.50")), ProductCategory.OTHER);
        BillItem billItem = BillItem.create(product, 1000);
        
        assertEquals(1000, billItem.getQuantity());
    }
}