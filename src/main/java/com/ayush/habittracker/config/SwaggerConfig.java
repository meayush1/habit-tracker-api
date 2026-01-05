package com.ayush.habittracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
