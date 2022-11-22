package com.carerra.goldminer.common.value

class PaginatedList<T>(val items: List<T>, val totalCount: Long) {
    constructor(items: List<T>, totalCount: Int) : this(items, totalCount.toLong())

    fun <U> translate(translator: (T) -> U): PaginatedList<U> {
        return PaginatedList(
            items.map { translator(it) },
            totalCount
        )
    }

    companion object {
        fun <T> empty(): PaginatedList<T> {
            return PaginatedList(emptyList(), 0L)
        }
    }
}
