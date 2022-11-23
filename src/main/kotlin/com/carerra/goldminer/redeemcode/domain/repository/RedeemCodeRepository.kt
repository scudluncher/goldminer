package com.carerra.goldminer.redeemcode.domain.repository

import com.carerra.goldminer.goldchange.domain.value.Gold
import com.carerra.goldminer.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

interface RedeemCodeRepository {
    fun findByCode(code: String): RedeemCode?
    fun save(redeemCode: RedeemCode): RedeemCode
    fun findByCodeAndRedeemed(code: String, redeemed: Boolean): RedeemCode?
    //todo fun findWithPagination
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
                redeemCode.gold,
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

    override fun findByCodeAndRedeemed(code: String, redeemed: Boolean): RedeemCode? {
        return redeemCodes.firstOrNull { it.code == code && it.redeemed == redeemed }
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)

    private val redeemCodes = mutableListOf(
        RedeemCode(
            1,
            "7K2J77US6V",
            Gold(100, aMonthLater),
            aMonthLater
        ),
        RedeemCode(
            2,
            "R1VQPB43RQ",
            Gold(200, aMonthLater),
            aMonthLater
        ),
        RedeemCode(
            3,
            "2BTQEAYUKN",
            Gold(300, aWeekLater),
            aWeekLater
        ), RedeemCode(
            4,
            "DN10U2KVLR",
            Gold(400, aMonthLater),
            aWeekLater
        )
    )
}
