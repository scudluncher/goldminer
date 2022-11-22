package com.carerra.goldminer.redeemcode.service

import com.carerra.goldminer.redeemcode.domain.entity.RedeemCode
import com.carerra.goldminer.redeemcode.domain.repository.RedeemCodeRepository
import com.carerra.goldminer.redeemcode.usecase.RedeemCodeIssueValue
import com.carerra.goldminer.redeemcode.usecase.RedeemCodeIssuing
import org.springframework.stereotype.Service

@Service
class RedeemCodeService(private val redeemCodeRepository: RedeemCodeRepository) {
    fun issueRedeemCode(value: RedeemCodeIssueValue): RedeemCode {
        return RedeemCodeIssuing(
            value,
            redeemCodeRepository
        )
            .execute()
    }
}
