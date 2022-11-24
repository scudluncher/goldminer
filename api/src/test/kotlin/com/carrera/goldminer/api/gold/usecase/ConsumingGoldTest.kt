package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.gold.exception.InsufficientGoldException
import com.carrera.goldminer.core.gold.domain.repository.FakeGoldBalanceRepository
import com.carrera.goldminer.core.gold.domain.repository.FakeGoldLedgerRepository
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ConsumingGoldTest : BehaviorSpec({
    given("남은 골드와 동일한 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(userId, 50000)
        When("차감하면") {
            val result = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
                .execute()
            then("골드는 0 이 된다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(0)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId)

                validGoldLedgers.size shouldBe 0
            }
        }
    }

    given("남은 골드보다 많은 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(userId, 100000000)
        When("차감하면") {
            val consumeGold = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("남은 골드보다 적은 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(userId, 45000)
        When("차감하면") {
            val result = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
                .execute()
            then("골드가 남는다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(5000)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId)

                validGoldLedgers.size shouldNotBe 0
                validGoldLedgers.first().chargedGold.gold shouldNotBe validGoldLedgers.first().usedGold
            }
        }
    }

    given("골드가 0인 user 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val allConsumedUserId = 3L
        val request = ConsumingGoldValue(allConsumedUserId, 40000)
        When("차감하면") {
            val consumeGold = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("골드 거래 내역이 없는 user 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val allConsumedUserId = 111L
        val request = ConsumingGoldValue(allConsumedUserId, 40000)
        When("차감하면") {
            val consumeGold = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

}) {
    companion object {
        private val userId = 1L
    }
}
