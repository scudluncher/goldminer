package com.carrera.goldminer.api.goldledger.viewmodel

import com.carrera.goldminer.core.goldledger.domain.value.GoldAmount

class CurrentGoldViewModel(
    private val goldAmount: Long,
) {
    constructor(gold: GoldAmount) : this(gold.amount)
}
