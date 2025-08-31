package sa.billing.discounts.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "discount")
public class DiscountConfig {
    
    private BigDecimal employeePercentage = new BigDecimal("30");
    private BigDecimal affiliatePercentage = new BigDecimal("10");
    private BigDecimal loyaltyPercentage = new BigDecimal("5");
    private BigDecimal billThreshold = new BigDecimal("100");
    private BigDecimal billDiscountAmount = new BigDecimal("5");
    
    public BigDecimal getEmployeePercentage() {
        return employeePercentage;
    }
    
    public void setEmployeePercentage(BigDecimal employeePercentage) {
        this.employeePercentage = employeePercentage;
    }
    
    public BigDecimal getAffiliatePercentage() {
        return affiliatePercentage;
    }
    
    public void setAffiliatePercentage(BigDecimal affiliatePercentage) {
        this.affiliatePercentage = affiliatePercentage;
    }
    
    public BigDecimal getLoyaltyPercentage() {
        return loyaltyPercentage;
    }
    
    public void setLoyaltyPercentage(BigDecimal loyaltyPercentage) {
        this.loyaltyPercentage = loyaltyPercentage;
    }
    
    public BigDecimal getBillThreshold() {
        return billThreshold;
    }
    
    public void setBillThreshold(BigDecimal billThreshold) {
        this.billThreshold = billThreshold;
    }
    
    public BigDecimal getBillDiscountAmount() {
        return billDiscountAmount;
    }
    
    public void setBillDiscountAmount(BigDecimal billDiscountAmount) {
        this.billDiscountAmount = billDiscountAmount;
    }
}