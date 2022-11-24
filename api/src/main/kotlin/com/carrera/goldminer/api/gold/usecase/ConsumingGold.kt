package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.gold.exception.InsufficientGoldException
import com.carrera.goldminer.api.gold.value.GoldChangeResult
import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class ConsumingGold(
    private val consumeValue: ConsumingGoldValue,
    private val goldLedgerRepository: GoldLedgerRepository,
    private val goldBalanceRepository: GoldBalanceRepository,
) : GoldChangeUseCase {
    override fun execute(): GoldChangeResult {
        // 잔고 조회
        val balance = goldBalanceRepository.findByUserId(consumeValue.userId)
            ?.also { if (it.gold.amount - consumeValue.amount < 0L) throw InsufficientGoldException() }
            ?: throw InsufficientGoldException()

        // 차감 가능 금액 조회
        val validGoldLedgers =
            goldLedgerRepository.findValidOneByUserIdOrderByExpired(consumeValue.userId)

        // 차감 후 잔고 예상액에 따라서 goldLedger list 처리 분기
        // 1. 차감 후 잔고 예상액 0 ==> 모든 gold ledger 소모 처리
        // 2. 차감 후 잔고 예상액이 0보다 클 때 ==> n 개의 gold ledger 소모 처리, n+1번째 부분 소모 처리
        // 변경 내역 저장
        val afterConsumeBalanceAmountExpectation = balance.gold.amount - consumeValue.amount
        val affectedLedgers = when (afterConsumeBalanceAmountExpectation) {
            0L -> validGoldLedgers.map { it.allConsumed() }
            else -> calculateForResidualCase(consumeValue.amount, validGoldLedgers)
        }

        affectedLedgers.map { goldLedgerRepository.save(it) }
        // 변경된 잔고 저장
        val consumedGold = GoldAmount(-consumeValue.amount)
        val afterConsumingBalance = goldBalanceRepository.save(balance.changeBalance(consumedGold))

        return GoldChangeResult(
            currentGoldBalanceAmount = afterConsumingBalance.gold,
            changedGold = consumedGold
        )
    }

    private fun calculateForResidualCase(amount: Long, goldLedgers: List<GoldLedger>): List<GoldLedger> {
        var residual = amount

        // 완전 소모 해야 하는 n 개 소모처리, n+1 번째 부분 소모 처리
        fun calculate(): List<GoldLedger> {
            val allConsumedLedgers = goldLedgers.takeWhile {
                val remainingGold = (it.chargedGold.gold - it.usedGold).amount
                residual -= remainingGold
                residual >= 0L
            }
            return if (residual == 0L) {
                allConsumedLedgers.map { it.allConsumed() }
            } else {
                allConsumedLedgers.map { it.allConsumed() } +
                        goldLedgers[allConsumedLedgers.size].partiallyConsumed(residual)
            }
        }

        return calculate()
    }
}

class ConsumingGoldValue(
    val userId: Long,
    val amount: Long,
)

inline fun <T> Iterable<T>.takeWhileInclusive(
    predicate: (T) -> Boolean,
): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = predicate(it)
        result
    }
}
