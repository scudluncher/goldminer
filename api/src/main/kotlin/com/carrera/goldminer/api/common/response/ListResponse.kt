package com.carrera.goldminer.api.common.response

sealed class ListResponse<L>(
    private val items: List<L>,
    private val meta: Meta,
    private val paging: Paging,
) {
    class Ok<T>(
        items: List<T>,
        paging: Paging = Paging(items.size, 1, items.size),
    ) : ListResponse<T>(items, Meta.Ok(), paging)
}
