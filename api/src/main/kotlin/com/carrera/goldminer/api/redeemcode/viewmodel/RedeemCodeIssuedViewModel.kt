package com.carrera.goldminer.api.redeemcode.viewmodel

import com.carrera.goldminer.api.redeemcode.value.ChargedGoldValue
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

class RedeemCodeIssuedViewModel(
    private val code: String,
    private val codeExpiredBy: ZonedDateTime,
    private val chargedGold: ChargedGoldValue,
) {
    constructor(redeemCode: RedeemCode) : this(
        code = redeemCode.code,
        codeExpiredBy = redeemCode.expiredBy,
        chargedGold = ChargedGoldValue(
            redeemCode.includedChargedGold.gold.amount,
            redeemCode.includedChargedGold.expiredBy
        )
    )
}

