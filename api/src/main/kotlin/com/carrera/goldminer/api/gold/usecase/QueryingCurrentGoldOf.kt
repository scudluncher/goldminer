package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.gold.value.CurrentGold
import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class QueryingCurrentGoldOf(
    private val userId: Long,
    private val goldBalanceRepository: GoldBalanceRepository,
) {
    fun execute(): CurrentGold {
        return goldBalanceRepository.findByUserId(userId)
            ?.let {
                CurrentGold(it.gold, it.version)
            }
            ?: CurrentGold(GoldAmount(0), 0)
    }
}




