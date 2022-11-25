package com.carrera.goldminer.core.redeemcode.domain.repository

import com.carrera.goldminer.core.common.value.PaginatedList
import com.carrera.goldminer.core.common.value.PagingCondition
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

interface RedeemCodeRepository {
    fun findByCode(code: String): RedeemCode?
    fun save(redeemCode: RedeemCode): RedeemCode
    fun findValidOneByCodeWithLock(code: String): RedeemCode?
    fun findAll(pagingCondition: PagingCondition): PaginatedList<RedeemCode>
}

class FakeRedeemCodeRepository : RedeemCodeRepository {
    override fun findByCode(code: String): RedeemCode? {
        return redeemCodes.firstOrNull { it.code == code }
    }

    override fun save(redeemCode: RedeemCode): RedeemCode {
        return if (redeemCode.id == 0L) {
            val newRedeemCode = RedeemCode(
                (redeemCodes.maxOfOrNull { it.id } ?: 0) + 1,
                redeemCode.code,
                redeemCode.includedChargedGold,
                redeemCode.expiredBy,
                redeemCode.redeemed,
                redeemCode.version
            )
            redeemCodes.add(newRedeemCode)

            newRedeemCode
        } else {
            redeemCodes.removeIf { it.id == redeemCode.id }
            redeemCodes.add(redeemCode)

            redeemCode
        }
    }

    override fun findValidOneByCodeWithLock(code: String): RedeemCode? {
        return redeemCodes.firstOrNull { it.code == code && !it.redeemed && it.expiredBy.isAfter(ZonedDateTime.now()) }
    }

    override fun findAll(pagingCondition: PagingCondition): PaginatedList<RedeemCode> {
        return if (pagingCondition.page == null || pagingCondition.perPage == null) {
            PaginatedList(redeemCodes,redeemCodes.size)
        } else {
            val from = (pagingCondition.page - 1) * pagingCondition.perPage
            val to = from + pagingCondition.perPage
            val results: List<RedeemCode> = redeemCodes.subList(from, to)

            PaginatedList(results, results.size)
        }
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    private val redeemCodes = mutableListOf(
        RedeemCode(
            id = 1,
            code = "7K2J77US6V",
            includedChargedGold = ChargedGold(GoldAmount(100), aMonthLater),
            expiredBy = aMonthLater
        ),
        RedeemCode(
            id = 2,
            code = "R1VQPB43RQ",
            includedChargedGold = ChargedGold(GoldAmount(200), aMonthLater),
            expiredBy = aMonthLater
        ),
        RedeemCode(
            id = 3,
            code = "2BTQEAYUKN",
            includedChargedGold = ChargedGold(GoldAmount(300), aWeekLater),
            expiredBy = aWeekLater
        ),
        RedeemCode(
            id = 4,
            code = "DN10U2KVLR",
            includedChargedGold = ChargedGold(GoldAmount(400), aMonthLater),
            expiredBy = aWeekLater
        ),
        RedeemCode(
            id = 5,
            code = "INVALID842",
            includedChargedGold = ChargedGold(GoldAmount(400), aWeekBefore),
            expiredBy = aWeekBefore
        ),
        RedeemCode(
            id = 6,
            code = "A4TV7TGBH3",
            includedChargedGold = ChargedGold(GoldAmount(400), aWeekBefore),
            expiredBy = aWeekBefore,
            redeemed = true
        )
    )
}
