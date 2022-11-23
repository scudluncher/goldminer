package com.carerra.goldminer.redeemcode.infra.repository

import com.carerra.goldminer.redeemcode.infra.entity.RedeemCodeJpaEntity
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime

@SpringBootTest
class JpaCrudRedeemCodeRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var jpaRedeemCodeRepository: JpaCrudRedeemCodeRepository

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    override suspend fun beforeSpec(spec: Spec) {
        val jpaRedeemCodes = listOf(
            RedeemCodeJpaEntity(
                0L,
                "7K2J77US6V",
                100,
                aMonthLater,
                aWeekLater,
                false
            ),
            RedeemCodeJpaEntity(
                0L,
                "051CESFOYF",
                500,
                aMonthLater,
                aWeekLater,
                false
            ),
            RedeemCodeJpaEntity(
                0L,
                "R1VQPB43RQ",
                1000,
                aWeekLater,
                aWeekLater,
                true
            ),
            RedeemCodeJpaEntity(
                0L,
                "2BTQEAYUKN",
                150,
                aWeekBefore,
                aWeekBefore,
                false
            ),
            RedeemCodeJpaEntity(
                0L,
                "NINQM6XEHY",
                300,
                aWeekBefore,
                aWeekBefore,
                true
            ),
        )

        jpaRedeemCodeRepository.saveAll(jpaRedeemCodes)
    }

    init {
        this.given("발행된 코드가 주어지고") {
            val code = "NINQM6XEHY"
            When("검색하면") {
                val result = jpaRedeemCodeRepository.findByCode(code)
                then("조건의 리딤 코드가 검색된다") {
                    result?.code shouldBe code
                }
            }
        }

        this.given("미발행된 코드가 주어지고") {
            val code = "123456ABCD"
            When("검색하면") {
                val result = jpaRedeemCodeRepository.findByCode(code)
                then("아무것도 나오지 않는다.") {
                    result shouldBe null
                }
            }
        }

        this.given("발행된 코드와 사용여부 값이 주어지고") {
            val code = "R1VQPB43RQ"
            val redeemed = true
            When("검색하면") {
                val result = jpaRedeemCodeRepository.findByCodeAndRedeemed(code, redeemed)
                then("조건의 리딤 코드가 검색된다.") {
                    result?.code shouldBe code
                    result?.redeemed shouldBe redeemed
                }
            }
        }
    }
}
