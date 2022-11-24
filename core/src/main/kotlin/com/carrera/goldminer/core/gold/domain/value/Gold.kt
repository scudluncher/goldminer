package com.carrera.goldminer.core.gold.domain.value

import java.time.ZonedDateTime

class ChargedGold(
    val gold: GoldAmount,
    val expiredBy: ZonedDateTime,
)

class GoldAmount(
    val amount: Long,
) {
    operator fun plus(gold: GoldAmount): GoldAmount {
        return GoldAmount(this.amount + gold.amount)
    }

    operator fun minus(gold: GoldAmount): GoldAmount {
        return GoldAmount(this.amount - gold.amount)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GoldAmount

        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        return amount.hashCode()
    }
}
