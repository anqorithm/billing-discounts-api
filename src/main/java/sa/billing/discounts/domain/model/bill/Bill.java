package sa.billing.discounts.domain.model.bill;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document(collection = "bills")
public class Bill {
    
    @Id
    private String id;
    private String customerId;
    private List<BillItem> items;
    private Money subtotal;
    private Money totalDiscount;
    private Money netAmount;
    private BillStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected Bill() {
        this.items = new ArrayList<>();
    }
    
    private Bill(String customerId, List<BillItem> items) {
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.items = new ArrayList<>(Objects.requireNonNull(items, "Items cannot be null"));
        this.status = BillStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateAmounts();
    }
    
    public static Bill create(String customerId, List<BillItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Bill must have at least one item");
        }
        return new Bill(customerId, items);
    }
    
    public void addItem(BillItem item) {
        Objects.requireNonNull(item, "Bill item cannot be null");
        this.items.add(item);
        calculateAmounts();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeItem(BillItem item) {
        this.items.remove(item);
        calculateAmounts();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Money calculateSubtotal() {
        return items.stream()
                .map(BillItem::getTotalPrice)
                .reduce(Money.zero(), Money::add);
    }
    
    public Money calculateEligibleAmountForPercentageDiscount() {
        return items.stream()
                .map(BillItem::calculateEligibleAmountForPercentageDiscount)
                .reduce(Money.zero(), Money::add);
    }
    
    public void applyDiscount(Money discountAmount) {
        this.totalDiscount = Objects.requireNonNull(discountAmount, "Discount amount cannot be null");
        calculateAmounts();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void finalize() {
        if (status == BillStatus.FINALIZED) {
            throw new IllegalStateException("Bill is already finalized");
        }
        this.status = BillStatus.FINALIZED;
        this.updatedAt = LocalDateTime.now();
    }
    
    private void calculateAmounts() {
        this.subtotal = calculateSubtotal();
        if (this.totalDiscount == null) {
            this.totalDiscount = Money.zero();
        }
        this.netAmount = this.subtotal.subtract(this.totalDiscount);
    }
    
    public String getId() {
        return id;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public List<BillItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public Money getSubtotal() {
        return subtotal;
    }
    
    public Money getTotalDiscount() {
        return totalDiscount != null ? totalDiscount : Money.zero();
    }
    
    public Money getNetAmount() {
        return netAmount;
    }
    
    public BillStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public boolean isFinalized() {
        return status == BillStatus.FINALIZED;
    }
    
    public Money getNonGroceryAmount() {
        return items.stream()
            .filter(item -> !item.isGrocery())
            .map(BillItem::getTotalPrice)
            .reduce(Money.zero(), Money::add);
    }
    
    public boolean hasNonGroceryItems() {
        return items.stream()
            .anyMatch(item -> !item.isGrocery());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(id, bill.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", subtotal=" + subtotal +
                ", totalDiscount=" + totalDiscount +
                ", netAmount=" + netAmount +
                ", status=" + status +
                '}';
    }
}