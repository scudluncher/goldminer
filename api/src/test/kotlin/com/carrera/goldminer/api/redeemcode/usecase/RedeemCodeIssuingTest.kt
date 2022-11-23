package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.core.redeemcode.domain.repository.FakeRedeemCodeRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime

class RedeemCodeIssuingTest : BehaviorSpec({
    given("10자리 Alphanumerical 무작위코드") {
        When("생성하면") {
            then("서로 다른 값이 생성된다.") {
                repeat(7) {
                    println(randomCode())
                }
            }
        }
    }

    given("redeem code 생성 값이 주어지고") {
        val monthLater = ZonedDateTime.now().plusDays(30L)
        val weekLater = ZonedDateTime.now().plusWeeks(1L)
        val goldAmount = 100L
        val redeemCodeIssueValue = RedeemCodeIssueValue(
            goldAmount,
            monthLater,
            weekLater
        )
        When("발행하면") {
            val issuedCode = RedeemCodeIssuing(
                redeemCodeIssueValue,
                redeemCodeRepository,
            )
                .execute()
            then("redeem code 가 발행된다.") {
                issuedCode.redeemed shouldBe false
                issuedCode.gold.amount shouldBe goldAmount
                issuedCode.gold.expiredBy shouldBe monthLater
                issuedCode.expiredBy shouldBe weekLater
            }
        }
    }

}) {
    companion object {
        private val redeemCodeRepository = FakeRedeemCodeRepository()
    }
}
