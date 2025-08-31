package sa.billing.discounts.domain.model.discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.valueobject.Percentage;
public class LoyaltyDiscount extends Discount {
    private final Percentage discountPercentage;
    
    public LoyaltyDiscount(BigDecimal percentage) {
        super(DiscountType.LOYALTY, percentage + "% discount for loyal customers (2+ years)");
        this.discountPercentage = Percentage.of(percentage);
    }
    
    @Override
    public Money calculateDiscount(Bill bill, Customer customer) {
        if (!isApplicable(bill, customer)) {
            return Money.zero();
        }
        
        Money eligibleAmount = bill.calculateEligibleAmountForPercentageDiscount();
        return discountPercentage.applyTo(eligibleAmount);
    }
    
    @Override
    public boolean isApplicable(Bill bill, Customer customer) {
        return customer.isLoyalCustomer(LocalDateTime.now()) && !bill.isEmpty();
    }
}