package com.carerra.goldminer.user.advice

import com.carerra.goldminer.common.response.ErrorResponse
import com.carerra.goldminer.user.exception.UnauthorizedException
import com.carerra.goldminer.user.exception.UserException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserErrorHandlingAdvice {
    @ExceptionHandler(UserException::class)
    fun internalServerError(e: UserException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    e.type.toString(),
                    "계정 인증 중 문제가 발생하였습니다."
                )
            )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorized(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    "UNAUTHORIZED",
                    "권한이 없습니다."
                )
            )
    }
}
