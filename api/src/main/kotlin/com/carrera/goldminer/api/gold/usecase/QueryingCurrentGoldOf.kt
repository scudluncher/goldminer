package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class QueryingCurrentGoldOf(
    private val userId: Long,
    private val goldBalanceRepository: GoldBalanceRepository,
) {
    fun execute(): GoldAmount {
        return goldBalanceRepository.findByUserId(userId)
            ?.gold
            ?: GoldAmount(0)


    }
}




