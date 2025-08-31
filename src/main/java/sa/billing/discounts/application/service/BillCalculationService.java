package sa.billing.discounts.application.service;

import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.dto.BillItemRequest;
import sa.billing.discounts.application.dto.BillItemResponse;
import sa.billing.discounts.domain.model.bill.Bill;
import sa.billing.discounts.domain.model.bill.BillItem;
import sa.billing.discounts.domain.model.customer.Customer;
import sa.billing.discounts.domain.model.product.Product;
import sa.billing.discounts.domain.model.valueobject.Money;
import sa.billing.discounts.domain.exception.CustomerNotFoundException;
import sa.billing.discounts.domain.exception.ProductNotFoundException;
import sa.billing.discounts.infrastructure.persistence.repository.CustomerRepository;
import sa.billing.discounts.infrastructure.persistence.repository.ProductRepository;
import sa.billing.discounts.domain.model.bill.BillBasedDiscount;
import sa.billing.discounts.domain.model.discount.AffiliateDiscount;
import sa.billing.discounts.domain.model.discount.Discount;
import sa.billing.discounts.domain.model.discount.EmployeeDiscount;
import sa.billing.discounts.domain.model.discount.LoyaltyDiscount;
import sa.billing.discounts.application.config.DiscountConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillCalculationService implements BillCalculationInterface {
    
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final List<Discount> availableDiscounts;
    
    public BillCalculationService(CustomerRepository customerRepository,
                                 ProductRepository productRepository,
                                 DiscountConfig discountConfig) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.availableDiscounts = List.of(
            new EmployeeDiscount(discountConfig.getEmployeePercentage()),
            new AffiliateDiscount(discountConfig.getAffiliatePercentage()), 
            new LoyaltyDiscount(discountConfig.getLoyaltyPercentage()),
            new BillBasedDiscount(discountConfig.getBillThreshold(), discountConfig.getBillDiscountAmount())
        );
    }
    
    @Override
    public BillCalculationResponse calculateBillDiscount(BillCalculationRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        List<BillItem> billItems = createBillItems(request.getItems());
        Bill bill = Bill.create(customer.getId(), billItems);
        
        Money totalDiscount = calculateTotalDiscount(bill, customer);
        
        return mapToResponse(request.getCustomerId(), billItems, bill, totalDiscount);
    }
    
    private Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }
    
    private List<BillItem> createBillItems(List<BillItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::createBillItem)
                .collect(Collectors.toList());
    }
    
    private BillItem createBillItem(BillItemRequest request) {
        Product product = findProductById(request.getProductId());
        return BillItem.create(product, request.getQuantity());
    }
    
    private Product findProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }
    
    private Money calculateTotalDiscount(Bill bill, Customer customer) {
        return availableDiscounts.stream()
            .filter(discount -> discount.isApplicable(bill, customer))
            .map(discount -> discount.calculateDiscount(bill, customer))
            .reduce(Money.zero(), Money::add);
    }
    
    private BillCalculationResponse mapToResponse(String customerId, List<BillItem> billItems, 
                                                 Bill bill, Money totalDiscount) {
        List<BillItemResponse> itemResponses = billItems.stream()
                .map(this::mapBillItemToResponse)
                .collect(Collectors.toList());
        
        return new BillCalculationResponse(
                customerId,
                itemResponses,
                bill.getSubtotal().getAmount(),
                totalDiscount.getAmount(),
                "MIXED",
                Money.zero().getAmount(),
                totalDiscount.getAmount(),
                bill.getSubtotal().subtract(totalDiscount).getAmount()
        );
    }
    
    private BillItemResponse mapBillItemToResponse(BillItem billItem) {
        return new BillItemResponse(
                billItem.getProduct().getId(),
                billItem.getProduct().getName(),
                billItem.getProduct().getCategory().name(),
                billItem.getQuantity(),
                billItem.getUnitPrice().getAmount(),
                billItem.getTotalPrice().getAmount(),
                !billItem.isGrocery()
        );
    }
}