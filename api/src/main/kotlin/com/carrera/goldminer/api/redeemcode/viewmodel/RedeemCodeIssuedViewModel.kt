package com.carrera.goldminer.api.redeemcode.viewmodel

import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

class RedeemCodeIssuedViewModel(
    private val code: String,
    private val goldAmount: Long,
    private val goldExpiredBy: ZonedDateTime,
    private val codeExpiredBy: ZonedDateTime,
) {
    constructor(redeemCode: RedeemCode) : this(
        code = redeemCode.code,
        goldAmount = redeemCode.includedChargedGold.gold.amount,
        goldExpiredBy = redeemCode.includedChargedGold.expiredBy,
        codeExpiredBy = redeemCode.expiredBy
    )
}
