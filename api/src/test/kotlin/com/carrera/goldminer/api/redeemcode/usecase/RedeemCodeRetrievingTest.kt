package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.core.common.value.PagingCondition
import com.carrera.goldminer.core.redeemcode.domain.repository.FakeRedeemCodeRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe

class RedeemCodeRetrievingTest : BehaviorSpec({
    given("모든 충전 코드에 대해") {
        val redeemCodeRepository = FakeRedeemCodeRepository()
        When("검색하면") {
            val result = redeemCodeRepository.findAll(PagingCondition(null, null))
            then("모든 충전 코드가 조회된다.") {
                result.items.size shouldBeGreaterThanOrEqual 0
                result.items.size shouldBe result.totalCount
            }
        }
    }

    given("충전 코드 페이지 값이 주어지고") {
        val redeemCodeRepository = FakeRedeemCodeRepository()
        val perPage = 2
        val pagingCondition = PagingCondition(1, perPage)
        When("검색하면") {
            val result = redeemCodeRepository.findAll(pagingCondition)
            then("충전 코드가 페이징 처리되어 조회된다.") {
                result.items.size shouldBe perPage
            }
        }
    }
})
