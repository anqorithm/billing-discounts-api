package sa.billing.discounts.infrastructure.persistence.repository;

import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.bill.BillStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillRepository {
    
    Bill save(Bill bill);
    
    Optional<Bill> findById(String id);
    
    List<Bill> findByCustomerId(String customerId);
    
    List<Bill> findByStatus(BillStatus status);
    
    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<Bill> findByCustomerIdAndStatus(String customerId, BillStatus status);
    
    List<Bill> findAll();
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    long countByStatus(BillStatus status);
    
    long countByCustomerId(String customerId);
}