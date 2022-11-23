package com.carrera.goldminer.api.goldledger.viewmodel

import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import java.time.ZonedDateTime

class ChargedGoldViewModel(
    private val chargedGold: Long,
    private val expiredBy: ZonedDateTime,
) {
    constructor(goldLedger:GoldLedger): this(
        chargedGold = goldLedger.chargedGold.amount,
        expiredBy = goldLedger.chargedGold.expiredBy
    )
}
