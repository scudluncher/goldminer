package com.carrera.goldminer.core.redeemcode.repository

import com.carrera.goldminer.core.redeemcode.infra.entity.RedeemCodeJpaEntity
import com.carrera.goldminer.core.redeemcode.infra.repository.JpaCrudRedeemCodeRepository
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@SpringBootTest
class JpaRedeemCodeRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var jpaRedeemCodeRepository: JpaCrudRedeemCodeRepository

    @Autowired
    private lateinit var transactionWrapper: TransactionWrapper

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    override suspend fun beforeSpec(spec: Spec) {
        val jpaRedeemCodes = listOf(
            RedeemCodeJpaEntity(
                id = 0L,
                code = "7K2J77US6V",
                gold = 100,
                goldExpiredBy = aMonthLater,
                expiredBy = aWeekLater,
                redeemed = false
            ),
            RedeemCodeJpaEntity(
                id = 0L,
                code = "051CESFOYF",
                gold = 500,
                goldExpiredBy = aMonthLater,
                expiredBy = aWeekLater,
                redeemed = false
            ),
            RedeemCodeJpaEntity(
                id = 0L,
                code = "R1VQPB43RQ",
                gold = 1000,
                goldExpiredBy = aWeekLater,
                expiredBy = aWeekLater,
                redeemed = true
            ),
            RedeemCodeJpaEntity(
                id = 0L,
                code = "2BTQEAYUKN",
                gold = 150,
                goldExpiredBy = aWeekBefore,
                expiredBy = aWeekBefore,
                redeemed = false
            ),
            RedeemCodeJpaEntity(
                id = 0L,
                code = "NINQM6XEHY",
                gold = 300,
                goldExpiredBy = aWeekBefore,
                expiredBy = aWeekBefore,
                redeemed = true
            ),
        )

        jpaRedeemCodeRepository.saveAll(jpaRedeemCodes)
    }

    init {
        this.given("발행된 코드가 주어지고") {
            val code = "NINQM6XEHY"
            When("검색하면") {
                val result = jpaRedeemCodeRepository.findByCode(code)
                then("동일한 코드명의 충전 코드가 검색된다.") {
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

        this.given("유효한 코드가 주어지고") {
            val code = "7K2J77US6V"
            When("검색하면") {
                val result = transactionWrapper.runWithTransaction {
                    jpaRedeemCodeRepository.findByCodeAndRedeemedAndExpiredByAfter(code)
                }
                then("만료기한이 남아있고, 충전되지 않았으며 입력한 코드와 동일한 충전코드가 검색된다.") {
                    result shouldNotBe null
                    requireNotNull(result)
                    result.code shouldBe code
                    result.expiredBy shouldBeAfter ZonedDateTime.now()

                }
            }
        }

        this.given("충전된 코드가 주어지고") {
            val code = "NINQM6XEHY"
            When("검색하면") {
                val result = transactionWrapper.runWithTransaction {
                    jpaRedeemCodeRepository.findByCodeAndRedeemedAndExpiredByAfter(code)
                }
                then("아무것도 나오지 않는다.") {
                    result shouldBe null
                }
            }
        }

        this.given("모든 충전 코드에 대해") {
            When("검색하면") {
                val result = jpaRedeemCodeRepository.findAllBy(Pageable.unpaged())
                then("모든 충전 코드가 검색된다") {
                    result.size shouldBeGreaterThanOrEqual 0
                }
            }
        }
    }
}

@Component
class TransactionWrapper {

    @Transactional
    fun <R> runWithTransaction(transactionRequiredFunction: () -> R): R {
        return transactionRequiredFunction()
    }
}
