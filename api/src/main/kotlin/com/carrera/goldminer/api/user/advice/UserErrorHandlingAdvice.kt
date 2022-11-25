package com.carrera.goldminer.api.user.advice

import com.carrera.goldminer.api.common.response.ErrorResponse
import com.carrera.goldminer.api.user.exception.NoPermissionException
import com.carrera.goldminer.api.user.exception.UserException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserErrorHandlingAdvice {
    @ExceptionHandler(UserException::class)
    fun authenticationFailed(e: UserException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    e.type.toString(),
                    "계정 인증 중 문제가 발생하였습니다."
                )
            )
    }

    @ExceptionHandler(NoPermissionException::class)
    fun unauthorized(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                ErrorResponse(
                    "FORBIDDEN",
                    "금지된 접근입니다."
                )
            )
    }
}
