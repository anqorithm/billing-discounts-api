package sa.billing.discounts.presentation.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Root", description = "Application root endpoint")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RootController {

    @GetMapping
    @Operation(
        summary = "Get API information",
        description = "Returns basic information about the Billing Discounts API"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "API information retrieved successfully")
    })
    public ResponseEntity<sa.billing.discounts.presentation.dto.ApiResponse<Map<String, Object>>> getApiInfo() {
        Map<String, Object> apiInfo = Map.of(
            "name", "Billing Discounts API",
            "version", "1.0.0",
            "description", "RESTful API for calculating retail store discounts",
            "endpoints", Map.of(
                "swagger", "/swagger-ui.html",
                "api-docs", "/v3/api-docs",
                "bills", "/api/v1/bills",
                "health", "/actuator/health"
            ),
            "status", "running"
        );
        
        return ResponseEntity.ok(
            sa.billing.discounts.presentation.dto.ApiResponse.success("Welcome to Billing Discounts API", apiInfo)
        );
    }
}