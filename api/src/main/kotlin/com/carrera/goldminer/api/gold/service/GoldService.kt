package com.carrera.goldminer.api.gold.service

import com.carrera.goldminer.api.gold.usecase.*
import com.carrera.goldminer.api.gold.value.CurrentGold
import com.carrera.goldminer.api.gold.value.GoldChangeResult
import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoldService(
    private val goldLedgerRepository: GoldLedgerRepository,
    private val goldBalanceRepository: GoldBalanceRepository,
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    @Transactional
    fun consumeGold(request: ConsumingGoldValue): GoldChangeResult {
        return ConsumingGold(
            request,
            goldLedgerRepository,
            goldBalanceRepository
        )
            .execute()
    }

    @Transactional(readOnly = true)
    fun currentGoldOf(userId: Long): CurrentGold {
        return QueryingCurrentGoldOf(
            userId,
            goldBalanceRepository
        )
            .execute()
    }

    @Transactional
    fun chargeGoldWithCode(value: ChargingValue): GoldChangeResult {
        return ChargingGoldWithCode(
            value,
            goldLedgerRepository,
            goldBalanceRepository,
            redeemCodeRepository,
        )
            .execute()
    }
}
