package sa.billing.discounts.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BillCalculationResponse {
    private String customerId;
    private List<BillItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal percentageBasedDiscount;
    private String percentageDiscountType;
    private BigDecimal billBasedDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal netAmount;
    private LocalDateTime calculatedAt;
    
    public BillCalculationResponse() {
        this.calculatedAt = LocalDateTime.now();
    }
    
    public BillCalculationResponse(String customerId, List<BillItemResponse> items, 
                                 BigDecimal subtotal, BigDecimal percentageBasedDiscount, 
                                 String percentageDiscountType, BigDecimal billBasedDiscount, 
                                 BigDecimal totalDiscount, BigDecimal netAmount) {
        this.customerId = customerId;
        this.items = items;
        this.subtotal = subtotal;
        this.percentageBasedDiscount = percentageBasedDiscount;
        this.percentageDiscountType = percentageDiscountType;
        this.billBasedDiscount = billBasedDiscount;
        this.totalDiscount = totalDiscount;
        this.netAmount = netAmount;
        this.calculatedAt = LocalDateTime.now();
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<BillItemResponse> getItems() {
        return items;
    }
    
    public void setItems(List<BillItemResponse> items) {
        this.items = items;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getPercentageBasedDiscount() {
        return percentageBasedDiscount;
    }
    
    public void setPercentageBasedDiscount(BigDecimal percentageBasedDiscount) {
        this.percentageBasedDiscount = percentageBasedDiscount;
    }
    
    public String getPercentageDiscountType() {
        return percentageDiscountType;
    }
    
    public void setPercentageDiscountType(String percentageDiscountType) {
        this.percentageDiscountType = percentageDiscountType;
    }
    
    public BigDecimal getBillBasedDiscount() {
        return billBasedDiscount;
    }
    
    public void setBillBasedDiscount(BigDecimal billBasedDiscount) {
        this.billBasedDiscount = billBasedDiscount;
    }
    
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }
    
    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
    
    public BigDecimal getNetAmount() {
        return netAmount;
    }
    
    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillCalculationResponse that = (BillCalculationResponse) o;
        return Objects.equals(customerId, that.customerId) && 
               Objects.equals(items, that.items) && 
               Objects.equals(subtotal, that.subtotal) && 
               Objects.equals(percentageBasedDiscount, that.percentageBasedDiscount) && 
               Objects.equals(percentageDiscountType, that.percentageDiscountType) && 
               Objects.equals(billBasedDiscount, that.billBasedDiscount) && 
               Objects.equals(totalDiscount, that.totalDiscount) && 
               Objects.equals(netAmount, that.netAmount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, items, subtotal, percentageBasedDiscount, 
                           percentageDiscountType, billBasedDiscount, totalDiscount, netAmount);
    }
    
    @Override
    public String toString() {
        return "BillCalculationResponse{" +
                "customerId='" + customerId + '\'' +
                ", subtotal=" + subtotal +
                ", percentageBasedDiscount=" + percentageBasedDiscount +
                ", percentageDiscountType='" + percentageDiscountType + '\'' +
                ", billBasedDiscount=" + billBasedDiscount +
                ", totalDiscount=" + totalDiscount +
                ", netAmount=" + netAmount +
                ", calculatedAt=" + calculatedAt +
                '}';
    }
}