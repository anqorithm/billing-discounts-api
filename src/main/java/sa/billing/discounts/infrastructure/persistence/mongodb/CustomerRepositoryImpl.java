package sa.billing.discounts.infrastructure.persistence.mongodb;

import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.customer.CustomerType;
import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    
    private final MongoTemplate mongoTemplate;
    
    public CustomerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Customer save(Customer customer) {
        return mongoTemplate.save(customer);
    }
    
    @Override
    public Optional<Customer> findById(String id) {
        Customer customer = mongoTemplate.findById(id, Customer.class);
        return Optional.ofNullable(customer);
    }
    
    @Override
    public Optional<Customer> findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        Customer customer = mongoTemplate.findOne(query, Customer.class);
        return Optional.ofNullable(customer);
    }
    
    @Override
    public List<Customer> findByType(CustomerType type) {
        Query query = new Query(Criteria.where("type").is(type));
        return mongoTemplate.find(query, Customer.class);
    }
    
    @Override
    public List<Customer> findAll() {
        return mongoTemplate.findAll(Customer.class);
    }
    
    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Customer.class);
    }
    
    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Customer.class);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.exists(query, Customer.class);
    }
    
    @Override
    public long count() {
        return mongoTemplate.count(new Query(), Customer.class);
    }
}