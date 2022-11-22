package com.carerra.goldminer.redeemcode.viewmodel

import com.carerra.goldminer.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime

class RedeemCodeIssuedViewModel(
    private val code: String,
    private val goldAmount: ULong,
    private val goldExpiredBy: ZonedDateTime,
    private val codeExpiredBy: ZonedDateTime,
) {
    constructor(redeemCode: RedeemCode) : this(
        code = redeemCode.code,
        goldAmount = redeemCode.gold.amount,
        goldExpiredBy = redeemCode.gold.expiredBy,
        codeExpiredBy = redeemCode.expiredBy
    )
}
