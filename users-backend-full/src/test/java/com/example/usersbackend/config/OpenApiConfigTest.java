package com.example.usersbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void testCustomOpenAPIConfiguration() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.customOpenAPI();

        // Assert
        assertNotNull(openAPI, "OpenAPI object should not be null");

        Info info = openAPI.getInfo();
        assertNotNull(info, "Info section should not be null");
        assertEquals("Users API", info.getTitle(), "Title should be 'Users API'");
        assertEquals("1.0", info.getVersion(), "Version should be '1.0'");

        // Verify security setup
        assertNotNull(openAPI.getComponents(), "Components should not be null");
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"),
                "Should contain 'bearerAuth' security scheme");

        SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());

        // Verify that security requirement is added
        assertFalse(openAPI.getSecurity().isEmpty(), "Security requirements should not be empty");
        SecurityRequirement requirement = openAPI.getSecurity().get(0);
        assertTrue(requirement.containsKey("bearerAuth"), "Security requirement should include 'bearerAuth'");
    }
}
