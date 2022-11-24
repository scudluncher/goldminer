package com.carrera.goldminer.api.gold.advice

import com.carrera.goldminer.api.common.response.ErrorResponse
import com.carrera.goldminer.api.gold.exception.InsufficientGoldException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GoldErrorHandlingAdvice {
    @ExceptionHandler(InsufficientGoldException::class)
    fun insufficientGold(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "NOT_ENOUGH_GOLD",
                    "잔여 금이 부족합니다."
                )
            )
    }
}
