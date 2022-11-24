package com.carrera.goldminer.api.gold.viewmodel

import com.carrera.goldminer.api.gold.value.GoldChangeResult

class RedeemResultViewModel(
    private val currentGold: CurrentGoldViewModel,
    private val chargedGold: ChargedGoldViewModel,
) {
    constructor(goldChangeResult: GoldChangeResult) : this(
        CurrentGoldViewModel(goldChangeResult.currentGoldBalanceAmount),
        ChargedGoldViewModel(goldChangeResult.changedGold, goldChangeResult.expiredBy!!)
    )
}
