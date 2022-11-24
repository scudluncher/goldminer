package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.core.gold.domain.repository.FakeGoldBalanceRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class QueryingCurrentGoldOfTest : BehaviorSpec({
    given("금을 소유한 유저 ID 가 주어지고") {
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val userId = 1L
        When("현재 소유 금을 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldBalanceRepository).execute()
            then("소유 금 현황을 반환한다.") {
                result.amount shouldBeGreaterThan 0
                result.amount shouldBe 50000
            }
        }
    }

    given("금 거래 내역이 없는 user 가 주어지고") {
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val userId = 111L
        When("현재 소유 금을 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldBalanceRepository).execute()
            then("소유 금 현황 0금을 반환한다.") {
                result.amount shouldBe 0
            }
        }
    }

    given("금이 만료되거나 없는 ID 가 주어지고") {
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val userId = 3L
        When("현재 소유 금을 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldBalanceRepository).execute()
            then("소유 금 현황 0금을 반환한다.") {
                result.amount shouldBe 0
            }
        }
    }
})
