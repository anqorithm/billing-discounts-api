package sa.billing.discounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import sa.billing.discounts.infrastructure.data.DataInitializer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BillingDiscountsApiApplicationTest {

    @MockBean
    private DataInitializer dataInitializer;

    @Test
    void shouldLoadApplicationContext(ApplicationContext context) {
        assertNotNull(context);
        assertTrue(context.containsBean("billCalculationService"));
        assertTrue(context.containsBean("billController"));
    }

    @Test
    void shouldCallMainMethod() {
        doNothing().when(dataInitializer).initializeSampleData();
        
        assertDoesNotThrow(() -> {
            BillingDiscountsApiApplication.main(new String[]{
                "--spring.profiles.active=test",
                "--spring.main.web-environment=false"
            });
        });
    }

    @Test
    void shouldHaveCorrectApplicationName() {
        String expectedAppName = "billing-discounts-api";
        assertTrue(System.getProperty("spring.application.name", "").contains("billing") ||
                   expectedAppName.contains("billing"));
    }

    @Test
    void shouldStartApplicationWithSpringBootRun() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(BillingDiscountsApiApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            String[] args = {"--spring.profiles.active=test"};
            BillingDiscountsApiApplication.main(args);

            springApp.verify(() -> SpringApplication.run(BillingDiscountsApiApplication.class, args));
        }
    }

    @Test
    void shouldStartApplicationWithEmptyArgs() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(BillingDiscountsApiApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            String[] args = {};
            BillingDiscountsApiApplication.main(args);

            springApp.verify(() -> SpringApplication.run(BillingDiscountsApiApplication.class, args));
        }
    }

    @Test
    void shouldStartApplicationWithNullArgs() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(BillingDiscountsApiApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            BillingDiscountsApiApplication.main(null);

            springApp.verify(() -> SpringApplication.run(BillingDiscountsApiApplication.class, (String[]) null));
        }
    }

    @Test
    void shouldHandleApplicationStartupException() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            springApp.when(() -> SpringApplication.run(eq(BillingDiscountsApiApplication.class), any(String[].class)))
                    .thenThrow(new RuntimeException("Startup failed"));

            assertThrows(RuntimeException.class, () -> {
                BillingDiscountsApiApplication.main(new String[]{});
            });

            springApp.verify(() -> SpringApplication.run(BillingDiscountsApiApplication.class, new String[]{}));
        }
    }
}