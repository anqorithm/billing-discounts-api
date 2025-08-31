package sa.billing.discounts.infrastructure.persistence.mongodb;

import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.product.ProductCategory;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final MongoTemplate mongoTemplate;
    
    public ProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Product save(Product product) {
        return mongoTemplate.save(product);
    }
    
    @Override
    public Optional<Product> findById(String id) {
        Product product = mongoTemplate.findById(id, Product.class);
        return Optional.ofNullable(product);
    }
    
    @Override
    public Optional<Product> findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Product product = mongoTemplate.findOne(query, Product.class);
        return Optional.ofNullable(product);
    }
    
    @Override
    public List<Product> findByCategory(ProductCategory category) {
        Query query = new Query(Criteria.where("category").is(category));
        return mongoTemplate.find(query, Product.class);
    }
    
    @Override
    public List<Product> findAll() {
        return mongoTemplate.findAll(Product.class);
    }
    
    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Product.class);
    }
    
    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Product.class);
    }
    
    @Override
    public boolean existsByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.exists(query, Product.class);
    }
    
    @Override
    public long count() {
        return mongoTemplate.count(new Query(), Product.class);
    }
    
    @Override
    public List<Product> findByNameContainingIgnoreCase(String name) {
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        return mongoTemplate.find(query, Product.class);
    }
}