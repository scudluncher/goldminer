package com.carrera.goldminer.api.goldledger.request

import com.carrera.goldminer.api.goldledger.usecase.ConsumingGoldValue
import javax.validation.constraints.Positive

class ConsumeGoldRequest(
    val ownerId: Long,
    @field:Positive
    private val amount: Long,
) {
    fun toValue(): ConsumingGoldValue {
        return ConsumingGoldValue(ownerId, amount)
    }
}
