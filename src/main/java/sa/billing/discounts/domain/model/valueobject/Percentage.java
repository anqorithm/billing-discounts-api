package sa.billing.discounts.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Percentage {
    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private final BigDecimal value;
    
    private Percentage(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Percentage value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.value = value.setScale(SCALE, ROUNDING_MODE);
    }
    
    public static Percentage of(BigDecimal value) {
        return new Percentage(value);
    }
    
    public static Percentage of(double value) {
        return new Percentage(BigDecimal.valueOf(value));
    }
    
    public static Percentage of(int value) {
        return new Percentage(BigDecimal.valueOf(value));
    }
    
    public static Percentage zero() {
        return new Percentage(BigDecimal.ZERO);
    }
    
    public Money applyTo(Money amount) {
        BigDecimal discountMultiplier = value.divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
        return amount.multiply(discountMultiplier);
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percentage that = (Percentage) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value + "%";
    }
}