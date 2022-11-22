package com.carerra.goldminer.goldchange.domain.entity

import java.time.ZonedDateTime

class Gold(
    val amount: ULong,
    val expiredBy: ZonedDateTime,
)

