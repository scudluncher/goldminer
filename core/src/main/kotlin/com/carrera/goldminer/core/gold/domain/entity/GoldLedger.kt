package com.carrera.goldminer.core.gold.domain.entity

import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode

class GoldLedger(
    val id: Long = 0L,
    val userId: Long,
    val chargedGold: ChargedGold,
    val usedGold: GoldAmount = GoldAmount(0),
) {
    constructor(redeemCode: RedeemCode, userId: Long) : this(
        userId = userId,
        chargedGold = redeemCode.includedChargedGold
    )

    fun allConsumed(): GoldLedger {
        return copy(
            usedGold = chargedGold.gold
        )
    }

    fun deductPartially(amount: GoldAmount): GoldLedger {
        return copy(
            usedGold = usedGold + amount
        )
    }

    private fun copy(
        id: Long = this.id,
        userId: Long = this.userId,
        chargedGold: ChargedGold = this.chargedGold,
        usedGold: GoldAmount = this.usedGold,
    ) = GoldLedger(
        id,
        userId,
        chargedGold,
        usedGold,
    )
}

fun List<GoldLedger>.usableAmountAsLong(): Long {
    return sumOf { (it.chargedGold.gold - it.usedGold).amount }
}
