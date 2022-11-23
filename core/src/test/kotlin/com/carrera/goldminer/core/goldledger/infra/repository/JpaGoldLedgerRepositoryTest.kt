package com.carrera.goldminer.core.goldledger.infra.repository

import com.carrera.goldminer.core.goldledger.infra.entity.GoldLedgerJpaEntity
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime

@SpringBootTest
class JpaGoldLedgerRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var jpaGoldLedgerRepository: JpaCrudGoldLedgerRepository

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val twoWeeksLater = ZonedDateTime.now().plusWeeks(2)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)
    private val tomorrow = ZonedDateTime.now().plusDays(1)

    override suspend fun beforeSpec(spec: Spec) {
        val jpaGoldLedgers = listOf(
            GoldLedgerJpaEntity(
                id = 0L,
                ownerId = 1L,
                chargedGoldAmount = 25000,
                chargedGoldExpiredBy = tomorrow,
                usedGoldAmount = 10000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                ownerId = 1L,
                chargedGoldAmount = 20000,
                chargedGoldExpiredBy = aMonthLater,
                usedGoldAmount = 0
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                ownerId = 1L,
                chargedGoldAmount = 30000,
                chargedGoldExpiredBy = twoWeeksLater,
                usedGoldAmount = 15000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                ownerId = 1L,
                chargedGoldAmount = 30000,
                chargedGoldExpiredBy = aWeekBefore,
                usedGoldAmount = 15000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                ownerId = 2L,
                chargedGoldAmount = 25000,
                chargedGoldExpiredBy = aWeekLater,
                usedGoldAmount = 10000
            ),
        )

        jpaGoldLedgerRepository.saveAll(jpaGoldLedgers)
    }

    init {
        this.given("owner Id와 현재 시간이 주어지고") {
            val ownerId = 1L
            val now = ZonedDateTime.now()
            When("검색하면") {
                val result = jpaGoldLedgerRepository.findByOwnerIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(
                    ownerId,
                    now
                )
                then("해당 owner의 다 사용되지 않고, 만기되지 않은 금 장부가 만기 오름차순으로 나온다") {
                    result.size shouldNotBe 0
                    result.first().chargedGoldExpiredBy shouldBeBefore result.last().chargedGoldExpiredBy
                    result.all { it.chargedGoldAmount > it.usedGoldAmount } shouldBe true
                    result.all { it.ownerId == ownerId} shouldBe true
                }
            }
        }
    }
}

