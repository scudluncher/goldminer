package com.carrera.goldminer.core.goldledger.domain.repository

import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.value.Gold
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount
import java.time.ZonedDateTime

interface GoldLedgerRepository {
    fun save(goldLedger: GoldLedger): GoldLedger
    fun findValidOneByUserIdOrderByExpired(ownerId: Long): List<GoldLedger>
}

class FakeGoldLedgerRepository : GoldLedgerRepository {
    override fun save(goldLedger: GoldLedger): GoldLedger {
        if (goldLedger.id == 0L) {
            val newGoldLedger = GoldLedger(
                (goldLedgers.maxOfOrNull { it.id } ?: 0) + 1,
                goldLedger.ownerId,
                goldLedger.chargedGold,
                goldLedger.usedGold
            )
            goldLedgers.add(newGoldLedger)

            return newGoldLedger
        } else {
            goldLedgers.removeIf { it.id == goldLedger.id }
            goldLedgers.add(goldLedger)

            return goldLedger
        }
    }

    override fun findValidOneByUserIdOrderByExpired(ownerId: Long): List<GoldLedger> {
        val now = ZonedDateTime.now()
        return goldLedgers.filter {
            it.ownerId == ownerId
                    && it.chargedGold.expiredBy.isAfter(now)
                    && it.chargedGold.amount != it.usedGold.amount
        }
            .sortedBy { it.chargedGold.expiredBy }
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val twoWeeksLater = ZonedDateTime.now().plusWeeks(2)
    private val tomorrow = ZonedDateTime.now().plusDays(1)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    private val goldLedgers = mutableListOf(
        // 1번 유저 도합 50000골드 소유중
        // 기한 임박, 25000 충전, 10000 사용 --> 15000 잔여
        GoldLedger(
            1,
            1,
            Gold(25000, tomorrow),
            GoldAmount(10000)
        ),
        // 기한 한달 뒤, 20000 충전, 0 사용 --> 20000 잔여
        GoldLedger(
            2,
            1,
            Gold(20000, aMonthLater)
        ),
        // 기한 2주 뒤, 30000 충전, 15000 사용 --> 15000 잔여
        GoldLedger(
            3,
            1,
            Gold(30000, twoWeeksLater),
            GoldAmount(15000)
        ),
        // 기한 만료
        GoldLedger(
            4,
            1,
            Gold(30000, aWeekBefore),
            GoldAmount(15000)
        ),
        // 다른 유저
        GoldLedger(
            5,
            2,
            Gold(50000, aWeekLater)
        ),
        // 3번 유저 골드 모두 소모해서 0인
        GoldLedger(
            5,
            3,
            Gold(50000, aWeekLater),
            GoldAmount(50000)
        ),
    )
}
