package com.carrera.goldminer.core.redeemcode.domain.value

import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode

class RedeemingResult(
    val consumedCode: RedeemCode,
    val newLedger: GoldLedger,
)
