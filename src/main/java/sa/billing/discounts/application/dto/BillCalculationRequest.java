package sa.billing.discounts.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class BillCalculationRequest {
    
    @NotNull(message = "Customer ID cannot be null")
    private String customerId;
    
    @NotEmpty(message = "Bill items cannot be empty")
    @Valid
    private List<BillItemRequest> items;
    
    public BillCalculationRequest() {
    }
    
    public BillCalculationRequest(String customerId, List<BillItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<BillItemRequest> getItems() {
        return items;
    }
    
    public void setItems(List<BillItemRequest> items) {
        this.items = items;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillCalculationRequest that = (BillCalculationRequest) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(items, that.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, items);
    }
    
    @Override
    public String toString() {
        return "BillCalculationRequest{" +
                "customerId='" + customerId + '\'' +
                ", items=" + items +
                '}';
    }
}