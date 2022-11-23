package com.carerra.goldminer.common.advice

import com.carerra.goldminer.common.BadRequestException
import com.carerra.goldminer.common.response.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class CommonErrorHandlingAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun internalServerError(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "INTERNAL_SERVER_ERROR",
                    e.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                )
            )
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(e: BadRequestException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "BAD_REQUEST",
                    e.message ?: "잘못된 요청입니다."
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun requestArgumentWrong(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    "WRONG_ARGUMENT",
                    e.bindingResult.allErrors[0].defaultMessage ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }
}
