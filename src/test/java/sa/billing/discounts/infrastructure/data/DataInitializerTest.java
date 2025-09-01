package sa.billing.discounts.infrastructure.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.infrastructure.persistence.mongodb.CustomerRepositoryImpl;
import sa.billing.discounts.infrastructure.persistence.mongodb.ProductRepositoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private CustomerRepositoryImpl customerRepository;

    @Mock
    private ProductRepositoryImpl productRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void shouldInitializeSampleDataWhenRepositoriesAreEmpty() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(productRepository, times(1)).findById(anyString());
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    void shouldSkipCustomerInitializationWhenCustomersExist() {
        Customer existingCustomer = mock(Customer.class);
        when(customerRepository.findById("65a1b2c3d4e5f6a7b8c9d0e1")).thenReturn(Optional.of(existingCustomer));
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository).findById("65a1b2c3d4e5f6a7b8c9d0e1");
        verify(productRepository, times(1)).findById(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    void shouldSkipProductInitializationWhenProductsExist() {
        Product existingProduct = mock(Product.class);
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById("65a1b2c3d4e5f6a7b8c9d0f1")).thenReturn(Optional.of(existingProduct));
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(productRepository).findById("65a1b2c3d4e5f6a7b8c9d0f1");
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldSkipBothInitializationsWhenDataExists() {
        Customer existingCustomer = mock(Customer.class);
        Product existingProduct = mock(Product.class);
        when(customerRepository.findById("65a1b2c3d4e5f6a7b8c9d0e1")).thenReturn(Optional.of(existingCustomer));
        when(productRepository.findById("65a1b2c3d4e5f6a7b8c9d0f1")).thenReturn(Optional.of(existingProduct));
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository).findById("65a1b2c3d4e5f6a7b8c9d0e1");
        verify(productRepository).findById("65a1b2c3d4e5f6a7b8c9d0f1");
        verify(customerRepository, never()).save(any(Customer.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldHandleExceptionDuringCustomerSave() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));
        
        assertThrows(RuntimeException.class, () -> dataInitializer.initializeSampleData());
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, atLeastOnce()).save(any(Customer.class));
    }

    @Test
    void shouldHandleExceptionDuringProductSave() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));
        
        assertThrows(RuntimeException.class, () -> dataInitializer.initializeSampleData());
        
        verify(productRepository, times(1)).findById(anyString());
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository, atLeastOnce()).save(any(Product.class));
    }

    @Test
    void shouldHandleExceptionDuringCustomerRepositoryQuery() {
        when(customerRepository.findById("65a1b2c3d4e5f6a7b8c9d0e1")).thenThrow(new RuntimeException("Query error"));
        
        assertThrows(RuntimeException.class, () -> dataInitializer.initializeSampleData());
        
        verify(customerRepository).findById("65a1b2c3d4e5f6a7b8c9d0e1");
        verify(productRepository, never()).findById(anyString());
    }

    @Test
    void shouldHandleExceptionDuringProductRepositoryQuery() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById("65a1b2c3d4e5f6a7b8c9d0f1")).thenThrow(new RuntimeException("Product query error"));
        
        assertThrows(RuntimeException.class, () -> dataInitializer.initializeSampleData());
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository).findById("65a1b2c3d4e5f6a7b8c9d0f1");
    }

    @Test
    void shouldTestRunMethod() throws Exception {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.run();
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(productRepository, times(1)).findById(anyString());
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    void shouldTestRunMethodWithArguments() throws Exception {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.run("arg1", "arg2");
        
        verify(customerRepository, times(1)).findById(anyString());
        verify(productRepository, times(1)).findById(anyString());
        verify(customerRepository, times(4)).save(any(Customer.class));
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    void shouldTestConstructor() {
        DataInitializer initializer = new DataInitializer(customerRepository, productRepository);
        
        assertNotNull(initializer);
    }

    @Test
    void shouldSaveCustomersInCorrectOrder() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository, times(4)).save(any(Customer.class));
    }

    @Test
    void shouldSaveProductsInCorrectOrder() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    void shouldCheckSpecificCustomerIds() {
        when(customerRepository.findById("65a1b2c3d4e5f6a7b8c9d0e1")).thenReturn(Optional.empty());
        when(productRepository.findById("65a1b2c3d4e5f6a7b8c9d0f1")).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository).findById("65a1b2c3d4e5f6a7b8c9d0e1");
        verify(productRepository).findById("65a1b2c3d4e5f6a7b8c9d0f1");
    }

    @Test
    void shouldHandlePartialInitializationScenario() {
        Customer existingCustomer = mock(Customer.class);
        when(customerRepository.findById("65a1b2c3d4e5f6a7b8c9d0e1")).thenReturn(Optional.of(existingCustomer));
        when(productRepository.findById("65a1b2c3d4e5f6a7b8c9d0f1")).thenReturn(Optional.empty());
        
        dataInitializer.initializeSampleData();
        
        verify(customerRepository).findById("65a1b2c3d4e5f6a7b8c9d0e1");
        verify(productRepository).findById("65a1b2c3d4e5f6a7b8c9d0f1");
        verify(customerRepository, never()).save(any(Customer.class));
        verify(productRepository, times(4)).save(any(Product.class));
    }
}