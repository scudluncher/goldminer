package com.carrera.goldminer.core

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.carrera.goldminer.core.*.infra.*"])
class CoreApplication {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder()
            .featuresToDisable(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            )
            .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .modulesToInstall(KotlinModule.Builder()
                .configure(KotlinFeature.NullIsSameAsDefault, enabled = true)
                .build())
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
