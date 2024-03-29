package com.yourssu.balanssu.core.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider
) {
    companion object {
        private const val ADMIN = "ADMIN"
        private const val USER = "USER"
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            httpBasic { disable() }
            csrf { disable() }
            cors { }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeRequests {
                authorize("/auth/refresh", hasRole(USER))
                authorize("/auth/info", hasRole(USER))
                authorize("/auth/withdrawal", hasRole(USER))
                authorize("/auth/**", permitAll)
                authorize("/admin/**", hasRole(ADMIN))
                authorize(anyRequest, hasRole(USER))
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                JwtAuthenticationFilter(objectMapper, jwtTokenProvider)
            )
        }
        return http.build()
    }

    @Bean
    fun ignoringCustomizer() =
        WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().antMatchers(
                "/v3/api-docs",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger/**"
            )
        }
}
