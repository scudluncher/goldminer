package com.carrera.goldminer.core.redeemcode.domain.entity

import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.value.Gold
import com.carrera.goldminer.core.redeemcode.domain.value.RedeemingResult
import java.time.ZonedDateTime

class RedeemCode(
    val id: Long = 0L,
    val code: String,
    val includedGold: Gold,
    val expiredBy: ZonedDateTime,
    val redeemed: Boolean = false,
) {
    private fun copy(
        id: Long = this.id,
        code: String = this.code,
        gold: Gold = this.includedGold,
        expiredBy: ZonedDateTime = this.expiredBy,
        redeemed: Boolean = this.redeemed,
    ) = RedeemCode(
        id,
        code,
        gold,
        expiredBy,
        redeemed
    )

    fun redeem(userId: Long): RedeemingResult {
        return RedeemingResult(
            copy(redeemed = true),
            GoldLedger(this, userId)
        )
    }
}

