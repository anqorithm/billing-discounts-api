package sa.billing.discounts.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Percentage Value Object Tests")
class PercentageTest {

    @Test
    @DisplayName("Should create percentage from BigDecimal")
    void shouldCreatePercentageFromBigDecimal() {
        Percentage percentage = Percentage.of(new BigDecimal("25.50"));
        
        assertEquals(new BigDecimal("25.5000"), percentage.getValue());
    }

    @Test
    @DisplayName("Should create percentage from double")
    void shouldCreatePercentageFromDouble() {
        Percentage percentage = Percentage.of(30.0);
        
        assertEquals(new BigDecimal("30.0000"), percentage.getValue());
    }

    @Test
    @DisplayName("Should create percentage from int")
    void shouldCreatePercentageFromInt() {
        Percentage percentage = Percentage.of(15);
        
        assertEquals(new BigDecimal("15.0000"), percentage.getValue());
    }

    @Test
    @DisplayName("Should create zero percentage")
    void shouldCreateZeroPercentage() {
        Percentage percentage = Percentage.zero();
        
        assertEquals(BigDecimal.ZERO.setScale(4), percentage.getValue());
        assertTrue(percentage.isZero());
    }

    @Test
    @DisplayName("Should apply percentage to money amount correctly")
    void shouldApplyPercentageToMoneyAmountCorrectly() {
        Percentage percentage = Percentage.of(30);
        Money amount = Money.of("100.00");
        
        Money discount = percentage.applyTo(amount);
        
        assertEquals(new BigDecimal("30.00"), discount.getAmount());
    }

    @Test
    @DisplayName("Should apply fractional percentage correctly")
    void shouldApplyFractionalPercentageCorrectly() {
        Percentage percentage = Percentage.of(12.5);
        Money amount = Money.of("200.00");
        
        Money discount = percentage.applyTo(amount);
        
        assertEquals(new BigDecimal("25.00"), discount.getAmount());
    }

    @Test
    @DisplayName("Should throw exception for null percentage")
    void shouldThrowExceptionForNullPercentage() {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of((BigDecimal) null));
    }

    @Test
    @DisplayName("Should throw exception for negative percentage")
    void shouldThrowExceptionForNegativePercentage() {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(-5.0));
    }

    @Test
    @DisplayName("Should throw exception for percentage over 100")
    void shouldThrowExceptionForPercentageOver100() {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(101.0));
    }

    @Test
    @DisplayName("Should allow exactly 100 percent")
    void shouldAllowExactly100Percent() {
        assertDoesNotThrow(() -> Percentage.of(100.0));
        
        Percentage percentage = Percentage.of(100.0);
        assertEquals(new BigDecimal("100.0000"), percentage.getValue());
    }

    @Test
    @DisplayName("Should have proper equals and hashCode")
    void shouldHaveProperEqualsAndHashCode() {
        Percentage percentage1 = Percentage.of(30.0);
        Percentage percentage2 = Percentage.of(30.0);
        Percentage percentage3 = Percentage.of(25.0);
        
        assertEquals(percentage1, percentage2);
        assertNotEquals(percentage1, percentage3);
        assertEquals(percentage1.hashCode(), percentage2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        Percentage percentage = Percentage.of(30.5);
        
        assertEquals("30.5000%", percentage.toString());
    }
}