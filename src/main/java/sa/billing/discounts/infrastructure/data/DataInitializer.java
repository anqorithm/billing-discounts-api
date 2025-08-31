package sa.billing.discounts.infrastructure.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner, DataInitializationService {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;

  public DataInitializer(CustomerRepository customerRepository, 
                        ProductRepository productRepository) {
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    initializeSampleData();
  }

  @Override
  public void initializeSampleData() {
    logger.info("starting sample data initialization...");

    initializeCustomers();
    initializeProducts();

    logger.info("sample data initialization completed successfully!");
  }

  private void initializeCustomers() {
    if (customerRepository.findById(SampleData.CUSTOMER_EMPLOYEE_ID).isPresent()) {
      logger.info("sample customers already exist, skipping customer initialization");
      return;
    }

    Customer employee = Customer.createEmployee(
        SampleData.CUSTOMER_EMPLOYEE_NAME,
        SampleData.CUSTOMER_EMPLOYEE_EMAIL,
        LocalDateTime.now().minusYears(1)
    );
    setCustomerId(employee, SampleData.CUSTOMER_EMPLOYEE_ID);

    Customer affiliate = Customer.createAffiliate(
        SampleData.CUSTOMER_AFFILIATE_NAME,
        SampleData.CUSTOMER_AFFILIATE_EMAIL,
        LocalDateTime.now().minusYears(2)
    );
    setCustomerId(affiliate, SampleData.CUSTOMER_AFFILIATE_ID);

    Customer loyalCustomer = Customer.createRegular(
        SampleData.CUSTOMER_LOYAL_NAME,
        SampleData.CUSTOMER_LOYAL_EMAIL,
        LocalDateTime.now().minusYears(3)
    );
    setCustomerId(loyalCustomer, SampleData.CUSTOMER_LOYAL_ID);

    Customer regularCustomer = Customer.createRegular(
        SampleData.CUSTOMER_REGULAR_NAME,
        SampleData.CUSTOMER_REGULAR_EMAIL,
        LocalDateTime.now().minusMonths(6)
    );
    setCustomerId(regularCustomer, SampleData.CUSTOMER_REGULAR_ID);

    customerRepository.save(employee);
    customerRepository.save(affiliate);
    customerRepository.save(loyalCustomer);
    customerRepository.save(regularCustomer);

    logger.info("created {} sample customers successfully", 4);
  }

  private void initializeProducts() {
    if (productRepository.findById(SampleData.PRODUCT_LAPTOP_ID).isPresent()) {
      logger.info("sample products already exist, skipping product initialization");
      return;
    }

    Product laptop = Product.create(
        SampleData.PRODUCT_LAPTOP_NAME,
        SampleData.PRODUCT_LAPTOP_DESCRIPTION,
        Money.of(new BigDecimal(SampleData.PRODUCT_LAPTOP_PRICE)),
        ProductCategory.ELECTRONICS
    );
    setProductId(laptop, SampleData.PRODUCT_LAPTOP_ID);

    Product apple = Product.create(
        SampleData.PRODUCT_APPLE_NAME,
        SampleData.PRODUCT_APPLE_DESCRIPTION,
        Money.of(new BigDecimal(SampleData.PRODUCT_APPLE_PRICE)),
        ProductCategory.GROCERY
    );
    setProductId(apple, SampleData.PRODUCT_APPLE_ID);

    Product shirt = Product.create(
        SampleData.PRODUCT_SHIRT_NAME,
        SampleData.PRODUCT_SHIRT_DESCRIPTION,
        Money.of(new BigDecimal(SampleData.PRODUCT_SHIRT_PRICE)),
        ProductCategory.CLOTHING
    );
    setProductId(shirt, SampleData.PRODUCT_SHIRT_ID);

    Product book = Product.create(
        SampleData.PRODUCT_BOOK_NAME,
        SampleData.PRODUCT_BOOK_DESCRIPTION,
        Money.of(new BigDecimal(SampleData.PRODUCT_BOOK_PRICE)),
        ProductCategory.BOOKS
    );
    setProductId(book, SampleData.PRODUCT_BOOK_ID);

    productRepository.save(laptop);
    productRepository.save(apple);
    productRepository.save(shirt);
    productRepository.save(book);

    logger.info("created {} sample products successfully", 4);
  }

  private void setCustomerId(Customer customer, String id) {
    try {
      var idField = Customer.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(customer, id);
    } catch (Exception e) {
      logger.warn("could not set customer id: {}", e.getMessage());
    }
  }

  private void setProductId(Product product, String id) {
    try {
      var idField = Product.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(product, id);
    } catch (Exception e) {
      logger.warn("could not set product id: {}", e.getMessage());
    }
  }

  private static final class SampleData {
    static final String CUSTOMER_EMPLOYEE_ID = "65a1b2c3d4e5f6a7b8c9d0e1";
    static final String CUSTOMER_AFFILIATE_ID = "65a1b2c3d4e5f6a7b8c9d0e2";
    static final String CUSTOMER_LOYAL_ID = "65a1b2c3d4e5f6a7b8c9d0e3";
    static final String CUSTOMER_REGULAR_ID = "65a1b2c3d4e5f6a7b8c9d0e4";

    static final String CUSTOMER_EMPLOYEE_NAME = "abdullah alqahtani";
    static final String CUSTOMER_EMPLOYEE_EMAIL = "anqorithm@protonmail.com";
    static final String CUSTOMER_AFFILIATE_NAME = "sarah almutairi";
    static final String CUSTOMER_AFFILIATE_EMAIL = "sarah.almutairi@partner.com";
    static final String CUSTOMER_LOYAL_NAME = "mohammed alghamdi";
    static final String CUSTOMER_LOYAL_EMAIL = "mohammed.alghamdi@customer.com";
    static final String CUSTOMER_REGULAR_NAME = "fatima alharbi";
    static final String CUSTOMER_REGULAR_EMAIL = "fatima.alharbi@customer.com";

    static final String PRODUCT_LAPTOP_ID = "65a1b2c3d4e5f6a7b8c9d0f1";
    static final String PRODUCT_APPLE_ID = "65a1b2c3d4e5f6a7b8c9d0f2";
    static final String PRODUCT_SHIRT_ID = "65a1b2c3d4e5f6a7b8c9d0f3";
    static final String PRODUCT_BOOK_ID = "65a1b2c3d4e5f6a7b8c9d0f4";

    static final String PRODUCT_LAPTOP_NAME = "gaming laptop";
    static final String PRODUCT_LAPTOP_DESCRIPTION = "high-performance gaming laptop";
    static final String PRODUCT_LAPTOP_PRICE = "1500.00";

    static final String PRODUCT_APPLE_NAME = "fresh apples";
    static final String PRODUCT_APPLE_DESCRIPTION = "organic red apples per kg";
    static final String PRODUCT_APPLE_PRICE = "5.99";

    static final String PRODUCT_SHIRT_NAME = "cotton t-shirt";
    static final String PRODUCT_SHIRT_DESCRIPTION = "100% cotton casual t-shirt";
    static final String PRODUCT_SHIRT_PRICE = "25.00";

    static final String PRODUCT_BOOK_NAME = "programming book";
    static final String PRODUCT_BOOK_DESCRIPTION = "learn java programming";
    static final String PRODUCT_BOOK_PRICE = "45.00";

    private SampleData() {
    }
  }
}