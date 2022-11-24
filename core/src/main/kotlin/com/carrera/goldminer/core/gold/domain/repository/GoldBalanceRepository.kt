package com.carrera.goldminer.core.gold.domain.repository

import com.carrera.goldminer.core.gold.domain.entity.GoldBalance
import com.carrera.goldminer.core.gold.domain.value.GoldAmount

interface GoldBalanceRepository {
    fun save(goldBalance: GoldBalance): GoldBalance
    fun findByUserId(userId: Long): GoldBalance?
}

class FakeGoldBalanceRepository : GoldBalanceRepository {
    override fun save(goldBalance: GoldBalance): GoldBalance {
        return if (goldBalance.id == 0L) {
            val newGoldBalance = GoldBalance(
                (goldBalances.maxOfOrNull { it.id } ?: 0) + 1,
                goldBalance.userId,
                goldBalance.gold
            )
            goldBalances.add(newGoldBalance)

            goldBalance
        } else {
            goldBalances.removeIf { it.id == goldBalance.id }
            goldBalances.add(goldBalance)

            goldBalance
        }
    }

    override fun findByUserId(userId: Long): GoldBalance? {
        return goldBalances.firstOrNull { it.userId == userId }
    }

    private val goldBalances = mutableListOf(
        GoldBalance(
            id = 1,
            userId = 1,
            gold = GoldAmount(50000)
        ),
        GoldBalance(
            id = 2,
            userId = 2,
            gold = GoldAmount(50000)
        ),
        GoldBalance(
            id = 3,
            userId = 3,
            gold = GoldAmount(0)
        )
    )
}
