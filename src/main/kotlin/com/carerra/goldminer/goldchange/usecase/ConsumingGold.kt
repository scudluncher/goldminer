package com.carerra.goldminer.goldchange.usecase

import com.carerra.goldminer.goldchange.domain.entity.GoldLedger
import com.carerra.goldminer.goldchange.domain.repository.GoldLedgerRepository
import com.carerra.goldminer.goldchange.exception.InsufficientGoldException

class ConsumingGold(
    private val request: ConsumingGoldRequest,
    private val goldLedgerRepository: GoldLedgerRepository,
) {
    fun execute(): Long {
        val validGoldLedgers =
            goldLedgerRepository.findByOwnerIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(request.ownerId)

        val usableGold = validGoldLedgers.sumOf { it.chargedGold.amount - it.usedGold.amount }

        val afterConsumeBalance = usableGold - request.amount

        val updatedGoldLedgers = when {
            afterConsumeBalance < 0 -> throw InsufficientGoldException()
            afterConsumeBalance == 0L -> validGoldLedgers.map { it.allConsumed() }
            else -> deductUntilNotOverflow(request.amount, validGoldLedgers)
        }

        updatedGoldLedgers.map { goldLedgerRepository.save((it)) }

        //todo afterConsume balance 값 저장
        return afterConsumeBalance
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

class ConsumingGoldRequest(
    val ownerId: Long,
    val amount: Long,
)
