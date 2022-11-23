package com.carrera.goldminer.core.goldledger.domain.entity

import com.carrera.goldminer.core.goldledger.domain.value.Gold
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode

class GoldLedger(
    val id: Long = 0L,
    val ownerId: Long,
    val chargedGold: Gold,
    val usedGold: GoldAmount = GoldAmount(0),
) {
    constructor(redeemCode: RedeemCode, userId: Long) : this(
        ownerId = userId,
        chargedGold = redeemCode.includedGold
    )

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


