package com.carrera.goldminer.api.redeemcode.service

import com.carrera.goldminer.api.redeemcode.usecase.ChargingValue
import com.carrera.goldminer.api.redeemcode.usecase.ChargingGoldWithCode
import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssueValue
import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssuing
import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import org.springframework.stereotype.Service

@Service
class RedeemCodeService(
    private val redeemCodeRepository: RedeemCodeRepository,
    private val goldLedgerRepository: GoldLedgerRepository,
) {
    fun issueRedeemCode(value: RedeemCodeIssueValue): RedeemCode {
        return RedeemCodeIssuing(
            value,
            redeemCodeRepository
        )
            .execute()
    }

    fun topUpGoldWithCode(value: ChargingValue): GoldLedger {
        return ChargingGoldWithCode(
            value,
            goldLedgerRepository,
            redeemCodeRepository,
        )
            .execute()
    }
}
