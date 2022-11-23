package com.carerra.goldminer.goldledger.viewmodel

import com.carerra.goldminer.goldledger.domain.value.GoldAmount

class CurrentGoldViewModel(
    private val goldAmount: Long,
) {
    constructor(gold: GoldAmount) : this(gold.amount)
}
