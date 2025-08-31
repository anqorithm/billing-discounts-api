package sa.billing.discounts.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money Value Object Tests")
class MoneyTest {

    @Test
    @DisplayName("Should create money from BigDecimal")
    void shouldCreateMoneyFromBigDecimal() {
        Money money = Money.of(new BigDecimal("100.50"));
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("Should create money from double")
    void shouldCreateMoneyFromDouble() {
        Money money = Money.of(100.50);
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("Should create money from string")
    void shouldCreateMoneyFromString() {
        Money money = Money.of("100.50");
        
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("Should create zero money")
    void shouldCreateZeroMoney() {
        Money money = Money.zero();
        
        assertEquals(BigDecimal.ZERO.setScale(2), money.getAmount());
        assertTrue(money.isZero());
    }

    @Test
    @DisplayName("Should add money correctly")
    void shouldAddMoneyCorrectly() {
        Money money1 = Money.of("100.50");
        Money money2 = Money.of("50.25");
        
        Money result = money1.add(money2);
        
        assertEquals(new BigDecimal("150.75"), result.getAmount());
    }

    @Test
    @DisplayName("Should subtract money correctly")
    void shouldSubtractMoneyCorrectly() {
        Money money1 = Money.of("100.50");
        Money money2 = Money.of("50.25");
        
        Money result = money1.subtract(money2);
        
        assertEquals(new BigDecimal("50.25"), result.getAmount());
    }

    @Test
    @DisplayName("Should not allow negative result in subtraction")
    void shouldNotAllowNegativeResultInSubtraction() {
        Money money1 = Money.of("50.00");
        Money money2 = Money.of("100.00");
        
        Money result = money1.subtract(money2);
        
        assertEquals(BigDecimal.ZERO.setScale(2), result.getAmount());
    }

    @Test
    @DisplayName("Should multiply money correctly")
    void shouldMultiplyMoneyCorrectly() {
        Money money = Money.of("100.00");
        
        Money result = money.multiply(2.5);
        
        assertEquals(new BigDecimal("250.00"), result.getAmount());
    }

    @Test
    @DisplayName("Should compare money amounts correctly")
    void shouldCompareMoneyAmountsCorrectly() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("50.00");
        Money money3 = Money.of("100.00");
        
        assertTrue(money1.isGreaterThan(money2));
        assertFalse(money2.isGreaterThan(money1));
        assertTrue(money1.isGreaterThanOrEqual(money3));
        assertTrue(money1.isGreaterThanOrEqual(money2));
    }

    @Test
    @DisplayName("Should throw exception for null amount")
    void shouldThrowExceptionForNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of((BigDecimal) null));
    }

    @Test
    @DisplayName("Should throw exception for negative amount")
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-10.00"));
    }

    @Test
    @DisplayName("Should have proper equals and hashCode")
    void shouldHaveProperEqualsAndHashCode() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("100.00");
        Money money3 = Money.of("200.00");
        
        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        Money money = Money.of("100.50");
        
        assertEquals("$100.50", money.toString());
    }
}