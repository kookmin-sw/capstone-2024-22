package com.moment.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
//        String securityJwtName = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");
//        Components components = new Components().addSecuritySchemes(securityJwtName, new SecurityScheme()
//                .name(securityJwtName)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("Bearer")
//                .bearerFormat(securityJwtName));


        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(components)
                .components(new Components())
                .info(apiInfo(springdocVersion));
    }

    private Info apiInfo(String springdocVersion) {
        return new Info()
                .title("Springdoc 테스트")
                .description("Springdoc-Swagger")
                .version(springdocVersion);
    }
}