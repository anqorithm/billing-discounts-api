package sa.billing.discounts.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private final BigDecimal amount;
    
    private Money(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
    }
    
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
    
    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    
    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }
    
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }
    
    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);
        return new Money(result.max(BigDecimal.ZERO));
    }
    
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier));
    }
    
    public Money multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }
    
    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
    
    @Override
    public String toString() {
        return "$" + amount.toString();
    }
}