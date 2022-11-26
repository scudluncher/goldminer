package com.carrera.goldminer.api

import com.carrera.goldminer.api.common.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.filter.ShallowEtagHeaderFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootApplication(scanBasePackages = ["com.carrera.goldminer"])
class ApiApplication

@Configuration
class HttpCacheConfig {
    @Bean
    fun shallowEtagHeaderFilter(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val filterRegistrationBean = FilterRegistrationBean(ShallowEtagHeaderFilter())
        filterRegistrationBean.addUrlPatterns("/user/*/golds")
        filterRegistrationBean.setName("etagFilter")

        return filterRegistrationBean
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfig(private val objectMapper: ObjectMapper) {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.authorizeHttpRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/users/**").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
            .and()
            .logout()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
            .authenticationEntryPoint { _, response, _ ->
                val errorResponse = ErrorResponse(
                    "401",
                    "로그인이 필요합니다."
                )
                response.contentType = "application/json;charset=UTF-8"
                response.status = HttpStatus.FORBIDDEN.value()
                response.writer.write(objectMapper.writeValueAsString(errorResponse))
            }
            .and()
            .httpBasic()
            .and()
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()

        return httpSecurity.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Component
class CustomAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    private fun setResponse(response: HttpServletResponse, errorResponse: ErrorResponse) {
        val json = objectMapper.writeValueAsString(errorResponse)
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.writer.print(json)
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val errorResponse = ErrorResponse(
            "FORBIDDEN",
            "금지된 접근입니다."
        )
       setResponse(response, errorResponse)
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
