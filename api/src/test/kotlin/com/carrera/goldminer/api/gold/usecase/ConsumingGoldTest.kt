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
    given("남은 금과 동일한 양의 차감 금이 주어지고") {
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
            then("금는 0 이 된다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(0)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId)

                validGoldLedgers.size shouldBe 0
            }
        }
    }

    given("남은 금보다 많은 양의 차감 금이 주어지고") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(userId, 100000000)
        When("차감하면") {
            val consumeGold = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
            then("금이 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("남은 금 50000 보다 적은 양의 차감 45000 금이 주어지고(gold_ledger 3개 중 3개 영향)") {
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
            then("1개의 유효한 충전금이 남고 5000 금이다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(5000)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId)

                validGoldLedgers.size shouldNotBe 0
                validGoldLedgers.size shouldBe 1
                val onlyLedger = validGoldLedgers.first()
                val remainingGold = onlyLedger.chargedGold.gold - onlyLedger.usedGold
                remainingGold.amount shouldBe 5000
            }
        }
    }

    given("남은 금 50000 보다 적은 양의 차감 15000 금이 주어지고(gold_ledger 3개 중 1개 영향)") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(userId, 15000)
        When("차감하면") {
            val result = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
                .execute()
            then("2개의 유효한 충전금이 남고 35000 금이다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(35000)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(userId)

                validGoldLedgers.size shouldNotBe 0
                validGoldLedgers.size shouldBe 2

                val remainingGold = validGoldLedgers.sumOf { it.chargedGold.gold.amount - it.usedGold.amount }
                remainingGold shouldBe 35000
            }
        }
    }

    given("(ledger 1개) 남은 금 50000 보다 적은 양의 차감 15000 금이 주어지고(gold_ledger 1개 중 1개 영향)") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(2, 15000)
        When("차감하면") {
            val result = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
                .execute()
            then("2개의 유효한 충전금이 남고 35000 금이다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(35000)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(2)

                validGoldLedgers.size shouldNotBe 0
                validGoldLedgers.size shouldBe 1

                val remainingGold = validGoldLedgers.sumOf { it.chargedGold.gold.amount - it.usedGold.amount }
                remainingGold shouldBe 35000
            }
        }
    }

    given("(ledger 1개) 남은 금 50000 과 동일한 차감 50000 금이 주어지고(gold_ledger 1개 중 1개 영향)") {
        val goldLedgerRepository = FakeGoldLedgerRepository()
        val goldBalanceRepository = FakeGoldBalanceRepository()
        val request = ConsumingGoldValue(2, 50000)
        When("차감하면") {
            val result = ConsumingGold(
                request,
                goldLedgerRepository,
                goldBalanceRepository
            )
                .execute()
            then("2개의 유효한 충전금이 남고 35000 금이다.") {
                result.currentGoldBalanceAmount shouldBe GoldAmount(0)

                val validGoldLedgers =
                    goldLedgerRepository.findValidOneByUserIdOrderByExpired(2)

                validGoldLedgers.size shouldBe 0

                val remainingGold = validGoldLedgers.sumOf { it.chargedGold.gold.amount - it.usedGold.amount }
                remainingGold shouldBe 0
            }
        }
    }

    given("금이 0인 user 가 주어지고") {
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
            then("금이 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

    given("금 거래 내역이 없는 user 가 주어지고") {
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
            then("금이 부족함을 알린다") {
                shouldThrow<InsufficientGoldException> { consumeGold.execute() }
            }
        }
    }

}) {
    companion object {
        private val userId = 1L
    }
}
