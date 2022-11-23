package com.carrera.goldminer.api.redeemcode.service

import com.carrera.goldminer.api.goldledger.usecase.usableAmountOf
import com.carrera.goldminer.api.redeemcode.usecase.ChargingValue
import com.carrera.goldminer.core.goldledger.infra.repository.JpaGoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.infra.entity.RedeemCodeJpaEntity
import com.carrera.goldminer.core.redeemcode.infra.repository.JpaCrudRedeemCodeRepository
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.time.ZonedDateTime

@SpringBootTest
class RedeemCodeServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)
    override suspend fun beforeSpec(spec: Spec) {
        val jpaRedeemCodes = listOf(
            RedeemCodeJpaEntity(
                id = 0L,
                code = "7K2J77US6V",
                gold = 1,
                goldExpiredBy = ZonedDateTime.now().plusDays(1),
                expiredBy = ZonedDateTime.now().plusDays(1),
                redeemed = false
            )
        )

        jpaRedeemCodeRepository.saveAll(jpaRedeemCodes)
    }

    @Autowired
    private lateinit var jpaRedeemCodeRepository: JpaCrudRedeemCodeRepository

    @Autowired
    private lateinit var jpaGoldLedgerRepository: JpaGoldLedgerRepository

    @Autowired
    private lateinit var redeemCodeService: RedeemCodeService

    init {
        this.given("하나의 유효 리딤코드가 주어지고") {

            val code = "7K2J77US6V"
            val chargingValue = ChargingValue(
                code,
                1L
            )
            When("동시에 여러 요청으로 해당 리딤코드를 활성화 하려 하면") {
                var exceptionCount = 0
                coroutineScope {
                    (0..4).map { // 5회 실행
                        launch(Dispatchers.Default) {
                            try {
                                redeemCodeService.chargeGoldWithCode(chargingValue)
                            } catch (e: ObjectOptimisticLockingFailureException) {
                                println(e)
                                exceptionCount++
                            }
                        }
                    }
                }
                then("한번만 활성화 된다") {
                    val totalSum = jpaGoldLedgerRepository.findValidOneByUserIdOrderByExpired(1).usableAmountOf()
                    totalSum shouldBe 1

                    val usedCode = jpaRedeemCodeRepository.findByCode(code)
                    usedCode?.redeemed shouldBe true

                    exceptionCount shouldBe 4
                }
            }
        }
    }
}
