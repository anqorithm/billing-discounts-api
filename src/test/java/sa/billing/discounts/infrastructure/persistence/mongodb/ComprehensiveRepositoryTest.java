package sa.billing.discounts.infrastructure.persistence.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComprehensiveRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private Customer customer;

    @Mock
    private Product product;

    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @Test
    void shouldSaveCustomer() {
        when(mongoTemplate.save(customer)).thenReturn(customer);
        
        Customer result = customerRepository.save(customer);
        
        assertEquals(customer, result);
        verify(mongoTemplate).save(customer);
    }

    @Test
    void shouldFindCustomerById() {
        String customerId = "customer-123";
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(customer);
        
        Optional<Customer> result = customerRepository.findById(customerId);
        
        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(mongoTemplate).findById(customerId, Customer.class);
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFoundById() {
        String customerId = "customer-123";
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(null);
        
        Optional<Customer> result = customerRepository.findById(customerId);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findById(customerId, Customer.class);
    }

    @Test
    void shouldFindCustomerByEmail() {
        String email = "test@example.com";
        Query expectedQuery = new Query(Criteria.where("email").is(email));
        when(mongoTemplate.findOne(any(Query.class), eq(Customer.class))).thenReturn(customer);
        
        Optional<Customer> result = customerRepository.findByEmail(email);
        
        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(mongoTemplate).findOne(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFoundByEmail() {
        String email = "test@example.com";
        when(mongoTemplate.findOne(any(Query.class), eq(Customer.class))).thenReturn(null);
        
        Optional<Customer> result = customerRepository.findByEmail(email);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findOne(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldFindCustomersByType() {
        CustomerType type = CustomerType.AFFILIATE;
        List<Customer> customers = List.of(customer);
        when(mongoTemplate.find(any(Query.class), eq(Customer.class))).thenReturn(customers);
        
        List<Customer> result = customerRepository.findByType(type);
        
        assertEquals(customers, result);
        verify(mongoTemplate).find(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldFindAllCustomers() {
        List<Customer> customers = List.of(customer);
        when(mongoTemplate.findAll(Customer.class)).thenReturn(customers);
        
        List<Customer> result = customerRepository.findAll();
        
        assertEquals(customers, result);
        verify(mongoTemplate).findAll(Customer.class);
    }

    @Test
    void shouldDeleteCustomerById() {
        String customerId = "customer-123";
        
        customerRepository.deleteById(customerId);
        
        verify(mongoTemplate).remove(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldCheckIfCustomerExistsById() {
        String customerId = "customer-123";
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(true);
        
        boolean result = customerRepository.existsById(customerId);
        
        assertTrue(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldCheckIfCustomerNotExistsById() {
        String customerId = "customer-123";
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(false);
        
        boolean result = customerRepository.existsById(customerId);
        
        assertFalse(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldCheckIfCustomerExistsByEmail() {
        String email = "test@example.com";
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(true);
        
        boolean result = customerRepository.existsByEmail(email);
        
        assertTrue(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldCheckIfCustomerNotExistsByEmail() {
        String email = "test@example.com";
        when(mongoTemplate.exists(any(Query.class), eq(Customer.class))).thenReturn(false);
        
        boolean result = customerRepository.existsByEmail(email);
        
        assertFalse(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldCountCustomers() {
        long expectedCount = 5L;
        when(mongoTemplate.count(any(Query.class), eq(Customer.class))).thenReturn(expectedCount);
        
        long result = customerRepository.count();
        
        assertEquals(expectedCount, result);
        verify(mongoTemplate).count(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldSaveProduct() {
        when(mongoTemplate.save(product)).thenReturn(product);
        
        Product result = productRepository.save(product);
        
        assertEquals(product, result);
        verify(mongoTemplate).save(product);
    }

    @Test
    void shouldFindProductById() {
        String productId = "product-123";
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(product);
        
        Optional<Product> result = productRepository.findById(productId);
        
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(mongoTemplate).findById(productId, Product.class);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFoundById() {
        String productId = "product-123";
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(null);
        
        Optional<Product> result = productRepository.findById(productId);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findById(productId, Product.class);
    }

    @Test
    void shouldFindProductByName() {
        String name = "Test Product";
        when(mongoTemplate.findOne(any(Query.class), eq(Product.class))).thenReturn(product);
        
        Optional<Product> result = productRepository.findByName(name);
        
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(mongoTemplate).findOne(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldReturnEmptyWhenProductNotFoundByName() {
        String name = "Test Product";
        when(mongoTemplate.findOne(any(Query.class), eq(Product.class))).thenReturn(null);
        
        Optional<Product> result = productRepository.findByName(name);
        
        assertFalse(result.isPresent());
        verify(mongoTemplate).findOne(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldFindProductsByCategory() {
        ProductCategory category = ProductCategory.ELECTRONICS;
        List<Product> products = List.of(product);
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(products);
        
        List<Product> result = productRepository.findByCategory(category);
        
        assertEquals(products, result);
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldFindAllProducts() {
        List<Product> products = List.of(product);
        when(mongoTemplate.findAll(Product.class)).thenReturn(products);
        
        List<Product> result = productRepository.findAll();
        
        assertEquals(products, result);
        verify(mongoTemplate).findAll(Product.class);
    }

    @Test
    void shouldDeleteProductById() {
        String productId = "product-123";
        
        productRepository.deleteById(productId);
        
        verify(mongoTemplate).remove(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldCheckIfProductExistsById() {
        String productId = "product-123";
        when(mongoTemplate.exists(any(Query.class), eq(Product.class))).thenReturn(true);
        
        boolean result = productRepository.existsById(productId);
        
        assertTrue(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldCheckIfProductNotExistsById() {
        String productId = "product-123";
        when(mongoTemplate.exists(any(Query.class), eq(Product.class))).thenReturn(false);
        
        boolean result = productRepository.existsById(productId);
        
        assertFalse(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldCheckIfProductExistsByName() {
        String name = "Test Product";
        when(mongoTemplate.exists(any(Query.class), eq(Product.class))).thenReturn(true);
        
        boolean result = productRepository.existsByName(name);
        
        assertTrue(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldCheckIfProductNotExistsByName() {
        String name = "Test Product";
        when(mongoTemplate.exists(any(Query.class), eq(Product.class))).thenReturn(false);
        
        boolean result = productRepository.existsByName(name);
        
        assertFalse(result);
        verify(mongoTemplate).exists(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldCountProducts() {
        long expectedCount = 10L;
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(expectedCount);
        
        long result = productRepository.count();
        
        assertEquals(expectedCount, result);
        verify(mongoTemplate).count(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldFindProductsByNameContainingIgnoreCase() {
        String searchName = "test";
        List<Product> products = List.of(product);
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(products);
        
        List<Product> result = productRepository.findByNameContainingIgnoreCase(searchName);
        
        assertEquals(products, result);
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldFindProductsByNameContainingIgnoreCaseWithEmptyResult() {
        String searchName = "nonexistent";
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(Collections.emptyList());
        
        List<Product> result = productRepository.findByNameContainingIgnoreCase(searchName);
        
        assertTrue(result.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldHandleNullResponsesInRepositoryMethods() {
        when(mongoTemplate.findAll(Customer.class)).thenReturn(null);
        
        List<Customer> result = customerRepository.findAll();
        
        assertNull(result);
        verify(mongoTemplate).findAll(Customer.class);
    }

    @Test
    void shouldHandleEmptyListsInRepositoryMethods() {
        when(mongoTemplate.find(any(Query.class), eq(Customer.class))).thenReturn(Collections.emptyList());
        
        List<Customer> result = customerRepository.findByType(CustomerType.REGULAR);
        
        assertTrue(result.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(Customer.class));
    }

    @Test
    void shouldTestRepositoryConstructors() {
        CustomerRepositoryImpl customerRepo = new CustomerRepositoryImpl(mongoTemplate);
        ProductRepositoryImpl productRepo = new ProductRepositoryImpl(mongoTemplate);
        
        assertNotNull(customerRepo);
        assertNotNull(productRepo);
    }

    @Test
    void shouldTestMultipleCustomerTypes() {
        for (CustomerType type : CustomerType.values()) {
            when(mongoTemplate.find(any(Query.class), eq(Customer.class))).thenReturn(List.of(customer));
            
            List<Customer> result = customerRepository.findByType(type);
            
            assertFalse(result.isEmpty());
        }
    }

    @Test
    void shouldTestMultipleProductCategories() {
        for (ProductCategory category : ProductCategory.values()) {
            when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(List.of(product));
            
            List<Product> result = productRepository.findByCategory(category);
            
            assertFalse(result.isEmpty());
        }
    }

    @Test
    void shouldTestSearchWithSpecialCharacters() {
        String searchName = "test@#$%";
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(Collections.emptyList());
        
        List<Product> result = productRepository.findByNameContainingIgnoreCase(searchName);
        
        assertTrue(result.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }

    @Test
    void shouldTestSearchWithEmptyString() {
        String searchName = "";
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(List.of(product));
        
        List<Product> result = productRepository.findByNameContainingIgnoreCase(searchName);
        
        assertFalse(result.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }
}