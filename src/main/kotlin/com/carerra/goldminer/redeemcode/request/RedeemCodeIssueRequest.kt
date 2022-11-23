package com.carerra.goldminer.redeemcode.request

import com.carerra.goldminer.common.BadRequestException
import com.carerra.goldminer.redeemcode.usecase.RedeemCodeIssueValue
import java.time.ZonedDateTime
import javax.validation.constraints.Future
import javax.validation.constraints.Positive

class RedeemCodeIssueRequest(
    @field:Positive
    private val amount: Long,
    @field:Future
    private val goldExpiredBy: ZonedDateTime,
    @field:Future
    private val codeExpiredBy: ZonedDateTime,
) {
    fun toValue(): RedeemCodeIssueValue {
        if (goldExpiredBy.isBefore(codeExpiredBy)) {
            throw BadRequestException("금 만기일은 리딤코드 만기일보다 뒤여야 합니다.")
        }
        return RedeemCodeIssueValue(
            amount,
            goldExpiredBy,
            codeExpiredBy
        )
    }
}
