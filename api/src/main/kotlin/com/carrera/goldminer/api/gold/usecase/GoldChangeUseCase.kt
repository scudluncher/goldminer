package com.carrera.goldminer.api.gold.usecase

import com.carrera.goldminer.api.gold.value.GoldChangeResult

interface GoldChangeUseCase {
    fun execute(): GoldChangeResult
}
