package sa.billing.discounts.presentation.controller.v1;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;
import sa.billing.discounts.application.service.BillCalculationService;
import sa.billing.discounts.presentation.api.BillApi;
import sa.billing.discounts.presentation.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/bills")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BillController implements BillApi {
    
    private static final Logger logger = LoggerFactory.getLogger(BillController.class);
    private final BillCalculationService billCalculationService;
    
    public BillController(BillCalculationService billCalculationService) {
        this.billCalculationService = billCalculationService;
    }
    
    @PostMapping("/calculate")
    @Override
    public ResponseEntity<ApiResponse<BillCalculationResponse>> calculateBill(
            @Valid @RequestBody BillCalculationRequest request) {
        
        logger.info("Processing bill calculation request for customer: {}", request.getCustomerId());
        logger.debug("Bill items count: {}", request.getItems().size());
        
        try {
            BillCalculationResponse response = billCalculationService.calculateBillDiscount(request);
            logger.info("Bill calculation completed successfully for customer: {} | Total: ${}", 
                       request.getCustomerId(), response.getNetAmount());
            return ResponseEntity.ok(
                ApiResponse.success("Bill calculated successfully", response)
            );
        } catch (Exception e) {
            logger.error("Failed to calculate bill for customer: {} | Error: {}", 
                        request.getCustomerId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to calculate bill: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    @Override
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        logger.info("Health check requested - Service is healthy");
        return ResponseEntity.ok(
            ApiResponse.success("Bill service is running", "OK")
        );
    }
}