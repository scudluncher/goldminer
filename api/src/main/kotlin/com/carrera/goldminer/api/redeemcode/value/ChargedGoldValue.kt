package com.carrera.goldminer.api.redeemcode.value

import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import java.time.ZonedDateTime

class ChargedGoldValue(
    private val goldAmount: Long,
    private val goldExpiredBy: ZonedDateTime,
) {
    constructor(chargedGold: ChargedGold) : this(
        goldAmount = chargedGold.gold.amount,
        goldExpiredBy = chargedGold.expiredBy
    )
}
