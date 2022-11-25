package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.core.common.value.PaginatedList
import com.carrera.goldminer.core.common.value.PagingCondition
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository

class RedeemCodeRetrieving(
    private val pagingCondition: PagingCondition,
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    fun execute(): PaginatedList<RedeemCode> {
        return redeemCodeRepository.findAll(pagingCondition)
    }
}
