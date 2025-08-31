package sa.billing.discounts.domain.model.bill;

import java.math.BigDecimal;

import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.discount.Discount;
import sa.billing.discounts.domain.model.discount.DiscountType;
import sa.billing.discounts.domain.model.valueobject.Money;
public class BillBasedDiscount extends Discount {
    private final Money discountThreshold;
    private final Money discountAmount;
    
    public BillBasedDiscount(BigDecimal threshold, BigDecimal amount) {
        super(DiscountType.BILL_BASED, "$" + amount + " for every $" + threshold + " on the bill");
        this.discountThreshold = Money.of(threshold);
        this.discountAmount = Money.of(amount);
    }
    
    @Override
    public Money calculateDiscount(Bill bill, Customer customer) {
        if (!isApplicable(bill, customer)) {
            return Money.zero();
        }
        
        Money subtotal = bill.calculateSubtotal();
        BigDecimal discountMultiplier = subtotal.getAmount()
                .divide(discountThreshold.getAmount(), 0, BigDecimal.ROUND_DOWN);
        
        return discountAmount.multiply(discountMultiplier);
    }
    
    @Override
    public boolean isApplicable(Bill bill, Customer customer) {
        return !bill.isEmpty() && 
               bill.calculateSubtotal().isGreaterThanOrEqual(discountThreshold);
    }
}