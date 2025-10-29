package com.example.usersbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for configuring the RestTemplate bean used for making HTTP requests.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Returns a new instance of RestTemplate.
     * @return the RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
