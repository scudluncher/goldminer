package com.carrera.goldminer.api.goldledger.usecase

import com.carrera.goldminer.api.goldledger.exception.InsufficientGoldException
import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount

class ConsumingGold(
    private val request: ConsumingGoldValue,
    private val goldLedgerRepository: GoldLedgerRepository,
) {
    fun execute(): GoldAmount {
        val validGoldLedgers =
            goldLedgerRepository.findValidOneByUserIdOrderByExpired(request.ownerId)

        val usableGoldAmount = validGoldLedgers.usableAmountOf()

        val afterConsumeBalance = usableGoldAmount - request.amount

        val updatedGoldLedgers = when {
            afterConsumeBalance < 0 -> throw InsufficientGoldException()
            afterConsumeBalance == 0L -> validGoldLedgers.map { it.allConsumed() }
            else -> deductUntilNotOverflow(request.amount, validGoldLedgers)
        }

        updatedGoldLedgers.map { goldLedgerRepository.save((it)) }

        //todo afterConsume balance 값 저장
        return GoldAmount(afterConsumeBalance)
    }

    private fun deductUntilNotOverflow(amount: Long, goldLedgers: List<GoldLedger>): List<GoldLedger> {
        var residual = amount

        fun calculate(): List<GoldLedger> {
            val affectedLedgers = goldLedgers.takeWhile {
                residual = -(it.chargedGold.amount - it.usedGold.amount)
                residual < 0
            }
            val allConsumedLedgers = affectedLedgers.dropLast(1).map { it.allConsumed() }
            val partiallyConsumedLedger = affectedLedgers.last().partiallyConsumed(residual)

            return allConsumedLedgers + partiallyConsumedLedger
        }

        return calculate()
    }
}

class ConsumingGoldValue(
    val ownerId: Long,
    val amount: Long,
)
