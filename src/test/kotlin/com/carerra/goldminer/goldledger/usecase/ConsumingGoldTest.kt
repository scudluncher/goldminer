package com.carerra.goldminer.goldledger.usecase

import com.carerra.goldminer.goldledger.domain.repository.FakeGoldLedgerRepository
import com.carerra.goldminer.goldledger.exception.InsufficientGoldException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ConsumingGoldTest : BehaviorSpec({
    given("남은 골드와 동일한 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val request = ConsumingGoldValue(ownerId, 50000)
        When("차감하면") {
            val result = ConsumingGold(request, goldLedgerRepository).execute()
            then("골드는 0 이 된다.") {
                result.amount shouldBe 0

                val validGoldLedgers =
                    goldLedgerRepository.findByOwnerIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(ownerId)

                validGoldLedgers.size shouldBe 0
            }
        }
    }

    given("남은 골드보다 많은 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val request = ConsumingGoldValue(ownerId, 100000000)
        When("차감하면") {
            val consumeGold = ConsumingGold(request, goldLedgerRepository)
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("남은 골드보다 적은 양의 차감 골드가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val request = ConsumingGoldValue(ownerId, 45000)
        When("차감하면") {
            val result = ConsumingGold(request, goldLedgerRepository).execute()
            then("골드가 남는다.") {
                result.amount shouldBe 5000

                val validGoldLedgers =
                    goldLedgerRepository.findByOwnerIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(ownerId)

                validGoldLedgers.size shouldNotBe 0
                validGoldLedgers.first().chargedGold.amount shouldNotBe validGoldLedgers.first().usedGold.amount
            }
        }
    }

    given("골드가 없는 user 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val allConsumedUserId = 3L
        val request = ConsumingGoldValue(allConsumedUserId, 40000)
        When("차감하면") {
            val consumeGold = ConsumingGold(request, goldLedgerRepository)
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("골드 거래 내역이 없는 user 가 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val allConsumedUserId = 111L
        val request = ConsumingGoldValue(allConsumedUserId, 40000)
        When("차감하면") {
            val consumeGold = ConsumingGold(request, goldLedgerRepository)
            then("골드가 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

}) {
    companion object {
        private val ownerId = 1L
    }
}
