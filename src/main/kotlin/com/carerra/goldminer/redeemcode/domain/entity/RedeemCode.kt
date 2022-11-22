package com.carerra.goldminer.redeemcode.domain.entity

import com.carerra.goldminer.goldchange.domain.entity.Gold
import java.time.ZonedDateTime

class RedeemCode(
    val id: Long = 0L,
    val code: String,
    val gold: Gold,
    val expiredBy: ZonedDateTime,
    val redeemed: Boolean = false,
) {
    private fun copy(
        id: Long = this.id,
        code: String = this.code,
        gold: Gold = this.gold,
        expiredBy: ZonedDateTime = this.expiredBy,
        redeemed: Boolean = this.redeemed,
    ) = RedeemCode(
        id,
        code,
        gold,
        expiredBy,
        redeemed
    )

    private fun redeem(): RedeemCode {
        return copy(redeemed = true)
    }
}
