package sa.billing.discounts.infrastructure.persistence.mongodb;

import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.bill.BillStatus;
import sa.billing.discounts.infrastructure.persistence.repository.BillRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BillRepositoryImpl implements BillRepository {
    
    private final MongoTemplate mongoTemplate;
    
    public BillRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Bill save(Bill bill) {
        return mongoTemplate.save(bill);
    }
    
    @Override
    public Optional<Bill> findById(String id) {
        Bill bill = mongoTemplate.findById(id, Bill.class);
        return Optional.ofNullable(bill);
    }
    
    @Override
    public List<Bill> findByCustomerId(String customerId) {
        Query query = new Query(Criteria.where("customerId").is(customerId));
        return mongoTemplate.find(query, Bill.class);
    }
    
    @Override
    public List<Bill> findByStatus(BillStatus status) {
        Query query = new Query(Criteria.where("status").is(status));
        return mongoTemplate.find(query, Bill.class);
    }
    
    @Override
    public List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        Query query = new Query(Criteria.where("createdAt").gte(start).lte(end));
        return mongoTemplate.find(query, Bill.class);
    }
    
    @Override
    public List<Bill> findByCustomerIdAndStatus(String customerId, BillStatus status) {
        Query query = new Query(Criteria.where("customerId").is(customerId).and("status").is(status));
        return mongoTemplate.find(query, Bill.class);
    }
    
    @Override
    public List<Bill> findAll() {
        return mongoTemplate.findAll(Bill.class);
    }
    
    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Bill.class);
    }
    
    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Bill.class);
    }
    
    @Override
    public long count() {
        return mongoTemplate.count(new Query(), Bill.class);
    }
    
    @Override
    public long countByStatus(BillStatus status) {
        Query query = new Query(Criteria.where("status").is(status));
        return mongoTemplate.count(query, Bill.class);
    }
    
    @Override
    public long countByCustomerId(String customerId) {
        Query query = new Query(Criteria.where("customerId").is(customerId));
        return mongoTemplate.count(query, Bill.class);
    }
}