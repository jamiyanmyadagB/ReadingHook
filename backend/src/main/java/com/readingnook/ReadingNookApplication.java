package com.readingnook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application entry point for ReadingNook backend.
 * 
 * This class bootstraps the Spring Boot application and enables
 * MongoDB repository support for data persistence.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.readingnook.repository")
public class ReadingNookApplication {

    /**
     * Main method that starts the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        SpringApplication.run(ReadingNookApplication.class, args);
    }
}
