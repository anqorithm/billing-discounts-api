package sa.billing.discounts.infrastructure.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final String mongoUri;

    public MongoConfig(@Value("${spring.data.mongodb.uri}") String mongoUri) {
        this.mongoUri = mongoUri;
    }

    @Override
    protected String getDatabaseName() {
        String fromUri = new ConnectionString(mongoUri).getDatabase();
        return (fromUri != null && !fromUri.isBlank()) ? fromUri : "billing_discounts";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
