package com.carrera.goldminer.api.redeemcode.viewmodel

import com.carrera.goldminer.api.redeemcode.value.ChargedGoldValue
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

class RedeemCodeViewModel(
    private val code: String,
    private val codeExpiredBy: ZonedDateTime,
    private val redeemed: String,
    private val chargedGold: ChargedGoldValue,
) {
    constructor(redeemCode: RedeemCode) : this(
        redeemCode.code,
        redeemCode.expiredBy,
        if (redeemCode.redeemed) "Y" else "N",
        chargedGold = ChargedGoldValue(
            redeemCode.includedChargedGold.gold.amount,
            redeemCode.includedChargedGold.expiredBy
        )
    )
}
