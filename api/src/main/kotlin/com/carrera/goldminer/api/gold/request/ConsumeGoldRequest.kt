package com.carrera.goldminer.api.gold.request

import com.carrera.goldminer.api.gold.usecase.ConsumingGoldValue
import javax.validation.constraints.Positive

class ConsumeGoldRequest(
    @field:Positive // ex) 30000금 사용 --> 30000 전송
    private val amount: Long,
) {
    fun toValue(userId: Long): ConsumingGoldValue {
        return ConsumingGoldValue(userId, amount)
    }
}
