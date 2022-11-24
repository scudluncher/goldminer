package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.gold.value.GoldChangeResult
import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import com.carrera.goldminer.core.gold.domain.entity.GoldBalance
import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository

class ChargingGoldWithCode(
    private val value: ChargingValue,
    private val goldLedgerRepository: GoldLedgerRepository,
    private val goldBalanceRepository: GoldBalanceRepository,
    private val redeemCodeRepository: RedeemCodeRepository,
) : GoldChangeUseCase {
    override fun execute(): GoldChangeResult {
        val redeemCode = redeemCodeRepository.findValidOneByCodeWithLock(value.code)
            ?: throw InvalidRedeemCodeException()

        val redeemingResult = redeemCode.redeem(value.userId)
        redeemCodeRepository.save(redeemingResult.consumedCode)
        goldLedgerRepository.save(redeemingResult.newLedger)

        val balance = goldBalanceRepository.findByUserId(value.userId)
            ?.changeBalance(redeemCode.includedChargedGold.gold)
            ?: GoldBalance(
                userId = value.userId,
                gold = redeemCode.includedChargedGold.gold
            )
        goldBalanceRepository.save(balance)

        return GoldChangeResult(
            currentGoldBalanceAmount = balance.gold,
            changedGold = redeemCode.includedChargedGold.gold,
            expiredBy = redeemCode.expiredBy
        )
    }
}

class ChargingValue(
    val code: String,
    val userId: Long,
)
