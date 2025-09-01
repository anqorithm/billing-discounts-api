package sa.billing.discounts.infrastructure.persistence.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    private CustomerRepositoryImpl customerRepository;
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl(mongoTemplate);
        productRepository = new ProductRepositoryImpl(mongoTemplate);
    }

    @Test
    void shouldFindCustomerById() {
        String customerId = "customer123";
        Customer expectedCustomer = Customer.createRegular("John Doe", "john@example.com", LocalDateTime.now());
        
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(expectedCustomer);
        
        Optional<Customer> result = customerRepository.findById(customerId);
        
        assertTrue(result.isPresent());
        assertEquals(expectedCustomer, result.get());
        verify(mongoTemplate).findById(customerId, Customer.class);
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFound() {
        String customerId = "nonexistent";
        
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(null);
        
        Optional<Customer> result = customerRepository.findById(customerId);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findById(customerId, Customer.class);
    }

    @Test
    void shouldSaveCustomer() {
        Customer customer = Customer.createEmployee("Jane Doe", "jane@example.com", LocalDateTime.now());
        
        when(mongoTemplate.save(customer)).thenReturn(customer);
        
        Customer result = customerRepository.save(customer);
        
        assertEquals(customer, result);
        verify(mongoTemplate).save(customer);
    }

    @Test
    void shouldFindAllCustomers() {
        Customer customer1 = Customer.createRegular("John", "john@test.com", LocalDateTime.now());
        Customer customer2 = Customer.createEmployee("Jane", "jane@test.com", LocalDateTime.now());
        List<Customer> expectedCustomers = Arrays.asList(customer1, customer2);
        
        when(mongoTemplate.findAll(Customer.class)).thenReturn(expectedCustomers);
        
        List<Customer> result = customerRepository.findAll();
        
        assertEquals(2, result.size());
        assertEquals(expectedCustomers, result);
        verify(mongoTemplate).findAll(Customer.class);
    }

    @Test
    void shouldExistsByIdForCustomer() {
        String customerId = "existing123";
        
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(true);
        
        boolean exists = customerRepository.existsById(customerId);
        
        assertTrue(exists);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldNotExistsByIdForCustomer() {
        String customerId = "nonexistent123";
        
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(false);
        
        boolean exists = customerRepository.existsById(customerId);
        
        assertFalse(exists);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldDeleteCustomerById() {
        String customerId = "delete123";
        
        customerRepository.deleteById(customerId);
        
        verify(mongoTemplate).remove(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldNotDeleteWhenCustomerNotFound() {
        String customerId = "nonexistent456";
        
        customerRepository.deleteById(customerId);
        
        verify(mongoTemplate).remove(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldFindProductById() {
        String productId = "product123";
        Product expectedProduct = Product.create("Test Product", "", Money.of(new BigDecimal("29.99")), ProductCategory.ELECTRONICS);
        
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(expectedProduct);
        
        Optional<Product> result = productRepository.findById(productId);
        
        assertTrue(result.isPresent());
        assertEquals(expectedProduct, result.get());
        verify(mongoTemplate).findById(productId, Product.class);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        String productId = "nonexistent";
        
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(null);
        
        Optional<Product> result = productRepository.findById(productId);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findById(productId, Product.class);
    }

    @Test
    void shouldSaveProduct() {
        Product product = Product.create("New Product", "", Money.of(new BigDecimal("19.99")), ProductCategory.GROCERY);
        
        when(mongoTemplate.save(product)).thenReturn(product);
        
        Product result = productRepository.save(product);
        
        assertEquals(product, result);
        verify(mongoTemplate).save(product);
    }

    @Test
    void shouldFindAllProducts() {
        Product product1 = Product.create("Apple", "", Money.of(new BigDecimal("1.50")), ProductCategory.GROCERY);
        Product product2 = Product.create("Phone", "", Money.of(new BigDecimal("500.00")), ProductCategory.ELECTRONICS);
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        
        when(mongoTemplate.findAll(Product.class)).thenReturn(expectedProducts);
        
        List<Product> result = productRepository.findAll();
        
        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(mongoTemplate).findAll(Product.class);
    }

    @Test
    void shouldExistsByIdForProduct() {
        String productId = "existing456";
        
        when(mongoTemplate.exists(any(Query.class), eq(Product.class))).thenReturn(true);
        
        boolean exists = productRepository.existsById(productId);
        
        assertTrue(exists);
        verify(mongoTemplate).exists(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldDeleteProductById() {
        String productId = "delete456";
        
        productRepository.deleteById(productId);
        
        verify(mongoTemplate).remove(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldHandleNullIdInFindById() {
        Optional<Customer> customerResult = customerRepository.findById(null);
        Optional<Product> productResult = productRepository.findById(null);
        
        assertFalse(customerResult.isPresent());
        assertFalse(productResult.isPresent());
    }

    @Test
    void shouldHandleEmptyIdInFindById() {
        when(mongoTemplate.findById("", Customer.class)).thenReturn(null);
        when(mongoTemplate.findById("", Product.class)).thenReturn(null);
        
        Optional<Customer> customerResult = customerRepository.findById("");
        Optional<Product> productResult = productRepository.findById("");
        
        assertFalse(customerResult.isPresent());
        assertFalse(productResult.isPresent());
    }

    @Test
    void shouldHandleMongoTemplateExceptions() {
        String customerId = "error123";
        
        when(mongoTemplate.findById(customerId, Customer.class))
                .thenThrow(new RuntimeException("MongoDB connection error"));
        
        assertThrows(RuntimeException.class, () -> {
            customerRepository.findById(customerId);
        });
    }
}