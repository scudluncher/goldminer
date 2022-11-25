package com.carrera.goldminer.api.common.request

import com.carrera.goldminer.core.common.value.PagingCondition
import javax.validation.constraints.Positive

class PagingRequest(
    @field:Positive
    val page: Int?,
    @field:Positive @field:Positive
    val perPage: Int?,
) {
    fun toPagingCondition(): PagingCondition {
        return PagingCondition(
            page,
            perPage
        )
    }
}
