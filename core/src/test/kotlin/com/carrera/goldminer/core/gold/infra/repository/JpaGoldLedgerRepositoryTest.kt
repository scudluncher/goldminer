package com.carrera.goldminer.core.gold.infra.repository

import com.carrera.goldminer.core.gold.infra.entity.GoldLedgerJpaEntity
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
                userId = 1L,
                chargedGoldAmount = 25000,
                chargedGoldExpiredBy = tomorrow,
                usedGoldAmount = 10000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                userId = 1L,
                chargedGoldAmount = 20000,
                chargedGoldExpiredBy = aMonthLater,
                usedGoldAmount = 0
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                userId = 1L,
                chargedGoldAmount = 30000,
                chargedGoldExpiredBy = twoWeeksLater,
                usedGoldAmount = 15000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                userId = 1L,
                chargedGoldAmount = 30000,
                chargedGoldExpiredBy = aWeekBefore,
                usedGoldAmount = 15000
            ),
            GoldLedgerJpaEntity(
                id = 0L,
                userId = 2L,
                chargedGoldAmount = 25000,
                chargedGoldExpiredBy = aWeekLater,
                usedGoldAmount = 10000
            ),
        )

        jpaGoldLedgerRepository.saveAll(jpaGoldLedgers)
    }

    init {
        this.given("?????? ????????? ID??? ?????? ????????? ????????????") {
            val userId = 1L
            val now = ZonedDateTime.now()
            When("??? ????????? ????????????") {
                val result = jpaGoldLedgerRepository.findByUserIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(
                    userId,
                    now
                )
                then("?????? ???????????? ??? ???????????? ??????, ???????????? ?????? ??? ????????? ?????? ?????????????????? ?????????.") {
                    result.size shouldNotBe 0
                    result.first().chargedGoldExpiredBy shouldBeBefore result.last().chargedGoldExpiredBy
                    result.all { it.chargedGoldAmount > it.usedGoldAmount } shouldBe true
                    result.all { it.userId == userId } shouldBe true
                }
            }
        }

        this.given("?????? ????????? ID??? ?????? ????????? ????????????") {
            val userId = 111L
            val now = ZonedDateTime.now()
            When("??? ????????? ????????????") {
                val result = jpaGoldLedgerRepository.findByUserIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(
                    userId,
                    now
                )
                then("???????????? ???????????? ?????????.") {
                    result.size shouldBe 0
                }
            }
        }
    }
}

