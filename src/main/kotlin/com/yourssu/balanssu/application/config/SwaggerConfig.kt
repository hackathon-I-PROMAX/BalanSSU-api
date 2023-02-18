package com.yourssu.balanssu.application.config

import com.yourssu.balanssu.core.security.UserInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
@EnableOpenApi
class SwaggerConfig {
    @Bean
    fun autApi(): Docket =
        Docket(DocumentationType.OAS_30)
            .ignoredParameterTypes(UserInfo::class.java)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.yourssu.balanssu.application"))
            .build()
            .securitySchemes(listOf(apiKey()))
            .securityContexts(listOf(securityContext()))

    private fun securityContext() =
        SecurityContext.builder().securityReferences(defaultAuth())
            .operationSelector { true }
            .build()

    private fun apiKey() =
        ApiKey("Authorization", "Bearer", "header")

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "access All")
        return listOf(SecurityReference("Authorization", arrayOf(authorizationScope)))
    }
}
