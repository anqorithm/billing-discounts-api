package sa.billing.discounts.domain.model.discount;

import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.valueobject.Percentage;

import java.math.BigDecimal;

public class AffiliateDiscount extends Discount {
    private final Percentage discountPercentage;
    
    public AffiliateDiscount(BigDecimal percentage) {
        super(DiscountType.AFFILIATE, percentage + "% discount for affiliates on non-grocery items");
        this.discountPercentage = Percentage.of(percentage);
    }
    
    @Override
    public Money calculateDiscount(Bill bill, Customer customer) {
        if (customer.getType() != CustomerType.AFFILIATE) {
            return Money.zero();
        }
        
        return discountPercentage.applyTo(bill.getNonGroceryAmount());
    }
    
    @Override
    public boolean isApplicable(Bill bill, Customer customer) {
        return customer.getType() == CustomerType.AFFILIATE 
            && bill.hasNonGroceryItems();
    }
}