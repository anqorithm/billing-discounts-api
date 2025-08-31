package sa.billing.discounts.domain.model.bill;

import java.util.Objects;

import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;
public class BillItem {
    private final Product product;
    private final int quantity;
    private final Money unitPrice;
    private final Money totalPrice;
    
    private BillItem(Product product, int quantity, Money unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.product = Objects.requireNonNull(product, "Product cannot be null");
        this.quantity = quantity;
        this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");
        this.totalPrice = unitPrice.multiply(quantity);
    }
    
    public static BillItem create(Product product, int quantity) {
        return new BillItem(product, quantity, product.getPrice());
    }
    
    public static BillItem create(Product product, int quantity, Money unitPrice) {
        return new BillItem(product, quantity, unitPrice);
    }
    
    public boolean isEligibleForPercentageDiscount() {
        return product.isEligibleForPercentageDiscount();
    }
    
    public Money calculateEligibleAmountForPercentageDiscount() {
        return isEligibleForPercentageDiscount() ? totalPrice : Money.zero();
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Money getUnitPrice() {
        return unitPrice;
    }
    
    public Money getTotalPrice() {
        return totalPrice;
    }
    
    public boolean isGrocery() {
        return product.getCategory() == ProductCategory.GROCERY;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return quantity == billItem.quantity &&
                Objects.equals(product, billItem.product) &&
                Objects.equals(unitPrice, billItem.unitPrice);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, unitPrice);
    }
    
    @Override
    public String toString() {
        return "BillItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}