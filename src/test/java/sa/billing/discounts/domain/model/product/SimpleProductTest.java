package sa.billing.discounts.domain.model.product;

import org.junit.jupiter.api.Test;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SimpleProductTest {

    @Test
    void shouldCreateProductWithBasicInfo() {
        Money price = Money.of(new BigDecimal("29.99"));
        Product product = Product.create("Test Product", "A test product", price, ProductCategory.ELECTRONICS);
        
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals("A test product", product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(ProductCategory.ELECTRONICS, product.getCategory());
    }

    @Test
    void shouldGetBasicProperties() {
        Money price = Money.of(new BigDecimal("15.50"));
        Product product = Product.create("Basic Product", "Basic description", price, ProductCategory.GROCERY);
        
        // ID generation handled internally
        assertEquals("Basic Product", product.getName());
        assertEquals("Basic description", product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(ProductCategory.GROCERY, product.getCategory());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldTestProductsWithDifferentCategories() {
        Money price = Money.of(new BigDecimal("10.00"));
        
        Product groceryProduct = Product.create("Apple", "", price, ProductCategory.GROCERY);
        Product electronicsProduct = Product.create("Phone", "", price, ProductCategory.ELECTRONICS);
        Product otherProduct = Product.create("Book", "", price, ProductCategory.OTHER);
        
        assertEquals(ProductCategory.GROCERY, groceryProduct.getCategory());
        assertEquals(ProductCategory.ELECTRONICS, electronicsProduct.getCategory());
        assertEquals(ProductCategory.OTHER, otherProduct.getCategory());
    }

    @Test
    void shouldTestEquals() {
        Money price = Money.of(new BigDecimal("25.99"));
        Product product1 = Product.create("Same Product", "Same desc", price, ProductCategory.OTHER);
        Product product2 = Product.create("Same Product", "Same desc", price, ProductCategory.OTHER);
        
        assertEquals(product1, product1);
        assertNotEquals(product1, null);
        assertNotEquals(product1, "not a product");
        
        // Products with same business data should be equal
        assertEquals(product1, product2);
    }

    @Test
    void shouldTestHashCode() {
        Money price = Money.of(new BigDecimal("12.50"));
        Product product1 = Product.create("Hash Test", "Description", price, ProductCategory.GROCERY);
        Product product2 = Product.create("Hash Test", "Description", price, ProductCategory.GROCERY);
        
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void shouldTestToString() {
        Money price = Money.of(new BigDecimal("99.99"));
        Product product = Product.create("ToString Test", "Test description", price, ProductCategory.ELECTRONICS);
        
        String productString = product.toString();
        assertNotNull(productString);
        assertTrue(productString.length() > 0);
        assertTrue(productString.contains("Product"));
    }

    @Test
    void shouldHandleEmptyDescription() {
        Money price = Money.of(new BigDecimal("5.99"));
        Product product = Product.create("No Description", "", price, ProductCategory.OTHER);
        
        assertEquals("", product.getDescription());
    }

    @Test
    void shouldHandleLongNames() {
        String longName = "Very Long Product Name That Exceeds Normal Product Name Length For Testing";
        Money price = Money.of(new BigDecimal("19.99"));
        Product product = Product.create(longName, "Long name test", price, ProductCategory.OTHER);
        
        assertEquals(longName, product.getName());
    }

    @Test
    void shouldHandleLongDescriptions() {
        String longDescription = "This is a very long product description that contains many words and details about the product to test how the system handles longer text fields in the product entity.";
        Money price = Money.of(new BigDecimal("39.99"));
        Product product = Product.create("Long Desc", longDescription, price, ProductCategory.OTHER);
        
        assertEquals(longDescription, product.getDescription());
    }

    @Test
    void shouldHandleHighPrices() {
        Money highPrice = Money.of(new BigDecimal("9999.99"));
        Product product = Product.create("Expensive Item", "Very expensive", highPrice, ProductCategory.ELECTRONICS);
        
        assertEquals(highPrice, product.getPrice());
    }

    @Test
    void shouldHandleLowPrices() {
        Money lowPrice = Money.of(new BigDecimal("0.01"));
        Product product = Product.create("Cheap Item", "Very cheap", lowPrice, ProductCategory.OTHER);
        
        assertEquals(lowPrice, product.getPrice());
    }

    @Test
    void shouldNotBeEqualWithDifferentNames() {
        Money price = Money.of(new BigDecimal("10.00"));
        Product product1 = Product.create("Product A", "Same desc", price, ProductCategory.OTHER);
        Product product2 = Product.create("Product B", "Same desc", price, ProductCategory.OTHER);
        
        assertNotEquals(product1, product2);
    }

    @Test
    void shouldNotBeEqualWithDifferentPrices() {
        Product product1 = Product.create("Same Name", "Same desc", Money.of(new BigDecimal("10.00")), ProductCategory.OTHER);
        Product product2 = Product.create("Same Name", "Same desc", Money.of(new BigDecimal("20.00")), ProductCategory.OTHER);
        
        assertNotEquals(product1, product2);
    }

    @Test
    void shouldNotBeEqualWithDifferentCategories() {
        Money price = Money.of(new BigDecimal("15.00"));
        Product product1 = Product.create("Same Name", "Same desc", price, ProductCategory.GROCERY);
        Product product2 = Product.create("Same Name", "Same desc", price, ProductCategory.ELECTRONICS);
        
        assertNotEquals(product1, product2);
    }
}