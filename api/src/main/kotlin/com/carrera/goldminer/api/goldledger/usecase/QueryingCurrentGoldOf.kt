package com.carrera.goldminer.api.goldledger.usecase

import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount

class QueryingCurrentGoldOf(
    private val ownerId: Long,
    private val goldLedgerRepository: GoldLedgerRepository,
) {
    fun execute(): GoldAmount {
        val validGoldLedgers =
            goldLedgerRepository.findByOwnerIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(ownerId)

        return GoldAmount(validGoldLedgers.usableAmountOf())
    }
}

fun List<GoldLedger>.usableAmountOf(): Long {
    return sumOf { it.chargedGold.amount - it.usedGold.amount }
}


