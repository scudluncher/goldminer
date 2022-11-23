package com.carrera.goldminer.api.goldledger.viewmodel

import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount

class RedeemResultViewModel(
    private val currentGold: CurrentGoldViewModel,
    private val chargedGold: ChargedGoldViewModel,
) {
    constructor(currentGold: GoldAmount, chargedGold: GoldLedger) : this(
        CurrentGoldViewModel(currentGold),
        ChargedGoldViewModel(chargedGold)
    )
}
