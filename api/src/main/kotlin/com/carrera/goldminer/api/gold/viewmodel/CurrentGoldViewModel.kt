package com.carrera.goldminer.api.gold.viewmodel

import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class CurrentGoldViewModel(
    private val goldAmount: Long,
) {
    constructor(gold: GoldAmount) : this(gold.amount)
}
