package com.carerra.goldminer.goldledger.domain.entity

import com.carerra.goldminer.goldledger.domain.value.Gold
import com.carerra.goldminer.goldledger.domain.value.GoldAmount

class GoldLedger(
    val id: Long,
    val ownerId: Long,
    val chargedGold: Gold,
    val usedGold: GoldAmount = GoldAmount(0),
) {
    fun allConsumed(): GoldLedger {
        return copy(
            usedGold = GoldAmount(chargedGold.amount)
        )
    }

    fun partiallyConsumed(lastResidual: Long): GoldLedger {
        return copy(
            usedGold = GoldAmount((chargedGold.amount - lastResidual) + usedGold.amount)
        )
    }

    private fun copy(
        id: Long = this.id,
        ownerId: Long = this.ownerId,
        chargedGold: Gold = this.chargedGold,
        usedGold: GoldAmount = this.usedGold,
    ) = GoldLedger(
        id,
        ownerId,
        chargedGold,
        usedGold,
    )
}


