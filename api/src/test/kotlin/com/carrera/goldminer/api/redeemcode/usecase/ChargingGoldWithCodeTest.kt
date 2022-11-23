package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.api.goldledger.usecase.usableAmountOf
import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import com.carrera.goldminer.core.goldledger.domain.repository.FakeGoldLedgerRepository
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
        val redeemCodeRepository = FakeRedeemCodeRepository()
        val redeemCode = redeemCodeRepository.findValidOneByCode(code)
        val beforeGoldAmount = goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId).usableAmountOf()
        When("충전하면") {
            val result = ChargingGoldWithCode(
                value,
                goldLedgerRepository,
                redeemCodeRepository
            )
                .execute()
            then("코드에 포함된 금이 충전된다.") {
                result.chargedGold.amount shouldBe redeemCode?.includedGold?.amount
                result.chargedGold.expiredBy shouldBe redeemCode?.includedGold?.expiredBy

                val afterGoldAmount = goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId).usableAmountOf()
                afterGoldAmount - beforeGoldAmount shouldBe redeemCode?.includedGold?.amount
            }
        }
    }

    given("유효하지 않은 코드가 주어지고") {
        val code = "1234509876"
        val userId = 1L
        val value = ChargingValue(code, userId)
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val redeemCodeRepository = FakeRedeemCodeRepository()
        When("충전하면") {
            val result = ChargingGoldWithCode(
                value,
                goldLedgerRepository,
                redeemCodeRepository
            )
            then("유효하지 않은 코드임을 알린다.") {
                shouldThrow<InvalidRedeemCodeException> { result.execute() }
            }
        }
    }
})
