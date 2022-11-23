package com.carerra.goldminer.goldledger.request

import com.carerra.goldminer.goldledger.usecase.ConsumingGoldValue
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
