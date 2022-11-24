package com.carrera.goldminer.core.gold.domain.entity

import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class GoldBalance(
    val id: Long = 0L,
    val userId: Long,
    val gold: GoldAmount,
    val version: Long = 1L,
) {
    fun changeBalance(changedGold: GoldAmount): GoldBalance {
        return copy(
            gold = GoldAmount(gold.amount + changedGold.amount)
        )
    }

    private fun copy(
        id: Long = this.id,
        userId: Long = this.userId,
        gold: GoldAmount = this.gold,
    ) = GoldBalance(
        id,
        userId,
        gold,
        this.version
    )
}
