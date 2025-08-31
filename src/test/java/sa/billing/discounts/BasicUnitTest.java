package sa.billing.discounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Basic Unit Tests with Mockito")
public class BasicUnitTest {

    @Mock
    private Object mockObject;

    @Test
    @DisplayName("Should verify Mockito is working")
    void shouldVerifyMockitoIsWorking() {
        // Given - Create a proper mockable interface
        java.util.List<String> mockList = mock(java.util.List.class);
        when(mockList.size()).thenReturn(5);
        
        // When
        int result = mockList.size();
        
        // Then
        assertEquals(5, result);
        verify(mockList).size();
    }

    @Test
    @DisplayName("Should test basic arithmetic")
    void shouldTestBasicArithmetic() {
        // Given
        BigDecimal value1 = new BigDecimal("100.00");
        BigDecimal value2 = new BigDecimal("30.00");
        
        // When
        BigDecimal result = value1.multiply(value2).divide(new BigDecimal("100"));
        
        // Then
        assertEquals(0, new BigDecimal("30.00").compareTo(result));
    }

    @Test
    @DisplayName("Should test null safety")
    void shouldTestNullSafety() {
        // Given
        String nullString = null;
        
        // Then
        assertThrows(NullPointerException.class, () -> {
            nullString.length();
        });
    }
}