package sa.billing.discounts.domain.model.customer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import sa.billing.discounts.domain.model.customer.CustomerType;
import java.util.Objects;

@Document(collection = "customers")
public class Customer {
    
    @Id
    private String id;
    private String name;
    private String email;
    private CustomerType type;
    private LocalDateTime registrationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected Customer() {
    }
    
    private Customer(String name, String email, CustomerType type, LocalDateTime registrationDate) {
        this.name = Objects.requireNonNull(name, "Customer name cannot be null");
        this.email = Objects.requireNonNull(email, "Customer email cannot be null");
        this.type = Objects.requireNonNull(type, "Customer type cannot be null");
        this.registrationDate = Objects.requireNonNull(registrationDate, "Registration date cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public static Customer createEmployee(String name, String email, LocalDateTime registrationDate) {
        return new Customer(name, email, CustomerType.EMPLOYEE, registrationDate);
    }
    
    public static Customer createAffiliate(String name, String email, LocalDateTime registrationDate) {
        return new Customer(name, email, CustomerType.AFFILIATE, registrationDate);
    }
    
    public static Customer createRegular(String name, String email, LocalDateTime registrationDate) {
        return new Customer(name, email, CustomerType.REGULAR, registrationDate);
    }
    
    public boolean isEmployee() {
        return type == CustomerType.EMPLOYEE;
    }
    
    public boolean isAffiliate() {
        return type == CustomerType.AFFILIATE;
    }
    
    public boolean isLoyalCustomer(LocalDateTime currentDate) {
        return type == CustomerType.REGULAR && 
               registrationDate.isBefore(currentDate.minusYears(2));
    }
    
    public void updateType(CustomerType newType) {
        this.type = Objects.requireNonNull(newType, "Customer type cannot be null");
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public CustomerType getType() {
        return type;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", registrationDate=" + registrationDate +
                '}';
    }
}