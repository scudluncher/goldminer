package com.carrera.goldminer.api.redeemcode.request

import com.carrera.goldminer.api.redeemcode.usecase.ChargingValue
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class ChargingGoldWithCodeRequest(
    @field:Size(min = 10, max = 10)
    private val code: String,
    @field:Positive
    val userId: Long,
) {
    fun toValue(): ChargingValue {
        return ChargingValue(
            code,
            userId
        )
    }
}
