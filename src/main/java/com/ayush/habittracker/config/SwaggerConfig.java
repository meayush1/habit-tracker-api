package com.ayush.habittracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

//@SecurityScheme(
//	    name = "bearerAuth",
//	    type = SecuritySchemeType.HTTP,
//	    scheme = "bearer",
//	    bearerFormat = "JWT"
//	)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI habitTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Habit Tracker API")
                        .description("Backend API for habit tracking, analytics & streaks")
                        .version("v1.0.0"));
    }
}
