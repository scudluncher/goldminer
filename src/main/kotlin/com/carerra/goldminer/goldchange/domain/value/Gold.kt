package com.carerra.goldminer.goldchange.domain.value

import java.time.ZonedDateTime

class Gold(
    val amount: Long,
    val expiredBy: ZonedDateTime,
)

class GoldAmount(
    val amount: Long,
)
