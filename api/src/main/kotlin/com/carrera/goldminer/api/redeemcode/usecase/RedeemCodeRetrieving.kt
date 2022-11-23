package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository

class RedeemCodeRetrieving(
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    fun execute(): List<RedeemCode> {
        TODO()
    }
}
