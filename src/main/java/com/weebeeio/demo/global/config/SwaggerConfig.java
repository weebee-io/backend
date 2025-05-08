package com.weebeeio.demo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            // 전역으로 보안 요구사항 추가
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .info(apiInfo());
    }

    @Bean
    public OpenApiCustomizer securityCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            // 회원가입·로그인·아이디체크 경로는 보안 스킴 제거
            if ("/users/signup".equals(path)
                || "/users/login".equals(path)
                || "/quiz/admin/upload".equals(path)
                || path.startsWith("/users/check-id")) {

                pathItem.readOperations()
                        .forEach(op -> op.setSecurity(Collections.emptyList()));
            }
        });
    }

    private Info apiInfo() {
        return new Info()
            .title("Spring Boot REST API Specifications")
            .description("설명")
            .version("1.0.0");
    }
}
