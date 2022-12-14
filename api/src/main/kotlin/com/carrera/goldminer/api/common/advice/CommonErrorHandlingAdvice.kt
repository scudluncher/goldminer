package com.carrera.goldminer.api.common.advice

import com.carrera.goldminer.api.common.exception.BadRequestException
import com.carrera.goldminer.api.common.response.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.orm.ObjectOptimisticLockingFailureException
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    fun concurrencyError(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "CONCURRENCY_ERROR",
                    "요청하신 행위는 유효하지 않습니다."
                )
            )
    }
}
