package com.github.rafaelsilvestri.dynamodb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Default app config.
 *
 * @author Rafael Silvestri
 */
@Configuration
@ComponentScan(basePackages = "com.github.rafaelsilvestri.dynamodb")
public class ApplicationConfig {

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
