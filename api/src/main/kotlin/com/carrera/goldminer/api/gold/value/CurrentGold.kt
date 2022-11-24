package com.carrera.goldminer.api.gold.value

import com.carrera.goldminer.core.gold.domain.value.GoldAmount

class CurrentGold(
    val currentGoldBalance: GoldAmount,
    val version: Long,
)

