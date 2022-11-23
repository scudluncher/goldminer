package com.carrera.goldminer.core.redeemcode.domain.repository

import com.carrera.goldminer.core.goldledger.domain.value.Gold
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

interface RedeemCodeRepository {
    fun findByCode(code: String): RedeemCode?
    fun save(redeemCode: RedeemCode): RedeemCode
//    fun findByCodeAndRedeemed(code: String, redeemed: Boolean): RedeemCode? // todo() no usage so far?

    fun findValidOneByCode(code: String): RedeemCode?
}

class FakeRedeemCodeRepository : RedeemCodeRepository {
    override fun findByCode(code: String): RedeemCode? {
        return redeemCodes.firstOrNull { it.code == code }
    }

    override fun save(redeemCode: RedeemCode): RedeemCode {
        if (redeemCode.id == 0L) {
            val newRedeemCode = RedeemCode(
                (redeemCodes.maxOfOrNull { it.id } ?: 0) + 1,
                redeemCode.code,
                redeemCode.includedGold,
                redeemCode.expiredBy,
                redeemCode.redeemed
            )
            redeemCodes.add(newRedeemCode)

            return newRedeemCode
        } else {
            redeemCodes.removeIf { it.id == redeemCode.id }
            redeemCodes.add(redeemCode)

            return redeemCode
        }
    }

    override fun findValidOneByCode(code: String): RedeemCode? {
        return redeemCodes.firstOrNull { it.code == code && !it.redeemed && it.expiredBy.isAfter(ZonedDateTime.now()) }
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    private val redeemCodes = mutableListOf(
        RedeemCode(
            id = 1,
            code = "7K2J77US6V",
            includedGold = Gold(100, aMonthLater),
            expiredBy = aMonthLater
        ),
        RedeemCode(
            id = 2,
            code = "R1VQPB43RQ",
            includedGold = Gold(200, aMonthLater),
            expiredBy = aMonthLater
        ),
        RedeemCode(
            id = 3,
            code = "2BTQEAYUKN",
            includedGold = Gold(300, aWeekLater),
            expiredBy = aWeekLater
        ),
        RedeemCode(
            id = 4,
            code = "DN10U2KVLR",
            includedGold = Gold(400, aMonthLater),
            expiredBy = aWeekLater
        ),
        RedeemCode(
            id = 5,
            code = "INVALID842",
            includedGold = Gold(400, aWeekBefore),
            expiredBy = aWeekBefore
        ),
        RedeemCode(
            id = 5,
            code = "A4TV7TGBH3",
            includedGold = Gold(400, aWeekBefore),
            expiredBy = aWeekBefore,
            redeemed = true
        )
    )
}
