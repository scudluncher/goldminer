package com.carrera.goldminer.api.gold.value

import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import java.time.ZonedDateTime

class GoldChangeResult(
    val currentGoldBalanceAmount: GoldAmount,
    val changedGold: GoldAmount,
    val expiredBy: ZonedDateTime? = null,
)
