package sa.billing.discounts.infrastructure.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Billing Discounts API")
                        .description("RESTful API for calculating retail store discounts on bills")
                        .version(appVersion)
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@company.com")
                                .url("https://github.com/company/billing-discounts-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development server"),
                        new Server().url("https://api.example.com").description("Production server")
                ));
    }
}