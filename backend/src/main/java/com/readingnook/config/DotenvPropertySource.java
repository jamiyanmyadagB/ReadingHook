package com.readingnook.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads environment variables from .env file and makes them available to Spring.
 */
public class DotenvPropertySource implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Convert dotenv entries to a map
        Map<String, Object> dotenvProperties = new HashMap<>();
        dotenv.entries().forEach(entry ->
            dotenvProperties.put(entry.getKey(), entry.getValue())
        );

        // Add the properties to Spring's environment
        if (!dotenvProperties.isEmpty()) {
            MapPropertySource propertySource = new MapPropertySource("dotenv", dotenvProperties);
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}

