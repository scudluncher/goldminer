package com.carerra.goldminer.goldledger.service

import com.carerra.goldminer.goldledger.domain.repository.GoldLedgerRepository
import com.carerra.goldminer.goldledger.domain.value.GoldAmount
import com.carerra.goldminer.goldledger.usecase.ConsumingGold
import com.carerra.goldminer.goldledger.usecase.ConsumingGoldValue
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
