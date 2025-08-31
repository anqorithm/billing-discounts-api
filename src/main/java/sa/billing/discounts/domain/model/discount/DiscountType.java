package sa.billing.discounts.domain.model.discount;

public enum DiscountType {
    EMPLOYEE(1),
    AFFILIATE(2),
    LOYALTY(3),
    BILL_BASED(4);
    
    private final int priority;
    
    DiscountType(int priority) {
        this.priority = priority;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public boolean isPercentageBased() {
        return this == EMPLOYEE || this == AFFILIATE || this == LOYALTY;
    }
}