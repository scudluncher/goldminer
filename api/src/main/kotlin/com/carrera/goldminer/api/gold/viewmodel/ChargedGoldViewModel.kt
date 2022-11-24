package com.carrera.goldminer.api.gold.viewmodel

import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import java.time.ZonedDateTime

class ChargedGoldViewModel(
    private val chargedGold: Long,
    private val expiredBy: ZonedDateTime,
) {
    constructor(changedGold: GoldAmount, expiredBy: ZonedDateTime) : this(
        chargedGold = changedGold.amount,
        expiredBy = expiredBy
    )
}
