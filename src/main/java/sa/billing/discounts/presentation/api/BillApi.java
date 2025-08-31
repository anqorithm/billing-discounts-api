package sa.billing.discounts.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import sa.billing.discounts.application.dto.BillCalculationRequest;
import sa.billing.discounts.application.dto.BillCalculationResponse;

@Tag(name = "bill management", description = "apis for bill calculation and discount management")
public interface BillApi {

  @Operation(
      summary = "calculate bill with discounts",
      description = "calculate the net payable amount for a bill after applying applicable discounts"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "bill calculated successfully"),
      @ApiResponse(responseCode = "400", description = "invalid request data"),
      @ApiResponse(responseCode = "404", description = "customer or product not found"),
      @ApiResponse(responseCode = "500", description = "internal server error")
  })
  ResponseEntity<sa.billing.discounts.presentation.dto.ApiResponse<BillCalculationResponse>> calculateBill(
      @Parameter(description = "bill calculation request containing customer id and items")
      @Valid @RequestBody BillCalculationRequest request
  );

  @Operation(
      summary = "health check",
      description = "check if the bill service is running"
  )
  @ApiResponse(responseCode = "200", description = "service is healthy")
  ResponseEntity<sa.billing.discounts.presentation.dto.ApiResponse<String>> healthCheck();
}