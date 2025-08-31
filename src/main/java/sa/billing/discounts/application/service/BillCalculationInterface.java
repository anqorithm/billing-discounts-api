package sa.billing.discounts.application.service;

import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;

public interface BillCalculationInterface {
    BillCalculationResponse calculateBillDiscount(BillCalculationRequest request);
}