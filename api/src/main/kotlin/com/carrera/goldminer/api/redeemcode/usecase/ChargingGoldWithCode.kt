package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository

class ChargingGoldWithCode(
    private val value: ChargingValue,
    private val goldLedgerRepository: GoldLedgerRepository,
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    fun execute(): GoldLedger {
        val redeemCode = redeemCodeRepository.findValidOneByCode(value.code)
            ?: throw InvalidRedeemCodeException()

        val redeemingResult = redeemCode.redeem(value.userId)

        redeemCodeRepository.save(redeemingResult.consumedCode)

        return goldLedgerRepository.save(redeemingResult.newLedger)
    }
}

class ChargingValue(
    val code: String,
    val userId: Long,
)
