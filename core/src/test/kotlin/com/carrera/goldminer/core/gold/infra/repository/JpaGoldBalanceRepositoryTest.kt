package com.carrera.goldminer.core.gold.infra.repository

import com.carrera.goldminer.core.gold.infra.entity.GoldBalanceJpaEntity
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JpaGoldBalanceRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var jpaGoldBalanceRepository: JpaCrudGoldBalanceRepository

    override suspend fun beforeSpec(spec: Spec) {
        val jpaGoldBalances = listOf(
            GoldBalanceJpaEntity(
                id = 0L,
                userId = 1L,
                goldAmount = 50000
            ),
            GoldBalanceJpaEntity(
                id = 0L,
                userId = 2L,
                goldAmount = 15000
            )
        )

        jpaGoldBalanceRepository.saveAll(jpaGoldBalances)
    }

    init {
        this.given("정상 사용자 ID 가 주어지고") {
            val userId = 1L
            When("금 잔고를 검색하면") {
                val result = jpaGoldBalanceRepository.findByUserId(userId)
                then("사용자의 잔고가 검색된다.") {
                    requireNotNull(result)
                    result.userId shouldBe userId
                }
            }
        }

        this.given("없는 사용자 ID가 주어지고") {
            val userId = 111L
            When("금 잔고를 검색하면") {
                val result = jpaGoldBalanceRepository.findByUserId(userId)
                then("아무것도 검색되지 않는다.") {
                    result shouldBe null
                }
            }
        }
    }
}
