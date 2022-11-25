package com.carrera.goldminer.api.gold.request

import com.carrera.goldminer.api.gold.usecase.ChargingValue
import javax.validation.constraints.Size

class ChargingGoldWithCodeRequest(
    @field:Size(min = 10, max = 10)
    private val code: String,
) {
    fun toValue(userId: Long): ChargingValue {
        return ChargingValue(
            code,
            userId
        )
    }
}
