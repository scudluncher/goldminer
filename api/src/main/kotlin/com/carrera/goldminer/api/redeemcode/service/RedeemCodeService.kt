package com.carrera.goldminer.api.redeemcode.service

import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssueValue
import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssuing
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedeemCodeService(
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    @Transactional
    fun issueRedeemCode(value: RedeemCodeIssueValue): RedeemCode {
        return RedeemCodeIssuing(
            value,
            redeemCodeRepository
        )
            .execute()
    }
}
