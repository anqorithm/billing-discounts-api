package sa.billing.discounts.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;
import sa.billing.discounts.infrastructure.persistence.repository.BillRepository;
import sa.billing.discounts.infrastructure.persistence.mongodb.CustomerRepositoryImpl;
import sa.billing.discounts.infrastructure.persistence.mongodb.ProductRepositoryImpl;
import sa.billing.discounts.infrastructure.persistence.mongodb.BillRepositoryImpl;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.bill.BillItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Repository Tests")
class RepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private BillRepository billRepository;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl(mongoTemplate);
        productRepository = new ProductRepositoryImpl(mongoTemplate);
        billRepository = new BillRepositoryImpl(mongoTemplate);
    }

    @Test
    @DisplayName("Should find customer by ID")
    void shouldFindCustomerById() {
        // Given
        String customerId = "customer123";
        Customer expectedCustomer = Customer.createEmployee("John Doe", "john@company.com", 
            LocalDateTime.now().minusYears(1));
        
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(expectedCustomer);
        
        // When
        Optional<Customer> result = customerRepository.findById(customerId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedCustomer, result.get());
        verify(mongoTemplate).findById(customerId, Customer.class);
    }

    @Test
    @DisplayName("Should return empty when customer not found")
    void shouldReturnEmptyWhenCustomerNotFound() {
        // Given
        String customerId = "nonexistent";
        when(mongoTemplate.findById(customerId, Customer.class)).thenReturn(null);
        
        // When
        Optional<Customer> result = customerRepository.findById(customerId);
        
        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save customer")
    void shouldSaveCustomer() {
        // Given
        Customer customer = Customer.createAffiliate("Jane Smith", "jane@partner.com", 
            LocalDateTime.now().minusMonths(6));
        when(mongoTemplate.save(customer)).thenReturn(customer);
        
        // When
        Customer result = customerRepository.save(customer);
        
        // Then
        assertEquals(customer, result);
        verify(mongoTemplate).save(customer);
    }

    @Test
    @DisplayName("Should find all customers")
    void shouldFindAllCustomers() {
        // Given
        List<Customer> expectedCustomers = List.of(
            Customer.createEmployee("John", "john@company.com", LocalDateTime.now()),
            Customer.createAffiliate("Jane", "jane@partner.com", LocalDateTime.now())
        );
        when(mongoTemplate.findAll(Customer.class)).thenReturn(expectedCustomers);
        
        // When
        List<Customer> result = customerRepository.findAll();
        
        // Then
        assertEquals(2, result.size());
        assertEquals(expectedCustomers, result);
        verify(mongoTemplate).findAll(Customer.class);
    }

    @Test
    @DisplayName("Should find product by ID")
    void shouldFindProductById() {
        // Given
        String productId = "product123";
        Product expectedProduct = Product.create("Laptop", "Gaming laptop", 
            Money.of("1200.00"), ProductCategory.ELECTRONICS);
        
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(expectedProduct);
        
        // When
        Optional<Product> result = productRepository.findById(productId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedProduct, result.get());
        verify(mongoTemplate).findById(productId, Product.class);
    }

    @Test
    @DisplayName("Should return empty when product not found")
    void shouldReturnEmptyWhenProductNotFound() {
        // Given
        String productId = "nonexistent";
        when(mongoTemplate.findById(productId, Product.class)).thenReturn(null);
        
        // When
        Optional<Product> result = productRepository.findById(productId);
        
        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save product")
    void shouldSaveProduct() {
        // Given
        Product product = Product.create("Smartphone", "iPhone", 
            Money.of("999.99"), ProductCategory.ELECTRONICS);
        when(mongoTemplate.save(product)).thenReturn(product);
        
        // When
        Product result = productRepository.save(product);
        
        // Then
        assertEquals(product, result);
        verify(mongoTemplate).save(product);
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAllProducts() {
        // Given
        List<Product> expectedProducts = List.of(
            Product.create("Laptop", "Gaming laptop", Money.of("1200.00"), ProductCategory.ELECTRONICS),
            Product.create("Bread", "Fresh bread", Money.of("2.50"), ProductCategory.GROCERY)
        );
        when(mongoTemplate.findAll(Product.class)).thenReturn(expectedProducts);
        
        // When
        List<Product> result = productRepository.findAll();
        
        // Then
        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(mongoTemplate).findAll(Product.class);
    }

    @Test
    @DisplayName("Should find products by category")
    void shouldFindProductsByCategory() {
        // Given
        ProductCategory category = ProductCategory.ELECTRONICS;
        List<Product> expectedProducts = List.of(
            Product.create("Laptop", "Gaming laptop", Money.of("1200.00"), ProductCategory.ELECTRONICS),
            Product.create("Phone", "Smartphone", Money.of("800.00"), ProductCategory.ELECTRONICS)
        );
        
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(expectedProducts);
        
        // When
        List<Product> result = productRepository.findByCategory(category);
        
        // Then
        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(mongoTemplate).find(any(Query.class), eq(Product.class));
    }

    @Test
    @DisplayName("Should save bill")
    void shouldSaveBill() {
        // Given
        String customerId = "customer123";
        Product product = Product.create("Laptop", "Gaming laptop", Money.of("1000.00"), ProductCategory.ELECTRONICS);
        BillItem billItem = BillItem.create(product, 1);
        Bill bill = Bill.create(customerId, List.of(billItem));
        
        when(mongoTemplate.save(bill)).thenReturn(bill);
        
        // When
        Bill result = billRepository.save(bill);
        
        // Then
        assertEquals(bill, result);
        verify(mongoTemplate).save(bill);
    }

    @Test
    @DisplayName("Should find bill by ID")
    void shouldFindBillById() {
        // Given
        String billId = "bill123";
        String customerId = "customer123";
        Product product = Product.create("Laptop", "Gaming laptop", Money.of("1000.00"), ProductCategory.ELECTRONICS);
        BillItem billItem = BillItem.create(product, 1);
        Bill expectedBill = Bill.create(customerId, List.of(billItem));
        
        when(mongoTemplate.findById(billId, Bill.class)).thenReturn(expectedBill);
        
        // When
        Optional<Bill> result = billRepository.findById(billId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedBill, result.get());
        verify(mongoTemplate).findById(billId, Bill.class);
    }

    @Test
    @DisplayName("Should find all bills")
    void shouldFindAllBills() {
        // Given
        String customerId1 = "customer1";
        String customerId2 = "customer2";
        Product product = Product.create("Laptop", "Gaming laptop", Money.of("1000.00"), ProductCategory.ELECTRONICS);
        
        BillItem billItem1 = BillItem.create(product, 1);
        BillItem billItem2 = BillItem.create(product, 2);
        
        List<Bill> expectedBills = List.of(
            Bill.create(customerId1, List.of(billItem1)),
            Bill.create(customerId2, List.of(billItem2))
        );
        
        when(mongoTemplate.findAll(Bill.class)).thenReturn(expectedBills);
        
        // When
        List<Bill> result = billRepository.findAll();
        
        // Then
        assertEquals(2, result.size());
        assertEquals(expectedBills, result);
        verify(mongoTemplate).findAll(Bill.class);
    }

    @Test
    @DisplayName("Should find bills by customer ID")
    void shouldFindBillsByCustomerId() {
        // Given
        String customerId = "customer123";
        Product product = Product.create("Laptop", "Gaming laptop", Money.of("1000.00"), ProductCategory.ELECTRONICS);
        BillItem billItem = BillItem.create(product, 1);
        
        List<Bill> expectedBills = List.of(Bill.create(customerId, List.of(billItem)));
        
        when(mongoTemplate.find(any(Query.class), eq(Bill.class))).thenReturn(expectedBills);
        
        // When
        List<Bill> result = billRepository.findByCustomerId(customerId);
        
        // Then
        assertEquals(1, result.size());
        assertEquals(expectedBills, result);
        verify(mongoTemplate).find(any(Query.class), eq(Bill.class));
    }

    @Test
    @DisplayName("Should handle repository exceptions gracefully")
    void shouldHandleRepositoryExceptionsGracefully() {
        // Given
        String customerId = "error123";
        when(mongoTemplate.findById(customerId, Customer.class))
            .thenThrow(new RuntimeException("Database connection failed"));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerRepository.findById(customerId);
        });
        
        assertEquals("Database connection failed", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle null ID parameters gracefully")
    void shouldHandleNullIdParametersGracefully() {
        // Given
        when(mongoTemplate.findById(null, Customer.class)).thenReturn(null);
        when(mongoTemplate.findById(null, Product.class)).thenReturn(null);
        when(mongoTemplate.findById(null, Bill.class)).thenReturn(null);
        
        // When & Then
        Optional<Customer> customerResult = customerRepository.findById(null);
        Optional<Product> productResult = productRepository.findById(null);
        Optional<Bill> billResult = billRepository.findById(null);
        
        assertFalse(customerResult.isPresent());
        assertFalse(productResult.isPresent());
        assertFalse(billResult.isPresent());
    }

    @Test
    @DisplayName("Should handle empty results from MongoDB")
    void shouldHandleEmptyResultsFromMongoDB() {
        // Given
        when(mongoTemplate.findAll(Customer.class)).thenReturn(List.of());
        when(mongoTemplate.findAll(Product.class)).thenReturn(List.of());
        when(mongoTemplate.findAll(Bill.class)).thenReturn(List.of());
        
        // When
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Bill> bills = billRepository.findAll();
        
        // Then
        assertTrue(customers.isEmpty());
        assertTrue(products.isEmpty());
        assertTrue(bills.isEmpty());
    }
}