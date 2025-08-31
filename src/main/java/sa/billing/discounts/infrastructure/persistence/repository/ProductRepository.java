package sa.billing.discounts.infrastructure.persistence.repository;

import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    
    Product save(Product product);
    
    Optional<Product> findById(String id);
    
    Optional<Product> findByName(String name);
    
    List<Product> findByCategory(ProductCategory category);
    
    List<Product> findAll();
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    boolean existsByName(String name);
    
    long count();
    
    List<Product> findByNameContainingIgnoreCase(String name);
}