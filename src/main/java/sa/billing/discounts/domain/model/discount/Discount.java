package sa.billing.discounts.domain.model.discount;

import java.math.BigDecimal;
import java.util.Objects;
import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.valueobject.Money;

public abstract class Discount {
    protected final DiscountType type;
    protected final String description;
    
    protected Discount(DiscountType type, String description) {
        this.type = Objects.requireNonNull(type, "Discount type cannot be null");
        this.description = Objects.requireNonNull(description, "Discount description cannot be null");
    }
    
    public abstract Money calculateDiscount(Bill bill, Customer customer);
    
    public abstract boolean isApplicable(Bill bill, Customer customer);
    
    public DiscountType getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return type == discount.type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
    
    @Override
    public String toString() {
        return "Discount{" +
                "type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}