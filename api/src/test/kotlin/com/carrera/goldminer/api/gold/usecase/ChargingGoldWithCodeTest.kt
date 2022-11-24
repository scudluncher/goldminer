package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import com.carrera.goldminer.core.gold.domain.entity.usableAmountAsLong
import com.carrera.goldminer.core.gold.domain.repository.FakeGoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.repository.FakeGoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.repository.FakeRedeemCodeRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ChargingGoldWithCodeTest : BehaviorSpec({
    given("유효한 코드가 주어지고") {
        val code = "7K2J77US6V"
        val userId = 1L
        val value = ChargingValue(code, userId)
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val redeemCodeRepository = FakeRedeemCodeRepository()
        val redeemCode = redeemCodeRepository.findValidOneByCodeWithLock(code)
        val beforeGoldAmountTotal = goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId).usableAmountAsLong()
        val beforeGoldBalance = goldBalanceRepository.findByUserId(userId)
        When("충전하면") {
            val result = ChargingGoldWithCode(
                value,
                goldLedgerRepository,
                goldBalanceRepository,
                redeemCodeRepository
            )
                .execute()
            then("코드에 포함된 금이 충전된다.") {
                result.changedGold shouldBe redeemCode?.includedChargedGold?.gold
                result.expiredBy shouldBe redeemCode?.includedChargedGold?.expiredBy

                val afterGoldAmountTotal = goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId).usableAmountAsLong()
                afterGoldAmountTotal - beforeGoldAmountTotal shouldBe redeemCode?.includedChargedGold?.gold?.amount

                val afterGoldBalance = goldBalanceRepository.findByUserId(userId)
                requireNotNull(beforeGoldBalance)
                requireNotNull(afterGoldBalance)
                afterGoldBalance.gold - beforeGoldBalance.gold shouldBe redeemCode?.includedChargedGold?.gold
            }
        }
    }

    given("유효하지 않은 코드가 주어지고") {
        val code = "1234509876"
        val userId = 1L
        val value = ChargingValue(code, userId)
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val redeemCodeRepository = FakeRedeemCodeRepository()
        When("충전하면") {
            val result = ChargingGoldWithCode(
                value,
                goldLedgerRepository,
                goldBalanceRepository,
                redeemCodeRepository
            )
            then("유효하지 않은 코드임을 알린다.") {
                shouldThrow<InvalidRedeemCodeException> { result.execute() }
            }
        }
    }
})
