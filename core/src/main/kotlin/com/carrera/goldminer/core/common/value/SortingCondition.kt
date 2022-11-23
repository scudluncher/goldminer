package com.carrera.goldminer.core.common.value

class SortingCondition(val items: List<SortingItem>) {
    constructor(field: String, descending: Boolean) : this(
        listOf(
            SortingItem(field, descending)
        )
    )

    class SortingItem(val field: String, val descending: Boolean)
}
