package sa.billing.discounts.domain.model.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    private String name;
    private String description;
    private Money price;
    private ProductCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected Product() {
    }
    
    private Product(String name, String description, Money price, ProductCategory category) {
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.description = description;
        this.price = Objects.requireNonNull(price, "Product price cannot be null");
        this.category = Objects.requireNonNull(category, "Product category cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public static Product create(String name, String description, Money price, ProductCategory category) {
        return new Product(name, description, price, category);
    }
    
    public boolean isGrocery() {
        return category == ProductCategory.GROCERY;
    }
    
    public boolean isEligibleForPercentageDiscount() {
        return !isGrocery();
    }
    
    public void updatePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "Product price cannot be null");
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCategory(ProductCategory newCategory) {
        this.category = Objects.requireNonNull(newCategory, "Product category cannot be null");
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Money getPrice() {
        return price;
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        // If both ids are null, compare by business identity (name + price + category)
        if (this.id == null && product.id == null) {
            return Objects.equals(this.name, product.name) && 
                   Objects.equals(this.price, product.price) &&
                   Objects.equals(this.category, product.category);
        }
        // If either id is null (but not both), treat as not equal (distinct transient/persistent entities)
        if (this.id == null || product.id == null) return false;
        return Objects.equals(this.id, product.id);
    }
    
    @Override
    public int hashCode() {
        // For consistent hashing with equals method
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, price, category);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
