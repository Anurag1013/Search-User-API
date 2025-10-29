package com.example.usersbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible for configuring the OpenAPI documentation for the Users API.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Returns a custom OpenAPI object with the Users API information and security requirements.
     * @return the custom OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String scheme = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Users API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(scheme))
                .components(new Components()
                        .addSecuritySchemes(scheme,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                );
    }
}
