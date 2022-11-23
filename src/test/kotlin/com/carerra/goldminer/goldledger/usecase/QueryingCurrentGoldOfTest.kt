package com.carerra.goldminer.goldledger.usecase

import com.carerra.goldminer.goldledger.domain.repository.FakeGoldLedgerRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class QueryingCurrentGoldOfTest : BehaviorSpec({
    given("골드를 소유한 유저 ID 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val userId = 1L
        When("현재 소유 골드를 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldLedgerRepository).execute()
            then("소유 골드 현황을 반환한다.") {
                result.amount shouldBeGreaterThan 0
                result.amount shouldBe 50000
            }
        }
    }

    given("골드 거래 내역이 없는 user 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val userId = 111L
        When("현재 소유 골드를 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldLedgerRepository).execute()
            then("소유 골드 현황 0골드를 반환한다.") {
                result.amount shouldBe 0
            }
        }
    }

    given("골드가 만료되거나 없는 ID 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val userId = 3L
        When("현재 소유 골드를 조회하면") {
            val result = QueryingCurrentGoldOf(userId, goldLedgerRepository).execute()
            then("소유 골드 현황 0골드를 반환한다.") {
                result.amount shouldBe 0
            }
        }
    }
})
