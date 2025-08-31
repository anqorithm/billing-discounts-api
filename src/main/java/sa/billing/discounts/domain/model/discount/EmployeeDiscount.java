package sa.billing.discounts.domain.model.discount;

import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.valueobject.Percentage;

import java.math.BigDecimal;

public class EmployeeDiscount extends Discount {
    private final Percentage discountPercentage;
    
    public EmployeeDiscount(BigDecimal percentage) {
        super(DiscountType.EMPLOYEE, percentage + "% discount for employees");
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
        return customer.isEmployee() && !bill.isEmpty();
    }
}