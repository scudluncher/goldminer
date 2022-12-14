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
        this.given("????????? ????????? ????????????") {
            val code = "NINQM6XEHY"
            When("????????????") {
                val result = jpaRedeemCodeRepository.findByCode(code)
                then("????????? ???????????? ?????? ????????? ????????????.") {
                    result?.code shouldBe code
                }
            }
        }

        this.given("???????????? ????????? ????????????") {
            val code = "123456ABCD"
            When("????????????") {
                val result = jpaRedeemCodeRepository.findByCode(code)
                then("???????????? ????????? ?????????.") {
                    result shouldBe null
                }
            }
        }

        this.given("????????? ????????? ????????????") {
            val code = "7K2J77US6V"
            When("????????????") {
                val result = transactionWrapper.runWithTransaction {
                    jpaRedeemCodeRepository.findByCodeAndRedeemedAndExpiredByAfter(code)
                }
                then("??????????????? ????????????, ???????????? ???????????? ????????? ????????? ????????? ??????????????? ????????????.") {
                    result shouldNotBe null
                    requireNotNull(result)
                    result.code shouldBe code
                    result.expiredBy shouldBeAfter ZonedDateTime.now()

                }
            }
        }

        this.given("????????? ????????? ????????????") {
            val code = "NINQM6XEHY"
            When("????????????") {
                val result = transactionWrapper.runWithTransaction {
                    jpaRedeemCodeRepository.findByCodeAndRedeemedAndExpiredByAfter(code)
                }
                then("???????????? ????????? ?????????.") {
                    result shouldBe null
                }
            }
        }

        this.given("?????? ?????? ????????? ??????") {
            When("????????????") {
                val result = jpaRedeemCodeRepository.findAllBy(Pageable.unpaged())
                then("?????? ?????? ????????? ????????????") {
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
