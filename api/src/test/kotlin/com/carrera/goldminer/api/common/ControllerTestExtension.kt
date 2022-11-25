package com.carrera.goldminer.api.common

import com.carrera.goldminer.api.common.advice.CommonErrorHandlingAdvice
import com.carrera.goldminer.api.gold.advice.GoldErrorHandlingAdvice
import com.carrera.goldminer.api.redeemcode.advice.RedeemCodeErrorHandlingAdvice
import com.carrera.goldminer.api.user.advice.UserErrorHandlingAdvice
import com.carrera.goldminer.core.user.domain.entity.User
import com.carrera.goldminer.core.user.domain.value.UserType
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter

interface ControllerTestExtension {
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .registerModules(
                ParameterNamesModule(),
                JavaTimeModule()
            )
    }

    fun defaultMockMvcBuilder(
        controller: Any,
    ): StandaloneMockMvcBuilder {
        return MockMvcBuilders.standaloneSetup(controller)
            .addFilters<StandaloneMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))

            .alwaysDo<StandaloneMockMvcBuilder>(MockMvcResultHandlers.print())
            .setControllerAdvice(
                UserErrorHandlingAdvice(),
                GoldErrorHandlingAdvice(),
                RedeemCodeErrorHandlingAdvice(),
                CommonErrorHandlingAdvice(),
            )
            .setMessageConverters(httpMessageConverter<Any>())
//            .apply(springSecurity()) // todo why spring security setting does not bring its Security Filter Chain?
    }

    fun <T> httpMessageConverter(): MappingJackson2HttpMessageConverter {
        val objectMapper = objectMapper()
        return MappingJackson2HttpMessageConverter(objectMapper)
    }

    fun setAdminSecurityContext() {
        SecurityContextHolder.clearContext()
        SecurityContextHolder.getContext()
            .authentication = UsernamePasswordAuthenticationToken(
            User(4L, "admin", "somepassword", UserType.ADMIN),
            "somepassword",
            mutableListOf(SimpleGrantedAuthority("ADMIN"))
        )
    }

    fun setUserSecurityContext() {
        SecurityContextHolder.clearContext()
        SecurityContextHolder.getContext()
            .authentication = UsernamePasswordAuthenticationToken(
            User(1L, "user", "somepassword", UserType.USER),
            "somepassword",
            mutableListOf(SimpleGrantedAuthority("USER"))
        )
    }

//    fun setAnonymousUserSecurityContext() {
//        SecurityContextHolder.clearContext()
//        SecurityContextHolder.getContext()
//            .authentication = null
//    }
}

