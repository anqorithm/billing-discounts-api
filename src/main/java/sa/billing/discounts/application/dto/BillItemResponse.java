package sa.billing.discounts.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class BillItemResponse {
    private String productId;
    private String productName;
    private String category;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private boolean eligibleForPercentageDiscount;
    
    public BillItemResponse() {
    }
    
    public BillItemResponse(String productId, String productName, String category, 
                           int quantity, BigDecimal unitPrice, BigDecimal totalPrice, 
                           boolean eligibleForPercentageDiscount) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.eligibleForPercentageDiscount = eligibleForPercentageDiscount;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
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
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public boolean isEligibleForPercentageDiscount() {
        return eligibleForPercentageDiscount;
    }
    
    public void setEligibleForPercentageDiscount(boolean eligibleForPercentageDiscount) {
        this.eligibleForPercentageDiscount = eligibleForPercentageDiscount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItemResponse that = (BillItemResponse) o;
        return quantity == that.quantity && 
               eligibleForPercentageDiscount == that.eligibleForPercentageDiscount && 
               Objects.equals(productId, that.productId) && 
               Objects.equals(productName, that.productName) && 
               Objects.equals(category, that.category) && 
               Objects.equals(unitPrice, that.unitPrice) && 
               Objects.equals(totalPrice, that.totalPrice);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, category, quantity, unitPrice, totalPrice, eligibleForPercentageDiscount);
    }
    
    @Override
    public String toString() {
        return "BillItemResponse{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", eligibleForPercentageDiscount=" + eligibleForPercentageDiscount +
                '}';
    }
}