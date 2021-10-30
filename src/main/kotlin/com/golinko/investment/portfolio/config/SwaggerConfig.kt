package com.golinko.investment.portfolio.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Investment portfolio REST API")
                .version("v0.0.1")
                .contact(Contact().name("Anna").url("https://github.com/golinko/investment-portfolio"))
        )
    }
}
