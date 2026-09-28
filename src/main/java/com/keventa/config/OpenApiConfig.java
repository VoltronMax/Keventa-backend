package com.keventa.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        final String securityScheme = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("API de Keventa")
                        .version("1.0")
                        .description("API REST para la gestión de productos, inventario, ventas y usuarios de Keventa, con autenticación y autorización basada en JWT."))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securityScheme))

                .components(new Components()
                        .addSecuritySchemes(securityScheme,
                                new SecurityScheme()
                                        .name(securityScheme)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introducir el token JWT en formato: Bearer (token)")));
    }
}
