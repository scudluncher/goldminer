package com.carrera.goldminer.api.common.response

import com.carrera.goldminer.api.common.request.PagingRequest
import com.fasterxml.jackson.annotation.JsonInclude
import kotlin.math.ceil

class Paging(
    private val totalCount: Long,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val page: Int?,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val perPage: Int?,
) {
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val totalPage: Int? =
        if (page == null || perPage == null) null
        else if (totalCount == 0L) 0
        else ceil(totalCount.toDouble() / perPage).toInt()

    constructor(totalCount: Int, page: Int, perPage: Int)
            : this(totalCount.toLong(), page, perPage)

    constructor(totalCount: Long, pagingRequest: PagingRequest)
            : this(totalCount, pagingRequest.page, pagingRequest.perPage)
}
