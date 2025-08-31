package sa.billing.discounts.infrastructure.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Override
    protected String getDatabaseName() {
        return "billing_discounts";
    }
    
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}