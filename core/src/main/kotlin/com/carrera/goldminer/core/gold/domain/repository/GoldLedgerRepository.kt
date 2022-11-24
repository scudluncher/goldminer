package com.carrera.goldminer.core.gold.domain.repository

import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import java.time.ZonedDateTime

interface GoldLedgerRepository {
    fun save(goldLedger: GoldLedger): GoldLedger
    fun findValidOneByUserIdOrderByExpired(userId: Long): List<GoldLedger>
}

class FakeGoldLedgerRepository : GoldLedgerRepository {
    override fun save(goldLedger: GoldLedger): GoldLedger {
        return if (goldLedger.id == 0L) {
            val newGoldLedger = GoldLedger(
                (chargedGoldLedgers.maxOfOrNull { it.id } ?: 0) + 1,
                goldLedger.userId,
                goldLedger.chargedGold,
                goldLedger.usedGold
            )
            chargedGoldLedgers.add(newGoldLedger)

            newGoldLedger
        } else {
            chargedGoldLedgers.removeIf { it.id == goldLedger.id }
            chargedGoldLedgers.add(goldLedger)

            goldLedger
        }
    }

    override fun findValidOneByUserIdOrderByExpired(userId: Long): List<GoldLedger> {
        val now = ZonedDateTime.now()
        return chargedGoldLedgers.filter {
            it.userId == userId
                    && it.chargedGold.expiredBy.isAfter(now)
                    && it.chargedGold.gold.amount != it.usedGold.amount
        }
            .sortedBy { it.chargedGold.expiredBy }
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val twoWeeksLater = ZonedDateTime.now().plusWeeks(2)
    private val tomorrow = ZonedDateTime.now().plusDays(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    private val chargedGoldLedgers = mutableListOf(
        // 1번 유저 도합 50000금 소유중
        // 기한 임박, 25000 충전, 10000 사용 --> 15000 잔여
        GoldLedger(
            1,
            1,
            ChargedGold(GoldAmount(25000), tomorrow),
            GoldAmount(10000)
        ),
        // 기한 한달 뒤, 20000 충전, 0 사용 --> 20000 잔여
        GoldLedger(
            2,
            1,
            ChargedGold(GoldAmount(20000), aMonthLater)
        ),
        // 기한 2주 뒤, 30000 충전, 15000 사용 --> 15000 잔여
        GoldLedger(
            3,
            1,
            ChargedGold(GoldAmount(30000), twoWeeksLater),
            GoldAmount(15000)
        ),
        // 기한 만료
        GoldLedger(
            4,
            1,
            ChargedGold(GoldAmount(30000), aWeekBefore),
            GoldAmount(15000)
        ),
        // 다른 유저
        GoldLedger(
            5,
            2,
            ChargedGold(GoldAmount(50000), aWeekLater)
        ),
        // 3번 유저 금 모두 소모해서 0인
        GoldLedger(
            6,
            3,
            ChargedGold(GoldAmount(50000), aWeekLater),
            GoldAmount(50000)
        ),
    )
}
