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
        // 2. 차감 후 잔고 예상액이 0보다 클 때 ==> n-1 개의 gold ledger 소모 처리, 마지막 1개 부분 소모 처리
        // 변경 내역 저장
        val afterConsumeBalanceAmountExpectation = balance.gold.amount - consumeValue.amount
        when (afterConsumeBalanceAmountExpectation) {
            0L -> validGoldLedgers.map { it.allConsumed() }
            else -> deductUntilNotOverflow(consumeValue.amount, validGoldLedgers)
        }
            .map { goldLedgerRepository.save((it)) }

        // 변경된 잔고 저장
        val consumedGold = GoldAmount(-consumeValue.amount)
        val afterConsumingBalance = goldBalanceRepository.save(balance.changeBalance(consumedGold))

        return GoldChangeResult(
            currentGoldBalanceAmount = afterConsumingBalance.gold,
            changedGold = consumedGold
        )
    }

    private fun deductUntilNotOverflow(amount: Long, goldLedgers: List<GoldLedger>): List<GoldLedger> {
        var residual = amount
        // n-1개의 gold ledger 소모 처리, 마지막 1개 부분 소모 처리
        fun calculate(): List<GoldLedger> {
            val affectedLedgers = goldLedgers.takeWhile {
                residual = -(it.chargedGold.gold - it.usedGold).amount
                residual < 0
            }
            val allConsumedLedgers = affectedLedgers.dropLast(1).map { it.allConsumed() }
            val partiallyConsumedLastLedger = affectedLedgers.last().partiallyConsumed(residual)

            return allConsumedLedgers + partiallyConsumedLastLedger
        }

        return calculate()
    }
}

class ConsumingGoldValue(
    val userId: Long,
    val amount: Long,
)
