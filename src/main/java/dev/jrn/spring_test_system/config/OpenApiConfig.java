package dev.jrn.spring_test_system.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Test System API",
                version = "0.1.0",
                description = "API for managing students, tests, graded attempts, and result reporting."
        ),
        servers = @Server(url = "http://localhost:8080", description = "Local development")
)
public class OpenApiConfig { }
