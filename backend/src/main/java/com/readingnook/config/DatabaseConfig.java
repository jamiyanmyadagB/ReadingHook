package com.readingnook.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDB configuration class for ReadingNook application.
 * 
 * Configures MongoDB connection using URI from application.properties
 * and provides connection validation on startup.
 */
@Configuration
public class DatabaseConfig extends AbstractMongoClientConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        // Extract database name from URI or use default
        return "readingnook";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        logger.info("Connecting to MongoDB with URI: {}", mongoUri);

        MongoClient client = MongoClients.create(mongoUri);
        
        // Test connection asynchronously (non-blocking)
        new Thread(() -> {
            try {
                client.getDatabase("readingnook").runCommand(new Document("ping", 1));
                logger.info("✅ MongoDB connection established successfully");
            } catch (Exception e) {
                logger.warn("⚠️  MongoDB connection test failed: {}. The app will continue, but verify your connection.", e.getMessage());
            }
        }).start();

        return client;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
