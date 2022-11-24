package com.carrera.goldminer.core.redeemcode.domain.entity

import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.redeemcode.domain.value.RedeemingResult
import java.time.ZonedDateTime

class RedeemCode(
    val id: Long = 0L,
    val code: String,
    val includedChargedGold: ChargedGold,
    val expiredBy: ZonedDateTime,
    val redeemed: Boolean = false,
    val version: Long? = null,
) {
    private fun copy(
        id: Long = this.id,
        code: String = this.code,
        chargedGold: ChargedGold = this.includedChargedGold,
        expiredBy: ZonedDateTime = this.expiredBy,
        redeemed: Boolean = this.redeemed,
    ) = RedeemCode(
        id,
        code,
        chargedGold,
        expiredBy,
        redeemed,
        this.version
    )

    fun redeem(userId: Long): RedeemingResult {
        return RedeemingResult(
            copy(redeemed = true),
            GoldLedger(this, userId)
        )
    }
}

