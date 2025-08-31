package sa.billing.discounts.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class BillItemRequest {
    
    @NotNull(message = "Product ID cannot be null")
    private String productId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    private BigDecimal unitPrice;
    
    public BillItemRequest() {
    }
    
    public BillItemRequest(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public BillItemRequest(String productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItemRequest that = (BillItemRequest) o;
        return quantity == that.quantity && Objects.equals(productId, that.productId) && Objects.equals(unitPrice, that.unitPrice);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, unitPrice);
    }
    
    @Override
    public String toString() {
        return "BillItemRequest{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}