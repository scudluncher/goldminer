package com.carrera.goldminer.api.goldledger.service

import com.carrera.goldminer.api.goldledger.usecase.ConsumingGold
import com.carrera.goldminer.api.goldledger.usecase.ConsumingGoldValue
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount
import org.springframework.stereotype.Service

@Service
class GoldLedgerService(private val goldLedgerRepository: GoldLedgerRepository) {
    fun consumeGold(request: ConsumingGoldValue): GoldAmount {
        return ConsumingGold(
            request,
            goldLedgerRepository
        )
            .execute()
    }
}
