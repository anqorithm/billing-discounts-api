package sa.billing.discounts.infrastructure.persistence.repository;

import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    
    Customer save(Customer customer);
    
    Optional<Customer> findById(String id);
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByType(CustomerType type);
    
    List<Customer> findAll();
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    boolean existsByEmail(String email);
    
    long count();
}