package com.carrera.goldminer.api.redeemcode.advice

import com.carrera.goldminer.api.common.response.ErrorResponse
import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class RedeemCodeErrorHandlingAdvice {
    @ExceptionHandler(InvalidRedeemCodeException::class)
    fun invalidRedeemCode(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "INVALID_REDEEM_CODE",
                    "유효하지 않은 충전 코드입니다."
                )
            )
    }
}
